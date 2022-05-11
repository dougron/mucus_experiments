package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.FloatBarChordProgression;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.da_utils.combo_variables.IntAndInt;

class EmbellishmentSchema_Tests
{

	@Test
	void test()
	{
		Mu mu = getMu(60, BarsAndBeats.at(0,  0.0), 57, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(-2);
	}
	
	
	@Test
	void test2()
	{
		Mu mu = getMu(57, BarsAndBeats.at(0,  0.0), 60, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(2);
	}
	
	
	@Test
	void test3()
	{
		Mu mu = getMu(57, BarsAndBeats.at(0,  0.0), 59, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am", 1.0, "Bm7b5"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(1);
	}
	
	
	@Test
	void test4()
	{
		Mu mu = getMu(57, BarsAndBeats.at(0,  0.0), 60, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am", 1.0, "Bm7b5"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(2);
//		assertThat(spacing.i2).isEqualTo(1);
	}
	
	
	@Test
	void test5()
	{
		Mu mu = getMu(57, BarsAndBeats.at(0,  0.0), 61, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am", 1.0, "Bm7b5"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(2);
		assertThat(spacing.i2).isEqualTo(1);
	}
	
	
	@Test
	void test6()
	{
		Mu mu = getMu(60, BarsAndBeats.at(0,  0.0), 56, BarsAndBeats.at(1,  0.0), 2.0, new Object[] {0.0, "Am", 1.0, "E7"});
		Mu note1 = mu.getMu("note1");
		Mu note2 = mu.getMu("note2");
		IntAndInt spacing = EmbellishmentSchema.getDiatonicSpacing(note1.getTopPitch(), note1.getPrevailingChord(), note2.getTopPitch(), note2.getPrevailingChord());
		assertThat(spacing.i1).isEqualTo(-3);
		assertThat(spacing.i2).isEqualTo(0);
	}

	
	
	private Mu getMu(
			int pitch1, 
			BarsAndBeats position1, 
			int pitch2, 
			BarsAndBeats position2, 
			double lengthInFloatBars, 
			Object[] floatBarChordGeneratorArgument)
	{
		Mu mu = new Mu("parent");
		mu.setLengthInBars((int)lengthInFloatBars);
		mu.setChordListGenerator(new FloatBarChordProgression(lengthInFloatBars, floatBarChordGeneratorArgument));
		Mu mu1 = new Mu("note1");
		mu1.addMuNote(new MuNote(pitch1, 64));
		Mu mu2 = new Mu("note2");
		mu2.addMuNote(new MuNote(pitch2, 64));
		mu.addMu(mu1, position1);
		mu.addMu(mu2, position2);
		
		return mu;
	}

}
