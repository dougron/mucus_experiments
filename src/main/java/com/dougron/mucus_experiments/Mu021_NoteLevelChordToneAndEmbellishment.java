package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_Enclosure;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import timed_notes_and_controllers.TimedNote;


/*
 * test class for a single chord tone and embellishment. has related junit (Mu021_TDD) to test functionality
 * 
 * as of 19 Dec 2020 working with Sufi_Enclosure, but could be used as a test bed for any accent.
 */
public class Mu021_NoteLevelChordToneAndEmbellishment
{
	
	String fileName = "Mu021_NoteLevelChordToneAndEmbellishment";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	
	
	
	public Mu021_NoteLevelChordToneAndEmbellishment()
	{
		Mu mu = new Mu("mu");
		Mu chordTone = new Mu("chordtone");
		mu.addMu(chordTone, 1);
		chordTone.addMuNote(new MuNote(50, 72));
		chordTone.setLengthInQuarters(1.5);
		chordTone.addSuFi(new SuFi_Enclosure(SuFi.AccentType.ACCENTED));
		mu.generate();
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	

	public int getNearestChordTone(int referenceNote, Chord chord)
	{
		int distance = 128; 
		ArrayList<Integer> sortedChordTones = getSortedChordTones(chord);
		int oldNote = -1;
		int octave = 0;
		while (true)
		{
			for (int i: sortedChordTones)
			{
				int note = octave + i;
				int tempDistance = Math.abs(note - referenceNote);
				if (tempDistance < distance)
				{
					distance = tempDistance;
				}
				else 
				{
					return oldNote;
				}
				oldNote = note;
			}			
			octave += 12;
		}
	}

	
	
	private ArrayList<Integer> getSortedChordTones(Chord chord)
	{
		int[] chordTones = chord.getChordTones();
		ArrayList<Integer> sortedChordTones = new ArrayList<Integer>();
		for (int i: chordTones) sortedChordTones.add(i);
		Collections.sort(sortedChordTones);
		return sortedChordTones;
	}

	
	
	public Integer getChordTone(int referenceNote, Chord chord, int chordToneOffset)
	{
		if (chordToneOffset == 0)
		{
			return getNearestChordTone(referenceNote, chord);
		}
		else
		{
			int increment = Math.abs(chordToneOffset) / chordToneOffset;
			int count = Math.abs(chordToneOffset);
			int[] chordTones = chord.getChordTones();
			while (true)
			{
				referenceNote += increment;
				if (contains(chordTones, referenceNote % 12))
				{
					count--;
					if (count == 0)
						return referenceNote;
				}
			} 
		}		
	}
	
	

	private boolean contains(int[] arr, int i)
	{
		for (int x: arr) if (x == i) return true;
		return false;
	}
	
	
	public List<TimedNote> makeTimeNoteList(Mu aMu)
	{
		List<TimedNote> timedNoteList = new ArrayList<TimedNote>();
		for (Mu mu: aMu.getMusWithNotes())
		{
			for (MuNote muNote: mu.getMuNotes())
			{
				timedNoteList.add(
						new TimedNote(
								muNote.getPitch(), muNote.getVelocity(), 
								mu.getGlobalPositionInQuarters(), mu.getLengthInQuarters()
						)
				);
			}
		}
		return timedNoteList;
	}
	
	
	public static void main(String[] args)
	{
		new Mu021_NoteLevelChordToneAndEmbellishment();
	}

}
