package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class StructureToneEvenlySpacedRandom extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(StructureToneEvenlySpacedRandom.class);

	private static final int STRENGTH_THRESHOLD = 2;

	private double[] options = new double[] {0.5, 1.0, 2.0};
	private static final double DEFAULT_LENGTH_IN_QUARTERS = 0.5;
	
	private Parameter parameter;
	
	StructureToneEvenlySpacedRepo evenlySpacedStructureToneRepo;
	PhraseLengthRepo phraseLengthRepo;
	TimeSignatureRepo timeSignatureRepo;	
	PhraseBoundRepo startPercentRepo;
	PhraseBoundRepo endPercentRepo;
	
	public StructureToneEvenlySpacedRandom() {
		super(
				Parameter.STRUCTURE_TONE_SPACING,
				new Parameter[] {
						Parameter.PHRASE_LENGTH,
						Parameter.TIME_SIGNATURE,
						Parameter.PHRASE_START_PERCENT,
						Parameter.PHRASE_END_PERCENT
				}
				);
	}
	
	
	public StructureToneEvenlySpacedRandom(double[] aOptions) {
		super(
				Parameter.STRUCTURE_TONE_SPACING,
				new Parameter[] {
						Parameter.PHRASE_LENGTH,
						Parameter.TIME_SIGNATURE,
						Parameter.PHRASE_START_PERCENT,
						Parameter.PHRASE_END_PERCENT
				}
				);
		options = aOptions;
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
		if (phraseLengthRepo != null && timeSignatureRepo != null && startPercentRepo != null && endPercentRepo != null) {
			List<Double> posList = getListOfStructureTonePositionsInFloatBars();
			BarsAndBeats finalPosition;
			for (Double position: posList) {
				BarsAndBeats globalPositionInBarsAndBeats = pack.getMu().getGlobalPositionInBarsAndBeatsFromGlobalPositionInFloatBars(position);
				if (position % 1.0 > 0.0) {
					double chosenPosition = getQuantisedQuartersPositionInBar(pack, globalPositionInBarsAndBeats);
					finalPosition = new BarsAndBeats(globalPositionInBarsAndBeats.getBarPosition(), chosenPosition);

				} else {
					finalPosition = globalPositionInBarsAndBeats;
					
				}
				Mu mu = new Mu("st");
				mu.setLengthInQuarters(DEFAULT_LENGTH_IN_QUARTERS);
				mu.addTag(MuTag.IS_STRUCTURE_TONE);
				pack.getMu().addMu(mu, finalPosition);
			}
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		evenlySpacedStructureToneRepo = makeNewRandomEvenlySpacedStructureToneRepo(pack);
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, evenlySpacedStructureToneRepo);
		return pack;
	}
	
	
	void getRepoFromPack(PooplinePackage pack)
	{
		evenlySpacedStructureToneRepo = (StructureToneEvenlySpacedRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_SPACING);
	}


	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		assignFieldReposFromPackRepo(pack);
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) {
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//		pack = super.process(pack);
//		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_SPACING) 
//				&& pack.getRepo().get(Parameter.STRUCTURE_TONE_SPACING).getClassName().equals(getClass().getName())) {			
//			evenlySpacedStructureToneRepo = (EvenlySpacedStructureToneRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_SPACING);
//		} else {
//			evenlySpacedStructureToneRepo = makeNewRandomEvenlySpacedStructureToneRepo(pack);
//			pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, evenlySpacedStructureToneRepo);
//		}
//		assignFieldReposFromPackRepo(pack);
//		
//		if (phraseLengthRepo != null && timeSignatureRepo != null && startPercentRepo != null && endPercentRepo != null) {
//			List<Double> posList = getListOfStructureTonePositionsInFloatBars();
//			BarsAndBeats finalPosition;
//			for (Double position: posList) {
//				BarsAndBeats globalPositionInBarsAndBeats = pack.getMu().getGlobalPositionInBarsAndBeatsFromGlobalPositionInFloatBars(position);
//				if (position % 1.0 > 0.0) {
//					double chosenPosition = getQuantisedQuartersPositionInBar(pack, globalPositionInBarsAndBeats);
//					finalPosition = new BarsAndBeats(globalPositionInBarsAndBeats.getBarPosition(), chosenPosition);
//
//				} else {
//					finalPosition = globalPositionInBarsAndBeats;
//					
//				}
//				Mu mu = new Mu("st");
//				mu.setLengthInQuarters(DEFAULT_LENGTH_IN_QUARTERS);
//				mu.addTag(MuTag.IS_STRUCTURE_TONE);
//				pack.getMu().addMu(mu, finalPosition);
//			}
//		}
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}


	private double getQuantisedQuartersPositionInBar(PooplinePackage pack, BarsAndBeats globalPositionInBarsAndBeats) {
		TimeSignature ts = pack.getMu().getTimeSignature(globalPositionInBarsAndBeats.getBarPosition());
		Map<Double, Integer> strengthMap = ts.getStrengthMap();
		double distance = ts.getLengthInQuarters() * 2; 	// arbitrarily large
		int strengthOfBestDistance = 8;	// lower is better, strength looked for is 2 or below
		double chosenPosition = 0.0;
		double positionInBar = globalPositionInBarsAndBeats.getOffsetInQuarters();
		for (Double key: strengthMap.keySet()) {
			double tempdist = Math.abs(key - positionInBar);
			if (strengthMap.get(key) <= STRENGTH_THRESHOLD ) {
				if (tempdist < distance) {
					distance = tempdist;
					strengthOfBestDistance = strengthMap.get(key);
					chosenPosition = key;
				} else if (distance == tempdist) {
					if (strengthMap.get(key) < strengthOfBestDistance) {
						strengthOfBestDistance = strengthMap.get(key);
						chosenPosition = key;
					} else if (strengthMap.get(key) < strengthOfBestDistance) {
						// favour the first one the algorithm found (based on hashing) 
						// or insert code here to add choices......
					}
				}
			}
		}
		return chosenPosition;
	}


	private List<Double> getListOfStructureTonePositionsInFloatBars() {
		double pos = 0.0;	// earliest possible structure tone is assumed here to be downbeat of the phrase. This assumption could change?
		List<Double> posList = new ArrayList<Double>();
		double startFloatBarPos = phraseLengthRepo.getSelectedValue() * startPercentRepo.getSelectedValue();
		double endFloatBarPos = phraseLengthRepo.getSelectedValue() * endPercentRepo.getSelectedValue();
		while (true) {
			if (pos >= endFloatBarPos) break;
			if (pos >= startFloatBarPos) {
				posList.add(pos);
			} 
			pos += evenlySpacedStructureToneRepo.getSelectedValueInFloatBars();
		}
		return posList;
	}


	private void assignFieldReposFromPackRepo(PooplinePackage pack) {
		if (pack.getRepo().containsKey(Parameter.PHRASE_LENGTH)) {
			phraseLengthRepo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		}
		if (pack.getRepo().containsKey(Parameter.TIME_SIGNATURE)) {
			timeSignatureRepo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
		}
		if (pack.getRepo().containsKey(Parameter.PHRASE_START_PERCENT)) {
			startPercentRepo = (PhraseBoundRepo)pack.getRepo().get(Parameter.PHRASE_START_PERCENT);
		}
		if (pack.getRepo().containsKey(Parameter.PHRASE_END_PERCENT)) {
			endPercentRepo = (PhraseBoundRepo)pack.getRepo().get(Parameter.PHRASE_END_PERCENT);
		}
	}


	private StructureToneEvenlySpacedRepo makeNewRandomEvenlySpacedStructureToneRepo(PooplinePackage pack) {
		double rndValue = pack.getRnd().nextDouble();
		return StructureToneEvenlySpacedRepo.builder()
				.rndValue(rndValue)
				.selectedValueInFloatBars((options[(int)(options.length * rndValue)]))
				.options(options)
				.className(getClass().getName())
				.build();
	}
}
