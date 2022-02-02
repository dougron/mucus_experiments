package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.part_generators.bass_part_generator.BassPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.chord_part_generator.ChordPartGenerator;
import main.java.com.dougron.mucus.algorithms.part_generators.drum_part_generator.DrumPartGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_001;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

public class Mu031_MultiPartOutputOfRandomMelody
{

	Random rnd = new Random();
	String fileName = "Mu031_MultiPartOutput";
	private Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
				.put(MuTag.PART_MELODY, new Integer[] {0, 0})
				.put(MuTag.PART_CHORDS, new Integer[] {1, 0})
				.put(MuTag.PART_DRUMS, new Integer[] {2, 0})
				.put(MuTag.PART_BASS, new Integer[] {3, 0})
				.build();
			
			
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	public Mu031_MultiPartOutputOfRandomMelody()
	{
		Mu parent = new Mu("parent");
		parent.addTag(MuTag.HAS_MULTIPART_CHILDREN);
		RandomMelodyGenerator rmg = RMG_001.getInstance();
		
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		RandomMelodyParameterObject po = rmg.getParameterObject(rndContainer, rnd);
		System.out.println(po.toString());
		Mu mu1 = rmg.getMuPhrase(po);
		mu1.setName("melody");
		mu1.addTag(MuTag.PART_MELODY);
		parent.addMu(mu1, 0);
		parent.setStartTempo(mu1.getStartTempo());
		

		Mu mu2 = ChordPartGenerator.addPadMuToParentMu(parent, mu1);
		mu2.setName("chords");
		mu2.addTag(MuTag.PART_CHORDS);
		
		Mu mu3 = BassPartGenerator.addBassMuToParentMu(parent, mu1);
		mu3.setName("bass");
		mu3.addTag(MuTag.PART_BASS);
		
		DrumPartGenerator.addTactusHiHatToDrumPart(parent);
		DrumPartGenerator.addKickAndSnareToDrumPart(parent);
		DrumPartGenerator.addSubTactusHiHatToDrumPart(parent);
		
		doOutput(parent);
	}
	
	
	
	private void doOutput(Mu aMu)
	{
		aMu.addTag(MuTag.PRINT_CHORDS);		
		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLive(
				aMu, 
				fileName,
				partTrackAndClipIndexMap,
				new ArrayList<MuController>(),	// placeholder for the controller list
				injector
				);
		System.out.println(x);
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu031_MultiPartOutputOfRandomMelody();
	}
}
