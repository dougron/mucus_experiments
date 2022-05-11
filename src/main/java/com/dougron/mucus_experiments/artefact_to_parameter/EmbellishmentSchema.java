package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.da_utils.combo_variables.IntAndInt;

public class EmbellishmentSchema extends ArrayList<NoteInfo>
{
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("----embellishment_schema-------------\n");
		for (NoteInfo item: this)
		{
			sb.append("  " + item.toString() + "\n");
		}
		return sb.toString();
	}
	

	// ########################################################################
	//
	//	rhythm stuff
	//
	// ########################################################################
	
	
	public static double[] getForwardQuartersSpacing(EmbellishmentSchema schema)
	{
		double[] arr = new double[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = nextInfo.getPositionInQuarters() - currentInfo.getPositionInQuarters();
			index++;
		}
		return arr;
	}
	
	
	
	public static double[] getBackwardQuartersSpacing(EmbellishmentSchema schema)
	{
		double[] arr = new double[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = nextInfo.getPositionInQuarters() - currentInfo.getPositionInQuarters();
			index++;
		}
		return arr;
	}

	
	
	public static double[] getForwardFloatBarsSpacing(EmbellishmentSchema schema)
	{
		double[] arr = new double[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = nextInfo.getPositionInFloatBars() - currentInfo.getPositionInFloatBars();
			index++;
		}
		return arr;
	}

	
	
	public static double[] getBackwardFloatBarsSpacing(EmbellishmentSchema schema)
	{
		double[] arr = new double[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = nextInfo.getPositionInFloatBars() - currentInfo.getPositionInFloatBars();
			index++;
		}
		return arr;
	}
	
	
	
	public static RelativeRhythmicPosition[] getForwardRelativeRhythmicSpacing(EmbellishmentSchema schema)
	{
		RelativeRhythmicPosition[] arr = new RelativeRhythmicPosition[schema.size() - 1];
		int index = 0;
		Mu muFromTheArea = schema.get(0).getRelatedMu();	// as this list is in reverse order, index 0 is the last structure tone, and as such should always exist and have a relatedMu
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = muFromTheArea.getRelativeRhythmicPosition(
					currentInfo.getPositionInBarsAndBeats(), 
					nextInfo.getPositionInBarsAndBeats()
					);
			index++;
		}
		return arr;
	}
	
	
	
	public static RelativeRhythmicPosition[] getBackwardRelativeRhythmicSpacing(EmbellishmentSchema schema)
	{
		RelativeRhythmicPosition[] arr = new RelativeRhythmicPosition[schema.size() - 1];
		int index = 0;
		Mu muFromTheArea = schema.get(0).getRelatedMu();	// as this list is in reverse order, index 0 is the last structure tone, and as such should always exist and have a relatedMu
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = muFromTheArea.getRelativeRhythmicPosition(
					currentInfo.getPositionInBarsAndBeats(), 
					nextInfo.getPositionInBarsAndBeats()
					);
			index++;
		}
		return arr;
	}
	
	
	// ########################################################################
	//
	//	pitch stuff
	//
	// ########################################################################
	
	
	public static int[] getForwardSemitoneSpacing(EmbellishmentSchema schema)
	{
		int[] arr = new int[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = nextInfo.getPitch() - currentInfo.getPitch();
			index++;
		}
		return arr;
	}
	
	
	
	public static int[] getBackwardSemitoneSpacing(EmbellishmentSchema schema)
	{
		int[] arr = new int[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = nextInfo.getPitch() - currentInfo.getPitch();
			index++;
		}
		return arr;
	}
	
	
	/*
	 * diatonic spacing:
	 * IntAndInt.i1 = number of diatonic steps
	 * IntAndInt.i2 = further semitone offset (most likely = -1,0 or 1, but could be -2 and +2 in harmonic minor)
	 */
	public static IntAndInt[] getForwardDiatonicSpacing(EmbellishmentSchema schema)
	{
		IntAndInt[] arr = new IntAndInt[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = new IntAndInt(0, 0);
			index++;
		}
		return arr;
	}
	
	
	
	public static IntAndInt getDiatonicSpacing(int startPitch, Chord startChord, int endPitch, Chord endChord)
	{
		int[] startDiatonicTones = endChord.getAllDiatonicNotes();
		int index = startChord.getClosestDiatonicToneIndex(startPitch);
		int vector = (int)Math.signum(endPitch - startPitch);
		int count = 0;
		int semi = 0;
		if (vector != 0)
		{
			while (true)
			{
				int currentNote = startDiatonicTones[index];
				if (vector == 1)
				{
					if (currentNote > endPitch)
					{
						count--;
						index--;
						currentNote = startDiatonicTones[index];
						semi = endPitch - currentNote;
						break;
					}
				}
				if (vector == -1)
				{
					if (currentNote < endPitch)
					{
						count++;
						index++;
						currentNote = startDiatonicTones[index];
						semi = currentNote - endPitch;
						break;
					}
				}
				if (currentNote == endPitch) break;
				count += vector;
				index += vector;
			}
		}
		return new IntAndInt(count, semi);
	}
	
	
	
	public static int[] getBackwardDiatonicSpacing(EmbellishmentSchema schema)
	{
		int[] arr = new int[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = nextInfo.getPitch() - currentInfo.getPitch();
			index++;
		}
		return arr;
	}
	
	
}


@Builder
@Data
class NoteInfo
{
	private double positionInQuarters;
	private double positionInFloatBars;
	private BarsAndBeats positionInBarsAndBeats;
	private int pitch;
	@ToString.Exclude Mu relatedMu;	
}
