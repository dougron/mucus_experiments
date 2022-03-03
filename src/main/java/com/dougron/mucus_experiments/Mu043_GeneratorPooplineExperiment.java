package main.java.com.dougron.mucus_experiments;

import java.util.Random;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.RandomDiatonicTriadProgression;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.RandomPhraseLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.RandomXMLKey;
import main.java.da_utils.render_name.RenderName;

public class Mu043_GeneratorPooplineExperiment {
	
	private MucusOutputManager outputManager = LocalOutputManager.getInstance();
	
	public Mu043_GeneratorPooplineExperiment() {
		Poopline pipeline = new Poopline();
		pipeline.addPlugin(new RandomPhraseLength());
		pipeline.addPlugin(new RandomXMLKey());
		pipeline.setPrimaryPlugin(new RandomDiatonicTriadProgression());
		
		PooplinePackage pack = new PooplinePackage("one", new Random());
		pack = pipeline.process(pack);
		System.out.println(pack.toString());
		
		Mu melody = new Mu("melody");
		melody.addTag(MuTag.PART_MELODY);
		melody.addTag(MuTag.PRINT_CHORDS);
		pack.getMu().addMu(melody, 0);
		
		
		outputManager.outputToMusicXML(RenderName.dateAndTime(), pack.getMu());
	}
	
	
	
	public static void main(String[] args) {
		new Mu043_GeneratorPooplineExperiment();
	}
}
