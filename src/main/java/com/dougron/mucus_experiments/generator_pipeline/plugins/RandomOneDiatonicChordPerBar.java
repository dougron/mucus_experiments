package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.random_number_containers.RNDValueObject;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.random_number_containers.ValueList;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordListGenerator;
import main.java.com.dougron.mucus.mu_framework.chord_list.FloatBarChordProgression;
import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.da_utils.static_chord_scale_dictionary.CSD;

public class RandomOneDiatonicChordPerBar implements GeneratorPipelinePlugIn {

	// these apply to the chord built on the degree derived from the index, so
	// in this case I, ii, iii, IV, V, vi
	String[] chordSuffixOptions = new String[] { "", "m", "m", "", "", "m" };
	public static final Logger logger = LogManager.getLogger(RandomOneDiatonicChordPerBar.class);

	
	@Override
	public PipelinePackage process(PipelinePackage aPackage) {
//		double[] rndValueArray = getRandomChordListArray(aPackage.getRnd(), )
//		aPackage.getRndContainer().put("phrase_length", getJsonRecord(rndValue));
//		logger.info(
//				"Set mu.lengthInBars=" + selection + " rndContainer('phrase_length')=" + rndValue + " for " + aPackage);
		return aPackage;
	}

	@Override
	public boolean hasRequiredResources(PipelinePackage pp) {
//		if (pp.getRnd() != null && pp.getMu().getXMLKey() != null)
//			return true;
		return false;
	}
	
	
//	private double[] getRndValueArrayBasedOnMaximumPossibleLength(PipelinePackage aPackage) {
//		JSONObject json = aPackage.getRndContainer();
//		if (!json.isNull("phrase_length")) {
//			JSONObject phraseLength = json.getJSONObject("phrase_length");
//			if (!phraseLength.isNull("options")){
//				JSONArray options = phraseLength.getJSONArray("options");
//			}
//		}
//	}
//	
//
//	// fill all chord possibilities, one chord per bar, without repeating a chord
//	public double[] getRandomChordListArray(Random rnd, int[] phraseLengthOptions) {
//		int previousIndex = -1;
//		int index = -1;
//		double[] arr = new double[getMaxValue(phraseLengthOptions)];
//		double next = 0.0;
//		for (int i = 0; i < arr.length; i++) {
//			while (index == previousIndex) {
//				next = rnd.nextDouble();
//				index = (int) (chordSuffix.length * next);
//			}
//			arr[i] = next;
//			previousIndex = index;
//		}
//		return arr;
//	}
//
//	protected int getMaxValue(int[] arr) {
//		int x = 0;
//		for (int i : arr) {
//			if (i > x)
//				x = i;
//		}
//		return x;
//	}
//
//	private ChordListGenerator makeRandomChordListGenerator(int phraseLength, int xmlKey,
//			RMRandomNumberContainer rndContainer, Random rnd) {
////		ArrayList<Double> porList = por.get(Parameter.CHORD_LIST_GENERATOR);
//		RNDValueObject rvo = rndContainer.get(Parameter.CHORD_LIST_GENERATOR);
//		if (rvo == null) {
//			rndContainer.put(Parameter.CHORD_LIST_GENERATOR, new ValueList());
//		}
//		rvo = rndContainer.get(Parameter.CHORD_LIST_GENERATOR);
//
//		double positionInFloatBars = 0.0;
//		int modKeyCentre = getModuloKeyCentre(xmlKey);
//		int[] intervals = CSD.IONIAN_MODE.getDiatonicIntervals();
//
//		int previousIndex = -1;
//		int index = -1;
//		int porIndex = 0;
//
//		ArrayList<Object> list = new ArrayList<Object>();
//
//		for (int i = 0; i < phraseLength; i++) {
//			index = getIndexOfChordRoot(rnd, rvo, previousIndex, index, porIndex);
//
//			previousIndex = index;
//			int root = modKeyCentre + intervals[index];
//			String rootPitchName = CSD.noteName(root);
//
//			list.add(positionInFloatBars);
//			list.add(rootPitchName + chordSuffix[index]);
//
//			positionInFloatBars += DEFAULT_CHORD_RHYTHM_IN_FLOATBARS;
//			porIndex++;
//		}
//		Object[] arr = list.toArray(new Object[list.size()]);
//		return new FloatBarChordProgression((double) phraseLength, arr);
//	}
//
//	public int getIndexOfChordRoot(Random rnd, RNDValueObject rvo, int previousIndex, int index, int porIndex) {
//		double rndValue = rvo.getValue(0, porIndex); // 0 - irrelevant placeholder as only used in Indexed
//														// RNDValueObjects and this is a ValueList
//		if (rndValue >= 0.0) {
//			index = (int) (rndValue * chordSuffix.length);
//		} else {
//			while (index == previousIndex) {
//				rndValue = rnd.nextDouble();
//				index = (int) (rndValue * chordSuffix.length);
//			}
//			rvo.add(0, rndValue); // 0 - placeholder again
//		}
//		return index;
//	}
//
//	private int getModuloKeyCentre(int xmlKey) {
//		int modKeyCentre = xmlKey * -5;
//		while (modKeyCentre < 0)
//			modKeyCentre += 12;
//		modKeyCentre = modKeyCentre % 12;
//		return modKeyCentre;
//	}

}
