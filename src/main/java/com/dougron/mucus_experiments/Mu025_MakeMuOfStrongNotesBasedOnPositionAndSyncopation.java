package main.java.com.dougron.mucus_experiments;



import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation.TextPlacement;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_utils.mucus_corpus_utility.MucusCorpusUtility;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.render_name.RenderName;

public class Mu025_MakeMuOfStrongNotesBasedOnPositionAndSyncopation
{
	
	
	private static final double DEFAULT_NOTE_LENGTH_FOR_DESYNCOPATED_STRUCTURE_TONES = 1.0;
	String fileName = "Mu025_";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
		.put(MuTag.PART_MELODY, new Integer[] {0, 0})
		.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
		.put(MuTag.PART_BASS, new Integer[] {3, 0})
		.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
		.build();
	
	
	
	public Mu025_MakeMuOfStrongNotesBasedOnPositionAndSyncopation()
	{
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
						new IntAndString(-3, "AccentedEscapeToneTest"),
//						new IntAndString(-3, "AnticipationTest"),
//						new IntAndString(-2, "Anthropology"),
//						new IntAndString(-3, "BlueBossa"),
//						new IntAndString(0, "BlackOrpheus"),
//						new IntAndString(-2, "Stella"),
//						new IntAndString(-1, "Confirmation"),
				};
		
		for (IntAndString corpusInfo: songNames)
		{
			Mu originalMu = MucusCorpusUtility.getMu(corpusInfo);
			Mu strongNotes = makeStrongNoteMuOfCorpusItem(originalMu);
			Mu desyncopatedStrongNotes = makeDesyncopatedStrongNoteMu(strongNotes);
			
			originalMu.addTag(MuTag.PART_MELODY);
			strongNotes.addTag(MuTag.PART_CHORDS);
			desyncopatedStrongNotes.addTag(MuTag.PART_DRUMS);
			
			desyncopatedStrongNotes.addTag(MuTag.PRINT_CHORDS);
			strongNotes.addTag(MuTag.PRINT_CHORDS);
			
			Mu multiMu = new Mu("Mu025");
			multiMu.addMu(originalMu, 0);
			multiMu.addMu(strongNotes, 0);
			multiMu.addMu(desyncopatedStrongNotes, 0);
			multiMu.addTag(MuTag.HAS_MULTIPART_CHILDREN);
						
//			String x = ContinuousIntegrator.outputMuToXMLandLive(
//															strongNotes, 
//															fileName + corpusInfo.str, 
//															trackIndex, 
//															clipIndex, 
//															injector
//															);
			String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
					multiMu, 
					fileName + corpusInfo.str + "_" + RenderName.dateAndTime(),
					partTrackAndClipIndexMap,
					injector
					);
			System.out.println(corpusInfo.str + " " + x);
		}
	}



	private Mu makeDesyncopatedStrongNoteMu (Mu aMu)
	{
		Mu newMu = aMu.getDeepCopy();
		for (Mu mu: newMu.getMusWithNotes())
		{
			if (mu.hasTag(MuTag.IS_SYNCOPATION))
			{
				ArrayList<MuTagBundle> mtbList = mu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
				double positionInQuarters 
				= (double)mtbList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
////				mu.setPositionModel(new BeginningOfParentInQuarters(positionInQuarters, mu));
				mu.movePosition(positionInQuarters - mu.getGlobalPositionInQuarters());			
			}
			mu.setLengthInQuarters(DEFAULT_NOTE_LENGTH_FOR_DESYNCOPATED_STRUCTURE_TONES);
		}
		return newMu;
	}



	private Mu makeStrongNoteMuOfCorpusItem(Mu aMu)
	{
		
//			mu.addTag(MuTag.PRINT_CHORDS);
		ChordToneAndEmbellishmentTagger.addTags(aMu);
		ChordToneAndEmbellishmentTagger
		.makeTagsIntoXMLAnnotations(
				aMu, 
									new MuTag[] {MuTag.PHRASE_START, MuTag.PHRASE_END}
									);
		addPositionStrengthAnnotations(aMu);
		
		Mu strongNotes = makeStrongNotes(aMu);
		return strongNotes;
	}

	
	
	private void addPositionStrengthAnnotations(Mu aMu)
	{
//		List<Mu> muList = aMu.getMus();
		for (Mu mu: aMu.getMusWithNotes())
		{
			int strength = mu.getStrengthOfPositionInBarConsideringSyncopation();
			mu.addMuAnnotation(new MuAnnotation("str=" + strength, TextPlacement.PLACEMENT_BELOW));
		}	
	}



	private Mu makeStrongNotes(Mu aMu)
	{
		Mu mu = new Mu(aMu.getName() + "_strongNotes");
		mu.setLengthInBars(aMu.getLengthInBars());
		mu.setXMLKey(aMu.getKeySignatureMap().getKey());
		for (Mu moo: aMu.getMusWithNotes())
		{
			if (moo.getStrengthOfPositionInBarConsideringSyncopation() < 2)
			{
				if (moo.isTupletPrintContainer())
				{
					Mu newMu = moo.getDeepCopy();
					for (MuNote muno: newMu.getMus().get(0).getMuNotes())
					{
						newMu.addMuNote(muno);
					}
					newMu.getMus().clear();
					newMu.setIsTupletPrintContainer(false);
				}
				else
				{
					moo.addDeepCopyAtSamePositionToParentMu(mu);
				}		
			}
		}
		mu.setChordListGenerator(aMu.getChordListGenerator());
		return mu;
	}



	public static void main(String[] args)
	{
		new Mu025_MakeMuOfStrongNotesBasedOnPositionAndSyncopation();
	}

}
