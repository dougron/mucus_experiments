package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;


@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DurationStaccato implements DurationModel
{
	
	private int staccatoDurationInMilliseconds = 100;

	
	@Override
	public void setDuration(Mu aMu)
	{
		double tempo = aMu.getStartTempo();
		double duration = tempo / 60 * staccatoDurationInMilliseconds / 1000;
		aMu.setLengthInQuarters(duration);
	}

}
