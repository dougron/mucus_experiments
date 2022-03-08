package test.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.GeneratorPipelinePlugIn;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomPhraseLengthInBars;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class RandomPhraseLengthInBars_Tests {

	@Test
	void instantiates() {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
	}
	
	
	@Test
	void when_test_value_for_random_is_0_5_then_mu_has_length_of_7() throws Exception {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.5));
//		System.out.println(pp);
		pp = plug.process(pp);
//		System.out.println(pp);
		assertThat(pp.getMu().getLengthInBars()).isEqualTo(7);
	}
	
	
	@Test
	void when_test_value_for_random_is_0_then_mu_has_length_of_2() throws Exception {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.0));
		pp = plug.process(pp);
		assertThat(pp.getMu().getLengthInBars()).isEqualTo(2);
	}
	
	
	@Test
	void when_test_value_for_random_is_0_999_then_mu_has_length_of_12() throws Exception {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.999));
		pp = plug.process(pp);
		assertThat(pp.getMu().getLengthInBars()).isEqualTo(12);
	}
	
	
	// currently this can't fail as the PipelinePackage has to instantiate with the Random 
	// in place, which is the criteria to pass
	@Test
	void when_PipelinePackage_is_default_then_hasRequiredResources_returns_true() throws Exception {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.999));
		assertThat(plug.hasRequiredResources(pp)).isTrue();
	}
	
	
	@Test
	void when_test_value_for_random_is_0_2_then_rndContainer_phrase_length_value_is_0_2() throws Exception {
		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.2));
		pp = plug.process(pp);
		assertThat(pp.rndContainerHas("phrase_length")).isTrue();
		JSONObject phraseLength = pp.getRndContainer().getJSONObject("phrase_length");
		JSONObject randomSeed = phraseLength.getJSONObject("random_seed");
		assertThat(randomSeed.get("value")).isEqualTo(0.2);
	}
	
	
//	@Test
//	void when_a_phrase_length_is_generated_then_the_rndContainer_contains_an_item_options_in_item_phrase_length_which_is_a_JSONArray() throws Exception {
//		GeneratorPipelinePlugIn plug = new RandomPhraseLengthInBars();
//		PipelinePackage pp = new PipelinePackage(new TestRandom(0.2));
//		pp = plug.process(pp);
////		assertThat(pp.rndContainerHas("phrase_length")).isTrue();
////		assertThat(pp.getRndContainer().getJSONObject("phrase_length").getJSONArray("options")).isExactlyIn;
//		JSONObject phraseLength = pp.getRndContainer().getJSONObject("phrase_length");
//		JSONObject options = phraseLength.getJSONObject("options");
//		assertThat(options.get("option_list")).isInstanceOf(JSONArray.class);
//	}

}
