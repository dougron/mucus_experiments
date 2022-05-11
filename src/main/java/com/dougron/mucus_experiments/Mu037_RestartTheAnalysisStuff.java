package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_Anticipation_RRP;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordListGeneratorFactory;
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
import main.java.com.dougron.mucus_experiments.artefact_to_parameter.ArtefactToParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ForceCreatePlugInsFromRepo;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.render_name.RenderName;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu037_RestartTheAnalysisStuff
{
	
	
	String fileName = "Mu037_RestartTheAnalysisStuff";
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	// this only affects injection into Live. Look to the MuXMLMaker for MuTags accomodated in musicxml output
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
			.put(MuTag.PART_1, new Integer[] {1, 0})
			.put(MuTag.PART_2, new Integer[] {2, 0})
//			.put(MuTag.PART_BASS, new Integer[] {3, 0})
			.build();
	private static final int STRENGTH_OF_0 = 0;
	private static final int STRENGTH_OF_1 = 1;
	private static final int STRENGTH_OF_2 = 2;
	private static final int STRENGTH_OF_4 = 4;		// this causes problems as a resolution for a structure tone melody
	
	private static final double LENGTH_OF_QUARTER = 1.0;
	private static final double LENGTH_OF_EIGHTH = 0.5;
	
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
//						new IntAndString(0, "BlackOrpheus_phrase1"),
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
			addBeatStrengthTags(originalMu);
			annotateSyncopations(originalMu);
			annotateBeatStrengths(originalMu);
			
			// add phrase length indicators based on phrase start info available in corpusInfo
//			ArrayList<Integer> phraseLengths 
//			= getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(originalMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, originalMu, phraseLengths);
			
			originalMu.addTag(MuTag.PART_MELODY);
			
			// top level wrapper Mu
			Mu totalMu = new Mu("grandaddy");
			totalMu.addMu(originalMu, 0);
			
			// chord tone mu (line 2 of score output)
			Mu chordToneMu = makeChordToneMu(originalMu);
			totalMu.addMu(chordToneMu, 0);
			ChordToneAndEmbellishmentTagger.addTags(chordToneMu);
			addBeatStrengthTags(chordToneMu);
			annotateSyncopations(chordToneMu);
			annotateBeatStrengths(chordToneMu);
			chordToneMu.addTag(MuTag.PART_1);
			
			// beat strength 2 reduction (line 3 of score)
			Mu level2Mu = makeReducedMu(chordToneMu, STRENGTH_OF_2, "reduce_2", LENGTH_OF_QUARTER);
			level2Mu.addTag(MuTag.PART_2);
			addWillSyncopateAnnotation(level2Mu);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(muchReducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, muchReducedMu, phraseLengths);
//			makePhraseBoundsBasedOnAverageInterOnsetDistance(level2Mu);
			
			// beat strength 1 reduction (line 4 of score)
			Mu level1Mu = makeReducedMu(chordToneMu, 1, "reduce_1", LENGTH_OF_QUARTER);
			level1Mu.addTag(MuTag.PART_3);
			addWillSyncopateAnnotation(level1Mu);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(reducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, reducedMu, phraseLengths);
//			makePhraseBoundsBasedOnAverageInterOnsetDistance(level1Mu);
			
			// beat strength 0 reduction (line 5 of score)
			Mu level0Mu = makeReducedMu(chordToneMu, STRENGTH_OF_0, "reduce_0", LENGTH_OF_QUARTER);
			level0Mu.addTag(MuTag.PART_4);
			addWillSyncopateAnnotation(level0Mu);
//			phraseLengths = getPhraseLengthsUsingRoundedFloatBarDistanceBetweenStartNotes(muchReducedMu);
//			addMuAnnotationsOnPhraseBarStarts(corpusInfo, muchReducedMu, phraseLengths);
//			makePhraseBoundsBasedOnAverageInterOnsetDistance(level0Mu);
			
//			totalMu.addMu(level2Mu, 0);
			totalMu.addMu(level1Mu, 0);
//			totalMu.addMu(level0Mu, 0);
			
			// regenerate mu 
			PooplinePackage pack = ArtefactToParameter.getPackFromMu(level1Mu);
			Poopline pipeline = new Poopline();
			pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
			pack = pipeline.process(pack);
			pack.getMu().addTag(MuTag.PART_5);
			pack.getMu().setName("reduce_1\nreproduced");
//			pack.getMu().addTag(MuTag.PRINT_CHORDS);
//			pack.getMu().setToGetLengthFromChildren();
			totalMu.addMu(pack.getMu(), 0);
			addWillSyncopateAnnotation(pack.getMu());
			
//			addMuIdAnnotationToAllMus(totalMu);
			
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



	private void addMuIdAnnotationToAllMus(Mu totalMu)
	{
		for (Mu mu: totalMu.getAllMus())
		{
			mu.addMuAnnotation(new MuAnnotation("muId:" + mu.getMuId(), 8, TextPlacement.PLACEMENT_ABOVE));
		}
	}



	private void addWillSyncopateAnnotation(Mu aMu)
	{
		for (Mu mu: aMu.getMusWithNotes())
		{
			if (mu.hasTag(MuTag.WILL_SYNCOPATE))
			{
				double syncValue = (double)mu.getMuTagBundleContaining(MuTag.WILL_SYNCOPATE)
						.get(0)
						.getNamedParameter(MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS);
				mu.addMuAnnotation(new MuAnnotation("sync" + syncValue, 8, TextPlacement.PLACEMENT_BELOW));
			}
		}
	}



	private Mu makeChordToneMu(Mu originalMu)
	{
		Mu reducedMu = new Mu("chord_tones");
		originalMu.getLengthModel().setSameLengthModel(reducedMu);
		List<Mu> muList = originalMu.getMusWithNotes();
		for (Mu mu: muList)
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE))
			{
				BarsAndBeats position = mu.getGlobalPositionInBarsAndBeats();
				Mu nuMu = new Mu("ct");
				nuMu.setLengthInQuarters(mu.getLengthInQuarters());
				copyMuNotesToNuMu(mu, nuMu);
				nuMu.addMuTagBundle(getHistoryMuTagBundle(mu));
				reducedMu.addMu(nuMu, position);
			}
		}
		return reducedMu;
	}



	private MuTagBundle getHistoryMuTagBundle(Mu mu)
	{
		MuTagBundle bundle = new MuTagBundle(MuTag.HISTORY);
		bundle.addNamedParameter(MuTagNamedParameter.ORIGINAL_MU, mu);
		return bundle;
	}



	private void makePhraseBoundsBasedOnAverageInterOnsetDistance(Mu aMu)
	{
		double length = aMu.getLengthInQuarters();
		int count = aMu.getMusWithNotes().size();
		double ioi = length / count;
		aMu.addMuAnnotation(new MuAnnotation("averageIOI=" + ioi));
	}



	private Mu makeReducedMu(Mu originalMu, int strengthThreshold, String aName, double lengthInQuarters)
	{
		Mu reducedMu = new Mu(aName);
//		reducedMu.setChordListGenerator(
//				ChordListGeneratorFactory
//				.getCopyOfGenerator(originalMu.getChordListGenerator())
//				);
		originalMu.getLengthModel().setSameLengthModel(reducedMu);
		List<Mu> muList = originalMu.getMusWithNotes();
		for (Mu mu: muList)
		{
			if (mu.hasTag(MuTag.IS_CHORD_TONE))
			{
				int strength = (int)mu.getMuTagBundleContaining(MuTag.BEAT_STRENGTH)
						.get(0)
						.getNamedParameter(MuTagNamedParameter.BEAT_STRENGTH_VALUE);
				BarsAndBeats position = getDeSyncopatedPosition(mu);
				double positionInQuarters = mu.getPositionInQuarters(position);
				if (positionInQuarters >= 0.0)
				{
					if (strength <= strengthThreshold)
					{
						Mu nuMu = new Mu("st");
						nuMu.setLengthInQuarters(lengthInQuarters);
						copyMuNotesToNuMu(mu, nuMu);
						nuMu.addTag(MuTag.IS_STRUCTURE_TONE);
						reducedMu.addMu(nuMu, position);
						addWillSyncopateTag(mu, nuMu);
						copyHistoryMuTag(mu, nuMu);
					}
				}
			}
		}
		return reducedMu;
	}



	private void copyHistoryMuTag(Mu originalMu, Mu newMu)
	{
		List<MuTagBundle> bundleList = originalMu.getMuTagBundleContaining(MuTag.HISTORY);
		for (MuTagBundle bundle: bundleList)
		{
			newMu.addMuTagBundle(bundle);
		}
		
	}



	private void addWillSyncopateTag(Mu oldMu, Mu newMu)
	{
		List<MuTagBundle> bundleList = oldMu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
		if (bundleList.size() > 0)
		{
			double offset = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS);
			MuTagBundle bundle = new MuTagBundle(MuTag.WILL_SYNCOPATE);
			bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS, offset);
			newMu.addMuTagBundle(bundle);
		}
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
				BarsAndBeats bab = mu.getGlobalPositionInBarsAndBeats(pos);
				mu.addMuAnnotation(new MuAnnotation("sync_" + bab.getOffsetInQuarters(), 8, TextPlacement.PLACEMENT_BELOW));
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
