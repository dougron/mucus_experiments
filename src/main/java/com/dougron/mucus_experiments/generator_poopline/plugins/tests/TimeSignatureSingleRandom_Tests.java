package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class TimeSignatureSingleRandom_Tests {

	@Test
	void instantiates() {
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_passing_a_blank_package_to_TimeSignatureSingleRandom_then_repo_TIME_SIGNATURE_will_have_random_seed_withTestRandom_value_and_type_double() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		TimeSignatureRepo repo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
		assertThat(repo.getRndValue()).isEqualTo(0.1);
	}

	
	@Test
	void when_passing_a_blank_package_to_TimeSignatureSingleRandom_then_repo_TIME_SIGNATURE_will_have_options_values_equal_to_TimeSignatureSingleRandom_options_and_type_int_array() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		TimeSignatureRepo repo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
		TimeSignature[] optionsArr = ((TimeSignatureSingleRandom)plug).getOptions();
		assertThat(optionsArr).containsOnly(repo.getOptions());
	}
	
	
	@Test
	void when_passing_a_blank_package_to_TimeSignatureSingleRandom_then_repo_TIME_SIGNATURE_will_have_selected_option_equal_to_4() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		TimeSignatureRepo repo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
		assertThat(repo.getSelectedValue().getName()).isEqualTo("7/8_223");
	}
	
	
	@Test
	void when_passing_a_blank_package_to_TimeSignatureSingleRandom_then_repo_TIME_SIGNATURE_will_have_plug_in_class_name_same_as_class_name_for_TimeSignatureSingleRandom() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		TimeSignatureRepo repo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}

	
	@Test
	void when_passing_a_blank_package_then_mu_will_have_time_signature_of_7_8_for_TestRandom_equal_to_0_2() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		assertThat(pack.getMu().getTimeSignature(0).getName()).isEqualTo("7/8_223");
	}
	
	

	
	@Test
	void when_passing_a_package_with_existing_data_then_existing_data_will_not_be_overwritten() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new TimeSignatureSingleRandom();
		pack = plug.process(pack);
		
		pack.setRnd(new TestRandom(0.9));
		
		pack = plug.process(pack);
		TimeSignatureRepo repo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);

		assertThat(repo.getRndValue()).isEqualTo(0.2);
		
		TimeSignature[] optionsArr = ((TimeSignatureSingleRandom)plug).getOptions();
//		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		assertThat(optionsArr).containsOnly(repo.getOptions());
		
		// selected_version
		assertThat(repo.getSelectedValue().getName()).isEqualTo("7/8_223");
		
		// plugin/class_name
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());

		// mu timesignature remains
		assertThat(pack.getMu().getTimeSignature(0).getName()).isEqualTo("7/8_223");
	}
	
}
