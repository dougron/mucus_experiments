package main.java.com.dougron.mucus_experiments.generator_poopline.poopback;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;

public class ContourShouldSpendMoreTimeDecending implements PoopbackObject
{

	@Override
	public PooplinePackage getNewPackWithParametersToChangeOmittedFromRepo(PooplinePackage aPack)
	{
		PooplinePackage pack = new PooplinePackage("variation", aPack.getRnd());
		for (Parameter key: aPack.getRepo().keySet())
		{
			if (key == Parameter.STRUCTURE_TONE_VECTOR
					|| key == Parameter.STRUCTURE_TONE_CONTOUR
					)
			{}
			else
			{
				pack.getRepo().put(key, aPack.getRepo().get(key).deepCopy());
			}
		}
		return pack;
	}

}
