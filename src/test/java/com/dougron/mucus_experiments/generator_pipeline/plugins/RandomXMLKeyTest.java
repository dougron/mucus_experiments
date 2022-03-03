package test.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomXMLKey;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class RandomXMLKeyTest {

	@Test
	void when_random_value_is_0_then_xml_key_equals_minus_5() {
		RandomXMLKey x = new RandomXMLKey();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0));
		pp = x.process(pp);
		assertThat(pp.getMu().getKeySignatureMap().getKey()).isEqualTo(-5);
	}
	
	
	@Test
	void when_random_value_is_0_5_then_xml_key_equals_0() {
		RandomXMLKey x = new RandomXMLKey();
		PipelinePackage pp = new PipelinePackage(new TestRandom(0.5));
		pp = x.process(pp);
		assertThat(pp.getMu().getKeySignatureMap().getKey()).isEqualTo(1);
	}

	
//	@Test
//	void when_no_random_generator_is_passes_then_hasRequiredResources_is_false() {
//		RandomXMLKey x = new RandomXMLKey();
//		PipelinePackage pp = new PipelinePackage(null);
//		assertThat(x.hasRequiredResources(pp)).isFalse();
//	}
	
	
	@Test
	void when_valid_random_generator_is_passes_then_hasRequiredResources_is_true() {
		RandomXMLKey x = new RandomXMLKey();
		PipelinePackage pp = new PipelinePackage(new Random());
		assertThat(x.hasRequiredResources(pp)).isTrue();
	}

}
