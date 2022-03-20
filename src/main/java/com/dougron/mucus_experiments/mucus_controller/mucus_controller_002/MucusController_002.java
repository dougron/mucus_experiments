package main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_002;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionData;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.ParameterSpecificFeedback;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.FeedbackObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.mucus_controller.MucusController;
import main.java.com.dougron.mucus_experiments.mucus_controller.MucusControllerInterface;
import main.java.da_utils.render_name.RenderName;



public class MucusController_002 extends MucusController implements MucusControllerInterface
{

	private enum UserApproval {
		INVALID, 
		LIKE_AND_MOVE_ON, LIKE_AND_TRY_AGAIN, 
		NOT_SURE_AND_MOVE_ON, NOT_SURE_AND_TRY_AGAIN,
		DISLIKE_AND_MOVE_ON, DISLIKE_AND_TRY_AGAIN,
		ADD_CRITERIA_AND_TRY_AGAIN, REPLACE_CRITERIA_AND_TRY_AGAIN
		};
	
	
//	private int maxUserVariationCount = 3;
//	private int userVariationCount = 0;
//	private ArrayList<MucusInteractionData> currentMidList = new ArrayList<MucusInteractionData>();
	MucusInteractionData currentPlayingMid = new MucusInteractionData();
	MucusInteractionData mostRecentApprovedMID = new MucusInteractionData();
	MucusInteractionData mostRecentBotMID = new MucusInteractionData();
	private String timestampOfMostRecentUserFeedback = "MOST_RECENT_USER_FEEDBACK_TIMESTAMP";
	private JSONObject currentUserFeedbackFile;
	private JSONObject currentUserFeedbackIndices;


	public MucusController_002 (MucusOutputManager aMucusOutputManager)
	{
		super(aMucusOutputManager);
	}

	
	
	@Override
	public JSONObject receiveUserInput (JSONObject aJSONObject)
	{
		JSONObject reply = new JSONObject();
		if (aJSONObject.isEmpty())
		{
			reply.put("message", "");
		}
		else if (aJSONObject.has("message"))
		{
			if (aJSONObject.get("message").equals("hello mucus")) reply.put("message", "hello minion");
		}
		else if(aJSONObject.has("start_new_session"))
		{
			String[] timeStamps = RenderName.twoDistinctButCloseDateAndTime();
			getOutputManager().startNewSession(timeStamps[0]);
			MucusInteractionData mid = getInitialBotOutput(timeStamps[1]);
			currentPlayingMid = mid;
			mostRecentBotMID = mid;
			outputAllItemsForNewMu(timeStamps[1], mid);
			loadReplyWithGetUserFeedbackInstructions(reply);			
		}
		else if(aJSONObject.has("user_feedback"))
		{
			manageUserFeedbackEvent(aJSONObject, reply);			
		}
		else if(aJSONObject.has("more_user_feedback"))
		{
			manageMoreUserFeedbackEvent(aJSONObject, reply);			
		}
		else if(aJSONObject.has("user_approval"))
		{
			UserApproval userApproval = getUserApproval(aJSONObject);
			if (userApproval == UserApproval.LIKE_AND_MOVE_ON)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.LIKE_AND_MOVE_ON));
				mostRecentApprovedMID = currentPlayingMid;
				getKernel().addToApprovedList(mostRecentApprovedMID);
				MucusInteractionData mid = getBotVariation();
				currentPlayingMid = mid;
				mostRecentBotMID = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserFeedbackInstructions(reply);
			}
			else if (userApproval == UserApproval.LIKE_AND_TRY_AGAIN)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.LIKE_AND_TRY_AGAIN));
				getKernel().addToApprovedList(mostRecentApprovedMID);
				MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
				currentPlayingMid = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserApprovalInstructions(reply);			
			}
			else if (userApproval == UserApproval.NOT_SURE_AND_MOVE_ON)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.NOT_SURE_AND_MOVE_ON));
				mostRecentApprovedMID = currentPlayingMid;
//				getKernel().addToApprovedList(mostRecentApprovedMID);
				MucusInteractionData mid = getBotVariation();
				currentPlayingMid = mid;
				mostRecentBotMID = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserFeedbackInstructions(reply);
			}
			else if (userApproval == UserApproval.NOT_SURE_AND_TRY_AGAIN)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.NOT_SURE_AND_TRY_AGAIN));
//				getKernel().addToApprovedList(mostRecentApprovedMID);
				MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
				currentPlayingMid = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserApprovalInstructions(reply);			
			}
			else if (userApproval == UserApproval.DISLIKE_AND_MOVE_ON)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.DISLIKE_AND_MOVE_ON));
				mostRecentApprovedMID = currentPlayingMid;
				getKernel().addToRejectedList(currentPlayingMid);
				MucusInteractionData mid = getBotVariation();
				currentPlayingMid = mid;
				mostRecentBotMID = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserFeedbackInstructions(reply);
			}
			else if (userApproval == UserApproval.DISLIKE_AND_TRY_AGAIN)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.NOT_SURE_AND_TRY_AGAIN));
				getKernel().addToRejectedList(currentPlayingMid);
				MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
				currentPlayingMid = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserApprovalInstructions(reply);			
			}
			else if (userApproval == UserApproval.ADD_CRITERIA_AND_TRY_AGAIN)
			{
				sendMostRecentBotMidToPlaybackSoTheUserCanReEvaluateFeedback();
				loadReplyWithGetMoreUserFeedbackInstructions(reply);
			}
			else if (userApproval == UserApproval.REPLACE_CRITERIA_AND_TRY_AGAIN)
			{
				sendMostRecentBotMidToPlaybackSoTheUserCanReEvaluateFeedback();
				loadReplyWithGetUserFeedbackInstructions(reply);
			}
			
			
			
			
//			else if (userApproval == UserApproval.TRY_AGAIN)
//			{
//				String timeStamp = RenderName.dateAndTime();
//				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.TRY_AGAIN));
//				getKernel().addToRejectedList(currentPlayingMid);
//				MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
//				currentPlayingMid = mid;
//				outputAllItemsForNewMu(timeStamp, mid);
//				loadReplyWithGetUserApprovalInstructions(reply);			
//			}
//			else if (userApproval == UserApproval.NEW_BOT_VARIATION)
//			{
//				String timeStamp = RenderName.dateAndTime();
//				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.NEW_BOT_VARIATION));
//				getKernel().addToRejectedList(currentPlayingMid);
//				MucusInteractionData mid = getBotVariation();
//				currentPlayingMid = mid;
//				outputAllItemsForNewMu(timeStamp, mid);
//				loadReplyWithGetUserFeedbackInstructions(reply);			
//			}
			else
			{
				loadReplyWithGetUserApprovalInstructions(reply);
			}
		}
		else if (aJSONObject.has("new_user"))
		{
			String timeStamp = RenderName.dateAndTime();
			getOutputManager().addNewUser(timeStamp, aJSONObject.getJSONObject("new_user"));
			reply.put("current_user", aJSONObject.getJSONObject("new_user").getString("user_name"));
		}
		else if (aJSONObject.has("existing_user_login"))
		{
			String timeStamp = RenderName.dateAndTime();
			getOutputManager().logInExistingUser(timeStamp, aJSONObject.getJSONObject("existing_user_login"));
			reply.put("current_user", aJSONObject.getJSONObject("existing_user_login").getString("user_name"));
		}
		return reply;
	}



	private void sendMostRecentBotMidToPlaybackSoTheUserCanReEvaluateFeedback() {
		getOutputManager().outputToPlayback(mostRecentBotMID.getDateTimeStamp(), mostRecentBotMID.getMu());
	}



	public void manageUserFeedbackEvent (JSONObject aJSONObject,
			JSONObject reply)
	{
		String timeStamp = RenderName.dateAndTime();
		JSONObject userFeedbackFile = getUserFeedbackFile(aJSONObject, "user_feedback");
		currentUserFeedbackFile = userFeedbackFile;
		currentUserFeedbackIndices = getUserFeedbackIndices(aJSONObject, "user_feedback");
		if (userFeedbackFile.getJSONArray("user_feedback").length() == 0)
		{
			loadReplyWithGetUserFeedbackInstructions(reply);
			reply.put("input_error", "the input could not be used for feedback");
		}
		else
		{
			getOutputManager().outputUserFeedbackFile(currentPlayingMid.getDateTimeStamp(), getUserFeedbackFile(aJSONObject, "user_feedback"));
			timestampOfMostRecentUserFeedback = currentPlayingMid.getDateTimeStamp();
			addParameterSpecificFeedbackToMostRecentBotMid(aJSONObject);						
			MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
			currentPlayingMid = mid;
			outputAllItemsForNewMu(timeStamp, mid);
			loadReplyWithGetUserApprovalInstructions(reply);
		}
	}
	
	
	
	private JSONObject getUserFeedbackIndices(JSONObject aJSONObject, String userFeedbackKey) {
		JSONObject json = new JSONObject();
		JSONArray arr = new JSONArray();
		if (aJSONObject.has(userFeedbackKey))
		{
			for (Object o: getObjectListOfFeedbackIndices(aJSONObject, userFeedbackKey))
			{
				arr.put((Integer)o);
			}
			json.put("user_feedback", arr);
		}
		return json;
	}



	public void manageMoreUserFeedbackEvent (JSONObject aJSONObject,
			JSONObject reply)
	{
		String timeStamp = RenderName.dateAndTime();
		JSONObject userFeedbackFile = getUserFeedbackFile(aJSONObject, "more_user_feedback");
		
		if (currentUserFeedbackFile != null)
		{
			JSONArray arr1 = userFeedbackFile.getJSONArray("user_feedback");
			JSONArray arr2 = currentUserFeedbackFile.getJSONArray("user_feedback");
			JSONArray result = concatJsonArrays(arr2, arr1);
			userFeedbackFile.put("user_feedback", result);
		}
		currentUserFeedbackFile = userFeedbackFile;
		if (currentUserFeedbackIndices != null)
		{
			JSONArray result = concatJsonArrays(
					currentUserFeedbackIndices.getJSONArray("user_feedback"),
					getUserFeedbackIndices(aJSONObject,"more_user_feedback").getJSONArray("user_feedback")
					);
			currentUserFeedbackIndices.put("user_feedback", result);
		}
		else
		{
			currentUserFeedbackIndices = new JSONObject();
			currentUserFeedbackIndices.append("user_feedback", aJSONObject.getJSONArray("more_user_feedback"));
		}
		
		if (userFeedbackFile.getJSONArray("user_feedback").length() == 0)
		{
			loadReplyWithGetUserFeedbackInstructions(reply);
			reply.put("input_error", "the input could not be used for feedback");
		}
		else
		{
			getOutputManager().outputUserFeedbackFile(mostRecentBotMID.getDateTimeStamp() + "-more_added_at_" + timeStamp, currentUserFeedbackFile);
//			getOutputManager().outputUserFeedbackFile(currentPlayingMid.getDateTimeStamp(), getUserFeedbackFile(userFeedbackFile, "user_feedback"));
//			timestampOfMostRecentUserFeedback = currentPlayingMid.getDateTimeStamp();
			addParameterSpecificFeedbackToMostRecentBotMid(currentUserFeedbackIndices);						
			MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
			currentPlayingMid = mid; 
			outputAllItemsForNewMu(timeStamp, mid);
			loadReplyWithGetUserApprovalInstructions(reply);
		}
	}



	private JSONArray concatJsonArrays(JSONArray... arrs) {
		JSONArray result = new JSONArray();
		for (JSONArray arr: arrs)
		{
			for (int i = 0; i < arr.length(); i++)
			{
				result.put(arr.get(i));
			}
		}
		return result;
	}



	public void addParameterSpecificFeedbackToMostRecentBotMid (
			JSONObject aJSONObject)
	{
		List<FeedbackObject> foList = getListOfFeedbackObjectsFromUserFeedbackIndices(
				aJSONObject, "user_feedback");
		mostRecentBotMID.clearParameterSpecificFeedbackList();
		mostRecentBotMID.addParameterSpecificFeedbackItem(foList);
	}


	
	public List<FeedbackObject> getListOfFeedbackObjectsFromUserFeedbackIndices (
			JSONObject aJSONObject, String userFeedbackKey)
	{
		List<Object> listOfIndices = getObjectListOfFeedbackIndices(aJSONObject, userFeedbackKey);
		
		List<FeedbackObject> foList = listOfIndices.stream()
				.filter(index -> (Integer)index < ParameterSpecificFeedback.getArray().length && (Integer)index > -1)				
				.map(item -> ParameterSpecificFeedback.getArray()[(Integer)item])
				.collect(Collectors.toList());
		return foList;
	}



	/*
	 *  "user_feedback" item can be string or array of integers. the try/catch will fail on 
	 *  String str = aJSONObject.getString("user_feedback");
	 *  if it is an array and the catch will deal with it correctly as an array
	 *  or it wont fail and will be handled as a string of comma separated ints
	 */
	private List<Object> getObjectListOfFeedbackIndices(JSONObject aJSONObject, String userFeedbackKey) 
	{
		List<Object> listOfIndices;
		try 
		{
			String str = aJSONObject.getString(userFeedbackKey);
			String[] split = str.split(",");
			listOfIndices = Arrays.asList(split).stream()
					.map(index -> Ints.tryParse(index.trim()))
					.filter(x -> x != null)					
					.collect(Collectors.toList());
		}
		catch (JSONException jex)
		{
//			System.out.println("json exception caught &&&&&&&&&&");
			listOfIndices = aJSONObject.getJSONArray(userFeedbackKey).toList();
		}
		return listOfIndices;
	}



	public void loadReplyWithGetUserApprovalInstructions (JSONObject reply)
	{
		String foMessage = getListOfFeedbackObjectsForConsoleMessage();
		reply.put("console_message", foMessage + "get_user_approval");
		reply.put("get_input", ImmutableMap.of
				(
				"input_message", 
				" 1)like and new bot variation\n 2)like but try again" + 
						"\n 3)not sure and new bot variation\n 4)not sure and try again" +
						"\n 5)dislike and new bot variation\n 6)dislike and try again" +
						"\n 7)add criteria\n 8)replace criteria", 
				"return_message", 
				"user_approval"
				));
	}



	private String getListOfFeedbackObjectsForConsoleMessage() {
		StringBuilder sb = new StringBuilder();
		if (currentUserFeedbackFile != null)
		{
			JSONArray arr = currentUserFeedbackFile.getJSONArray("user_feedback");
			sb.append("current feedback items\n");
			for (Object o: arr)
			{
				sb.append("- " + (String)o + "\n");
			}
			
		}
		
		return sb.toString();
	}



	public void loadReplyWithGetUserFeedbackInstructions (JSONObject reply)
	{
		reply.put("console_message", "feedback_options");
		reply.put("get_input", ImmutableMap.of("input_message", "get feedback", "return_message", "user_feedback"));
		List<String> list = getFeedbackOptionDescriptionsAsAListOfStrings();
		reply.put("feedback_options", new JSONArray(list));
	}
	
	
	
	public void loadReplyWithGetMoreUserFeedbackInstructions (JSONObject reply)
	{
		reply.put("console_message", "feedback_options");
		reply.put("get_input", ImmutableMap.of("input_message", "get feedback", "return_message", "more_user_feedback"));
		List<String> list = getFeedbackOptionDescriptionsAsAListOfStrings();
		reply.put("feedback_options", new JSONArray(list));
	}



	int index = -1;
	public List<String> getFeedbackOptionDescriptionsAsAListOfStrings ()
	{	
		index = -1;
		List<String> list = ParameterSpecificFeedback.getList().stream()
		.map(item -> {
			index++;
			return index + ") " + item.getDescription();
		})
		.collect(Collectors.toList());
		return list;
	}



	public void outputAllItemsForNewMu (String timeStamp,
			MucusInteractionData mid)
	{
		setMelodyClipNameToTimeStamp(mid, timeStamp);
		getOutputManager().outputToPlayback(timeStamp, mid.getMu());
		getOutputManager().outputToMusicXML(timeStamp, mid.getMu());
		getOutputManager().outputThinkingFile(timeStamp, mid.getThinkingJSON());
		getOutputManager().outputRndContainer(timeStamp, mid.getRndContainer());
		getOutputManager().outputParameterObject(timeStamp, mid.getPo());
		getOutputManager().outputMuAsXML(timeStamp, mid.getMu());
		getOutputManager().outputStatVarFile(timeStamp, mid.getRandomMelodyGenerator().makeJSONObject());
	}
	
	
	
	private void setMelodyClipNameToTimeStamp (MucusInteractionData mid, String timeStamp)
	{
		Mu mu = mid.getMu();
		Mu melody = mu.getMu("melody");
		melody.setName(timeStamp);
	}



	private UserApproval getUserApproval (JSONObject aJSONObject)
	{
		switch (aJSONObject.getInt("user_approval"))
		{
		case 1:	return UserApproval.LIKE_AND_MOVE_ON;
		case 2:	return UserApproval.LIKE_AND_TRY_AGAIN;
		case 3:	return UserApproval.NOT_SURE_AND_MOVE_ON;
		case 4:	return UserApproval.NOT_SURE_AND_TRY_AGAIN;
		case 5:	return UserApproval.DISLIKE_AND_MOVE_ON;
		case 6:	return UserApproval.DISLIKE_AND_TRY_AGAIN;
		case 7:	return UserApproval.ADD_CRITERIA_AND_TRY_AGAIN;
		case 8:	return UserApproval.REPLACE_CRITERIA_AND_TRY_AGAIN;
		}
		return UserApproval.INVALID;
	}



	private JSONObject getUserApprovalFile (UserApproval aOpinion)
	{
		JSONObject json = new JSONObject();
		json.put("user_approval", aOpinion);
		json.put("current_playing_file_timestamp", currentPlayingMid.getDateTimeStamp());
		json.put("time_stamp_of_user_feedback", timestampOfMostRecentUserFeedback);
		return json;
	}



	private MucusInteractionData getBotVariation ()
	{
		if (mostRecentApprovedMID == null)
		{
			return getKernel().getBotVariation(mostRecentBotMID, getOutputManager());
		}
		return getKernel().getBotVariation(mostRecentApprovedMID, getOutputManager());
	}



	private JSONObject getUserFeedbackFile (JSONObject aUserFeedbackFile, String userFeedbackKey)
	{
		JSONObject json = new JSONObject();
		List<String> list = getListOfFeedbackObjectsFromUserFeedbackIndices(aUserFeedbackFile, userFeedbackKey).stream()
				.map(item -> item.getDescription())
				.collect(Collectors.toList());		
		json.put("user_feedback", list);
		return json;
	}



//	private MucusInteractionData getMIDThatWasOuputForApproval ()
//	{
//		MucusInteractionData mid = new MucusInteractionData();
//		mid.setMu(new Mu("user_variation"));
//		return mid;
//	}



//	private JSONObject getApprovalThinkingJSON ()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}



	private MucusInteractionData getUserVariation (String aTimeStamp, MucusInteractionData aMid)
	{
//		MucusInteractionData mid = new MucusInteractionData();
//		mid.setMu(new Mu("user_variation"));
//		mid.setRandomMelodyGenerator(RMG_001.getInstance());
//		return mid;
////		return getKernel().get
		return getKernel().getUserVariation(aTimeStamp, aMid);
	}



//	private JSONObject getUserFeedbackThinkingFile ()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}



	private MucusInteractionData getInitialBotOutput(String aTimeStamp)
	{
		return getKernel().getInitialBotVariation(aTimeStamp);
	}

}
