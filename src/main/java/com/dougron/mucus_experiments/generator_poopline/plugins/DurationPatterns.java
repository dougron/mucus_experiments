package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternsRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;


/*
 * very simple duration implementation. Legato, Staccato or Default_Short
 */

public class DurationPatterns extends PlugGeneric implements PooplinePlugin {

	public enum EndNoteDurationSolution {LEGATO_TILL_END_OF_BAR, STACCATO}
	
	public static final Logger logger = LogManager.getLogger(DurationPatterns.class);
	
	private DurationPatternsRepo durationPatternsRepo;
	TempoRepo tempoRepo;
	
	private int staccatoDurationInMilliseconds = 100;
	private static final double DEFAULT_STACCATO_IN_QUARTERS_IF_NO_TEMPO_EXISTS = 0.25;
//	private EndNoteDurationSolution endNoteSolution = EndNoteDurationSolution.LEGATO_TILL_END_OF_BAR;
	private double SHORT_DURATION_IN_QUARTERS = 0.5;	// staccato is an actual tempo dependant note, this is just to make short notes on the score
	
	private Map<Integer, DurationType[]> durationPatternMap;
	private int[] patternIndices;
	private MuTag tagToFilterStructureTones = MuTag.IS_STRUCTURE_TONE;
	private MuTag tagToFilterEmbellishments = MuTag.IS_EMBELLISHMENT;
	

	
	
	public DurationPatterns(int[] indices, Map<Integer, DurationType[]> aDurationPatternMap)
	{
		super(
				Parameter.DURATION_EMBELLISHMENT,
				new Parameter[] 
						{
								Parameter.TEMPO,
								Parameter.STRUCTURE_TONE_GENERATOR,
								Parameter.EMBELLISHMENT_GENERATOR
						}
				);
		durationPatternMap = aDurationPatternMap;
		patternIndices = indices;
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
		double staccatoDurationInQuarters = getStaccatoDurationInQuarters(pack);
		pack.getMu().makePreviousNextMusWithNotes();
		processMuDurations(pack, staccatoDurationInQuarters);
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		durationPatternsRepo = DurationPatternsRepo.builder()
				.durationPatternMap(durationPatternMap)
				.patternIndices(patternIndices)
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.tagToFilterStructureTones(tagToFilterStructureTones)
				.tagToFilterEmbellishments(tagToFilterEmbellishments)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.DURATION_EMBELLISHMENT, durationPatternsRepo);
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		durationPatternsRepo = (DurationPatternsRepo)pack.getRepo().get(Parameter.DURATION_EMBELLISHMENT);
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{}
	
	

	private void processMuDurations(PooplinePackage pack, double staccatoDurationInQuarters) {
		List<Mu> structureTones = pack.getMu().getMuWithTag(tagToFilterStructureTones);
		Collections.sort(structureTones, Mu.globalPositionInQuartersComparator);
		
		int patternIndex = 0;
		for (Mu structureTone: structureTones)
		{
			List<Mu> embellishments = structureTone.getMuWithTag(tagToFilterEmbellishments);
			Collections.sort(embellishments, Mu.globalPositionInQuartersComparator);
			DurationType[] dtypes = durationPatternMap.get(patternIndices[patternIndex]);
			int dTypeIndex = dtypes.length - 1;
			for (int i = embellishments.size() - 1; i > -1; i--)
			{
				Mu embellishment = embellishments.get(i);
				double durationInQuarters = 0.0;
				DurationType type = dtypes[dTypeIndex];
				switch (type)
				{
				case LEGATO:
					Mu nextMu = embellishment.getNextMu();
					if (nextMu == null)
					{
						durationInQuarters = SHORT_DURATION_IN_QUARTERS;
					}
					else
					{
						double thisGlobalPosition = embellishment.getGlobalPositionInQuarters();
						double nextGlobalPosition = embellishment.getNextMu().getGlobalPositionInQuarters();
						durationInQuarters = nextGlobalPosition - thisGlobalPosition;
					}
					break;
				case STACCATO:
					durationInQuarters = staccatoDurationInQuarters;
					break;
				case DEFAULT_SHORT:
					durationInQuarters = SHORT_DURATION_IN_QUARTERS;
					break;
				}
				embellishment.setLengthInQuarters(durationInQuarters);
				dTypeIndex--;
				if (dTypeIndex <= 0) dTypeIndex = dtypes.length - 1;
			}
			patternIndex++;
			if (patternIndex >= patternIndices.length) patternIndex = 0;
		}
	}



//	private void dealWithAllMuDurationExceptLast(double staccatoDurationInQuarters, List<Mu> muList) {
//		DurationType type;
//		double durationInQuarters = 0.0;
//		for (int i = 0; i < muList.size() - 1; i++)
//		{
//			type = durationPattern[i % durationPattern.length];
//			
//			switch (type)
//			{
//			case LEGATO:
//				double thisGlobalPosition = muList.get(i).getGlobalPositionInQuarters();
//				double nextGlobalPosition = muList.get(i).getNextMu().getGlobalPositionInQuarters();
//				durationInQuarters = nextGlobalPosition - thisGlobalPosition;
//				break;
//			case STACCATO:
//				durationInQuarters = staccatoDurationInQuarters;
//				break;
//			case DEFAULT_SHORT:
//				durationInQuarters = SHORT_DURATION_IN_QUARTERS;
//				break;
//			}
//			muList.get(i).setLengthInQuarters(durationInQuarters);
//		}
//	}



//	private void dealWithDurationOfLastMuInList(double staccatoDurationInQuarters, List<Mu> muList) {
//		if (muList.size() > 0)
//		{
//			Mu lastMu = muList.get(muList.size() - 1);
//			switch(endNoteSolution)
//			{
//			case LEGATO_TILL_END_OF_BAR:
//				setLastMuDurationToEndOfBar(lastMu);
//				break;
//			case STACCATO:
//				lastMu.setLengthInQuarters(staccatoDurationInQuarters);
//				break;
//			}
//		}
//	}



//	private void setLastMuDurationToEndOfBar(Mu lastMu) {
//		BarsAndBeats durationBab = new BarsAndBeats(1, 0.0);
//		if (lastMu.hasTag(MuTag.IS_SYNCOPATION))
//		{
//			BarsAndBeats currentPosition = lastMu.getGlobalPositionInBarsAndBeats();
//			ArrayList<MuTagBundle> bundleList = lastMu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
//			double originalPosition = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
//			BarsAndBeats originalBab = lastMu.getGlobalPositionInBarsAndBeats(originalPosition);
//			if (currentPosition.getBarPosition() < originalBab.getBarPosition())	// syncopation has placed the note outside of its original bar, therefore needs to jump barine and end on 2nd
//			{
//				durationBab.setBarPosition(2);
//			}
//			
//		}
//		lastMu.setLengthInBarsAndBeats(durationBab);
//	}



	private double getStaccatoDurationInQuarters(PooplinePackage pack) {
		double staccatoDurationInQuarters = DEFAULT_STACCATO_IN_QUARTERS_IF_NO_TEMPO_EXISTS;
		if (pack.getRepo().containsKey(Parameter.TEMPO))
		{
			TempoRepo tempoRepo = (TempoRepo)pack.getRepo().get(Parameter.TEMPO);
			staccatoDurationInQuarters = 60.0 / tempoRepo.getSelectedTempo() / 1000 * staccatoDurationInMilliseconds;
		}
		return staccatoDurationInQuarters;
	}



	public DurationPatterns setStaccatoDurationInMilliseconds(int staccatoDurationInMilliseconds) {
		this.staccatoDurationInMilliseconds = staccatoDurationInMilliseconds;
		return this;
	}



//	public DurationPatterns setEndNoteSolution(EndNoteDurationSolution endNoteSolution) {
//		this.endNoteSolution = endNoteSolution;
//		return this;
//	}
}
