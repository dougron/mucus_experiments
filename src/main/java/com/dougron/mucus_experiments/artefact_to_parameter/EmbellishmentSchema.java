package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffsetFactory;
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
	
	
	
	public static RhythmOffset[] getForwardRhythmOffsets(EmbellishmentSchema schema)
	{
		RhythmOffset[] arr = new RhythmOffset[schema.size() - 1];
		int index = 0;
		Mu muFromTheArea = schema.get(0).getRelatedMu();	// as this list is in reverse order, index 0 is the last structure tone, and as such should always exist and have a relatedMu
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			arr[index] = RhythmOffsetFactory.getRhythmOffset(currentInfo.getPositionInBarsAndBeats(), nextInfo.getPositionInBarsAndBeats(), nextInfo.getRelatedMu());
			index++;
		}
		return arr;
	}
	
	
	
	public static RhythmOffset[] getBackwardRhythmOffsets(EmbellishmentSchema schema)
	{
		RhythmOffset[] arr = new RhythmOffset[schema.size() - 1];
		int index = 0;
		Mu muFromTheArea = schema.get(0).getRelatedMu();	// as this list is in reverse order, index 0 is the last structure tone, and as such should always exist and have a relatedMu
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 2; i >= 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i + 1);
			arr[index] = RhythmOffsetFactory.getRhythmOffset(currentInfo.getPositionInBarsAndBeats(), nextInfo.getPositionInBarsAndBeats(), currentInfo.getRelatedMu());
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
			Chord firstChord;
			if (currentInfo.getRelatedMu() == null)
			{
				firstChord = nextInfo.getRelatedMu().getPrevailingChord();
			}
			else
			{
				firstChord = currentInfo.getRelatedMu().getPrevailingChord();
			}
			arr[index] = getDiatonicSpacing(
					currentInfo.getPitch(), 
					firstChord, 
					nextInfo.getPitch(), 
					nextInfo.getRelatedMu().getPrevailingChord()
					);
			index++;
		}
		return arr;
	}

	
	
	public static IntAndInt[] getBackwardDiatonicSpacing(EmbellishmentSchema schema)
	{
		IntAndInt[] arr = new IntAndInt[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i - 1);
			nextInfo = schema.get(i);
			Chord nextChord;
			if (nextInfo.getRelatedMu() == null)
			{
				nextChord = currentInfo.getRelatedMu().getPrevailingChord();
			}
			else
			{
				nextChord = nextInfo.getRelatedMu().getPrevailingChord();
			}
			arr[index] = getDiatonicSpacing(
					currentInfo.getPitch(), 
					currentInfo.getRelatedMu().getPrevailingChord(),
					nextInfo.getPitch(), 
					nextChord
					);
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


	public static Integer[] getForwardChordToneSpacing(EmbellishmentSchema schema)
	{
		Integer[] arr = new Integer[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i);
			nextInfo = schema.get(i - 1);
			Chord nextChord = nextInfo.getRelatedMu().getPrevailingChord();
			if (nextChord.isChordTone(nextInfo.getPitch()))
			{
				if (currentInfo.getPitch() == nextInfo.getPitch())
				{
					arr[index] = 0;
				}
				else
				{
					int vector  = (int)Math.signum(nextInfo.getPitch() - currentInfo.getPitch());
					int count = 0;
					int currentNote = currentInfo.getPitch();
					int closestChordTone;
					while (true)
					{
						count += vector;
						closestChordTone = nextChord.getClosestChordTone(currentNote, vector);
						if (closestChordTone == nextInfo.getPitch())
						{
							arr[index] = count;
							break;
						}
						currentNote = closestChordTone;
					}
				}
			}
			else
			{
				arr[index] = null;
			}
			index++;
		}
		return arr;
	}
	
	
	
	public static Integer[] getBackwardChordToneSpacing(EmbellishmentSchema schema)
	{
		Integer[] arr = new Integer[schema.size() - 1];
		int index = 0;
		NoteInfo currentInfo;
		NoteInfo nextInfo;
		for (int i = schema.size() - 1; i > 0; i--)
		{
			currentInfo = schema.get(i - 1);
			nextInfo = schema.get(i);
			Mu relatedMu = nextInfo.getRelatedMu();
			if (relatedMu == null) relatedMu = currentInfo.getRelatedMu();
			Chord nextChord = relatedMu.getPrevailingChord();
			if (nextChord.isChordTone(nextInfo.getPitch()))
			{
				if (currentInfo.getPitch() == nextInfo.getPitch())
				{
					arr[index] = 0;
				}
				else
				{
					int vector  = (int)Math.signum(nextInfo.getPitch() - currentInfo.getPitch());
					int count = 0;
					int currentNote = currentInfo.getPitch();
					int closestChordTone;
					while (true)
					{
						count += vector;
						closestChordTone = nextChord.getClosestChordTone(currentNote, vector);
						if (closestChordTone == nextInfo.getPitch())
						{
							arr[index] = count;
							break;
						}
						currentNote = closestChordTone;
					}
				}
			}
			else
			{
				arr[index] = null;
			}
			index++;
		}
		return arr;
	}


	
	public static List<List<MuTagBundle>> getEmbellishmentTagLists(EmbellishmentSchema schema)
	{
		List<List<MuTagBundle>> list = new ArrayList<List<MuTagBundle>>();
		for (int i = 1; i < schema.size() - 1; i++)
		{
			List<MuTagBundle> bundleList = Arrays.stream(schema.get(i).getRelatedMu().getMuTagBundles())
					.map(x -> x)
					.collect(Collectors.toList());
			list.add(bundleList);
		}
		return list;
	}
	
	
	
	public static List<Double[]> getLengthAndInterOnsetDistance(EmbellishmentSchema schema)
	{
		List<Double[]> list = new ArrayList<Double[]>();
		for (int i = schema.size() - 2; i >=0; i--)
		{
			double length = 0;
			double interOnsetInterval = schema.get(i).getPositionInQuarters() - schema.get(i + 1).getPositionInQuarters();
			Mu mu = schema.get(i + 1).getRelatedMu();
			if (mu != null)
			{
				length = mu.getLengthInQuarters();
			}
			list.add(new Double[] {length, interOnsetInterval});
		}
		return list;
	}
	
	
}
