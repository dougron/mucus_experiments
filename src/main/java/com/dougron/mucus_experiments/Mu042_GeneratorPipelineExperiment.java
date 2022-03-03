package main.java.com.dougron.mucus_experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import main.java.com.dougron.mucus_experiments.generator_pipeline.GeneratorPipeline;
import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomOneDiatonicChordPerBar;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomPhraseLengthInBars;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomSingleTimeSignature;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomTempo;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.RandomXMLKey;

/*
 * most likely to be a reimagining of the RandomMelodyGenerator with modular bits
 * 
 */

public class Mu042_GeneratorPipelineExperiment {
	
	String jsonPath = "D:/Documents/miscForBackup/Mu042/Mu042output.json";
	
	public Mu042_GeneratorPipelineExperiment() {
		GeneratorPipeline gp = GeneratorPipeline.builder()
				.plug(new RandomPhraseLengthInBars())
				.plug(new RandomXMLKey())
				.plug(new RandomSingleTimeSignature())
				.plug(new RandomOneDiatonicChordPerBar())
				.plug(new RandomTempo())
//				.plug(new RandomPhraseStartAndEndPercent())
//				.plug(new RandomStructureToneSpacing())
//				.plug(new RandomStartNote())
//				.plug(new RandomStructureToneContour())
//				.plug(new RandomEmbellishmentClearanceValueInFloatBars())
//				.plug(new RandomEmbellishmentRepetitionPattern())
//				.plug(new RandomRhythmAndEmbellishmentPairs())
				.build();
		PipelinePackage pp = gp.getPackage(new Random());
		System.out.println(pp.getRndContainer().toString(4));
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(new File(jsonPath)));
			bf.write(pp.getRndContainer().toString());
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		new Mu042_GeneratorPipelineExperiment();
	}
}
