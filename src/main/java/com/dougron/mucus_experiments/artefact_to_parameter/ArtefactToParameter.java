package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.shade.guava.math.DoubleMath;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.mu_generator.enums.ChordToneType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordEvent;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.Anticipation;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.ChordTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.ChromaticStepTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.SemiTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.StepTone;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionFloatBarFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationFixedInQuarters;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPatterns;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EmbellishmentFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlaceHolderRenderParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationInFloatBarsToNearestStrength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationInQuarters;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationLegato;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.BooleanRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ChordProgressionRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationFixedInQuartersRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternsRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EmbellishmentFixedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PlaceHolderRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneSyncopationDoublePatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneUnevenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;
import main.java.com.dougron.mucus_experiments.mu_length_in_float_bars.MuLengthInFloatBars;
import main.java.da_utils.combo_variables.IntAndInt;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class ArtefactToParameter
{
	
	private static final double PHRASE_BOUND_GAP = 0.005;
	private static final int MELODY_TESSITURA_WIGGLE_ROOM = 5;
	private static final int START_NOTE_TESSITURA_WIGGLE_ROOM = 5;

	public static final Logger logger = LogManager.getLogger(ArtefactToParameter.class);
	private static final int DEFAULT_START_NOTE_IF_NO_STRUCTURE_TONES_EXIST = 48;	// middle C
	private static final double EPSILON_FOR_DOUBLES_EQUALITY = 0.0001d;
	
	
	public static String sayHello()
	{
		return "hello";
	}

	
	
	public static PooplinePackage getPackFromMu(Mu aStructureToneMu, Mu aOriginalMu)
	{
		ChordToneAndEmbellishmentTagger.addTags(aStructureToneMu);
		justForNowConvertChordTonesToStructureTones(aStructureToneMu);
		aOriginalMu.makePreviousNextMusWithNotesSkippingNotesInTupletHolders();
		PooplinePackage pack = new PooplinePackage(aStructureToneMu.getName(), new Random());
		pack = addTempoRepo(aStructureToneMu, pack);
		pack = addPhraseLengthSetLengthRepo(aStructureToneMu, pack);
		pack = addTimeSignatureRandomRepo(aStructureToneMu, pack);
		pack = addXmlKey(aStructureToneMu, pack);
		pack = addChordProgression(aStructureToneMu, pack);
		pack = addReposForStructureTones(aStructureToneMu, pack);
		
		pack = addEmbellishmentRepos(aStructureToneMu, pack);
		
		return pack;
	}



	private static PooplinePackage addTempoRepo(Mu aMu, PooplinePackage pack)
	{
		TempoRepo repo = TempoRepo.builder()
				.rndValue(0.5)
				.selectedTempo((int)aMu.getStartTempo())
				.className(TempoRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.TEMPO, repo);
		return pack;
	}



	private static void justForNowConvertChordTonesToStructureTones(Mu aMu)
	{
		for (Mu mu: aMu.getMuWithTag(MuTag.IS_CHORD_TONE))
		{
			mu.addTag(MuTag.IS_STRUCTURE_TONE);
		}
	}

	
	/*
	 * currently assumes that aMu is a chord tone reduction which contains references to the original 
	 * chord tones using the HISTORY MuTag and the ORIGINAL_MU MuTagNamedParameter
	 * 
	 * without the above, nothing will be generated for embellishments
	 */
	private static PooplinePackage addReposForStructureTones(Mu aMu, PooplinePackage pack)
	{
		pack = addStartNoteRepo(aMu, pack);
		pack = addTessituraForStartNoteRepo(aMu, pack);
		pack = addPhraseStartPercentRepo(aMu, pack);
		pack = addPhraseEndPercentRepo(aMu, pack);
		pack = addStructureToneSpacingRepo(aMu, pack);
		pack = addStructureToneSyncopation(aMu, pack);
		pack = addMelodyTessituraRepo(aMu, pack);
		pack = addVectorChordTonesRepo(aMu, pack);
		pack = addDurationRepo(aMu, pack);
		return pack;
	}

	
		
	private static PooplinePackage addEmbellishmentRepos(Mu aMu, PooplinePackage pack)
	{
		List<EmbellishmentSchema> schemaList = getEmbellishmentSchemaList(aMu, pack);
//		makeArtefactToParameterLog(schemaList);
		
//		pack = addFixedEmbellishmentGeneratorRepo(pack, schemaList);
		pack = addMuEmbellishmentGeneratorRepo(pack, schemaList);
		pack = addEmbellishmentDurationRepo(pack, schemaList);
		
		return pack;
	}



	protected static PooplinePackage addMuEmbellishmentGeneratorRepo
	(
			PooplinePackage pack,
			List<EmbellishmentSchema> schemaList)
	{
		makeArtefactToParameterLog(schemaList);
		Map<Integer, List<MuEmbellisher>> selectedPitchMap = new HashMap<Integer, List<MuEmbellisher>>();
		List<Integer> pitchIndexList = new ArrayList<Integer>();
		Map<Integer, List<RhythmOffset>> selectedRhythmOffsetMap = new HashMap<Integer, List<RhythmOffset>>();
		List<Integer> rhythmIndexList = new ArrayList<Integer>();
		List<MuEmbellisher> pitchOptionList = new ArrayList<MuEmbellisher>();
		List<RhythmOffset> rhythmOptionList = new ArrayList<RhythmOffset>();
		Map<Integer, Integer> selectedCountMap = new HashMap<Integer, Integer>();
		List<Integer> countIndexList = new ArrayList<Integer>();
		
		int index = 0;
		for (EmbellishmentSchema es: schemaList)
		{
			List<MuEmbellisher> pitchList = new ArrayList<MuEmbellisher>();
			List<RhythmOffset> rhythmList = new ArrayList<RhythmOffset>();
			for (int i = es.size() - 2; i > 0; i--)
			{
				MuEmbellisher embellishmentOption = getEmbellishmentOptions(es, i);
				pitchList.add(embellishmentOption);
				pitchOptionList.add(embellishmentOption);
				RhythmOffset rhythmOption = EmbellishmentSchema.getBackwardRhythmOffsets(es)[i];
				rhythmList.add(rhythmOption);
				rhythmOptionList.add(rhythmOption);
			}
			selectedPitchMap.put(index, pitchList);
			pitchIndexList.add(index);
			selectedRhythmOffsetMap.put(index, rhythmList);
			rhythmIndexList.add(index);
			selectedCountMap.put(index, pitchList.size());
			countIndexList.add(index);
			index++;
		}
		Map<Integer, List<Double>> pitchGeneratorRndValues = makePitchGeneratorRndValuesMap(selectedPitchMap, pitchOptionList);
		Map<Integer, List<Double>> rhythmGeneratorRndValues = makeRhythmGeneratorRndValuesMap(selectedRhythmOffsetMap, rhythmOptionList);
		PatternEmbellishmentRepo repo = PatternEmbellishmentRepo.builder()
				.selectedPitchGenerators(selectedPitchMap)
				.selectedPitchIndexPattern(pitchIndexList.stream().mapToInt(x -> x).toArray())
				.pitchGeneratorRndValues(pitchGeneratorRndValues)
				.pitchGeneratorOptions(pitchOptionList.toArray(new MuEmbellisher[pitchOptionList.size()]))
				.selectedRhythmOffsets(selectedRhythmOffsetMap)
				.selectedRhythmIndexPattern(rhythmIndexList.stream().mapToInt(x -> x).toArray())
				.rhythmOffsetRndValues(rhythmGeneratorRndValues)
				.rhythmOffsetOptions(rhythmOptionList.toArray(new RhythmOffset[rhythmOptionList.size()]))
				.selectedCollisionIndexPattern(new int[] {0})
				.selectedCollisionOffsets(ImmutableMap.<Integer, RhythmOffset>builder().put(0, new RhythmOffset(0,0,0,1,0)).build())
				.selectedCounts(selectedCountMap)
				.selectedCountIndexPattern(countIndexList.stream().mapToInt(x -> x).toArray())
				.className(PatternEmbellisherRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
		return pack;
	}


	
	private static Map<Integer, List<Double>> makeRhythmGeneratorRndValuesMap
	(
			Map<Integer, List<RhythmOffset>> map, 
			List<RhythmOffset> optionList
			)
	{
		Map<Integer, List<Double>> rndMap = new HashMap<Integer, List<Double>>();
		double centreAmount = 0.5 / optionList.size(); 		// amount to add to lower bound of rnd value to centre it
		for (Integer key: map.keySet())
		{
			List<Double> rndList = new ArrayList<Double>();
			for (RhythmOffset ro: map.get(key))
			{
				rndList.add((double)optionList.indexOf(ro) / optionList.size() + centreAmount);
			}
			rndMap.put(key, rndList);
		}
		return rndMap;
	}
	
	
	
	private static Map<Integer, List<Double>> makePitchGeneratorRndValuesMap
	(
			Map<Integer, List<MuEmbellisher>> map, 
			List<MuEmbellisher> optionList
			)
	{
		Map<Integer, List<Double>> rndMap = new HashMap<Integer, List<Double>>();
		double centreAmount = 0.5 / optionList.size(); 		// amount to add to lower bound of rnd value to centre it
		for (Integer key: map.keySet())
		{
			List<Double> rndList = new ArrayList<Double>();
			for (MuEmbellisher emb: map.get(key))
			{
				rndList.add((double)optionList.indexOf(emb) / optionList.size() + centreAmount);
			}
			rndMap.put(key, rndList);
		}
		return rndMap;
	}



	private static MuEmbellisher getEmbellishmentOptions(EmbellishmentSchema es, int index)
	{
		int[] backwardSemitones = EmbellishmentSchema.getBackwardSemitoneSpacing(es);
		if (backwardSemitones[index] == 0) return Anticipation.getInstance();
		Integer[] backwardChordTones = EmbellishmentSchema.getBackwardChordToneSpacing(es);
		ChordToneType chordToneType;
		if (backwardChordTones[index] != null)
		{
			if (backwardChordTones[index] > 0) chordToneType = ChordToneType.CLOSEST_ABOVE; else chordToneType = ChordToneType.CLOSEST_BELOW;
			
			return new ChordTone(chordToneType, Math.abs(backwardChordTones[index]));
		}
		IntAndInt[] backwardStepTones = EmbellishmentSchema.getBackwardDiatonicSpacing(es);
		if (backwardStepTones[index].i1 != 0)
		{
			if (backwardStepTones[index].i1 > 0) chordToneType = ChordToneType.CLOSEST_ABOVE; else chordToneType = ChordToneType.CLOSEST_BELOW;
			return new ChromaticStepTone(chordToneType, Math.abs(backwardStepTones[index].i1), Math.abs(backwardStepTones[index].i2));
		}
		
//		if (backwardStepTones[index] != null)
//		{
//			if (backwardStepTones[index].i2 == 0) {
//				if (backwardStepTones[index].i1 > 0) chordToneType = ChordToneType.CLOSEST_ABOVE; else chordToneType = ChordToneType.CLOSEST_BELOW;
//				return new StepTone(chordToneType, Math.abs(backwardStepTones[index].i1));
//			}
//			else
//			{
//				if (backwardStepTones[index].i1 == 0)
//				{
//					if (backwardStepTones[index].i2 > 0) chordToneType = ChordToneType.CLOSEST_ABOVE; else chordToneType = ChordToneType.CLOSEST_BELOW;
//				}
//			}
//		}
		return new SemiTone(backwardSemitones[index]);
	}



	private static PooplinePackage addFixedEmbellishmentGeneratorRepo(PooplinePackage pack, List<EmbellishmentSchema> schemaList)
	{
		EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
				.schemaList(schemaList)
				.className(EmbellishmentFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
		return pack;
	}



	/*
	 * currently this does not look for any repeated embellishment patterns
	 * 
	 * also only does LEGATO and DEFAULT_SHORT
	 */
	private static PooplinePackage addEmbellishmentDurationRepo(PooplinePackage pack, List<EmbellishmentSchema> schemaList)
	{
		Map<Integer, DurationType[]> dpMap = new HashMap<Integer, DurationType[]>();
		List<Integer> indexList = new ArrayList<Integer>();
		int index = 0;
		for (EmbellishmentSchema schema: schemaList)
		{
			List<Double[]> lengthAndIoiList = schema.getLengthAndInterOnsetDistance(schema);
			DurationType[] dtArr = new DurationType[lengthAndIoiList.size()];
			int dtArrIndex = 0;
			for (Double[] arr: schema.getLengthAndInterOnsetDistance(schema))
			{
				if (DoubleMath.fuzzyEquals(arr[0], arr[1], EPSILON_FOR_DOUBLES_EQUALITY))
				{
					dtArr[dtArrIndex] = DurationType.LEGATO;
				}
				else
				{
					dtArr[dtArrIndex] = DurationType.DEFAULT_SHORT;
				}
				dtArrIndex++;
			}
			dpMap.put(index, dtArr);
			indexList.add(index);
			index++;
		}
		
		DurationPatternsRepo dpRepo = DurationPatternsRepo.builder()
				.durationPatternMap(dpMap)
				.patternIndices(indexList.stream().mapToInt(x -> x).toArray())
				.className(DurationPatterns.class.getName())
				.build();
		pack.getRepo().put(Parameter.DURATION_EMBELLISHMENT, dpRepo);
		return pack;
	}



	private static void makeArtefactToParameterLog(List<EmbellishmentSchema> schemaList)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./artefactToParamter.log")));
			for (EmbellishmentSchema schema: schemaList)
			{
				bw.write("\n" + schema.toString());
				bw.write("\tquarters" + Arrays.toString(EmbellishmentSchema.getForwardQuartersSpacing(schema)) + "\n");
				bw.write("\tquarters" + Arrays.toString(EmbellishmentSchema.getBackwardQuartersSpacing(schema)) + "\n");
				bw.write("\tfloatBars" + Arrays.toString(EmbellishmentSchema.getForwardFloatBarsSpacing(schema)) + "\n");
				bw.write("\tfloatBars" + Arrays.toString(EmbellishmentSchema.getBackwardFloatBarsSpacing(schema)) + "\n");
				bw.write("\trelativeRhythmicPosition" + Arrays.toString(EmbellishmentSchema.getForwardRelativeRhythmicSpacing(schema)) + "\n");
				bw.write("\trelativeRhythmicPosition" + Arrays.toString(EmbellishmentSchema.getBackwardRelativeRhythmicSpacing(schema)) + "\n");
				bw.write("\trhythmOffsets" + Arrays.toString(EmbellishmentSchema.getForwardRhythmOffsets(schema)) + "\n");
				bw.write("\trhythmOffsets" + Arrays.toString(EmbellishmentSchema.getBackwardRhythmOffsets(schema)) + "\n");
				bw.write("\tsemitones" + Arrays.toString(EmbellishmentSchema.getForwardSemitoneSpacing(schema)) + "\n");
				bw.write("\tsemitones" + Arrays.toString(EmbellishmentSchema.getBackwardSemitoneSpacing(schema)) + "\n");
				
				Stream<IntAndInt> stream1 = Arrays.stream(EmbellishmentSchema.getForwardDiatonicSpacing(schema));
				List<String> toString1 = stream1.map(x -> x.toString()).collect(Collectors.toList());
				bw.write("\tdiatonic_steps" + toString1.toString() + "\n");
				Stream<IntAndInt> stream2 = Arrays.stream(EmbellishmentSchema.getBackwardDiatonicSpacing(schema));
				List<String> toString2 = stream2.map(x -> x.toString()).collect(Collectors.toList());
				bw.write("\tdiatonic_steps" + toString2.toString() + "\n");
				
				bw.write("\tchordtones" + Arrays.toString(EmbellishmentSchema.getForwardChordToneSpacing(schema)) + "\n");
				bw.write("\tchordtones" + Arrays.toString(EmbellishmentSchema.getBackwardChordToneSpacing(schema)) + "\n");
				
				bw.write("\tembellishment_tags\n");
				int index = 0;
				for (List<MuTagBundle> muTagList: EmbellishmentSchema.getEmbellishmentTagLists(schema))
				{
					bw.write("\t\tindex=" + index + ": " + muTagList.toString() + "\n");
					index++;
				}
				List<String> lengthAndIOItoString = EmbellishmentSchema.getLengthAndInterOnsetDistance(schema).stream()
						.map(x -> "[" + x[0] + "," + x[1] + "]")
						.collect(Collectors.toList());
				bw.write("\tlengthAndInterOnsetInterval=" + lengthAndIOItoString);
			}
			bw.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



	static List<EmbellishmentSchema> getEmbellishmentSchemaList(Mu aMu, PooplinePackage pack)
	{
		List<EmbellishmentSchema> schemaList = new ArrayList<EmbellishmentSchema>();
		Mu previousStructureTone = null;
		int currentBar = 0;
		for (Mu structureTone: aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE))
		{
//			System.out.println("structureTone: " + structureTone.getName() + " " + structureTone.getGlobalPositionInQuarters());
			currentBar = structureTone.getGlobalPositionInBars();
			Mu originalStructureTone = getOriginalStructureToneFromHistoryTag(structureTone);
			Mu previousMu = originalStructureTone.getPreviousMu();
			EmbellishmentSchema schema = new EmbellishmentSchema();
			addAMuToSchemaListConsideringWillSyncopateTag(originalStructureTone, schema);
			while (true)
			{
				if (previousMu == null)
				{
					addPlaceholderNoteInfoForBeginningOfBarOfVeryFirstNote(aMu, pack, currentBar, schema);
					break;
				}
				if (previousMu == previousStructureTone)
				{
					addAMuToSchemaList(previousMu, schema);
					break;
				}
				addAMuToSchemaList(previousMu, schema);
				currentBar = previousMu.getGlobalPositionInBars();
				previousMu = previousMu.getPreviousMu();
			}
			previousStructureTone = originalStructureTone;
			schemaList.add(schema);
		}
		return schemaList;
	}



	private static void addAMuToSchemaListConsideringWillSyncopateTag(Mu aMu, EmbellishmentSchema schema)
	{
//		double globalPositionInQuarters;
		BarsAndBeats globalPositionInBarsAndBeats;
		double globalPositionInFloatBars;
		List<MuTagBundle> bundleList =  aMu.getMuTagBundleContaining(MuTag.WILL_SYNCOPATE);
		double globalPositionInQuarters = aMu.getGlobalPositionInQuarters();
		if (bundleList.size() > 0)
		{
			globalPositionInQuarters += (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS);
			globalPositionInBarsAndBeats = aMu.getGlobalPositionInBarsAndBeats(globalPositionInQuarters);
			globalPositionInFloatBars = aMu.getGlobalPositionInFloatBars(globalPositionInQuarters);
		}
		else
		{
			globalPositionInBarsAndBeats = aMu.getGlobalPositionInBarsAndBeats();
			globalPositionInFloatBars = aMu.getGlobalPositionInFloatBars();
		}
		schema.add(NoteInfo.builder()
				.positionInQuarters(globalPositionInQuarters)
				.positionInFloatBars(globalPositionInFloatBars)
				.positionInBarsAndBeats(globalPositionInBarsAndBeats)
				.relatedMu(aMu)
				.pitch(aMu.getTopPitch())
				.build());
	}
	
	
	
	private static void addAMuToSchemaList(Mu aMu, EmbellishmentSchema schema)
	{
		double globalPositionInQuarters = aMu.getGlobalPositionInQuarters();
		BarsAndBeats globalPositionInBarsAndBeats = aMu.getGlobalPositionInBarsAndBeats();
		double globalPositionInFloatBars = aMu.getGlobalPositionInFloatBars();
		schema.add(NoteInfo.builder()
				.positionInQuarters(globalPositionInQuarters)
				.positionInFloatBars(globalPositionInFloatBars)
				.positionInBarsAndBeats(globalPositionInBarsAndBeats)
				.relatedMu(aMu)
				.pitch(aMu.getTopPitch())
				.build());
	}



	private static void addPlaceholderNoteInfoForBeginningOfBarOfVeryFirstNote(Mu aMu, PooplinePackage pack,
			int currentBar, EmbellishmentSchema schema)
	{
		StartNoteRepo repo = (StartNoteRepo)pack.getRepo().get(Parameter.START_NOTE);
		int pitch = repo.getSelectedValue();
		schema.add(NoteInfo.builder()
				.positionInQuarters(aMu.getGlobalPositionInQuarters(BarsAndBeats.at(currentBar, 0.0)))
				.positionInFloatBars(currentBar)
				.positionInBarsAndBeats(BarsAndBeats.at(currentBar, 0.0))
				.pitch(pitch)
				.build());
	}
	
	
	private static PooplinePackage makeListOfEmbellishmentSchemas(Mu aMu, PooplinePackage pack)
	{
		
		
		return pack;
	}



	private static Mu getOriginalStructureToneFromHistoryTag(Mu structureTone)
	{
		Mu originalStructureTone = structureTone;
		List<MuTagBundle> bundleList = structureTone.getMuTagBundleContaining(MuTag.HISTORY);
		if (bundleList.size() > 0)
		{
			originalStructureTone = (Mu)bundleList.get(0).getNamedParameter(MuTagNamedParameter.ORIGINAL_MU);
		}
		return originalStructureTone;
	}



	private static PooplinePackage addStructureToneSyncopation(Mu aMu, PooplinePackage pack)
	{
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		List<Double> syncopationsInQuarters = new ArrayList<Double>();
		boolean hasSyncopations = false;
		for (Mu mu: structureTones)
		{
			List<MuTagBundle> bundleList = mu.getMuTagBundleContaining(MuTag.WILL_SYNCOPATE);
			if (bundleList.size() == 0)
			{
				syncopationsInQuarters.add(0.0);
			}
			else
			{
				syncopationsInQuarters.add(
						(double)bundleList
						.get(0)
						.getNamedParameter(
								MuTagNamedParameter.SYNCOPATED_OFFSET_IN_QUARTERS
								)
						);
				hasSyncopations = true;
			}
		}
		if (hasSyncopations)
		{
			pack = addSyncopationRepos(pack, syncopationsInQuarters);
		}
		else
		{
			pack.getRepo().put(
					Parameter.STRUCTURE_TONE_SYNCOPATION, 
					PlaceHolderRepo.builder()
						.parameter(Parameter.STRUCTURE_TONE_SYNCOPATION)
						.className(PlaceHolderRenderParameter.class.getName())
						.build()
					);
		}
		
		return pack;
	}



	private static PooplinePackage addSyncopationRepos(PooplinePackage pack, List<Double> syncopationsInQuarters)
	{
		List<Double> finalList = getRepeatingDoublePattern(syncopationsInQuarters);
		BooleanRepo shouldIuseTheSyncopation = BooleanRepo.builder()
				.selectedOption(true)
				.rndValue(0)
				.className(ShouldIUseTheStructureToneSyncopator.class.getName())
				.build();
		pack.getRepo().put(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR, shouldIuseTheSyncopation);
		
		StructureToneSyncopationDoublePatternRepo syncRepo = StructureToneSyncopationDoublePatternRepo.builder()
				.selectedOption(finalList.stream().mapToDouble(x -> x).toArray())
				.className(StructureToneSyncopatorInQuartersFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SYNCOPATION, syncRepo);
		
		return pack;
	}



	private static PooplinePackage addDurationRepo(Mu aMu, PooplinePackage pack)
	{
		pack = getDurationPatternModelsForStructureTones(aMu, pack);
		
//		pack = getFixedLengthDurationsFromOriginalStructureTones(aMu, pack);
		
//		pack = getFixedLengthDurationForStructureTones(pack, 0.5);			// this just makes all durations=0.5
		return pack;
	}



	private static PooplinePackage getDurationPatternModelsForStructureTones(Mu aMu, PooplinePackage pack)
	{
		List<Mu> originalStructureTones = getOriginalStructureTones(aMu);
		
		DurationModel[] durationTypeArr = new DurationModel[originalStructureTones.size() - 1];
		for (int i = 0; i < originalStructureTones.size() - 1; i++)
		{
			Mu structureTone = originalStructureTones.get(i);
			double endPosition = structureTone.getGlobalEndPositionInQuarters();
			double nextStart = structureTone.getNextMu().getGlobalPositionInQuarters();
			if (DoubleMath.fuzzyEquals(nextStart, endPosition, EPSILON_FOR_DOUBLES_EQUALITY))
			{
				durationTypeArr[i] = new DurationLegato();
			}
			else
			{
				durationTypeArr[i] = new DurationInQuarters(structureTone.getLengthInQuarters());
			}
		}
		DurationModel endNoteDurationModel = getEndNoteDurationModel(originalStructureTones);
		DurationPatternRepo repo = DurationPatternRepo.builder()
				.durationModelPattern(durationTypeArr)
				.endNoteDurationModel(endNoteDurationModel)
				.staccatoDurationInMilliseconds(100)
				.tagToActUpon(MuTag.IS_STRUCTURE_TONE)
				.className(DurationPattern.class.getName())
				.build();
		pack.getRepo().put(Parameter.DURATION, repo);
		return pack;
	}



	private static DurationModel getEndNoteDurationModel(List<Mu> muList)
	{
		if (muList.size() == 0)
		{
			return new DurationInQuarters(DurationInQuarters.SHORT_DURATION_IN_QUARTERS);
		}
		else
		{
			Mu endNote = muList.get(muList.size() - 1);
//			return new DurationInQuarters(endNote.getLengthInQuarters());
			return new DurationInFloatBarsToNearestStrength(
					MuLengthInFloatBars.getLengthInFloatBars(endNote), 
					endNote.getBeatStrengthOfEnd());
		}
	}



	private static List<Mu> getOriginalStructureTones(Mu aMu)
	{
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		List<Mu> structureTonesWithHistory = structureTones.stream()
				.filter(x -> x.getMuTagBundleContaining(MuTag.HISTORY).size() > 0)
				.collect(Collectors.toList());
		List<Mu> originalStructureTones;
		if (structureTonesWithHistory.size() > 0)
		{
			originalStructureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).stream()
//					.filter(x -> x.getMuTagBundleContaining(MuTag.HISTORY).size() > 0)
					.map(x -> (Mu)x.getMuTagBundleContaining(MuTag.HISTORY)
							.get(0)
							.getNamedParameter(MuTagNamedParameter.ORIGINAL_MU)
						)
					.collect(Collectors.toList());
		}
		else
		{
			originalStructureTones = structureTones;
		}
		return originalStructureTones;
	}



	private static PooplinePackage getFixedLengthDurationsFromOriginalStructureTones(Mu aMu, PooplinePackage pack)
	{
		List<Mu> originalStructureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).stream()
				.map(x -> (Mu)x.getMuTagBundleContaining(MuTag.HISTORY)
						.get(0)
						.getNamedParameter(MuTagNamedParameter.ORIGINAL_MU)
					)
				.collect(Collectors.toList());
		List<Double> list = originalStructureTones.stream()
				.map(x -> x.getLengthInQuarters())
				.collect(Collectors.toList());
		
//		Parameter requiredParameter = getRequiredParameter(pack);
		
		DurationFixedInQuartersRepo repo = DurationFixedInQuartersRepo.builder()
				.durationPattern(ArrayUtils.toPrimitive(list.toArray(new Double[list.size()])))
//				.requiredParameter(requiredParameter)
				.className(DurationFixedInQuarters.class.getName())
				.build();
		pack.getRepo().put(Parameter.DURATION, repo);
		return pack;
	}



	private static PooplinePackage getFixedLengthDurationForStructureTones(PooplinePackage pack, double aFixedLengthInQuarters)
	{
//		Parameter requiredParameter = getRequiredParameter(pack);
		
		DurationFixedInQuartersRepo repo = DurationFixedInQuartersRepo.builder()
				.durationPattern(new double[] {aFixedLengthInQuarters})
//				.requiredParameter(requiredParameter)
				.className(DurationFixedInQuarters.class.getName())
				.build();
		pack.getRepo().put(Parameter.DURATION, repo);
		return pack;
	}



//	private static Parameter getRequiredParameter(PooplinePackage pack)
//	{
//		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_CONTOUR))
//		{
//			return Parameter.STRUCTURE_TONE_CONTOUR;
//		}
//		else if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_VECTOR))
//		{
//			return Parameter.STRUCTURE_TONE_VECTOR;
//		}
//		return null;
//	}
	



	private static PooplinePackage addVectorChordTonesRepo(Mu aMu, PooplinePackage pack)
	{
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		Collections.sort(structureTones, Mu.globalPositionInQuartersComparator);
		List<Integer> vectorList = new ArrayList<Integer>();
		for (int i = 1; i < structureTones.size(); i++)
		{
			int firstPitch = structureTones.get(i - 1).getTopPitch();
			int secondPitch = structureTones.get(i).getTopPitch();
			Chord chord = structureTones.get(i).getPrevailingChord();
			vectorList.add(getChordToneVector(firstPitch, secondPitch, chord));
		}
		List<Integer> pattern = getRepeatingIntegerPattern(vectorList);
		int[] arr = pattern.stream().mapToInt(x -> x).toArray();
		
		
		VectorChordTonesRepo repo = VectorChordTonesRepo.builder()
//				.rndValue(0.5)
				.selectedVectorArray(arr)
				.className(VectorChordTonesFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_GENERATOR, repo);
		return pack;
	}
	
	
	static int getChordToneVector(int firstPitch, int secondPitch, Chord chord)
	{
		if (firstPitch == secondPitch)
		{
			return 0;
		}
		else
		{
			int vector = (int)Math.signum(secondPitch - firstPitch);
			int currentPitch = firstPitch;
			int offset = 0;
			while (true)
			{
				currentPitch = chord.getClosestChordTone(currentPitch, vector);
				offset += vector;
				if ((int)Math.signum(secondPitch - currentPitch) != vector)
				{
					break;
				}
			}
			return offset;
		}
	}
	
	
	
	private static PooplinePackage addMelodyTessituraRepo(Mu aMu, PooplinePackage pack)
	{
		int highRange = 0;
		int lowRange = 127;
		int currentPitch;
		for (Mu mu: aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE))
		{
			currentPitch = mu.getTopPitch();
			if (currentPitch > highRange) highRange = currentPitch;
			if (currentPitch < lowRange) lowRange = currentPitch;
		}
		highRange += MELODY_TESSITURA_WIGGLE_ROOM;
		lowRange -= MELODY_TESSITURA_WIGGLE_ROOM;
		
		TessituraRepo repo = TessituraRepo.builder()
				.highValue(highRange)
				.lowValue(lowRange)
				.parameter(Parameter.TESSITURA_MELODY_RANGE)
				.className(TessituraFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.TESSITURA_MELODY_RANGE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addStructureToneSpacingRepo(Mu aMu, PooplinePackage pack)
	{
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		Collections.sort(structureTones, Mu.globalPositionInQuartersComparator);
		List<Double> ioiList = getInterOnsetIntervals(structureTones);
		List<Double> repeatingPattern = getRepeatingDoublePattern(ioiList);
		
		// repeating pattern returning empty list means only one structure tone
		// this throws the entire uneven structure tone thing into disarray
		// should probably implement a startPositionInFloatBars and ditch the PhraseStartPercent
		// will have to see how this works out, in the meantime will assume the position
		// of the only structure tone in the bar is a way of fudging the current 
		// approach, which kind of ties it to the structure tone resolution, which should 
		// be around a resolution of 1.0 or 0.5....
		if (repeatingPattern.size() == 0)	// there was only one structure tone
		{
			double fudge = structureTones.get(0).getGlobalPositionInFloatBars() % 1.0;
			if (fudge == 0.0) fudge = 1;
			StructureToneEvenlySpacedRepo repo = StructureToneEvenlySpacedRepo.builder()
					.rndValue(0.5)
					.selectedValueInFloatBars(fudge)
					.className(StructureToneEvenlySpacedFixed.class.getName())
					.build();
			pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, repo);
		}
		else if (repeatingPattern.size() == 1)
		{
			StructureToneEvenlySpacedRepo repo = StructureToneEvenlySpacedRepo.builder()
					.rndValue(0.5)
					.selectedValueInFloatBars(repeatingPattern.get(0))
					.className(StructureToneEvenlySpacedFixed.class.getName())
					.build();
			pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, repo);
		}
		else
		{
			double[] arr = repeatingPattern.stream().mapToDouble(x -> x).toArray();
			StructureToneUnevenlySpacedRepo repo = StructureToneUnevenlySpacedRepo.builder()
					.rndValue(0.5)
					.selectedValuesInFloatBars(arr)
					.className(StructureToneEvenlySpacedFixed.class.getName())
					.build();
			pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, repo);
		}
		
		return pack;
	}
	
	
	
	static List<Double> getRepeatingDoublePattern(List<Double> ioiList)
	{
		List<Double> pattern = new ArrayList<Double>();
		for (int patternLength = 0; patternLength < ioiList.size(); patternLength++)
		{
			pattern.add(ioiList.get(patternLength));
			boolean fail = false;
			for (int testIndex = 0; testIndex < ioiList.size(); testIndex += patternLength + 1)
			{
				for (int patternIndex = 0; patternIndex < patternLength + 1; patternIndex++)
				{
					if (testIndex + patternIndex < ioiList.size())
					{
						double x = ioiList.get(testIndex + patternIndex);
						double y = pattern.get(patternIndex);
						if (x != y) 
						{
							fail = true;
							break;
						}
					}
				}
				if (fail) break;
			}
			if (!fail) return pattern;
		}
		return pattern;
	}
	
	
	static List<Integer> getRepeatingIntegerPattern(List<Integer> list)
	{
		List<Integer> pattern = new ArrayList<Integer>();
		for (int patternLength = 0; patternLength < list.size(); patternLength++)
		{
			pattern.add(list.get(patternLength));
			boolean fail = false;
			for (int testIndex = 0; testIndex < list.size(); testIndex += patternLength + 1)
			{
				for (int patternIndex = 0; patternIndex < patternLength + 1; patternIndex++)
				{
					if (testIndex + patternIndex < list.size())
					{
						double x = list.get(testIndex + patternIndex);
						double y = pattern.get(patternIndex);
						if (x != y) 
						{
							fail = true;
							break;
						}
					}
				}
				if (fail) break;
			}
			if (!fail) return pattern;
		}
		return pattern;
	}



	private static List<Double> getInterOnsetIntervals(List<Mu> aSortedMuList)
	{
		List<Double> ioiList = new ArrayList<Double>();
		for (int i = 1; i < aSortedMuList.size(); i++)
		{
			ioiList.add(
					aSortedMuList.get(i).getGlobalPositionInFloatBars()
					- aSortedMuList.get(i - 1).getGlobalPositionInFloatBars()
					);
		}
		return ioiList;
	}



	private static PooplinePackage addPhraseEndPercentRepo(Mu aMu, PooplinePackage pack)
	{
		double length = aMu.getLengthInQuarters();
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		double selectedValue = 0 - PHRASE_BOUND_GAP;
		if (structureTones.size() > 0)
		{
			double position = structureTones.get(structureTones.size() - 1).getPositionInQuarters();
			selectedValue = (position / length) + PHRASE_BOUND_GAP;
		}
		pack = addPhraseBoundRepo(pack, selectedValue, Parameter.PHRASE_END_PERCENT);
		return pack;
	}
	
	
	
	private static PooplinePackage addPhraseStartPercentRepo(Mu aMu, PooplinePackage pack)
	{
		double length = aMu.getLengthInQuarters();
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		double selectedValue = 0 - PHRASE_BOUND_GAP;
		if (structureTones.size() > 0)
		{
			double position = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).get(0).getPositionInQuarters();
			selectedValue = (position / length) - PHRASE_BOUND_GAP;
		}
		pack = addPhraseBoundRepo(pack, selectedValue, Parameter.PHRASE_START_PERCENT);
		return pack;
	}

	
	
	private static PooplinePackage addPhraseBoundRepo(PooplinePackage pack, double selectedValue, Parameter repoKey)
	{
		PhraseBoundRepo repo = PhraseBoundRepo.builder()
				.rndValue(0.5)
				.selectedValue(selectedValue)
				.parameter(repoKey)
				.className(PhraseBoundPercentSetAmount.class.getName())
				.build();
		pack.getRepo().put(repoKey, repo);
		return pack;
	}
	
	
	private static PooplinePackage addTessituraForStartNoteRepo(Mu aMu, PooplinePackage pack)
	{
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		int startNotePitch = DEFAULT_START_NOTE_IF_NO_STRUCTURE_TONES_EXIST;
		if (structureTones.size() > 0)
		{
			startNotePitch = structureTones.get(0).getTopPitch();
		}
		
		int highRange = startNotePitch + START_NOTE_TESSITURA_WIGGLE_ROOM;
		int lowRange = startNotePitch - START_NOTE_TESSITURA_WIGGLE_ROOM;
		
		TessituraRepo repo = TessituraRepo.builder()
				.highValue(highRange)
				.lowValue(lowRange)
				.parameter(Parameter.TESSITURA_START_NOTE)
				.className(TessituraFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.TESSITURA_START_NOTE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addStartNoteRepo(Mu aMu, PooplinePackage pack)
	{
		int selectedValue = DEFAULT_START_NOTE_IF_NO_STRUCTURE_TONES_EXIST;
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		if (structureTones.size() > 0)
		{
			selectedValue = structureTones.get(0).getTopPitch();
		}
		StartNoteRepo repo = StartNoteRepo.builder()
				.rndValue(0.5)
				.selectedValue(selectedValue)
				.className(StartNoteMelodyRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.START_NOTE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addXmlKey(Mu aMu, PooplinePackage pack)
	{
		XmlKeyRepo repo = XmlKeyRepo.builder()
				.rndValue(0.5)
				.selectedValue(0)	// this is what will need to change.....
				.options(new int[] {0})
				.className(XmlKeyRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.XMLKEY, repo);
		return pack;
	}

	
	
	private static PooplinePackage addChordProgression(Mu aMu, PooplinePackage pack)
	{
//		double lengthInFloatBars = (double)aMu.getLengthInBars();
		
		Map<Double, String> floatBarChordMap = new HashMap<Double, String>();
		// fyi, Mu makes a chordlist if none exists
		List<ChordEvent> ceList = aMu.getMuSpecificParentChordListExcerpt().getChordEventList();
		for (ChordEvent ce: ceList)
		{
			Chord chord = ce.getChord();
			BarsAndBeats position = ce.getPositionInBarsAndBeats();
			TimeSignature ts = aMu.getTimeSignature(position.getBarPosition());
			double positionInFloatBars = position.getBarPosition() + position.getOffsetInQuarters() / ts.getLengthInQuarters();
			String chordName = null;
			if (chord != null) 
			{
				if (chord.getAssociatedChordInKeyObject() != null)
				{
					chordName = chord.getAssociatedChordInKeyObject().chordSymbol();
				}
			}
			logger.debug("adding chord: " + chordName + " at " + positionInFloatBars + " floatBars");
			floatBarChordMap.put(positionInFloatBars, chordName);
		}
		ChordProgressionRepo repo = ChordProgressionRepo.builder()
				.rndValue(new double[] {0.1, 0.2})
				.floatBarChordMap(floatBarChordMap)
				.className(ChordProgressionFloatBarFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.CHORD_LIST_GENERATOR, repo);
		return pack;
	}
	

	
	private static PooplinePackage addTimeSignatureRandomRepo(Mu aMu, PooplinePackage pack)
	{
		TimeSignature ts = aMu.getTimeSignature(0);
		TimeSignatureRepo repo = TimeSignatureRepo.builder()
				.rndValue(0.5)
				.selectedValue(ts)
				.options(new TimeSignature[] {ts})
				.className(TimeSignatureSingleRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.TIME_SIGNATURE, repo);
		return pack;
	}

	
	
	private static PooplinePackage addPhraseLengthSetLengthRepo(Mu aMu, PooplinePackage pack)
	{
		PhraseLengthRepo repo = PhraseLengthRepo.builder()
				.rndValue(0.5)
				.selectedValue(aMu.getLengthInBars())
				.options(new int[] {aMu.getLengthInBars()})
				.className(PhraseLengthSetLength.class.getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, repo);
		return pack;
	}

}
