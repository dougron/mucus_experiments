package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGenerator;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.corpus_capture.CorpusItem;
import main.java.da_utils.corpus_capture.CorpusManager;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;


public class Mu024_ChordToneAndEmbellishmentAnalysis
{
	
	String melodyPath = "D:/Documents/repos/CorpusMelodyFiles/";
	String chordsPath = "D:/Documents/repos/ChordProgressionTestFiles/";
	
	String fileName = "Mu024_";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 

	
	
	public Mu024_ChordToneAndEmbellishmentAnalysis()
	{
//		makeChordsXMLOutput(-2, "Anthropology");

		IntAndString[] songNames = new IntAndString[] 
				{					
//						new IntAndString(-2, "AnthropologyTriplet"),
//						new IntAndString(-2, "AnthropologyEnclosureTest"),
//						new IntAndString(-2, "ChromaticApproachTest"),
//						new IntAndString(-3, "TripletTest"),
//						new IntAndString(0, "TripletRizingPTs"),
//						new IntAndString(0, "BigTripletTest"),
//						new IntAndString(-3, "RizingPTTest"),
//						new IntAndString(-3, "Syncopation1Test"),
//						new IntAndString(-3, "EscapeToneTest"),
//						new IntAndString(-3, "AccentedEscapeToneTest"),
//						new IntAndString(-3, "AnticipationTest"),
						new IntAndString(-2, "Anthropology"),
//						new IntAndString(-3, "BlueBossa"),
//						new IntAndString(0, "BlackOrpheus"),
//						new IntAndString(-2, "Stella"),
//						new IntAndString(-1, "Confirmation"),
				};
		for (IntAndString songName: songNames)
		{
			makeAnalysisOutput(songName);
		}
	}
	
	
//	private void makeChordsXMLOutput(int aXMLKey, String songName)
//	{
//		melodyPath += songName + ".liveclip";
//		LiveClip lcMelody = getLiveClipFromFile(melodyPath);
////		System.out.println(lcMelody.toString());
//		LiveClip lcChords = getChordsClip(lcMelody);		
////		System.out.println(lcChords.toString());
//		
//		makeOldXMLOutput(songName, lcChords, lcChords, aXMLKey);
//		
//	}


	private void makeAnalysisOutput(IntAndString xmlKeyAndSongName)
	{
		CorpusItem corpusItem = CorpusManager.getCorpusItem(xmlKeyAndSongName.str);
		
		LiveClip lcMelody = corpusItem.getLiveClip("melody");
//		LiveClip lcChords = corpusItem.getLiveClip("chords");
		
//		System.out.println(lcMelody.toString());
		
		Mu mu = makeMuMelody(xmlKeyAndSongName.str, lcMelody);
//		Mu mu = makeMuAnalysis(xmlKeyAndSongName.str, lcMelody, lcChords);
		mu.setXMLKey(xmlKeyAndSongName.i);
		
		ChordToneAndEmbellishmentTagger.addTags(mu);
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu);
		
		String x = ContinuousIntegrator.outputMuToXMLandLive(mu, fileName + xmlKeyAndSongName.str, trackIndex, clipIndex, injector);
		System.out.println(x);
	}


	
//	private void makeOldXMLOutput(String songName, LiveClip lcMelody, LiveClip lcChords, int aXMLKey)
//	{
//		ProgressionAnalyzer pa = new ProgressionAnalyzer(new ChordChunkList("4n", lcChords));		
//		String xmlPath = TestingStuff.getQuickNastyXMLPathString(fileName + songName + "_oldXML");		
//		exportToXML(lcMelody, pa, xmlPath, aXMLKey);		// old xml maker
//	}

	
	
	private Mu makeMuMelody(String songName, LiveClip lcMelody)
	{
		ArrayList<LiveMidiNote> tripletList = new ArrayList<LiveMidiNote>();
		boolean hasTriplet = false;
		Mu mu = new Mu(songName);
		mu.setTimeSignatureGenerator(getTimeSignatureGenerator(lcMelody));
		mu.setLengthInBars(mu.getGlobalPositionInBarsAndBeats(lcMelody.loopEnd).getBarPosition() - mu.getGlobalPositionInBarsAndBeats(lcMelody.loopStart).getBarPosition());
		for (LiveMidiNote lmn: lcMelody.noteList)
		{
			if (hasTripletPosition(lmn) || hasTripletLength(lmn))
			{
				tripletList.add(lmn);
				hasTriplet = true;
			}
			else
			{
				if (hasTriplet)
				{
					Mu tripletHolder = new Mu("triplet-holder");
					tripletHolder.setIsTupletPrintContainer(true);
					double position = tripletList.get(0).position;
					double endPosition = Mu.round(tripletList.get(tripletList.size() - 1).position + tripletList.get(tripletList.size() - 1).length, 10);
					double length = Mu.round(endPosition - position, 10);
					tripletHolder.setLengthInQuarters(length);
					tripletHolder.setTupletNumerator(3);
					tripletHolder.setTupletDenominator(2);					
					for (LiveMidiNote tripLmn: tripletList)
					{
						Mu tripMu = new Mu("triplet");
						tripMu.setLengthInQuarters(tripLmn.length);
						tripMu.addMuNote(new MuNote(tripLmn.note, tripLmn.velocity));
						double pos = tripLmn.position - position;
						tripletHolder.addMu(tripMu, pos);
					}
					mu.addMu(tripletHolder, position);
					hasTriplet = false;
					tripletList.clear();
				}
				Mu noteMu = new Mu("note");
				noteMu.addMuNote(new MuNote(lmn.note, lmn.velocity));
				noteMu.setLengthInQuarters(lmn.length);
				mu.addMu(noteMu, lmn.position - lcMelody.loopStart);
			}
			
		}
		if (hasTriplet)
		{
			Mu tripletHolder = new Mu("triplet-holder");
			tripletHolder.setIsTupletPrintContainer(true);
			double position = tripletList.get(0).position;
			double endPosition = Mu.round(tripletList.get(tripletList.size() - 1).position + tripletList.get(tripletList.size() - 1).length);
			double length = Mu.round(endPosition - position);
			tripletHolder.setLengthInQuarters(length);
			tripletHolder.setTupletNumerator(3);
			tripletHolder.setTupletDenominator(2);					
			for (LiveMidiNote tripLmn: tripletList)
			{
				Mu tripMu = new Mu("triplet");
				tripMu.setLengthInQuarters(tripLmn.length);
				tripMu.addMuNote(new MuNote(tripLmn.note, tripLmn.velocity));
				double pos = tripLmn.position - position;
				tripletHolder.addMu(tripMu, pos);
			}
			mu.addMu(tripletHolder, position);
			hasTriplet = false;
			tripletList.clear();
		}
		return mu;
	}


	
	private double[] tripletLengths = new double[] {0.333333333333, 0.666666666667};
	private double tripletTestEpsilon = 0.001;
	
	private boolean hasTripletPosition(LiveMidiNote lmn)
	{
		double modPosition = lmn.position % 1.0;
		for (double d: tripletLengths)
		{
			if (Math.abs(modPosition - d) < tripletTestEpsilon) return true;
		}
		return false;
	}
	
	
	
	private boolean hasTripletLength(LiveMidiNote lmn)
	{
		double modLength = lmn.length % 1.0;
		for (double d: tripletLengths)
		{
			if (Math.abs(modLength - d) < tripletTestEpsilon) return true;
		}
		return false;
	}
	


	private TimeSignatureListGenerator getTimeSignatureGenerator(LiveClip aLc)
	{
		return TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FOUR_FOUR);
	}



//	private void exportToXML(LiveClip lc, ProgressionAnalyzer pa, String aPath, int aXMLKey)
//	{
//
//		
//		XMLKey xmlKey = MXM.xmlKeyMap.get(aXMLKey);		// default key of C just for expedience
//		MusicXMLMaker mxm = new MusicXMLMaker(xmlKey);
//		int barCount = (int)((lc.loopEnd - lc.loopStart) / lc.signatureDenominator);
//		mxm.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(lc.signatureNumerator, lc.signatureDenominator, barCount)); 
//		mxm.keyMap.addNewKeyZone(new XMLKeyZone(xmlKey, barCount));
//		mxm.addPart(pa.name(), lc);
//		addChordSymbols(mxm, pa, lc);
////		mxm.addPart("variation", outputClip);
//		mxm.makeXML(aPath);
//	}
	
	
	
//	private void addChordSymbols(MusicXMLMaker mxm, ProgressionAnalyzer pa, LiveClip lc) 
//	{
////		mxm.addTextDirection(cpa.name(), CSD.noteName(cpa.bestKeyAnalysis().keyIndex()), 0.0, MXM.PLACEMENT_ABOVE);
//		for (ChordInKeyObject ciko: pa.analysisChordInKeyObjectList())
//		{
//			double position = ciko.position() + lc.loopStart;
//			mxm.addTextDirection(pa.name(), ciko.toStringKeyChordAndFunction(), position, MXM.PLACEMENT_ABOVE);
//		}		
//	}



//	private LiveClip getChordsClip(LiveClip lcMelody)
//	{
//		LiveClip lcChords;
//		if (lcMelody.getChordsClipPath() == null)
//		{
//			String path = chordsPath + lcMelody.name + ".chords.liveclip";
//			return getLiveClipFromFile(path);
//		}
//		else
//		{
//			return getLiveClipFromFile(lcMelody.getChordsClipPath());
//		}
//	}
	
	
	
//	private LiveClip getLiveClipFromFile(String aPath)
//	{
//		LiveClip lc;
//		try 
//		{
//			FileReader fr = new FileReader(aPath);
//			BufferedReader br = new BufferedReader(fr);
//			lc = new LiveClip(br);
//			return lc;
//		} 
//		catch (Exception ex)
//		{
//			System.out.println(ex.toString());
//		}
//		return null;
//	}



	public static void main(String[] args)
	{
		new Mu024_ChordToneAndEmbellishmentAnalysis();
	}
	
}
