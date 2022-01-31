package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_Anticipation;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_EscapeTone;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuGenerator;
import main.java.com.dougron.mucus.algorithms.mu_generator.enums.EscapeToneType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordListGenerator;
import main.java.com.dougron.mucus.mu_framework.chord_list.SimpleEvenChordProgression;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGenerator;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;




public class Mu028_AnticipationOfEscape
{
	
	private static final int DEFAULT_VELOCITY = 64;
	int lengthInBars = 8;
	ChordListGenerator chordListGenerator = new SimpleEvenChordProgression(new String[] {"Db", "Ab", "Bbm", "Gb"});
	int xmlKey = -5;
	TimeSignatureListGenerator timeSignatureGenerator = TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FOUR_FOUR);
	double lengthOfStructureTonesInFloatBars = 1.0;
	int[] structureToneIntervals = new int[] { 2, -1, -1, 0, 1};
	int startNote = 54;
	double structureToneLengthInQuarters = 3.0;
	private MuTag[] embellishmentOptions = new MuTag[] {MuTag.IS_ANTICIPATION, MuTag.IS_ESCAPE_TONE};
	
	String fileName = "Mu028_AnticipationOfEscape";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	
	private int aNoteCount = 1;
	private double aNoteLengthInQuarters = 1.0;
	
	
	//bot personality
	double anticipationRatio = 1.0;		// this is what the bot thinks the human likes
	double pleaseVsProvoke = 0.5;		// will do this randomly
	boolean escape = false;
	Random rnd = new Random();
	
	
	public Mu028_AnticipationOfEscape()
	{
		int cycle = 0;
		List<MuTag> emFromBot = Arrays.asList(new MuTag[]{MuTag.IS_ANTICIPATION});
		Mu muFromBot;
		ArrayList<MuTag> emFromMuFromBot;
		ArrayList<MuTag> emFromUser;
		Mu muFromUser;
		ArrayList<MuTag> emFromMuFromUser;
		
		
		while (!escape)
		{
			System.out.println("cycle=" + cycle + " =======================================");
			waitForKeyInput();
			
			muFromBot = generateOutput(emFromBot, "_cyc_" + cycle + "_bot");
			waitForKeyInput();
						
			emFromMuFromBot = getAnalysisOfMu(muFromBot, "bot");
			
			emFromUser = getEmbellishmentModelFromUser(emFromMuFromBot);
			if (escape) break;
			
			muFromUser = generateOutput(emFromUser, "_cyc_" + cycle + "_user");
			waitForKeyInput();
			
			emFromMuFromUser = getAnalysisOfMu(muFromUser, "user");
			
			emFromBot = getEmbellishmentModelFromBot(emFromMuFromUser, emFromMuFromBot);
			waitForKeyInput();	
			
			cycle++;
		}
		
		System.out.println("program exited");
	}



	private void waitForKeyInput()
	{
		Scanner in = new Scanner(System.in);
		String x = in.nextLine();
		System.out.println(x);
		in.close();
	}



	private ArrayList<MuTag> getAnalysisOfMu(Mu aMu, String aString)
	{
		System.out.println("Analysis of " + aString + " output");
		ArrayList<MuTag> list = analyzeMuForNewEmbellishmentModel(aMu);
		printEmbellishmentOptions(list);
		return list;
	}



	private ArrayList<MuTag> getEmbellishmentModelFromBot(
			ArrayList<MuTag> newModel,
			ArrayList<MuTag> previousModel)
	{
		MuTag addedTag = getTagThatWasAdded(newModel, previousModel);
		boolean please = choosePleaseOrProvoke(newModel, addedTag);
		if (theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(newModel, please, addedTag)) please = flipBoolean(please);
		printStance(please);
		MuTag chosenEmbellishment = getChosenEmbellishment(addedTag, please);
		return getNewEmbellishmentModelFromBot(newModel, chosenEmbellishment);
	}



	public static boolean flipBoolean(boolean please)
	{
		if (!please) return true;
		return false;
	}



	public static boolean theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(List<MuTag> newModel, boolean please,
			MuTag addedTag)
	{
		if (
				please
				&& addedTag == MuTag.IS_ESCAPE_TONE
				&& embellishmentTypeCount(newModel, MuTag.IS_ANTICIPATION) == 1
				&& newModel.get(0) == MuTag.IS_ANTICIPATION
				)
		{
			return true;
		}
		if (
				!please
				&& addedTag == MuTag.IS_ANTICIPATION
				&& embellishmentTypeCount(newModel, MuTag.IS_ANTICIPATION) == 1
				&& newModel.get(0) == MuTag.IS_ANTICIPATION
				)
		{
			return true;
		}
		return false;
	}



	private static int embellishmentTypeCount(List<MuTag> newModel, MuTag aMuTag)
	{
		int count = 0;
		for (MuTag muTag: newModel)
		{
			if (muTag == aMuTag) count++;
		}
		return count;
	}



	private ArrayList<MuTag> getNewEmbellishmentModelFromBot(ArrayList<MuTag> newModel, MuTag chosenEmbellishment)
	{
		int index = getIndexForEmbellishmentChange(newModel, chosenEmbellishment);
		return copyMuTagListAndChangeAtIndex(newModel, index, chosenEmbellishment);
	}



	private MuTag getChosenEmbellishment(MuTag addedTag, boolean please)
	{
		MuTag chosenEmbellishment;
		if (please)
		{
			chosenEmbellishment = addedTag;
		}
		else
		{
			chosenEmbellishment = MuTag.IS_ANTICIPATION;
			if (addedTag == MuTag.IS_ANTICIPATION) chosenEmbellishment = MuTag.IS_ESCAPE_TONE;
		}
		return chosenEmbellishment;
	}



	private boolean choosePleaseOrProvoke(ArrayList<MuTag> newModel, MuTag addedTag)
	{
		boolean please = pleaseOrProvoke();
		if (!isPossibleToPlease(addedTag, newModel))	// will provoke if not possible to added any more of the previously added tag
		{
			please = false;
		}
		return please;
	}



	private MuTag getTagThatWasAdded(ArrayList<MuTag> newModel, ArrayList<MuTag> previousModel)
	{
		for (int i = 0; i < newModel.size(); i++)
		{
			if (newModel.get(i) != previousModel.get(i % previousModel.size()))
			{
				return newModel.get(i);
			}
		}
		return null;
	}



	private boolean pleaseOrProvoke()
	{
		if (rnd.nextDouble() > 0.5) return true;
		return false;
	}



	private boolean isPossibleToPlease(MuTag addedTag, ArrayList<MuTag> aMuTagList)
	{
		for (MuTag muTag: aMuTagList)
		{
			if (muTag != addedTag) return true;
		}
		return false;
	}



	private void printStance(boolean please)
	{
		if (please)
		{
			System.out.println("I shall please.");
		}
		else
		{
			System.out.println("I shall provoke.");
		}
		
	}



	private int getIndexForEmbellishmentChange(ArrayList<MuTag> newModel, MuTag chosenEmbellishment)
	{
		int index;
		while (true)
		{
			index = getRandomIndex(newModel.size());
			if (newModel.get(index) != chosenEmbellishment) break;
		}
		return index;
	}



	private int getRandomIndex(int size)
	{
		return rnd.nextInt(size);
	}



	private ArrayList<MuTag> getEmbellishmentModelFromUser(ArrayList<MuTag> aMuTagList)
	{
//		printEmbellishmentOptions(aMuTagList);
		return getUserEmbellishmentModel(aMuTagList);
	}



	private Mu generateOutput(List<MuTag> embellishmentModel, String filenameAppendage)
	{
		System.out.println("Output for " + filenameAppendage + " generated");
		Mu mu = makeStructureTones();
		addEmbellishments(mu, embellishmentModel);
		tagStructureTonesAndEmbellishments(mu);
		doOutputs(mu, filenameAppendage);
		return mu;
	}



	private void tagStructureTonesAndEmbellishments(Mu aMu)
	{
		ChordToneAndEmbellishmentTagger.addTags(aMu);
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(aMu, new MuTag[] {MuTag.IS_STRUCTURE_TONE, MuTag.IS_ANTICIPATION, MuTag.IS_ESCAPE_TONE});
	}



	private String doOutputs(Mu mu, String filenameAppendage)
	{
		mu.addTag(MuTag.PRINT_CHORDS);
		
		String x = ContinuousIntegrator.outputMuToXMLandLive(
				mu, 
				fileName + filenameAppendage, 
				trackIndex, 
				clipIndex, 
				injector
				);
		return x;
	}



	private ArrayList<MuTag> getUserEmbellishmentModel(ArrayList<MuTag> aMuTagList)
	{
		int index = getIndexToChange(aMuTagList);
		if (index == -100) 
		{
			escape = true;
			return null;
		}
		MuTag newEmbellishment = getNewEmbellishment();
		if (newEmbellishment == null) 
		{
			escape = true;
			return null;
		}
		ArrayList<MuTag> list = copyMuTagListAndChangeAtIndex(aMuTagList, index, newEmbellishment);
		return list;
	}



	private ArrayList<MuTag> copyMuTagListAndChangeAtIndex(ArrayList<MuTag> aMuTagList, int aIndex,
			MuTag aMuTag)
	{
		ArrayList<MuTag> list = new ArrayList<MuTag>();
		for (int i = 0; i < aMuTagList.size(); i++)
		{
			if (i == aIndex)
			{
				list.add(aMuTag);
			}
			else
			{
				list.add(aMuTagList.get(i));
			}
		}
		return list;
	}



	private MuTag getNewEmbellishment()
	{
		int index = -1;
		Scanner in = new Scanner(System.in);
		while (index < 0 || index > 1)
		{
			System.out.println("Enter 0) Anticipation or 1) EscapeTone");
			
			String x = in.nextLine();
			if (x.equals("exit")) 
			{
				in.close();
				return null;
			}
			index = Integer.valueOf(x);
		}
		in.close();
		return embellishmentOptions[index];
	}



	private int getIndexToChange(ArrayList<MuTag> aMuTagList)
	{
		int index = -1;
		Scanner in = new Scanner(System.in);
		while (index <= 0 || index >= aMuTagList.size())		// cannot change index 1 as escape tone is not handled correctly in analysis where there is no preceding note
		{
			System.out.println("Enter index to change");
			
			String x = in.nextLine();
			if (x.equals("exit")) 
			{
				in.close();
				return -100;
			}
			index = Integer.valueOf(x);
		}
		in.close();
		return index;
	}



	private void printEmbellishmentOptions(ArrayList<MuTag> aMuTagList)
	{
		int index = 0;
		for (MuTag tag: aMuTagList)
		{
			System.out.println(index + ")\t" + tag);
			index++;
		}	
	}



	private ArrayList<MuTag> analyzeMuForNewEmbellishmentModel(Mu aMu)
	{
		ArrayList<MuTag> list = new ArrayList<MuTag>();
		for (Mu mu: aMu.getMusWithNotes())
		{
			if (!mu.hasTag(MuTag.IS_STRUCTURE_TONE))
			{
				if (mu.hasTag(MuTag.IS_ANTICIPATION)) list.add(MuTag.IS_ANTICIPATION);
				if (mu.hasTag(MuTag.IS_ESCAPE_TONE)) list.add(MuTag.IS_ESCAPE_TONE);
			}
		}
		return list;
	}



	private void addEmbellishments(Mu aMu, List<MuTag> eList)
	{
		int embellishmentIndex = 0;
		Mu[] structureTones = aMu.getMusWithNotes().toArray(new Mu[aMu.getMusWithNotes().size()]);
		MuGenerator mug;
		for (Mu mu: structureTones)
		{
			MuTag embellishment = eList.get(embellishmentIndex);
			
			switch(embellishment)
			{
			case IS_ANTICIPATION:
				mug = new MuG_Anticipation(aNoteCount , aNoteLengthInQuarters);
				mu.addMuGenerator(mug);
				break;
			case IS_ESCAPE_TONE:
				mug = new MuG_EscapeTone(aNoteLengthInQuarters , EscapeToneType.STEP_JUMP, MuGenerator.AccentType.UNACCENTED);
				mu.addMuGenerator(mug);
				break;
			default:
				break;
			}		
			embellishmentIndex++;
			if (embellishmentIndex >= eList.size()) embellishmentIndex = 0;
		}
		aMu.generate();
	}



	private Mu makeStructureTones()
	{
		Mu mu = new Mu("anticipation_of_escape");
		mu.setXMLKey(xmlKey);
		mu.setLengthInBars(lengthInBars);
		mu.setChordListGenerator(chordListGenerator);
		mu.setTimeSignatureGenerator(timeSignatureGenerator);
		generateStructureTones(mu, lengthOfStructureTonesInFloatBars, structureToneIntervals, startNote);
		return mu;
	}
	
	

	private void generateStructureTones(
			Mu aMu, 
			double aLengthOfStructureTonesInFloatBars, 
			int[] aStructureToneIntervals,
			int aStartNote)
	{
		double positionInFloatBars = 0.0;
		boolean isFirstNote = true;
		int note = aStartNote;
		int intervalIndex = 0;
		while (true)
		{
			if (positionInFloatBars >= aMu.getLengthInBars()) break;
			BarsAndBeats bab = aMu.getGlobalPositionInBarsAndBeatsFromGlobalPositionInFloatBars(positionInFloatBars);
			Chord chord = aMu.getChordAt(bab);
			if (isFirstNote)
			{
				isFirstNote = false;				
				note = chord.getClosestChordTone(note);
			}
			else
			{
				note = chord.getClosestChordTone(note, aStructureToneIntervals[intervalIndex]);
				intervalIndex++;
				if (intervalIndex >= aStructureToneIntervals.length) intervalIndex = 0;
			}
			Mu mu = new Mu("structure_tone");
			mu.addTag(MuTag.IS_STRUCTURE_TONE);
			mu.addMuNote(new MuNote(note, DEFAULT_VELOCITY));
			mu.setLengthInQuarters(structureToneLengthInQuarters);
			aMu.addMu(mu, bab);
			positionInFloatBars += aLengthOfStructureTonesInFloatBars;
		}
		
	}



	public static void main(String[] args)
	{
		new Mu028_AnticipationOfEscape();

	}

}
