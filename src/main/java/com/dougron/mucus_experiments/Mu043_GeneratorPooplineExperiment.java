package main.java.com.dougron.mucus_experiments;

import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.RequiredPlugInsRunner;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.da_utils.render_name.RenderName;

public class Mu043_GeneratorPooplineExperiment 
{
	
	private MucusOutputManager outputManager = LocalOutputManager.getInstance();
	
	public Mu043_GeneratorPooplineExperiment() 
	{
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new PhraseLengthRandom());
		pipeline.addPlugin(new XmlKeyRandom());
		pipeline.addPlugin(new TimeSignatureSingleRandom());
		pipeline.addPlugin(new DiatonicTriadProgressionRandom());
		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_START_PERCENT, -0.05, 0.0));
		pipeline.addPlugin(new PhraseBoundPercentRandom(Parameter.PHRASE_END_PERCENT, 0.35, 0.8));
		pipeline.addPlugin(new EvenlySpacedStructureToneRandom());
		pipeline.addPlugin(new TessituraFixed());
		pipeline.addPlugin(new StartNoteMelodyRandom());
		pipeline.addPlugin(new ContourMultiplierRandom());
		pipeline.addPlugin(new ContourChordTonesRandom());
		pipeline.setPrimaryPlugin(new RequiredPlugInsRunner(
				new Parameter[] {
						Parameter.PHRASE_LENGTH,
						Parameter.XMLKEY,
						Parameter.TIME_SIGNATURE,
						Parameter.CHORD_LIST_GENERATOR, 
						Parameter.PHRASE_START_PERCENT, 
						Parameter.PHRASE_END_PERCENT, 
						Parameter.STRUCTURE_TONE_SPACING, 
						Parameter.TESSITURA, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
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
		
		
		outputManager.outputToMusicXML(RenderName.dateAndTime(), totalMu);
	}
	
	
	
	public static void main(String[] args) {
		new Mu043_GeneratorPooplineExperiment();
	}
}
