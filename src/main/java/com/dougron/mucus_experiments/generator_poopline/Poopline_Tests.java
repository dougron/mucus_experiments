package main.java.com.dougron.mucus_experiments.generator_poopline;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugA;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugB;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugC;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class Poopline_Tests {

	@Test
	void test() {
		Poopline pl = new Poopline();
		pl.setPrimaryPlugin(new PlugA());
		pl.addPlugin(new PlugB());
		PooplinePackage pp = pl.process(new PooplinePackage("one", new Random()));
		assertThat(pp.getRenderedParametersFromJson())
			.hasSameElementsAs(
				Arrays.asList(
						Parameter.PHRASE_LENGTH, 
						Parameter.CHORD_LIST_GENERATOR
						));
	}
	
	
	@Test
	void given_pipeline_with_primaryPlug_when_when_an_empty_package_is_passed_to_pipeline_then_package_rendered_parameters_are_same_as_plugin() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new Random());
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		assertThat(pack.getRenderedParametersFromJson())
			.hasSameElementsAs(Arrays.asList(pipe.getPrimaryPlugin().getRenderParameters()));
	}
	
	
	
	
	@Test
	void given_pipeline_with_primaryPlug_when_when_an_empty_package_is_passed_to_pipeline_then_package_json_contains_parameters_the_same_as_plugin() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new Random());
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		JsonObject json = pack.getJson();
//		System.out.println(pipe.getPrimaryPlugin().getRenderedParameters().length);
		for (Parameter parameter: pipe.getPrimaryPlugin().getRenderParameters()) {
			assertThat(json.has(parameter.toString())).isTrue();
		}
	}
	
		
	@Test
	void given_pipeline_with_primaryPlug_supplying_only_PHRASE_LENGTH_when_when_an_empty_package_is_passed_to_pipeline_then_package_json_PHRASE_LENGTH_item_contains_random_seed() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new Random());
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.getAsJsonObject("PHRASE_LENGTH");
		assertThat(phraseLength.has("random_seed")).isTrue();
	}
	
	
	@Test
	void given_pipeline_with_primaryPlug_supplying_only_PHRASE_LENGTH_when_when_an_empty_package_is_passed_to_pipeline_then_package_json_PHRASE_LENGTH_random_seed_item_contains_type() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new Random());
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.getAsJsonObject("PHRASE_LENGTH");
		JsonObject randomSeed = phraseLength.getAsJsonObject("random_seed");
		assertThat(randomSeed.has("type")).isTrue();
	}
	
	
	@Test
	void given_pipeline_with_primaryPlug_supplying_only_PHRASE_LENGTH_when_when_an_empty_package_is_passed_to_pipeline_then_package_json_PHRASE_LENGTH_random_seed_item_contains_type_equal_to_double() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new TestRandom(0.55));
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.getAsJsonObject("PHRASE_LENGTH");
		JsonObject randomSeed = phraseLength.getAsJsonObject("random_seed");
		System.out.println(json);
		assertThat(randomSeed.get("type").getAsString()).isEqualTo("double");
	}
	
	
	@Test
	void given_pipeline_with_primaryPlug_supplying_only_PHRASE_LENGTH_when_when_an_empty_package_is_passed_to_pipeline_then_package_json_PHRASE_LENGTH_random_seed_item_contains_value_passed_by_TestRandom() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new TestRandom(0.55));
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		JsonObject json = pack.getJson();
		JsonObject phraseLength = json.getAsJsonObject("PHRASE_LENGTH");
		JsonObject randomSeed = phraseLength.getAsJsonObject("random_seed");
//		System.out.println(json);
		assertThat(randomSeed.get("value").getAsDouble()).isEqualTo(0.55);
	}
	
	
	@Test
	void given_pipeline_with_primaryPlug_supplying_only_PHRASE_LENGTH_when_when_an_package_with_PHRASE_LENGTH_is_passed_to_pipeline_then_package_json_PHRASE_LENGTH_random_seed_value_is_not_changed() throws Exception {
		PooplinePackage pack = new PooplinePackage("package_name", new TestRandom(0.55));
		Poopline pipe = new Poopline();
		pipe.setPrimaryPlugin(new PlugC());
		pack = pipe.process(pack);
		
		
		Poopline pipe2 = new Poopline();
		pipe2.setPrimaryPlugin(new PlugC());
		pack.setRnd(new TestRandom(0.33));
		pack = pipe2.process(pack);
		
		JsonObject json = pack.getJson()
				.getAsJsonObject("PHRASE_LENGTH")
				.getAsJsonObject("random_seed");
//		System.out.println(json);
		assertThat(json.get("value").getAsDouble()).isEqualTo(0.55);
	}
	
	
	@Test
	void given_empty_pipeline_when_a_package_with_parameter_and_plugin_name_but_no_random_seed_value_then_pipeline_will_load_that_plugin() throws Exception {
		JsonObject json = getJsonObjectWithOnlyPluginClassName();
		Poopline pipe = new Poopline();
		PooplinePackage pp = new PooplinePackage("one", new TestRandom(0.66));
		pp.addItemToJson(Parameter.PHRASE_LENGTH.toString(), json);
		pp = pipe.process(pp);
		@SuppressWarnings("rawtypes")
		List<Class> classList = pipe.getPlugins().stream()
				.map(e -> e.getClass())
				.collect(Collectors.toList());
		assertThat(classList).hasSameElementsAs(Arrays.asList(new Class[] {new PlugC().getClass()}));
	}


	private JsonObject getJsonObjectWithOnlyPluginClassName() {
		JsonObject phraseLength = new JsonObject();
		JsonObject plugin = new JsonObject();
		plugin.addProperty("class_name", new PlugC().getClass().getName());
		phraseLength.add("plugin", plugin);
		return phraseLength;
	}
	
	
	@Test
	void given_a_pipeline_with_a_PHRASE_LENGTH_plugin_when_a_package_with_json_PHRASE_LENGTH_with_no_random_seed_but_another_plugin_name_then_that_plugin_will_be_loaded_and_the_existing_one_will_be_sorted_to_the_end() throws Exception {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
