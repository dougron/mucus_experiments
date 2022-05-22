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
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationInBarsAndBeats;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationModel;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DurationPatternRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;


/*
 * very simple duration implementation. Legato, Staccato or Default_Short
 */

public class DurationPattern extends PlugGeneric implements PooplinePlugin {

//	public enum EndNoteDurationSolution {LEGATO_TILL_END_OF_BAR, STACCATO}
	
	public static final Logger logger = LogManager.getLogger(DurationPattern.class);
	
	private DurationPatternRepo durationPatternRepo;
	private int staccatoDurationInMilliseconds = 100;
	private static final double DEFAULT_STACCATO_IN_QUARTERS_IF_NO_TEMPO_EXISTS = 0.25;
	private DurationModel endNoteDurationModel = new DurationInBarsAndBeats(BarsAndBeats.at(1, 0));
	
	
	TempoRepo tempoRepo;

	private MuTag tagToActUpon;	// if parameterScope is empty, then the plugin works on allMusWithNotes 

	private DurationModel[] durationModelPattern;
	
	
	public DurationPattern(DurationModel[] aDurationModelPattern, MuTag aTagToActUpon)
	{
		super(
				Parameter.DURATION,
				new Parameter[] 
						{
								Parameter.TEMPO,
								Parameter.STRUCTURE_TONE_SYNCOPATION,
								Parameter.STRUCTURE_TONE_GENERATOR,
								Parameter.EMBELLISHMENT_GENERATOR
						}
				);
		durationModelPattern = aDurationModelPattern;
		tagToActUpon = aTagToActUpon;
	}
	
	
//	public DurationPattern(DurationType[] aDurationPattern, MuTag aTagToActUpon)
//	{
//		super(
//				Parameter.DURATION,
//				new Parameter[] 
//						{
//								Parameter.TEMPO,
//								Parameter.STRUCTURE_TONE_SYNCOPATION,
//								Parameter.STRUCTURE_TONE_GENERATOR,
//								Parameter.EMBELLISHMENT_GENERATOR
//						}
//				);
//		durationPattern = aDurationPattern;
//		tagToActUpon = aTagToActUpon;
//	}
	
	
	
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
		durationPatternRepo = DurationPatternRepo.builder()
				.durationModelPattern(durationModelPattern)
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.tagToActUpon(tagToActUpon)
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
		List<Mu> muList;
		if (tagToActUpon == null)
		{
			muList = pack.getMu().getMusWithNotes();
		}
		else
		{
			muList = pack.getMu().getMuWithTag(tagToActUpon);
		}
		
		Collections.sort(muList, Mu.globalPositionInQuartersComparator);
		dealWithAllMuDurationExceptLast(staccatoDurationInQuarters, muList);
		dealWithDurationOfLastMuInList(staccatoDurationInQuarters, muList);
	}



	private void dealWithAllMuDurationExceptLast(double staccatoDurationInQuarters, List<Mu> muList) {
		DurationModel model;
		double durationInQuarters = 0.0;
		for (int i = 0; i < muList.size() - 1; i++)
		{
			model = durationModelPattern[i % durationModelPattern.length];
			
			model.setDuration(muList.get(i));
		}
	}



	private void dealWithDurationOfLastMuInList(double staccatoDurationInQuarters, List<Mu> muList) {
		if (muList.size() > 0)
		{
			Mu lastMu = muList.get(muList.size() - 1);
			endNoteDurationModel.setDuration(lastMu);
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



	public DurationPattern setEndNoteDurationModel(DurationModel aDurationModel) {
		endNoteDurationModel = aDurationModel;
		return this;
	}
}
