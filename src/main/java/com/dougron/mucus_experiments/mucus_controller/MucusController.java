package main.java.com.dougron.mucus_experiments.mucus_controller;

import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.mucus_kernel.MucusKernel;

public class MucusController
{

	private MucusOutputManager outputManager;
	private MucusKernel kernel;
	

	public MucusController (MucusOutputManager aMucusOutputManager)
	{
		outputManager = aMucusOutputManager;
		kernel = new MucusKernel();
	}



	public MucusOutputManager getOutputManager ()
	{
		return outputManager;
	}



	public MucusKernel getKernel ()
	{
		return kernel;
	}
}
