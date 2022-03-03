package main.java.com.dougron.mucus_experiments.completed;

import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_AddBarLengthEmbellishmentHolder;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_Anticipation;
import main.java.com.dougron.mucus.algorithms.superimposifier.overwritable_vectors.SuFi_SimpleStructureTones;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu015_SimpleToneAndWrappedEmbellishment
{


	String fileName = "Mu015_SimpleToneAndWrappedEmbellishment";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	public Mu015_SimpleToneAndWrappedEmbellishment()
	{
		Mu mu = new Mu("015");
		mu.setLengthInBars(8);
		mu.setStartPitch(56);
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FIVE_EIGHT_32));
		int numberOfAnticipations = 3;
		double lengthOfAnticipations = 0.5;
		SuFi sufi1 = new SuFi_Anticipation(numberOfAnticipations, lengthOfAnticipations);
		SuFi sufi2 = new SuFi_Anticipation(2, 1.0);
		mu.addSuFi(
			new SuFi_SimpleStructureTones(
				-0.1, 
				0.76, 
				new SuFi[] {
					new SuFi_AddBarLengthEmbellishmentHolder(sufi1),
					new SuFi_AddBarLengthEmbellishmentHolder(sufi2)
				}
			)
		);
		
		mu.generate();
//		for (Mu mun: mu.getMusWithNotes())
//		{
//			System.out.println(mun.getPositionInBarsAndBeats() + " globalPositionInQuarters=" + mun.getGlobalPositionInQuarters());
//		}
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu015_SimpleToneAndWrappedEmbellishment();
	}
}
