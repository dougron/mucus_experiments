package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;

/*
 * This could totally not be neccessary but going leave it in to see if it t 
 * has any merit.
 * Alternative is to pass the high and low values as arguments.....
 */
public class TessituraFixed extends PlugGeneric implements PooplinePlugin  {
	
	
	public static final Logger logger = LogManager.getLogger(TessituraFixed.class);

	
	private int highValue = 70;
	private int lowValue = 42;


	private TessituraRepo tessituraRepo;
	
	public TessituraFixed() {
		super(
				new Parameter[] {Parameter.TESSITURA},
				new Parameter[] {}
				);
	}
	
	
	public TessituraFixed(int aLowValue, int aHighValue) {
		super(
				new Parameter[] {Parameter.TESSITURA},
				new Parameter[] {}
				);
		lowValue = aLowValue;
		highValue = aHighValue;
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.TESSITURA) 
				&& pack.getRepo().get(Parameter.TESSITURA).getClassName().equals(getClass().getName())) {			
		} else {
			tessituraRepo = TessituraRepo.builder()
					.lowValue(lowValue)
					.highValue(highValue)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.TESSITURA, tessituraRepo);
		}
		return pack;
	}
}
