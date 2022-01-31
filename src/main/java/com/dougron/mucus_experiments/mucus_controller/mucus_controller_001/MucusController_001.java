package main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_001;

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



public class MucusController_001 extends MucusController implements MucusControllerInterface
{

	private enum UserApproval {APPROVED, TRY_AGAIN, INVALID, NEW_BOT_VARIATION};
	
	
//	private int maxUserVariationCount = 3;
//	private int userVariationCount = 0;
//	private ArrayList<MucusInteractionData> currentMidList = new ArrayList<MucusInteractionData>();
	MucusInteractionData currentPlayingMid = new MucusInteractionData();
	MucusInteractionData mostRecentApprovedMID = new MucusInteractionData();
	MucusInteractionData mostRecentBotMID = new MucusInteractionData();
	private String timestampOfMostRecentUserFeedback = "MOST_RECENT_USER_FEEDBACK_TIMESTAMP";



	public MucusController_001 (MucusOutputManager aMucusOutputManager)
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
		else if(aJSONObject.has("user_approval"))
		{
			UserApproval userApproval = getUserApproval(aJSONObject);
			if (userApproval == UserApproval.APPROVED)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.APPROVED));
				mostRecentApprovedMID = currentPlayingMid;
				getKernel().addToApprovedList(mostRecentApprovedMID);
				MucusInteractionData mid = getBotVariation();
				currentPlayingMid = mid;
				mostRecentBotMID = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserFeedbackInstructions(reply);
			}
			else if (userApproval == UserApproval.TRY_AGAIN)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.TRY_AGAIN));
				getKernel().addToRejectedList(currentPlayingMid);
				MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
				currentPlayingMid = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserApprovalInstructions(reply);			
			}
			else if (userApproval == UserApproval.NEW_BOT_VARIATION)
			{
				String timeStamp = RenderName.dateAndTime();
				getOutputManager().outputUserApprovalFile(currentPlayingMid.getDateTimeStamp(), getUserApprovalFile(UserApproval.NEW_BOT_VARIATION));
				getKernel().addToRejectedList(currentPlayingMid);
				MucusInteractionData mid = getBotVariation();
				currentPlayingMid = mid;
				outputAllItemsForNewMu(timeStamp, mid);
				loadReplyWithGetUserFeedbackInstructions(reply);			
			}
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



	public void manageUserFeedbackEvent (JSONObject aJSONObject,
			JSONObject reply)
	{
		String timeStamp = RenderName.dateAndTime();
		JSONObject userFeedbackFile = getUserFeedbackFile(aJSONObject);
		if (userFeedbackFile.getJSONArray("user_feedback").length() == 0)
		{
			loadReplyWithGetUserFeedbackInstructions(reply);
			reply.put("input_error", "the input could not be used for feedback");
		}
		else
		{
			getOutputManager().outputUserFeedbackFile(currentPlayingMid.getDateTimeStamp(), getUserFeedbackFile(aJSONObject));
			timestampOfMostRecentUserFeedback = currentPlayingMid.getDateTimeStamp();
			addParameterSpecificFeedbackToMostRecentBotMid(aJSONObject);						
			MucusInteractionData mid = getUserVariation(timeStamp, mostRecentBotMID);
			currentPlayingMid = mid;
			outputAllItemsForNewMu(timeStamp, mid);
			loadReplyWithGetUserApprovalInstructions(reply);
		}
	}



	public void addParameterSpecificFeedbackToMostRecentBotMid (
			JSONObject aJSONObject)
	{
		List<FeedbackObject> foList = getListOfFeedbackObjectsFromUserFeedbackIndices(
				aJSONObject);
		mostRecentBotMID.clearParameterSpecificFeedbackList();
		mostRecentBotMID.addParameterSpecificFeedbackItem(foList);
	}


	
	/*
	 *  "user_feedback" item can be string or array of integers. the try/catch will fail on 
	 *  String str = aJSONObject.getString("user_feedback");
	 *  if it is an array and the catch will deal with it correctly as an array
	 *  or it wont fail and will be handled as a string of comma separated ints
	 */
	public List<FeedbackObject> getListOfFeedbackObjectsFromUserFeedbackIndices (
			JSONObject aJSONObject)
	{
		List<Object> listOfIndices;
		try 
		{
			String str = aJSONObject.getString("user_feedback");
			String[] split = str.split(",");
			listOfIndices = Arrays.asList(split).stream()
					.map(index -> Ints.tryParse(index.trim()))
					.filter(x -> x != null)					
					.collect(Collectors.toList());
		}
		catch (JSONException jex)
		{
//			System.out.println("json exception caught &&&&&&&&&&");
			listOfIndices = aJSONObject.getJSONArray("user_feedback").toList();
		}
		
		List<FeedbackObject> foList = listOfIndices.stream()
				.filter(index -> (Integer)index < ParameterSpecificFeedback.getArray().length && (Integer)index > -1)				
				.map(item -> ParameterSpecificFeedback.getArray()[(Integer)item])
				.collect(Collectors.toList());
		return foList;
	}



	public void loadReplyWithGetUserApprovalInstructions (JSONObject reply)
	{
		reply.put("console_message", "get_user_approval");
		reply.put("get_input", ImmutableMap.of("input_message", "1)approve\n2)try again\n3)new bot variation", "return_message", "user_approval"));
	}



	public void loadReplyWithGetUserFeedbackInstructions (JSONObject reply)
	{
		reply.put("console_message", "feedback_options");
		reply.put("get_input", ImmutableMap.of("input_message", "get feedback", "return_message", "user_feedback"));
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
		case 1:	return UserApproval.APPROVED;
		case 2:	return UserApproval.TRY_AGAIN;
		case 3:	return UserApproval.NEW_BOT_VARIATION;
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



	private JSONObject getUserFeedbackFile (JSONObject aUserFeedbackFile)
	{
		JSONObject json = new JSONObject();
		List<String> list = getListOfFeedbackObjectsFromUserFeedbackIndices(aUserFeedbackFile).stream()
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
