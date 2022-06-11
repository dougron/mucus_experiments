package main.java.com.dougron.mucus_experiments.generator_poopline.embellisher;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;

public class Anticipation implements MuEmbellisher {

	
	
	
	private static MuEmbellisher instance;
	
	private Anticipation() {
		
	}
	
	
	public static MuEmbellisher getInstance() {
		if (instance == null) {
			instance = new Anticipation();
		}
		return instance;
	}


	@Override
	public void addNotes(Mu aMu) {
		aMu.addTag(MuTag.IS_ANTICIPATION);
		int pitch = aMu.getParent().getTopPitch();
		aMu.addMuNote(new MuNote(pitch, MuEmbellisher.DEFAULT_VELOCITY));		
	}
	
	
	public String toString()
	{
		return "Anticipation: ...";
	}

}
