package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;

public class RandomTempo implements GeneratorPipelinePlugIn {
	
	private int low = 65;
	private int high = 190;
	public static final Logger logger = LogManager.getLogger(RandomTempo.class);


	@Override
	public PipelinePackage process(PipelinePackage aPackage) {
		double rndValue = aPackage.getRnd().nextDouble();
		int tempo = (int)((high - low) * rndValue + low);
		aPackage.getMu().setStartTempo(tempo);
		aPackage.getRndContainer().put("tempo", getJsonRecord(rndValue));
		logger.info("Set mu.startTempo=" + tempo + " rndContainer('tempo')=" + rndValue + " for " + aPackage);
		return aPackage;
	}
	
	
	private JSONObject getJsonRecord(double rndValue) {
		JSONObject json = new JSONObject();
		
		addRandomSeedToJson(rndValue, json);		
		addHighAndLowConstraintsToJson(json);
			
		return json;
	}


	private void addHighAndLowConstraintsToJson(JSONObject json) {
		JSONObject optionList = new JSONObject();
		optionList.put("high", high);
		optionList.put("low", low);
		optionList.put("type", "high and low int value");
		json.put("options", optionList);
	}


	private void addRandomSeedToJson(double rndValue, JSONObject json) {
		JSONObject randomSeed = new JSONObject();
		randomSeed.put("type", "double");
		randomSeed.put("value", rndValue);
		json.put("random_seed", randomSeed);
	}



	@Override
	public boolean hasRequiredResources(PipelinePackage pp) {
		// TODO Auto-generated method stub
		return false;
	}

}
