package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.BooleanRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;

public class ShouldIUseTheStructureToneSyncopator extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(ShouldIUseTheStructureToneSyncopator.class);

	private boolean[] options = new boolean[] {true, false};


	
	public ShouldIUseTheStructureToneSyncopator() {
		super(
				Parameter.USE_STRUCTURE_TONE_SYNCOPATOR,
				new Parameter[] {Parameter.STRUCTURE_TONE_SPACING}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
//	@Override
//	PooplinePackage updateMu(PooplinePackage pack)
//	{
//		//update pack.mu
//		return pack;
//	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		BooleanRepo repo = BooleanRepo.builder()
				.rndValue(rndValue)
				.selectedOption(options[(int)(options.length * rndValue)])
				.options(options)
				.className(getClass().getName())
				.build();		
		pack.getRepo().put(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR, repo);
		return pack;
	}
	
	
//	@Override
//	void getRepoFromPack(PooplinePackage pack)
//	{
//		// does not need to recover from repo as this is only necessary when mu needs updating
//	}

	
//	@Override
//	void getAncilliaryRepos(PooplinePackage pack)
//	{}
	
	
}
