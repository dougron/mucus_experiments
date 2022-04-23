package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;

public class TempoRandom extends PlugGeneric implements PooplinePlugin {

	
	public static final Logger logger = LogManager.getLogger(TempoRandom.class);
	
	private int highLimit = 200;
	private int lowLimit = 55;
	
	TempoRepo tempoRepo;
	
	
	public TempoRandom()
	{
		super(
				Parameter.TEMPO,
				new Parameter[] {}
				);
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
		if (tempoRepo == null)
		{
			logger.info("mu not updated as tempoRepo is null");
		}
		else
		{
			pack.getMu().setStartTempo(tempoRepo.getSelectedTempo());
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		int tempo = (int)((highLimit - lowLimit) * rndValue + lowLimit);
		tempoRepo = TempoRepo.builder()
				.rndValue(rndValue)
				.selectedTempo(tempo)
				.highLimit(highLimit)
				.lowLimit(lowLimit)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.TEMPO, tempoRepo);
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		tempoRepo = (TempoRepo)pack.getRepo().get(Parameter.TEMPO);
	}

	
	
}
