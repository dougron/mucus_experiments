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
	
	

	public PluginThatDoesNothingButDemonstrateNewArchitecture()
	{
		super(
				Parameter.CHORD_LIST_GENERATOR,
				new Parameter[] {Parameter.PHRASE_LENGTH, Parameter.XMLKEY}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
	@Override
	PooplinePackage updateMu(PooplinePackage pack)
	{
		//update pack.mu
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		// make repo and drop in pack.repo
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		// TODO Auto-generated method stub
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{}

	

}
