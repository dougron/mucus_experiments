package main.java.com.dougron.mucus_experiments.generator_poopline.embellisher;

import lombok.Getter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;

public class SemiTone implements MuEmbellisher
{
	
	@Getter private int semitoneOffset;
	
	
	
	public SemiTone(int aSemitoneOffset)
	{
		semitoneOffset = aSemitoneOffset;
	}

	
	
	@Override
	public void addNotes(Mu embellishment)
	{
		for (MuNote note: embellishment.getParent().getMuNotes())
		{
			int parentPitch = note.getPitch();
			
			embellishment.addMuNote(
					new MuNote(parentPitch + semitoneOffset, MuEmbellisher.DEFAULT_VELOCITY));
		}

	}

}
