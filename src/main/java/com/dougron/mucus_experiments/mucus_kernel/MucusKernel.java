package main.java.com.dougron.mucus_experiments.mucus_kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import main.java.com.dougron.mucus.algorithms.bot_personality.BotPersonality;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionData;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionDataFactory;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_002;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.da_utils.render_name.RenderName;


/*
 * Utility class for Mu interactions circa Mu038-Mu040.
 */
public class MucusKernel
{
	
	
	RandomMelodyGenerator rmg = RMG_002.getInstance();
	Random rnd = new Random();
	
	int variationCount = 4;
	double minimumMutation = 0.15;
	int numberOfMutantAttacks = 4;
	int minumumMutantAttacks = 2;
	int maximumMutantAttacks = 10;
	private int botOptionCount = 10;
	int userOptionCount = 10;
	
	ArrayList<MucusInteractionData> approvedList = new ArrayList<MucusInteractionData>();
	ArrayList<MucusInteractionData> rejectedList = new ArrayList<MucusInteractionData>();
	
	double[] safeRiskyOptions = new double[] {1.0};
	double[] pleaseProvokeOptions = new double[] {0.0};
	
	public static final Logger logger = LogManager.getLogger(MucusKernel.class);
	
	
	
	public MucusInteractionData getInitialBotVariation (String aTimeStamp)
	{
		MucusInteractionData mid = getInitialBotMID(aTimeStamp);
		mid.appendJSONThinking("initial_bot_output", getThinkingForInitialBotMID(aTimeStamp));
		return mid;
	}
	
	

	public MucusInteractionData getBotVariation (MucusInteractionData aUserMid, MucusOutputManager aOutputManager)
	{
		MucusInteractionData mid = new MucusInteractionData("generate bot artefact");
		mid.setDateTimeStamp(RenderName.dateAndTime());
		mid.setRandomMelodyGenerator(rmg);
		mid = getMIDWithBotPersonality(mid, rnd);
		mid = makeParameterScopeForVariations(mid);
		mid = addApprovedAndRejectedListInfo(mid);
		ArrayList<MucusInteractionData> midList = getListOfOptions(mid, aUserMid);
		for (MucusInteractionData listMid: midList)
		{
			aOutputManager.saveBotVariationOptionItem(listMid);
		}
		mid = selectBestOptionFromList(midList, mid);
		return mid;
	}
	
	
	
	private MucusInteractionData addApprovedAndRejectedListInfo (
			MucusInteractionData aMid)
	{
		List<String> list = approvedList.stream()
				.map(item -> item.getDateTimeStamp())
				.collect(Collectors.toList());
		aMid.appendJSONThinking("approved_list", list);
		list = rejectedList.stream()
				.map(item -> item.getDateTimeStamp())
				.collect(Collectors.toList());
		aMid.appendJSONThinking("rejected_list", list);
		return aMid;
	}



	// aMid requires an rndContainer to be mutated, a ParameterSpecificFeedbackList and a RandomMelodyGenerator 
	public MucusInteractionData getUserVariation (String aTimeStamp, MucusInteractionData aMid)
	{
		MucusInteractionData mid = MucusInteractionDataFactory.generateUserOption(aMid, rnd, userOptionCount, minimumMutation);
		mid.setDateTimeStamp(aTimeStamp);		
		return mid;
	}
	
	
	
	public boolean addToApprovedList (MucusInteractionData e)
	{
		return approvedList.add(e);
	}
	
	
	
	public boolean addToRejectedList (MucusInteractionData e)
	{
		return rejectedList.add(e);
	}

	
// privates --------------------------------------------------------------------------------------------------
	
	
	
	
	
//	private ArrayList<RndContainerScoreWrapper> makeUserRndContainerList
//	(
//			List<FeedbackObject> aFeedbackObjectList, 
//			Random rnd,
//			int aUserOptionCount,
//			double aMinimumMutation,
//			RandomMelodyGenerator rmg
//			)
//	{
//		ArrayList<RndContainerScoreWrapper> list = new ArrayList<RndContainerScoreWrapper>();
//		Parameter[] parameterList = getSetOfMutableParameters(aFeedbackObjectList);
//		int mutantAttackCount = 0;
//		for (Parameter parameter: parameterList)
//		{
//			mutantAttackCount += aMid.getRndContainer().get(parameter).getValueCount();
//		}
//		for (int i = 0; i < aUserOptionCount; i++)
//		{
//			System.out.println("makeUserContainerList i=" + i);
//			RMRandomNumberContainer rndContainer 
//			= aMid.getRandomMelodyGenerator().getMutatedRandomNumberContainer
//			(
//					aMinimumMutation, 
//					parameterList.length, 
//					parameterList, 
//					aMid.getRndContainer(),
//					rnd
//					);
//			IntAndString scoreAndNarrative = getScoreAndNarrativeOfNewRndContainer(aMid, rndContainer);
//			list.add(new RndContainerScoreWrapper(rndContainer, scoreAndNarrative.i, scoreAndNarrative.str));
//		}
//		return list;
//	}
	
	
	
//	private Parameter[] getSetOfMutableParameters(MucusInteractionData aMid)
//	{
//		HashSet<Parameter> parameterList = new HashSet<Parameter>();
//		for (FeedbackObject fo: aMid.getParameterSpecificFeedbackList())
//		{
//			parameterList.add(fo.getRelatedParameter());
//		}
//		return parameterList.toArray(new Parameter[parameterList.size()]);
//	}
	
	
	
	private JSONObject getThinkingForInitialBotMID(String aTimeStamp)
	{
		JSONObject json = new JSONObject();
		json.put("start", aTimeStamp);
		json.put("end", RenderName.dateAndTime());
		json.put("thinking", "generate random initial bot output");
		return json;
	}
	
	
	private MucusInteractionData getInitialBotMID(String dateTimeStamp)
	{
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		MucusInteractionData mid = MucusInteractionDataFactory.getMid(rndContainer, "bot", rmg, rnd);
		mid.setRndContainer(rndContainer);
		mid.setDateTimeStamp(dateTimeStamp);
		mid.setRandomMelodyGenerator(rmg);
		return mid;
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
		(minumumMutantAttacks 
				+ (
						(maximumMutantAttacks - minumumMutantAttacks) 
						* aBotMid.getBotPersonality().getSafeRisky()
				)
		);
		for (int i = 0; i < botOptionCount ; i++)
		{
//			System.out.println("generating bot option=" + i);
			RMRandomNumberContainer rndContainer = rmg.getMutatedRandomNumberContainer
					(
							minimumMutation, 
							mutantAttackCount, 
							aBotMid.getParameterScope(), 
							aUserMid.getRndContainer(),
							rnd);
			MucusInteractionData mid = MucusInteractionDataFactory.getMid(rndContainer, "bot_option_" + getZeroPaddedNumber(i, 2), rmg, rnd);
			mid.appendThinking("bot variation=" + i);
			mid.appendJSONThinking("bot_variation", i);
			mid.setBotPersonality(aBotMid.getBotPersonality());
			mid.setDateTimeStamp(aBotMid.getDateTimeStamp());
			transferBotMidAndRndContainerThinkingToMid(aBotMid, rndContainer,
					mid);
			midList.add(mid);
		}
		return midList;
	}



	public void transferBotMidAndRndContainerThinkingToMid (
			MucusInteractionData aBotMid, 
			RMRandomNumberContainer rndContainer,
			MucusInteractionData mid)
	{
		if (aBotMid.getThinkingJSON() != null)
		{
			for (String key: aBotMid.getThinkingJSON().keySet())
			{
				mid.appendJSONThinking(key, aBotMid.getThinkingJSON().get(key));
			}
		}
		if (rndContainer.getJSONThinking() != null)
		{
			for (String key: rndContainer.getJSONThinking().keySet())
			{
				mid.appendJSONThinking(key, rndContainer.getJSONThinking().get(key));
			}			
		}
	}
	
	
	
	private MucusInteractionData makeParameterScopeForVariations(MucusInteractionData aBotMid)
	{
		aBotMid.setParameterScope(Parameter.values());
		aBotMid.appendJSONThinking(
				"parameter_scope_for_bot_variations", 
				new JSONArray(Arrays.asList(Parameter.values()).stream()
				.map(Parameter::name)
				.collect(Collectors.toList())
				));
		return aBotMid;
	}
	
	
	
	private MucusInteractionData getMIDWithBotPersonality(MucusInteractionData aMid, Random aRnd)
	{
		double safeRisky = safeRiskyOptions[aRnd.nextInt(safeRiskyOptions.length)];
		double pleaseProvoke = pleaseProvokeOptions[aRnd.nextInt(pleaseProvokeOptions.length)];	
		aMid.setBotPersonality(new BotPersonality(safeRisky, pleaseProvoke));
		aMid.appendThinking("\nbot personality:\n" + aMid.getBotPersonality().toString());
		aMid.appendJSONThinking("bot_personality", aMid.getBotPersonality().asJSON());
		return aMid;
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



	public MucusInteractionData addTestControllerToMelody(MucusInteractionData mid, String timeStamp) {
		Mu melody = mid.getMu().getMu("melody");
		if (melody == null)
		{
			logger.info("Mu named 'melody' not found");
		}
		else
		{
			Random rnd = new Random();
			double lengthInQuarters = melody.getLengthInQuarters();
			for (double pos = 0.0; pos < lengthInQuarters; pos++)
			{
				double value = rnd.nextDouble();
				Mu ccMu = new Mu("cc");
				ccMu.setControllerValue(value);
				ccMu.addTag(MuTag.CONTROLLER_LP);
				melody.addMu(ccMu, pos);
			}
		}
		return mid;
	}



	



	
	
	
	

}
