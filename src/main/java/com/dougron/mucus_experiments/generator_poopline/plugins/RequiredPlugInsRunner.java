package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;

public class RequiredPlugInsRunner extends PlugGeneric {
	
	
	public static final Logger logger = LogManager.getLogger(RequiredPlugInsRunner.class);

	
	public RequiredPlugInsRunner(Parameter[] requiredParameters) {
		super(
				Parameter.PLUGIN_RUNNER,
				requiredParameters
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}

	
//	@Override
//	PooplinePackage updateMu(PooplinePackage pack)
//	{
//		//update pack.mu
//		return pack;
//	}
//
//
//	@Override
//	PooplinePackage makeRepo(PooplinePackage pack)
//	{
//		// make repo and drop in pack.repo
//		return pack;
//	}
//	
//	
//	@Override
//	void getRepoFromPack(PooplinePackage pack)
//	{
//		// TODO Auto-generated method stub
//	}
//
//	
//	@Override
//	void getAncilliaryRepos(PooplinePackage pack)
//	{}
//
//	@Override
//	public PooplinePackage process (PooplinePackage pack) {
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		pack = super.process(pack);
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}
}
