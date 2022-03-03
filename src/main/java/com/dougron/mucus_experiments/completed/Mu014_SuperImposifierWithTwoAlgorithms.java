package main.java.com.dougron.mucus_experiments.completed;

import main.java.com.dougron.mucus.algorithms.mu_development_factory.MuDevelopmentFactory;
import main.java.com.dougron.mucus.algorithms.superimposifier.left_to_right.SuFi_IntervalModel_Generator;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

/*
 * uses the SuFiSu_LeftToRight 
 */


public class Mu014_SuperImposifierWithTwoAlgorithms
{
	
	
	String fileName = "Mu014_SuperImposifierWithTwoAlgorithms";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	
	public Mu014_SuperImposifierWithTwoAlgorithms()
	{
		int[] intervals = new int[] {2, 2, -3};
		Mu phrase = makePhrase(intervals);
		ContinuousIntegrator.outputMuToXMLandLive(phrase, fileName, trackIndex, clipIndex, injector);
		MuDevelopmentFactory.resetRandom(1);
		MuDevelopmentFactory.addIntervalAndRythmModelGenerator(phrase, 3.0, 8.0, MuDevelopmentFactory.Relationship.SIMILAR);
		phrase.generate();
		ContinuousIntegrator.outputMuToXMLandLive(phrase, fileName, trackIndex, clipIndex + 1, injector);
	}
	
	
	
	private Mu makePhrase(int[] intervals)
	{
		Mu mu = new Mu("phrase1");	
		mu.addMuAnnotation(new MuAnnotation(mu.getName()));
		mu.setLengthInBars(4);
		mu.setStartPitch(50);
		mu.addSuFi(new SuFi_IntervalModel_Generator(0.0, 13.0, intervals));
		mu.runSuFiSuGenerator();
		return mu;
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu014_SuperImposifierWithTwoAlgorithms();
	}
}
