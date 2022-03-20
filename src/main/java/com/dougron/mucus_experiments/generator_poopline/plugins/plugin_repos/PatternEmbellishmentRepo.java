package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;

@Builder
@ToString
public class PatternEmbellishmentRepo implements RepoInterface{
	
	
	// might be that this issue is a choice of PatternEmbellisher with different presets, and might belong in 
	// a wrapper class for these two PatternEmbellishers......
	public enum EmbellishmentRhythmResolution {EIGHTHS, SIXTEENTHS}
	
	@Getter @Setter private double resolutionRndValue;
	@Getter @Setter private EmbellishmentRhythmResolution selectedResolution;
	@Getter @Setter private EmbellishmentRhythmResolution[] resolutionOptions;
	
	
	@Getter @Setter private double pitchAndRhythmPatternSyncRndValue;
	@Getter @Setter private boolean selectedPitchAndRhythmPatternSync;
	@Getter @Setter private boolean[] pitchAndRhythmPatternSyncOptions;
	
	
	@Getter @Setter private double countIndexPatternRndValue;
	@Getter @Setter private int[] selectedCountIndexPattern;
	@Getter @Setter private int[][] countIndexPatternOptions;

	
	@Getter @Setter private Map<Integer, Double> countRndValues;
	@Getter @Setter private Map<Integer, Integer> selectedCounts;
	@Getter @Setter private int[] countOptions;
	
	
	@Getter @Setter private double pitchIndexPatternRndValue;
	@Getter @Setter private int[] selectedPitchIndexPattern;
	@Getter @Setter private int[][] pitchIndexPatternOptions;
	
	
	@Getter @Setter private Map<Integer, List<Double>> pitchGeneratorRndValues;
	@Getter @Setter private Map<Integer, List<MuEmbellisher>> selectedPitchGenerators;
	@Getter @Setter private Map<Integer, Integer> pitchGeneratorsUsedCount;		// set during generation to be used during mutation
	@Getter @Setter private MuEmbellisher[] pitchGeneratorOptions;
	
	
	@Getter @Setter private double rhythmIndexPatternRndValue;
	@Getter @Setter private int[] selectedRhythmIndexPattern;
	@Getter @Setter private int[][] rhythmIndexPatternOptions;
	
	
	@Getter @Setter private Map<Integer, List<Double>> rhythmOffsetRndValues;
	@Getter @Setter private Map<Integer, List<RhythmOffset>> selectedRhythmOffsets;
	@Getter @Setter private Map<Integer, Integer> rhythmGeneratorsUsedCount;	// set during generation to be used during mutation
	@Getter @Setter private RhythmOffset[] rhythmOffsetOptions;
	
	
	
	
	// this one is about embellishments colliding with previous structure tones, or 
	// the latest note in a structure tone group. Measured from the begining of the 
	// last previous note or the syncopation position if the note is syncopated
	// also, the values loop
	@Getter @Setter private double collisionIndexPatternRndValue;
	@Getter @Setter private int[] selectedCollisionIndexPattern;
	@Getter @Setter private int[][] collisionIndexPatternOptions;
	
	
	@Getter @Setter private Map<Integer, Double> collisionOffsetRndValues;
	@Getter @Setter private Map<Integer, RhythmOffset> selectedCollisionOffsets;
	@Getter @Setter private RhythmOffset[] collisionOffsetOptions;

	@Getter @Setter private String className;

	public void clearPitchAndRhythmUsedCountMaps() {
		if (pitchGeneratorsUsedCount == null)
		{
			pitchGeneratorsUsedCount = new HashMap<Integer, Integer>();
		}
		else
		{
			pitchGeneratorsUsedCount.clear();
		}
		if (rhythmGeneratorsUsedCount == null)
		{
			rhythmGeneratorsUsedCount = new HashMap<Integer, Integer>();
		}
		else
		{
			rhythmGeneratorsUsedCount.clear();
		}
		
	}
}
