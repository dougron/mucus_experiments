package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo.EmbellishmentRhythmResolution;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class PatternEmbellishmentRandom_Tests {

	@Test
	void instantiates() {
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_repo_gets_a_EMBELLISHMENT_GENERATOR_item() throws Exception {
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(0.1));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.EMBELLISHMENT_GENERATOR)).isTrue();
	}

	
	@Test
	void when_empty_pack_is_processed_then_repo_EMBELLISHMENT_GENERATOR_item_has_resolutionRndValue_equal_to_TestRandomValue() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		assertThat(repo.getResolutionRndValue()).isEqualTo(testRandomValue);
	}
	
	
	@Test
	void when_empty_pack_is_processed_and_TestRandomValue_is_0_1_then_repo_EMBELLISHMENT_GENERATOR_item_has_selectedResolution_equal_to_EIGHTHS() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		assertThat(repo.getSelectedResolution()).isEqualTo(EmbellishmentRhythmResolution.EIGHTHS);
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_repo_EMBELLISHMENT_GENERATOR_item_has_resolutionOption_equal_to_resolutionOptions_in_PatternEmbellisherRandom() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		assertThat(repo.getResolutionOptions()).containsExactly(plug.getResolutionOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_pitch_and_rhythm_sync_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchAndRhythmPatternSyncRndValue = testRandomValue
		assertThat(repo.getPitchAndRhythmPatternSyncRndValue()).isEqualTo(testRandomValue);
		// selected option = false
		assertThat(repo.isSelectedPitchAndRhythmPatternSync()).isEqualTo(false);
		// selected options are same as plug defaults
		assertThat(repo.getPitchAndRhythmPatternSyncOptions()).containsExactly(plug.getPitchAndRhythmPatternSyncOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_pitch_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchAndRhythmPatternSyncRndValue = testRandomValue
		assertThat(repo.getPitchIndexPatternRndValue()).isEqualTo(testRandomValue);
		// selected option = false
		assertThat(repo.getSelectedPitchIndexPattern()).containsExactly(new int[] {0, 1});
		// selected options are same as plug defaults
		int[][] repoStuff = repo.getPitchIndexPatternOptions();
		int[][] plugStuff = plug.getPitchIndexPatternOptions();
		assertThat(repoStuff.length).isEqualTo(plugStuff.length);
		for (int i = 0; i < repoStuff.length; i++) {
			assertThat(repoStuff[i]).containsExactly(plugStuff[i]);
			
		}
	}

	
	@Test
	void when_empty_pack_is_processed_then_pitch_generator_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchGeneratorRndValues = testRandomValue
		Map<Integer, List<Double>> rndValues = repo.getPitchGeneratorRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(2);
		assertThat(rndValues.keySet()).contains(0);
		assertThat(rndValues.keySet()).contains(1);
		assertThat(rndValues.get(0).size()).isEqualTo(plug.getDEFAULT_MU_EMBELLISHER_COUNT());	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected option = false
		Map<Integer, List<MuEmbellisher>> selectedPitchGenerators = repo.getSelectedPitchGenerators();
		assertThat(selectedPitchGenerators.keySet().size()).isEqualTo(2);
		assertThat(selectedPitchGenerators.keySet()).contains(0);
		assertThat(selectedPitchGenerators.keySet()).contains(1);
		assertThat(selectedPitchGenerators.get(0).size()).isEqualTo(plug.getDEFAULT_MU_EMBELLISHER_COUNT());	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected options are same as plug defaults
		assertThat(repo.getPitchGeneratorOptions()).containsExactly(plug.getPitchGeneratorOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_rhythm_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchAndRhythmPatternSyncRndValue = testRandomValue
		assertThat(repo.getRhythmIndexPatternRndValue()).isEqualTo(testRandomValue);
		// selected option = false
		assertThat(repo.getSelectedRhythmIndexPattern()).containsExactly(new int[] {0, 1});
		// selected options are same as plug defaults
		int[][] repoStuff = repo.getRhythmIndexPatternOptions();
		int[][] plugStuff = plug.getRhythmIndexPatternOptions();
		assertThat(repoStuff.length).isEqualTo(plugStuff.length);
		for (int i = 0; i < repoStuff.length; i++) {
			assertThat(repoStuff[i]).containsExactly(plugStuff[i]);
			
		}
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_rhythm_generator_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchGeneratorRndValues = testRandomValue
		Map<Integer, List<Double>> rndValues = repo.getRhythmOffsetRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(2);
		assertThat(rndValues.keySet()).contains(0);
		assertThat(rndValues.keySet()).contains(1);
		assertThat(rndValues.get(0).size()).isEqualTo(plug.getDEFAULT_MU_EMBELLISHER_COUNT());	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected option = false
		Map<Integer, List<RhythmOffset>> selectedRhythmOffsets = repo.getSelectedRhythmOffsets();
		assertThat(selectedRhythmOffsets.keySet().size()).isEqualTo(2);
		assertThat(selectedRhythmOffsets.keySet()).contains(0);
		assertThat(selectedRhythmOffsets.keySet()).contains(1);
		assertThat(selectedRhythmOffsets.get(0).size()).isEqualTo(plug.getDEFAULT_MU_EMBELLISHER_COUNT());	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected options are same as plug defaults
		assertThat(repo.getRhythmOffsetOptions()).containsExactly(plug.getRhythmOffsetOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_collision_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchAndRhythmPatternSyncRndValue = testRandomValue
		assertThat(repo.getCollisionIndexPatternRndValue()).isEqualTo(testRandomValue);
		// selected option = false
		assertThat(repo.getSelectedCollisionIndexPattern()).containsExactly(new int[] {0});
		// selected options are same as plug defaults
		int[][] repoStuff = repo.getCollisionIndexPatternOptions();
		int[][] plugStuff = plug.getCollisionIndexPatternOptions();
		assertThat(repoStuff.length).isEqualTo(plugStuff.length);
		for (int i = 0; i < repoStuff.length; i++) {
			assertThat(repoStuff[i]).containsExactly(plugStuff[i]);
			
		}
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_collision_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// collisionOffsetRndValues = testRandomValue
		Map<Integer, Double> rndValues = repo.getCollisionOffsetRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(1);
		assertThat(rndValues.keySet()).contains(0);
		// selected option = false
		Map<Integer, RhythmOffset> selectedCollisionOffsets = repo.getSelectedCollisionOffsets();
		assertThat(selectedCollisionOffsets.keySet().size()).isEqualTo(1);
		assertThat(selectedCollisionOffsets.keySet()).contains(0);
		// selected options are same as plug defaults
		assertThat(repo.getCollisionOffsetOptions()).containsExactly(plug.getCollisionOffsetOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_count_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// pitchAndRhythmPatternSyncRndValue = testRandomValue
		assertThat(repo.getCountIndexPatternRndValue()).isEqualTo(testRandomValue);
		// selected option = false
		assertThat(repo.getSelectedCountIndexPattern()).containsExactly(new int[] {0, 1});
		// selected options are same as plug defaults
		int[][] repoStuff = repo.getCountIndexPatternOptions();
		int[][] plugStuff = plug.getCountIndexPatternOptions();
		assertThat(repoStuff.length).isEqualTo(plugStuff.length);
		for (int i = 0; i < repoStuff.length; i++) {
			assertThat(repoStuff[i]).containsExactly(plugStuff[i]);
			
		}
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_count_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		// collisionOffsetRndValues = testRandomValue
		Map<Integer, Double> rndValues = repo.getCountRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(1);
		assertThat(rndValues.keySet()).contains(0);
		// selected option = false
		Map<Integer, Integer> selectedCounts = repo.getSelectedCounts();
		assertThat(selectedCounts.keySet().size()).isEqualTo(1);
		assertThat(selectedCounts.keySet()).contains(0);
		// selected options are same as plug defaults
		assertThat(repo.getCountOptions()).containsExactly(plug.getCountOptions());
	}

	
	@Test
	void when_empty_pack_is_processed_then_class_name_item_is_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		pack.setDebugMode(true);
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}
	
	
	@Test
	void when_pack_with_structure_tones_is_processed_then_structure_tones_get_embellishments() throws Exception {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		pack.setDebugMode(true);
		TessituraFixed tessPlug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		pack = tessPlug.process(pack);
		StartNoteMelodyRandom startPlug = new StartNoteMelodyRandom();
		pack = startPlug.process(pack);
		ContourMultiplierRandom multPlug = new ContourMultiplierRandom();
		pack = multPlug.process(pack);
		ContourChordTonesRandom contourPlug = new ContourChordTonesRandom();
		pack = contourPlug.process(pack);
		PatternEmbellisherRandom embellisherPlug = new PatternEmbellisherRandom();
		pack = embellisherPlug.process(pack);
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		assertThat(structureTones.size()).isGreaterThan(0);
		assertThat(structureTones.get(0).getAllMus().size()).isGreaterThan(1);
	}
	
	
	@Test
	void when_empty_pack_is_processed_with_a_higher_rhythm_offset_count_than_created_by_default_then_new_items_will_be_added() throws Exception {
		PooplinePackage pack = getPackWithPitchFreeStructureTones();
		
		TessituraFixed tessPlug = new TessituraFixed(Parameter.TESSITURA_START_NOTE);
		pack = tessPlug.process(pack);
		StartNoteMelodyRandom startPlug = new StartNoteMelodyRandom();
		pack = startPlug.process(pack);
		ContourMultiplierRandom multPlug = new ContourMultiplierRandom();
		pack = multPlug.process(pack);
		ContourChordTonesRandom contourPlug = new ContourChordTonesRandom();
		pack = contourPlug.process(pack);
		PatternEmbellisherRandom embellisherPlug = new PatternEmbellisherRandom();
		int newForcedCount = 7;
		embellisherPlug.setCountOptions(new int[] {newForcedCount});
		embellisherPlug.setCollisionOffsetOptions(new RhythmOffset[] {new RhythmOffset(0, 0, 1, 0, 0)});
		pack = embellisherPlug.process(pack);
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
		assertThat(repo.getSelectedCounts().get(0)).isEqualTo(newForcedCount);
		assertThat(repo.getRhythmOffsetRndValues().get(0).size()).isEqualTo(newForcedCount);
	}
	
	
//	@Test
//	void when_empty_pack_is_processed_with_a_higher_rhythm_offset_count_than_created_by_default_then_new_items_will_be_added() throws Exception {
//		double testRandomValue = 0.1;
//		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
//		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
//		pack = plug.process(pack);
//	}
	
	
// ------- privates -------------------------------------------------------------------

	private PooplinePackage getPackWithPitchFreeStructureTones() {
		// 0.75 of 8 bar phrase with spacing 1.0 floatbars given 5/4 time equates to 6
		// items with the tag at 0.0, 5.0, 10.0, 15.0, 20.0, 25.0 quartrs position
		PooplinePackage pack = new PooplinePackage("x", new TestRandom(0.55));
		pack.setDebugMode(true);
		// length 8 bars
		PooplinePlugin lengthPlug = new PhraseLengthRandom();
		pack = lengthPlug.process(pack);

		// time signature 5/4
		PooplinePlugin timeSignaturePlug = new TimeSignatureSingleRandom();
		pack.setRnd(new TestRandom(0.7));
		pack = timeSignaturePlug.process(pack);

		// start and end percent can be faked without plugins as they have no effect on
		// the mu, unlike phrase length and time signature
		PhraseBoundRepo phraseStartRepo = getPhraseStartPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_START_PERCENT, phraseStartRepo);
		PhraseBoundRepo phraseEndRepo = getPhraseEndPercentRepo();
		pack.getRepo().put(Parameter.PHRASE_END_PERCENT, phraseEndRepo);
		StructureToneEvenlySpacedRandom plug = new StructureToneEvenlySpacedRandom();
		pack.setRnd(new TestRandom(0.5));
		pack = plug.process(pack);

		return pack;
	}
	
	
	private PhraseBoundRepo getPhraseEndPercentRepo() {
		PhraseBoundRepo phraseEndRepo = PhraseBoundRepo.builder()
				.selectedValue(0.75)
				.rndValue(0.75)
				.rangeLow(0.0)
				.rangeHigh(1.0)
				.className(PhraseBoundPercentRandom.class.getName())
				.build();
		return phraseEndRepo;
	}


	private PhraseBoundRepo getPhraseStartPercentRepo() {
		PhraseBoundRepo phraseStartRepo = PhraseBoundRepo.builder()
				.selectedValue(-0.01)
				.rndValue(0.5)
				.rangeLow(-0.05)
				.rangeHigh(0.01)
				.className(PhraseBoundPercentRandom.class.getName())
				.build();
		return phraseStartRepo;
	}
}
