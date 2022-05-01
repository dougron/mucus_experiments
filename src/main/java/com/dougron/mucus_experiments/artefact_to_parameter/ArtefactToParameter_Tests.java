package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_utils.mu_liveclip_utils.MuLiveClipUtils;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ForceCreatePlugInsFromRepo;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;


class ArtefactToParameter_Tests
{

	@Test
	void instantiates_and_says_hello()
	{
		assertThat(ArtefactToParameter.sayHello()).isEqualTo("hello");
	}
	
	
//	@Test
//	void given_input_of_single_chord_and_chord_tone_then_pack_from_ArtefactToParameter_contains_required_repo_items() throws Exception
//	{
//		LiveClip[] clips = getMelodyAndChordClip();
//		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
//		PooplinePackage pack = ArtefactToParameter.getPackFromMu(mu);
//		assertThat(pack.getRepo().containsKey(Parameter.PHRASE_LENGTH)).isTrue();
//	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_length_of_mu_returned_from_Poopline_is_the_same() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(mu.getLengthInBars());
	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_mu_returned_from_Poopline_has_correct_timeSignature() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		assertThat(pack.getMu().getTimeSignature(0).getName()).isEqualTo("4/4");
	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_in_7_8_time_then_mu_returned_from_Poopline_has_correct_timeSignature() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip78();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		assertThat(pack.getMu().getTimeSignature(0).getName()).isEqualTo("7/8_322");
	}

	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_chord_of_mu_returned_from_Poopline_is_the_same() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		assertThat(pack.getMu().getChordAt(BarsAndBeats.at(0, 0.0)).getAssociatedChordInKeyObject().chordSymbol()).isEqualTo("C#Maj");
	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_chord_tone_of_mu_returned_from_Poopline_is_the_same_pitch() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(muList.size()).isEqualTo(1);
		List<MuNote> muNotes = muList.get(0).getMuNotes();
		assertThat(muNotes.size()).isEqualTo(1);
		int originalPitch = mu.getMus().get(0).getTopPitch();
		assertThat(muList.get(0).getMuNotes().get(0).getPitch()).isEqualTo(originalPitch);
	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_chord_tone_of_mu_returned_from_Poopline_is_the_same_duration() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(muList.size()).isEqualTo(1);
		double originalLengthInQuarters = mu.getMus().get(0).getLengthInQuarters();
		assertThat(muList.get(0).getLengthInQuarters()).isEqualTo(originalLengthInQuarters);
	}
	
	
	@Test
	void given_list_of_repeated_double_then_getRepeatingPattern_is_list_of_single_item() throws Exception
	{
		Double[] arr = new Double[] {1.0, 1.0, 1.0};
		List<Double> list = Arrays.asList(arr);
		List<Double> pattern = ArtefactToParameter.getRepeatingDoublePattern(list);
		assertThat(pattern.size()).isEqualTo(1);
		assertThat(pattern.get(0)).isEqualTo(1.0);
	}

	
	@Test
	void given_list_of_two_repeated_double_then_getRepeatingPattern_is_list_of_two_items() throws Exception
	{
		Double[] arr = new Double[] {1.0, 1.1, 1.0, 1.1, 1.0};
		List<Double> list = Arrays.asList(arr);
		List<Double> pattern = ArtefactToParameter.getRepeatingDoublePattern(list);
		assertThat(pattern.size()).isEqualTo(2);
		assertThat(pattern.get(0)).isEqualTo(1.0);
		assertThat(pattern.get(1)).isEqualTo(1.1);
	}
	
	
	@Test
	void given_list_of_non_repeated_doubles_then_getRepeatingPattern_is_original_list() throws Exception
	{
		Double[] arr = new Double[] {1.0, 1.1, 1.2, 1.3, 1.4};
		List<Double> list = Arrays.asList(arr);
		List<Double> pattern = ArtefactToParameter.getRepeatingDoublePattern(list);
		assertThat(pattern.size()).isEqualTo(5);
		assertThat(pattern.get(0)).isEqualTo(1.0);
		assertThat(pattern.get(1)).isEqualTo(1.1);
		assertThat(pattern.get(2)).isEqualTo(1.2);
		assertThat(pattern.get(3)).isEqualTo(1.3);
		assertThat(pattern.get(4)).isEqualTo(1.4);
	}
	
	
	@Test
	void when_two_chord_tone_are_equal_then_getChordToneVector_returns_vector_of_0() throws Exception
	{
		assertThat(ArtefactToParameter.getChordToneVector(62, 62, new Chord("Bm7b5"))).isEqualTo(0);
	}
	
	
	@Test
	void when_2nd_chord_tone_is_one_below_1st_then_getChordToneVector_returns_vector_of_minus_1() throws Exception
	{
		assertThat(ArtefactToParameter.getChordToneVector(60, 59, new Chord("Bm7b5"))).isEqualTo(-1);
	}
	
	
	@Test
	void when_2nd_chord_tone_is_two_below_1st_then_getChordToneVector_returns_vector_of_minus_2() throws Exception
	{
		assertThat(ArtefactToParameter.getChordToneVector(60, 57, new Chord("Bm7b5"))).isEqualTo(-2);
	}
	
	
	@Test
	void when_2nd_chord_tone_is_one_above_1st_then_getChordToneVector_returns_vector_of_1() throws Exception
	{
		assertThat(ArtefactToParameter.getChordToneVector(60, 62, new Chord("Bm7b5"))).isEqualTo(1);
	}
	
	
	@Test
	void when_2nd_chord_tone_is_two_above_1st_then_getChordToneVector_returns_vector_of_2() throws Exception
	{
		assertThat(ArtefactToParameter.getChordToneVector(60, 65, new Chord("Bm7b5"))).isEqualTo(2);
	}
	
	
	
	
	
//	@Test
//	void given_input_of_single_chord_and_two_chord_tones_then_two_chord_tones_are_returned_from_Poopline() throws Exception
//	{
//		LiveClip[] clips = getTwoMelodyAndChordClip();
//		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
//		PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
//		List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
//		assertThat(muList.size()).isEqualTo(2);
		
//		List<MuNote> muNotes = muList.get(0).getMuNotes();
//		assertThat(muNotes.size()).isEqualTo(1);
//		int originalPitch = mu.getMus().get(0).getTopPitch();
//		assertThat(muList.get(0).getMuNotes().get(0).getPitch()).isEqualTo(originalPitch);
//	}
	
	
	
	
//	@Test
//	void looping_test()
//	{
//		LiveClip[] clips = getMelodyAndChordClip();
//		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
//		List<List<Mu>> listOfLists = new ArrayList<List<Mu>>();
//		for (int i = 0; i < 10; i++)
//		{
//			PooplinePackage pack = getPackFromPipelineWithForceCreatePlugin(mu);
//			List<Mu> muList = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
//			listOfLists.add(muList);
//		}
//		for (List<Mu> muList: listOfLists)
//		{
//			System.out.println("muList.size=" + muList.size());
//			System.out.println("muNotes.size=" + muList.get(0).getMuNotes().size());
//		}
//	}


// privates -----------------------------------------------------------------------------------------------
	

	private PooplinePackage getPackFromPipelineWithForceCreatePlugin(Mu mu)
	{
		PooplinePackage pack = ArtefactToParameter.getPackFromMu(mu);
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		pack = pipeline.process(pack);
		return pack;
	}
	

	private LiveClip[] getMelodyAndChordClip()
	{
		LiveClip melodyClip = new LiveClip();
		melodyClip.loopEnd = 8.0;
		melodyClip.addNote(53, 0.0, 1.0, 64, 0);
		LiveClip chordClip = new LiveClip();
		chordClip.loopEnd = 8.0;
		chordClip.addNote(49, 0.0, 8.0, 64, 0);
		chordClip.addNote(44, 0.0, 8.0, 64, 0);
		chordClip.addNote(41, 0.0, 8.0, 64, 0);
		return new LiveClip[] {melodyClip, chordClip};
	}
	
	
	private LiveClip[] getTwoMelodyAndChordClip()
	{
		LiveClip melodyClip = new LiveClip();
		melodyClip.loopEnd = 8.0;
		melodyClip.addNote(53, 0.0, 1.0, 64, 0);
		melodyClip.addNote(49, 4.0, 1.0, 64, 0);
		LiveClip chordClip = new LiveClip();
		chordClip.loopEnd = 8.0;
		chordClip.addNote(49, 0.0, 8.0, 64, 0);
		chordClip.addNote(44, 0.0, 8.0, 64, 0);
		chordClip.addNote(41, 0.0, 8.0, 64, 0);
		return new LiveClip[] {melodyClip, chordClip};
	}
	
	
	private LiveClip[] getMelodyAndChordClip78()
	{
		LiveClip melodyClip = new LiveClip();
		melodyClip.setSignDenom(8);
		melodyClip.setSignNum(7);
		melodyClip.loopEnd = 7.0;
		melodyClip.addNote(53, 0.0, 1.0, 64, 0);
		LiveClip chordClip = new LiveClip();
		chordClip.setSignDenom(8);
		chordClip.setSignNum(7);
		chordClip.loopEnd = 7.0;
		chordClip.addNote(49, 0.0, 7.0, 64, 0);
		chordClip.addNote(44, 0.0, 7.0, 64, 0);
		chordClip.addNote(41, 0.0, 7.0, 64, 0);
		return new LiveClip[] {melodyClip, chordClip};
	}

}















