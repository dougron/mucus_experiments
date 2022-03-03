package main.java.com.dougron.mucus_experiments.completed;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus_experiments.mucus_controller.MucusControllerInterface;
import main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_001.MucusController_001;

public class Mu039_LikeMu038ButAutomated
{
	
	MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
	
	
	
	public Mu039_LikeMu038ButAutomated () throws InterruptedException
	{
		
		// user login
		System.out.println("\n##### USER LOGIN ####################################\n");
		JSONObject json_send = new JSONObject();
		json_send.put("new_user", ImmutableMap.of("user_name", "doug", "password", "oooo"));
		JSONObject json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		// in initial bot output
		System.out.println("\n##### START NEW SESSION WITH INITIAL BOT OUTPUT #####\n");
		json_receive = Mu038_MuKernelConsoleFrontEnd.logStartNewSession(json_receive, mk);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		Thread.sleep(1001);
		// user sends feedback
		System.out.println("\n##### USER FEEDBACK #################################\n");
		json_send.clear();
		json_send.put("user_feedback", new JSONArray(new int[] {30}));	// new int is the list of indices of feedback objects captured from user
		json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		Thread.sleep(1001);
		// user sends approval
		System.out.println("\n##### USER APPROVAL AND NEW BOT VARIATION ###########\n");
		json_send.clear();
		json_send.put("user_approval", 1);	// 1 - approve, 2 - reject, 3 - new bot output
		json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		Thread.sleep(1001);
		// user sends feedback
		System.out.println("\n##### USER FEEDBACK #################################\n");
		json_send.clear();
		json_send.put("user_feedback", new JSONArray(new int[] {0, 2}));	// new int is the list of indices of feedback objects captured from user
		json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		Thread.sleep(1001);
		// user sends try_again
		System.out.println("\n##### USER TRY_AGAIN AND NEW USER VARIATION #########\n");
		json_send.clear();
		json_send.put("user_approval", 2);	// 1 - approve, 2 - reject, 3 - new bot output
		json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		Thread.sleep(1001);
		// user sends try_again
		System.out.println("\n##### USER SAYS MAKE NEW BOT VARIATION #########\n");
		json_send.clear();
		json_send.put("user_approval", 3);	// 1 - approve, 2 - reject, 3 - new bot output
		json_receive = mk.receiveUserInput(json_send);
		System.out.println("backend to frontend:\n" + json_receive.toString(4));
		
		
		System.out.println("done.");
	}

	
	
	public static void main (String[] args)
	{
		try
		{
			new Mu039_LikeMu038ButAutomated();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

}
