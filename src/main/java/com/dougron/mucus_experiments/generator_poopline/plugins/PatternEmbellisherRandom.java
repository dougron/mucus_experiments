package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.Anticipation;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo.LoopModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo.EmbellishmentRhythmResolution;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;



public class PatternEmbellisherRandom  extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(PatternEmbellisherRandom.class);
	
	private PatternEmbellishmentRepo patternEmbellishmentRepo;
	private LoopModelRepo loopModelRepo;
	private int DEFAULT_MU_EMBELLISHER_COUNT = 8;

	@Getter private EmbellishmentRhythmResolution[] resolutionOptions = new EmbellishmentRhythmResolution[]
			{
					EmbellishmentRhythmResolution.EIGHTHS, EmbellishmentRhythmResolution.SIXTEENTHS
			};
	@Getter private boolean[] pitchAndRhythmPatternSyncOptions = new boolean[] {false, true};
	@Getter private int[][] pitchIndexPatternOptions = new int[][] 
			{
				new int[] {0},
				new int[] {0, 1},
				new int[] {0, 0, 1},
				new int[] {0, 1, 0},
				new int[] {0, 1, 1},
				new int[] {0, 0, 0, 1},
				new int[] {0, 0, 1, 0},
				new int[] {0, 1, 0, 0},
				new int[] {0, 0, 1, 1},
				new int[] {0, 1, 1, 0},
				new int[] {0, 1, 1, 1},
			};
	@Getter private MuEmbellisher[] pitchGeneratorOptions = new MuEmbellisher[]
			{
					Anticipation.getInstance(),
			};
	@Getter private int[][] rhythmIndexPatternOptions = new int[][] 
			{
				new int[] {0},
				new int[] {0, 1},
				new int[] {0, 0, 1},
				new int[] {0, 1, 0},
				new int[] {0, 1, 1},
				new int[] {0, 0, 0, 1},
				new int[] {0, 0, 1, 0},
				new int[] {0, 1, 0, 0},
				new int[] {0, 0, 1, 1},
				new int[] {0, 1, 1, 0},
				new int[] {0, 1, 1, 1},
			};
	@Getter private RhythmOffset[] rhythmOffsetOptions = new RhythmOffset[]
			{
					new RhythmOffset(0, 0, 0, -1, 0),
			};
	@Getter private int[][] collisionIndexPatternOptions = new int[][] 
			{
				new int[] {0},
				new int[] {0, 1},
			};
	@Getter private RhythmOffset[] collisionOffsetOptions = new RhythmOffset[]
			{
				new RhythmOffset(0, 0, 1, 0, 0),
				new RhythmOffset(0, 1, 0, 0, 0),
				new RhythmOffset(0, 1, 1, 0, 0),
			};

	
	
	
	public PatternEmbellisherRandom() {
		super(
				new Parameter[] {},
				new Parameter[] {}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		logger.info("Received " + pack);
		pack = super.process(pack);
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		loopModelRepo = dealWithLoopModelRepo(pack);
		
		if (pack.getRepo().containsKey(Parameter.PATTERN_EMBELLISHER) 
				&& pack.getRepo().get(Parameter.PATTERN_EMBELLISHER).getClassName().equals(getClass().getName())) 
		{	
			patternEmbellishmentRepo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		} 
		else 
		{
			patternEmbellishmentRepo = makePatternEmbellishmentRepo(pack);
			pack.getRepo().put(Parameter.PATTERN_EMBELLISHER, patternEmbellishmentRepo);
		}
		
		if (structureTones.size() > 0)
		{
			pack = doEmbellishments(pack, patternEmbellishmentRepo, loopModelRepo, structureTones);
		}
		return pack;
	}

	

	private LoopModelRepo dealWithLoopModelRepo(PooplinePackage pack) {
		LoopModelRepo repo;
		if (pack.getRepo().containsKey(Parameter.LOOP_MODEL))
		{
			repo = (LoopModelRepo)pack.getRepo().get(Parameter.LOOP_MODEL);
		}
		else
		{
			repo = LoopModelRepo.builder()
					.selectedLoopModel(LoopModel.LOOP)
					.className(getClass().getName())  	// gets the class name of the plug that created it, even if as in this case it is a default created by a class not really meant to create LoopModels
					.build();
			logger.info("Created temporary loopModelRepo=" + loopModelRepo.getSelectedLoopModel() + " because there was not one present in the pack.repo");
		}
		return repo;
	}


	
	private PatternEmbellishmentRepo makePatternEmbellishmentRepo(PooplinePackage pack) 
	{
		double resolutionRndValue;
		double pAndRPatternSyncRndValue;
		double pitchIndexPatternRndValue;
		int[] pitchIndices;
		Map<Integer, List<Double>> pitchGeneratorRndValue;
		double rhythmIndexPatternRndValue;
		int[] rhythmIndices;
		Map<Integer, List<Double>> rhythmOffsetRndValues;
		double collisionIndexPatternRndValue;
		int[] collisionIndices;
		Map<Integer, Double> collisionRndValues;
		PatternEmbellishmentRepo repo = PatternEmbellishmentRepo.builder()
				.resolutionRndValue(resolutionRndValue = pack.getRnd().nextDouble())
				.selectedResolution(resolutionOptions[(int)(resolutionOptions.length * resolutionRndValue)])
				.resolutionOptions(resolutionOptions)
				.pitchAndRhythmPatternSyncRndValue(pAndRPatternSyncRndValue = pack.getRnd().nextDouble())
				.selectedPitchAndRhythmPatternSync(pitchAndRhythmPatternSyncOptions[(int)(pitchAndRhythmPatternSyncOptions.length * pAndRPatternSyncRndValue)])
				.pitchAndRhythmPatternSyncOptions(pitchAndRhythmPatternSyncOptions)
				.pitchIndexPatternRndValue(pitchIndexPatternRndValue = pack.getRnd().nextDouble())
				.selectedPitchIndexPattern(pitchIndices = pitchIndexPatternOptions[(int)(pitchIndexPatternOptions.length * pitchIndexPatternRndValue)])
				.pitchIndexPatternOptions(pitchIndexPatternOptions)
				.pitchGeneratorRndValues(pitchGeneratorRndValue = getRndValueMap(pitchIndices, pack.getRnd(), DEFAULT_MU_EMBELLISHER_COUNT))
				.selectedPitchGenerators(getSelectedPitchGenerators(pitchGeneratorRndValue, pitchGeneratorOptions))
				.pitchGeneratorOptions(pitchGeneratorOptions)
				.rhythmIndexPatternRndValue(rhythmIndexPatternRndValue = pack.getRnd().nextDouble())
				.selectedRhythmIndexPattern(rhythmIndices = rhythmIndexPatternOptions[(int)(rhythmIndexPatternOptions.length * rhythmIndexPatternRndValue)])
				.rhythmIndexPatternOptions(rhythmIndexPatternOptions)
				.rhythmOffsetRndValues(rhythmOffsetRndValues = getRndValueMap(rhythmIndices, pack.getRnd(), DEFAULT_MU_EMBELLISHER_COUNT))
				.selectedRhythmOffsets(getSelectedRhythmOffsets(rhythmOffsetRndValues, rhythmOffsetOptions))
				.rhythmOffsetOptions(rhythmOffsetOptions)
				.collisionIndexPatternRndValue(collisionIndexPatternRndValue = pack.getRnd().nextDouble())
				.selectedCollisionIndexPattern(collisionIndices = collisionIndexPatternOptions[(int)(collisionIndexPatternOptions.length * collisionIndexPatternRndValue)])
				.collisionIndexPatternOptions(collisionIndexPatternOptions)
				.collisionOffsetRndValues(collisionRndValues = getSingleRndValueMap(collisionIndices, pack.getRnd()))
				.selectedCollisionOffsets(getSelectedCollisionOffsets(collisionRndValues, collisionOffsetOptions))
				.collisionOffsetOptions(collisionOffsetOptions)
				.className(getClass().getName())
				.build();
		return repo;
	}

	



	private Map<Integer, RhythmOffset> getSelectedCollisionOffsets
	(
			Map<Integer, Double> rndValueMap,
			RhythmOffset[] options
			) 
	{
		Map<Integer, RhythmOffset> map = new HashMap<Integer, RhythmOffset>();
		for (Integer key: rndValueMap.keySet()) 
		{
			map.put(key, options[(int)(options.length * rndValueMap.get(key))]);
		}
		return map;
	}


	private Map<Integer, Double> getSingleRndValueMap
	(
			int[] rndValues, 
			@NonNull Random rnd
			) 
	{
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Integer key: rndValues) 
		{
			if (!map.containsKey(key))
			{
				map.put(key,  rnd.nextDouble());
			}
		}
		return map;
	}


	private Map<Integer, List<RhythmOffset>> getSelectedRhythmOffsets
	(
			Map<Integer, List<Double>> rndValueMap,
			RhythmOffset[] options
			) 
	{
		Map<Integer, List<RhythmOffset>> map = new HashMap<Integer, List<RhythmOffset>>();
		List<RhythmOffset> list;
		for (Integer key: rndValueMap.keySet()) 
		{
			list = new ArrayList<RhythmOffset>();
			for (Double d: rndValueMap.get(key))
			{
				list.add(options[(int)(options.length * d)]);
			}
			map.put(key, list);
		}
		return map;
	}


	
	private Map<Integer, List<MuEmbellisher>> getSelectedPitchGenerators
	(
			Map<Integer, List<Double>> rndValueMap, 
			MuEmbellisher[] options
			) 
	{
		Map<Integer, List<MuEmbellisher>> map = new HashMap<Integer, List<MuEmbellisher>>();
		List<MuEmbellisher> list;
		for (Integer key: rndValueMap.keySet()) 
		{
			list = new ArrayList<MuEmbellisher>();
			for (Double d: rndValueMap.get(key))
			{
				list.add(options[(int)(options.length * d)]);
			}
			map.put(key, list);
		}
		return map;
	}

	

	private Map<Integer, List<Double>> getRndValueMap(int[] indices, Random rnd, int rndValueCount) 
	{
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		List<Double> list;
		for (int index: indices) 
		{
			if (!map.containsKey(index)) 
			{
				list = new ArrayList<Double>();
				for (int i = 0; i < rndValueCount; i++) 
				{
					list.add(rnd.nextDouble());
				}
				map.put(index, list);
			}
		}
		return map;
	}


	private PooplinePackage doEmbellishments
	(
			PooplinePackage pack, 
			PatternEmbellishmentRepo repo, 
			LoopModelRepo loopModelRepo,
			List<Mu> structureTones
			) 
	{
		// set up parameters
		int[] pitchIndexPattern =repo.getSelectedPitchIndexPattern();
		int pitchIndexPatternIndex = 0;
		int[] rhythmIndexPattern;
		int rhythmIndexPatternIndex = 0;
		if (repo.isSelectedPitchAndRhythmPatternSync())
		{
			rhythmIndexPattern = pitchIndexPattern;
		}
		else
		{
			rhythmIndexPattern = repo.getSelectedPitchIndexPattern();
		}
		int[] collisionIndexPattern = repo.getSelectedCollisionIndexPattern();
		int collisionIndexPatternIndex = 0;
		
		// loop through structure tones and apply embellishment patterns
		for (Mu structureTone: structureTones)
		{
			
			
			// increment and check indices......
			pitchIndexPatternIndex++;
			if (pitchIndexPatternIndex >= pitchIndexPattern.length) pitchIndexPatternIndex = 0;
			rhythmIndexPatternIndex++;
			if (rhythmIndexPatternIndex >= rhythmIndexPattern.length) rhythmIndexPatternIndex = 0;
			collisionIndexPatternIndex++;
			if (collisionIndexPatternIndex >= collisionIndexPattern.length) collisionIndexPatternIndex = 0;
		}
		
		
		
		return pack;
	}

}
