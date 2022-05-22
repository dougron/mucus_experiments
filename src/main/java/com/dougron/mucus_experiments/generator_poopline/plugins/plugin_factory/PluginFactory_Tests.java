package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionDiatonicTriadRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.LoopModelSetter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.BooleanRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ChordProgressionRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourMultiplierRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo.LoopModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.RepoInterface;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneSyncopationIntegerPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraSolverRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;

class PluginFactory_Tests
{
	
	
//	TestCase tc = new TestCase(new XmlKeyRandom(), XmlKeyRepo.builder().className(XmlKeyRandom.class.getName()).build());
	
	Object[][] testList = new Object[][] {
		new Object[] {new ContourChordTonesRandom(), ContourChordTonesRepo.builder().className(ContourChordTonesRandom.class.getName()).build()},
		new Object[] {new ContourMultiplierRandom(), ContourMultiplierRepo.builder().className(ContourMultiplierRandom.class.getName()).build()},
		new Object[] {new ChordProgressionDiatonicTriadRandom(), ChordProgressionRepo.builder().className(ChordProgressionDiatonicTriadRandom.class.getName()).build()},
		new Object[] {new DurationPattern(new DurationModel[] {}, null), DurationPatternRepo.builder().className(DurationPattern.class.getName()).build()},
		new Object[] {new StructureToneEvenlySpacedFixed(1.0), StructureToneEvenlySpacedRepo.builder().className(StructureToneEvenlySpacedFixed.class.getName()).build()},
		new Object[] {new LoopModelSetter(LoopModel.LOOP), LoopModelRepo.builder().className(LoopModelSetter.class.getName()).build()},
		new Object[] {new PatternEmbellisherRandom(), PatternEmbellishmentRepo.builder().className(PatternEmbellisherRandom.class.getName()).build()},
		new Object[] {new PhraseBoundPercentRandom(Parameter.PHRASE_START_PERCENT), 
				PhraseBoundRepo.builder()
				.parameter(Parameter.PHRASE_START_PERCENT)
				.className(PhraseBoundPercentRandom.class.getName())
				.build()
		},
		new Object[] {new PhraseBoundPercentSetAmount(Parameter.PHRASE_START_PERCENT, 0.1), 
				PhraseBoundRepo.builder()
				.parameter(Parameter.PHRASE_START_PERCENT)
				.className(PhraseBoundPercentSetAmount.class.getName())
				.build()
		},
		new Object[] {new PhraseLengthRandom(), PhraseLengthRepo.builder().className(PhraseLengthRandom.class.getName()).build()},
		new Object[] {new PhraseLengthSetLength(2), PhraseLengthRepo.builder().className(PhraseLengthSetLength.class.getName()).build()},
		new Object[] {new ShouldIUseTheStructureToneSyncopator(), BooleanRepo.builder().className(ShouldIUseTheStructureToneSyncopator.class.getName()).build()},
		new Object[] {new StartNoteMelodyRandom(),StartNoteRepo.builder().className(StartNoteMelodyRandom.class.getName()).build()},
		new Object[] {new StructureToneSyncopatorInQuartersRandom(),StructureToneSyncopationIntegerPatternRepo.builder().className(StructureToneSyncopatorInQuartersRandom.class.getName()).build()},
		new Object[] {new TempoRandom(), TempoRepo.builder().className(TempoRandom.class.getName()).build()},
		new Object[] {new TessituraFixed(Parameter.TESSITURA_MELODY_RANGE), 
				TessituraRepo.builder()
				.parameter(Parameter.TESSITURA_MELODY_RANGE)
				.className(TessituraFixed.class.getName())
				.build()
				},
		new Object[] {new TessituraSolverOneBreakpointRandom(Parameter.STRUCTURE_TONE_GENERATOR, MuTag.IS_STRUCTURE_TONE), TessituraSolverRepo.builder().className(TessituraSolverOneBreakpointRandom.class.getName()).build()},
		new Object[] {new TimeSignatureSingleRandom(), TimeSignatureRepo.builder().className(TimeSignatureSingleRandom.class.getName()).build()},
		new Object[] {new VectorChordTonesFixed(new int[] {1,-1}), VectorChordTonesRepo.builder().className(VectorChordTonesFixed.class.getName()).build()},
		new Object[] {new XmlKeyRandom(), XmlKeyRepo.builder().className(XmlKeyRandom.class.getName()).build()},
	};
	
	
	/*
	 * check logger output for which item it fails on, if it fails
	 */
	@Test
	void plugInFactory_return_correct_plugin_for_related_repo()
	{
		for (Object[] oarr: testList)
		{
			PooplinePlugin plug = (PooplinePlugin)oarr[0];
			RepoInterface repo = (RepoInterface)oarr[1];
//			System.out.println(plug.toString());
//			System.out.println(repo.toString());
			assertThat(PluginFactory.getPlugin(repo)).isInstanceOf(plug.getClass());
		}
	}

}


