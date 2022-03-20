package main.java.com.dougron.mucus_experiments;

import java.util.List;
import java.util.Random;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation.TextPlacement;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.RequiredPlugInsRunner;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.da_utils.render_name.RenderName;

public class Mu043_GeneratorPooplineExperiment 
{
	
	private MucusOutputManager outputManager = LocalOutputManager.getInstance();
	DurationType dt;
	
	public Mu043_GeneratorPooplineExperiment() 
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthRandom());
		pipeline.addPlugin(new TempoRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new DiatonicTriadProgressionRandom());
		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_START_PERCENT, -0.05, 0.0));
		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_END_PERCENT, 0.35, 0.8));
		pipeline.addPlugin(new EvenlySpacedStructureToneRandom());
		pipeline.addPlugin(new ShouldIUseTheStructureToneSyncopator());
		pipeline.addPlugin(new StructureToneSyncopatorInQuartersRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_START_NOTE));
		pipeline.addPlugin(new StartNoteMelodyRandom());
		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new ContourChordTonesRandom());
		pipeline.addPlugin(new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE));
		pipeline.addPlugin(new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_CONTOUR, MuTag.IS_STRUCTURE_TONE));
		pipeline.addPlugin(new PatternEmbellisherRandom());
		pipeline.addPlugin(new DurationPattern(new DurationType[] {DurationType.LEGATO, DurationType.STACCATO}));
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
						Parameter.USE_STRUCTURE_TONE_SYNCOPATOR,
						Parameter.STRUCTURE_TONE_SYNCOPATION,
						Parameter.TESSITURA_START_NOTE, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
						Parameter.TESSITURA_MELODY_RANGE,
						Parameter.TESSITURA_SOLVER,
						Parameter.PATTERN_EMBELLISHER,
						Parameter.DURATION
				}
				));
		
		Mu totalMu = new Mu("parent");
		boolean first = true;
		Mu previousMu = null;
		for (int i = 0; i < 10; i++)
		{
			PooplinePackage pack = new PooplinePackage("pack" + i, new Random());
			pack = pipeline.process(pack);
			System.out.println(pack.toString());
			Mu tempMu = pack.getMu();
			tempMu.addTag(MuTag.PART_MELODY);
			tempMu.addTag(MuTag.PRINT_CHORDS);
			tempMu.setHasLeadingDoubleBar(true);
			if (previousMu == null)
			{
				totalMu.addMu(tempMu, 0);
			}
			else
			{
				totalMu.addMuToEndOfSibling(tempMu, 0, previousMu);
			}
			previousMu = tempMu;
		}
		
		Mu melody = new Mu("melody");
//		melody.addTag(MuTag.PART_MELODY);
//		melody.addTag(MuTag.PRINT_CHORDS);
//		pack.getMu().addMu(melody, 0);
		List<Mu> muList = totalMu.getAllMus();
		for (Mu mu: muList)
		{
			if (mu.hasTag(MuTag.IS_STRUCTURE_TONE))
			{
				mu.addMuAnnotation(new MuAnnotation("st", TextPlacement.PLACEMENT_BELOW));
			}
		}
		for (Mu mu: totalMu.getMus())
		{
			double tempo = mu.getStartTempo();
			mu.addMuAnnotation(new MuAnnotation(tempo + " bpm"));
		}
		
		outputManager.outputToMusicXML(RenderName.dateAndTime(), totalMu);
	}
	
	
	
	public static void main(String[] args) {
		new Mu043_GeneratorPooplineExperiment();
	}
}
