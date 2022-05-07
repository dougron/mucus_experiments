package main.java.com.dougron.mucus_experiments;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.FloatBarChordProgression;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_controller.MuController;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.render_name.RenderName;

public class Mu037_MuseScoreErrorThing
{
	public static void main(String[] args)
	{
		String fileName = "Mu037_MuseScoreErrorThing";
		MuucusLOMInjector injector = new MuucusLOMInjector(7800);
		Map<MuTag, Integer[]> partTrackAndClipIndexMap = ImmutableMap.<MuTag, Integer[]>builder()
				.put(MuTag.PART_MELODY, new Integer[] {0, 0})
				.put(MuTag.PART_1, new Integer[] {1, 0})
				.put(MuTag.PART_2, new Integer[] {2, 0})
//				.put(MuTag.PART_BASS, new Integer[] {3, 0})
				.build();
		
		Mu totalMu = new Mu("totalMu");
		totalMu.setLengthInBars(4);
		totalMu.setXMLKey(0);
		totalMu.setChordListGenerator(new FloatBarChordProgression(1.0, new Object[] {0.0, "Bm7b5", 0.5, "E7"}));
		totalMu.addTag(MuTag.PRINT_CHORDS);
		totalMu.addTag(MuTag.PART_1);
		
		Mu note1 = new Mu("note1");
		note1.addMuNote(new MuNote(56, 64));
		note1.setLengthInQuarters(1.0);
		totalMu.addMu(note1, 2.0);
		
		Mu note2 = new Mu("note2");
		note2.addMuNote(new MuNote(56, 64));
		note2.setLengthInQuarters(1.0);
		totalMu.addMu(note2, 5.5);
		
		Mu note5 = new Mu("note2");
		note5.addMuNote(new MuNote(57, 64));
		note5.setLengthInQuarters(0.5);
		totalMu.addMu(note5, 8.0);
		
		Mu note3 = new Mu("note3");
		note3.addMuNote(new MuNote(56, 64));
		note3.setLengthInQuarters(0.5);
		totalMu.addMu(note3, 9.5);
		
		Mu note4 = new Mu("note4");
		note4.addMuNote(new MuNote(52, 64));
		note4.setLengthInQuarters(0.5);
		totalMu.addMu(note4, 11.5);
		
		
		
		
		String x = ContinuousIntegrator.outputMultiPartMuToXMLandLiveWithoutTimeStamp(
				totalMu, 
				fileName + "_accidental_error_" + RenderName.dateAndTime(),
				partTrackAndClipIndexMap,
				new ArrayList<MuController>(),	// placeholder for the controller list
				injector
				);
		System.out.println(x);
	}
}
