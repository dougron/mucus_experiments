package main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.TestingOutputManager;
import main.java.com.dougron.mucus_experiments.completed.Mu038_MuKernelConsoleFrontEnd;
import main.java.com.dougron.mucus_experiments.mucus_controller.MucusControllerInterface;
import main.java.com.dougron.mucus_experiments.mucus_controller.mucus_controller_001.MucusController_001;

class MucusController_001_Tests
{
	
	public JSONObject logStartOfSessionAndGetInitialBotOutput (
			MucusControllerInterface mk, JSONObject json_receive)
	{
		return Mu038_MuKernelConsoleFrontEnd.logStartNewSession(json_receive, mk);
	}


	public JSONObject sendUserFeedack (MucusControllerInterface mk, int[] aUserFeedbackIndexArray)
	{
		JSONObject json_send = new JSONObject();
		json_send.put("user_feedback", new JSONArray(aUserFeedbackIndexArray));	// new int is the list of indices of feedback objects captured from user
		return mk.receiveUserInput(json_send);
	}


	public JSONObject logInNewUser (MucusControllerInterface mk)
	{
		JSONObject json_send = new JSONObject();
		json_send.put("new_user", ImmutableMap.of("user_name", "doug", "password", "oooo"));
		return mk.receiveUserInput(json_send);
	}
	
	@Nested
	class mucuskernel_001
	{
		@Test
		void _instantiates () throws Exception
		{
			MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
			assertNotNull(mk);
		}
		
		
		@Test
		void _receives_json_input () throws Exception
		{
			MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
			JSONObject json = new JSONObject();
			mk.receiveUserInput(json);
		}
		
		
		@Test
		void _returns_json_object_after_recweiving_json_input () throws Exception
		{
			MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
			JSONObject json = new JSONObject();
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply instanceof JSONObject);
		}
	}

		
	@Nested
	class reply_contains
	{
		@Test
		void _empty_string_message_if_input_was_empty () throws Exception
		{
			MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
			JSONObject json = new JSONObject();
			JSONObject reply = mk.receiveUserInput(json);
//			System.out.println(reply.getJSONArray("message").get(0));
			assertEquals("", reply.get("message"));
		}
		
		
		@Test
		void _hello_minion_message_if_input_was_hello_mucus () throws Exception
		{
			MucusControllerInterface mk = new MucusController_001(LocalOutputManager.getInstance());
			JSONObject json = new JSONObject();
			json.put("message", "hello mucus");
			JSONObject reply = mk.receiveUserInput(json);
			assertEquals("hello minion", reply.get("message"));
		}
		
	}
	
	
	@Nested
	class when_user_input_is_start_then_reply_contains
	{
		@Test
		void _console_message_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("console_message"));
		}
		
		
		@Test
		void _get_input_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("get_input"));
		}
	}
		
	
	@Nested
	class when_user_input_is_start_then_output_manager_indicates
	{
		@Test
		void _session_start_has_been_noted () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("application_start_called"));
		}

		
		@Test
		void _mu_passed_to_output_to_playback () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_playback"));
		}
		
		
		@Test
		void _mu_passed_to_musicxml_maker () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_musicxml_maker"));
		}
		
		
		@Test
		void _json_passed_to_rnd_container_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_rnd_container_file"));
		}
		
		
		@Test
		void _json_passed_to_parameter_object_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_parameter_object_file"));
		}
		
		
		@Test
		void _json_passed_to_mu_xml_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_muxml_file"));	// NB not a musicxml file for output to score display. This is a persistance object for Mu object
		}
		
		
		@Test
		void _json_passed_to_thinking_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_thinking_file"));	
		}
		
		
		@Test
		void _json_passed_to_random_melody_generator_static_variable_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			JSONObject json = new JSONObject();
			json.put("start_new_session", true);
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_statvar_file"));	
		}
		
		
	}
	
	
	@Nested
	class when_user_returns_feedback_then_reply_contains
	{

		@Test
		void _get_user_approval_console_message_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("console_message"));
			assertEquals("get_user_approval", reply.get("console_message"));
		}
		
		
		@Test
		void _get_input_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("get_input"));
		}
		
	}
	
	
	@Nested
	class when_user_returns_feedback_then_output_manager_indicates
	{

		@Test
		void _json_passed_to_user_feedback_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_user_feedback_file"));
		}
		
		
		@Test
		void _mu_passed_to_output_to_playback () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_playback"));
		}
		
		
		@Test
		void _mu_passed_to_musicxml_maker () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_musicxml_maker"));
		}
		
		
		@Test
		void _json_passed_to_thinking_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_thinking_file"));
		}
		

		@Test
		void _json_passed_to_rnd_container_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_rnd_container_file"));
		}
		
		
		@Test
		void _json_passed_to_parameter_object_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_parameter_object_file"));
		}
		
		
		@Test
		void _json_passed_to_mu_xml_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_muxml_file"));	// NB not a musicxml file for output to score display. This is a persistance object for Mu object
		}

		
		@Test
		void _json_passed_to_random_melody_generator_static_variable_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_feedback", new JSONArray(new int[] {0, 4}));
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_statvar_file"));	
		}
		
		
	}
	

	@Nested
	class when_user_returns_approval_event_equal_to_approved	// approved = "1"
	{
		
		@Test
		void _json_passed_to_user_approval_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_user_approval_file"));
		}
	
		
		@Test
		void _mu_passed_to_output_to_playback () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_playback"));
		}
		
		
		@Test
		void _mu_passed_to_musicxml_maker () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_musicxml_maker"));
		}
		
		
		@Test
		void _json_passed_to_thinking_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_thinking_file"));	
		}
		
		
		@Test
		void _json_passed_to_rnd_container_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_rnd_container_file"));
		}
		
		
		@Test
		void _json_passed_to_parameter_object_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_parameter_object_file"));
		}
		
		
		@Test
		void _json_passed_to_mu_xml_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_muxml_file"));	// NB not a musicxml file for output to score display. This is a persistance object for Mu object
		}
		
		
		@Test
		void _json_passed_to_random_melody_generator_static_variable_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_statvar_file"));	
		}
		
		
		@Test
		void _reply_contains_feedback_options_console_message_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});
			
			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "1");
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("console_message"));
			assertEquals("feedback_options", reply.get("console_message"));
		}


		
	}
	
	
	@Nested
	class when_user_returns_approval_event_equal_to_try_another		// try_another = "2"
	{
		@Test
		void _json_passed_to_user_approval_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_user_approval_file"));
		}
		
		
		@Test
		void _mu_passed_to_output_to_playback () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_playback"));
		}
		
		
		@Test
		void _mu_passed_to_musicxml_maker () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_musicxml_maker"));
		}
		
		
		@Test
		void _json_passed_to_thinking_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_thinking_file"));	
		}
		
		
		@Test
		void _json_passed_to_rnd_container_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_rnd_container_file"));
		}
		
		
		@Test
		void _json_passed_to_parameter_object_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_parameter_object_file"));
		}
		
		
		@Test
		void _json_passed_to_mu_xml_file_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_muxml_file"));	// NB not a musicxml file for output to score display. This is a persistance object for Mu object
		}
		
		
		@Test
		void _json_passed_to_random_melody_generator_static_variable_output () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			mk.receiveUserInput(json);
			JSONObject testOutputLog = tom.getTestOutputLog();
			assertTrue(testOutputLog.has("output_to_statvar_file"));	
		}
		
		
		@Test
		void _reply_contains_feedback_options_console_message_item () throws Exception
		{
			TestingOutputManager tom = new TestingOutputManager();
			MucusControllerInterface mk = new MucusController_001(tom);
			
			// prior steps of interaction
			JSONObject json_receive = logInNewUser(mk);
			logStartOfSessionAndGetInitialBotOutput(mk, json_receive);
			sendUserFeedack(mk, new int[] {30});

			// interaction under test
			JSONObject json = new JSONObject();
			json.put("user_approval", "2");
			JSONObject reply = mk.receiveUserInput(json);
			assertTrue(reply.has("console_message"));
			assertEquals("get_user_approval", reply.get("console_message"));
		}
		
		
//		@Test
//		void _reply_contains_choose_best_of_a_bad_bunch_console_message_after_user_option_count_is_full () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("user_approval", "2");
//			mk.receiveUserInput(json);
//			assertTrue(reply.has("console_message"));
//			assertEquals("get_user_approval", reply.get("console_message"));
//			
//			reply = mk.receiveUserInput(json);
//			assertTrue(reply.has("console_message"));
//			assertEquals("get_user_approval", reply.get("console_message"));
//			
//			reply = mk.receiveUserInput(json);
//			assertTrue(reply.has("console_message"));
//			assertEquals("get_user_approval", reply.get("console_message"));
//			
//			reply = mk.receiveUserInput(json);
//			assertTrue(reply.has("console_message"));
//			assertEquals("choose_best_of_a_bad_bunch", reply.get("console_message"));
//		}
	}
	
//	@Disabled
//	@Nested
//	class when_best_of_a_bad_bunch_returned
//	{
//		@Test
//		void _json_passed_to_best_of_a_bad_bunch_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_best_of_a_bad_bunch_file"));
//		}
//		
//		
//		@Test
//		void _mu_passed_to_output_to_playback () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_playback"));
//		}
//		
//		
//		@Test
//		void _mu_passed_to_musicxml_maker () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_musicxml_maker"));
//		}
//		
//		
//		@Test
//		void _json_passed_to_thinking_file_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_thinking_file"));	
//		}
//		
//		
//		@Test
//		void _json_passed_to_rnd_container_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_rnd_container_file"));
//		}
//		
//		
//		@Test
//		void _json_passed_to_parameter_object_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_parameter_object_file"));
//		}
//		
//		
//		@Test
//		void _json_passed_to_mu_xml_file_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_muxml_file"));	// NB not a musicxml file for output to score display. This is a persistance object for Mu object
//		}
//		
//		
//		@Test
//		void _json_passed_to_random_melody_generator_static_variable_output () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			JSONObject testOutputLog = tom.getTestOutputLog();
//			assertTrue(testOutputLog.has("output_to_statvar_file"));	
//		}
//		
//		
//		@Test
//		void _reply_contains_feedback_options_console_message_item () throws Exception
//		{
//			TestingOutputManager tom = new TestingOutputManager();
//			MucusControllerInterface mk = new MucusController_001(tom);
//			JSONObject json = new JSONObject();
//			json.put("best_of_a_bad_bunch", "some_result");
//			mk.receiveUserInput(json);
//			assertTrue(reply.has("console_message"));
//			assertEquals("feedback_options", reply.get("console_message"));
//		}
//	}
	
	
	@Test
	void when_a_new_user_is_added_then_the_output_logs_a_new_user_added () throws Exception
	{
		TestingOutputManager tom = new TestingOutputManager();
		MucusControllerInterface mk = new MucusController_001(tom);
		JSONObject json = new JSONObject();
		json.put("new_user", ImmutableMap.of("user_name", "a_name"));
		mk.receiveUserInput(json);
		JSONObject testOutputLog = tom.getTestOutputLog();
		assertTrue(testOutputLog.has("new_user_added"));
	}
	
	
	@Test
	void when_a_previous_user_logs_in_then_the_output_logs_previous_user_logged_in () throws Exception
	{
		TestingOutputManager tom = new TestingOutputManager();
		MucusControllerInterface mk = new MucusController_001(tom);
		JSONObject json = new JSONObject();
		json.put("existing_user_login", ImmutableMap.of("user_name", "a_user_name", "password", "a_password"));
		mk.receiveUserInput(json);
		JSONObject testOutputLog = tom.getTestOutputLog();
		assertTrue(testOutputLog.has("existing_user_logged_in"));
	}
	
}
