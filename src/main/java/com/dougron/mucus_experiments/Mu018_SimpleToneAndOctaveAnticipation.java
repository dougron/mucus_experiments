package main.java.com.dougron.mucus_experiments;


import main.java.com.dougron.mucus.algorithms.mu_generator.SuFi_OctaveAnticipation;
import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_AddBarLengthEmbellishmentHolder;
import main.java.com.dougron.mucus.algorithms.superimposifier.overwritable_vectors.SuFi_SimpleStructureTones;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu018_SimpleToneAndOctaveAnticipation
{
	
	
	String fileName = "Mu018_SimpleToneAndOctaveAnticipation";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	

	public Mu018_SimpleToneAndOctaveAnticipation()
	{
		Mu mu = new Mu("018");
		mu.setLengthInBars(8);
		mu.setStartPitch(56);
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FIVE_EIGHT_32));
		int noteCount = 2;
		double noteLength = 0.5;
		SuFi sufi = new SuFi_OctaveAnticipation(noteCount, noteLength);
		mu.addSuFi(new SuFi_SimpleStructureTones(-0.1, 0.76, new SuFi_AddBarLengthEmbellishmentHolder(sufi)));
//		mu.addSuFi(new SuFi_SimpleStructureTones(0.1, 0.76));
		
		mu.generate();
//		for (Mu mun: mu.getMusWithNotes())
//		{
//			System.out.println(mun.getPositionInBarsAndBeats() + " globalPositionInQuarters=" + mun.getGlobalPositionInQuarters());
//		}
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu018_SimpleToneAndOctaveAnticipation();
	}
}
