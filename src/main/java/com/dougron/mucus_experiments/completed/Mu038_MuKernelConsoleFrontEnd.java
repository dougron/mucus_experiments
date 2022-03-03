package main.java.com.dougron.mucus_experiments.completed;

import java.util.Scanner;

import org.json.JSONObject;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus_experiments.mucus_controller.MucusControllerInterface;
import main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_001.MucusController_001;



public class Mu038_MuKernelConsoleFrontEnd
{

	private JSONObject log = new JSONObject();
	Scanner in = new Scanner(System.in);
	MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
	
	
	
	public Mu038_MuKernelConsoleFrontEnd ()
	{
		String input = "";
		JSONObject json_receive;
		JSONObject json_send;
		json_receive = logInUser();
		printJSON(json_receive);
		
		json_receive = logStartNewSession(json_receive, mk);
		
		
		
		while (true)
		{
			if (json_receive.has("get_input"))
			{
				printJSON(json_receive);
				System.out.println(json_receive.getString("console_message"));
				do 
				{
					System.out.println(json_receive.getJSONObject("get_input").get("input_message"));
					input = in.nextLine(); 					
				}
				while (input.equals(""));
				json_send = new JSONObject();
				json_send.put(json_receive.getJSONObject("get_input").getString("return_message"), input);
				json_receive = mk.receiveUserInput(json_send);
			}
			else
			{
				System.out.println("no input required. exiting......");
				break;
			}
		}
	}



	public static JSONObject logStartNewSession (JSONObject jsonUserDetails, MucusControllerInterface aMci)
	{
		JSONObject json_send = new JSONObject();
		json_send.put("start_new_session", true);
		json_send.put("current_user", jsonUserDetails.getString("current_user"));
		JSONObject json_receive = aMci.receiveUserInput(json_send);
		return json_receive;
	}



	private void printJSON (JSONObject aJsonObject)
	{
		System.out.println(aJsonObject.toString(4));
		
	}



	public JSONObject logInUser ()
	{
		String input;
		JSONObject json_send;
		JSONObject json_receive;
		while (true)
		{
			System.out.println("<login/new_user> <user name> <password>");
			input = in.nextLine();
			json_send = getLoginJson(input);
			if (json_send.has("new_user") || json_send.has("existing_user_login"))
			{
				json_receive = mk.receiveUserInput(json_send);
				if (json_receive.has("current_user")) break;
			}			
		}
		return json_receive;
	}
	
	
	
	private JSONObject getLoginJson (String input)
	{
		JSONObject json = new JSONObject();
		String[] split = input.split(" ");
		if (split.length == 3)
		{
			if (split[0].equals("new_user"))
			{
				json.put("new_user", ImmutableMap.of("user_name", split[1], "password", split[2]));
			}
			else if (split[0].equals("login"))
			{
				json.put("existing_user_login", ImmutableMap.of("user_name", split[1], "password", split[2]));
			}
		}
		return json;
	}



	void processIncomingJSON (JSONObject aJson)
	{
		for (String key: aJson.keySet())
		{
			log .append("received", "received item " + key);
			if (key.equals("console_message"))
			{
//				log.put("console_message", aJson.get(key));
				System.out.println(aJson.get(key));
			}
			else if (key.equals("get_user_input"))
			{
//				log.put("get_user_input", aJson.get(key));
			}
		}
	}
	
	
	
	public JSONObject getLog ()
	{
		return log;
	}
	
	
	
	public static void main (String[] args)
	{
		new Mu038_MuKernelConsoleFrontEnd();
	}


}
