package test.java.com.dougron.mucus_experiments.generator_pipeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus_experiments.generator_pipeline.GeneratorPipeline;
import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomPhraseLengthInBars;

class GeneratorPipeline_Tests {

	@Test
	void instantiates() {
		GeneratorPipeline gp = GeneratorPipeline.builder().build();
	}
	
	
	@Test
	void when_pipeline_has_no_plugins_then_returned_PipelinePackage_has_random() throws Exception {
		GeneratorPipeline gp = GeneratorPipeline.builder().build();
		Random rnd = new Random();
		PipelinePackage pp = gp.getPackage(rnd);
		assertThat(pp.getRnd()).isEqualTo(rnd);
	}
	
	
	@Test
	void when_pipeline_has_no_plugins_then_returned_PipelinePackage_has_empty_rndContainer() throws Exception {
		GeneratorPipeline gp = GeneratorPipeline.builder().build();
		Random rnd = new Random();
		PipelinePackage pp = gp.getPackage(rnd);
		assertThat(pp.getRndContainer().length()).isEqualTo(0);
	}
	
	
	@Test
	void when_pipeline_has_no_plugins_then_returned_PipelinePackage_has_mu_with_length_of_0_bars() throws Exception {
		GeneratorPipeline gp = GeneratorPipeline.builder().build();
		Random rnd = new Random();
		PipelinePackage pp = gp.getPackage(rnd);
		assertThat(pp.getMu().getLengthInBars()).isEqualTo(0);
	}
	
	
	@Test
	void when_pipeline_has_phrase_length_plug_in_then_returned_PipelinePackage_rndContainer_has_phrase_length_item() throws Exception {
		GeneratorPipeline gp = GeneratorPipeline.builder()
				.plug(new RandomPhraseLengthInBars())
				.build();
		Random rnd = new Random();
		PipelinePackage pp = gp.getPackage(rnd);
		assertThat(pp.rndContainerHas("phrase_length")).isTrue();	
	}
	
	
	@Test
	void when_pipeline_has_phrase_length_plug_in_then_returned_PipelinePackage_mu_has_length_in_bars_greater_than_1() throws Exception {
		GeneratorPipeline gp = GeneratorPipeline.builder()
				.plug(new RandomPhraseLengthInBars())
				.build();
		Random rnd = new Random();
		PipelinePackage pp = gp.getPackage(rnd);
		assertThat(pp.getMu().getLengthInBars()).isGreaterThan(1);
	}

}
