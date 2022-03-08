package main.java.com.dougron.mucus_experiments.generator_poopline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugGeneric;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;

public class RequiredPlugInsRunner extends PlugGeneric {
	
	
	public static final Logger logger = LogManager.getLogger(RequiredPlugInsRunner.class);

	
	public RequiredPlugInsRunner(Parameter[] requiredParameters) {
		super(
				new Parameter[] {},
				requiredParameters
				);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		
		return pack;
	}
}
