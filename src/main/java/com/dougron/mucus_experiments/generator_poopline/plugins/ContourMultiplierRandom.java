package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourMultiplierRepo;

public class ContourMultiplierRandom  extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(ContourMultiplierRandom.class);
	
	int highValue = 14;
	int lowValue = 2;

	private ContourMultiplierRepo contourMultiplierRepo;
	
	public ContourMultiplierRandom() {
		super(
				new Parameter[] {Parameter.STRUCTURE_TONE_MULTIPLIER},
				new Parameter[] {}
				);
	}
	
	
	public ContourMultiplierRandom(int aLowValue, int aHighValue) {
		super(
				new Parameter[] {Parameter.STRUCTURE_TONE_MULTIPLIER},
				new Parameter[] {}
				);
		lowValue = aLowValue;
		highValue = aHighValue;
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_MULTIPLIER) 
				&& pack.getRepo().get(Parameter.STRUCTURE_TONE_MULTIPLIER).getClassName().equals(getClass().getName())) {			
		} else {
			double rndValue = pack.getRnd().nextDouble();
			int multiplier = (int)(rndValue * (highValue - lowValue)) + lowValue;
			contourMultiplierRepo = ContourMultiplierRepo.builder()
					.rndValue(rndValue)
					.multiplier(multiplier)
					.lowValue(lowValue)
					.highValue(highValue)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.STRUCTURE_TONE_MULTIPLIER, contourMultiplierRepo);
		}
		return pack;
	}

}
