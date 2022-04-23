package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;


/*
 * very simple duration implementation. Legato or Staccato
 */

public class DurationPattern extends PlugGeneric implements PooplinePlugin {

	public enum EndNoteDurationSolution {LEGATO_TILL_END_OF_BAR, STACCATO}
	
	public static final Logger logger = LogManager.getLogger(DurationPattern.class);
	
	private DurationPatternRepo durationPatternRepo;
	private int staccatoDurationInMilliseconds = 100;
	private static final double DEFAULT_STACCATO_IN_QUARTERS_IF_NO_TEMPO_EXISTS = 0.25;
	private EndNoteDurationSolution endNoteSolution = EndNoteDurationSolution.LEGATO_TILL_END_OF_BAR;
	
	
	private DurationType[] durationPattern;
	
	TempoRepo tempoRepo;
	
	
	public DurationPattern(DurationType[] aDurationPattern)
	{
		super(
				Parameter.DURATION,
				new Parameter[] {Parameter.TEMPO}
				);
		durationPattern = aDurationPattern;
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
		processMuDurations(pack, staccatoDurationInQuarters);
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		durationPatternRepo = DurationPatternRepo.builder()
				.durationPattern(durationPattern)
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.DURATION, durationPatternRepo);
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		durationPatternRepo = (DurationPatternRepo)pack.getRepo().get(Parameter.DURATION);
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{}
	
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) 
//	{
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		pack = super.process(pack);
//		double staccatoDurationInQuarters = getStaccatoDurationInQuarters(pack);
//		if (pack.getRepo().containsKey(Parameter.DURATION) 
//				&& pack.getRepo().get(Parameter.DURATION).getClassName().equals(getClass().getName())) 
//		{	
//			durationPatternRepo = (DurationPatternRepo)pack.getRepo().get(Parameter.DURATION);
//		} 
//		else
//		{
//			durationPatternRepo = DurationPatternRepo.builder()
//					.durationPattern(durationPattern)
//					.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
//					.className(getClass().getName())
//					.build();
//			pack.getRepo().put(Parameter.DURATION, durationPatternRepo);
//		}
//
//		processMuDurations(pack, staccatoDurationInQuarters);
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}



	private void processMuDurations(PooplinePackage pack, double staccatoDurationInQuarters) {
		List<Mu> muList = pack.getMu().getMusWithNotes();
		Collections.sort(muList, Mu.globalPositionInQuartersComparator);
		dealWithAllMuDurationExceptLast(staccatoDurationInQuarters, muList);
		dealWithDurationOfLastMuInList(staccatoDurationInQuarters, muList);
	}



	private void dealWithAllMuDurationExceptLast(double staccatoDurationInQuarters, List<Mu> muList) {
		DurationType type;
		double durationInQuarters = 0.0;
		for (int i = 0; i < muList.size() - 1; i++)
		{
			type = durationPattern[i % durationPattern.length];
			
			switch (type)
			{
			case LEGATO:
				double thisGlobalPosition = muList.get(i).getGlobalPositionInQuarters();
				double nextGlobalPosition = muList.get(i + 1).getGlobalPositionInQuarters();
				durationInQuarters = nextGlobalPosition - thisGlobalPosition;
				break;
			case STACCATO:
				durationInQuarters = staccatoDurationInQuarters;
				break;
			}
			muList.get(i).setLengthInQuarters(durationInQuarters);
		}
	}



	private void dealWithDurationOfLastMuInList(double staccatoDurationInQuarters, List<Mu> muList) {
		if (muList.size() > 0)
		{
			Mu lastMu = muList.get(muList.size() - 1);
			switch(endNoteSolution)
			{
			case LEGATO_TILL_END_OF_BAR:
				setLastMuDurationToEndOfBar(lastMu);
				break;
			case STACCATO:
				lastMu.setLengthInQuarters(staccatoDurationInQuarters);
				break;
			}
		}
	}



	private void setLastMuDurationToEndOfBar(Mu lastMu) {
		BarsAndBeats durationBab = new BarsAndBeats(1, 0.0);
		if (lastMu.hasTag(MuTag.IS_SYNCOPATION))
		{
			BarsAndBeats currentPosition = lastMu.getGlobalPositionInBarsAndBeats();
			ArrayList<MuTagBundle> bundleList = lastMu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
			double originalPosition = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
			BarsAndBeats originalBab = lastMu.getGlobalPositionInBarsAndBeats(originalPosition);
			if (currentPosition.getBarPosition() < originalBab.getBarPosition())	// syncopation has placed the note outside of its original bar, therefore needs to jump barine and end on 2nd
			{
				durationBab.setBarPosition(2);
			}
			
		}
		lastMu.setLengthInBarsAndBeats(durationBab);
	}



	private double getStaccatoDurationInQuarters(PooplinePackage pack) {
		double staccatoDurationInQuarters = DEFAULT_STACCATO_IN_QUARTERS_IF_NO_TEMPO_EXISTS;
		if (pack.getRepo().containsKey(Parameter.TEMPO))
		{
			TempoRepo tempoRepo = (TempoRepo)pack.getRepo().get(Parameter.TEMPO);
			staccatoDurationInQuarters = 60.0 / tempoRepo.getSelectedTempo() / 1000 * staccatoDurationInMilliseconds;
		}
		return staccatoDurationInQuarters;
	}



	public DurationPattern setStaccatoDurationInMilliseconds(int staccatoDurationInMilliseconds) {
		this.staccatoDurationInMilliseconds = staccatoDurationInMilliseconds;
		return this;
	}



	public DurationPattern setEndNoteSolution(EndNoteDurationSolution endNoteSolution) {
		this.endNoteSolution = endNoteSolution;
		return this;
	}
}
