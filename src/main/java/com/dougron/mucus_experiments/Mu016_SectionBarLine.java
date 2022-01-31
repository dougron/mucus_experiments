package main.java.com.dougron.mucus_experiments;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mucus_output_manager.continuous_integrator.ContinuousIntegrator;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;

public class Mu016_SectionBarLine
{
	
	String fileName = "Mu016_SectionBarLine";
	int trackIndex = 0;
	int clipIndex = 0;
	MuucusLOMInjector injector = new MuucusLOMInjector(7800);
	
	
	public Mu016_SectionBarLine()
	{
		Mu mu = new Mu("parent");
		Mu child = new Mu("child");
		child.setLengthInBars(2);
		child.setHasLeadingDoubleBar(true);
		mu.addMu(child, 2);
		
		ContinuousIntegrator.outputMuToXMLandLive(mu, fileName, trackIndex, clipIndex, injector);
	}

	
	
	public static void main(String[] args)
	{
		new Mu016_SectionBarLine();

	}

}
