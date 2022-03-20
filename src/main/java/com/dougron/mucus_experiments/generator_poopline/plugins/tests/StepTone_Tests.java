package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.mu_generator.enums.ChordToneType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordListGeneratorFactory;
import main.java.com.dougron.mucus.mu_framework.chord_list.FloatBarChordProgression;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.StepTone;



class StepTone_Tests {

	@Test
	void given_embellishment_with_no_MuNotes_when_passed_to_StepTone_then_MuNote_is_added() {
		Mu parent = new Mu("parent");
		Mu note = new Mu("note");
		parent.addMu(note, 1);
		note.addMuNote(new MuNote(60, 64));
		Mu embellishment = new Mu("emb");
		note.addMu(embellishment, -1.0);
		StepTone stepTone = new StepTone(ChordToneType.CLOSEST_ABOVE, 1);
		stepTone.addNotes(embellishment);
		assertThat(embellishment.getMuNotes().size()).isEqualTo(1);
	}

	
	@Test
	void given_embellishment_with_no_MuNotes_and_parent_pitch_of_60_and_key_of_F_when_passed_to_StepTone_1_step_above_then_MuNote_pitch_is_62() {
		Mu parent = new Mu("parent");
		parent.setLengthInBars(2);
		parent.setChordListGenerator(new FloatBarChordProgression(2.0, new Object[] {0.0, "C", 1.0, "F"}));
		Mu note = new Mu("note");
		parent.addMu(note, 1);
		note.addMuNote(new MuNote(60, 64));
		Mu embellishment = new Mu("emb");
		note.addMu(embellishment, -1.0);
		StepTone stepTone = new StepTone(ChordToneType.CLOSEST_ABOVE, 1);
		stepTone.addNotes(embellishment);
		assertThat(embellishment.getMuNotes().get(0).getPitch()).isEqualTo(62);
	}
	
	
	@Test
	void given_embellishment_with_no_MuNotes_and_parent_pitch_of_60_and_key_of_C_when_passed_to_StepTone_1_step_below_then_MuNote_pitch_is_59() {
		Mu parent = new Mu("parent");
		parent.setLengthInBars(2);
		parent.setChordListGenerator(new FloatBarChordProgression(2.0, new Object[] {0.0, "C", 1.0, "G"}));
		Mu note = new Mu("note");
		parent.addMu(note, 1);
		note.addMuNote(new MuNote(60, 64));
		Mu embellishment = new Mu("emb");
		note.addMu(embellishment, -1.0);
		StepTone stepTone = new StepTone(ChordToneType.CLOSEST_BELOW, 1);
		stepTone.addNotes(embellishment);
		assertThat(embellishment.getMuNotes().get(0).getPitch()).isEqualTo(59);
	}
	
	
	@Test
	void given_embellishment_with_no_MuNotes_and_parent_pitch_of_60_and_key_of_F_when_passed_to_StepTone_2_step_above_then_MuNote_pitch_is_64() {
		Mu parent = new Mu("parent");
		parent.setLengthInBars(2);
		parent.setChordListGenerator(new FloatBarChordProgression(2.0, new Object[] {0.0, "C", 1.0, "F"}));
		Mu note = new Mu("note");
		parent.addMu(note, 1);
		note.addMuNote(new MuNote(60, 64));
		Mu embellishment = new Mu("emb");
		note.addMu(embellishment, -1.0);
		StepTone stepTone = new StepTone(ChordToneType.CLOSEST_ABOVE, 2);
		stepTone.addNotes(embellishment);
		assertThat(embellishment.getMuNotes().get(0).getPitch()).isEqualTo(64);
	}
	
	
	@Test
	void given_embellishment_with_no_MuNotes_and_parent_pitch_of_60_and_key_of_C_when_passed_to_StepTone_2_step_below_then_MuNote_pitch_is_57() {
		Mu parent = new Mu("parent");
		parent.setLengthInBars(2);
		parent.setChordListGenerator(new FloatBarChordProgression(2.0, new Object[] {0.0, "C", 1.0, "G"}));
		Mu note = new Mu("note");
		parent.addMu(note, 1);
		note.addMuNote(new MuNote(60, 64));
		Mu embellishment = new Mu("emb");
		note.addMu(embellishment, -1.0);
		StepTone stepTone = new StepTone(ChordToneType.CLOSEST_BELOW, 2);
		stepTone.addNotes(embellishment);
		assertThat(embellishment.getMuNotes().get(0).getPitch()).isEqualTo(57);
	}

}
