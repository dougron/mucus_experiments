package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;


@AllArgsConstructor
public class DurationInFloatBarsToNearestStrength implements DurationModel
{
	
	@Getter private double lengthInFloatBars;
	@Getter private int toNearestBeatStrength;

	
	
	@Override
	public void setDuration(Mu aMu)
	{
		List<MuTagBundle> bundleList = aMu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
		double globalStartPositionWithoutSyncopation = getGlobalStartPositionWithoutSyncopation(aMu, bundleList);
		BarsAndBeats postionInBarsAndBeats = aMu.getGlobalPositionInBarsAndBeats(globalStartPositionWithoutSyncopation);
		int endBarIndex = postionInBarsAndBeats.getBarPosition() + (int)lengthInFloatBars;
		TimeSignature ts = aMu.getTimeSignatureFromGlobalBarPosition(endBarIndex);
		double positionInBarInQuarters = lengthInFloatBars % 1.0 * ts.getLengthInQuarters();

		List<Double> strengthPositions = getListOfStrengthPositions(ts);
		
		double finalPosition = getClosestStrengthPosition(positionInBarInQuarters, strengthPositions);
		
		BarsAndBeats currentPositionWithSyncopation = aMu.getGlobalPositionInBarsAndBeats();
		BarsAndBeats lengthInBarsAndBeats = BarsAndBeats.at(
				endBarIndex - currentPositionWithSyncopation.getBarPosition(), 
				finalPosition
				);
		aMu.setLengthInBarsAndBeats(lengthInBarsAndBeats);
	}

	
	
	private double getGlobalStartPositionWithoutSyncopation(Mu aMu, List<MuTagBundle> bundleList)
	{
		double globalStartPositionWithoutSyncopation;
		if (bundleList.size() > 0)
		{
			MuTagBundle bundle = bundleList.get(0);
			globalStartPositionWithoutSyncopation = (double)bundle.getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
		}	
		else
		{
			globalStartPositionWithoutSyncopation = aMu.getGlobalPositionInQuarters();
		}
		return globalStartPositionWithoutSyncopation;
	}

	
	
	private List<Double> getListOfStrengthPositions(TimeSignature ts)
	{
		List<Double> strengthPositions = new ArrayList<Double>();
		for (Entry<Double, Integer> entry: ts.getStrengthMap().entrySet())
		{
			if (entry.getValue() == toNearestBeatStrength) strengthPositions.add(entry.getKey());
		}
		return strengthPositions;
	}

	
	
	private double getClosestStrengthPosition(double positionInBarInQuarters, List<Double> strengthPositions)
	{
		double distance = 10^6;	// arbitrarily large
		double finalPosition = 0.0;
		for (Double pos: strengthPositions)
		{
			double newDistance = Math.abs(pos - positionInBarInQuarters);
			if (newDistance < distance)
			{
				finalPosition = pos;
				distance = newDistance;
			}
		}
		return finalPosition;
	}

}





















