package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class StructureToneSyncopationInQuartersRandom_Tests {

	@Test
	void test() {
		StructureToneSyncopatorInQuartersRandom repo = new StructureToneSyncopatorInQuartersRandom();
		assertThat(repo).isNotNull();
	}

	@Test
	void testName() throws Exception {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6
		// items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.01));
		pack.setDebugMode(true);

		// length 2 bars
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);

		// time signature 5/4
		PooplinePlugin timeSignaturePlug = new TimeSignatureSingleRandom();
		pack.setRnd(new TestRandom(0.7));
		pack = timeSignaturePlug.process(pack);

		// start and end percent can be faked without plugins as they have no effect on
		// the mu, unlike phrase length and time signature
		PhraseBoundRepo phraseStartRepo = getPhraseStartPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_START_PERCENT, phraseStartRepo);
		PhraseBoundRepo phraseEndRepo = getPhraseEndPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_END_PERCENT, phraseEndRepo);
		StructureToneEvenlySpacedRandom structureTonePlug = new StructureToneEvenlySpacedRandom();
		pack.setRnd(new TestRandom(0.5));
		pack = structureTonePlug.process(pack);
		ArrayList<Mu> list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.size()).isEqualTo(1);
		
		pack.setRnd(new TestRandom(0.1));
		ShouldIUseTheStructureToneSyncopator shouldIPlug = new ShouldIUseTheStructureToneSyncopator();
		pack = shouldIPlug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR));
		
		StructureToneSyncopatorInQuartersRandom syncPlug 
		= new StructureToneSyncopatorInQuartersRandom(new double[] {-0.5}, new int[][] {new int[] {0}});
		pack = syncPlug.process(pack);
		list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.get(0).getGlobalPositionInQuarters()).isEqualTo(4.5);
	}


	
	
// ------- privates -------------------------------------------------------------------

	private PhraseBoundRepo getPhraseEndPercentRepo() {
		PhraseBoundRepo phraseEndRepo = PhraseBoundRepo.builder()
				.selectedValue(0.75)
				.rndValue(0.75)
				.rangeLow(0.0)
				.rangeHigh(1.0)
				.className(PhraseBoundPercentRandom.class.getName())
				.build();
		return phraseEndRepo;
	}

	private PhraseBoundRepo getPhraseStartPercentRepo() {
		PhraseBoundRepo phraseStartRepo = PhraseBoundRepo.builder()
				.selectedValue(0.25)
				.rndValue(0.25)
				.rangeLow(0.0)
				.rangeHigh(1.0)
				.className(PhraseBoundPercentRandom.class.getName())
				.build();
		return phraseStartRepo;
	}

}
