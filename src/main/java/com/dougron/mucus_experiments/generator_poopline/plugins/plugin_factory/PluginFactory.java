package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionDiatonicTriadRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionFloatBarFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationFixedInQuarters;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPatterns;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EmbellishmentFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.LoopModelSetter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlaceHolderRenderParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneUnevenlySpacedFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationFixedInQuartersRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternsRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PlaceHolderRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.RepoInterface;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneUnevenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraSolverRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;


/*
 * To add a new plugin to the factory:
 * 		- create the test case in the list in PluginFactory_Tests, and have it fail in true TDD style
 * 		- if there is a new Repo involved, add an item to the switch statement in getPlugIn below
 * 			and make the method to return the required plugin
 * 		- if the Repo is already represented, you need to sort out the method for that repo to return a
 * 			valid plugin under the right conditions.
 * 
 * 		NB: remember some complex plugins (like ContourChordTonesRandom) have many repos as fields 
 * 			to capture the detail of earlier decisions on which they are dependant. The repo such 
 * 			plugins are associated with are the ones that capture their behaviour, and most often
 * 			carry a similar name to the plugin.
 * 			.... unles of course they are using a super generic BooleanRepo or the like.... 
 * 			
 */


public class PluginFactory
{

	public static final Logger logger = LogManager.getLogger(PluginFactory.class);
	
	
	public static PooplinePlugin getPlugin(RepoInterface repo)
	{
		logger.info("Received repo of class=" + repo.getClass().getSimpleName() + " and className=" + repo.getClassName());
		switch (repo.getClass().getSimpleName())
		{
		case "ContourChordTonesRepo":
			return getContourChordTonesPlugin(repo);
		case "ContourMultiplierRepo":
			return getContourMultiplierPlugin(repo);
		case "ChordProgressionRepo":
			return getChordProgressionPlugin(repo);
		case "DurationPatternRepo":
			return getDurationPatternPlugin(repo);
		case "DurationPatternsRepo":
			return getDurationPatternsPlugin(repo);
		case "DurationFixedInQuartersRepo":
			return getDurationFixedInQuartersPlugin(repo);
		case "EmbellishmentFixedRepo":
			return getEmbellishmentFixedPlugin(repo);
		case "StructureToneEvenlySpacedRepo":
			return getStructureToneEvenlySpacedPlugin(repo);
		case "StructureToneUnevenlySpacedRepo":
			return getStructureToneUnevenlySpacedPlugin(repo);
		case "LoopModelRepo":
			return getLoopModelPlugin(repo);
		case "PatternEmbellishmentRepo":
			return getPatternEmbellishmentPlugin(repo);
		case "PhraseBoundRepo":
			return getPhraseBoundPlugin(repo);
		case "PhraseLengthRepo":
			return getPhraseLengthPlugin(repo);
		case "PlaceHolderRepo":
			return getPlaceholderRenderPlugin(repo);
		case "BooleanRepo":
			return getBooleanRepoRelatedPlugin(repo);
		case "StartNoteRepo":
			return getStartNotePlugin(repo);
		case "StructureToneSyncopationIntegerPatternRepo":
			return getStructureToneSyncopationIntegerPlugin(repo);
		case "StructureToneSyncopationDoublePatternRepo":
			return getStructureToneSyncopationDoublePlugin(repo);
		case "TempoRepo":
			return getTempoPlugin(repo);
		case "TessituraRepo":
			return getTessituraPlugin(repo);
		case "TessituraSolverRepo":
			return getTessituraSolverPlugin(repo);
		case "TimeSignatureRepo":
			return getTimeSignaturePlugin(repo);
		case "VectorChordTonesRepo":
			return getVectorChordTonesPlugin(repo);
		case "XmlKeyRepo":
			return getXmlKeyPlugin(repo);
		default:
			return null;
		}
	}

	
	
	private static PooplinePlugin getPlaceholderRenderPlugin(RepoInterface repo)
	{
		PlaceHolderRepo phRepo = (PlaceHolderRepo) repo;
		return new PlaceHolderRenderParameter(phRepo.getParameter());
	}



	private static PooplinePlugin getEmbellishmentFixedPlugin(RepoInterface repo)
	{
		return new EmbellishmentFixed();
	}



	private static PooplinePlugin getVectorChordTonesPlugin(RepoInterface repo)
	{
		// currently only 1
		VectorChordTonesRepo vrepo = (VectorChordTonesRepo)repo;
		return new VectorChordTonesFixed(vrepo.getSelectedVectorArray());
	}


	
	private static PooplinePlugin getTessituraSolverPlugin(RepoInterface repo)
	{
		// currently only 1
		TessituraSolverRepo tsrepo = (TessituraSolverRepo)repo;
		return new TessituraSolverOneBreakpointRandom(tsrepo.getTessituraParameter(), tsrepo.getMuTagToActUpon());
	}

	

	private static PooplinePlugin getTessituraPlugin(RepoInterface repo)
	{
		// currently only 1
		TessituraRepo trepo = (TessituraRepo)repo;
		return new TessituraFixed(trepo.getParameter());
	}

	

	private static PooplinePlugin getTempoPlugin(RepoInterface repo)
	{
		// currently only 1
		return new TempoRandom();
	}


	
	private static PooplinePlugin getStructureToneSyncopationIntegerPlugin(RepoInterface repo)
	{
		// currently only 1
		return new StructureToneSyncopatorInQuartersRandom();
	}
	
	
	
	private static PooplinePlugin getStructureToneSyncopationDoublePlugin(RepoInterface repo)
	{
		// currently only 1
		return new StructureToneSyncopatorInQuartersFixed();
	}


	
	private static PooplinePlugin getStartNotePlugin(RepoInterface repo)
	{
		// currently only 1
		return new StartNoteMelodyRandom();
	}


	
	private static PooplinePlugin getBooleanRepoRelatedPlugin(RepoInterface repo)
	{
		// currently only 1
		return new ShouldIUseTheStructureToneSyncopator();
	}


	
	private static PooplinePlugin getPhraseBoundPlugin(RepoInterface repo)
	{
		PhraseBoundRepo nurepo = (PhraseBoundRepo)repo;
		String cName = repo.getClassName();
//		logger.info("repo has className=" + cName);
		String[] split = cName.split("\\.");
		if (split.length == 0)
		{
			logger.info("className.split produced no options for repo=" + repo.getClass().getName());
			return null;
		}
		switch (split[split.length - 1])
		{
		case "PhraseBoundPercentRandom":
			return new PhraseBoundPercentRandom(nurepo.getParameter());
		case "PhraseBoundPercentSetAmount":
			return new PhraseBoundPercentSetAmount(nurepo.getParameter(), nurepo.getSelectedValue());
		}
		return null;
	}

	

	private static PooplinePlugin getPatternEmbellishmentPlugin(RepoInterface repo)
	{
		// currently only 1
		return new PatternEmbellisherRandom();
	}

	

	private static PooplinePlugin getLoopModelPlugin(RepoInterface repo)
	{
		// currently only 1
		LoopModelRepo lmrepo = (LoopModelRepo)repo;
		return new LoopModelSetter(lmrepo.getSelectedLoopModel());
	}


	
	private static PooplinePlugin getStructureToneEvenlySpacedPlugin(RepoInterface repo)
	{
		StructureToneEvenlySpacedRepo nurepo = (StructureToneEvenlySpacedRepo)repo;
		String cName = repo.getClassName();
//		logger.info("repo has className=" + cName);
		String[] split = cName.split("\\.");
		if (split.length == 0)
		{
			logger.info("className.split produced no options for repo=" + repo.getClass().getName());
			return null;
		}
		switch (split[split.length - 1])
		{
		case "StructureToneEvenlySpacedRandom":
			return new StructureToneEvenlySpacedRandom();
		case "StructureToneEvenlySpacedFixed":
			return new StructureToneEvenlySpacedFixed(nurepo.getSelectedValueInFloatBars());
		}
		return null;
	}
	
	
	
	private static PooplinePlugin getStructureToneUnevenlySpacedPlugin(RepoInterface repo)
	{
		StructureToneUnevenlySpacedRepo nurepo = (StructureToneUnevenlySpacedRepo)repo;
		String cName = repo.getClassName();
		String[] split = cName.split("\\.");
		if (split.length == 0)
		{
			logger.info("className.split produced no options for repo=" + repo.getClass().getName());
			return null;
		}
		return new StructureToneUnevenlySpacedFixed(nurepo.getSelectedValuesInFloatBars());
	}

	

	private static PooplinePlugin getDurationFixedInQuartersPlugin(RepoInterface repo)
	{
		// currently only 1
		DurationFixedInQuartersRepo drepo = (DurationFixedInQuartersRepo)repo;
		return new DurationFixedInQuarters(drepo.getDurationPattern());
	}

	
	
	private static PooplinePlugin getDurationPatternPlugin(RepoInterface repo)
	{
		// currently only 1
		DurationPatternRepo drepo = (DurationPatternRepo)repo;
		return new DurationPattern(drepo.getDurationModelPattern(), drepo.getTagToActUpon());
	}
	
	
	
	private static PooplinePlugin getDurationPatternsPlugin(RepoInterface repo)
	{
		// currently only 1
		DurationPatternsRepo drepo = (DurationPatternsRepo)repo;
		return new DurationPatterns(drepo.getPatternIndices(), drepo.getDurationPatternMap());
	}
	

	
	private static PooplinePlugin getChordProgressionPlugin(RepoInterface repo)
	{
//		ChordProgressionRepo nurepo = (ChordProgressionRepo)repo;
		String cName = repo.getClassName();
		String[] split = cName.split("\\.");
		if (split.length == 0)
		{
			logger.info("className.split produced no options for repo=" + repo.getClass().getName());
			return null;
		}
		switch (split[split.length - 1])
		{
		case "ChordProgressionDiatonicTriadRandom":
			return new ChordProgressionDiatonicTriadRandom();
		case "ChordProgressionFloatBarFixed":
			return new ChordProgressionFloatBarFixed();
		}
		return null;
	}

	

	private static PooplinePlugin getContourMultiplierPlugin(RepoInterface repo)
	{
		// currently only 1
		return new ContourMultiplierRandom();
	}

	

	private static PooplinePlugin getContourChordTonesPlugin(RepoInterface repo)
	{
		// currently only 1
		return new ContourChordTonesRandom();
	}

	

	private static PooplinePlugin getXmlKeyPlugin(RepoInterface repo)
	{
		// currently only 1
		return new XmlKeyRandom();
	}
	
	
	
	private static PooplinePlugin getTimeSignaturePlugin(RepoInterface repo)
	{
		// currently only 1
		return new TimeSignatureSingleRandom();
	}

	

	private static PooplinePlugin getPhraseLengthPlugin(RepoInterface repo)
	{
		PhraseLengthRepo nurepo = (PhraseLengthRepo)repo;
		String cName = repo.getClassName();
		logger.info("repo has className=" + cName);
		String[] split = cName.split("\\.");
		if (split.length == 0)
		{
			logger.info("className.split produced no options");
			return null;
		}
		switch (split[split.length - 1])
		{
		case "PhraseLengthRandom":
			return new PhraseLengthRandom();
		case "PhraseLengthSetLength":
			return new PhraseLengthSetLength(nurepo.getSelectedValue());
			
		}
		return null;
	}
}
