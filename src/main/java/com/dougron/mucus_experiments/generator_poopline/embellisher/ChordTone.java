package main.java.com.dougron.mucus_experiments.generator_poopline.embellisher;

import main.java.com.dougron.mucus.algorithms.mu_generator.enums.ChordToneType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.da_utils.static_chord_scale_dictionary.ChordToneName;

public class ChordTone implements MuEmbellisher {
	
	
	
	private ChordToneType chordToneType;
	private int jumpCount;
	private ChordToneName[] chordToneNames;

	public ChordTone(
			ChordToneType aChordToneType, 
			int aJumpCount
			) 
	{
		chordToneType = aChordToneType;
		jumpCount = aJumpCount;
	}

	@Override
	public void addNotes(Mu embellishment) {
		Chord chord = getPrevailingChord(embellishment);
		for (MuNote note: embellishment.getParent().getMuNotes())
		{
//			int parentPitch = mu.getTopPitch();
			int parentPitch = note.getPitch();
			int pitch = 0;
			switch (chordToneType)
			{
			case CLOSEST:
				if (chordToneNames == null)
				{
					pitch = chord.getClosestChordTone(parentPitch, 0);
				} else
				{
					pitch = chord.getClosestChordTone(parentPitch, 0,
							chordToneNames);
				}

				break;
			case CLOSEST_ABOVE:
				pitch = getJumpedChordTone(chord, parentPitch, 1);
				break;
			case CLOSEST_ABOVE_OR_EQUAL:
				pitch = getClosestOrEqual(chord, parentPitch, 1);
				break;
			case CLOSEST_BELOW:
				pitch = getJumpedChordTone(chord, parentPitch, -1);
				break;
			case CLOSEST_BELOW_OR_EQUAL:
				pitch = getClosestOrEqual(chord, parentPitch, -1);
				break;
			default:
				break;
			}
			embellishment.addMuNote(
					new MuNote(pitch, MuEmbellisher.DEFAULT_VELOCITY));
		}

	}



	public Chord getPrevailingChord (Mu embellishment)
	{
		Chord chord = embellishment.getPrevailingChord();
		if (chord == null && embellishment.getGlobalPositionInQuarters() < 0.0)
		{
			Mu muGlobalParent = embellishment.ancestorSeach(e -> !e.hasParent());
			double quartersPosition = embellishment.getGlobalPositionInQuarters() + muGlobalParent.getLengthInQuarters();
			BarsAndBeats position = muGlobalParent.getGlobalPositionInBarsAndBeats(quartersPosition);
			chord = muGlobalParent.getChordAt(position);
		}
		return chord;
	}


	
	public int getClosestOrEqual (Chord chord, int parentPitch, int contour)
	// closestOrEqual does not consider jumping chord tones as the use case includes equal so why jump at all
	{
		int pitch;
		if (chordToneNames == null)
		{
			if (chord.isChordTone(parentPitch))
			{
				pitch = parentPitch;
			}
			else
			{
				pitch = chord.getClosestChordTone(parentPitch, contour);
			}
		} 
		else
		{
			if (chord.isChordTone(parentPitch, chordToneNames))
			{
				pitch = parentPitch;
			}
			else
			{
				pitch = chord.getClosestChordTone(parentPitch, contour, chordToneNames);
			}
		}
		
		return pitch;
	}


	
	public int getJumpedChordTone (Chord chord, int parentPitch, int contour)	// contour currently is 1 or -1 for search up or down
	{
		int tempPitch = parentPitch;
		int pitch = 0;

		if (chordToneNames == null)
		{
			pitch = chord.getClosestChordTone(tempPitch, contour * jumpCount);
		}
		else
		{
			pitch = chord.getClosestChordTone(tempPitch, contour * jumpCount, chordToneNames);
		}
		return pitch;
	}



	private Mu getPreviousMu(Mu aMu)
	{
		Mu parent = aMu.getParent();
		Mu theMu = parent.getMus().get(parent.getMus().size() - 1);
		for (Mu mu: parent.getMus())
		{
			if (mu == aMu) return theMu;
			theMu = mu;
		}
		return null;
	}



}
