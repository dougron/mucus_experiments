package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;

@AllArgsConstructor
@ToString
public class DurationInBarsAndBeats implements DurationModel
{
	
	private BarsAndBeats durationInBarsAndBeats;

	@Override
	public void setDuration(Mu aMu)
	{
		List<MuTagBundle> bundleList = aMu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
		if (bundleList.size() > 0)
		{
			double desyncedGlobalPositionInQuarters = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
			double globalPositionInQuarters = aMu.getGlobalPositionInQuarters();
			int globalPositionInBars = aMu.getGlobalPositionInBars();
			int desyncedGlobalPositionInBars = aMu.getGlobalPositionInBarsAndBeats(desyncedGlobalPositionInQuarters).getBarPosition();
			aMu.setLengthInBarsAndBeats(BarsAndBeats.at(
					durationInBarsAndBeats.getBarPosition() + desyncedGlobalPositionInBars - globalPositionInBars,
					durationInBarsAndBeats.getOffsetInQuarters()));
		}
		else
		{
			aMu.setLengthInBarsAndBeats(durationInBarsAndBeats);
		}
	}
}
