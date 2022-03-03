package main.java.com.dougron.mucus_experiments.completed;

import main.java.com.dougron.mucus.algorithms.contour.ContourFunction;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_Anticipation;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuG_EscapeTone;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuGenerator;
import main.java.com.dougron.mucus.algorithms.mu_generator.MuGenerator.AccentType;
import main.java.com.dougron.mucus.algorithms.mu_generator.enums.EscapeToneType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.SimpleEvenChordProgression;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.resource_objects.RandomNumberSequence;

/**
 * based on the rumination in the development notes from 2020_12_06
 * 
 * This an attempt to make that melody generator, and implement similarity matching
 * as a way of getting similar variations of a melody
 * 
 * This is phrase level stuff
 * 
 * renumbered to Mu022 from 021 so I could do a note level test first
 * 
 * 
 * @author dougr
 *
 */

public class Mu022_ChordToneAndEmbellishmentDecisionMaker
{
	
	enum AlgorithmOption {THIS_ONE, SOME_OTHER_ONE};
	enum ChordTonePattern{ONE_CHORD_TONE_PER_BAR, SOME_OTHER_CHORD_TONE_PATTERN};
	
	String fileName = "Mu022_ChordToneAndEmbellishmentDecisionMaker";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	
	
	public Mu022_ChordToneAndEmbellishmentDecisionMaker()
	{
		// a formality, no other options at this point
//		AlgorithmOption choiceOfAlgorithm = AlgorithmOption.THIS_ONE;
		
		// probably defined by the section level phrase length algorithm
		int lengthInBars = 8;
		
		double percentageToFill = 0.62;
		
		int startPitch = 58;
		int startEndContour = -2; 	// 0 = closest, -1, 1 closest in a particular direction, and then onwards....
		ContourFunction chordToneContour = new ContourFunction();
		chordToneContour.addBreakpoint(0.25, 1.0);
		int noteRangeMultiplier = 7;
		
//		RhythmPlacement[] firstAndLastChordToneRhythmPlacement = new RhythmPlacement[] {new NoAdjustment(), new nextTactus()};
		
		ChordTonePattern chordTonePattern = ChordTonePattern.ONE_CHORD_TONE_PER_BAR;
//		int numberOfChordTonesPerBar = 1;
		
		RandomNumberSequence embellishmentChooserRNS = new RandomNumberSequence(24, 2);
		System.out.println(embellishmentChooserRNS.toString());
		MuGenerator[] mugOptions = makeMuGeneratorOptions();
		
		
		Mu mu = createMu(lengthInBars);	
		makeStartAndEndNotePositionFirst(percentageToFill, mu);		
		addChordTones(chordTonePattern, mu);		
		doStartAndEndPitch(startPitch, startEndContour, mu);		
		makeInterveningChordTonePitchesFromContour(chordToneContour, noteRangeMultiplier, mu);		
		makeEmbellishments(embellishmentChooserRNS, mugOptions, mu);
		
		
		
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}



	private MuGenerator[] makeMuGeneratorOptions()
	{
		MuGenerator[] mugOptions = new MuGenerator[] {
				new MuG_Anticipation(1, 0.5),
				new MuG_Anticipation(1, 1.0),
				new MuG_Anticipation(2, 0.5),
				new MuG_Anticipation(2, 1.0),
				new MuG_Anticipation(3, 0.5),
				new MuG_EscapeTone(0.5, EscapeToneType.JUMP_STEP, AccentType.UNACCENTED),
				new MuG_EscapeTone(0.5, EscapeToneType.STEP_JUMP, AccentType.UNACCENTED),
				new MuG_EscapeTone(0.5, EscapeToneType.JUMP_STEP, AccentType.ACCENTED),
				new MuG_EscapeTone(0.5, EscapeToneType.STEP_JUMP, AccentType.ACCENTED),
				new MuG_EscapeTone(1.0, EscapeToneType.JUMP_STEP, AccentType.UNACCENTED),
				new MuG_EscapeTone(1.0, EscapeToneType.STEP_JUMP, AccentType.UNACCENTED),
				new MuG_EscapeTone(1.0, EscapeToneType.JUMP_STEP, AccentType.ACCENTED),
				new MuG_EscapeTone(1.0, EscapeToneType.STEP_JUMP, AccentType.ACCENTED),
		};
		return mugOptions;
	}



	private void makeEmbellishments(RandomNumberSequence embellishmentChooserRNS, MuGenerator[] mugOptions, Mu mu)
	{
		for (Mu ct: mu.getMus())
		{
			int mugIndex = (int)(embellishmentChooserRNS.next() * mugOptions.length);
			System.out.println("------\nchordTone:" + ct.toString());
			System.out.println(".... gets embellishment: " + mugOptions[mugIndex].toString());
			ct.addMuGenerator(mugOptions[mugIndex].getDeepCopy());
		}
		mu.generate();
	}



	private void makeInterveningChordTonePitchesFromContour(ContourFunction chordToneContour, int noteRangeMultiplier,
			Mu mu)
	{
		Mu startNote = mu.getMu("startNote");
		Mu endNote = mu.getMu("endNote");
		int pitchStart = startNote.getTopPitch();
		int pitchEnd = endNote.getTopPitch();
		double startInFloatBars = startNote.getGlobalPositionInFloatBars();
		double endInFloatBars = endNote.getGlobalPositionInFloatBars();
		for (Mu ctMu: mu.getMus())
		{
			if (ctMu.getName().equals("startNote") || ctMu.getName().equals("endNote"))
			{
				
			}
			else
			{
				double positionInFloatBars = ctMu.getGlobalPositionInFloatBars();
				int contourPitch = chordToneContour.getPitch(positionInFloatBars, noteRangeMultiplier, pitchStart, pitchEnd, startInFloatBars, endInFloatBars);
				Chord chord = ctMu.getPrevailingChord();
				int pitch = chord.getClosestChordTone(contourPitch, 0);
				ctMu.clearMuNotes();
				ctMu.addMuNote(new MuNote(pitch, 64));
			}
		}
	}



	private void doStartAndEndPitch(int startPitch, int startEndContour, Mu mu)
	{
		Mu startNote = mu.getMu("startNote");
		Chord startChord = startNote.getPrevailingChord();
		int startNotePitch = startChord.getClosestChordTone(startPitch);
		
		Mu endNote = mu.getMu("endNote");
		Chord endChord = endNote.getPrevailingChord();
		int endNotePitch = endChord.getClosestChordTone(startNotePitch, startEndContour);
		
		startNote.clearMuNotes();
		startNote.addMuNote(new MuNote(startNotePitch, 80));
		endNote.clearMuNotes();
		endNote.addMuNote(new MuNote(endNotePitch, 80));
	}



	private void addChordTones(ChordTonePattern chordTonePattern, Mu mu)
	{
		switch (chordTonePattern)
		{
		case ONE_CHORD_TONE_PER_BAR:	positionOneChordTonePerBarBetweenStartAndEndNote(mu);
										break;
		case SOME_OTHER_CHORD_TONE_PATTERN:
			break;
		default:
			break;
		}
	}



	private void positionOneChordTonePerBarBetweenStartAndEndNote(Mu mu)
	{
		int startBar = getStartBarForOneChordTonePerBar(mu);
		int endBar = getEndBarForOneChordTonePerBar(mu);
		int index = 0;
		for (int i = startBar; i <= endBar; i++)
		{
			Mu note = makeNewMu("chordTone" + index, 1.0, 60, 80);
			mu.addMu(note, new BarsAndBeats(i, 0.0));
			index++;
		}
	}



	private int getEndBarForOneChordTonePerBar(Mu mu)
	{
		int endBar;
		BarsAndBeats endBab = mu.getMu("endNote").getPositionInBarsAndBeats();
		if (endBab.getOffsetInQuarters() > 0.0)
		{
			endBar = endBab.getBarPosition();
		}
		else
		{
			endBar = endBab.getBarPosition() - 1;
		}
		return endBar;
	}



	private int getStartBarForOneChordTonePerBar(Mu mu)
	{
		int startNoteBar = mu.getMu("startNote").getPositionInBarsAndBeats().getBarPosition();
		int startBar = startNoteBar + 1;
		return startBar;
	}



	private void makeStartAndEndNotePositionFirst(double percentageToFill, Mu mu)
	{
		Mu startNote = makeNewMu("startNote", 1.0, 60, 80);
		mu.addMu(startNote, new BarsAndBeats(0, 0.0));
		
		double endPositionInFloatBars = mu.getLengthInBars() * percentageToFill;
		double closestTactusInQuarters = mu.getClosestTactus(endPositionInFloatBars);
		
		Mu endNote = makeNewMu("endNote", 1.0, 60, 80);
		mu.addMu(endNote, closestTactusInQuarters);
	}



	private Mu createMu(int lengthInBars)
	{
		Mu mu = new Mu("phrase");
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator());		// default 4/4
		mu.setChordListGenerator(new SimpleEvenChordProgression(new String[] {"C", "G", "Am", "F"}));
		mu.setLengthInBars(lengthInBars);
		return mu;
	}

	
	
	private Mu makeNewMu(String name, double lengthInQuarters, int pitch, int velocity)
	{
		Mu mu = new Mu(name);
		mu.setLengthInQuarters(lengthInQuarters);
		mu.addMuNote(new MuNote(pitch, velocity));
		return mu;
	}



	public static void main(String[] args)
	{
		new Mu022_ChordToneAndEmbellishmentDecisionMaker();

	}

}
