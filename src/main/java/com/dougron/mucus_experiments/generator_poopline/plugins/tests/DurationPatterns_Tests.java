package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DurationPatterns;

class DurationPatterns_Tests
{

	

	@Test
	void when_two_DurationType_items_exist_then_they_are_applied_in_the_correct_order()
	{
		int[] indices = new int[] {0, 1};
		Map<Integer, DurationType[]> map = new HashMap<Integer, DurationType[]>();
		map.put(0, new DurationType[] {DurationType.LEGATO});
		map.put(1, new DurationType[] {DurationType.DEFAULT_SHORT});
		DurationPatterns plug = new DurationPatterns(indices, map);
		
		Mu parent = makeParentMu();
		
		PooplinePackage pack = new PooplinePackage("name", new Random());
		pack.setDebugMode(true);
		pack.setMu(parent);
		
		pack = plug.process(pack);
		assertThat(parent.getMu("st1").getMu("emb1").getLengthInQuarters()).isEqualTo(2.0);	// legato
		assertThat(parent.getMu("st2").getMu("emb2").getLengthInQuarters()).isEqualTo(0.5);	// default_short
	}
	
	
	@Test
	void when_two_DurationType_items_exist_and_there_are_more_than_two_structure_tones_then_they_are_applied_in_the_correct_order_and_loop_correctly()
	{
		int[] indices = new int[] {0, 1};
		Map<Integer, DurationType[]> map = new HashMap<Integer, DurationType[]>();
		map.put(0, new DurationType[] {DurationType.LEGATO});
		map.put(1, new DurationType[] {DurationType.DEFAULT_SHORT});
		DurationPatterns plug = new DurationPatterns(indices, map);
		
		Mu parent = makeParentMu();
		addThirdStructureToneAndEmbellishment(parent);
		
		PooplinePackage pack = new PooplinePackage("name", new Random());
		pack.setDebugMode(true);
		pack.setMu(parent);
		
		pack = plug.process(pack);
		assertThat(parent.getMu("st1").getMu("emb1").getLengthInQuarters()).isEqualTo(2.0);
		assertThat(parent.getMu("st2").getMu("emb2").getLengthInQuarters()).isEqualTo(0.5);
		assertThat(parent.getMu("st3").getMu("emb3").getLengthInQuarters()).isEqualTo(2.0);
	}

	
	
	private void addThirdStructureToneAndEmbellishment(Mu parent)
	{
		Mu st3 = new Mu("st3");
		st3.addMuNote(new MuNote(60, 64));
		st3.setLengthInQuarters(1.0);
		st3.addTag(MuTag.IS_STRUCTURE_TONE);
		parent.addMu(st3, 16.0);
		
		Mu emb3 = new Mu("emb3");
		emb3.addMuNote(new MuNote(60, 64));
		emb3.setLengthInQuarters(1.0);
		emb3.addTag(MuTag.IS_EMBELLISHMENT);
		st3.addMu(emb3, -2.0);
		
	}


	private Mu makeParentMu()
	{
		Mu parent = new Mu("parent");
		
		Mu st1 = new Mu("st1");
		st1.addMuNote(new MuNote(60, 64));
		st1.setLengthInQuarters(1.0);
		st1.addTag(MuTag.IS_STRUCTURE_TONE);
		parent.addMu(st1, 4.0);
		
		Mu emb1 = new Mu("emb1");
		emb1.addMuNote(new MuNote(60, 64));
		emb1.setLengthInQuarters(1.0);
		emb1.addTag(MuTag.IS_EMBELLISHMENT);
		st1.addMu(emb1, -2.0);
		
		Mu st2 = new Mu("st2");
		st2.addMuNote(new MuNote(60, 64));
		st2.setLengthInQuarters(1.0);
		st2.addTag(MuTag.IS_STRUCTURE_TONE);
		parent.addMu(st2, 12.0);
		
		Mu emb2 = new Mu("emb2");
		emb2.addMuNote(new MuNote(60, 64));
		emb2.setLengthInQuarters(1.0);
		emb2.addTag(MuTag.IS_EMBELLISHMENT);
		st2.addMu(emb2, -2.0);
		
		return parent;
	}

}
