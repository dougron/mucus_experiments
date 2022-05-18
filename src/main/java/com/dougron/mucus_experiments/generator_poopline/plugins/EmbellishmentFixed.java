package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.List;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.artefact_to_parameter.EmbellishmentSchema;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EmbellishmentFixedRepo;

public class EmbellishmentFixed extends PlugGeneric
{
	
	private static final int DEFAULT_VELOCITY = 48;
	private static final double DEFAULT_LENGTH_IN_QUARTERS = 0.5;
	EmbellishmentFixedRepo embellishmentRepo;

	
	public EmbellishmentFixed() {
		super(
				Parameter.EMBELLISHMENT_GENERATOR,
				new Parameter[] {Parameter.STRUCTURE_TONE_GENERATOR}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}
	
	
	
	@Override
	PooplinePackage updateMu(PooplinePackage pack)
	{
		List<Mu> structureTones = pack.getMu().getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		
		// this is QUARTERS_FROM_BACK approach for now, without looping
		int embellishmentSchemaIndex = 0;
		Mu currentMu;
		for (Mu structureTone: structureTones)
		{
			currentMu = structureTone;
			EmbellishmentSchema schema = embellishmentRepo.getSchemaList().get(embellishmentSchemaIndex);
			double[] quartersOffsets = EmbellishmentSchema.getBackwardQuartersSpacing(schema);
			int[] semitoneOffsets = EmbellishmentSchema.getBackwardSemitoneSpacing(schema);
			int embellishmentCount = schema.size() - 2;
			for (int i = 0; i < embellishmentCount; i++)
			{
				int quartersOffsetIndex = quartersOffsets.length - (i % quartersOffsets.length) - 1;
				int semitoneOffsetIndex = semitoneOffsets.length - (i % semitoneOffsets.length) - 1;
				double quartersOffset = quartersOffsets[quartersOffsetIndex];
				int semitoneOffset = semitoneOffsets[semitoneOffsetIndex];
				// make embellishment mu
				Mu emb = new Mu("emb");
				for (MuNote note: currentMu.getMuNotes())
				{
					emb.addMuNote(new MuNote(note.getPitch() + semitoneOffset, DEFAULT_VELOCITY));
				}
				emb.setLengthInQuarters(DEFAULT_LENGTH_IN_QUARTERS);
				emb.addTag(MuTag.IS_EMBELLISHMENT);
				currentMu.addMu(emb, quartersOffset);
				currentMu = emb;
			}
			
			embellishmentSchemaIndex++;
			if (embellishmentSchemaIndex >= embellishmentRepo.getSchemaList().size()) embellishmentSchemaIndex = 0;
		}
		return pack;
	}
	
	
	
	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		return pack;
	}

	 
	
	@Override
	void getRepoFromPack(PooplinePackage pack)
	{
		embellishmentRepo = (EmbellishmentFixedRepo)pack.getRepo().get(Parameter.EMBELLISHMENT_GENERATOR);
	}

	
	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
	}
	
}
