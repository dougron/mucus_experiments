package main.java.com.dougron.mucus_experiments.completed;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

public class Mu023_XMLChordSymbols
{
	
	String fileName = "Mu023_XMLChordSymbols";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	
	public Mu023_XMLChordSymbols()
	{
		Mu mu = new Mu("023");
		mu.setLengthInBars(4);
		mu.addTag(MuTag.PRINT_CHORDS);
		
		Mu mu1 = new Mu("Mu1");
		mu1.addMuAnnotation(new MuAnnotation("Cm"));		
		mu.addMu(mu1, new BarsAndBeats(0, 2.0));
		
		Mu note0 = new Mu("note0");
		note0.addMuNote(new MuNote(60, 48));
		note0.setLengthInQuarters(1.0);
		mu.addMu(note0, 0.0);
		
		Mu note1 = new Mu("note0");
		note1.addMuNote(new MuNote(60, 48));
		note1.setLengthInQuarters(1.0);
		mu.addMu(note1, 1.0);
		
		Mu note2 = new Mu("note0");
		note2.addMuNote(new MuNote(60, 48));
		note2.setLengthInQuarters(1.0);
		mu.addMu(note2, 2.0);
		
		Mu note3 = new Mu("note0");
		note3.addMuNote(new MuNote(60, 48));
		note3.setLengthInQuarters(1.0);
		mu.addMu(note3, 3.0);
		
		
		Mu mu2 = new Mu("Mu2");
		mu2.addMuAnnotation(new MuAnnotation("Xm"));		
		mu.addMu(mu2, new BarsAndBeats(0, 1.0));
		
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	
	
	
	
	public static void main(String[] args)
	{
		new Mu023_XMLChordSymbols();
	}
}
