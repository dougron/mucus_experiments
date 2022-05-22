package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

public class DurationModelFactory
{

	public static DurationModel getDurationModel(BarsAndBeats aBarsAndBeats)
	{
		return new DurationInBarsAndBeats(aBarsAndBeats);
	}
	
	
	
	public static DurationModel getDurationModel(DurationType aDurationType)
	{
		return new DurationLegato();
	}
}
