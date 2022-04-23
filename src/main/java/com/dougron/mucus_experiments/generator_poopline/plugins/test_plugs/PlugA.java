package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.test_plugs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugGeneric;

public class PlugA extends PlugGeneric implements PooplinePlugin {
	
	
	public static final Logger logger = LogManager.getLogger(PlugA.class);

	
	public PlugA() {
		super(
				Parameter.CHORD_LIST_GENERATOR,
				new Parameter[] {
						Parameter.PHRASE_LENGTH}
				);
	}
	
	
	
	@Override
	public PooplinePackage process(PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		pack.addItemToJson(getRenderParameter().toString(), "placeholder");
		return pack;
	}
}
