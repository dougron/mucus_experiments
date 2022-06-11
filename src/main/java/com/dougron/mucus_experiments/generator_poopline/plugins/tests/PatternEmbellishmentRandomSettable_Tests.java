package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PatternEmbellisherRandomSettable;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo;

class PatternEmbellishmentRandomSettable_Tests
{

	@Test
	void instantiates() {
		PatternEmbellisherRandomSettable plug = new PatternEmbellisherRandomSettable();
		assertThat(plug).isNotNull();
	}
	
	
	@Test
	void patterns_are_settable() {
		PatternEmbellisherRandomSettable plug = new PatternEmbellisherRandomSettable();
		
		Mu mu = new Mu("test");
		Mu note = new Mu("st");
		note.setLengthInQuarters(1.0);
		note.addMuNote(60, 64);
		note.addTag(MuTag.IS_STRUCTURE_TONE);
		mu.addMu(note, 1);
		
		PooplinePackage pack = new PooplinePackage("XX", new Random());
		PatternEmbellishmentRepo repo = PatternEmbellishmentRepo.builder()
				.selectedCollisionIndexPattern(new int[] {0})
				.selectedCollisionOffsets(null)
				.build();
	}

}
