package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class TessituraFixed_Tests {

	@Test
	void instantiates() {
		TessituraFixed plug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_nothing_is_unusual_then_pack_repo_acquires_TESSITURA_item() throws Exception {
		TessituraFixed plug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.TESSITURA_START_NOTE)).isTrue();
	}
	
	
	@Test
	void when_nothing_is_unusual_then_pack_repo_TESSITURA_item_has_lowValue_of_42_and_highValue_of_70() throws Exception {
		TessituraFixed plug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack = plug.process(pack);
		TessituraRepo repo = (TessituraRepo)pack.getRepo().get(Parameter.TESSITURA_START_NOTE);
		assertThat(repo.getLowValue()).isEqualTo(42);
		assertThat(repo.getHighValue()).isEqualTo(70);
	}
	
	
	@Test
	void when_TessituraFixed_is_instantiated_with_new_values_then_pack_repo_TESSITURA_item_has_these_values() throws Exception {
		int lowValue = 36;
		int highValue = 88;
		
		TessituraFixed plug = new TessituraFixed(Parameter.TESSITURA_START_NOTE, lowValue, highValue);
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack = plug.process(pack);
		TessituraRepo repo = (TessituraRepo)pack.getRepo().get(Parameter.TESSITURA_START_NOTE);
		assertThat(repo.getLowValue()).isEqualTo(lowValue);
		assertThat(repo.getHighValue()).isEqualTo(highValue);
	}

}
