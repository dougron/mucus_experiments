package main.java.com.dougron.mucus_experiments.completed;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.part_generators.bass_part_generator.BassPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.chord_part_generator.ChordPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.drum_part_generator.DrumPartGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_001;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mu_framework.mu_xml_utility.MuXMLUtility;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_output_manager.musicxml_maker.MuXMLMaker;
import main.java.da_utils.render_name.RenderName;

public class Mu035_MusicXMLMakerFailureTest
{
	Random rnd = new Random();
	static String dateTimeStamp = RenderName.dateAndTime();
	static int count = 0;
//	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
//			.put(MuTag.PART_MELODY, new Integer[] {0, 0})
//			.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
//			.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
//			.put(MuTag.PART_BASS, new Integer[] {3, 0})
//			.build();
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	private int phraseCount = 0;
	
	
	
	
	public Mu035_MusicXMLMakerFailureTest()
	{
		iterateThroughAllFiles();
//		testSingleFile("D:/Documents/miscForBackup/Mu033/old/20210415_131007822.muxml");	// file that fails
//		testSingleFile("D:/Documents/miscForBackup/Mu033/old/20210415_130545243.muxml");	// file that passes
//		getFailMuFromRandomNumberContainer();
		System.out.println("done");
    }



//	private void getFailMuFromRandomNumberContainer()
//	{
//		File file = new File("D:/Documents/miscForBackup/Mu033/old/20210415_131007822.rnd_container");
//		RMRandomNumberContainer rndContainer 
//		= RMRandomNumberContainer.loadFile(file);
//		System.out.println(rndContainer.toString());
//		Mu mu = Mu033_getMu(rndContainer, "x").getMu();
//		MuXMLUtility.saveMuToXMLFile("D:/Documents/miscForBackup/Mu035/Mu035_musicxml_fail_test.muxml", mu);
//	}



//	private void testSingleFile(String muXMLPath)
//	{
//		Mu mu = MuXMLUtility.loadMuFromXMLFile(muXMLPath);
////		String mupath = "D:/Documents/miscForBackup/Mu035/" + dateTimeStamp + "_" + getZeroPaddedNumber(count, 3);
//		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
//				mu, 
//				"Mu035_MusicXMLMakerFailureTest_" + dateTimeStamp,
//				partTrackAndClipIndexMap,
//				injector
//				);
////		MuXMLMaker.makeMultiPartXML(mu, mupath);
//	}



	private void iterateThroughAllFiles()
	{
		String dirPath = "D:/Documents/miscForBackup/Mu033/old/";
		Path dir = Paths.get(dirPath);
        try
		{
			Files.walk(dir).forEach(path -> showFile(path.toFile()));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
    public static void showFile(File file) {
        if (file.isDirectory()) {
            System.out.println("Directory: " + file.getAbsolutePath());
        } else {
            System.out.println("File: " + file.getAbsolutePath());
            String extension = getExtensionByStringHandling(file.getName()).get();
            System.out.println(extension);
            if (extension.equals("muxml"))
            {
            	System.out.println("######");
            	testMuXMLFile(file);
            }
        }
    
	}
    
    
    
    private static void testMuXMLFile(File file)
	{
		try
		{
			Mu mu = MuXMLUtility.loadMuFromXMLFile(file.getAbsolutePath());
			String mupath = "D:/Documents/miscForBackup/Mu035/" + dateTimeStamp + "_" + getZeroPaddedNumber(count, 3) + ".musicxml";
			count++;
			MuXMLMaker.makeMultiPartXML(mu, mupath);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}
    
    
    
    public static String getZeroPaddedNumber(int i, int zeroPaddingCount)
	{
		StringBuilder sb = new StringBuilder();
		String x = "" + i;
		for (int j = 0; j < zeroPaddingCount - x.length(); j++)
		{
			sb.append('0');
		}
		sb.append(x);
		return sb.toString();
	}



	public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
	
	
	
	MuContainer Mu033_getMu(RMRandomNumberContainer rndContainer, String agentName)
	{
		Mu parent = new Mu("Mu033_phrase_" + agentName + "_" + phraseCount );
		phraseCount ++;
		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);		
		RandomMelodyParameterObject po = RMG_001.getInstance().getParameterObject(rndContainer, rnd);
		parent.setXMLKey(po.getXmlKey());
		
		Mu mu1 = getMelodyForMu(parent, po);
		getChordsPartForMu(parent, po, mu1);		
		getBassPartForMu(parent, po, mu1);		
		getDrumPartForMu(parent);
		
		MuContainer muc = new MuContainer(parent, po);
		return muc;
	}



	private Mu getMelodyForMu(Mu parent, RandomMelodyParameterObject po)
	{
		Mu mu1 = RMG_001.getInstance().getMuPhrase(po);
		mu1.setName("melody");
		mu1.addTag(MuTag.PART_MELODY);
		mu1.addTag(MuTag.PRINT_CHORDS);
		mu1.setHasLeadingDoubleBar(true);
		mu1.setXMLKey(po.getXmlKey());
		ChordToneAndEmbellishmentTagger.makeTagsIntoXMLAnnotations(mu1);
		addNamesAsMuAnnotationsToStructureTones(mu1);
		parent.addMu(mu1, 0);
		parent.setStartTempo(mu1.getStartTempo());
		parent.setLengthInBars(mu1.getLengthInBars());
		return mu1;
	}



	private void addNamesAsMuAnnotationsToStructureTones(Mu mu1)
	{
		for (Mu mu: mu1.getAllMus())
		{
			if (mu.hasTag(MuTag.IS_STRUCTURE_TONE))
			{
				mu.addMuAnnotation(new MuAnnotation(mu.getName(), MuAnnotation.TextPlacement.PLACEMENT_BELOW));
			}
		}
	}



	private void getDrumPartForMu(Mu parent)
	{
		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
		parent.getMu("drums").setHasLeadingDoubleBar(true);
	}



	private void getBassPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
	{
		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
		mu3.setName("bass");
		mu3.addTag(MuTag.PART_BASS);
		mu3.setHasLeadingDoubleBar(true);
		mu3.setXMLKey(po.getXmlKey());
//		mu3.addTag(MuTag.BASS_CLEF);
	}



	private void getChordsPartForMu(Mu parent, RandomMelodyParameterObject po, Mu mu1)
	{
		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
		mu2.setName("chords");
		mu2.addTag(MuTag.PART_CHORDS);
		mu2.setHasLeadingDoubleBar(true);
		mu2.setXMLKey(po.getXmlKey());
	}
	


	
	
	
	public static void main(String[] args)
	{
		new Mu035_MusicXMLMakerFailureTest();
	}
	
}
