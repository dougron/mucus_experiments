package main.java.com.dougron.mucus_experiments;



import main.java.com.dougron.mucus.algorithms.mu_generator.SuFi_AddBarLengthEmbellishmentHolder;
import main.java.com.dougron.mucus.algorithms.mu_generator.SuFi_DiatonicPassingTones;
import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.algorithms.superimposifier.overwritable_vectors.SuFi_SimpleStructureTones;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu019_SimpleToneAndPassingTones
{
	
	
	String fileName = "Mu019_SimpleToneAndPassingTones";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	

	public Mu019_SimpleToneAndPassingTones()
	{
		Mu mu = new Mu("019");
		
		mu.setLengthInBars(8);
		mu.setStartPitch(56);
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FIVE_EIGHT_32));
		double noteLength = 0.5;
		SuFi sufi = new SuFi_DiatonicPassingTones(noteLength, SuFi.AccentType.ACCENTED);
		mu.addSuFi(new SuFi_SimpleStructureTones(-0.1, 0.76, new SuFi_AddBarLengthEmbellishmentHolder(sufi)));
		
		mu.generate();
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu019_SimpleToneAndPassingTones();
	}
}
