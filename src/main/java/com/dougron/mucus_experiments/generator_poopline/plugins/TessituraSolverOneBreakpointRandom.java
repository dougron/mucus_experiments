package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraSolverRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraSolverRepo.BreakPointChoice;


/*
 * creates TESSITURA_SOLVER item
 * 
 * requires aTessituraParameter to operate (e.g. TESSITURA_MELODY_RANGE)
 * 
 * acts on mus tagged with aMuTag
 * 
 * there may be a missing verification step of a plugin that has generated MuNotes in Mu. 
 * Currently this is tested by the size of the muList tagged with muTagToActUpon
 */
public class TessituraSolverOneBreakpointRandom extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(TessituraSolverOneBreakpointRandom.class);
	

	private TessituraSolverRepo tessituraSolverRepo;
	// will look for a plugin or repo item that generates/contains this Parameter
	private Parameter tessituraParameter;
	// will act on mus with this MuTag
	private MuTag muTagToActUpon;
		


	private TessituraRepo melodyRangeRepo;


	
	
	public TessituraSolverOneBreakpointRandom(
			Parameter aTessituraParameter, 
			MuTag aMuTagToActUpon) {
		super(
				new Parameter[] {Parameter.TESSITURA_SOLVER},
				new Parameter[] {}
				);
		setRequiredParameters(new Parameter[] {aTessituraParameter});
		tessituraParameter = aTessituraParameter;
		muTagToActUpon = aMuTagToActUpon;
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info(getInfoLevelPackReceiptMessage(pack));
		pack = super.process(pack);
		List<Mu> muList = pack.getMu().getMuWithTag(muTagToActUpon);
		assignFieldReposFromPackRepo(pack);
		
		if (pack.getRepo().containsKey(Parameter.TESSITURA_SOLVER) 
				&& pack.getRepo().get(Parameter.TESSITURA_SOLVER).getClassName().equals(getClass().getName())) {
			tessituraSolverRepo = (TessituraSolverRepo)pack.getRepo().get(Parameter.TESSITURA_SOLVER);
		} else {
			if (melodyRangeRepo != null) {
				if (muList.size() == 0) {
					logger.info("No mus tagged with " + muTagToActUpon.toString() + " were found.");
				} else {
					double rndValue = pack.getRnd().nextDouble();
					BreakPointChoice solverType 
					= TessituraSolverRepo.breakPointOptions[(int)(TessituraSolverRepo.breakPointOptions.length * rndValue)];
					double randomSolverRndValue = pack.getRnd().nextDouble();
					tessituraSolverRepo = TessituraSolverRepo.builder()
							.rndValue(rndValue)
							.breakPointChoice(solverType)
							.rndValueForRandomChoice(randomSolverRndValue)
							.tessituraParameter(tessituraParameter)
							.muTagToActUpon(muTagToActUpon)
							.className(getClass().getName())
							.build();
					pack.getRepo().put(Parameter.TESSITURA_SOLVER, tessituraSolverRepo);
				}
			} else {
				logger.info("melodyRangeRepo was not found. No Tessitura solving done.");
			}
		}
		if (muList.size() > 0 && tessituraSolverRepo != null && melodyRangeRepo != null) {
			adjustMuList(muList);
		}
		return pack;
	}
	
	
	private void adjustMuList(List<Mu> muList) {
		List<Integer> pitchList = getListOfTopPitches(muList);
		int[] scores = getScoreAndAbsoluteScore(melodyRangeRepo, pitchList);
		if (scores[0] == 0) {
			// do nothing
		} else {
			int octave = getOctaveAdjustment(scores);
			List<IndexAndScore> iasList = getIndexAndScoreList(melodyRangeRepo, pitchList, octave);
			int bestSplitIndex = getBestSplitIndex(iasList);
			muList = adjustMuNotesAccordingToOctaveAndBestSplitIndex(muList, octave, bestSplitIndex);
		}
	}
	
	
	private List<Mu> adjustMuNotesAccordingToOctaveAndBestSplitIndex(List<Mu> muList, int octave, int bestSplitIndex) {
		for (int i = 0; i < muList.size(); i++) {
			if (i >= bestSplitIndex) {
				Mu mu = muList.get(i);
				for (MuNote muNote: mu.getMuNotes()) {
					muNote.setNote(muNote.getPitch() + octave);
				}
			}
		}
		return muList;
	}


	private int getBestSplitIndex(List<IndexAndScore> iasList) {
		int bestSplitIndex = 0;
		switch (tessituraSolverRepo.getBreakPointChoice()) {
		case FIRST:
			Collections.sort(iasList, IndexAndScore.scoreAndFirstIndexComparator);
			bestSplitIndex = iasList.get(0).index;
			break;
		case LAST:
			Collections.sort(iasList, IndexAndScore.scoreAndLastIndexComparator);
			bestSplitIndex = iasList.get(0).index;
			break;
		case RANDOM:
			Collections.sort(iasList, IndexAndScore.scoreAndFirstIndexComparator);
			List<IndexAndScore> onlyTheBest = new ArrayList<IndexAndScore>();
			int bestScore = iasList.get(0).score;
			for (IndexAndScore ias: iasList) {
				if (ias.score > bestScore) break;
				onlyTheBest.add(ias);
			}
			int index = (int)(onlyTheBest.size() * tessituraSolverRepo.getRndValueForRandomChoice());
			bestSplitIndex = iasList.get(index).index;
			break;
		}
		return bestSplitIndex;
	}
	
	
	private List<IndexAndScore> getIndexAndScoreList(TessituraRepo repo, List<Integer> pitchList, int octave) {
		List<IndexAndScore> iasList = new ArrayList<IndexAndScore>();
		List<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < pitchList.size(); i++) {	// change will happen before this index
			tempList.clear();
			for (int j = 0; j < pitchList.size(); j++) {
				if (j >= i) {
					tempList.add(pitchList.get(j) + octave);
				} else {
					tempList.add(pitchList.get(j));
				}
			}
			iasList.add(new IndexAndScore(i, getScoreAndAbsoluteScore(repo, tempList)[1]));
		}
		return iasList;
//		Collections.sort(iasList, IndexAndScore.scoreAndIndexComparator);
//		int bestSplitIndex = iasList.get(0).index;
//		return bestSplitIndex;
	}


	private int getOctaveAdjustment(int[] scores) {
		int octave = 12;
		if (scores[0] > 0) octave = -12;
		return octave;
	}

	
	private int[] getScoreAndAbsoluteScore(TessituraRepo repo, List<Integer> pitchList) {
		int currentScore = 0;
		int absoluteScore = 0;
		int low = repo.getLowValue();
		int high = repo.getHighValue();
		int score;
		for (Integer i: pitchList) {
			if (i < low) {
				score = i - low;	// score is negative if below register
			} else if (i > high) {
				score = i - high;
			} else {
				score = 0;
			}
			currentScore += score;
			absoluteScore += Math.abs(score);
		}
		return new int[] {currentScore, absoluteScore};
	}
	
	
	private List<Integer> getListOfTopPitches(List<Mu> muList) {
		List<Integer> pitchList = new ArrayList<Integer>();
		for (Mu mu: muList) {
			pitchList.add(mu.getTopPitch());
		}
		return pitchList;
	}


	private void assignFieldReposFromPackRepo(PooplinePackage pack) {

		if (pack.getRepo().containsKey(Parameter.TESSITURA_MELODY_RANGE)) {
			melodyRangeRepo = (TessituraRepo)pack.getRepo().get(Parameter.TESSITURA_MELODY_RANGE);
		}
	}
	
	
	class IndexAndScore{
		int index;
		int score;

		public IndexAndScore(int index, int score) {
			this.index = index;
			this.score = score;
		}
		
		// this favours lowest score and latest change
		public static Comparator<IndexAndScore> scoreAndLastIndexComparator = new Comparator<IndexAndScore>() {

			@Override
			public int compare(IndexAndScore o1, IndexAndScore o2) {
				if (o1.score < o2.score) return -1;
				if (o1.score > o2.score) return 1;
				if (o1.index < o2.index) return 1;
				if (o1.index > o2.index) return -1;
				return 0;
			}			
		};
		
		public static Comparator<IndexAndScore> scoreAndFirstIndexComparator = new Comparator<IndexAndScore>() {
			
			@Override
			public int compare(IndexAndScore o1, IndexAndScore o2) {
				if (o1.score < o2.score) return -1;
				if (o1.score > o2.score) return 1;
				if (o1.index < o2.index) return -1;
				if (o1.index > o2.index) return 1;
				return 0;
			}			
		};

		@Override
		public String toString() {
			return "IndexAndScore [index=" + index + ", score=" + score + "]";
		}
	}
	
	
}
