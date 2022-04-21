package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourMultiplierRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.da_utils.four_point_contour.FourPointContour;

public class ContourChordTonesRandom   extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(ContourMultiplierRandom.class);

	// save these for the ContourChordTonesRandomParameters class
//	public enum ContourType {TWO_POINT, THREE_POINT, FOUR_POINT};
//	private ContourType[] contourTypeOptions = new ContourType[] {ContourType.TWO_POINT};
	
	private static final int DEFAULT_STRUCTURE_TONE_VELOCITY = 80;
	
	FourPointContour[] options = new FourPointContour[] 
				{
					new FourPointContour(FourPointContour.DOWN),
					new FourPointContour(FourPointContour.UP),
					new FourPointContour(FourPointContour.DOWNUP),
					new FourPointContour(FourPointContour.UPDOWN),
					new FourPointContour(FourPointContour.STRAIGHT),
					new FourPointContour(0.5, 1.0, -1.0),
					new FourPointContour(0.5, -1.0, 1.0),
					new FourPointContour(0.25, 1.0, 0.0),
					new FourPointContour(0.25, -1.0, 0.0),
					new FourPointContour(0.75, 1.0, 0.0),
					new FourPointContour(0.75, -1.0, 0.0),
				};

	private ContourMultiplierRepo contourMultiplierRepo;

	private PhraseLengthRepo phraseLengthRepo;
	private StartNoteRepo startNoteRepo;
	private TessituraRepo tessituraRepo;
	private PhraseBoundRepo startPercentRepo;
	private PhraseBoundRepo endPercentRepo;

	private ContourChordTonesRepo contourChordTonesRepo;
	
//	private TessituraSolver tessituraSolver = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
	
	public ContourChordTonesRandom() {
		super(
				new Parameter[] {Parameter.STRUCTURE_TONE_CONTOUR},
				new Parameter[] {
						Parameter.TESSITURA_START_NOTE,
						Parameter.START_NOTE,
						Parameter.PHRASE_START_PERCENT,
						Parameter.PHRASE_END_PERCENT,
						Parameter.STRUCTURE_TONE_MULTIPLIER,
						Parameter.STRUCTURE_TONE_SPACING,
						Parameter.CHORD_LIST_GENERATOR
					}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info(getInfoLevelPackReceiptMessage(pack));
		pack = super.process(pack);
		assignFieldReposFromPackRepo(pack);
		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_CONTOUR) 
				&& pack.getRepo().get(Parameter.STRUCTURE_TONE_CONTOUR).getClassName().equals(getClass().getName())) {			
			contourChordTonesRepo = (ContourChordTonesRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_CONTOUR);
		} else {
			if (noFieldReposAreNull()) {
				double rndValue = pack.getRnd().nextDouble();
				FourPointContour selectedOption = options[(int) (options.length * rndValue)];
				contourChordTonesRepo = ContourChordTonesRepo.builder().rndValue(rndValue)
						.selectedOption(selectedOption).options(options).className(getClass().getName()).build();
				pack.getRepo().put(Parameter.STRUCTURE_TONE_CONTOUR, contourChordTonesRepo);
				logger.info("Contour selected from pack.rnd.nextDouble and saved to repo");
			} else {
				logger.info("STRUCTURE_TONE_CONTOUR item not created because one or more field repos are null.");
			}
		}
		if (noFieldReposAreNull() && contourChordTonesRepo != null) {
			
			pack = updateMu(pack);
		} else {
			logger.info("mu not updated as all required repos were not present.");
		}
		logger.debug(this.getClass().getSimpleName() + ".process() exited");
		return pack;
	}


	private boolean noFieldReposAreNull() {
		return  phraseLengthRepo != null
				&& startNoteRepo != null
				&& tessituraRepo != null
				&& startPercentRepo != null
				&& endPercentRepo != null
				&& contourMultiplierRepo != null;
	}
	
	
	
	private void assignFieldReposFromPackRepo(PooplinePackage pack) {
		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_MULTIPLIER)) {
			contourMultiplierRepo = (ContourMultiplierRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_MULTIPLIER);
		}
		if (pack.getRepo().containsKey(Parameter.PHRASE_LENGTH)) {
			phraseLengthRepo = (PhraseLengthRepo)pack.getRepo().get(Parameter.PHRASE_LENGTH);
		}
		if (pack.getRepo().containsKey(Parameter.START_NOTE)) {
			startNoteRepo = (StartNoteRepo)pack.getRepo().get(Parameter.START_NOTE);
		}
		if (pack.getRepo().containsKey(Parameter.TESSITURA_START_NOTE)) {
			tessituraRepo = (TessituraRepo)pack.getRepo().get(Parameter.TESSITURA_START_NOTE);
		}
		if (pack.getRepo().containsKey(Parameter.PHRASE_START_PERCENT)) {
			startPercentRepo = (PhraseBoundRepo)pack.getRepo().get(Parameter.PHRASE_START_PERCENT);
		}
		if (pack.getRepo().containsKey(Parameter.PHRASE_END_PERCENT)) {
			endPercentRepo = (PhraseBoundRepo)pack.getRepo().get(Parameter.PHRASE_END_PERCENT);
		}
	}


	private PooplinePackage updateMu(PooplinePackage pack) {
		int lengthInBars = phraseLengthRepo.getSelectedValue();
		int startNote = startNoteRepo.getSelectedValue();
		double startPositionInFloatBars = lengthInBars * startPercentRepo.getSelectedValue();
		double endPositionInFloatBars = lengthInBars * endPercentRepo.getSelectedValue();
		int multiplier = contourMultiplierRepo.getMultiplier();
		FourPointContour contour = contourChordTonesRepo.getSelectedOption();
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		
		for (Mu mu: structureTones) {
			double pos = mu.getGlobalPositionInFloatBars();
			// note to self, percentagePosition should calculate within range 0.0 - 1.0
			double percentagePosition = (pos - startPositionInFloatBars) / (endPositionInFloatBars - startPositionInFloatBars);
			double contourPosition = contour.getValue(percentagePosition);
			int contourNote = startNote + (int)Math.round(contourPosition * multiplier);
			int note;
			List<MuTagBundle> bundleList = mu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
			if (bundleList.size() == 0) {
				note = mu.getPrevailingChord().getClosestChordTone(contourNote);
			} else {
				double positionInQuarters = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
				note = mu.getChordAtGlobalPosition(mu.getGlobalPositionInBarsAndBeats(positionInQuarters)).getClosestChordTone(contourNote);
			}
			mu.addMuNote(new MuNote(note, DEFAULT_STRUCTURE_TONE_VELOCITY));
		}
		return pack;
	}
}
