package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

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
				new Parameter[] {},
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
		for (PooplinePlugin plug: parent.getPlugins())
		{
			if (plug != this)
			{
				pack = plug.process(pack);
			}
		}
		return pack;
	}
	

//	
//	
//	@Override
//	public void setParent(Poopline aPoopline)
//	{
//		// TODO Auto-generated method stub
//
//	}
//
//	
//	
//	@Override
//	public boolean isValidated()
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	
//	
//	@Override
//	public boolean canSupplyParameter(Parameter aParameter)
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	
//	
//	@Override
//	public Parameter[] getRenderParameters()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}

}
