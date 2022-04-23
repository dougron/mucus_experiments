package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
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
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class TessituraSolverOneBreakPointRandom_Tests {

	@Test
	void instantiates() {
		TessituraSolverOneBreakpointRandom plug 
		= new TessituraSolverOneBreakpointRandom(
				Parameter.TESSITURA_MELODY_RANGE,
				MuTag.IS_STRUCTURE_TONE
				);
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_melody_is_in_TESSITURA_MELODY_RANGE_then_TESSITURA_SOLVER_is_created_in_repo() throws Exception {
		PooplinePackage pack = addMelodyContent();
		TessituraFixed melodyRangePlug = new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE, 40, 90);
		pack = melodyRangePlug.process(pack);
		TessituraSolverOneBreakpointRandom tessPlug = new TessituraSolverOneBreakpointRandom(Parameter.TESSITURA_MELODY_RANGE, MuTag.IS_STRUCTURE_TONE);
		pack = tessPlug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.TESSITURA_SOLVER)).isTrue();
	}

	
	@Test
	void when_melody_is_in_TESSITURA_MELODY_RANGE_then_mu_notes_are_not_changed() throws Exception {
		PooplinePackage pack = addMelodyContent();
		List<Integer> initialPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
		TessituraFixed melodyRangePlug = new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE, 40, 90);
		pack = melodyRangePlug.process(pack);
		TessituraSolverOneBreakpointRandom tessPlug = new TessituraSolverOneBreakpointRandom(Parameter.TESSITURA_MELODY_RANGE, MuTag.IS_STRUCTURE_TONE);
		pack = tessPlug.process(pack);
		List<Integer> finalPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
		assertThat(finalPitchList).hasSameElementsAs(initialPitchList);
	}
	
	
	@Test
	void when_melody_is_not_in_TESSITURA_MELODY_RANGE_and_breakPointChoice_is_FIRST_then_mu_notes_are_changed_from_index_0() throws Exception {
		PooplinePackage pack = addMelodyContent();
		List<Integer> initialPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
		TessituraFixed melodyRangePlug = new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE, 40, 60);
		pack = melodyRangePlug.process(pack);
		
		pack.setRnd(new TestRandom(0.1));
		TessituraSolverOneBreakpointRandom tessPlug = new TessituraSolverOneBreakpointRandom(Parameter.TESSITURA_MELODY_RANGE, MuTag.IS_STRUCTURE_TONE);
		pack = tessPlug.process(pack);
		List<Integer> finalPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
//		List<Integer> expectedList = Arrays.asList(55,60,52,55,60,64);
		assertThat(finalPitchList).containsExactly(43,48,52,55,55,60);
	}
	
	
	@Test
	void when_melody_is_not_in_TESSITURA_MELODY_RANGE_and_breakPointChoice_is_LAST_then_mu_notes_are_changed_from_index_2() throws Exception {
		PooplinePackage pack = addMelodyContent();
		List<Integer> initialPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
		TessituraFixed melodyRangePlug = new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE, 40, 60);
		pack = melodyRangePlug.process(pack);
		
		pack.setRnd(new TestRandom(0.5));
		TessituraSolverOneBreakpointRandom tessPlug = new TessituraSolverOneBreakpointRandom(Parameter.TESSITURA_MELODY_RANGE, MuTag.IS_STRUCTURE_TONE);
		pack = tessPlug.process(pack);
		List<Integer> finalPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
//		List<Integer> expectedList = Arrays.asList(55,60,52,55,60,64);
		assertThat(finalPitchList).containsExactly(55,60,52,55,55,60);
	}
	
	
	@Test
	void when_melody_is_not_in_TESSITURA_MELODY_RANGE_and_breakPointChoice_is_RANDOM_then_mu_notes_are_changed_from_index_1_when_rndValueForRandom_is_set_correct_as_2nd_value_of_testRandom() throws Exception {
		PooplinePackage pack = addMelodyContent();
		List<Integer> initialPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
		TessituraFixed melodyRangePlug = new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE, 40, 60);
		pack = melodyRangePlug.process(pack);
		
		pack.setRnd(new TestRandom(new double[] {0.9, 0.5}));
		TessituraSolverOneBreakpointRandom tessPlug = new TessituraSolverOneBreakpointRandom(Parameter.TESSITURA_MELODY_RANGE, MuTag.IS_STRUCTURE_TONE);
		pack = tessPlug.process(pack);
		List<Integer> finalPitchList = getPitchList(pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE));
//		List<Integer> expectedList = Arrays.asList(55,60,52,55,60,64);
		assertThat(finalPitchList).containsExactly(55,48,52,55,55,60);
	}
	
	
	
	
	
// ---------privates------------------------------------------------------	
	
	private List<Integer> getPitchList(ArrayList<Mu> muList) {
		List<Integer> list = new ArrayList<Integer>();
		for (Mu mu: muList) {
			list.add(mu.getTopPitch());
		}
		return list;
	}


	private PooplinePackage addMelodyContent() {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		
		TessituraFixed tessPlug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		pack = tessPlug.process(pack);
		StartNoteMelodyRandom startPlug = new StartNoteMelodyRandom();
		pack = startPlug.process(pack);
		
		pack.setRnd(new TestRandom(0.8));
		ContourMultiplierRandom multPlug = new ContourMultiplierRandom(2, 24);
		pack = multPlug.process(pack);
		
		pack.setRnd(new TestRandom(0.1));
		ContourChordTonesRandom contourPlug = new ContourChordTonesRandom();
		pack = contourPlug.process(pack);
//		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		return pack;
	}
	
	
	private PooplinePackage getPackWithPitchFreeStructureTones() {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6
		// items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.55));
		pack.setDebugMode(true);
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
