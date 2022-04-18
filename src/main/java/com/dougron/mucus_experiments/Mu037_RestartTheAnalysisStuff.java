package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_Anticipation_RRP;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation.TextPlacement;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_utils.mucus_corpus_utility.MucusCorpusUtility;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.render_name.RenderName;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu037_RestartTheAnalysisStuff
{
	
	
	String fileName = "Mu037_RestartTheAnalysisStuff";
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
			.put(MuTag.PART_1, new Integer[] {1, 0})
			.put(MuTag.PART_2, new Integer[] {2, 0})
//			.put(MuTag.PART_BASS, new Integer[] {3, 0})
			.build();
	
	private int STRENGTH_THRESHOLD = 1;
	
	
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
//						new IntAndString(-2, "Anthropollywoggle"),	// 'Anthropology' with extended chords
//						new IntAndString(-3, "BlueBossa"),
						new IntAndString(0, "BlackOrpheus"),
//						new IntAndString(-2, "Stella"),
//						new IntAndString(-1, "Confirmation"),
				};
		
		for (IntAndString corpusInfo: songNames)
		{
			Mu originalMu = MucusCorpusUtility.getMu(corpusInfo);
			originalMu.setHasLeadingDoubleBar(true);
			originalMu.addTag(MuTag.PRINT_CHORDS);
			
			ChordToneAndEmbellishmentTagger.addTags(originalMu);
			
			annotateChordTones(originalMu);
//			annotateSyncopations(originalMu);
			addBeatStrengthTags(originalMu);
			annotateBeatStrengths(originalMu);
			
			ArrayList<Integer> phraseLengths 
			= getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(originalMu);
			addMuAnnotationsOnPhraseBarStarts(corpusInfo, originalMu, phraseLengths);
			
			originalMu.addTag(MuTag.PART_MELODY);
			
			Mu totalMu = new Mu("grandaddy");
			totalMu.addMu(originalMu, 0);
			
			Mu chordToneMu = makeChordToneMu(originalMu);
			totalMu.addMu(chordToneMu, 0);
			ChordToneAndEmbellishmentTagger.addTags(chordToneMu);
			addBeatStrengthTags(chordToneMu);
			annotateBeatStrengths(chordToneMu);
			chordToneMu.addTag(MuTag.PART_1);
			
			Mu level2Mu = makeReducedMu(chordToneMu, 2);
			level2Mu.addTag(MuTag.PART_2);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(muchReducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, muchReducedMu, phraseLengths);
			makePhraseBoundsBasedOnAverageInterOnsetDistance(level2Mu);
			
			Mu level1Mu = makeReducedMu(chordToneMu, 1);
			level1Mu.addTag(MuTag.PART_3);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(reducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, reducedMu, phraseLengths);
			makePhraseBoundsBasedOnAverageInterOnsetDistance(level1Mu);
			
			Mu level0Mu = makeReducedMu(chordToneMu, 0);
			level0Mu.addTag(MuTag.PART_4);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(muchReducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, muchReducedMu, phraseLengths);
			makePhraseBoundsBasedOnAverageInterOnsetDistance(level0Mu);
			
			totalMu.addMu(level2Mu, 0);
			totalMu.addMu(level1Mu, 0);
			totalMu.addMu(level0Mu, 0);
			
			String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
					totalMu, 
					fileName + "_" + corpusInfo.str + "_" + RenderName.dateAndTime(),
					partTrackAndClipIndexMap,
					new ArrayList<MuController>(),	// placeholder for the controller list
					injector
					);
			System.out.println(corpusInfo.str + " " + x);
			
		}		
	}



	private Mu makeChordToneMu(Mu originalMu)
	{
		Mu reducedMu = new Mu("chord_tones");
		List<Mu> muList = originalMu.getMusWithNotes();
		for (Mu mu: muList)
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE))
			{
				BarsAndBeats position = mu.getGlobalPositionInBarsAndBeats();
				Mu nuMu = new Mu("ct");
				nuMu.setLengthInQuarters(mu.getLengthInQuarters());
				copyMuNotesToNuMu(mu, nuMu);
				reducedMu.addMu(nuMu, position);
			}
		}
		return reducedMu;
	}



	private void makePhraseBoundsBasedOnAverageInterOnsetDistance(Mu aMu)
	{
		double length = aMu.getLengthInQuarters();
		int count = aMu.getMusWithNotes().size();
		double ioi = length / count;
		aMu.addMuAnnotation(new MuAnnotation("averageIOI=" + ioi));
	}



	private Mu makeReducedMu(Mu originalMu, int strengthThreshold)
	{
		Mu reducedMu = new Mu("reduced");
		List<Mu> muList = originalMu.getMusWithNotes();
		for (Mu mu: muList)
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE))
			{
				int strength = (int)mu.getMuTagBundleContaining(MuTag.BEAT_STRENGTH)
						.get(0)
						.getNamedParameter(MuTagNamedParameter.BEAT_STRENGTH_VALUE);
				if (strength <= strengthThreshold)
				{
					Mu nuMu = new Mu("st");
					nuMu.setLengthInQuarters(1.0);
					BarsAndBeats position = getDeSyncopatedPosition(mu);
					copyMuNotesToNuMu(mu, nuMu);
					reducedMu.addMu(nuMu, position);
				}
			}
		}
		return reducedMu;
	}



	private void copyMuNotesToNuMu(Mu mu, Mu nuMu)
	{
		for (MuNote note: mu.getMuNotes())
		{
			MuNote nuNote = new MuNote(note.getPitch(), note.getVelocity());
			nuMu.addMuNote(nuNote);
		}
	}



	private BarsAndBeats getDeSyncopatedPosition(Mu mu)
	{
		BarsAndBeats position = mu.getGlobalPositionInBarsAndBeats();
		if (mu.hasTag(MuTag.IS_SYNCOPATION))
		{
			double globalPositionInQuarters = mu.getGlobalPositionInQuarters();
			double syncOffset = (double)mu
					.getMuTagBundleContaining(MuTag.IS_SYNCOPATION)
					.get(0)
					.getNamedParameter(MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS);
			position = mu.getGlobalPositionInBarsAndBeats(globalPositionInQuarters - syncOffset);
		}
		return position;
	}



	private void annotateBeatStrengths(Mu aMu)
	{
		for (Mu mu: aMu.getMusWithNotes())
		{
			MuTagBundle bundle = mu.getMuTagBundleContaining(MuTag.BEAT_STRENGTH).get(0);
			int strength = (int)bundle.getNamedParameter(MuTagNamedParameter.BEAT_STRENGTH_VALUE);
			mu.addMuAnnotation(new MuAnnotation("" + strength, TextPlacement.PLACEMENT_BELOW));
		}
	}



	private void addBeatStrengthTags(Mu aMu)
	{
		for (Mu mu: aMu.getMusWithNotes())
		{
//			double position = mu.getGlobalPositionInQuarters();
			int beatStrength;
			if (mu.hasTag(MuTag.IS_SYNCOPATION))
			{
				MuTagBundle bundle = mu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION).get(0);
				beatStrength = (int)bundle.getNamedParameter(MuTagNamedParameter.STRENGTH_OF_SYNCOPATED_BEAT_POSITION);
			}
			else
			{
				BarsAndBeats babPos = mu.getGlobalPositionInBarsAndBeats();
				TimeSignature ts = mu.getTimeSignature(babPos.getBarPosition());
				beatStrength = ts.getStrengthOfPositionInQuarters(babPos.getOffsetInQuarters());
			}
			
			MuTagBundle beatStrengthTag = new MuTagBundle(MuTag.BEAT_STRENGTH);
			beatStrengthTag.addNamedParameter(MuTagNamedParameter.BEAT_STRENGTH_VALUE, beatStrength);
			mu.addMuTagBundle(beatStrengthTag);
		}
	}



	private void annotateSyncopations(Mu aMu)
	{
		for (Mu mu: aMu.getAllMus())
		{
			if (mu.hasTag(MuTag.IS_SYNCOPATION))
			{
				MuTagBundle bundle = mu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION).get(0);
				double pos = (double)bundle.getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
				mu.addMuAnnotation(new MuAnnotation("sync" + pos));
			}
		}
	}



	private void annotateChordTones(Mu aMu)
	{
		for (Mu mu: aMu.getAllMus())
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE))
			{
				mu.addMuAnnotation(new MuAnnotation("ct"));
			}
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