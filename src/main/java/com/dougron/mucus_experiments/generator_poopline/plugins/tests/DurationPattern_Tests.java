package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern.EndNoteDurationSolution;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class DurationPattern_Tests {

	@Test
	void instantiates() {
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.LEGATO});
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void given_legato_model_when_two_notes_then_first_end_at_beginning_of_second() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
		TempoRandom tempoPlug = new TempoRandom();
		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, 2);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.LEGATO});
		pack = plug.process(pack);
		assertThat(note1.getLengthInQuarters()).isEqualTo(4.0);
	}
	
	
	@Test
	void given_legato_model_when_two_notes_then_second_ends_at_next_barline() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
		TempoRandom tempoPlug = new TempoRandom();
		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, 2);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.LEGATO});
		pack = plug.process(pack);
		assertThat(note2.getLengthInQuarters()).isEqualTo(4.0);
	}
	
	
	@Test
	void given_legato_model_when_last_note_is_a_syncopation_then_last_note_ends_at_2nd_next_barline() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
		TempoRandom tempoPlug = new TempoRandom();
		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, new BarsAndBeats(1, 3.5));
		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 8.0);
		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.LEGATO});
		pack = plug.process(pack);
		assertThat(note2.getLengthInQuarters()).isEqualTo(4.5);
	}
	
	
	@Test
	void given_legato_model_when_last_note_is_a_syncopation_that_is_in_its_original_bar_then_last_note_ends_at_next_barline() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
		TempoRandom tempoPlug = new TempoRandom();
		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, new BarsAndBeats(2, 1.5));
		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 10.0);
		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.LEGATO});
		pack = plug.process(pack);
		assertThat(note2.getLengthInQuarters()).isEqualTo(2.5);
	}
	
	
	@Test
	void given_staccato_model_when_no_tempo_is_present_all_notes_are_duration_0_25() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
//		TempoRandom tempoPlug = new TempoRandom();
//		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, new BarsAndBeats(2, 1.5));
		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 10.0);
		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.STACCATO});
		pack = plug.process(pack);
		assertThat(note1.getLengthInQuarters()).isEqualTo(0.25);
	}
	
	
	@Test
	void given_staccato_model_when_tempo_is_present_all_notes_are_low_duration() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
		TempoRandom tempoPlug = new TempoRandom();
		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, new BarsAndBeats(2, 1.5));
		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 10.0);
		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.STACCATO});
		pack = plug.process(pack);
		assertThat(note1.getLengthInQuarters()).isLessThan(0.25);
	}
	
	
	@Test
	void given_staccato_end_model_when_tempo_is_not_present_last_note_is_0_25_duration() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
//		TempoRandom tempoPlug = new TempoRandom();
//		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, new BarsAndBeats(2, 1.5));
		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 10.0);
		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.STACCATO});
		plug.setEndNoteSolution(EndNoteDurationSolution.STACCATO);
		pack = plug.process(pack);
		assertThat(note2.getLengthInQuarters()).isEqualTo(0.25);
	}
	
	
	@Test
	void given_staccato_legato_model_when_tempo_is_not_present_then_note1_duration_0_25_and_note2_duration_4() throws Exception {
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.4));
		pack.setDebugMode(true);
//		TempoRandom tempoPlug = new TempoRandom();
//		pack = tempoPlug.process(pack);
		Mu parent = pack.getMu();
		parent.setLengthInBars(4);
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(60, 64));
		parent.addMu(note1, 1);
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(64, 64));
		parent.addMu(note2, 2);
		Mu note3 = new Mu("note3");
		note3.addMuNote(new MuNote(64, 64));
		parent.addMu(note3, 3);
//		MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
//		bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, 10.0);
//		note2.addMuTagBundle(bundle);
		DurationPattern plug = new DurationPattern(new DurationType[] {DurationType.STACCATO, DurationType.LEGATO});
//		plug.setEndNoteSolution(EndNoteDurationSolution.STACCATO);
		pack = plug.process(pack);
		assertThat(note1.getLengthInQuarters()).isEqualTo(0.25);
		assertThat(note2.getLengthInQuarters()).isEqualTo(4.0);
	}

}
