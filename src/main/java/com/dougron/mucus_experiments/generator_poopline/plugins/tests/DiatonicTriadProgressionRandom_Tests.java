package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DiatonicTriadRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class DiatonicTriadProgressionRandom_Tests {
	
	
	Type listDoubleType = new TypeToken<List<Double>>() {}.getType();
	Type listIntegerType = new TypeToken<List<Integer>>() {}.getType();

	@Test
	void instantiates() {
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		assertThat(plug).isNotNull();
	}
	 
	
//	@Test
//	void when_passing_a_blank_package_and_no_access_to_required_parameter_plugins_then_getRenderedParameters_will_be_empty() throws Exception {
//		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.1, 0.7}));
//		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
//		pack = plug.process(pack);
//		assertThat(pack.getRenderedParametersFromJson().size()).isEqualTo(0);
//	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_and_no_access_to_required_parameter_plugins_then_repo_will_have_PHRASE_LENGTH_but_not_CHORD_LIST_GENERATOR() throws Exception {
		// because only the PHRASE_LENGTH will have rendered. CHORD_PROGRESSION will not render due to absence of XMLKEY
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.2}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.1, 0.7}));
		DiatonicTriadProgressionRandom plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		
//		assertThat(pack.getRenderedParametersFromJson().size()).isEqualTo(1);
		assertThat(pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)).isFalse();
		assertThat(pack.getRepo().containsKey(Parameter.PHRASE_LENGTH)).isTrue();
	}
	

	@Test
	void when_passing_a_package_with_XMLKEY_and_no_access_to_required_parameter_plugins_then_repo_will_have_XMLKEY_but_not_CHORD_LIST_GENERATOR() throws Exception {
		// because only the XMLKEY will have rendered. CHORD_PROGRESSION will not render due to absence of PHRASE_LENGTH
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.2}));
		pack.setDebugMode(true);
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.1, 0.7}));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
//		assertThat(pack.getRenderedParametersFromJson().size()).isEqualTo(1);
		assertThat(pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)).isFalse();
		assertThat(pack.getRepo().containsKey(Parameter.XMLKEY)).isTrue();
	}
	
	
//	@Test
//	void when_passing_a_package_with_PHRASE_LENGTH_and_XMLKEY_and_no_access_to_required_parameter_plugins_then_getRenderedParameters_will_be_length_3() throws Exception {
//		// all three will render
//		
//		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.2}));
//		PooplinePlugin lengthPlug = new RandomPhraseLength();
//		pack = lengthPlug.process(pack);
//		PooplinePlugin xmlkeyPlug = new RandomXMLKey();
//		pack = xmlkeyPlug.process(pack);
//		
//		pack.setRnd(new TestRandom(new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4}));
//		PooplinePlugin plug = new RandomDiatonicTriadProgression();
//		pack = plug.process(pack);
//		assertThat(pack.getRenderedParametersFromJson().size()).isEqualTo(3);
//	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_only_then_repo_will_not_have_CHORD_LIST_GENERATOR_item() throws Exception {
		// because only the PHRASE_LENGTH will have rendered. CHORD_PROGRESSION will not render due to absence of XMLKEY
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.2}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.1, 0.7}));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)).isFalse();
	}
	
	
	@Test
	void when_passing_a_package_with_XMLKEY_only_then_repo_will_not_have_CHORD_LIST_GENERATOR_item() throws Exception {
		// because only the XMLKEY will have rendered. CHORD_PROGRESSION will not render due to absence of PHRASE_LENGTH
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.2}));
		pack.setDebugMode(true);
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.1, 0.7}));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)).isFalse();
//		assertThat(pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString())).isFalse();
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_repo_will_have_CHORD_LIST_GENERATOR_item() throws Exception {
		// all three will render
		
		// phrase length is 2 bars
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4}));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.CHORD_LIST_GENERATOR)).isTrue();
//		assertThat(pack.getJson().has(Parameter.CHORD_LIST_GENERATOR.toString())).isTrue();
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_CHORD_LIST_GENERATOR_item_will_rndValue_array_same_same_random_generator_for_required_number_of_bars() throws Exception {
		// all three will render
		
		// phrase length is 2 bars
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
//		assertThat(pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.has("random_seed")).isTrue();
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7});
	}
	
//	@Disabled
//	@Test
//	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_json_CHORD_LIST_GENERATOR_random_seed_item_will_be_test_random_sequence() throws Exception {
//		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
//		PooplinePlugin lengthPlug = new PhraseLengthRandom();
//		pack = lengthPlug.process(pack);
//		
//		// xml key is 0 (key of C)
//		pack.setRnd(new TestRandom(0.45));
//		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
//		pack = xmlkeyPlug.process(pack);
//		
//		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
//		pack.setRnd(new TestRandom(chordsRandomSequence));
//		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
//		pack = plug.process(pack);
//		
//		JsonArray jsonArray = pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.get("random_seed").getAsJsonObject()
//				.get("values").getAsJsonArray();
//		
//		
//		List<Double> array = new Gson().fromJson(jsonArray, listDoubleType);//.toArray(new double[jsonArray.size()]);
//				
//		assertThat(array).isEqualTo(Arrays.asList(new double[] {0.1, 0.7}));
//		
//	}

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_3_and_XMLKEY_0_then_repo_CHORD_LIST_GENERATOR_rndValue_item_will_be_test_random_sequence_accomodating_the_no_repeat_rule() throws Exception {
		// cannot be 0.1, 0.7, 0.1 as this implies a repeat from the last to the first bar. 
		// will need to cycle through till it finds 0.4 for the last element
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.1}));
		pack.setDebugMode(true);
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
//		JsonArray jsonArray = pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.get("random_seed").getAsJsonObject()
//				.get("values").getAsJsonArray();
		
//		List<Double> array = new Gson().fromJson(jsonArray, listDoubleType);//.toArray(new double[jsonArray.size()]);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.4});	
	}
	

	@Test
	void when_passing_a_package_with_a_shorter_previous_PHRASE_LENGTH_then_repo_CHORD_LIST_GENERATOR_random_seed_item_will_be_test_random_sequence_accomodating_the_no_repeat_rule() throws Exception {
	
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.1}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.7));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1, 0.7});	
	}
	

	@Test
	void when_passing_a_package_with_a_longer_previous_PHRASE_LENGTH_then_json_CHORD_LIST_GENERATOR_random_seed_item_will_be_test_random_sequence_accomodating_the_no_repeat_rule() throws Exception {
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.1})); 
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.7));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.2));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.4, 0.7, 0.7, 0.1, 0.7, 0.1, 0.7});	
		// the extra values in the random_seed list remain behind even though they are not used as they are thematically relevant to
		// an incrementally evolving co creative work. There may be a situation when the phrase length grows longer and the 
		// algorithm 'remembers' what went before. Of course there is a repeat issue waiting to happen with the repeated 0.7
		// which a later test handles
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_5_and_XMLKEY_0_and_rnd_with_repeated_values_then_json_CHORD_LIST_GENERATOR_random_seed_item_will_be_satisfy_the_no_repeat_rule() throws Exception {

		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.3}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
//		JsonArray jsonArray = pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.get("random_seed").getAsJsonObject()
//				.get("values").getAsJsonArray();
//		
//		List<Double> array = new Gson().fromJson(jsonArray, listDoubleType);//.toArray(new double[jsonArray.size()]);
//		
//		assertThat(array).isEqualTo(Arrays.asList(new double[] {0.1, 0.7, 0.1, 0.7, 0.4}));	
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.1, 0.7, 0.4});	
	}
	

	@Test
	void when_passing_a_package_with_extra_prior_values_in_CHORD_LIST_GENERATOR_random_seed_which_will_fail_a_repeated_items_test_then_this_is_resolved_by_changing_the_2nd_repeated_item() throws Exception {
		
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.1})); 
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.7));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.2));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.4, 0.7, 0.7, 0.1, 0.7, 0.1, 0.7});	
		
		pack.getRepo().remove(Parameter.PHRASE_LENGTH);
		pack.setRnd(new TestRandom(0.5));
		lengthPlug.setExecutedThisCycle(false);
		pack = lengthPlug.process(pack);
		
		pack.setRnd(new TestRandom(chordsRandomSequence));
		plug.setExecutedThisCycle(false);
		pack = plug.process(pack);
		
		assertThat(repo.getRndValue()).containsExactly(new double[] {0.1, 0.7, 0.4, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7});	
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_repo_CHORD_LIST_GENERATOR_selected_options_will_have_indices_0_and_4() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		int[] array = repo.getSelectedValues();
		assertThat(array).isEqualTo(new int[] {0, 4});		
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_repo_CHORD_LIST_GENERATOR_selected_options_will_have_position_and_chords_0_C_1_G() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
//		JsonObject jsonObject = pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.get("selected_options").getAsJsonObject()
//				.get("position_and_chords").getAsJsonObject();
//		
//		
//		Map<String, String> map = new Gson().fromJson(jsonObject,Map.class);//.toArray(new double[jsonArray.size()]);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getFloatBarChordMap().containsKey(0.0)).isTrue();
		assertThat(repo.getFloatBarChordMap().containsKey(1.0)).isTrue();
		assertThat(repo.getFloatBarChordMap().get(0.0)).isEqualTo("C");
		assertThat(repo.getFloatBarChordMap().get(1.0)).isEqualTo("G");
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_json_CHORD_LIST_GENERATOR_will_have_correct_plugin_class_name() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
//		String jsonPlugInClassName = pack.getJson()
//				.get(Parameter.CHORD_LIST_GENERATOR.toString()).getAsJsonObject()
//				.get("plugin").getAsJsonObject()
//				.get("class_name").getAsString();
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_json_CHORD_LIST_GENERATOR_will_have_options_item() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		DiatonicTriadRepo repo = (DiatonicTriadRepo)pack.getRepo().get(Parameter.CHORD_LIST_GENERATOR);
		assertThat(repo.getOptions()).isNotNull();
	}
	

	@Test
	void when_passing_a_package_with_PHRASE_LENGTH_2_and_XMLKEY_0_then_mu_will_have_length_2() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(new double[] {0.05}));
		pack.setDebugMode(true);
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// xml key is 0 (key of C)
		pack.setRnd(new TestRandom(0.45));
		PooplinePlugin xmlkeyPlug = new XmlKeyRandom();
		pack = xmlkeyPlug.process(pack);
		
		double[] chordsRandomSequence = new double[] {0.1, 0.7, 0.1, 0.7, 0.1, 0.7, 0.4, 0.1, 0.7, 0.1, 0.7, 0.1};
		pack.setRnd(new TestRandom(chordsRandomSequence));
		PooplinePlugin plug = new DiatonicTriadProgressionRandom();
		pack = plug.process(pack);
		
		assertThat(pack.getMu().getChordListToString()).isEqualTo("C_G_");
	}
	
	

}
