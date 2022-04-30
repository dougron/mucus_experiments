package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;

public class PhraseBoundPercentSetAmount extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(PhraseBoundPercentSetAmount.class);
	
	private Parameter parameter;
	private double phraseBoundPercent;
	
	PhraseBoundRepo phraseBoundRepo;
	
	
	
	public PhraseBoundPercentSetAmount(Parameter aParameter, double aPhraseBoundPercent) 
	{
		super(
				aParameter,
				new Parameter[] {}
				);
		parameter = aParameter;
		phraseBoundPercent = aPhraseBoundPercent;
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
	
	@Override
	PooplinePackage updateMu(PooplinePackage pack)
	{
		//update pack.mu
		return pack;
	}


	
	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		phraseBoundRepo = PhraseBoundRepo.builder()
				.selectedValue(phraseBoundPercent)
				.parameter(parameter)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(parameter, phraseBoundRepo);
		return pack;
	}
	
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		phraseBoundRepo = (PhraseBoundRepo)pack.getRepo().get(parameter);
	}

	
	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{}
			
		
	
}
