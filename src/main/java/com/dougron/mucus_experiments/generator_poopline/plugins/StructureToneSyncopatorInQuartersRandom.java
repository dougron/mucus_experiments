package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.BooleanRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneSyncopationRepo;

public class StructureToneSyncopatorInQuartersRandom  extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(ShouldIUseTheStructureToneSyncopator.class);

	private double[] syncopationInQuarters = new double[] {0.0, -0.5};
	private int[][] options = new int[][] {
			new int[] {0, 1},
			new int[] {0, 1, 0},
			new int[] {0, 1, 1},
			new int[] {0, 1, 0, 0},
			new int[] {0, 1, 1, 0},
			new int[] {0, 0, 1},
			new int[] {0, 0, 1, 0},
			new int[] {0, 0, 1, 1},
			new int[] {0, 0, 0, 1},
			new int[] {1, 0},
			new int[] {1, 0, 1},
			new int[] {1, 0, 0},
			new int[] {1, 0, 1, 1},
			new int[] {1, 0, 0, 1},
			new int[] {1, 1, 0},
			new int[] {1, 1, 0, 1},
			new int[] {1, 1, 0, 0},
			new int[] {1, 1, 1, 0},
					
	};

	BooleanRepo useStructureToneSyncopationRepo;
	StructureToneSyncopationRepo structureToneSyncopationRepo;
	
	public StructureToneSyncopatorInQuartersRandom() {
		super(
				Parameter.STRUCTURE_TONE_SYNCOPATION,
				new Parameter[] {Parameter.USE_STRUCTURE_TONE_SYNCOPATOR}
				);
	}
	
	
	public StructureToneSyncopatorInQuartersRandom(double[] syncopationInQuarters, int[][] repPatterns) {
		super(
				Parameter.STRUCTURE_TONE_SYNCOPATION,
				new Parameter[] {Parameter.USE_STRUCTURE_TONE_SYNCOPATOR}
				);
		this.syncopationInQuarters = syncopationInQuarters;
		options = repPatterns;
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
		if (useStructureToneSyncopationRepo.isSelectedOption()) 
		{
			pack = addSyncopationsToMu(pack);
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		structureToneSyncopationRepo = StructureToneSyncopationRepo.builder()
				.rndValue(rndValue)
				.selectedOption((options[(int)(options.length * rndValue)]))
				.options(options)
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SYNCOPATION, structureToneSyncopationRepo);
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		structureToneSyncopationRepo = (StructureToneSyncopationRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_SYNCOPATION);

	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		useStructureToneSyncopationRepo = (BooleanRepo)pack.getRepo().get(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR); 
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) {
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		pack = super.process(pack);
//		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_SYNCOPATION) 
//				&& pack.getRepo().get(Parameter.STRUCTURE_TONE_SYNCOPATION).getClassName().equals(getClass().getName())) {			
//			// don't change existing info from same plugin
//			
//		} else {
//			double rndValue = pack.getRnd().nextDouble();
//			structureToneSyncopationRepo = StructureToneSyncopationRepo.builder()
//					.rndValue(rndValue)
//					.selectedOption((options[(int)(options.length * rndValue)]))
//					.options(options)
//					.className(getClass().getName())
//					.build();
//		}
//		if (pack.getRepo().containsKey(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR)) {
//			useStructureToneSyncopationRepo = (BooleanRepo)pack.getRepo().get(Parameter.USE_STRUCTURE_TONE_SYNCOPATOR); 
//			if (useStructureToneSyncopationRepo.isSelectedOption()) {
//				
//				pack = addSyncopationsToMu(pack);
//			}
//			
//		} else {
//			logger.info("No USE_STRUCTURE_TONE_SYNCOPATOR info in pack.repo");
//		}
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}


	private PooplinePackage addSyncopationsToMu(PooplinePackage pack) {
		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		Collections.sort(muList, Mu.globalPositionInQuartersComparator);
		int[] indexArr = structureToneSyncopationRepo.getSelectedOption();
		int index = 0;
		for (Mu mu: muList) {
			double sync = syncopationInQuarters[indexArr[index]];
			if (sync != 0.0) {
				double globalPositionBeforeSyncopation = mu.getGlobalPositionInQuarters();
				MuTagBundle bundle = new MuTagBundle(MuTag.IS_SYNCOPATION);
				bundle.addNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION, globalPositionBeforeSyncopation);
				mu.movePosition(sync);
				mu.addMuTagBundle(bundle);
			}
			index++;
			if (index == indexArr.length) index = 0;
		}
		return pack;
	}
	

}
