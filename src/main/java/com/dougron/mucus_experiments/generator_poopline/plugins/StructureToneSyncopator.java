package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;

public class StructureToneSyncopator extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(StructureToneSyncopator.class);
	
	int[] options = new int[] {2,3,4,5,6,7,8,9,10,11,12};

	private PhraseLengthRepo phraseLengthRepo;
	
	public StructureToneSyncopator() {
		super(
				new Parameter[] {},
				new Parameter[] {}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info(getInfoLevelPackReceiptMessage(pack));
		pack = super.process(pack);
		
		logger.debug(this.getClass().getSimpleName() + ".process() exited");
		return pack;	
	}
}
