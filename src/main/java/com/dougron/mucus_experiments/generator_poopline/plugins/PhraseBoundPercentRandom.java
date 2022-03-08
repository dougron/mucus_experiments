package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;

public class PhraseBoundPercentRandom extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(PhraseBoundPercentRandom.class);

	private double rangeLow = -0.05;
	private double rangeHigh = 0.25;
	
	private Parameter parameter;
	
	PhraseBoundRepo phraseBoundRepo;
	
	public PhraseBoundPercentRandom(Parameter aParameter) {
		super(
				new Parameter[] {aParameter},
				new Parameter[] {}
				);
		parameter = aParameter;
	}
			
	
	public PhraseBoundPercentRandom(Parameter aParameter, double aRangeLow, double aRangeHigh) {
		super(
				new Parameter[] {aParameter},
				new Parameter[] {}
				);
		parameter = aParameter;
		rangeLow = aRangeLow;
		rangeHigh = aRangeHigh;
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		if (pack.getRepo().containsKey(parameter) 
				&& pack.getRepo().get(parameter).getClassName().equals(getClass().getName())) {			
			// don't change existing info from same plugin
		} else {
			double rndValue = pack.getRnd().nextDouble();
			phraseBoundRepo = PhraseBoundRepo.builder()
					.rndValue(rndValue)
					.selectedValue((rangeHigh - rangeLow) * rndValue + rangeLow)
					.rangeLow(rangeLow)
					.rangeHigh(rangeHigh)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(parameter, phraseBoundRepo);
		}
		
		// for now, this does nothing to the Mu
		
		return pack;
	}

	
}
