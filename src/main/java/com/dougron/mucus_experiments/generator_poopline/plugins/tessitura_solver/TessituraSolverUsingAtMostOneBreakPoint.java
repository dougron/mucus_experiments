package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tessitura_solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

/*
 * converted to a Plugin, so probably depreciated on the same day it was written
 * 
 * 
 * solve for tessitura transgressions by making only one octave register change in the melody
 * 
 * if the melody goes below range, score will be negative, and an upward octave will be used
 * if the melody goes above range, score will be positive, and a downward octave will be used
 * if the melody transgesses both ways, octave change will be based as above, with the logic that whichever is higher is the significant octave change to make
 * 
 * octave change is tested between each note, with the aim of getting the lowest absolute score. 
 * can favour the first, last, middle or random choice if there is more than one.
 * default is first
 * 
 * also, this code assumes the melody starts in the register, so change always happens after the break point
 */

public class TessituraSolverUsingAtMostOneBreakPoint implements TessituraSolver {
	
	
	public enum FavoursBreak {FIRST, LAST, MIDDLE, RANDOM};
	
	
	private static TessituraSolverUsingAtMostOneBreakPoint instance;
	private int[] shiftOptions = new int[] {12, -12};	// octave up or down
	private FavoursBreak breakAt = FavoursBreak.FIRST;
	
	private TessituraSolverUsingAtMostOneBreakPoint() {}
	
	

	public static TessituraSolver getInstance() {
		if (instance == null) {
			instance = new TessituraSolverUsingAtMostOneBreakPoint();
		}
		return instance;
	}

	
	@Override
	public List<Mu> solveForTessitura(List<Mu> muList, TessituraRepo repo) {
		List<Integer> pitchList = getListOfTopPitches(muList);
		int[] scores = getScoreAndAbsoluteScore(repo, pitchList);
		
		if (scores[0] == 0) {
			// do nothing
		} else {
			int octave = getOctaveAdjustment(scores);
			int bestSplitIndex = getBestSplitIndex(repo, pitchList, octave);
			
			muList = adjustMuNotesAccordingToOctaveAndBestSplitIndex(muList, octave, bestSplitIndex);
		}
		
		return muList;
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


	private int getBestSplitIndex(TessituraRepo repo, List<Integer> pitchList, int octave) {
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
		Collections.sort(iasList, IndexAndScore.scoreAndIndexComparator);
		int bestSplitIndex = iasList.get(0).index;
		return bestSplitIndex;
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

	
	
	class IndexAndScore{
		int index;
		int score;

		public IndexAndScore(int index, int score) {
			this.index = index;
			this.score = score;
		}
		
		// this favours lowest score and latest change
		public static Comparator<IndexAndScore> scoreAndIndexComparator = new Comparator<IndexAndScore>() {

			@Override
			public int compare(IndexAndScore o1, IndexAndScore o2) {
				if (o1.score < o2.score) return 1;
				if (o1.score > o2.score) return -1;
				if (o1.index < o2.index) return 1;
				if (o1.index > o2.index) return -1;
				return 0;
			}			
		};

		@Override
		public String toString() {
			return "IndexAndScore [index=" + index + ", score=" + score + "]";
		}
	}
	
	

}
