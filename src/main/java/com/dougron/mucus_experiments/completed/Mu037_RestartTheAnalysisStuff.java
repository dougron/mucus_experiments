package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_Anticipation_RRP;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_utils.mucus_corpus_utility.MucusCorpusUtility;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.render_name.RenderName;

public class Mu037_RestartTheAnalysisStuff
{
	
	
	String fileName = "Mu037_RestartTheAnalysisStuff";
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
//			.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
//			.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
//			.put(MuTag.PART_BASS, new Integer[] {3, 0})
			.build();
	
	
	
	
	public Mu037_RestartTheAnalysisStuff ()
	{
//		anticpationTaggingTest();
		
		IntAndString[] songNames = new IntAndString[] 
				{					
//						new IntAndString(-2, "AnthropologyTriplet"),
//						new IntAndString(-2, "AnthropologyEnclosureTest"),
//						new IntAndString(-2, "ChromaticApproachTest"),
//						new IntAndString(-3, "TripletTest"),
//						new IntAndString(0, "TripletRizingPTs"),
//						new IntAndString(0, "BigTripletTest"),
//						new IntAndString(-3, "RizingPTTest"),
//						new IntAndString(-3, "Syncopation1Test"),
//						new IntAndString(-3, "EscapeToneTest"),
//						new IntAndString(-3, "AccentedEscapeToneTest"),
//						new IntAndString(-3, "AnticipationTest"),
//						new IntAndString(-2, "Anthropology"),
//						new IntAndString(-3, "BlueBossa"),
						new IntAndString(0, "BlackOrpheus"),
//						new IntAndString(-2, "Stella"),
//						new IntAndString(-1, "Confirmation"),
				};
		
		for (IntAndString corpusInfo: songNames)
		{
			Mu originalMu = MucusCorpusUtility.getMu(corpusInfo);
			ArrayList<Integer> phraseLengths 
			= getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(originalMu);
			
			addMuAnnotationsOnPhraseBarStarts(corpusInfo, originalMu, phraseLengths);
			
			originalMu.addTag(MuTag.PART_MELODY);
			String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
					originalMu, 
					fileName + "_" + corpusInfo.str + "_" + RenderName.dateAndTime(),
					partTrackAndClipIndexMap,
					new ArrayList<MuController>(),	// placeholder for the controller list
					injector
					);
			System.out.println(corpusInfo.str + " " + x);
			
		}
		
		
	}



	public void addMuAnnotationsOnPhraseBarStarts (IntAndString corpusInfo,
			Mu originalMu, ArrayList<Integer> phraseLengths)
	{
		System.out.println(corpusInfo.str);
		int barCount = 0;
		for (Integer i: phraseLengths)
		{
			System.out.println(i);
			Mu annoMu = new Mu("phraseBar");
			annoMu.addMuAnnotation(new MuAnnotation("phrase:" + i + "bars"));
			originalMu.addMu(annoMu, barCount);
			barCount += i;
		}
	}



	public ArrayList<Integer> getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes (
			Mu originalMu)
	{
		Mu previousStartMu = null;
		ArrayList<Integer> phraseLengths = new ArrayList<Integer>();
		for (Mu mu: originalMu.getMusWithNotes())
		{
			if (mu.hasTag(MuTag.PHRASE_START))
			{
				if (previousStartMu == null)
				{
					previousStartMu = mu;
				}
				else
				{
//					double distance = mu.getGlobalPositionInFloatBars() - previousStartMu.getGlobalPositionInFloatBars();
					double previous = Math.round(previousStartMu.getGlobalPositionInFloatBars());
					double current = Math.round(mu.getGlobalPositionInFloatBars());
					phraseLengths.add((int)(current - previous));
					previousStartMu = mu;
				}
			}
		}
		double previous = Math.round(previousStartMu.getGlobalPositionInFloatBars());
		phraseLengths.add((int)(originalMu.getLengthInBars() - previous));
		return phraseLengths;
	}

	
	
	// this test was to remind myself of the approach used to tag anticipations.
	public void anticpationTaggingTest ()
	{
		Mu parent = new Mu("parent");
		parent.addTag(MuTag.PART_MELODY);
		Mu structureTone = new Mu("st");
		structureTone.addMuNote(new MuNote(60, 72));
		structureTone.setLengthInQuarters(1.0);
		structureTone.addMuGenerator(new MuG_Anticipation_RRP(new RelativeRhythmicPosition(0, 0, 0, -1)));
		parent.addMu(structureTone, 1);
		structureTone.generate();
		Mu ant = structureTone.getMus().get(0);
		ant.addMuGenerator(new MuG_Anticipation_RRP(new RelativeRhythmicPosition(0, 0, 0, -1)));
		ant.generate();
		
		ChordToneAndEmbellishmentTagger.addTags(parent);
		addStructureToneTag(parent);
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(parent);
		
//		ArrayList<MuTag> pitchList = new ArrayList<MuTag>();
		
		
		
		doOutput(parent);
	}
	
	
	// current logic is a structure tone must be on, or of syncopation of, a supertactus
	private void addStructureToneTag (Mu aMu)
	{
		for (Mu mu: aMu.getMusWithNotes())
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE) && mu.isOnOrSyncopationOfSuperTactus())
			{
				mu.addTag(MuTag.IS_STRUCTURE_TONE);
			}
		}
		
	}



	private void doOutput(Mu aMu)
	{
		aMu.addTag(MuTag.PRINT_CHORDS);		
		String x = ContinuousIntegrator.outputMuToXMLandLive(
				aMu, 
				fileName,
				0, 0,
				injector
				);
		System.out.println(x);
	}
	

	public static void main (String[] args)
	{
		new Mu037_RestartTheAnalysisStuff();

	}

}
