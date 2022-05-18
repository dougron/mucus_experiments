package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.artefact_to_parameter.EmbellishmentSchema;
import main.java.com.dougron.mucus_experiments.artefact_to_parameter.NoteInfo;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EmbellishmentFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EmbellishmentFixedRepo;

class EmbellishmentFixed_Tests
{

	@Nested
	class when_mu_has_one_structure_tone_and_schema_has_one_embellishment_then
	{
		@Test
		void _mu_returns_with_two_mus_with_notes()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithOneEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			assertThat(pack.getMu().getMusWithNotes().size()).isEqualTo(2);
		}
		
		
		@Test
		void _mu_embellishment_has_correct_global_position_parameter()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithOneEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			Mu embellishment = pack.getMu().getMus().get(0).getMus().get(0);
			assertThat(embellishment.getGlobalPositionInQuarters()).isEqualTo(1.0);
		}
		
		
		@Test
		void _mu_embellishment_has_correct_pitch_parameter()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithOneEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			Mu embellishment = pack.getMu().getMus().get(0).getMus().get(0);
			assertThat(embellishment.getMuNotes().size()).isEqualTo(1);
			assertThat(embellishment.getMuNotes().get(0).getPitch()).isEqualTo(60);
		}
		
	}
	
	
	@Nested
	class when_mu_has_one_structure_tone_and_schema_has_two_embellishments_then
	{
		@Test
		void _mu_returns_with_three_mus_with_notes()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithTwoEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			assertThat(pack.getMu().getMusWithNotes().size()).isEqualTo(3);
		}
		
		
		@Test
		void _mu_embellishment_has_correct_global_position_parameter()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithTwoEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			Mu embellishment1 = pack.getMu().getMus().get(0).getMus().get(0);
			assertThat(embellishment1.getGlobalPositionInQuarters()).isEqualTo(1.5);
			Mu embellishment2 = pack.getMu().getMus().get(0).getMus().get(0).getMus().get(0);
			assertThat(embellishment2.getGlobalPositionInQuarters()).isEqualTo(1.0);
		}
		
		
		@Test
		void _mu_embellishment_has_correct_pitch_parameter()
		{
			Mu parent = makeParentMuWithOneStructureTone();
			EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
					.schemaList(getSchemaListWithTwoEmbellishment())
					.className(EmbellishmentFixed.class.getName())
					.build();
			
			PooplinePackage pack = new PooplinePackage("pack", new Random());
			pack.setDebugMode(true);
			pack.setMu(parent);
			pack.getRepo().put(Parameter.EMBELLISHMENT_GENERATOR, repo);
			
			EmbellishmentFixed plug = new EmbellishmentFixed();
			
			pack = plug.process(pack);
			
			Mu embellishment1 = pack.getMu().getMus().get(0).getMus().get(0);
			assertThat(embellishment1.getMuNotes().size()).isEqualTo(1);
			assertThat(embellishment1.getMuNotes().get(0).getPitch()).isEqualTo(60);
			
			
			Mu embellishment2 = pack.getMu().getMus().get(0).getMus().get(0).getMus().get(0);
			assertThat(embellishment2.getMuNotes().size()).isEqualTo(1);
			assertThat(embellishment2.getMuNotes().get(0).getPitch()).isEqualTo(55);
		}
		
	}



	private EmbellishmentFixedRepo makeEmbellishmentFixedRepo()
	{
		EmbellishmentFixedRepo repo = EmbellishmentFixedRepo.builder()
				.schemaList(getSchemaListWithOneEmbellishment())
				.className(EmbellishmentFixed.class.getName())
				.build();
		return repo;
	}



	private Mu makeParentMuWithOneStructureTone()
	{
		Mu parent = new Mu("parent");
		parent.setLengthInBars(1);
		Mu structureTone = new Mu("st");
		structureTone.setLengthInQuarters(1.0);
		structureTone.addTag(MuTag.IS_STRUCTURE_TONE);
		structureTone.addMuNote(new MuNote(60, 64));
		parent.addMu(structureTone, 2.0);
		return parent;
	}

	
	
	private List<EmbellishmentSchema> getSchemaListWithOneEmbellishment()
	{
		List<EmbellishmentSchema> list = new ArrayList<EmbellishmentSchema>();
		EmbellishmentSchema schema= new EmbellishmentSchema();
		
		// structure tone
		schema.add(NoteInfo.builder()
				.positionInQuarters(2.0)
				.pitch(60)
				.build());
		
		// embellishment tone
		schema.add(NoteInfo.builder()
				.positionInQuarters(1.0)
				.pitch(60)
				.build());
		
		// previous structure tone, or placeholder for the same
		schema.add(NoteInfo.builder()
				.positionInQuarters(0.0)
				.pitch(60)
				.build());
		
		list.add(schema);
		return list;
	}
	
	
	
	private List<EmbellishmentSchema> getSchemaListWithTwoEmbellishment()
	{
		List<EmbellishmentSchema> list = new ArrayList<EmbellishmentSchema>();
		EmbellishmentSchema schema= new EmbellishmentSchema();
		
		// structure tone
		schema.add(NoteInfo.builder()
				.positionInQuarters(2.0)
				.pitch(60)
				.build());
		
		// embellishment tone
		schema.add(NoteInfo.builder()
				.positionInQuarters(1.5)
				.pitch(60)
				.build());
		
		// embellishment tone
		schema.add(NoteInfo.builder()
				.positionInQuarters(1.0)
				.pitch(55)
				.build());
		
		// previous structure tone, or placeholder for the same
		schema.add(NoteInfo.builder()
				.positionInQuarters(0.0)
				.pitch(60)
				.build());
		
		list.add(schema);
		return list;
	}

}
