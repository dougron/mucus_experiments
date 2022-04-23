package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class StartNoteMelodyRandom_Tests {

	@Test
	void instantiates() {
		StartNoteMelodyRandom plug = new StartNoteMelodyRandom	();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_StartNoteMelodyRandom_does_not_have_required_parameters_then_pack_repo_does_not_acquire_START_NOTE_item() throws Exception {
		StartNoteMelodyRandom plug = new StartNoteMelodyRandom	();
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.0));
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.START_NOTE)).isFalse();
	}
	
	
	@Test
	void when_StartNoteMelodyRandom_does_have_required_parameters_then_pack_repo_does_acquire_START_NOTE_item() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.0));
		pack.setDebugMode(true);
		TessituraFixed tessituraPlug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		pack = tessituraPlug.process(pack);
		StartNoteMelodyRandom plug = new StartNoteMelodyRandom	();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.START_NOTE)).isTrue();
	}
	
	
	@Test
	void when_pack_rand_is_0_then_StartNoteMelodyRandom_returns_lowValue_of_42() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.0));
		pack.setDebugMode(true);
		TessituraFixed tessituraPlug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		pack = tessituraPlug.process(pack);
		StartNoteMelodyRandom plug = new StartNoteMelodyRandom();
		pack = plug.process(pack);
		StartNoteRepo repo = (StartNoteRepo)pack.getRepo().get(Parameter.START_NOTE);
		assertThat(repo.getSelectedValue()).isEqualTo(42);
	}

}
