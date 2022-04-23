package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;

public class PhraseLengthRandom extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(PhraseLengthRandom.class);
	
	int[] options = new int[] {2,3,4,5,6,7,8,9,10,11,12};

	private PhraseLengthRepo phraseLengthRepo;
	
	public PhraseLengthRandom() {
		super(
				Parameter.PHRASE_LENGTH,
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
		if (pack.getRepo().containsKey(Parameter.PHRASE_LENGTH) 
				&& pack.getRepo().get(Parameter.PHRASE_LENGTH).getClassName().equals(getClass().getName())) {
			if (phraseLengthRepo == null) {
				phraseLengthRepo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
			}
	
			pack.getMu().setLengthInBars(phraseLengthRepo.getSelectedValue());
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		phraseLengthRepo = PhraseLengthRepo.builder()
				.rndValue(rndValue)
				.selectedValue(getSelectedValue(rndValue))
				.options(options)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, phraseLengthRepo);	
		return pack;
	}
	
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		phraseLengthRepo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
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