package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationFixedInQuarters;

class DurationFixedInQuarters_Tests
{

	@Test
	void when_plugin_is_initialized_with_an_array_of_durations_then_these_reflect_in_the_length_of_the_mus()
	{
		PooplinePackage pack = new PooplinePackage("XX", new Random());
		pack.setDebugMode(true);
		Mu mu = pack.getMu();
		mu.setLengthInBars(2);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(77, 88));
		mu.addMu(note1, BarsAndBeats.at(0, 0));
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(66, 77));
		mu.addMu(note2, BarsAndBeats.at(2, 0));
		
		DurationFixedInQuarters plug = new DurationFixedInQuarters(new double[] {1.0, 1.5}, Parameter.STRUCTURE_TONE_CONTOUR);
		pack = plug.process(pack);
		assertThat(pack.getMu().getMus().get(0).getLengthInQuarters()).isEqualTo(1.0);
		assertThat(pack.getMu().getMus().get(1).getLengthInQuarters()).isEqualTo(1.5);
	}

}
