package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class PhraseLengthRandom_Tests {

	@Test
	void instantiates() {
		PooplinePlugin plug = new PhraseLengthRandom();
	}
	 	
	
	@Test
	void when_passing_a_blank_package_to_RandomPhraseLength_then_repo_PHRASE_LENGTH_will_have_random_seed_withTestRandom_value_and_type_double() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		PhraseLengthRepo repo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		assertThat(repo.getRndValue()).isEqualTo(0.1);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomPhraseLength_then_repo_PHRASE_LENGTH_will_have_options_values_equal_to_RandomPhraseLength_options_and_type_int_array() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		PhraseLengthRepo repo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		int[] optionsArr = ((PhraseLengthRandom)plug).getOptions();
		assertThat(optionsArr).containsOnly(repo.getOptions());
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomPhraseLength_then_repo_PHRASE_LENGTH_will_have_selected_option_equal_to_4() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		PhraseLengthRepo repo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		assertThat(repo.getSelectedValue()).isEqualTo(4);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomPhraseLength_then_repo_PHRASE_LENGTH_will_have_plug_in_class_name_same_as_class_name_for_RandomPhraseLength() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		PhraseLengthRepo repo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}
	
	
	@Test
	void when_passing_a_blank_package_then_mu_will_have_length_of_4_bars_for_TestRandom_equal_to_0_2() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(4);
	}

	
	@Test
	void when_passing_a_package_with_existing_data_then_existing_data_will_not_be_overwritten() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new PhraseLengthRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		
		pack.setRnd(new TestRandom(0.9));
		
		pack = plug.process(pack);
		PhraseLengthRepo repo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);

		assertThat(repo.getRndValue()).isEqualTo(0.2);
		
		int[] optionsArr = ((PhraseLengthRandom)plug).getOptions();
		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		assertThat(optionsArr).containsOnly(repo.getOptions());
		
		// selected_version
		assertThat(repo.getSelectedValue()).isEqualTo(4);
		
		// plugin/class_name
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());

		// mu length remains
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(4);
	}
	
	
	
	private void addPooplineParent(PooplinePlugin plug)
	{
		Poopline p = new Poopline();
		plug.setParent(p);
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
