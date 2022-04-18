package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
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
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_pack_from_ArtefactToParameter_contains_required_repo_items() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = ArtefactToParameter.getPackFromMu(mu);
		assertThat(pack.getRepo().containsKey(Parameter.PHRASE_LENGTH)).isTrue();
	}
	
	
	@Test
	void given_input_of_single_chord_and_chord_tone_then_mu_returned_from_Poopline_is_the_same() throws Exception
	{
		LiveClip[] clips = getMelodyAndChordClip();
		Mu mu = MuLiveClipUtils.makeMu(clips[0], clips[1]);
		PooplinePackage pack = ArtefactToParameter.getPackFromMu(mu);
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		pack = pipeline.process(pack);
//		System.out.println(pack.toString());
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(mu.getLengthInBars());
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

}















