package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.ParameterRepository;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_factory.PluginFactory;

public class ForceCreatePlugInsFromRepo extends PlugGeneric implements PooplinePlugin 
{
	
	public static final Logger logger = LogManager.getLogger(ForceCreatePlugInsFromRepo.class);

	
	
	public ForceCreatePlugInsFromRepo(Poopline parent)
	{
		super(
				Parameter.PLUGIN_RUNNER,
				new Parameter[] {}
				);
		this.parent = parent;
	}


	
	@Override
	public PooplinePackage process(PooplinePackage pack)
	{
		logger.info(getInfoLevelPackReceiptMessage(pack));
		ParameterRepository repo = pack.getRepo();
		for (Parameter parameter: repo.keySet())
		{
			PooplinePlugin plug = PluginFactory.getPlugin(repo.get(parameter));
			if (plug == null)
			{
				logger.error("PluginFactory returned a null plugin for " + repo.get(parameter).getClassName());
			}
			else
			{
				parent.addPlugin(plug);
			}
		}
		// plugins always processed in reverese order, to accommodate loading default plugins first
		// and them being encountered second
//		logger.debug(
//				"Poopline.plugin() content=" 
//						+ parent.getPlugins().stream()
//							.map(x -> "\n" + x.getClass().getSimpleName())
//							.collect(Collectors.toList())
				
//				);
		List<String> debugList = new ArrayList<String>();
		for (int i = parent.getPlugins().size() - 1; i >= 0; i--)
		{
			PooplinePlugin plug = parent.getPlugins().get(i);
			debugList.add(plug.getClass().getSimpleName());
			if (plug != this)
			{
				pack = plug.process(pack);
				logger.debug(
						"Plug just processed=" + plug.getClass().getSimpleName()
						+ parent.getPlugins().stream()
							.map(x -> "\n\t" + x.getClass().getSimpleName() + "\t" + x.isExecutedThisCycle())
							.collect(Collectors.toList())
						);
			}
		}
		logger.debug("processing order: " + debugList.stream().map(x -> "\n" + x).collect(Collectors.toList()));
		logger.debug(this.getClass().getSimpleName() + ".process() exited");
		return pack;
	}
	


}
