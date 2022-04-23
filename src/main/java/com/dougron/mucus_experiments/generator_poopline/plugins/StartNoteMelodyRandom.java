package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

public class StartNoteMelodyRandom extends PlugGeneric implements PooplinePlugin {

	public static final Logger logger = LogManager.getLogger(StartNoteMelodyRandom.class);
	
	private TessituraRepo tessituraRepo;
	private StartNoteRepo startNoteRepo;


	public StartNoteMelodyRandom() {
		super(
				Parameter.START_NOTE,
				new Parameter[] {Parameter.TESSITURA_START_NOTE}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) 
	{
		pack = super.process(pack);
		return pack;
	}



	@Override
	PooplinePackage makeRepo(PooplinePackage pack)
	{
		double rndValue = pack.getRnd().nextDouble();
		int startNote = (int)(rndValue * 
				(tessituraRepo.getHighValue() - tessituraRepo.getLowValue()))
				+ tessituraRepo.getLowValue();
		startNoteRepo = StartNoteRepo.builder()
				.rndValue(rndValue)
				.selectedValue(startNote)
				.rangeLow(tessituraRepo.getLowValue())
				.rangeHigh(tessituraRepo.getHighValue())
				.className(getClass().getName())
				.build();
		pack.getRepo().put(Parameter.START_NOTE, startNoteRepo);
		return pack;
	}
	
	
	
	@Override
	void getAncilliaryRepos(PooplinePackage pack)
	{
		tessituraRepo = (TessituraRepo)pack.getRepo().get(Parameter.TESSITURA_START_NOTE);
	}
	
	
}
