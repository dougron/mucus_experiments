package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;

@AllArgsConstructor
@ToString
public class DurationInQuarters implements DurationModel
{
	
	private double durationInQuarters;
	public static double SHORT_DURATION_IN_QUARTERS = 0.5;	// staccato is an actual tempo dependant note, this is just to make short notes on the score
	
	
	@Override
	public void setDuration(Mu aMu)
	{
		aMu.setLengthInQuarters(durationInQuarters);
	}

}
