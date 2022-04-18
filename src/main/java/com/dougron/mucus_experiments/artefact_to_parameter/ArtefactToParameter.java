package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;

public class ArtefactToParameter
{

	public static String sayHello()
	{
		return "hello";
		
	}

	public static PooplinePackage getPackFromMu(Mu aMu)
	{
		PooplinePackage pack = new PooplinePackage(aMu.getName(), new Random());
		pack = addPhraseLengthSetLengthRepo(aMu, pack);
		return pack;
	}

	private static PooplinePackage addPhraseLengthSetLengthRepo(Mu aMu, PooplinePackage pack)
	{
		PhraseLengthRepo repo = PhraseLengthRepo.builder()
				.rndValue(0.5)
				.selectedValue(aMu.getLengthInBars())
				.options(new int[] {aMu.getLengthInBars()})
				.className(PhraseLengthSetLength.class.getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, repo);
		return pack;
	}

}
