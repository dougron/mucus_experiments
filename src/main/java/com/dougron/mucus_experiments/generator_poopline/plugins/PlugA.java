package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;

public class PlugA extends PlugGeneric implements PooplinePlugin {
	
	
	public static final Logger logger = LogManager.getLogger(PlugA.class);

	
	public PlugA() {
		super(
				new Parameter[] {Parameter.CHORD_LIST_GENERATOR},
				new Parameter[] {Parameter.PHRASE_LENGTH}
				);
	}
	
	
	
	@Override
	public PooplinePackage process(PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		for (Parameter p: getRenderParameters()) {
			pack.addItemToJson(p.toString(), "placeholder");
		}
		return pack;
	}
}
