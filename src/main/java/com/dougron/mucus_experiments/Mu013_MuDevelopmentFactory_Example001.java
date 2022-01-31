package main.java.com.dougron.mucus_experiments;

import main.java.com.dougron.mucus.algorithms.mu_development_factory.MuDevelopmentFactory;
import main.java.com.dougron.mucus.algorithms.mu_development_factory.MuDevelopmentFactory.Relationship;
import main.java.com.dougron.mucus.algorithms.superimposifier.left_to_right.SuFi_IntervalModel_Generator;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuAnnotation;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

/*
 * first integration example of the MuDevelopmentFactory.
 * 
 * 	1)	A phrase length Mu is generated
 * 	2)	this is passed to theMuDevelopmentFactory and returned is a 2 phrase section 
 * 		with a MuRelationship
 * 
 */

public class Mu013_MuDevelopmentFactory_Example001
{
	
	
	String fileName = "Mu013_MuDevelopmentFactory_Example001";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	
	
	public Mu013_MuDevelopmentFactory_Example001()
	{
		int[] intervals = new int[] {2, -1};
		Mu phrase = makePhrase(intervals);
//		System.out.println(phrase.stateOfMusToString());
		ContinuousIntegrator.outputMuToXMLandLive(phrase, fileName, trackIndex, clipIndex, injector);
		Mu section = MuDevelopmentFactory.makeTwoPhraseSectionFromOnePhraseWithRelationship(phrase, Relationship.SIMILAR);
		section.generate();
		System.out.println(section.stateOfMusToString());
		ContinuousIntegrator.outputMuToXMLandLive(section, fileName, trackIndex, clipIndex + 1, injector);
	}
	


	private Mu makePhrase(int[] intervals)
	{
		Mu mu = new Mu("phrase1");	
		mu.addMuAnnotation(new MuAnnotation(mu.getName()));
		mu.setLengthInBars(4);
		mu.setStartPitch(50);
		mu.addSuFi(new SuFi_IntervalModel_Generator(0.0, 9.0, intervals));
		mu.runSuFiSuGenerator();
		return mu;
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu013_MuDevelopmentFactory_Example001();
	}


	
}
