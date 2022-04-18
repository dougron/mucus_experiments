package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPattern;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.LoopModelSetter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ShouldIUseTheStructureToneSyncopator;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneSyncopatorInQuartersRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraSolverOneBreakpointRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EvenlySpacedStructureToneRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.RepoInterface;
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
		case "DiatonicTriadRepo":
			return getDiatonicTriadPlugin(repo);
		case "DurationPatternRepo":
			return getDurationPatternPlugin(repo);
		case "EvenlySpacedStructureToneRepo":
			return getEvenlySpacedStructureTonePlugin(repo);
		case "LoopModelRepo":
			return getLoopModelPlugin(repo);
		case "PatternEmbellishmentRepo":
			return getPatternEmbellishmentPlugin(repo);
		case "PhraseBoundRepo":
			return getPhraseBoundPlugin(repo);
		case "PhraseLengthRepo":
			return getPhraseLengthPlugin(repo);
		case "BooleanRepo":
			return getBooleanRepoRelatedPlugin(repo);
		case "StartNoteRepo":
			return getStartNotePlugin(repo);
		case "StructureToneSyncopationRepo":
			return getStructureToneSyncopationPlugin(repo);
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


	private static PooplinePlugin getStructureToneSyncopationPlugin(RepoInterface repo)
	{
		// currently only 1
		return new StructureToneSyncopatorInQuartersRandom();
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


	private static PooplinePlugin getEvenlySpacedStructureTonePlugin(RepoInterface repo)
	{
		EvenlySpacedStructureToneRepo nurepo = (EvenlySpacedStructureToneRepo)repo;
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
		case "EvenlySpacedStructureToneRandom":
			return new EvenlySpacedStructureToneRandom();
		case "EvenlySpacedStructureToneFixed":
			return new EvenlySpacedStructureToneFixed(nurepo.getSelectedValueInFloatBars());
		}
		return null;
	}


	private static PooplinePlugin getDurationPatternPlugin(RepoInterface repo)
	{
		// currently only 1
		DurationPatternRepo drepo = (DurationPatternRepo)repo;
		return new DurationPattern(drepo.getDurationPattern());
	}


	private static PooplinePlugin getDiatonicTriadPlugin(RepoInterface repo)
	{
		// currently only 1
		return new DiatonicTriadProgressionRandom();
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
