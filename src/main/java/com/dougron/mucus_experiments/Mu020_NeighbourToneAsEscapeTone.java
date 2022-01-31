package main.java.com.dougron.mucus_experiments;


import main.java.com.dougron.mucus.algorithms.superimposifier.SuFi;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_AddBarLengthEmbellishmentHolder;
import main.java.com.dougron.mucus.algorithms.superimposifier.not_quite_sufis.SuFi_EscapeTone;
import main.java.com.dougron.mucus.algorithms.superimposifier.overwritable_vectors.SuFi_RepeatedScaleTones;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class Mu020_NeighbourToneAsEscapeTone
{
	
	
	String fileName = "Mu020_NeighbourToneAsEscapeTone";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800); 
	

	public Mu020_NeighbourToneAsEscapeTone()
	{
		Mu mu = new Mu("020");
		
		mu.setLengthInBars(8);
		mu.setStartPitch(56);
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FIVE_EIGHT_32));
		SuFi sufi = new SuFi_EscapeTone(1.0, SuFi_EscapeTone.EscapeToneType.STEP_JUMP, SuFi.AccentType.UNACCENTED);
		SuFi sufi2 = new SuFi_EscapeTone(1.0, SuFi_EscapeTone.EscapeToneType.STEP_JUMP, SuFi.AccentType.UNACCENTED, SuFi_EscapeTone.NeighbourToneType.LOWER_NEIGHBOUR);
		mu.addSuFi(
			new SuFi_RepeatedScaleTones(
				-0.1, 
				0.76, 
				new SuFi[]{
					new SuFi_AddBarLengthEmbellishmentHolder(sufi),
					new SuFi_AddBarLengthEmbellishmentHolder(sufi2)
				}
			)
		);
		
		mu.generate();
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu020_NeighbourToneAsEscapeTone();
	}
}
