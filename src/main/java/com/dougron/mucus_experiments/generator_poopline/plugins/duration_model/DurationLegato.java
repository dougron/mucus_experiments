package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DurationLegato implements DurationModel
{
	
	
	private DurationModel defaultDurationModelInAbscenceOfNextMu
		= DurationModelFactory.getDurationModel(BarsAndBeats.at(1, 0.0));

	
	@Override
	public void setDuration(Mu aMu)
	{
		if (aMu.getNextMu() == null)
		{
			defaultDurationModelInAbscenceOfNextMu .setDuration(aMu);
		}
		else
		{
			double thisPosition = aMu.getGlobalPositionInQuarters();
			double nextPosition = aMu.getNextMu().getGlobalPositionInQuarters();
			double durationInQuarters = nextPosition - thisPosition;
			aMu.setLengthInQuarters(durationInQuarters);
		}
	}
}
