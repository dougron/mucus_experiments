package main.java.com.dougron.mucus_experiments.completed;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.musicxml_maker.MuXMLMaker;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import test.java.com.dougron.mucus.TestingStuff;


/*
 * after getting to Mu010_..... this test fails 
 */

public class Mu002_QuickNastyPopSong_v3 {
	
//	TimeSignature fourFour;
//	TimeSignature sevenEight;
//	private TimeSignature fiveEight;
	Mu song;
	
	int verse1Length = 16;
	int chorusLength = 16;
	
	String xmlpath = "D:/Documents/miscForBackup/QuickNastyOutput/";
	
	
	public Mu002_QuickNastyPopSong_v3() {
//		makeTimeSignatures();
		
		makeSongAndSections(false);		
		
//		testFillListOfTimeSignatureOrigins(false);
//		
//		testMakeGlobalTimeSignatureMap(false);
//		
//		testMakeOneOfThePhrasesATSMOrigin(false);
//		
////		testMakeGlobalTimeSignatureMapOnALowerLevelMu();	// it fails. Only important for a Mu that represents a structure like a song or a movement
//		
////		makePhrases(false);
//		
////		for (Mu mu: song.getAllMus()) {
////			System.out.println(mu.getName() + "=" + mu.getGlobalBarPositionInQuarters() + " " + mu.getGlobalPositionInQuarters());
////		}
//		
////		LiveClip lc = MuUtils.makeLiveClip(song);
////		System.out.println(lc.toString());
//		
////		makeXML(lc, true, new int[] {3, 2, 1, 0});
//		
////		System.out.println(song.getGlobalBarPosition());
//		
////		makeNewXML(song);
		MuXMLMaker.makeXML(song, TestingStuff.getQuickNastyXMLPathString("QuickNastyPopSong_v3"));
		
		System.out.println("QuickNastyPopSong_v3 done.");
	}

//	private void makeNewXML(Mu mu) 
//	{
//		String path = xmlpath + "QuickNastyPopSong_v3_" + RenderName.dateAndTime() + ".musicxml";
//		MuXMLMaker.makeXML(mu, path);
//		
//	}

	
	
//	private LiveClip makeLiveClip() 
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}

	
	
//	private void makeXML(LiveClip lc, boolean b, int[] levelsToAnnotate) 
//
//		MusicXMLMaker mx = new MusicXMLMaker(MXM.KEY_OF_C);
//		for (TimeSignatureZone tsz: song.getTimeSignatureList().getTimeSignatureZones()) {
//			mx.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(tsz.getTs().getNumerator(), tsz.getTs().getDenominator(), tsz.getBarCount()));
//		}
//		mx.keyMap.addNewKeyZone(new XMLKeyZone(MXM.KEY_OF_C, song.getEndiestEnd()));
//		
//		mx.addPart("quiknasty3", lc);
//		
//		// add section names
//		for (int i: levelsToAnnotate) {
//			ArrayList<Mu> list = song.getMusOfLevel(i);
//			for (Mu mu: list) {
//				String partName = "quiknasty3";
//				mx.addTextDirection(partName, mu.getName(), mu.getGlobalBarPositionInQuarters() + mu.getPosition().getOffset(), MXM.PLACEMENT_ABOVE);
//			}
//		}
//		
//		mx.makeXML(xmlpath + "QuickNastyPopSong_v3-" + RenderName.dateAndTime() + ".musicxml");
//		
//		
//	}

	
//	private void makePhrases(boolean b) 
//	{
//		makeVersePhrases(b);
//		makeChorusPhrases(b);
//		
//		if (b) System.out.println("---song.toString()----------------------------------\n" + song.toString());
//	}

//	private void makeChorusPhrases(boolean b) {
//		//copy and pasted from createVersePhrases to see if everything works if I abuse things a bit
//		int[] intervalModel1 = new int[] {1, -1};
//		int[] intervalModel2 = new int[] {1, 0};
//		int[] intervalModel3 = new int[] {2, -1};
//		
//		//the following should all be the same length to avois an error
//		int[][] modelArr = new int[][] {intervalModel1, intervalModel2, intervalModel1, intervalModel3};
//		int[] direction = new int[] {1, -1, 1, -1};
//		double[] fill = new double[] {0.76, 0.55, 0.76, 0.55};
//		int[] scale = new int[] {0, 2, 4, 5, 7, 9, 11};
//		int[] startNotes = new int[] {60, 56, 65, 70};
//		
//		if (b) System.out.println("verse2 generate phrases");
//		
//		int index = 0;
//		for (Mu mu: song.getMus(1).getMus()) {		// phrases in chorus
//			if (b) System.out.println(mu.getName());
//			Mu003_MakePhrases.getTactusMelody(
//					mu, 
//					modelArr[index % modelArr.length], 
//					direction[index % direction.length], 
//					fill[index % fill.length], 
//					scale, 
//					startNotes[index % startNotes.length]);
//			index++;
//		}
//		
//	}

//	private void makeVersePhrases(boolean b) {
//		int[] intervalModel1 = new int[] {1};
//		int[] intervalModel2 = new int[] {1, 0};
//		int[] intervalModel3 = new int[] {2, -3};
//		
//		//the following should all be the same length to avois an error
//		int[][] modelArr = new int[][] {intervalModel1, intervalModel2, intervalModel1, intervalModel3};
//		int[] direction = new int[] {1, -1, 1, -1};
//		double[] fill = new double[] {0.76, 0.55, 0.76, 0.55};
//		int[] scale = new int[] {0, 2, 4, 5, 7, 9, 11};
//		int[] startNotes = new int[] {60, 56, 65, 70};
//		
//		if (b) System.out.println("verse1 generate phrases");
//		
//		int index = 0;
//		for (Mu mu: song.getMus(0).getMus()) {		// phrases in verse1
//			if (b) System.out.println(mu.getName());
//			Mu003_MakePhrases.getTactusMelody(
//					mu, 
//					modelArr[index % modelArr.length], 
//					direction[index % direction.length], 
//					fill[index % fill.length], 
//					scale, 
//					startNotes[index % startNotes.length]);
//			index++;
//		}
//		
//	}

	private void makeSongAndSections(boolean b) {
		song = new Mu("QuickNastyPopSong_v3");
//		song.setPosition(new BarPositionObject(0, 0.0, song));
		Mu verse1 = makeVerse1();
		song.addMu(verse1, 0);
		Mu chorus = makeChorus();
		song.addMuToEndOfSibling(chorus, 0, verse1);
//		printInitialData();
//		if (b) System.out.println(song.toString());
//		if (b) System.out.println("-------------------------");
//		if (b) System.out.println(song.reportGlobalPositions());
//		if (b) System.out.println("get end=" + song.getEndiestEnd());
		
	}

//	private void testMakeGlobalTimeSignatureMapOnALowerLevelMu() {
//		// test what happens when makeGlobalTimeSignature() gets called on a lower level Mu
//		song.getMus(1).makeGlobalTimeSignatureMap();
//		System.out.println("test of misplaced makeGlobalTimeSignature() call........");
//		System.out.println(song.toString());
//		
//	}

//	private void testMakeOneOfThePhrasesATSMOrigin(boolean b) {
//		// make one of the phrases a tsm origin
//		Mu phraseMu = song.getMus(1).getMus(2);
//		if (b) System.out.println(phraseMu.toString());
//		TSMGenerator tsgi = new TSMGenerator(new TimeSignature[] {TimeSignature.FIVE_EIGHT_32}, 4);
//		phraseMu.setTsMap(tsgi.getTimeSignatureMap(4));
//		song.makeGlobalTimeSignatureMap();
//		if (b) System.out.println(song.getTsMap().toString());
//		
//	}

//	private void testMakeGlobalTimeSignatureMap(boolean b) {
//		// test make global time signature map;
//		song.makeGlobalTimeSignatureMap();
//		if (b) System.out.println(song.getTsMap().toString());
//		
//	}

//	private void testFillListOfTimeSignatureOrigins(boolean b) {
//		// testing fillListOfTimeSignatureOrigins()
//		if (b) System.out.println("ListOfTimeSignatureOrigins--------");
//		ArrayList<Mu> tsMus = new ArrayList<Mu>();
//		song.fillListOfTimeSignatureOrigins(tsMus);
//		if (b) {
//			for (Mu mu : tsMus) {
//				System.out.println(mu.getName());
//				System.out.println(mu.getTsMap().toString());
//			} 
//		}
//		
//	}

	private Mu makeChorus() {
		Mu chorus = new Mu("chorus");
		chorus.addMuAnnotation(new MuAnnotation(chorus.getName()));
		
		chorus.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(new TimeSignature[] 
				{
					TimeSignature.FOUR_FOUR,
					TimeSignature.FOUR_FOUR,
					TimeSignature.FOUR_FOUR,
					TimeSignature.SEVEN_EIGHT_322
				}));
		
		// time signature
//		TSMGenerator tsg = new TSMGenerator();
//		tsg.addGenItem(new TSMGenerator(new TimeSignature[] 
//				{
//					TimeSignature.FOUR_FOUR,
//					TimeSignature.FOUR_FOUR,
//					TimeSignature.FOUR_FOUR,
//					TimeSignature.SEVEN_EIGHT_322
//				}, 1));
//		TSMapInterface tsm = tsg.getTimeSignatureMap(chorusLength);
//		chorus.setTsMap(tsm);
		
		// length in bars
		chorus.setLengthInBars(16);
		
		// phrases
//		for (Mu mu: Mu003_MakePhrases.getPhrases(new int[] {2, 2, 4, 2, 2, 4})) {
//			chorus.addMus(mu);
//		}		
		return chorus;
	}

	private Mu makeVerse1() {		// this is actually a MuGen. might be a separate class later
		Mu verse1 = new Mu("verse1");
		verse1.addMuAnnotation(new MuAnnotation(verse1.getName()));
		
		verse1.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator
				(
						new TimeSignature[] 
								{
										TimeSignature.FOUR_FOUR,
										TimeSignature.SEVEN_EIGHT_322,
										TimeSignature.SEVEN_EIGHT_322,
										TimeSignature.FOUR_FOUR,
								}	
						));
		
		// time signature
//		TSMGenerator tsg = new TSMGenerator();
//		tsg.addGenItem(new TSMGenerator(new TimeSignature[] 
//				{
//					TimeSignature.FOUR_FOUR,
//					TimeSignature.SEVEN_EIGHT_322,
//					TimeSignature.SEVEN_EIGHT_322
//				}, 1));
//		tsg.addGenItem(new TSMGenerator(new TimeSignature[] 
//				{
//					TimeSignature.FOUR_FOUR,
//					TimeSignature.SEVEN_EIGHT_322
//				}, 1));
//		TSMapInterface tsm = tsg.getTimeSignatureMap(verse1Length);
//		verse1.setTsMap(tsm);
		
		// length in bars
		verse1.setLengthInBars(16);
		
		// phrases
//		for (Mu mu: Mu003_MakePhrases.getPhrases(new int[] {4, 4, 4, 4})) {
//			verse1.addMus(mu);
//		}
		
		return verse1;
	}

//	private void printInitialData() {
//		System.out.println(TimeSignature.FOUR_FOUR.toString());
//		System.out.println(TimeSignature.SEVEN_EIGHT_322.toString());
//		System.out.println(song.toString());
//	}

	// removed 23 July 2020, going to use the presets from TimeSignature
//	private void makeTimeSignatures() {
//		fourFour = new TimeSignature(4, 4, new SubDivItem(4, 4, 0, 
//				new SubDivItem[] {
//						new SubDivItem(2, 4, 1),
//						new SubDivItem(2, 4, 2)
//				}));
//		sevenEight = new TimeSignature(7, 8, new SubDivItem(7, 8, 0, 
//				new SubDivItem[] {
//						new SubDivItem(3, 8, 1),
//						new SubDivItem(2, 8, 2),
//						new SubDivItem(2, 8, 2)
//				}));
//		fiveEight = new TimeSignature(5, 8, new SubDivItem(7, 8, 0, 
//				new SubDivItem[] {
//						new SubDivItem(3, 8, 1),
//						new SubDivItem(2, 8, 2)
//				}));
//	}

	public static void main(String[] args) {
		new Mu002_QuickNastyPopSong_v3();
		
		
	}
}
