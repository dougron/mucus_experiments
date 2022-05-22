package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PlaceHolderRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

/* returns true that the given render parameter exists. 
 * Does nothing else
 * 
 */
public class PlaceHolderRenderParameter extends PlugGeneric implements PooplinePlugin  {
	
	
	public static final Logger logger = LogManager.getLogger(PlaceHolderRenderParameter.class);
	PlaceHolderRepo repo;

	
	public PlaceHolderRenderParameter(Parameter aRenderParameter) {
		super(
				aRenderParameter,
				new Parameter[] {}
				);
	}

	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	

	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		repo = PlaceHolderRepo.builder()
				.parameter(getRenderParameter())
				.className(getClass().getName())
				.build();
		pack.getRepo().put(getRenderParameter(), repo);
		return pack;
	}
	
	
}
