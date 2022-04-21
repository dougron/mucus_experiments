package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;

public class PluginThatDoesNothingButDemonstrateNewArchitecture extends PlugGeneric 
{
	
	
	public static final Logger logger = LogManager.getLogger(PluginThatDoesNothingButDemonstrateNewArchitecture.class);
	@Getter @Setter private boolean executedThisCycle = false;
	

	public PluginThatDoesNothingButDemonstrateNewArchitecture()
	{
		super(
				new Parameter[] {Parameter.CHORD_LIST_GENERATOR},
				new Parameter[] {Parameter.PHRASE_LENGTH, Parameter.XMLKEY}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		if (executedThisCycle)
		{
			logger.debug("already executed this cycle.");
		}
		else
		{
			if (!requiredParameterPluginsExist())
			{
				logger.info("Required plugins not present in Pipeline");
			}
			else
			{
				pack = runRequiredPlugins(pack);
				getAncilliaryRepos(pack);
				if (!doesThisRepoAlreadyExistInThePack(pack))
				{
					pack = makeRepo(pack);
				}
				pack = updateMu(pack);
				setExecutedThisCycle(true);
			}
		}
		
		return pack;
	}

	

	private PooplinePackage updateMu(PooplinePackage pack)
	{
		//update pack.mu
		return pack;
	}


	
	private PooplinePackage makeRepo(PooplinePackage pack)
	{
		// make repo and drop in pack.repo
		return pack;
	}

	

	private void getAncilliaryRepos(PooplinePackage pack)
	{
		// TODO Auto-generated method stub
		
	}


	private boolean doesThisRepoAlreadyExistInThePack(PooplinePackage pack)
	{
		return 
				pack.getRepo().containsKey(getPrimaryRenderParameter())
				&& pack.getRepo().get(getPrimaryRenderParameter()).getClassName()
				== this.getClass().getName();
	}


	
	private PooplinePackage runRequiredPlugins(PooplinePackage pack)
	{
		for (Parameter p: getRequiredParameters())
		{
			PooplinePlugin plug = parent.getPluginThatSolves(p);
			pack = plug.process(pack);
		}
		return pack;
	}


	
	private boolean requiredParameterPluginsExist()
	{
		
		if (parent == null) 
		{
			logger.warn("has no parent Pipeline. No access to required plugins");
			return false;
		}
		boolean b = true;
		for (Parameter p: getRequiredParameters())
		{
			if (!parent.hasPluginThatCanSupply(p))
			{
				b = false;
			}
		}
		return b;
	}

}
