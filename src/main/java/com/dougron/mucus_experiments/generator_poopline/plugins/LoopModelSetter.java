package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.LoopModelRepo.LoopModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

public class LoopModelSetter extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(LoopModelSetter.class);
	private LoopModel loopModel;
	private LoopModelRepo loopModelRepo;
	



	public LoopModelSetter(LoopModel aLoopModel) {
		super(
				new Parameter[] {Parameter.LOOP_MODEL},
				new Parameter[] {}
				);
		loopModel = aLoopModel;
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info(getInfoLevelPackReceiptMessage(pack));
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.LOOP_MODEL))
		{
			
		}
		else
		{
			loopModelRepo = LoopModelRepo.builder()
					.selectedLoopModel(loopModel)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.LOOP_MODEL, loopModelRepo);
		}
		
		return pack;
	}

}
