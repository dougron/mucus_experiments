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

public class PhraseLengthSetLength extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(PhraseLengthSetLength.class);
	

	private PhraseLengthRepo phraseLengthRepo;

	private int lengthInBars;
	
	
	
	public PhraseLengthSetLength(int aLengthInBars) 
	{
		super(
				Parameter.PHRASE_LENGTH,
				new Parameter[] {}
				);
		lengthInBars = aLengthInBars;
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
		pack.getMu().setLengthInBars(phraseLengthRepo.getSelectedValue());
		return pack;
	}

	

	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		phraseLengthRepo = PhraseLengthRepo.builder()
				.selectedValue(lengthInBars)
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
	
	
	
	
	
}