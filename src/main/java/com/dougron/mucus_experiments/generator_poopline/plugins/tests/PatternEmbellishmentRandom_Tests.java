package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo.EmbellishmentRhythmResolution;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;
import test.java.com.dougron.mucus.algorithms.TestRandom;

class PatternEmbellishmentRandom_Tests {

	@Test
	void instantiates() {
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_repo_gets_a_PATTERN_EMBELLISHER_item() throws Exception {
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(0.1));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		assertThat(pack.getRepo().containsKey(Parameter.PATTERN_EMBELLISHER)).isTrue();
	}

	
	@Test
	void when_empty_pack_is_processed_then_repo_PATTERN_EMBELLISHER_item_has_resolutionRndValue_equal_to_TestRandomValue() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		assertThat(repo.getResolutionRndValue()).isEqualTo(testRandomValue);
	}
	
	
	@Test
	void when_empty_pack_is_processed_and_TestRandomValue_is_0_1_then_repo_PATTERN_EMBELLISHER_item_has_selectedResolution_equal_to_EIGHTHS() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		assertThat(repo.getSelectedResolution()).isEqualTo(EmbellishmentRhythmResolution.EIGHTHS);
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_repo_PATTERN_EMBELLISHER_item_has_resolutionOption_equal_to_resolutionOptions_in_PatternEmbellisherRandom() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		assertThat(repo.getResolutionOptions()).containsExactly(plug.getResolutionOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_pitch_and_rhythm_sync_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
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
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
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
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		// pitchGeneratorRndValues = testRandomValue
		Map<Integer, List<Double>> rndValues = repo.getPitchGeneratorRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(2);
		assertThat(rndValues.keySet()).contains(0);
		assertThat(rndValues.keySet()).contains(1);
		assertThat(rndValues.get(0).size()).isEqualTo(8);	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected option = false
		Map<Integer, List<MuEmbellisher>> selectedPitchGenerators = repo.getSelectedPitchGenerators();
		assertThat(selectedPitchGenerators.keySet().size()).isEqualTo(2);
		assertThat(selectedPitchGenerators.keySet()).contains(0);
		assertThat(selectedPitchGenerators.keySet()).contains(1);
		assertThat(selectedPitchGenerators.get(0).size()).isEqualTo(8);	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected options are same as plug defaults
		assertThat(repo.getPitchGeneratorOptions()).containsExactly(plug.getPitchGeneratorOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_rhythm_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
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
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		// pitchGeneratorRndValues = testRandomValue
		Map<Integer, List<Double>> rndValues = repo.getRhythmOffsetRndValues();
		assertThat(rndValues.keySet().size()).isEqualTo(2);
		assertThat(rndValues.keySet()).contains(0);
		assertThat(rndValues.keySet()).contains(1);
		assertThat(rndValues.get(0).size()).isEqualTo(8);	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected option = false
		Map<Integer, List<RhythmOffset>> selectedRhythmOffsets = repo.getSelectedRhythmOffsets();
		assertThat(selectedRhythmOffsets.keySet().size()).isEqualTo(2);
		assertThat(selectedRhythmOffsets.keySet()).contains(0);
		assertThat(selectedRhythmOffsets.keySet()).contains(1);
		assertThat(selectedRhythmOffsets.get(0).size()).isEqualTo(8);	// DEFAULT_MU_EMBELLISHER_COUNT
		// selected options are same as plug defaults
		assertThat(repo.getRhythmOffsetOptions()).containsExactly(plug.getRhythmOffsetOptions());
	}
	
	
	@Test
	void when_empty_pack_is_processed_then_collision_index_pattern_items_are_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
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
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
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
	void when_empty_pack_is_processed_then_class_name_item_is_present_in_repo() throws Exception {
		double testRandomValue = 0.1;
		PooplinePackage pack = new PooplinePackage("XX", new TestRandom(testRandomValue));
		PatternEmbellisherRandom plug = new PatternEmbellisherRandom();
		pack = plug.process(pack);
		PatternEmbellishmentRepo repo = (PatternEmbellishmentRepo)pack.getRepo().get(Parameter.PATTERN_EMBELLISHER);
		assertThat(repo.getClassName()).isEqualTo(plug.getClass().getName());
	}

}
