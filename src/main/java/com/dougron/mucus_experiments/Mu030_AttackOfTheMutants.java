package main.java.com.dougron.mucus_experiments;

import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_001;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

public class Mu030_AttackOfTheMutants
{

	Random rnd = new Random();
	
	String fileName = "Mu030_AttackOfTheMutants";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	
	public Mu030_AttackOfTheMutants()
	{
		Mu mu = attackOfTheMutants();
		doOutput(mu);
	}
	
	
	
	private Mu attackOfTheMutants()
	{
		double minimumMutation = 0.15;
		int numberOfMutantAttacks = 1;
		int numberOfPhrases = 5;
		RandomMelodyGenerator rmg = RMG_001.getInstance();
		
		Mu parent = new Mu("parent");		
		Parameter[] listOfMutableParameters = getListOfMutableParameters();
		
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		RandomMelodyParameterObject po = rmg.getParameterObject(rndContainer, rnd);
		Mu mu1 = rmg.getMuPhrase(po);
		parent.addMu(mu1, 0);
		
		
		Mu previousMu = mu1;
		
		for (int j = 0; j < numberOfPhrases; j++)
		{
			System.out.println("Phrase:" + j + " -------------------------");
			RMRandomNumberContainer mutant 
			= rmg.getMutatedRandomNumberContainer
			(
					minimumMutation,
					numberOfMutantAttacks, 
					listOfMutableParameters, 
					rndContainer, 
					rnd
					);
			RandomMelodyParameterObject po2 = rmg.getParameterObject(mutant, rnd);
			Mu mu2 = rmg.getMuPhrase(po2);
			parent.addMuToEndOfSibling(mu2, 0, previousMu);
			previousMu = mu2;
		}
		return parent;
	}



	private Parameter[] getListOfMutableParameters()
	{
		Parameter[] listOfMutableParameters = new Parameter[]
				{
						Parameter.PHRASE_LENGTH, 
						Parameter.TIME_SIGNATURE, 
						Parameter.PHRASE_START_PERCENT, 
						Parameter.PHRASE_END_PERCENT, 
						Parameter.STRUCTURE_TONE_SPACING, 
						Parameter.TEMPO, 
						Parameter.XMLKEY, 
						Parameter.START_NOTE, 
						Parameter.STRUCTURE_TONE_CONTOUR, 
						Parameter.STRUCTURE_TONE_MULTIPLIER, 
						Parameter.CHORD_LIST_GENERATOR, 
						Parameter.EMBELLISHMENT_CLEARANCE, 
						Parameter.EMBELLISHMENT_REPETITION_PATTERN, 
						Parameter.MUG_LISTS
				};
		return listOfMutableParameters;
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
		new Mu030_AttackOfTheMutants();
	}
}
