package main.java.com.dougron.mucus_experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_xml_utility.MuXMLUtility;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.render_name.RenderName;

/*
 * next interaction test, begun 4 April 2021. The Cycle to be attempted is as follows
 * 
 * 1.	bot generates output - random
 * 2.	user interacts:
 * 			- selects area
 * 			- cahnges parameters or returns good/bad evaluation
 * 3.	bot generates user output
 * 4.	bot does analysis, 
 * 5.	bot generates and analyzes variations. the new closest to the bot ideal is selected and output
 * 6.	goto 2.
 * 
 * 
 */



public class Mu033_InteractionVersionTwo
{
	
	Random rnd = new Random();
	String fileName = "Mu033_InteractionVersionTwo";
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
			.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
			.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
			.put(MuTag.PART_BASS, new Integer[] {3, 0})
			.build();
	int variationCount = 4;
	double minimumMutation = 0.15;
	int numberOfMutantAttacks = 4;
	
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private int phraseCount;
	
	enum Feedback {GOOD, NOT_GOOD};
	int maxThumbCount = 3;	// maximum number of times the bot will attempt a satisfying user result
	
	String path = "D:/Documents/miscForBackup/Mu033/";
	
	
	
	public Mu033_InteractionVersionTwo()
	{
		saveMu033StartTextFile();
		RandomMelodyGenerator rmg = RMG_001.getInstance();
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		Parameter[] listOfMutableParameters = getListOfMutableParameters();
		int thumbCount;
		Scanner in = new Scanner(System.in);
		StringBuilder thinkingSB = new StringBuilder();
		thinkingSB.append("generate bot output\nrandom choice");
		
		String dateTimeStamp = RenderName.dateAndTime();
		MuContainer botMu = getMu(rndContainer, "bot", rmg);
		botMu.setRndContainer(rndContainer);
		botMu.appendThinking(thinkingSB.toString());
		
		while (true)
		{		
			
			doOutput(botMu, RenderName.dateAndTime(), rmg);
			
//		//get user feedback
			List<Mu> selectionMuListForDisplay = botMu.getMu().getMu("melody").getAllMus();
			thinkingSB = new StringBuilder("get user feedback\n");
			thinkingSB.append(printSelectionListToConsole(selectionMuListForDisplay));
			Integer selectedIndex = getSelectedIndex(in, selectionMuListForDisplay);
			thinkingSB.append("\nselected index=" + selectedIndex);
			dateTimeStamp = RenderName.dateAndTime();
			if (selectedIndex == null) break;
			Mu selectedMu = selectionMuListForDisplay.get(selectedIndex);
			
			thinkingSB.append("\nfeedback options:");
			thinkingSB.append(printFeedbackOptionsToConsole());
			Feedback feedback = getFeedback(in);
			
			System.out.println(feedback);
			if (feedback == null) 
			{
				thinkingSB.append("feedback was null. exiting.");
				saveThinkingTextFile(dateTimeStamp, thinkingSB.toString());
				break;
			}
			thinkingSB.append("\n" + selectedMu.getName() + "=" + feedback);
			saveThinkingTextFile(dateTimeStamp, thinkingSB.toString());
		
		// generate and approve user output
			thumbCount = 0;
			while (true)
			{
				if (thumbCount == maxThumbCount)
				{
					String str = "maximum re render options used. Moving on to my idea.";
					System.out.println(str);
					saveThinkingTextFile(RenderName.dateAndTime(), "getting approval for rerendered output: count=" + thumbCount + "\nthumbsUp=" + str);
					break;
				}
				dateTimeStamp = RenderName.dateAndTime();
				MuContainer userMuContainer = generateUserOutput(rndContainer, listOfMutableParameters, rmg);
				doOutput(userMuContainer, dateTimeStamp, rmg);
				boolean thumbsUp = getUserThumbsUp(in);
				saveThinkingTextFile(RenderName.dateAndTime(), "getting approval for rerendered output: count=" + thumbCount + "\nthumbsUp=" + thumbsUp);
				if (thumbsUp) break;
				thumbCount++;
			}
			
		//bot generates output
			ArrayList<MuContainer> muList = getListOfMutatedMus(rndContainer, listOfMutableParameters, rmg);
			dateTimeStamp = RenderName.dateAndTime();
			rmg.saveStaticVariablesAsTextFile(path, dateTimeStamp);
			for (MuContainer muc: muList)
			{
				muc.getMu().addTag(MuTag.PRINT_CHORDS);	
				String muName = muc.getMu().getName();
				MuXMLUtility.saveMuToXMLFile(path + dateTimeStamp + "_" + muName + ".muxml", muc.getMu());
				muc.getRndContainer().saveAsTextDocument(path, dateTimeStamp + "_" + muName);
				muc.getPo().saveToStringToFile(path + dateTimeStamp + "_" + muName);				
				saveThinkingTextFile(dateTimeStamp  + "_" + muName, muc.getThinkingToString());
			}
			botMu = getBestOption(muList);
			
//			break;
		}
		System.out.println("done.");
	}



	private void saveMu033StartTextFile()
	{
		String file = path + RenderName.dateAndTime() + ".mu033.application.start";
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
			bw.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
	
	
	
	private void saveThinkingTextFile(String aDateTimeStamp, String aThinking)
	{
		String file = path + aDateTimeStamp + ".thinking";
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
			bw.write(aThinking);
			bw.close();
			System.out.println("saved file:" + file);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}



	private MuContainer getBestOption(ArrayList<MuContainer> muContainerList)
	{
		MuContainer selected = muContainerList.get(0);
		MuContainer newMuC = new MuContainer(selected.getMu(), selected.getPo());
		newMuC.setRndContainer(selected.getRndContainer());
		newMuC.appendThinking("Selected " + newMuC.getMu().getName() + " because index in list of options=0");
		return newMuC;
	}



	private boolean getUserThumbsUp(Scanner in)
	{
		while (true)
		{
			System.out.println("approve?(y/n)");
			String inputString = in.nextLine();
			if (inputString.equals("y") || inputString.equals("Y"))
			{
				return true;
			}
			else if (inputString.equals("n") || inputString.equals("N"))
			{
				return false;
			}	
		}
	}



	private MuContainer generateUserOutput
	(
			RMRandomNumberContainer rndContainer,
			Parameter[] listOfMutableParameters, 
			RandomMelodyGenerator aRmg
			)
	{
		RMRandomNumberContainer mutant = aRmg.getMutatedRandomNumberContainer
				(
						minimumMutation,
						numberOfMutantAttacks, 
						listOfMutableParameters, 
						rndContainer, 
						rnd
						);
		StringBuilder sb = new StringBuilder();
		sb.append("generating user output\n");
		sb.append("numberOfMutantAttacks=" + numberOfMutantAttacks + "\n");
		sb.append("minimumMutation=" + minimumMutation + "\n");
		sb.append("mutable parameters:\n");
		for (Parameter p: listOfMutableParameters)
		{
			sb.append(p + "\n");
		}
		MuContainer muContainer = getMu(mutant, "user", aRmg);
		sb.append(mutant.getNarrative());
		muContainer.appendThinking(sb.toString());
		muContainer.setRndContainer(mutant);
		return muContainer;
	}
	
	
	
	



	private Feedback getFeedback(Scanner in)
	{
//		Feedback feedback;
		while (true)
		{
			String inputString = in.nextLine();
			System.out.println(inputString);
			if (inputString.equals("exit")) break;
			if (isNumeric(inputString))
			{
				int x = Integer.parseInt(inputString);
				if (x < 0 || x >= Feedback.values().length)
				{
					System.out.println("enter an index in range");
				} else
				{
					return Feedback.values()[x];
				}
			} else
			{
				System.out.println("enter a number, fool");
			} 
		}
		return null;
	}



	private StringBuilder printFeedbackOptionsToConsole()
	{
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (Feedback fb: Feedback.values())
		{
			sb.append("\n" + i + ") " + fb);
			i++;
		}
		System.out.println(sb.toString());
		return sb;
	}



	private Integer getSelectedIndex(Scanner in, List<Mu> selectionMuList)
	{	
		while (true)
		{
			System.out.println("Get index to comment on:");
			String inputString = in.nextLine();
			System.out.println(inputString);
			if (inputString.equals("exit")) break;
			if (isNumeric(inputString))
			{
				int x = Integer.parseInt(inputString);
				if (x < 0 || x >= selectionMuList.size()) 
				{
					System.out.println("enter an index in range");
				}
				else
				{
					return x;
				}
			}
			else
			{
				System.out.println("enter a number, fool");
			}		
		}
		return null;
	}
	
	
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	
	
	private StringBuilder printSelectionListToConsole(List<Mu> aMuList)
	{	
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Mu mu: aMuList)
		{
			int level = mu.getLevel();
			String str = getLeaderStringBasedOnLevel(level);
			String muTagString = getMuTagString(mu);
			sb.append(str.repeat(mu.getLevel()) + index + ") " + mu.selectionListEntry() + ": " + muTagString + "\n");
			index++;
		}	
		System.out.println(sb.toString());
		return sb;
	}



	private String getMuTagString(Mu mu)
	{
		StringBuilder sb = new StringBuilder();
		for (MuTagBundle muTagBundle: mu.getMuTagBundles())
		{
			sb.append(muTagBundle.toString() + ",");
		}
		String muTagString = sb.toString();
		return muTagString;
	}



	private String getLeaderStringBasedOnLevel(int level)
	{
		String str = "  ";
		switch (level)
		{
		case 2: str = "\n=="; break;
		case 3: str = "--"; break;
		}
		return str;
	}



	private ArrayList<MuContainer> getListOfMutatedMus
	(
			RMRandomNumberContainer rndContainer,
			Parameter[] listOfMutableParameters,
			RandomMelodyGenerator aRmg
			)
	{
//		String dateTimeStamp = RenderName.dateAndTime();
		ArrayList<MuContainer> muList = new ArrayList<MuContainer>();
		RMRandomNumberContainer mutant;
//		String fileName = RenderName.dateAndTime();
		for (int i = 0; i < variationCount; i++)
		{
			mutant = aRmg.getMutatedRandomNumberContainer
					(
							minimumMutation,
							numberOfMutantAttacks, 
							listOfMutableParameters, 
							rndContainer, 
							rnd
							);	
			String variationName = "bot_variation" + getZeroPaddedNumber(i, 3);
			MuContainer muContainer = getMu(mutant, variationName, aRmg);
			muContainer.getMu().setName(variationName);
			StringBuilder sb = makeBotVariationThinking(listOfMutableParameters, variationName);
			Mu mu = muContainer.getMu();
			sb.append(mutant.getNarrative());
			setMelodyPartToPrintChordsInScore(mu);
			muContainer.setRndContainer(mutant);
			muContainer.appendThinking(sb.toString());
			muList.add(muContainer);					
		}
		return muList;
	}



	private void setMelodyPartToPrintChordsInScore(Mu mu)
	{
		for (Mu child: mu.getMus())
		{
			child.setHasLeadingDoubleBar(true);
			if (child.hasTag(MuTag.PART_MELODY)) child.addTag(MuTag.PRINT_CHORDS);
		}
	}



	private StringBuilder makeBotVariationThinking(Parameter[] listOfMutableParameters, String variationName)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("generating bot output options\n");
		sb.append("variationName=" + variationName + "\n");
		sb.append("numberOfMutantAttacks=" + numberOfMutantAttacks + "\n");
		sb.append("minimumMutation=" + minimumMutation + "\n");
		sb.append("mutable parameters:\n");
		for (Parameter p: listOfMutableParameters)
		{
			sb.append(p + "\n");
		}
		return sb;
	}
	
	
	
	public static String getZeroPaddedNumber(int i, int zeroPaddingCount)
	{
		StringBuilder sb = new StringBuilder();
		String x = "" + i;
		for (int j = 0; j < zeroPaddingCount - x.length(); j++)
		{
			sb.append('0');
		}
		sb.append(x);
		return sb.toString();
	}



	private void doOutput(MuContainer aMuContainer, String aDateTimeStamp, RandomMelodyGenerator aRmg)
	{
		aMuContainer.getMu().addTag(MuTag.PRINT_CHORDS);	
		MuXMLUtility.saveMuToXMLFile(path + aDateTimeStamp + ".muxml", aMuContainer.getMu());
		aMuContainer.getRndContainer().saveAsTextDocument(path, aDateTimeStamp);
		aMuContainer.getPo().saveToStringToFile(path + aDateTimeStamp);
		aRmg.saveStaticVariablesAsTextFile(path, aDateTimeStamp);
		saveThinkingTextFile(aDateTimeStamp, aMuContainer.getThinkingToString());
		
		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
				aMuContainer.getMu(), 
				fileName + "_" + aDateTimeStamp,
				partTrackAndClipIndexMap,
				new ArrayList<MuController>(),	// placeholder for the controller list
				injector
				);
		System.out.println(x);
	}
	
	
	
	MuContainer getMu(RMRandomNumberContainer rndContainer, String agentName, RandomMelodyGenerator aRmg)
	{
		Mu parent = new Mu("Mu033_phrase_" + agentName + "_" + phraseCount);
		phraseCount ++;
		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);				
		RandomMelodyParameterObject po = aRmg.getParameterObject(rndContainer, rnd);
		parent.setXMLKey(po.getXmlKey());
		
		Mu mu1 = getMelodyForMu(parent, po, aRmg);
		getChordsPartForMu(parent, po, mu1);		
		getBassPartForMu(parent, po, mu1);		
		getDrumPartForMu(parent);
		
		MuContainer muc = new MuContainer(parent, po);
		return muc;
	}
	
	
	
//	private MuContainer getVariation(RandomMelodyRandomNumberContainer rndContainer)
//	{
//		Mu parent = new Mu("Mu032_phrase_" + phraseCount);
//		phraseCount ++;
//		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);
//		
//		
//		RandomMelodyParameterObject po = RandomMelodyGenerator.getParameterObject(rndContainer, rnd);
////		System.out.println(po.toString());
//		Mu mu1 = RandomMelodyGenerator.getMuPhrase(po);
//		mu1.setName("melody");
//		mu1.addTag(MuTag.PART_MELODY);
//		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu1);
//		parent.addMu(mu1, 0);
//		parent.setStartTempo(mu1.getStartTempo());
//		parent.setLengthInBars(mu1.getLengthInBars());
//		
//
//		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
//		mu2.setName("chords");
//		mu2.addTag(MuTag.PART_CHORDS);
//		
//		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
//		mu3.setName("bass");
//		mu3.addTag(MuTag.PART_BASS);
////		mu3.addTag(MuTag.BASS_CLEF);
//		
//		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
//		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
//		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
//		
//		MuContainer muContainer = new MuContainer(parent, po);
//		muContainer.setRndContainer(rndContainer);
//		return muContainer;
//	}



	private Mu getMelodyForMu(Mu parent, RandomMelodyParameterObject po, RandomMelodyGenerator aRmg)
	{
		Mu mu1 = aRmg.getMuPhrase(po);
		mu1.setName("melody");
		mu1.addTag(MuTag.PART_MELODY);
		mu1.addTag(MuTag.PRINT_CHORDS);
		mu1.setHasLeadingDoubleBar(true);
		mu1.setXMLKey(po.getXmlKey());
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu1);
		addNamesAsMuAnnotationsToStructureTones(mu1);
		parent.addMu(mu1, 0);
		parent.setStartTempo(mu1.getStartTempo());
		parent.setLengthInBars(mu1.getLengthInBars());
		return mu1;
	}



	private void addNamesAsMuAnnotationsToStructureTones(Mu mu1)
	{
		for (Mu mu: mu1.getAllMus())
		{
			if (mu.hasTag(MuTag.IS_STRUCTURE_TONE))
			{
				mu.addMuAnnotation(new MuAnnotation(mu.getName(), MuAnnotation.TextPlacement.PLACEMENT_BELOW));
			}
		}
	}



	private void getDrumPartForMu(Mu parent)
	{
		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
		parent.getMu("drums").setHasLeadingDoubleBar(true);
	}



	private void getBassPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
	{
		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
		mu3.setName("bass");
		mu3.addTag(MuTag.PART_BASS);
		mu3.setHasLeadingDoubleBar(true);
		mu3.setXMLKey(po.getXmlKey());
//		mu3.addTag(MuTag.BASS_CLEF);
	}



	private void getChordsPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
	{
		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
		mu2.setName("chords");
		mu2.addTag(MuTag.PART_CHORDS);
		mu2.setHasLeadingDoubleBar(true);
		mu2.setXMLKey(po.getXmlKey());
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
						Parameter.TEMPO, 			
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
	
	

	public static void main(String[] args)
	{
		new Mu033_InteractionVersionTwo();
	}

}

// wrapper for material from the generation process

class MuContainer
{
	
	private Mu mu;
	private RandomMelodyParameterObject po;
	private RMRandomNumberContainer rndContainer;
	private StringBuilder thinking = new StringBuilder();
	
	
	public MuContainer(Mu aMu, RandomMelodyParameterObject aParameterOject)
	{
		mu = aMu;
		po = aParameterOject;
	}
	
	
	
	public RMRandomNumberContainer getRndContainer()
	{
		return rndContainer;
	}
 
	

	public void setRndContainer(RMRandomNumberContainer rndContainer)
	{
		this.rndContainer = rndContainer;
	}
 
	

	public Mu getMu()
	{
		return mu;
	}
 
	

	public RandomMelodyParameterObject getPo()
	{
		return po;
	}



	public String getThinkingToString()
	{
		return thinking.toString();
	}
	
	
	
	public StringBuilder getThinking()
	{
		return thinking;
	}



	public void appendThinking(String aString)
	{
		thinking.append(aString);
	}



}
