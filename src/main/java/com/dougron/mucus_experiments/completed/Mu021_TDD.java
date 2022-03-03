package main.java.com.dougron.mucus_experiments.completed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.mu_generator.SuFi_Enclosure;
import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.SingleChordGenerator;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import timed_notes_and_controllers.TimedNote;

class Mu021_TDD
{
	
	Mu021_NoteLevelChordToneAndEmbellishment mutest; 
	int DEFAULT_CHORD_TONE_VELOCITY = 72;
	
	
	@BeforeEach	
	private void makeMuTest()
	{
		mutest = new Mu021_NoteLevelChordToneAndEmbellishment();
	}

	
	// chord tones from reference note
	
	@Nested
	class given_chord_of_C_major_and_reference_note_of
	{
		@Test
		void _61_then_getNearestChordTone_returns_60() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 61;
			assertEquals(60, mutest.getNearestChordTone(referenceNote, chord));
		}
		

		@Test
		void _63_then_getNearestChordTone_returns_64() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(64, mutest.getNearestChordTone(referenceNote, chord));
		}
		
		
		@Test
		void _63_then_getChordTone_plus1_returns_64() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(64, mutest.getChordTone(referenceNote, chord, 1));
		}
		
		
		@Test
		void _63_then_getChordTone_minus1_returns_60() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(60, mutest.getChordTone(referenceNote, chord, -1));
		}
		
		
		@Test
		void _63_then_getChordTone_plus2_returns_67() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(67, mutest.getChordTone(referenceNote, chord, 2));
		}
		
		
		@Test
		void _63_then_getChordTone_minus2_returns_55() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(55, mutest.getChordTone(referenceNote, chord, -2));
		}
		

		@Test
		void _63_then_getChordTone_0_returns_64() throws Exception
		{
			Chord chord = new Chord("C");
			int referenceNote = 63;
			assertEquals(64, mutest.getChordTone(referenceNote, chord, 0));
		}
	}
	

	@Test
	void test_short_test_output_for_phrase_with_one_note_at_0_bars() throws Exception
	{
		Mu phrase = new Mu("phrase");
		phrase.setChordListGenerator(new SingleChordGenerator());
		Mu chordTone = new Mu("chordTone");
		phrase.addMu(chordTone, 0);
		Chord prevailingChord = new Chord();
		int referenceNote = 50;
		int pitch = mutest.getNearestChordTone(referenceNote, prevailingChord);
		chordTone.addMuNote(new MuNote(pitch, DEFAULT_CHORD_TONE_VELOCITY));
		List<TimedNote> tnList = mutest.makeTimeNoteList(phrase);
		assertEquals("48_0.0_0.0_72", tnList.get(0).toShortString());
	}
	

	@Test
	void test_short_test_output_for_phrase_with_note_at_0_bars_and_note_at_0_bars_2_beats() throws Exception
	{
		Mu phrase = new Mu("phrase");
		phrase.setChordListGenerator(new SingleChordGenerator());
		Mu chordTone = new Mu("chordTone");
		phrase.addMu(chordTone, 0);
		Chord prevailingChord = new Chord();
		int referenceNote = 50;
		int pitch = mutest.getNearestChordTone(referenceNote, prevailingChord);
		chordTone.addMuNote(new MuNote(pitch, DEFAULT_CHORD_TONE_VELOCITY));
		Mu chordTone2 = new Mu("chordTone2");
		phrase.addMu(chordTone2, new BarsAndBeats(0, 2.0));
		chordTone2.addMuNote(new MuNote(pitch + 2, DEFAULT_CHORD_TONE_VELOCITY));
		
		List<TimedNote> tnList = mutest.makeTimeNoteList(phrase);
		assertEquals("48_0.0_0.0_72", tnList.get(0).toShortString());
		assertEquals("50_2.0_0.0_72", tnList.get(1).toShortString());
	}
	
	
	@Test
	void given_chord_tone_at_() throws Exception
	{
		Mu phrase = new Mu("phrase");
		phrase.setChordListGenerator(new SingleChordGenerator());
		Mu chordTone = new Mu("chordTone");
		phrase.addMu(chordTone, 0);
		Chord prevailingChord = new Chord();
		int referenceNote = 50;
		int pitch = mutest.getNearestChordTone(referenceNote, prevailingChord);
		chordTone.addMuNote(new MuNote(pitch, DEFAULT_CHORD_TONE_VELOCITY));
		SuFi sufi = new SuFi_Enclosure();
		chordTone.addSuFi(sufi);
		phrase.generate();
		
		List<TimedNote> tnList = mutest.makeTimeNoteList(phrase);
		assertEquals("48_0.0_0.0_72", tnList.get(0).toShortString());
	}
	
	

	
	
}
