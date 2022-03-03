package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.shade.guava.base.Preconditions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.da_utils.static_chord_scale_dictionary.CSD;

public class RandomDiatonicTriadProgression extends PlugGeneric implements PooplinePlugin {

	
	public static final Logger logger = LogManager.getLogger(RandomDiatonicTriadProgression.class);
	
	String[] options = new String[] {"", "m", "m", "", "", "m"};
	
	Type listDoubleType = new TypeToken<List<Double>>() {}.getType();
	
	public RandomDiatonicTriadProgression() {
		super(
				new Parameter[] {Parameter.CHORD_LIST_GENERATOR},
				new Parameter[] {Parameter.PHRASE_LENGTH, Parameter.XMLKEY}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		// super.process handles required parameters if they are available
		pack = super.process(pack);
		pack = setAndCheckChordProgressionRandomSeedValues(pack);
		pack = setOptionsInJson(pack);
		pack = setSelectedValuesInJson(pack);
		pack = setPlugInNameInJson(pack);
		
		pack = makeMu(pack);
		return pack;
	}


	private PooplinePackage makeMu(PooplinePackage pack) {
		Map<String, String> chordMap = getChordMap(pack);
		return null;
	}


	private Map<String, String> getChordMap(PooplinePackage pack) {
		JsonObject chordMapJson = getChordListGeneratorJsonObject(pack)
				.get("selected_options").getAsJsonObject()
				.get("position_and_chords").getAsJsonObject();
		for (String key: chordMapJson.keySet())
		{
			
		}
		return null;
	}


	private PooplinePackage setOptionsInJson(PooplinePackage pack) {
		if (pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString())) {
			JsonObject optionsJson = new JsonObject();
			optionsJson.addProperty("type",	"string_array");
			JsonArray array = new Gson().toJsonTree(options).getAsJsonArray();
			optionsJson.add("values", array);
			getChordListGeneratorJsonObject(pack).add("options", optionsJson);
		}
		return pack;
	}


	private PooplinePackage setPlugInNameInJson(PooplinePackage pack) {
		if (pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString())) {
			JsonObject pluginJson = new JsonObject();
			pluginJson.addProperty("class_name", this.getClass().getName());
			getChordListGeneratorJsonObject(pack).add("plugin", pluginJson);
		}
		return pack;
	}


	private PooplinePackage setSelectedValuesInJson(PooplinePackage pack) {
		if (pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString())) {
			JsonObject chordListGenerator = getChordListGeneratorJsonObject(pack);
			JsonObject selectedOptions = new JsonObject();
			// add type
			selectedOptions.addProperty("type", "int_array");
			// add values
			int[] indexArray = getIndexArrayForPhraseLength(pack);
			JsonArray array = new Gson().toJsonTree(indexArray).getAsJsonArray();
			selectedOptions.add("values", array);
			// add position_and_chords
			JsonObject floatBarChordPairJsonObject = getFloatBarChordPairJsonObject(indexArray, pack);
			selectedOptions.add("position_and_chords", floatBarChordPairJsonObject);
			chordListGenerator.add("selected_options", selectedOptions);
//		pack.addItemToJson(Parameter.CHORD_LIST_GENERATOR, chordListGenerator);
		}
		return pack;
	}
	


	private JsonObject getFloatBarChordPairJsonObject(int[] indexArray, PooplinePackage pack) {
		int xmlKey = getXmlKeyFromJson(pack);
		int modKeyCentre = getModuloKeyCentre(xmlKey);
		int[] intervals = CSD.IONIAN_MODE.getDiatonicIntervals();
		Map<Double, String> chordMap = new TreeMap<Double, String>();
		double pos = 0.0;
		for (int i: indexArray) {
			int root = (modKeyCentre + intervals[i]) % 12;
			String chordName = CSD.noteName(root, modKeyCentre) + options[i];
			chordMap.put(pos, chordName);
			pos += 1.0;
		}
		return new Gson().toJsonTree(chordMap).getAsJsonObject();
	}


	private int getXmlKeyFromJson(PooplinePackage pack) {
		return pack.getJson()
				.get(Parameter.XMLKEY.toString()).getAsJsonObject()
				.get("selected_option").getAsInt();
	}
	
	
	private int getModuloKeyCentre(int xmlKey)
	{
		int modKeyCentre = xmlKey * -5;
		while (modKeyCentre < 0) modKeyCentre += 12;
		modKeyCentre = modKeyCentre % 12;
		return modKeyCentre;
	}


	private JsonObject getChordListGeneratorJsonObject(PooplinePackage pack) {
		
		return pack.getJson().get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject();
	}


	private PooplinePackage setAndCheckChordProgressionRandomSeedValues(PooplinePackage pack) {
		if (checkForRequiredParameters(pack)) {
			if (jsonHasChordListGeneratorItem(pack)) {
				if (hasEnoughBars(pack)) {
					if (adheresToNoRepeatCriteria(pack)) {
						// parameters are valid and can generate mu material
					} else {
						pack = checkForSequentialRepeatsAndChangeSecondOne(pack);
						pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
					}
				} else {
					pack = addExtraBarsAndSatisfyNoRepeatCriteria(pack);
				}
				
			} else {
				pack = generateChordListGeneratorItemFromRnd(pack);
			}
			
		}
		return pack;
	}



	private PooplinePackage checkForSequentialRepeatsAndChangeSecondOne(PooplinePackage pack) {
		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		Double[] rndArr = rndList.toArray(new Double[rndList.size()]);
		int previousIndex = -1;
		int nextIndex = 0;
		for (int i = 0; i < lengthInBars; i++) {
			double rndVal = 0.0;
			int index = (int)(options.length * rndArr[i]);
			if (i < lengthInBars - 1) {
				nextIndex = (int)(options.length * rndArr[i + 1]);
			}
			boolean update = false;
			if (index == previousIndex) {
				while ((i == lengthInBars - 1 && index == previousIndex)
						||(i < lengthInBars - 1 && (index == previousIndex || index == nextIndex))
						) {
					rndVal = pack.getRnd().nextDouble();
					index = (int)(options.length * rndVal);
					update = true;
				}
			} 
			if (update) rndArr[i] = rndVal;
			previousIndex = index;
		}
		JsonObject randomSeed = getRandomSeedJsonObject(pack);
		randomSeed.add("values", new Gson().toJsonTree(rndArr).getAsJsonArray());
		return pack;
	}


	private PooplinePackage generateChordListGeneratorItemFromRnd(PooplinePackage pack) {
		double[] rndArr = getRndArrayFromPackRndFunction(pack);
		pack = addChordListGeneratorJsonItem(pack, rndArr);
		pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
		return pack;
	}


	private double[] getRndArrayFromPackRndFunction(PooplinePackage pack) {
		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
		double[] rndArr = new double[lengthInBars];
		int previousIndex = -1;
		for (int i = 0; i < lengthInBars; i++) {
			double rndVal = pack.getRnd().nextDouble();
			int index = (int)(options.length * rndVal);
			while (index == previousIndex) {
				rndVal = pack.getRnd().nextDouble();
				index = (int)(options.length * rndVal);
			}
			rndArr[i] = rndVal;
			previousIndex = index;
		}
		return rndArr;
	}



	private PooplinePackage addExtraBarsAndSatisfyNoRepeatCriteria(PooplinePackage pack) {
		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		int previousIndex = (int)(rndList.get(rndList.size() - 1) * options.length);
		for (int i = rndList.size(); i < lengthInBars; i++) {
			double rndValue = pack.getRnd().nextDouble();
			int index = (int)(rndValue * options.length);
			while (previousIndex == index) {
				rndValue = pack.getRnd().nextDouble();
				index = (int)(rndValue * options.length);
			}
			rndList.add(rndValue);
		}
		JsonObject randomSeed = getRandomSeedJsonObject(pack);
		randomSeed.add("values", new Gson().toJsonTree(rndList).getAsJsonArray());
		pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
		return pack;
	}


	private int maxLoopCount = 25;
	private PooplinePackage changeLastBarToSatisfyNoRepeatCriteria(PooplinePackage pack) {
		Preconditions.checkArgument(chordListGeneratorRandomSeedHasEnoughValues(pack), "This method must be called after chord_list_generator.random_seed has enough values for phrase length");
		int indexOfLastChord = getPhraseLengthInBarsFromJson(pack) - 1;
		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		Double[] array = rndList.toArray(new Double[rndList.size()]);
		int loopCount = 0;
		while (!satisfiesLastChordRepeatCriteria(indexOfLastChord, array)) {
			array[indexOfLastChord] = pack.getRnd().nextDouble();
			loopCount++;
			if (loopCount == maxLoopCount) {
				logger.info("could not find satidfactory value for last bar repeat criteria within " + maxLoopCount + " iterations");
				break;
			}
		}
		if (loopCount > 0 && loopCount < maxLoopCount) {
			JsonObject randomSeed = getRandomSeedJsonObject(pack);
			randomSeed.add("values", new Gson().toJsonTree(array).getAsJsonArray());
		}
		return pack;
	}

	
	private JsonObject getRandomSeedJsonObject(PooplinePackage pack) {
		return getChordListGeneratorJsonObject(pack)
				.get("random_seed").getAsJsonObject();
	}


	private boolean satisfiesLastChordRepeatCriteria(int indexOfLastChord, Double[] rndArray) {
		if (rndArray.length > 2) {
			int firstIndex = (int)(rndArray[0] * options.length);
			int lastIndex = (int)(rndArray[indexOfLastChord] * options.length);
			int secondLastIndex = (int)(rndArray[indexOfLastChord - 1] * options.length);
			return lastIndex != firstIndex && lastIndex != secondLastIndex; 
		}
		return false;
	}


	private boolean chordListGeneratorRandomSeedHasEnoughValues(PooplinePackage pack) {
		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		return rndList.size() >= lengthInBars;	
	}


	private List<Double> getChordListGeneratorRandomSeedValuesAsDoublesList(PooplinePackage pack) {
		return new Gson().fromJson(
				getChordListGeneratorRandomSeedFromJson(pack),
				listDoubleType);
	}


	private PooplinePackage addChordListGeneratorJsonItem(PooplinePackage pack, double[] rndArr) {
		JsonObject chordListGenerator = new JsonObject();
		JsonArray array = new Gson().toJsonTree(rndArr).getAsJsonArray();
		JsonObject randomSeed = new JsonObject();
		randomSeed.add("values", array);
		randomSeed.addProperty("type", "double_array");
		chordListGenerator.add("random_seed", randomSeed);
		pack.addItemToJson(Parameter.CHORD_LIST_GENERATOR.toString(), chordListGenerator);
		return pack;
	}
	

	private boolean adheresToNoRepeatCriteria(PooplinePackage pack) {
		int[] indexArray = getIndexArrayForPhraseLength(pack);
		int previous = -1;
		for (int i: indexArray) {
			if (i == previous) return false;
			previous = i;
		}
		if (previous == indexArray[0]) return false;
		return true;
	}


	// NB does not confirm NoRepeat criteria
	private int[] getIndexArrayForPhraseLength(PooplinePackage pack) {
		int phraseLengthInBars = getPhraseLengthInBarsFromJson(pack);
		int[] arr = new int[phraseLengthInBars];
		JsonArray randomSeed = getChordListGeneratorRandomSeedFromJson(pack);
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int)(randomSeed.get(i).getAsDouble() * options.length);
		}
		return arr;
	}


	private boolean hasEnoughBars(PooplinePackage pack) {
		int phraseLengthInBars = getPhraseLengthInBarsFromJson(pack);
		int lengthOfChordListGeneratorRandomSeed = getChordListGeneratorRandomSeedFromJson(pack).size();		
		return phraseLengthInBars <= lengthOfChordListGeneratorRandomSeed;
	}


	private JsonArray getChordListGeneratorRandomSeedFromJson(PooplinePackage pack) {
		return getChordListGeneratorJsonObject(pack)
				.get("random_seed").getAsJsonObject()
				.get("values").getAsJsonArray();
	}


	private int getPhraseLengthInBarsFromJson(PooplinePackage pack) {
		int phraseLengthInBars = pack.getJson()
				.get(Parameter.PHRASE_LENGTH.toString()).getAsJsonObject()
				.get("selected_option").getAsInt();
		return phraseLengthInBars;
	}


	private boolean jsonHasChordListGeneratorItem(PooplinePackage pack) {
		return pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString());
	}


	private double[] getRndArrayWithNoRepeatedChordsForPhraseLength(PooplinePackage pack) {
//		if ()
		return null;
	}


	private Map<String, String> getFloatBarChordMap(double[] rndArray, PooplinePackage pack) {
		int xmlKey = pack.getJson()
				.get(Parameter.XMLKEY.toString()).getAsJsonObject()
				.get("selected_option").getAsInt();
		int moduloKey = ((xmlKey * 7) + 120) % 12;	// 120 = ten octaves, an arbitrarily large sum to make everything positive
		int[] degrees = CSD.getDiatonicModeDegrees(CSD.AOELIAN_MODE);
		for (int i = 0; i < degrees.length; i++) {
			degrees[i] = (degrees[i] + moduloKey) % 12;
		}
//		for (int i = 0; i < )
		return null;
	}


	private int getMaxBarLengthOption(JsonObject aJsonObject) {
		int value = 0;
		JsonObject options = aJsonObject.get("options").getAsJsonObject();
		JsonArray array = options.get("values").getAsJsonArray();
		for (JsonElement element: array) {
			int i = element.getAsInt();
			if (i > value) value = i;
		}
		
		return value;
	}

}
