package test.java.com.dougron.mucus_experiments.generator_pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class PipelinePackage_Tests {

	@Test
	void instantiates() {
		PipelinePackage pp = new PipelinePackage(new Random());
	}
	
	
	@Test
	void when_PipelinePackage_is_instantiated_then_mu_has_length_of_0_bars() throws Exception {
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.5));
		assertThat(pp.getMu().getLengthInBars()).isEqualTo(0);
	}
	
	
	@Test
	void when_PipelinePackage_is_instantiated_then_rndContainer_is_empty_json() throws Exception {
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.5));
//		pp = plug.process(pp);
		assertThat(pp.getRndContainer()).isExactlyInstanceOf(JSONObject.class);
		assertThat(pp.getRndContainer().length()).isEqualTo(0);
	}
	
	
	@Test
	void when_PipelinePackage_is_instantiated_then_rndContainerHas_returns_false_for_an_arbitrary_key() throws Exception {
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.5));
		assertThat(pp.rndContainerHas("poopy")).isFalse();
	}

}
