package main.java.com.dougron.mucus_experiments;

import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionData;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.MucusInteractionDataFactory;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_002;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.MugList_CountIsLess;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.MugList_RhythmLongerOnAverage;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.PhraseEnd_Earlier;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.parameter_specific_feedback.feedback_objects.PhraseLength_Longer;
//import main.java.com.dougron.mucus.mucus_experiments.mucus_kernel.MucusKernel;
import main.java.com.dougron.mucus.mucus_output_manager.LocalOutputManager;
import main.java.com.dougron.mucus.mucus_output_manager.MucusOutputManager;
import main.java.da_utils.render_name.RenderName;



public class Mu040_RenderFromRNDContainer
{
	
	MucusOutputManager outputManager;
	String rndContainerPath = "D:/Documents/miscForBackup/Mucus Output/json/20210715_105428622.rnd_container";
//	MucusKernel kernel = new MucusKernel();
	
	public Mu040_RenderFromRNDContainer ()
	{
		outputManager = LocalOutputManager.getInstance();
		((LocalOutputManager)outputManager).setPath("D:/Documents/miscForBackup/Mu040/");
		Random rnd = new Random();
		
		// load rndContainer
		RMRandomNumberContainer rndContainer = RMRandomNumberContainer.getRndContainerFromJSONFile(rndContainerPath);
//		System.out.println(rndContainer.toString());
		
		// Test that rndContainer is the correct item
//		MucusInteractionData mid1 = MucusInteractionDataFactory.getMid(rndContainer, "Mu040", RMG_002.getInstance(), rnd);
		
		String timeStamp = RenderName.dateAndTime();
		
		// make mid to send for variations
		MucusInteractionData mid = new MucusInteractionData();
		mid.addParameterSpecificFeedbackItem(MugList_CountIsLess.getInstance());
		mid.addParameterSpecificFeedbackItem(MugList_RhythmLongerOnAverage.getInstance());
		mid.addParameterSpecificFeedbackItem(PhraseEnd_Earlier.getInstance());
		mid.addParameterSpecificFeedbackItem(PhraseLength_Longer.getInstance());
		mid.setRndContainer(rndContainer);
		mid.setRandomMelodyGenerator(RMG_002.getInstance());
		
		MucusInteractionData mid2 = MucusInteractionDataFactory.generateUserOption(mid, rnd, 10, 0.15);
		
		outputAllItemsForNewMu(timeStamp, mid2);
		System.out.println("done");
	}
	
	
	private void outputAllItemsForNewMu (String timeStamp,
			MucusInteractionData mid)
	{
		outputManager.outputToPlayback(timeStamp, mid.getMu());
		outputManager.outputToMusicXML(timeStamp, mid.getMu());
		outputManager.outputThinkingFile(timeStamp, mid.getThinkingJSON());
		outputManager.outputRndContainer(timeStamp, mid.getRndContainer());
		outputManager.outputParameterObject(timeStamp, mid.getPo());
		outputManager.outputMuAsXML(timeStamp, mid.getMu());
		outputManager.outputStatVarFile(timeStamp, mid.getRandomMelodyGenerator().makeJSONObject());
	}
	
	
	
	public static void main (String[] args)
	{
		new Mu040_RenderFromRNDContainer();
	}
}
