package main.java.com.dougron.mucus_experiments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionData;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.com.dougron.mucus_experiments.mucus_kernel.MucusKernel;
import main.java.da_utils.render_name.RenderName;

public class Mu041_ControllerDemo {
	
	
	private MucusOutputManager outputManager = LocalOutputManager.getInstance();
	private MucusKernel kernel = new MucusKernel();
	public static final Logger logger = LogManager.getLogger(Mu041_ControllerDemo.class);
	
	
	public Mu041_ControllerDemo() {
		String timeStamp = RenderName.dateAndTime();
		logger.info("I'm working: " + timeStamp);
		Mu mu = makeRandomMelodyWithControllers(timeStamp);
		outputManager.outputToPlayback(timeStamp, mu);
	}

	
	private Mu makeRandomMelodyWithControllers(String timeStamp) {
		MucusInteractionData mid = kernel.getInitialBotVariation(timeStamp);
		mid = kernel.addTestControllerToMelody(mid, timeStamp);
		return mid.getMu();
	}

	
	public static void main(String[] args) {
		new Mu041_ControllerDemo();
	}
}
