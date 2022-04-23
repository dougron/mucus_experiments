package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagBundle;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTagNamedParameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;

public class VectorChordTonesFixed   extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(VectorChordTonesFixed.class);

	
	private static final int DEFAULT_STRUCTURE_TONE_VELOCITY = 80;
	

	private PhraseLengthRepo phraseLengthRepo;
	private StartNoteRepo startNoteRepo;
	private TessituraRepo tessituraRepo;
	private PhraseBoundRepo startPercentRepo;
	private PhraseBoundRepo endPercentRepo;

	private VectorChordTonesRepo vectorChordTonesRepo;


	private int[] vectorArray;
	
//	private TessituraSolver tessituraSolver = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
	
	
	/*
	 * items in vectorArray are jumps to next chord tone. e.g. 1 = next chord tone up, -2 two chord tones down
	 * 	special case 0 = closest, either up or down.....
	 */
	public VectorChordTonesFixed(int[] aVectorArray) 
	{
		super(
				Parameter.STRUCTURE_TONE_VECTOR,	// possible place for a general STRUCTURE_TONE_GENERATOR Parameter to cover a variety of options
				new Parameter[] {
						Parameter.TESSITURA_START_NOTE,
						Parameter.START_NOTE,
						Parameter.PHRASE_START_PERCENT,
						Parameter.PHRASE_END_PERCENT,
//						Parameter.STRUCTURE_TONE_MULTIPLIER,
						Parameter.STRUCTURE_TONE_SPACING,
						Parameter.CHORD_LIST_GENERATOR
					}
				);
		vectorArray = aVectorArray;
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
		if (noFieldReposAreNull() && vectorChordTonesRepo != null) 
		{
			pack = doMuUpdate(pack);
		} 
		else 
		{
			logger.info("mu not updated as all required repos were not present.");
		}
		return pack;
	}


	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		if (noFieldReposAreNull()) {
			double rndValue = pack.getRnd().nextDouble();
//			FourPointContour selectedOption = options[(int) (options.length * rndValue)];
			vectorChordTonesRepo = VectorChordTonesRepo.builder()
					.selectedVectorArray(vectorArray)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.STRUCTURE_TONE_VECTOR, vectorChordTonesRepo);
			logger.info("Created vectorChordTonesRepo and saved to pack.repo");
		} else {
			logger.info("STRUCTURE_TONE_VECTOR item not created because one or more field repos are null.");
		}
		return pack;
	}
	
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		vectorChordTonesRepo = (VectorChordTonesRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_VECTOR);
	}

	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		assignFieldReposFromPackRepo(pack);
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) 
//	{
//		logger.info(getInfoLevelPackReceiptMessage(pack));
//	
//		pack = super.process(pack);
//		logger.debug("After processing the required plugins for VectorChordTones chord progression was: " + pack.getMu().getChordListToString());
//		assignFieldReposFromPackRepo(pack);
//		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_VECTOR) 
//				&& pack.getRepo().get(Parameter.STRUCTURE_TONE_VECTOR).getClassName().equals(getClass().getName())) 
//		{			
//			vectorChordTonesRepo = (VectorChordTonesRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_VECTOR);
//		} 
//		else 
//		{
//			if (noFieldReposAreNull()) {
//				double rndValue = pack.getRnd().nextDouble();
////				FourPointContour selectedOption = options[(int) (options.length * rndValue)];
//				vectorChordTonesRepo = VectorChordTonesRepo.builder()
//						.selectedVectorArray(vectorArray)
//						.className(getClass().getName())
//						.build();
//				pack.getRepo().put(Parameter.STRUCTURE_TONE_VECTOR, vectorChordTonesRepo);
//				logger.info("Created vectorChordTonesRepo and saved to pack.repo");
//			} else {
//				logger.info("STRUCTURE_TONE_VECTOR item not created because one or more field repos are null.");
//			}
//		}
//		if (noFieldReposAreNull() && vectorChordTonesRepo != null) 
//		{
//			pack = updateMu(pack);
//		} 
//		else 
//		{
//			logger.info("mu not updated as all required repos were not present.");
//		}
//		logger.debug(this.getClass().getSimpleName() + ".process() exited");
//		return pack;
//	}


	private boolean noFieldReposAreNull() {
		return  phraseLengthRepo != null
				&& startNoteRepo != null
				&& tessituraRepo != null
				&& startPercentRepo != null
				&& endPercentRepo != null;
//				&& contourMultiplierRepo != null;
	}
	
	
	
	private void assignFieldReposFromPackRepo(PooplinePackage pack) {
//		if (pack.getRepo().containsKey(Parameter.STRUCTURE_TONE_MULTIPLIER)) {
//			contourMultiplierRepo = (ContourMultiplierRepo)pack.getRepo().get(Parameter.STRUCTURE_TONE_MULTIPLIER);
//		}
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


	private PooplinePackage doMuUpdate(PooplinePackage pack) 
	{
//		int lengthInBars = phraseLengthRepo.getSelectedValue();
		int note = startNoteRepo.getSelectedValue();
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		logger.info("start note=" + note
				+ " structure tone count=" + structureTones.size()
				+ " structure tone positions=" + getStructureTonePositionsInFloatBarsToString(structureTones)
				);
		
		int vectorIndex = 0;
		Chord chord;
		for (Mu mu: structureTones) {
			List<MuTagBundle> bundleList = mu.getMuTagBundleContaining(MuTag.IS_SYNCOPATION);
			if (bundleList.size() == 0) {
				chord = mu.getPrevailingChord();
			} else {
				double positionInQuarters = (double)bundleList.get(0).getNamedParameter(MuTagNamedParameter.SYNCOPATED_BEAT_GLOBAL_POSITION);
				chord = mu.getChordAtGlobalPosition(mu.getGlobalPositionInBarsAndBeats(positionInQuarters));
			}
			
			int vector = vectorArray[vectorIndex];
			note = chord.getClosestChordTone(note, vector);
			logger.info("Mu at " + mu.getGlobalPositionInFloatBars() 
				+ " chord=" + chord.name()
				+ " vector=" + vector
				+ " selected note=" + note);
			
			mu.addMuNote(new MuNote(note, DEFAULT_STRUCTURE_TONE_VELOCITY));
			vectorIndex++;
			if (vectorIndex >= vectorArray.length) vectorIndex = 0;
		}
		return pack;
	}


	private String getStructureTonePositionsInFloatBarsToString(List<Mu> muList)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Mu mu: muList)
		{
			sb.append(mu.getGlobalPositionInFloatBars() + ", ");
		}
		if (sb.length() > 2) sb.setLength(sb.length() - 2);
		sb.append("]");
		return sb.toString();
	}
}
