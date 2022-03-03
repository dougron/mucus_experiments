package main.java.com.dougron.mucus_experiments.completed;


import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_001;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;


public class Mu029_QuickNastyRandomPhrase
{	
	Random rnd = new Random();
	
	String fileName = "Mu029_QuickNastyRandomPhrase";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	
	public Mu029_QuickNastyRandomPhrase()
	{
		Mu parent = makeSeriesOfRandomPhrases();	
		doOutput(parent);
	}



	private Mu makeSeriesOfRandomPhrases()
	{
		RandomMelodyGenerator rmg = RMG_001.getInstance();
		Mu parent = new Mu("parent");
		Mu previousMu = null;
		boolean hasPreviousMu = false;
		for (int i = 0; i < 4; i++) 
		{
			RMRandomNumberContainer por = rmg.getRandomNumberContainer(rnd);
			RandomMelodyParameterObject po = rmg.getParameterObject(por, rnd);	
			Mu mu = rmg.getMuPhrase(po);
			if (hasPreviousMu)
			{
				parent.addMuToEndOfSibling(mu, new BarsAndBeats(0, 0.0), previousMu);
				previousMu = mu;
			}
			else
			{
				parent.addMu(mu, 0);
				previousMu = mu;
				hasPreviousMu = true;
			}
		}
		return parent;
	}



	private void doOutput(Mu mu)
	{
		mu.addTag(MuTag.PRINT_CHORDS);		
		String x = ContinuousIntegrator.outputMuToXMLandLive(
				mu, 
				fileName, 
				trackIndex, 
				clipIndex, 
				injector
				);
		System.out.println(x);
	}



	public static void main(String[] args)
	{
		new Mu029_QuickNastyRandomPhrase();
	}
}
