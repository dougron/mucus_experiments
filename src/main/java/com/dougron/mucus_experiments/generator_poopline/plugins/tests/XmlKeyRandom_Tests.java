package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class XmlKeyRandom_Tests {

	@Test
	void instantiates() {
		PooplinePlugin plug = new XmlKeyRandom();
		assertThat(plug).isNotNull();
	}
	 
	
	@Test
	void when_passing_a_blank_package_then_json_will_get_the_list_of_rendered_parameters() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		for (Parameter p: plug.getRenderParameters()) {
			assertThat(pack.getRepo().containsKey(p)).isTrue();
		}
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomXMLKey_then_json_XMLKEY_will_have_random_seed_withTestRandom_value_and_type_double() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		XmlKeyRepo repo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		assertThat(repo.getRndValue()).isEqualTo(0.1);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomXMLKey_then_json_XMLKEY_will_have_options_values_equal_to_RandomXMLKey_options_and_type_int_array() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		
		XmlKeyRepo repo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		int[] optionsArr = ((XmlKeyRandom)plug).getOptions();
		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		List<Integer> jsonOptions = Arrays.stream(repo.getOptions()).boxed().toList();
		
		assertThat(plugOptions).hasSameElementsAs(jsonOptions);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomXMLKey_then_json_XMLKEY_will_have_selected_option_equal_to_minus_3() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		XmlKeyRepo repo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		assertThat(repo.getSelectedValue()).isEqualTo(-3);
	}
	
	
	@Test
	void when_passing_a_blank_package_to_RandomXMLKey_then_json_XMLKEY_will_have_plug_in_class_name_same_as_class_name_for_RandomXMLKey() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		XmlKeyRepo repo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}
	
	
	@Test
	void when_passing_a_blank_package_then_mu_will_have_xml_key_of_0_for_TestRandom_equal_to_0_2() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.45));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		assertThat(pack.getMu().getKeySignatureMap().getKey(0)).isEqualTo(0);
	}

	
	@Test
	void when_passing_a_package_with_existing_data_then_existing_data_will_not_be_overwritten() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.2));
		PooplinePlugin plug = new XmlKeyRandom();
		pack = plug.process(pack);
		
		pack.setRnd(new TestRandom(0.9));
		
		pack = plug.process(pack);
		XmlKeyRepo repo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
		assertThat(repo.getRndValue()).isEqualTo(0.2);
		
		int[] optionsArr = ((XmlKeyRandom)plug).getOptions();
		List<Integer> plugOptions = Arrays.stream(optionsArr).boxed().toList();
		List<Integer> jsonOptions = Arrays.stream(repo.getOptions()).boxed().toList();
		assertThat(plugOptions).hasSameElementsAs(jsonOptions);
		
		// selected_version
		assertThat(repo.getSelectedValue()).isEqualTo(-3);
		
		// plugin/class_name
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());

		// mu length remains
		assertThat(pack.getMu().getKeySignatureMap().getKey(0)).isEqualTo(-3);
	}
	
	
	
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
