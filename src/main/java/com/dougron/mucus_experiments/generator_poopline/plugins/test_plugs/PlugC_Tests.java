package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.test_plugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class PlugC_Tests {

	@Test
	void instantiates() {
		PooplinePlugin plug = new PlugC();
		assertThat(plug).isNotNull();
	}
	 
	
	@Test
	void when_passing_a_blank_package_then_json_will_get_the_list_of_rendered_parameters() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		for (Parameter p: plug.getRenderParameters()) {
			assertThat(json.has(p.toString())).isTrue();
		}
	}
	
	
	@Test
	void when_passing_a_blank_package_to_PlugC_then_json_PHRASE_LENGTH_will_have_random_seed_withTestRandom_value_and_type_double() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.get("PHRASE_LENGTH").getAsJsonObject();
		JsonObject randomSeed = phraseLength.get("random_seed").getAsJsonObject();
		assertThat(randomSeed.get("value").getAsDouble()).isEqualTo(0.1);
		assertThat(randomSeed.get("type").getAsString()).isEqualTo("double");
	}
	
	
	@Test
	void when_passing_a_blank_package_to_PlugC_then_json_PHRASE_LENGTH_will_have_options_values_equal_to_PlugC_options_and_type_int_array() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.get("PHRASE_LENGTH").getAsJsonObject();
		JsonObject options = phraseLength.get("options").getAsJsonObject();
		
		int[] optionsArr = ((PlugC)plug).getOptions();
		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		Integer[] jsonOptionArr = new Gson().fromJson(options.get("values").getAsJsonArray(), Integer[].class);
		List<Integer> jsonOptions = Arrays.stream(jsonOptionArr).toList();
		
		assertThat(plugOptions).hasSameElementsAs(jsonOptions);
		assertThat(options.get("type").getAsString()).isEqualTo("int_array");
	}
	
	
	@Test
	void when_passing_a_blank_package_to_PlugC_then_json_PHRASE_LENGTH_will_have_selected_option_equal_to_3() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.get("PHRASE_LENGTH").getAsJsonObject();
		assertThat(phraseLength.get("selected_option").getAsInt()).isEqualTo(3);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_PlugC_then_json_PHRASE_LENGTH_will_have_plug_in_class_name_same_as_class_name_for_PlugC() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.get("PHRASE_LENGTH").getAsJsonObject();
		assertThat(
				phraseLength.get("plugin").getAsJsonObject().get("class_name").getAsString()
				)
		.isEqualTo(plug.getClass().getName());
	}
	
	
	@Test
	void when_passing_a_blank_package_then_mu_will_have_length_of_3_bars_for_TestRandom_equal_to_0_2() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(3);
	}

	
	@Test
	void when_passing_a_package_with_existing_data_then_existing_data_will_not_be_overwritten() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PlugC();
		pack = plug.process(pack);
		
		pack.setRnd(new TestRandom(0.9));
		
		pack = plug.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.get("PHRASE_LENGTH").getAsJsonObject();
		
//		random_seed remains the same
		JsonObject randomSeed = pack.getJson().get("PHRASE_LENGTH").getAsJsonObject().get("random_seed").getAsJsonObject();
		assertThat(randomSeed.get("value").getAsDouble()).isEqualTo(0.2);
		assertThat(randomSeed.get("type").getAsString()).isEqualTo("double");
		
		// selected_option
		JsonObject options = phraseLength.get("options").getAsJsonObject();
		int[] optionsArr = ((PlugC)plug).getOptions();
		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		Integer[] jsonOptionArr = new Gson().fromJson(options.get("values").getAsJsonArray(), Integer[].class);
		List<Integer> jsonOptions = Arrays.stream(jsonOptionArr).toList();
		assertThat(plugOptions).hasSameElementsAs(jsonOptions);
		assertThat(options.get("type").getAsString()).isEqualTo("int_array");
		
		// selected_version
		assertThat(phraseLength.get("selected_option").getAsInt()).isEqualTo(3);
		
		// plugin/class_name
		assertThat(phraseLength.get("plugin").getAsJsonObject().get("class_name").getAsString())
		.isEqualTo(plug.getClass().getName());

		// mu length remains
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(3);
	}
	
	
//	
//	@Test
//	void when_required_parameters_are_not_fulfilled_then_this_is_the_effect_on_the_json() throws Exception {
//		
//	}
//	
//	@Test
//	void when_required_parameters_are_not_fulfilled_then_this_is_the_effect_on_the_mu() throws Exception {
//		
//	}
//	
//	@Test
//	void when_hasProcessed_is_false_then_plugin_will_process_blank_package() throws Exception {
//		
//	}
//	
//	@Test
//	void given_hasProcessed_is_false_when_after_blank_package_is_processed_then_hasProcessed_is_true() throws Exception {
//		
//	}
//	
//	@Test
//	void when_hasProcessed_is_true_then_plugin_will_not_process_blank_package() throws Exception {
//		
//	}
}
