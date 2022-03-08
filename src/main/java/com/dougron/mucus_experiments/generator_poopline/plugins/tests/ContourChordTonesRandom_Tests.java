package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class ContourChordTonesRandom_Tests {

	@Test
	void instantiates() {
		ContourChordTonesRandom plug = new ContourChordTonesRandom();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_ContourChordTonesRandom_does_not_have_required_parameters_then_repo_does_not_acquire_STRUCTURE_TONE_CONTOUR_item() throws Exception {
		ContourChordTonesRandom plug = new ContourChordTonesRandom();
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_CONTOUR)).isFalse();
	}
	
	
	@Test
	void when_ContourChordTonesRandom_does_not_have_required_parameters_then_mu_structure_tones_do_not_get_notes() throws Exception {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		ContourChordTonesRandom plug = new ContourChordTonesRandom();
		pack = plug.process(pack);
		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(muList.get(0).getMuNotes().size()).isEqualTo(0);
	}
	
	
	@Test
	void when_ContourChordTonesRandom_does_have_required_parameters_then_repo_does_acquire_STRUCTURE_TONE_CONTOUR_item() throws Exception {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		TessituraFixed tessPlug = new TessituraFixed();
		pack = tessPlug.process(pack);
		StartNoteMelodyRandom startPlug = new StartNoteMelodyRandom();
		pack = startPlug.process(pack);
		ContourMultiplierRandom multPlug = new ContourMultiplierRandom();
		pack = multPlug.process(pack);
		ContourChordTonesRandom contourPlug = new ContourChordTonesRandom();
		pack = contourPlug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_CONTOUR)).isTrue();
	}

	
	@Test
	void when_ContourChordTonesRandom_does_have_required_parameters_then_mu_structure_tones_get_notes() throws Exception {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		TessituraFixed tessPlug = new TessituraFixed();
		pack = tessPlug.process(pack);
		StartNoteMelodyRandom startPlug = new StartNoteMelodyRandom();
		pack = startPlug.process(pack);
		ContourMultiplierRandom multPlug = new ContourMultiplierRandom();
		pack = multPlug.process(pack);
		ContourChordTonesRandom contourPlug = new ContourChordTonesRandom();
		pack = contourPlug.process(pack);
		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(muList.get(0).getMuNotes().size()).isNotEqualTo(0);
	}
	
	
	
	
// ------- privates -------------------------------------------------------------------

	private PooplinePackage getPackWithPitchFreeStructureTones() {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6
		// items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.55));

		// length 8 bars
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
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		pack.setRnd(new TestRandom(0.5));
		pack = plug.process(pack);

		return pack;
	}
	
	
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
				.selectedValue(-0.01)
				.rndValue(0.5)
				.rangeLow(-0.05)
				.rangeHigh(0.01)
				.className(PhraseBoundPercentRandom.class.getName())
				.build();
		return phraseStartRepo;
	}

}
