package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.RequiredPlugInsRunner;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.poopback.ContourShouldSpendMoreTimeDecending;
import main.java.com.dougron.mucus_experiments.generator_poopline.poopback.PoopbackObject;
import main.java.da_utils.render_name.RenderName;
import test.java.com.dougron.mucus.algorithms.TestRandom;


/*
 * demonstration of the PoopbackObject (to become FeedbackObject when I rename)
 * 
 * currently (2022_04_11) causing a null pointer exception
 */
public class Mu044_FeedbackGeneralisationWithStructureTones 
{
	
	private static MucusOutputManager outputManager = LocalOutputManager.getInstance();
	private static Poopline vectorPipeline;
	private static Poopline contourPipeline;
	
	
	public static void main(String[] args) 
	{
		Poopline pipeline = getVectorPipeline();
		PooplinePackage pack = getInitialMu(pipeline);
		
		PoopbackObject fo = new ContourShouldSpendMoreTimeDecending();
		
		int variationCount = 10;
		List<PooplinePackage> packList = getMuVariationList(pipeline, pack, fo, variationCount);
		
		Mu totalMu = makeOneBigFinalOutputMu(pack, packList);
		outputManager.outputToMusicXML(RenderName.dateAndTime(), totalMu);
		System.out.println("done");
	}


	private static PooplinePackage getInitialMu(Poopline pipeline)
	{
		PooplinePackage pack = getStructureTones("main", new Random(), pipeline);
		Mu mu = pack.getMu();
		mu.addTag(MuTag.PART_MELODY);
		mu.addTag(MuTag.PRINT_CHORDS);
		mu.setHasLeadingDoubleBar(true);
		mu.addMuAnnotation(new MuAnnotation("descending percentage=" + getCurrentDecendingPercentage(pack)));
		return pack;
	}


	private static Mu makeOneBigFinalOutputMu(PooplinePackage pack, List<PooplinePackage> packList)
	{
		Mu totalMu = new Mu("total");
		totalMu.addMu(pack.getMu(), 2);
		int nextBarPos = pack.getMu().getGlobalEndPositionInBarsAndBeats().getBarPosition() + 2;
		for (PooplinePackage pk: packList)
		{
			Mu muu = pk.getMu();
			muu.addTag(MuTag.PART_MELODY);
			muu.addTag(MuTag.PRINT_CHORDS);
			muu.setHasLeadingDoubleBar(true);
			muu.addMuAnnotation(new MuAnnotation("descending percentage=" + getCurrentDecendingPercentage(pk)));
			totalMu.addMu(muu, nextBarPos);
			nextBarPos += muu.getLengthInBars() + 2;
		}
		return totalMu;
	}


	private static List<PooplinePackage> getMuVariationList
	(
			Poopline pipeline, 
			PooplinePackage pack, 
			PoopbackObject fo,
			int count
			)
	{
		List<PooplinePackage> packList = new ArrayList<PooplinePackage>();
		for (int i = 0; i < count ; i++)
		{
			packList.add(getPackVariationBasedOnFeedback(pack, fo, pipeline));
		}
		return packList;
	}


	private static PooplinePackage getPackVariationBasedOnFeedback
	(
			PooplinePackage aPack, 
			PoopbackObject fo, 
			Poopline pipeline
			)
	{
		PooplinePackage pack = fo.getNewPackWithParametersToChangeOmittedFromRepo(aPack);
		pack = pipeline.process(pack);
		return pack;
	}


	private static double getCurrentDecendingPercentage(PooplinePackage aPack)
	{
		Mu mu = aPack.getMu();
		List<Mu> muList = mu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		Collections.sort(muList, Mu.globalPositionInQuartersComparator);
		double count = 0;
		double downCount = 0;
		for (int i = 0; i < muList.size() - 1; i++)
		{
			if (muList.get(i).getTopPitch() > muList.get(i + 1).getTopPitch()) downCount++;
			count++;
		}
		return downCount / count;
	}


	private static PooplinePackage getContourStructureTones()
	{
		return getStructureTones("x", new TestRandom(new double[] {0.0, 0.7}), contourPipeline);
	}

	
	
	private static PooplinePackage getVectorStructureTones()
	{
		return getStructureTones("x", new Random(), vectorPipeline);
	}
	
	
	private static PooplinePackage getStructureTones(String aName, Random rnd, Poopline pipeline)
	{
		PooplinePackage pack = new PooplinePackage(aName, rnd);
		pack = pipeline.process(pack);
		return pack;
	}

	
	
	private static Poopline getContourPipeline()
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthSetLength(8));
		pipeline.addPlugin(new TempoRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new DiatonicTriadProgressionRandom());
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_START_PERCENT, -0.05));
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_END_PERCENT, 0.76));
		pipeline.addPlugin(new EvenlySpacedStructureToneFixed(1.0));
//		pipeline.addPlugin(new ShouldIUseTheStructureToneSyncopator());
//		pipeline.addPlugin(new StructureToneSyncopatorInQuartersRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_START_NOTE));
		pipeline.addPlugin(new StartNoteMelodyRandom());
		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new ContourChordTonesRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE));
		pipeline.addPlugin(new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_CONTOUR, MuTag.IS_STRUCTURE_TONE));
//		pipeline.addPlugin(new PatternEmbellisherRandom());
		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.LEGATO}));
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
						Parameter.STRUCTURE_TONE_VECTOR, 
						Parameter.TESSITURA_MELODY_RANGE,
						Parameter.TESSITURA_SOLVER,
//						Parameter.PATTERN_EMBELLISHER,
						Parameter.DURATION
				}
				));
		return pipeline;
	}
	
	
	
	private static Poopline getVectorPipeline()
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthSetLength(8));
		pipeline.addPlugin(new TempoRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new DiatonicTriadProgressionRandom());
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_START_PERCENT, -0.05));
		pipeline.addPlugin(new PhraseBoundPercentSetAmount(Parameter.PHRASE_END_PERCENT, 0.76));
		pipeline.addPlugin(new EvenlySpacedStructureToneFixed(1.0));
//		pipeline.addPlugin(new ShouldIUseTheStructureToneSyncopator());
//		pipeline.addPlugin(new StructureToneSyncopatorInQuartersRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_START_NOTE));
		pipeline.addPlugin(new StartNoteMelodyRandom());
//		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new VectorChordTonesFixed(new int[] {0}));
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE));
		pipeline.addPlugin(new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_VECTOR, MuTag.IS_STRUCTURE_TONE));
//		pipeline.addPlugin(new PatternEmbellisherRandom());
		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.LEGATO}));
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
//						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.STRUCTURE_TONE_VECTOR, 
						Parameter.TESSITURA_MELODY_RANGE,
						Parameter.TESSITURA_SOLVER,
//						Parameter.PATTERN_EMBELLISHER,
						Parameter.DURATION
				}
				));
		return pipeline;
	}
}
