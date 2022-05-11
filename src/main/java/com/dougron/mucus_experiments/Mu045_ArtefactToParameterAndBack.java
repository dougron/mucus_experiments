package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionDiatonicTriadRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.RequiredPlugInsRunner;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.da_utils.render_name.RenderName;



/*
 * seems that as of 9 May 2022, this does not do the 'and back' part of the name
 * 
 * generates a mu but does not convert back to paramters
 */
public class Mu045_ArtefactToParameterAndBack
{
	
	
	static String fileName = "Mu045_ArtefactToParameterAndBack";
	static MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private static Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
//			.put(MuTag.PART_1, new Integer[] {1, 0})
//			.put(MuTag.PART_2, new Integer[] {2, 0})
//			.put(MuTag.PART_BASS, new Integer[] {3, 0})
			.build();
	
	

	public static void main(String[] args)
	{
		Poopline pipeline = getANotherPipelineLoadedWithPlugs();
		
		PooplinePackage pack0 = new PooplinePackage("original", new Random());
		
		pack0 = pipeline.process(pack0);
		
		Mu masterMu = new Mu("master");
		pack0.getMu().addTag(MuTag.PART_MELODY);
		pack0.getMu().addTag(MuTag.PRINT_CHORDS);
		masterMu.addMu(pack0.getMu(), 0);
		
		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
				masterMu, 
				fileName + "_" + RenderName.dateAndTime(),
				partTrackAndClipIndexMap,
				new ArrayList<MuController>(),	// placeholder for the controller list
				injector
				);
		System.out.println(x);
	}



	private static Poopline getPooplineLoadedWithPlugins()
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthRandom());
		pipeline.addPlugin(new TempoRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_START_PERCENT, -0.05));
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_END_PERCENT, 0.55));
		pipeline.addPlugin(new ChordProgressionDiatonicTriadRandom());
		pipeline.addPlugin(new StartNoteMelodyRandom());
		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new StructureToneEvenlySpacedRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_START_NOTE));
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE));
		pipeline.addPlugin(new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_CONTOUR, MuTag.IS_SYNCOPATION));
		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.STACCATO, DurationType.LEGATO}));
		pipeline.addPlugin(new ContourChordTonesRandom());
		pipeline.setPrimaryPlugin(new RequiredPlugInsRunner(
				new Parameter[] {
						Parameter.PHRASE_LENGTH,
						Parameter.TEMPO,
						Parameter.XMLKEY,
						Parameter.TIME_SIGNATURE,
						Parameter.CHORD_LIST_GENERATOR, 
						Parameter.PHRASE_START_PERCENT, 
						Parameter.PHRASE_END_PERCENT, 
						Parameter.STRUCTURE_TONE_SPACING, 
//						Parameter.USE_STRUCTURE_TONE_SYNCOPATOR,
//						Parameter.STRUCTURE_TONE_SYNCOPATION,
						Parameter.TESSITURA_START_NOTE, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
						Parameter.TESSITURA_MELODY_RANGE,
						Parameter.TESSITURA_SOLVER,
//						Parameter.PATTERN_EMBELLISHER,
						Parameter.DURATION
				}
				));
		return pipeline;
	}
	
	private static Poopline getANotherPipelineLoadedWithPlugs()
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthRandom());
		pipeline.addPlugin(new TempoRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new ChordProgressionDiatonicTriadRandom());
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_START_PERCENT, -0.05));
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_END_PERCENT, 0.65));
//		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_START_PERCENT, -0.05, 0.0));
//		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_END_PERCENT, 0.35, 0.8));
		pipeline.addPlugin(new StructureToneEvenlySpacedRandom());
//		pipeline.addPlugin(new ShouldIUseTheStructureToneSyncopator());
//		pipeline.addPlugin(new StructureToneSyncopatorInQuartersRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_START_NOTE));
		pipeline.addPlugin(new StartNoteMelodyRandom());
		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new ContourChordTonesRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE));
		pipeline.addPlugin(new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_CONTOUR, MuTag.IS_STRUCTURE_TONE));
//		pipeline.addPlugin(new PatternEmbellisherRandom());
//		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.LEGATO, DurationType.STACCATO}));
		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.STACCATO, DurationType.LEGATO}));
		pipeline.setPrimaryPlugin(new RequiredPlugInsRunner(
				new Parameter[] {
						Parameter.PHRASE_LENGTH,
						Parameter.TEMPO,
						Parameter.XMLKEY,
						Parameter.TIME_SIGNATURE,
						Parameter.CHORD_LIST_GENERATOR, 
						Parameter.PHRASE_START_PERCENT, 
						Parameter.PHRASE_END_PERCENT, 
						Parameter.STRUCTURE_TONE_SPACING, 
//						Parameter.USE_STRUCTURE_TONE_SYNCOPATOR,
//						Parameter.STRUCTURE_TONE_SYNCOPATION,
						Parameter.TESSITURA_START_NOTE, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
						Parameter.TESSITURA_MELODY_RANGE,
						Parameter.TESSITURA_SOLVER,
//						Parameter.PATTERN_EMBELLISHER,
						Parameter.DURATION
				}
				));
		return pipeline;
	}

}
