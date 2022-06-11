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
import lombok.Setter;
import main.java.com.dougron.mucus.algorithms.mu_generator.enums.ChordToneType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.Anticipation;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.ChordTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.StepTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo.LoopModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo.EmbellishmentRhythmResolution;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;



public class PatternEmbellisherRandom  extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(PatternEmbellisherRandom.class);
	
	private PatternEmbellishmentRepo patternEmbellishmentRepo;
	private LoopModelRepo loopModelRepo;
	@Getter private int DEFAULT_MU_EMBELLISHER_COUNT = 4;

	@Getter @Setter private EmbellishmentRhythmResolution[] resolutionOptions = new EmbellishmentRhythmResolution[]
			{
					EmbellishmentRhythmResolution.EIGHTHS, EmbellishmentRhythmResolution.SIXTEENTHS
			};
	@Getter @Setter private boolean[] pitchAndRhythmPatternSyncOptions = new boolean[] {false, true};
	@Getter @Setter private int[][] countIndexPatternOptions = new int[][] 
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
	@Getter @Setter private int[] countOptions = new int[]
			{
					0,1,2,3,4,5,6,7,8
			};
	@Getter @Setter private int[][] pitchIndexPatternOptions = new int[][] 
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
	@Getter @Setter private MuEmbellisher[] pitchGeneratorOptions = new MuEmbellisher[]
			{
				Anticipation.getInstance(),
				new ChordTone(ChordToneType.CLOSEST_BELOW, 1),
				new ChordTone(ChordToneType.CLOSEST_ABOVE, 1),
				new StepTone(ChordToneType.CLOSEST_BELOW, 1),
				new StepTone(ChordToneType.CLOSEST_ABOVE, 1),
				new StepTone(ChordToneType.CLOSEST_ABOVE, 1),
				new StepTone(ChordToneType.CLOSEST_ABOVE, 1),
				new StepTone(ChordToneType.CLOSEST_ABOVE, 1),
					
			};
	@Getter @Setter private int[][] rhythmIndexPatternOptions = new int[][] 
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
	@Getter @Setter private RhythmOffset[] rhythmOffsetOptions = new RhythmOffset[]
			{
					new RhythmOffset(0, 0, 0, -1, 0),
					new RhythmOffset(0, 0, -1, 0, 0),
					new RhythmOffset(0, 0, -1, -1, 0),
			};
	@Getter @Setter private int[][] collisionIndexPatternOptions = new int[][] 
			{
				new int[] {0},
				new int[] {0, 1},
			};
	@Getter @Setter private RhythmOffset[] collisionOffsetOptions = new RhythmOffset[]
			{
				new RhythmOffset(0, 0, 1, 0, 0),
//				new RhythmOffset(0, 1, 0, 0, 0),
//				new RhythmOffset(0, 1, 1, 0, 0),
			};

	
	
	
	public PatternEmbellisherRandom() {
		super(
				Parameter.EMBELLISHMENT_GENERATOR,
				new Parameter[] {Parameter.STRUCTURE_TONE_GENERATOR}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
	@Override
	PooplinePackage updateMu(PooplinePackage pack)
	{
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		if (structureTones.size() > 0)
		{
			pack = doEmbellishments(pack, patternEmbellishmentRepo, loopModelRepo, structureTones);
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		patternEmbellishmentRepo = makePatternEmbellishmentRepo(pack);
		pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, patternEmbellishmentRepo);
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		patternEmbellishmentRepo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		loopModelRepo = dealWithLoopModelRepo(pack);
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) 
//	{
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		pack = super.process(pack);
//		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
//		loopModelRepo = dealWithLoopModelRepo(pack);
//		
//		if (pack.getRepo().containsKey(Parameter.PATTERN_EMBELLISHER) 
//				&& pack.getRepo().get(Parameter.PATTERN_EMBELLISHER).getClassName().equals(getClass().getName())) 
//		{	
//			patternEmbellishmentRepo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
//		} 
//		else 
//		{
//			patternEmbellishmentRepo = makePatternEmbellishmentRepo(pack);
//			pack.getRepo().put(Parameter.PATTERN_EMBELLISHER, patternEmbellishmentRepo);
//		}
//		
//		if (structureTones.size() > 0)
//		{
//			pack = doEmbellishments(pack, patternEmbellishmentRepo, loopModelRepo, structureTones);
//		}
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}

	

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
			logger.info("Created temporary loopModelRepo=" + repo.getSelectedLoopModel() + " because there was not one already present in the pack.repo");
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
		double countIndexPatternRndValue;
		int[] countIndices;
		Map<Integer, Double> countRndValues;
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
				.countIndexPatternRndValue(countIndexPatternRndValue = pack.getRnd().nextDouble())
				.selectedCountIndexPattern(countIndices = countIndexPatternOptions[(int)(countIndexPatternOptions.length * countIndexPatternRndValue)])
				.countIndexPatternOptions(countIndexPatternOptions)
				.countRndValues(countRndValues = getSingleRndValueMap(collisionIndices, pack.getRnd()))
				.selectedCounts(getSelectedCounts(countRndValues, countOptions))
				.countOptions(countOptions)
				.className(getClass().getName())
				.build();
		return repo;
	}

	



	private Map<Integer, Integer> getSelectedCounts
	(
			Map<Integer, Double> rndValueMap, 
			int[] options
			) 
	{
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer key: rndValueMap.keySet()) 
		{
			map.put(key, options[(int)(options.length * rndValueMap.get(key))]);
		}
		return map;
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
		int pitchIndexPatternIndex = 0;
		int[] pitchIndexPattern =repo.getSelectedPitchIndexPattern();
		int rhythmIndexPatternIndex = 0;
		int[] rhythmIndexPattern = getRhythmIndexPattern(repo, pitchIndexPattern);
		int collisionIndexPatternIndex = 0;
		int[] collisionIndexPattern = repo.getSelectedCollisionIndexPattern();
		int countIndexPatternIndex = 0;
		int[] countIndexPattern = repo.getSelectedCountIndexPattern();
		repo.clearPitchAndRhythmUsedCountMaps();
		int rhythmIndex;
		int pitchIndex;
		int collisionIndex;
		int countIndex;

		
		// loop through structure tones and apply embellishment patterns
		Mu previousStructureTone = null;
		Mu tempMu;
		for (Mu structureTone: structureTones)
		{
			rhythmIndex = rhythmIndexPattern[rhythmIndexPatternIndex];
			pitchIndex = pitchIndexPattern[pitchIndexPatternIndex];
			collisionIndex = collisionIndexPattern[collisionIndexPatternIndex];
			countIndex = countIndexPattern[countIndexPatternIndex];
			List<RhythmOffset> offsetList = repo.getSelectedRhythmOffsets().get(rhythmIndex);
			List<MuEmbellisher> embellisherList = repo.getSelectedPitchGenerators().get(pitchIndex);
			RhythmOffset collisionOffset = repo.getSelectedCollisionOffsets().get(collisionIndex);
			double globalPositionOfPreviousMu = getGlobalPositionOfPreviousMu(structureTone, previousStructureTone, loopModelRepo, structureTones);
			double globalPositionOfCollisionPoint = globalPositionOfPreviousMu + collisionOffset.getOffsetInQuarters(globalPositionOfPreviousMu, structureTone);
			tempMu = structureTone;
			Integer count = repo.getSelectedCounts().get(countIndex);
			int numberOfEmbellishmentsUsed = 0;
			logger.debug("Structure tone position=" + structureTone.getGlobalPositionInQuarters());
			for (int i = 0; i < count; i++)
			{
				RhythmOffset ro = getRhythmOffset(pack, offsetList, i, rhythmIndex);
				double offsetInQuarters = getOffsetInQuarters(ro, tempMu);
				logger.debug("Embellishment rhythmOffset: " + ro.toString() + " offsetInQuarters=" + offsetInQuarters);
				if (tempMu.getGlobalPositionInQuarters() + offsetInQuarters >= globalPositionOfCollisionPoint)
				{
					numberOfEmbellishmentsUsed = i;
					Mu mu = new Mu("emb");
					mu.addTag(MuTag.IS_EMBELLISHMENT);
					tempMu.addMu(mu, offsetInQuarters);
					MuEmbellisher currentEmbellisher = getEmbellisher(pack, embellisherList, i, pitchIndex);
					currentEmbellisher.addNotes(mu);
					tempMu = mu;
				}
			}
			setCountMapKeyAndValue(rhythmIndex, numberOfEmbellishmentsUsed, repo.getRhythmGeneratorsUsedCount());
			setCountMapKeyAndValue(pitchIndex, numberOfEmbellishmentsUsed, repo.getPitchGeneratorsUsedCount());
			
			
			// increment and check indices......
			pitchIndexPatternIndex++;
			if (pitchIndexPatternIndex >= pitchIndexPattern.length) pitchIndexPatternIndex = 0;
			rhythmIndexPatternIndex++;
			if (rhythmIndexPatternIndex >= rhythmIndexPattern.length) rhythmIndexPatternIndex = 0;
			collisionIndexPatternIndex++;
			if (collisionIndexPatternIndex >= collisionIndexPattern.length) collisionIndexPatternIndex = 0;
			countIndexPatternIndex++;
			if (countIndexPatternIndex >= countIndexPattern.length) countIndexPatternIndex = 0;
			previousStructureTone = structureTone;
		}
		return pack;
	}




	private int[] getRhythmIndexPattern(PatternEmbellishmentRepo repo, int[] pitchIndexPattern) {
		int[] rhythmIndexPattern;
		if (repo.isSelectedPitchAndRhythmPatternSync())
		{
			rhythmIndexPattern = pitchIndexPattern;
		}
		else
		{
			rhythmIndexPattern = repo.getSelectedPitchIndexPattern();
		}
		return rhythmIndexPattern;
	}


	private void setCountMapKeyAndValue(int rhythmIndex, int numberOfEmbellishmentsUsed,
			Map<Integer, Integer> rhythmCountMap) {
		if (rhythmCountMap.containsKey(rhythmIndex))
		{
			if (rhythmCountMap.get(rhythmIndex) < numberOfEmbellishmentsUsed)
			{
				rhythmCountMap.put(rhythmIndex, numberOfEmbellishmentsUsed);
			}
		}
		else
		{
			rhythmCountMap.put(rhythmIndex, numberOfEmbellishmentsUsed);
		}
	}


	
	private double getOffsetInQuarters(RhythmOffset ro, Mu aMu) 
	{
		return ro.getOffsetInQuarters(aMu);
	}

	
	
	private MuEmbellisher getEmbellisher
	(
			PooplinePackage pack, 
			List<MuEmbellisher> embellisherList, 
			int pitchListIndex, 
			int pitchIndex
			) 
	{
		if (embellisherList.size() <= pitchListIndex)
		{
			for (int i = embellisherList.size(); i <= pitchListIndex; i++)
			{
				double newRnd = pack.getRnd().nextDouble();
				patternEmbellishmentRepo.getPitchGeneratorRndValues().get(pitchIndex).add(newRnd);
				MuEmbellisher[] options = patternEmbellishmentRepo.getPitchGeneratorOptions();
				MuEmbellisher selectedEmbellisher = options[(int)(options.length * newRnd)];
				patternEmbellishmentRepo.getSelectedPitchGenerators().get(pitchIndex).add(selectedEmbellisher);
			}
		}
		return embellisherList.get(pitchListIndex);
	}


	private RhythmOffset getRhythmOffset
	(
			PooplinePackage pack, 
			List<RhythmOffset> offsetList, 
			int offsetListIndex, 
			int rhythmIndex
			) 
	{
		if (offsetList.size() <= offsetListIndex)
		{
			for (int i = offsetList.size(); i <= offsetListIndex; i++)
			{
				double newRnd = pack.getRnd().nextDouble();
				patternEmbellishmentRepo.getRhythmOffsetRndValues().get(rhythmIndex).add(newRnd);
				RhythmOffset[] options = patternEmbellishmentRepo.getRhythmOffsetOptions();
				RhythmOffset selectedOffset = options[(int)(options.length * newRnd)];
				patternEmbellishmentRepo.getSelectedRhythmOffsets().get(rhythmIndex).add(selectedOffset);
			}
		}
		return offsetList.get(offsetListIndex);
	}


	private double getGlobalPositionOfPreviousMu
	(
			Mu thisMu, 
			Mu previousMu, 
			LoopModelRepo repo, 
			List<Mu> structureTones
			) 
	{
		if (previousMu == null)
		{
			if (repo.getSelectedLoopModel() == LoopModel.LOOP)
			{
				return getFinalStructureToneAsThoughBeforeCurrentNote(thisMu, structureTones);
			}
			else if (repo.getSelectedLoopModel() == LoopModel.CONTINUOUS)
			{
				return getPreviousStructureToneFromGlobalParent(thisMu, structureTones);
			}
		}
		else
		{
			return previousMu.getGlobalPositionInQuarters();
		}
		return 0;
	}
	
	

	// this is temporary for now, obviously this is just LoopModel.LOOP
	private double getPreviousStructureToneFromGlobalParent(Mu thisMu, List<Mu> structureTones) 
	{
		return getFinalStructureToneAsThoughBeforeCurrentNote(thisMu, structureTones);
	}

	

	private double getFinalStructureToneAsThoughBeforeCurrentNote(Mu aMu, List<Mu> structureTones) 
	{
		Mu parentMu = aMu.getParent();
		double lengthInQuarters = parentMu.getLengthInQuarters();
		Mu lastMu = structureTones.get(structureTones.size() - 1);
		return lastMu.getGlobalPositionInQuarters() - lengthInQuarters;
	}
	



}
