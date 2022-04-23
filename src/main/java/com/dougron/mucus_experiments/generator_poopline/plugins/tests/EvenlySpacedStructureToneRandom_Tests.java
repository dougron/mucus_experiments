package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EvenlySpacedStructureToneRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class EvenlySpacedStructureToneRandom_Tests {

	@Test
	void instantiates() {
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_plug_receives_blank_package_then_STRUCTURE_TONE_SPACING_item_is_created_in_repo_with_random_seed_equal_to_TestRandom_value() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack.setDebugMode(true);
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		addPooplineParent(plug);
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_SPACING)).isTrue();
		EvenlySpacedStructureToneRepo repo = (EvenlySpacedStructureToneRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_SPACING);
		assertThat(repo.getRndValue()).isEqualTo(0.1);	
	}

	
	@Test
	void when_plug_receives_blank_package_then_Mu_does_not_conatin_any_children_with_tag_IS_STRUCTURE_TONE() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		pack = plug.process(pack);
		ArrayList<Mu> list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.size()).isEqualTo(0);	
	}
	
	
	@Test
	void when_pack_has_all_required_info_for_plug_to_proceed_then_Mu_has_children_with_tag_IS_STRUCTURE_TONE() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.1));
		pack.setDebugMode(true);
		PhraseLengthRepo phraseLengthRepo = getPhraseLengthRepo();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, phraseLengthRepo);
		TimeSignatureRepo timeSignatureRepo = getTimeSignatureRepo();
		pack.getRepo().put(Parameter.TIME_SIGNATURE, timeSignatureRepo);
		PhraseBoundRepo phraseStartRepo = getPhraseStartPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_START_PERCENT, phraseStartRepo);
		PhraseBoundRepo phraseEndRepo = getPhraseEndPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_END_PERCENT, phraseEndRepo);
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		pack = plug.process(pack);
		ArrayList<Mu> list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.size()).isGreaterThan(0);	
	}
	
	
	@Test
	void when_pack_has_all_required_info_for_plug_to_proceed_then_Mu_has_children_with_tag_IS_STRUCTURE_TONE_at_expected_positions() throws Exception {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6 items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.55));
		pack.setDebugMode(true);
		
		// length 8 bars
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// time signature 5/4
		PooplinePlugin timeSignaturePlug = new TimeSignatureSingleRandom();
		pack.setRnd(new TestRandom(0.7));
		pack = timeSignaturePlug.process(pack);
		
		// start and end percent can be faked without plugins as they have no effect on the mu, unlike phrase length and time signature
		PhraseBoundRepo phraseStartRepo = getPhraseStartPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_START_PERCENT, phraseStartRepo);
		PhraseBoundRepo phraseEndRepo = getPhraseEndPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_END_PERCENT, phraseEndRepo);
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		pack.setRnd(new TestRandom(0.5));
		pack = plug.process(pack);
		ArrayList<Mu> list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.size()).isEqualTo(6);
		assertThat(list.get(0).getGlobalPositionInQuarters()).isEqualTo(0.0);
		assertThat(list.get(1).getGlobalPositionInQuarters()).isEqualTo(5.0);
		assertThat(list.get(2).getGlobalPositionInQuarters()).isEqualTo(10.0);
		assertThat(list.get(3).getGlobalPositionInQuarters()).isEqualTo(15.0);
		assertThat(list.get(4).getGlobalPositionInQuarters()).isEqualTo(20.0);
		assertThat(list.get(5).getGlobalPositionInQuarters()).isEqualTo(25.0);
	}

	
	@Test
	void check_the_float_bar_quantisation() throws Exception {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6 items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.55));
		pack.setDebugMode(true);
		
		// length 8 bars
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);
		
		// time signature 5/4
		PooplinePlugin timeSignaturePlug = new TimeSignatureSingleRandom();
		pack.setRnd(new TestRandom(0.7));
		pack = timeSignaturePlug.process(pack);
		
		// start and end percent can be faked without plugins as they have no effect on the mu, unlike phrase length and time signature
		PhraseBoundRepo phraseStartRepo = getPhraseStartPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_START_PERCENT, phraseStartRepo);
		PhraseBoundRepo phraseEndRepo = getPhraseEndPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_END_PERCENT, phraseEndRepo);
		EvenlySpacedStructureToneRandom plug = new EvenlySpacedStructureToneRandom();
		pack.setRnd(new TestRandom(0.1));
		pack = plug.process(pack);
		ArrayList<Mu> list = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(list.size()).isEqualTo(12);
		assertThat(list.get(0).getGlobalPositionInQuarters()).isEqualTo(0.0);
		assertThat(list.get(1).getGlobalPositionInQuarters()).isEqualTo(3.0);
		assertThat(list.get(2).getGlobalPositionInQuarters()).isEqualTo(5.0);
		assertThat(list.get(3).getGlobalPositionInQuarters()).isEqualTo(8.0);
//		assertThat(list.get(4).getGlobalPositionInQuarters()).isEqualTo(20.0);
//		assertThat(list.get(5).getGlobalPositionInQuarters()).isEqualTo(25.0);
	}
	
	
	
// ------- privates -------------------------------------------------------------------
	
	private void addPooplineParent(PooplinePlugin plug)
	{
		Poopline p = new Poopline();
		p.addPlugin(new PhraseLengthRandom());
		p.addPlugin( new TimeSignatureSingleRandom());
		p.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_START_PERCENT));
		p.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_END_PERCENT));
		
		plug.setParent(p);
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


	private TimeSignatureRepo getTimeSignatureRepo() {
		TimeSignatureRepo timeSignatureRepo = TimeSignatureRepo.builder()
				.selectedValue(TimeSignature.FIVE_FOUR)
				.rndValue(0.5)
				.options(new TimeSignature[] {TimeSignature.FIVE_FOUR})
				.className(TimeSignatureSingleRandom.class.getName())
				.build();
		return timeSignatureRepo;
	}


	private PhraseLengthRepo getPhraseLengthRepo() {
		PhraseLengthRepo phraseLengthRepo = PhraseLengthRepo.builder()
				.selectedValue(8)
				.rndValue(0.5)
				.options(new int[] {8})
				.className(PhraseLengthRandom.class.getName())
				.build();
		return phraseLengthRepo;
	}

}
