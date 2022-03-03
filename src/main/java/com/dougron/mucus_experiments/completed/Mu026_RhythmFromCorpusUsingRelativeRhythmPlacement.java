package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_utils.mucus_corpus_utility.MucusCorpusUtility;
import main.java.da_utils.combo_variables.IntAndString;


/**
 * This test will take the notes from a corpus item or items as defined in the songNames array and 
 * convert them into rhythm on a single note and a duration of 1/8th note
 * 
 * Current errors are incorrect representation of 16th notes (no resolution for sub sub tactus)
 * 
 * 
 * @author dougr
 *
 */
public class Mu026_RhythmFromCorpusUsingRelativeRhythmPlacement
{
	
	String fileName = "Mu026_";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	

	public Mu026_RhythmFromCorpusUsingRelativeRhythmPlacement()
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
//						new IntAndString(-3, "AccentedEscapeToneTest"),
//						new IntAndString(-3, "AnticipationTest"),
//					new IntAndString(-2, "Anthropology_line1"),
//					new IntAndString(-3, "BlueBossa_line1"),
//						new IntAndString(-2, "Anthropology"),
//						new IntAndString(-3, "BlueBossa"),
//						new IntAndString(0, "BlackOrpheus"),
//						new IntAndString(-2, "Stella"),
						new IntAndString(-1, "Confirmation"),
				};
		
		for (IntAndString corpusInfo: songNames)
		{
//			Mu mu = makeMu(corpusInfo);
			Mu mu = makeMuUsingRelativeRhythmicPlacementFrom1stNote(corpusInfo);
//			Mu mu = getTaggedCorpusItemAsMu(corpusInfo);
						
			String x = ContinuousIntegrator.outputMuToXMLandLive(
															mu, 
															fileName + corpusInfo.str, 
															trackIndex, 
															clipIndex, 
															injector
															);
			System.out.println(corpusInfo.str + " " + x);
			trackIndex++;
		}
	}
	
	
	
	
	private Mu makeMuUsingRelativeRhythmicPlacementFrom1stNote(IntAndString corpusInfo)
	{
		Mu corpusMu = getTaggedCorpusItemAsMu(corpusInfo);
		
		Mu parent = corpusMu.getDeepCopy();
		parent.getMus().clear();
		
		Mu firstNote = corpusMu.getMus().get(0);
	
//		ArrayList<RelativeRhythmicPosition> rrpList = getRelativeRhythmicPositionListUsingNextMu(firstNote);
		ArrayList<RelativeRhythmicPosition> rrpList = getRelativeRhythmicPositionListUsingMus(corpusMu);
		
		Mu firstNoteCopy = firstNote.addDeepCopyAtSamePositionToParentMu(parent);
		int pitch = 60;
		int velocity = 64;
		int noteCount = 0;
		Mu startMu = firstNoteCopy;
		Mu tempMu;
		for (RelativeRhythmicPosition rrp: rrpList)
		{
			tempMu = parent.addMuAtBarsAndBeatsPosition(startMu, rrp);
			tempMu.addMuNote(new MuNote(pitch, velocity));
			tempMu.addMuAnnotation(new MuAnnotation("" + noteCount, MuAnnotation.TextPlacement.PLACEMENT_ABOVE));
			startMu = tempMu;
			noteCount++;
		}
		
		return parent;
	}




//	private ArrayList<RelativeRhythmicPosition> getRelativeRhythmicPositionListUsingNextMu(Mu firstNote)
//	{
//		Mu startMu = firstNote;
//		Mu nextMu;
//		ArrayList<RelativeRhythmicPosition> rrpList = new ArrayList<RelativeRhythmicPosition>();
//		while (true)
//		{
//			if (startMu.getNextMu() == null)
//			{
//				break;
//			}
//			else
//			{
//				nextMu = startMu.getNextMu();
//				rrpList.add(startMu.getRelativeRhythmicPosition(nextMu));
//				startMu = nextMu;
//			}
//		}
//		return rrpList;
//	}
	
	
	
	// this one will represent triplets as a single note
	private ArrayList<RelativeRhythmicPosition> getRelativeRhythmicPositionListUsingMus(Mu aParentMu)
	{
		ArrayList<RelativeRhythmicPosition> rrpList = new ArrayList<RelativeRhythmicPosition>();
		for (int i = 0; i < aParentMu.getMus().size() - 1; i++)
		{
			rrpList.add(aParentMu.getMus().get(i).getRelativeRhythmicPosition(aParentMu.getMus().get(i + 1)));
		}
		return rrpList;
	}




//	private Mu makeMuOfOnlyStartAndEndNotes(IntAndString corpusInfo)
//	{
//		Mu corpusMu = getTaggedCorpusItemAsMu(corpusInfo);
//		corpusMu.sortMus(Mu.globalPositionInQuartersComparator);
//		makePhraseDelimiterTagsIntoMuAnnotations(corpusMu);
//		
//		ArrayList<Mu> muList = makeListOfPhraseStarts(corpusMu);
//		HashMap<Mu, ArrayList<RelativeRhythmicPosition>> relativePositionMap = makeRelativePositionMap(muList);
//		Mu parentMu = corpusMu.getDeepCopy();
//		parentMu.getMus().clear();
//		for (Mu keyMu: relativePositionMap.keySet())
//		{
//			Mu addedMu = keyMu.addDeepCopyAtSamePositionToParentMu(parentMu);
//			addedMu.addTag(MuTag.PHRASE_START);
//			ArrayList<RelativeRhythmicPosition> rrpList = relativePositionMap.get(keyMu);
//			
//		}
//		
//		return parentMu;
//	}




//	private void makePhraseDelimiterTagsIntoMuAnnotations(Mu corpusMu)
//	{
//		ChordToneAndEmbellishmentTagger
//		.makeTagsIntoXMLAnnotations(
//									corpusMu, 
//									new MuTag[] {MuTag.PHRASE_START, MuTag.PHRASE_END}
//									);
//	}




	private Mu getTaggedCorpusItemAsMu(IntAndString corpusInfo)
	{
		Mu corpusMu = MucusCorpusUtility.getMu(corpusInfo);
		
		ChordToneAndEmbellishmentTagger.addTags(corpusMu);
		return corpusMu;
	}



//	private HashMap<Mu, ArrayList<RelativeRhythmicPosition>> makeRelativePositionMap(ArrayList<Mu> muListOfPhraseStarts)
//	{
//		HashMap<Mu, ArrayList<RelativeRhythmicPosition>> map = new HashMap<Mu, ArrayList<RelativeRhythmicPosition>>();
//		for (Mu mu: muListOfPhraseStarts)
//		{
//			ArrayList<RelativeRhythmicPosition> list = new ArrayList<RelativeRhythmicPosition>();
//			Mu nextMu;;
//			Mu thisMu = mu;
//			while (true)
//			{
//				nextMu = thisMu.getNextMu();
//				if (nextMu == null || nextMu.hasTag(MuTag.PHRASE_START)) break;
//				list.add(thisMu.getRelativeRhythmicPosition(nextMu));
//				thisMu = nextMu;
//			}
//			map.put(mu, list);
//		}
//		return map;
//	}



//	private ArrayList<Mu> makeListOfPhraseStarts(Mu aMu)
//	{
//		ArrayList<Mu> list = new ArrayList<Mu>();
//		for (Mu mu: aMu.getMus())
//		{
//			if (mu.hasTag(MuTag.PHRASE_START)) list.add(mu);
//		}		
//		return list;
//	}



	public static void main(String[] args)
	{
		new Mu026_RhythmFromCorpusUsingRelativeRhythmPlacement();
	}
}
