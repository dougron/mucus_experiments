package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.part_generators.bass_part_generator.BassPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.chord_part_generator.ChordPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.drum_part_generator.DrumPartGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_001;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.render_name.RenderName;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu032_RandomMelodyMutantAttackVariations
{

	Random rnd = new Random();
	String fileName = "Mu032_RandomMelodyMutantAttackVariations";
	int variationCount = 6;
	double minimumMutation = 0.15;
	int numberOfMutantAttacks = 4;
	
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
		.put(MuTag.PART_MELODY, new Integer[] {0, 0})
		.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
		.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
		.put(MuTag.PART_BASS, new Integer[] {3, 0})
		.build();

	
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private int phraseCount = 0;
	
	
	
	
	public Mu032_RandomMelodyMutantAttackVariations()
	{
		RandomMelodyGenerator rmg = RMG_001.getInstance();
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		Parameter[] listOfMutableParameters = getListOfMutableParameters();
		ArrayList<Mu> muList = getListOfMutatedMus(rndContainer, listOfMutableParameters, rmg);
		doOutput(muList);
	}



	private ArrayList<Mu> getListOfMutatedMus
	(
			RMRandomNumberContainer rndContainer,
			Parameter[] listOfMutableParameters,
			RandomMelodyGenerator aRmg)
	{
		ArrayList<Mu> muList = new ArrayList<Mu>();
		RMRandomNumberContainer mutant = rndContainer;
		String fileName = RenderName.dateAndTime();
		for (int i = 0; i < variationCount; i++)
		{
			Mu mu = getVariation(mutant, aRmg);
			for (Mu child: mu.getMus())
			{
				child.setHasLeadingDoubleBar(true);
				if (child.hasTag(MuTag.PART_MELODY)) child.addTag(MuTag.PRINT_CHORDS);
			}
			mutant.saveAsTextDocument("RandomMelodyRandomNumberContainer_" + fileName + "_" + i + ".randcon");
			muList.add(mu);
			mutant = aRmg.getMutatedRandomNumberContainer
			(
					minimumMutation,
					numberOfMutantAttacks, 
					listOfMutableParameters, 
					rndContainer, 
					rnd
					);			
		}
		return muList;
	}



	private void doOutput(ArrayList<Mu> muList)
	{
		int clipIndex = 0;
		for (Mu mu: muList)
		{
			HashMap<MuTag, Integer[]> map = makeTrackAndClipIndexMapWithRevisedClipIndex(clipIndex);
			ContinuousIntegrator.injectMultiPartMuIntoLive(mu, map, injector);
			clipIndex++;
		}
		
		Mu muForXML = getMuForXMLOutput(muList);				
		ContinuousIntegrator.makeMultiPartMusicXML(fileName, muForXML);
		System.out.println("done.");
	}



	private HashMap<MuTag, Integer[]> makeTrackAndClipIndexMapWithRevisedClipIndex(int clipIndex)
	{
		HashMap<MuTag, Integer[]> map = new HashMap<MuTag, Integer[]>();
		for (MuTag tag: partTrackAndClipIndexMap.keySet())
		{
			Integer[] arr = partTrackAndClipIndexMap.get(tag);
			map.put(tag, new Integer[] {arr[0], clipIndex});
		}
		return map;
	}



	private Mu getMuForXMLOutput(ArrayList<Mu> muList)
	{
		Mu parent = new Mu("parentForXML");
		TimeSignature ts = muList.get(0).getTimeSignature(0);
		parent.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(ts));
		Mu previousMu = null;
		Mu currentMu;
		for (int i = 0; i < muList.size(); i++)
		{
			currentMu = muList.get(i);
			if (i == 0) 
			{
				parent.addMu(currentMu, 0);
				previousMu = currentMu;
			}
			else
			{
				parent.addMuToEndOfSibling(currentMu, 0, previousMu);
				previousMu = currentMu;
			}
		}
		parent.addTag(MuTag.PRINT_CHORDS);
		return parent;
	}



	private Parameter[] getListOfMutableParameters()
	{
		Parameter[] listOfMutableParameters = new Parameter[]
				{
						Parameter.PHRASE_LENGTH, 
						Parameter.TIME_SIGNATURE, 
						Parameter.PHRASE_START_PERCENT, 
						Parameter.PHRASE_END_PERCENT, 
						Parameter.STRUCTURE_TONE_SPACING, 
//						Parameter.TEMPO, 			// tempo not relevant to change as this wont come out in a multi variation output to Live
						Parameter.XMLKEY, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.CHORD_LIST_GENERATOR, 
						Parameter.EMBELLISHMENT_CLEARANCE, 
						Parameter.EMBELLISHMENT_REPETITION_PATTERN, 
						Parameter.MUG_LISTS
				};
		return listOfMutableParameters;
	}



	private Mu getVariation(RMRandomNumberContainer rndContainer, RandomMelodyGenerator aRmg)
	{
		Mu parent = new Mu("Mu032_phrase_" + phraseCount);
		phraseCount ++;
		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);
		
		
		RandomMelodyParameterObject po = aRmg.getParameterObject(rndContainer, rnd);
		System.out.println(po.toString());
		Mu mu1 = aRmg.getMuPhrase(po);
		mu1.setName("melody");
		mu1.addTag(MuTag.PART_MELODY);
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu1);
		parent.addMu(mu1, 0);
		parent.setStartTempo(mu1.getStartTempo());
		parent.setLengthInBars(mu1.getLengthInBars());
		

		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
		mu2.setName("chords");
		mu2.addTag(MuTag.PART_CHORDS);
		
		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
		mu3.setName("bass");
		mu3.addTag(MuTag.PART_BASS);
//		mu3.addTag(MuTag.BASS_CLEF);
		
		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
		return parent;
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu032_RandomMelodyMutantAttackVariations();
	}
}
