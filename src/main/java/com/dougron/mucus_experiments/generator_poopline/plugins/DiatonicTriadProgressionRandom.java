package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.shade.guava.base.Preconditions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordListGeneratorFactory;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DiatonicTriadRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;
import main.java.da_utils.static_chord_scale_dictionary.CSD;

public class DiatonicTriadProgressionRandom extends PlugGeneric implements PooplinePlugin {

	
	public static final Logger logger = LogManager.getLogger(DiatonicTriadProgressionRandom.class);
	
	String[] options = new String[] {"", "m", "m", "", "", "m"};
	
//	Type listDoubleType = new TypeToken<List<Double>>() {}.getType();
	@Getter private DiatonicTriadRepo diatonicTriadRepo;
	@Getter private PhraseLengthRepo phraseLengthRepo;
	@Getter private XmlKeyRepo xmlKeyRepo;
	
	public DiatonicTriadProgressionRandom() {
		super(
				Parameter.CHORD_LIST_GENERATOR,
				new Parameter[] {Parameter.PHRASE_LENGTH, Parameter.XMLKEY}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
	@Override
	PooplinePackage updateMu(PooplinePackage pack)
	{
		pack = makeMu(pack);
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		if (hasRequiredRepos())
		{
			diatonicTriadRepo = makeDiatonicTriadRepo(pack);
			pack.getRepo().put(Parameter.CHORD_LIST_GENERATOR, diatonicTriadRepo);
		}
		else
		{
			logger.debug("Repo not created as not all required repos were present.");
		}
		return pack;
	}
	


	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		diatonicTriadRepo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		if (hasEnoughBars(diatonicTriadRepo, phraseLengthRepo)) {
			if (adheresToNoRepeatCriteria(diatonicTriadRepo, phraseLengthRepo)) {
				// parameters are valid and can generate mu material
			} else {
				double[] rndArr = checkForSequentialRepeatsAndChangeSecondOne(diatonicTriadRepo, phraseLengthRepo.getSelectedValue(), pack.getRnd());
				diatonicTriadRepo.setRndValue(rndArr);
				rndArr = changeLastBarToSatisfyNoRepeatCriteria(diatonicTriadRepo, phraseLengthRepo.getSelectedValue(), pack.getRnd());
				diatonicTriadRepo.setRndValue(rndArr);
				int[] indexArr = getIndexArrayForPhraseLength(phraseLengthRepo.getSelectedValue(), rndArr);
				Map<Double, String> map = getFloatBarChordMap(xmlKeyRepo.getSelectedValue(), indexArr);
				diatonicTriadRepo.setSelectedValues(indexArr);
				diatonicTriadRepo.setFloatBarChordMap(map);
			}
		} else {
			extendDiatonicTriadRepoLength(pack);
		}
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		getRequiredParameterRepos(pack);
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) {
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		// super.process handles required parameters if they are available
//		pack = super.process(pack);
//		if (pack.getRepo().containsKey(Parameter.PHRASE_LENGTH) 
//				&& pack.getRepo().containsKey(Parameter.XMLKEY))
//		{
//			getRequiredParameterRepos(pack);
//			
//			if (pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)
//					&& pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR).getClassName().equals(getClass().getName())) 
//			{
//				diatonicTriadRepo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
//				if (hasEnoughBars(diatonicTriadRepo, phraseLengthRepo)) {
//					if (adheresToNoRepeatCriteria(diatonicTriadRepo, phraseLengthRepo)) {
//						// parameters are valid and can generate mu material
//					} else {
//						double[] rndArr = checkForSequentialRepeatsAndChangeSecondOne(diatonicTriadRepo, phraseLengthRepo.getSelectedValue(), pack.getRnd());
//						diatonicTriadRepo.setRndValue(rndArr);
//						rndArr = changeLastBarToSatisfyNoRepeatCriteria(diatonicTriadRepo, phraseLengthRepo.getSelectedValue(), pack.getRnd());
//						diatonicTriadRepo.setRndValue(rndArr);
//						int[] indexArr = getIndexArrayForPhraseLength(phraseLengthRepo.getSelectedValue(), rndArr);
//						Map<Double, String> map = getFloatBarChordMap(xmlKeyRepo.getSelectedValue(), indexArr);
//						diatonicTriadRepo.setSelectedValues(indexArr);
//						diatonicTriadRepo.setFloatBarChordMap(map);
//					}
//				} else {
//					extendDiatonicTriadRepoLength(pack);
//				}
//			} else {
//				diatonicTriadRepo = makeDiatonicTriadRepo(pack);
//				pack.getRepo().put(Parameter.CHORD_LIST_GENERATOR, diatonicTriadRepo);
//			}			
//			pack = makeMu(pack);
//			
//		}
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}
	
	
	

	
	private boolean hasRequiredRepos()
	{
		if (phraseLengthRepo == null || xmlKeyRepo == null) return false;
		return true;
	}



	private void extendDiatonicTriadRepoLength(PooplinePackage pack) {
		double[] rndArr = addExtraBarsAndSatisfyNoRepeatCriteria(
				pack.getRnd(), 
				diatonicTriadRepo.getRndValue(), 
				phraseLengthRepo.getSelectedValue()
				);
		int[] indexArr = getIndexArrayForPhraseLength(phraseLengthRepo.getSelectedValue(), rndArr);
		Map<Double, String> map = getFloatBarChordMap(xmlKeyRepo.getSelectedValue(), indexArr);
		diatonicTriadRepo.setRndValue(rndArr);
		diatonicTriadRepo.setSelectedValues(indexArr);
		diatonicTriadRepo.setFloatBarChordMap(map);
	}


	


	private DiatonicTriadRepo makeDiatonicTriadRepo(PooplinePackage pack) {
		double[] rndArr = getRndArrayWithNoRepeatsFromPackRndFunction(pack, phraseLengthRepo);
		int[] indexArr = getIndexArrayForPhraseLength(phraseLengthRepo.getSelectedValue(), rndArr);
		Map<Double, String> map = getFloatBarChordMap(xmlKeyRepo.getSelectedValue(), indexArr);
		return DiatonicTriadRepo.builder()
				.rndValue(rndArr)
				.selectedValues(indexArr)
				.options(options)
				.floatBarChordMap(map)
				.className(getClass().getName())
				.build();
	}


	private void getRequiredParameterRepos(PooplinePackage pack) {
		if (pack.getRepo().containsKey(Parameter.PHRASE_LENGTH))
		{
			phraseLengthRepo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		}
		if (pack.getRepo().containsKey(Parameter.XMLKEY))
		{
			xmlKeyRepo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		}
	}
	


	private PooplinePackage makeMu(PooplinePackage pack) {
		if (hasRequiredRepos())
		{
			Mu mu = pack.getMu();
			mu.setLengthInBars(phraseLengthRepo.getSelectedValue());
			mu.setXMLKey(xmlKeyRepo.getSelectedValue());
			mu.setChordListGenerator(new ChordListGeneratorFactory().getGenerator(diatonicTriadRepo.getMapAsObjectList((double)phraseLengthRepo.getSelectedValue())));
		}
		else
		{
			logger.debug("Mu not generated ");
		}
		return pack;
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
//			int[] indexArray = getIndexArrayForPhraseLength(pack);
//			JsonArray array = new Gson().toJsonTree(indexArray).getAsJsonArray();
//			selectedOptions.add("values", array);
			// add position_and_chords
//			JsonObject floatBarChordPairJsonObject = getFloatBarChordPairJsonObject(indexArray, pack);
//			selectedOptions.add("position_and_chords", floatBarChordPairJsonObject);
			chordListGenerator.add("selected_options", selectedOptions);
//		pack.addItemToJson(Parameter.CHORD_LIST_GENERATOR, chordListGenerator);
		}
		return pack;
	}
	


	private Map<Double, String> getFloatBarChordMap(int xmlKey, int[] indexArray) {
//		int xmlKey = getXmlKeyFromJson(pack);
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
		return chordMap;
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


//	private PooplinePackage setAndCheckChordProgressionRandomSeedValues(PooplinePackage pack) {
//		
//		
//		if (checkForRequiredParameters(pack)) {
//			if (jsonHasChordListGeneratorItem(pack)) {
//				if (hasEnoughBars(pack)) {
//					if (adheresToNoRepeatCriteria(diatonicTriadRepo, phraseLengthRepo)) {
//						// parameters are valid and can generate mu material
//					} else {
//						pack = checkForSequentialRepeatsAndChangeSecondOne(pack);
//						pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
//					}
//				} else {
//					pack = addExtraBarsAndSatisfyNoRepeatCriteria(pack);
//				}
//				
//			} else {
//				pack = generateChordListGeneratorItemFromRnd(pack);
//			}
//			
//		}
//		return pack;
//	}



	private double[] checkForSequentialRepeatsAndChangeSecondOne(DiatonicTriadRepo dtRepo, int phraseLengthInBars, Random rnd) {
//		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
//		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		double[] rndArr = dtRepo.getRndValue();
		int previousIndex = -1;
		int nextIndex = 0;
		for (int i = 0; i < phraseLengthInBars; i++) {
			double rndVal = 0.0;
			int index = (int)(options.length * rndArr[i]);
			if (i < phraseLengthInBars - 1) {
				nextIndex = (int)(options.length * rndArr[i + 1]);
			}
			boolean update = false;
			if (index == previousIndex) {
				while ((i == phraseLengthInBars - 1 && index == previousIndex)
						||(i < phraseLengthInBars - 1 && (index == previousIndex || index == nextIndex))
						) {
					rndVal = rnd.nextDouble();
					index = (int)(options.length * rndVal);
					update = true;
				}
			} 
			if (update) rndArr[i] = rndVal;
			previousIndex = index;
		}
//		JsonObject randomSeed = getRandomSeedJsonObject(pack);
//		randomSeed.add("values", new Gson().toJsonTree(rndArr).getAsJsonArray());
		return rndArr;
	}


	private PooplinePackage generateChordListGeneratorItemFromRnd(PooplinePackage pack) {
//		double[] rndArr = getRndArrayFromPackRndFunction(pack);
//		pack = addChordListGeneratorJsonItem(pack, rndArr);
//		pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
		return pack;
	}


	private double[] getRndArrayWithNoRepeatsFromPackRndFunction(PooplinePackage pack, PhraseLengthRepo phRepo) {
		int lengthInBars = phRepo.getSelectedValue();
		double[] rndArr = new double[lengthInBars];
		int previousIndex = -1;
		int firstIndex = -1;
		for (int i = 0; i < lengthInBars; i++) {
			double rndVal = pack.getRnd().nextDouble();
			int index = (int)(options.length * rndVal);
			if (i == 0) firstIndex = index;
			while ((i < lengthInBars - 1 && index == previousIndex)
					|| (i == lengthInBars - 1 && (index == previousIndex || index == firstIndex)))
					 {
				rndVal = pack.getRnd().nextDouble();
				index = (int)(options.length * rndVal);
			}
			rndArr[i] = rndVal;
			previousIndex = index;
		}
		return rndArr;
	}

	
	private double[] addExtraBarsAndSatisfyNoRepeatCriteria(
			@NonNull Random rnd, 
			double[] shortRndValues, 
			int lengthInBars) {
		Preconditions.checkArgument(lengthInBars > shortRndValues.length, "lengthInBars should be longer than length of shortRndValues array");
		double[] rndArr = new double[lengthInBars];
		int previousIndex = -1;
		int firstIndex = (int)(shortRndValues[0] * options.length);
		double rndVal;
		for (int i = 0; i < lengthInBars; i++) {
			if (i < shortRndValues.length) {
				rndVal = shortRndValues[i];
			} else {
				rndVal = rnd.nextDouble();
			}
			int index = (int)(options.length * rndVal);
			if (i == 0) firstIndex = index;
			while ((i < lengthInBars - 1 && index == previousIndex)
					|| (i == lengthInBars - 1 && (index == previousIndex || index == firstIndex)))
					 {
				rndVal = rnd.nextDouble();
				index = (int)(options.length * rndVal);
			}
			rndArr[i] = rndVal;
			previousIndex = index;
		}
		return rndArr;
	}


	private PooplinePackage addExtraBarsAndSatisfyNoRepeatCriteria(PooplinePackage pack) {
//		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
//		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
//		int previousIndex = (int)(rndList.get(rndList.size() - 1) * options.length);
//		for (int i = rndList.size(); i < lengthInBars; i++) {
//			double rndValue = pack.getRnd().nextDouble();
//			int index = (int)(rndValue * options.length);
//			while (previousIndex == index) {
//				rndValue = pack.getRnd().nextDouble();
//				index = (int)(rndValue * options.length);
//			}
//			rndList.add(rndValue);
//		}
//		JsonObject randomSeed = getRandomSeedJsonObject(pack);
//		randomSeed.add("values", new Gson().toJsonTree(rndList).getAsJsonArray());
//		pack = changeLastBarToSatisfyNoRepeatCriteria(pack);
		return pack;
	}


	private int maxLoopCount = 25;
	private double[] changeLastBarToSatisfyNoRepeatCriteria(DiatonicTriadRepo dtRepo, int phraseLengthInBars, Random rnd) {
		Preconditions.checkArgument(dtRepo.getRndValue().length >= phraseLengthInBars, "This method must be called after chord_list_generator.random_seed has enough values for phrase length");
		int indexOfLastChord = phraseLengthInBars - 1;
//		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
		double[] array = dtRepo.getRndValue();
		int loopCount = 0;
		while (!satisfiesLastChordRepeatCriteria(indexOfLastChord, array)) {
			if (loopCount == maxLoopCount) {
				logger.info("could not find satidfactory value for last bar repeat criteria within " + maxLoopCount + " iterations");
				break;
			}
			array[indexOfLastChord] = rnd.nextDouble();
			loopCount++;
		}
//		if (loopCount > 0 && loopCount < maxLoopCount) {
//			JsonObject randomSeed = getRandomSeedJsonObject(pack);
//			randomSeed.add("values", new Gson().toJsonTree(array).getAsJsonArray());
//		}
		return array;
	}

	
	private JsonObject getRandomSeedJsonObject(PooplinePackage pack) {
		return getChordListGeneratorJsonObject(pack)
				.get("random_seed").getAsJsonObject();
	}


	private boolean satisfiesLastChordRepeatCriteria(int indexOfLastChord, double[] rndArray) {
		if (rndArray.length > 2) {
			int firstIndex = (int)(rndArray[0] * options.length);
			int lastIndex = (int)(rndArray[indexOfLastChord] * options.length);
			int secondLastIndex = (int)(rndArray[indexOfLastChord - 1] * options.length);
			return lastIndex != firstIndex && lastIndex != secondLastIndex; 
		}
		return false;
	}


//	private boolean chordListGeneratorRandomSeedHasEnoughValues(PooplinePackage pack) {
//		int lengthInBars = getPhraseLengthInBarsFromJson(pack);
//		List<Double> rndList = getChordListGeneratorRandomSeedValuesAsDoublesList(pack);
//		return rndList.size() >= lengthInBars;	
//	}


//	private List<Double> getChordListGeneratorRandomSeedValuesAsDoublesList(PooplinePackage pack) {
//		return new Gson().fromJson(
//				getChordListGeneratorRandomSeedFromJson(pack),
//				listDoubleType);
//	}


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
	

	private boolean adheresToNoRepeatCriteria(DiatonicTriadRepo dtRepo, PhraseLengthRepo plRepo) {
		int[] indexArray = getIndexArrayForPhraseLength(plRepo.getSelectedValue(), dtRepo.getRndValue());
		int previous = indexArray[indexArray.length - 1];
		for (int i: indexArray) {
			if (i == previous) return false;
			previous = i;
		}
//		if (previous == indexArray[0]) return false;
		return true;
	}


	// NB does not confirm NoRepeat criteria
	private int[] getIndexArrayForPhraseLength(int phraseLengthInBars, double[] rndArr) {
//		int phraseLengthInBars = phRepo.getSelectedValue();
		int[] arr = new int[phraseLengthInBars];
//		JsonArray randomSeed = getChordListGeneratorRandomSeedFromJson(pack);
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int)(rndArr[i] * options.length);
		}
		return arr;
	}
	
	

	private boolean hasEnoughBars(PooplinePackage pack) {
		int phraseLengthInBars = getPhraseLengthInBarsFromJson(pack);
		int lengthOfChordListGeneratorRandomSeed = getChordListGeneratorRandomSeedFromJson(pack).size();		
		return phraseLengthInBars >= lengthOfChordListGeneratorRandomSeed;
	}
	
	
	private boolean hasEnoughBars(DiatonicTriadRepo dtRepo, PhraseLengthRepo phRepo) {
//		int phraseLengthInBars = getPhraseLengthInBarsFromJson(pack);
//		int lengthOfChordListGeneratorRandomSeed = getChordListGeneratorRandomSeedFromJson(pack).size();		
		return phRepo.getSelectedValue() <= dtRepo.getRndValue().length;
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
