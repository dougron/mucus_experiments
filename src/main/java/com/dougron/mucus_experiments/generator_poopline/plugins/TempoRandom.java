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
				new Parameter[] {Parameter.TEMPO},
				new Parameter[] {}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		logger.info("Received " + pack);
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.TEMPO) 
				&& pack.getRepo().get(Parameter.TEMPO).getClassName().equals(getClass().getName())) 
		{	
			// nothing required to be done
		} 
		else 
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
			pack.getMu().setStartTempo(tempo);
		}
		return pack;
	}
}
