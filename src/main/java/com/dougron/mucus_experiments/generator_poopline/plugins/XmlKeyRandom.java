package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;

public class XmlKeyRandom extends PlugGeneric implements PooplinePlugin {
	
	
	public static final Logger logger = LogManager.getLogger(XmlKeyRandom.class);

	
	int[] options = new int[] {-5,-4,-3,-2,-1,0,1,2,3,4,5,6};


	private XmlKeyRepo xmlKeyRepo;
	
	public XmlKeyRandom() {
		super(
				Parameter.XMLKEY,
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
	PooplinePackage updateMu(PooplinePackage pack)
	{
		if (pack.getRepo().containsKey(Parameter.XMLKEY) 
				&& pack.getRepo().get(Parameter.XMLKEY).getClassName().equals(getClass().getName())) {
			if (xmlKeyRepo == null) {
				xmlKeyRepo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
			}
	
			pack.getMu().setXMLKey(xmlKeyRepo.getSelectedValue());
		}
		return pack;
	}

	

	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		xmlKeyRepo = XmlKeyRepo.builder()
				.rndValue(rndValue)
				.selectedValue(getSelectedValue(rndValue))
				.options(options)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.XMLKEY, xmlKeyRepo);	
		return pack;
	}

	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		xmlKeyRepo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
	}

	
	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{}
	
	
	
	private int getSelectedValue(double rndValue) {
		int index = (int)(options.length * rndValue);
		return options[index];
	}

	
	
	public int[] getOptions() {
		return options;
	}
}