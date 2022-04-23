package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.test_plugs;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PlugGeneric;

public class PlugB extends PlugGeneric implements PooplinePlugin {

	
	public PlugB() {
		super(
				Parameter.PHRASE_LENGTH,
				new Parameter[] {}
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
