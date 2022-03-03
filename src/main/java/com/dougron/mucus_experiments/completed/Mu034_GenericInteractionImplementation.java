package main.java.com.dougron.mucus_experiments.completed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.bot_personality.BotPersonality;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionData;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionDataFactory;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_002;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.ParameterSpecificFeedback;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.FeedbackObject;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.random_number_containers.RNDValueObject;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.render_name.RenderName;

public class Mu034_GenericInteractionImplementation
{
	
	Random rnd = new Random();
	String fileName = "Mu034_GenericInteractionImplementation";
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
		.put(MuTag.PART_MELODY, new Integer[] {0, 0})
		.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
		.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
		.put(MuTag.PART_BASS, new Integer[] {3, 0})
		.build();

	int variationCount = 4;
	double minimumMutation = 0.15;
	int numberOfMutantAttacks = 4;
	int minumumMutantAttacks = 2;
	int maximumMutantAttacks = 10;
	
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	Scanner in = new Scanner(System.in);
//	private int phraseCount;
	
	enum Feedback {GOOD, NOT_GOOD};
	int maxThumbCount = 3;	// maximum number of times the bot will attempt a satisfying user result
	
	String path = "D:/Documents/miscForBackup/Mu034/";
	private int botOptionCount = 10;
	
	ArrayList<MucusInteractionData> approvedList = new ArrayList<MucusInteractionData>();
	ArrayList<MucusInteractionData> rejectedList = new ArrayList<MucusInteractionData>();
	
	double[] safeRiskyOptions = new double[] {1.0};
	double[] pleaseProvokeOptions = new double[] {0.0};
	
	RandomMelodyGenerator rmg = RMG_002.getInstance();
	int userOptionCount = 10;
	
	
	//==========================================
	// Main loop
	//==========================================
		
	public Mu034_GenericInteractionImplementation()
	{
		saveMu034StartTextFile();
		MucusInteractionData mid = generateInitialBotOutput();
		while (true)
		{
			outputBotArtefact(mid);
			mid = getUserFeedback(mid);
			if (mid.getEscape()) break;
			mid = renderAndOutputAndApproveUserArtefact(mid);
			mid = generateBotArtefact(mid);
		}
		System.out.println("done.");
	}
	
	
	
	//================================================
	// privates
	//================================================
	
	private void saveMu034StartTextFile()
	{
		String file = path + RenderName.dateAndTime() + ".mu034.application.start";
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
	
	
	
	private MucusInteractionData generateBotArtefact(MucusInteractionData aUserMid)
	{
		MucusInteractionData mid = new MucusInteractionData("generate bot artefact");
		mid.setDateTimeStamp(RenderName.dateAndTime());
		mid.setRandomMelodyGenerator(rmg);
		mid = getMIDWithBotPersonality(mid, rnd);
		mid = makeParameterScopeForVariations(mid, aUserMid);
		ArrayList<MucusInteractionData> midList = getListOfOptions(mid, aUserMid);
		mid = selectBestOptionFromList(midList, mid);
		return mid;
	}



	private MucusInteractionData getMIDWithBotPersonality(MucusInteractionData aMid, Random aRnd)
	{
		double safeRisky = safeRiskyOptions[aRnd.nextInt(safeRiskyOptions.length)];
		double pleaseProvoke = pleaseProvokeOptions[aRnd.nextInt(pleaseProvokeOptions.length)];	
		aMid.setBotPersonality(new BotPersonality(safeRisky, pleaseProvoke));
		aMid.appendThinking("\nbot personality:\n" + aMid.getBotPersonality().toString());
		return aMid;
	}



	private MucusInteractionData selectBestOptionFromList
	(
			ArrayList<MucusInteractionData> midList, 
			MucusInteractionData aMid
			)
	{
		return aMid.getBotPersonality().getBestOption(midList, approvedList, rejectedList, aMid.getRandomMelodyGenerator());
	}



	private ArrayList<MucusInteractionData> getListOfOptions(MucusInteractionData aBotMid, MucusInteractionData aUserMid)
	{
		ArrayList<MucusInteractionData> midList = new ArrayList<MucusInteractionData>();
		int mutantAttackCount 
		= (int)
		(minumumMutantAttacks + 
				(
						(maximumMutantAttacks - minumumMutantAttacks) * aBotMid.getBotPersonality().getSafeRisky()
				)
		);
		for (int i = 0; i < botOptionCount ; i++)
		{
			System.out.println("generating bot option=" + i);
			RMRandomNumberContainer rndContainer = rmg.getMutatedRandomNumberContainer
					(
							minimumMutation, 
							mutantAttackCount, 
							aBotMid.getParameterScope(), 
							aUserMid.getRndContainer(),
							rnd);
			MucusInteractionData mid = MucusInteractionDataFactory.getMid(rndContainer, "bot_option_" + getZeroPaddedNumber(i, 2), rmg, rnd);
			mid.appendThinking("bot variation=" + i);
			mid.setBotPersonality(aBotMid.getBotPersonality());
			mid.setDateTimeStamp(aBotMid.getDateTimeStamp());
			midList.add(mid);
		}
		return midList;
	}



	private MucusInteractionData makeParameterScopeForVariations(MucusInteractionData aBotMid, MucusInteractionData aUserMid)
	{
		aBotMid.setParameterScope(Parameter.values());
		return aBotMid;
	}



	private MucusInteractionData renderAndOutputAndApproveUserArtefact(MucusInteractionData aMid)
	{
		int thumbCount = 0;
		
		ArrayList<MucusInteractionData> midList = new ArrayList<MucusInteractionData>();
		while (true)
		{
			String dateTimeStamp = RenderName.dateAndTime();
			MucusInteractionData mid = MucusInteractionDataFactory.generateUserOption(aMid, rnd, userOptionCount, minimumMutation);
			mid.setDateTimeStamp(dateTimeStamp);
			midList.add(mid);
			doOutput(mid, dateTimeStamp);
			System.out.println(mid.getPo());
			if (approveUserOption())
			{
				saveApprovedUserArtefact(mid, dateTimeStamp);
				approvedList.add(mid);
				return mid;
			}
			else
			{
				mid.appendThinking("\nrejected by user");
				saveAllMIDItemsToFile(mid, path, dateTimeStamp);
				rejectedList.add(mid);
				thumbCount++;
				if (thumbCount == maxThumbCount)
				{
					return selectPreferredUserOption(midList, RenderName.dateAndTime());
				}			
			}
		}
	}



	private void saveApprovedUserArtefact(MucusInteractionData aMid, String aDateTimeStamp)
	{
		aMid.appendThinking("\napproved by user");
		saveAllMIDItemsToFile(aMid, path, aDateTimeStamp);
	}



	private MucusInteractionData selectPreferredUserOption(ArrayList<MucusInteractionData> aMidList, String aDateTimeStamp)
	{
		
		MucusInteractionData mid = selectPreferredOptionByIndex(aMidList);
		saveAndOutputPreferredUnapprovedUserArtefact(mid, aDateTimeStamp);
		return mid;
	}



	private void saveAndOutputPreferredUnapprovedUserArtefact(MucusInteractionData aMid, String aDateTimeStamp)
	{
		doOutputAndSaveToFile(aMid, path, aDateTimeStamp);		
	}



	private MucusInteractionData selectPreferredOptionByIndex(ArrayList<MucusInteractionData> aMidList)
	{
		while (true)
		{
			printUnapprovedUserOptionsListToConsole(aMidList);
			String input = in.nextLine();
			if (isNumeric(input))
			{
				int selected = Integer.parseInt(input);
				if (selected >= 0 && selected < aMidList.size()) 
				{
					MucusInteractionData mid = aMidList.get(selected);
					mid.appendThinking("\nselected from unapproved list");
					return mid;
				}
			} 
		}
	}



	private void printUnapprovedUserOptionsListToConsole(ArrayList<MucusInteractionData> aMidList)
	{
		System.out.println("select item from unapproved list:");
		int index = 0;
		for (MucusInteractionData mid : aMidList)
		{
			System.out.println(index + ")\t" + mid.getDateTimeStamp());
			index++;
		}		
	}



	private boolean approveUserOption()
	{
		while (true)
		{
			System.out.print("Approve new version (y/n) ");
			String inputString = in.nextLine();
			if (inputString.equals("y") || inputString.equals("Y"))
			{
				return true;
			} else if (inputString.equals("n") || inputString.equals("N"))
			{
				return false;
			} 
			System.out.println("enter y or n");
		}		
	}



//	private MucusInteractionData generateUserOption(MucusInteractionData aMid, int aUserOptionCount)
//	{
//		
//		ArrayList<RndContainerScoreWrapper> rcswList = makeUserRndContainerList(aMid, aUserOptionCount );
//		Collections.sort(rcswList, RndContainerScoreWrapper.scoreComparator);
//		StringBuilder sb = makeGenerateUserOptionsThinking(rcswList);
//		System.out.println(sb.toString());
//		
//		MucusInteractionData mid = getMu(rcswList.get(0).rndContainer, "user");
//		mid.setRndContainer(rcswList.get(0).rndContainer);
//		mid.appendThinking(sb.toString());
//		return mid;
//	}



//	private StringBuilder makeGenerateUserOptionsThinking(ArrayList<RndContainerScoreWrapper> rcswList)
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append("list of scores:\n");
//		for (RndContainerScoreWrapper sw: rcswList)
//		{
//			sb.append("score=" + sw.score + "\n" + sw.narrative);
//		}
//		return sb;
//	}



//	private ArrayList<RndContainerScoreWrapper> makeUserRndContainerList
//	(
//			MucusInteractionData aMid, 
//			int aCount
//			)
//	{
//		ArrayList<RndContainerScoreWrapper> list = new ArrayList<RndContainerScoreWrapper>();
//		Parameter[] parameterList = getSetOfMutableParameters(aMid);
//		for (int i = 0; i < aCount; i++)
//		{
//			System.out.println("makeUserContainerList i=" + i);
//			RMRandomNumberContainer rndContainer = rmg.getMutatedRandomNumberContainer
//																(
//																		minimumMutation, 
//																		parameterList.length, 
//																		parameterList, 
//																		aMid.getRndContainer(),
//																		rnd
//																		);
//			IntAndString scoreAndNarrative = getScoreAndNarrativeOfNewRndContainer(aMid, rndContainer);
//			list.add(new RndContainerScoreWrapper(rndContainer, scoreAndNarrative.i, scoreAndNarrative.str));
//		}
//		return list;
//	}



//	private IntAndString getScoreAndNarrativeOfNewRndContainer(MucusInteractionData aMid, RMRandomNumberContainer rndContainer)
//	{
//		int score = 0;
//		StringBuilder sb = new StringBuilder();		
//		for (FeedbackObject fo: aMid.getParameterSpecificFeedbackList())
//		{
//			RNDValueObject rvo = rndContainer.get(fo.getRelatedParameter());
//			String str = "RNDValueObject is null";
//			if (rvo != null) str = rvo.getTextFileString();
//			sb.append(fo.getDescription() + "\t" + str);
//			boolean isTrue = fo.isTrue(aMid.getRndContainer(), rndContainer, aMid.getRandomMelodyGenerator());
//			if (isTrue) 
//			{
//				score++;
//				sb.append("\tpasses\n");
//			}
//			else
//			{
//				sb.append("\tfails\n");
//			}
//		}
//		return new IntAndString(score, sb.toString());
//	}
//
//
//
//	private Parameter[] getSetOfMutableParameters(MucusInteractionData aMid)
//	{
//		HashSet<Parameter> parameterList = new HashSet<Parameter>();
//		for (FeedbackObject fo: aMid.getParameterSpecificFeedbackList())
//		{
//			parameterList.add(fo.getRelatedParameter());
//		}
//		return parameterList.toArray(new Parameter[parameterList.size()]);
//	}



	private MucusInteractionData getUserFeedback(MucusInteractionData aMid)
	{ 
		String dateTimeStamp = RenderName.dateAndTime();
		MucusInteractionData mid = displayFeedbackFocusAreas(aMid);
		mid = selectFeedbackFocusArea(mid);
		saveGetUserFeedackToFile(mid, dateTimeStamp);
		return mid;
	}



	private void saveGetUserFeedackToFile(MucusInteractionData aMid, String aDateTimeStamp)
	{
		StringBuilder thinkingSB = new StringBuilder();
		thinkingSB.append("User feedback options captured");
		for (FeedbackObject fo: aMid.getParameterSpecificFeedbackList())
		{
			thinkingSB.append("\n" + fo.getDescription());
		}
		MucusInteractionData.saveThinkingTextFile(thinkingSB.toString(), path, aDateTimeStamp);
		
	}



	private MucusInteractionData selectFeedbackFocusArea(MucusInteractionData aMid)
	{
		aMid.clearParameterSpecificFeedbackList();
		while (true)
		{
			System.out.print("Select index of item to change: ");
			String inputString = in.nextLine();
			if (inputString.equals("exit"))
			{
				aMid.appendThinking("\nexit selected");
				aMid.setEscape(true);
				return aMid;
			}
			if (isNumeric(inputString))
			{
				int x = Integer.parseInt(inputString);
				if (x < 0 || x >= aMid.getFeedbackOptionList().size()) 
				{
					System.out.println("enter an index in range");
				}
				else
				{
					FeedbackObject fo = aMid.getFeedbackOptionList().get(x);
					aMid.addParameterSpecificFeedbackItem(fo);
					return aMid;
				}
			}
			else
			{
				System.out.println("enter a number, fool");
			}	
		}
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



	private MucusInteractionData displayFeedbackFocusAreas(MucusInteractionData aMid)
	{
		Map<Parameter, FeedbackObject[]> map = ParameterSpecificFeedback.getParameterSpecificFeedackMap();
		ArrayList<FeedbackObject> foList = new ArrayList<FeedbackObject>();
		int index = 0;
		for (Parameter parameter: map.keySet())
		{
			RNDValueObject rvo = aMid.getRndContainer().get(parameter);
			StringBuilder sb = new StringBuilder("\n"+ parameter + "\t");
			if (rvo == null)
			{
				sb.append("null");
			}
			else
			{
				sb.append(aMid.getPo().getDescription(parameter) + "\n  ");
				sb.append(rvo.getTextFileString());
			}
			System.out.println(sb.toString());
			
			for (FeedbackObject fo: map.get(parameter))
			{
				foList.add(fo);
				System.out.println("\t" + index + ")\t" + fo.getDescription());
				index++;
			}
		}
		aMid.setFeedbackOptionList(foList);
		return aMid;
	}



	private MucusInteractionData outputBotArtefact(MucusInteractionData aMid)
	{
		doOutputAndSaveToFile(aMid, path, aMid.getDateTimeStamp());
		return null;
	}
	
	
	
	private void doOutputAndSaveToFile(MucusInteractionData aMid, String aDirectoryPath, String aDateTimeStamp)
	{
		String x = doOutput(aMid, aDateTimeStamp);
		saveAllMIDItemsToFile(aMid, aDirectoryPath, aDateTimeStamp);
		System.out.println(x);
	}



	public void saveAllMIDItemsToFile(MucusInteractionData aMid, String aDirectoryPath, String aDateTimeStamp)
	{
		MucusInteractionData.saveToFile(aMid, aDirectoryPath, aDateTimeStamp);
	}



	public String doOutput(MucusInteractionData aMid, String aDateTimeStamp)
	{
		aMid.getMu().addTag(MuTag.PRINT_CHORDS);		
		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
				aMid.getMu(), 
				fileName + "_" + aDateTimeStamp,
				partTrackAndClipIndexMap,
				new ArrayList<MuController>(),	// placeholder for the controller list
				injector
				);
		return x;
	}
	


	private MucusInteractionData generateInitialBotOutput()
	{
		String dateTimeStamp = RenderName.dateAndTime();
		MucusInteractionData mid = getInitialBotMID(dateTimeStamp);
		mid.appendThinking(getThinkingForInitalBotMID(dateTimeStamp));
		return mid;
	}



	public String getThinkingForInitalBotMID(String dateTimeStamp)
	{
		StringBuilder thinkingSB = new StringBuilder();
		thinkingSB.append("start=" + dateTimeStamp + " end=" + RenderName.dateAndTime());
		thinkingSB.append("\ngenerate bot output\nrandom choice");
		return thinkingSB.toString();
	}



	public MucusInteractionData getInitialBotMID(String dateTimeStamp)
	{
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		MucusInteractionData mid = MucusInteractionDataFactory.getMid(rndContainer, "bot", rmg, rnd);
		mid.setRndContainer(rndContainer);
		mid.setDateTimeStamp(dateTimeStamp);
		mid.setRandomMelodyGenerator(rmg);
		return mid;
	}



//	private MucusInteractionData getMu(RMRandomNumberContainer rndContainer, String agentName)
//	{
//		Mu parent = new Mu("Mu034_phrase_" + agentName + "_" + phraseCount);
//		phraseCount ++;
//		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);				
//		RandomMelodyParameterObject po = rmg.getParameterObject(rndContainer, rnd);
//		parent.setXMLKey(po.getXmlKey());
//		
//		Mu mu1 = getMelodyForMu(parent, po);
//		getChordsPartForMu(parent, po, mu1);		
//		getBassPartForMu(parent, po, mu1);		
//		getDrumPartForMu(parent);
//		
//		MucusInteractionData mid = new MucusInteractionData(parent, po);
//		mid.setRndContainer(rndContainer);
//		mid.setRandomMelodyGenerator(rmg);
//		return mid;
//	}
	
	
	
//	private Mu getMelodyForMu(Mu parent, RandomMelodyParameterObject po)
//	{
//		Mu mu1 = rmg.getMuPhrase(po);
//		mu1.setName("melody");
//		mu1.addTag(MuTag.PART_MELODY);
//		mu1.addTag(MuTag.PRINT_CHORDS);
//		mu1.setHasLeadingDoubleBar(true);
//		mu1.setXMLKey(po.getXmlKey());
//		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu1);
//		addNamesAsMuAnnotationsToStructureTones(mu1);
//		parent.addMu(mu1, 0);
//		parent.setStartTempo(mu1.getStartTempo());
//		parent.setLengthInBars(mu1.getLengthInBars());
//		return mu1;
//	}



//	private void addNamesAsMuAnnotationsToStructureTones(Mu mu1)
//	{
//		for (Mu mu: mu1.getAllMus())
//		{
//			if (mu.hasTag(MuTag.IS_STRUCTURE_TONE))
//			{
//				mu.addMuAnnotation(new MuAnnotation(mu.getName(), MuAnnotation.TextPlacement.PLACEMENT_BELOW));
//			}
//		}
//	}



//	private void getDrumPartForMu(Mu parent)
//	{
//		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
//		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
//		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
//		parent.getMu("drums").setHasLeadingDoubleBar(true);
//	}



//	private void getBassPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
//	{
//		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
//		mu3.setName("bass");
//		mu3.addTag(MuTag.PART_BASS);
//		mu3.setHasLeadingDoubleBar(true);
//		mu3.setXMLKey(po.getXmlKey());
////		mu3.addTag(MuTag.BASS_CLEF);
//	}
//
//
//
//	private void getChordsPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
//	{
//		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
//		mu2.setName("chords");
//		mu2.addTag(MuTag.PART_CHORDS);
//		mu2.setHasLeadingDoubleBar(true);
//		mu2.setXMLKey(po.getXmlKey());
//	}
	
	
	
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
	
	
	



	public static void main(String[] args)
	{
		new Mu034_GenericInteractionImplementation();
	}

}
