package main.java.com.dougron.mucus_experiments.completed;

import java.util.HashMap;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation.TextPlacement;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_utils.mucus_corpus_utility.MucusCorpusUtility;
import main.java.da_utils.combo_variables.IntAndString;


/**
 * This automatically identifies very short notes. It is obviously not without limitations.
 * 
 * The threshold value is the significant value. When the avaerage distance of all notes and 
 * their note ends drops below this value, that is considered the subdivision for normal notes.
 * 
 * resolution is the amount a whole note is divided by.
 * 
 * max resolution is 2 - i.e. 8th notes. Got some silly results from April in Paris when 1 was a possibility
 * 
 * It ignores triplets and things in tuplet containers
 * 
 * Currently am putting this on hold while I prepare something for supervisors. 
 * 
 * @author dougr
 *
 */

public class Mu027_Confirmation16ths
{
	
	String fileName = "Mu027_";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private double threshold = 0.2; 
	int[] resolution = new int[] {2, 4, 8, 16};
	

	public Mu027_Confirmation16ths()
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
//					new IntAndString(0, "LaFiesta_MiniTuplet"),
						
						new IntAndString(-2, "Anthropology"),
						new IntAndString(-3, "BlueBossa"),
						new IntAndString(0, "BlackOrpheus"),
						new IntAndString(-2, "Stella"),
						new IntAndString(-1, "Confirmation"),
						new IntAndString(0, "AprilInParis"),
						new IntAndString(0, "Ceora"),
						new IntAndString(0, "LaFisesta"),
				};
		
		HashMap<String, Integer> results = new HashMap<String, Integer>();
		for (IntAndString corpusInfo: songNames)
		{
			Mu mu = makeMu(corpusInfo);
			results.put(corpusInfo.str, getResolution(mu));	// resolution is the amount a quarter note must be divided 
			tagNotesBelowTheThreshold(results.get(corpusInfo.str), mu);
						
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
		printResults(results);
	}
	
	
	
	
	private void tagNotesBelowTheThreshold(Integer resolution, Mu aMu)
	{
		for (Mu mu: aMu.getMus())
		{
			if (mu.hasMuNotes())
			{
				double position = mu.getGlobalPositionInQuarters();
				double endPosition = mu.getGlobalEndPositionInQuarters();
				position *= resolution;
				endPosition *= resolution;
				if (position != Math.round(position)
						|| endPosition != Math.round(endPosition))
				{
					mu.addTag(MuTag.IS_SHORT_NOTE);
					mu.addMuAnnotation(new MuAnnotation("x", TextPlacement.PLACEMENT_BELOW));
				}
			}
		}	
	}




	private void printResults(HashMap<String, Integer> map)
	{
		for (String str: map.keySet())
		{
			System.out.println(str + ":\t" + map.get(str));
		}
		
	}




	private int getResolution(Mu aMu)
	{
		
		int firstToDropBelowThreshold = 0;
		boolean hasFoundResolution = false;
		for (int res: resolution)
		{
			int count = 0;
			double totalDistance = 0;	
			for (Mu mu: aMu.getMus())
			{
				double position = mu.getGlobalPositionInQuarters();
				double endPosition = mu.getGlobalEndPositionInQuarters();
				position *= res;
				endPosition *= res;
				double roundPosition = Math.round(position);
				double roundEndPosition = Math.round(endPosition);
				totalDistance += Math.abs(position - roundPosition);
				totalDistance += Math.abs(endPosition - roundEndPosition);
				count++;
			}
			double score = totalDistance / count;
			if (!hasFoundResolution && score < threshold )
			{
				hasFoundResolution = true;
				firstToDropBelowThreshold = res;			
			}
			System.out.println(res + ": " + score);
		}
		return firstToDropBelowThreshold;
	}




	private Mu makeMu(IntAndString corpusInfo)
	{
		Mu corpusMu = getTaggedCorpusItemAsMu(corpusInfo);
		System.out.println(corpusInfo.str);
		
		return corpusMu;
	}
	
	
	
	private Mu getTaggedCorpusItemAsMu(IntAndString corpusInfo)
	{
		Mu corpusMu = MucusCorpusUtility.getMu(corpusInfo);		
		ChordToneAndEmbellishmentTagger.addTags(corpusMu);
		return corpusMu;
	}




	public static void main(String[] args)
	{
		new Mu027_Confirmation16ths();

	}

}
