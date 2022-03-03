package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;

/*
 * generic phrase length option generator in range 2 - 12 bars
 */

public class RandomPhraseLengthInBars implements GeneratorPipelinePlugIn {

	
	private int[] lengthOptions = new int[] {2,3,4,5,6,7,8,9,10,11,12};
	public static final Logger logger = LogManager.getLogger(RandomPhraseLengthInBars.class);

	
	
	@Override
	public PipelinePackage process(PipelinePackage aPackage) {
		double rndValue = aPackage.getRnd().nextDouble();
		int selection = lengthOptions[(int)(lengthOptions.length * rndValue)];
		aPackage.getMu().setLengthInBars(selection);
		aPackage.getRndContainer().put("phrase_length", getJsonRecord(rndValue));
		logger.info("Set mu.lengthInBars=" + selection + " rndContainer('phrase_length')=" + rndValue + " for " + aPackage);
		return aPackage;
	}

	
	private JSONObject getJsonRecord(double rndValue) {
		JSONObject json = new JSONObject();
		
		addRandomSeedToJson(rndValue, json);		
		addListOfOptionsToJson(json);
			
		return json;
	}


	private void addListOfOptionsToJson(JSONObject json) {
		JSONObject optionList = new JSONObject();
		optionList.put("option_list", lengthOptions);
		optionList.put("type", "int");
		json.put("options", optionList);
	}


	private void addRandomSeedToJson(double rndValue, JSONObject json) {
		JSONObject randomSeed = new JSONObject();
		randomSeed.put("type", "double");
		randomSeed.put("value", rndValue);
		json.put("random_seed", randomSeed);
	}


	/*
	 * this method is for testing whether process() will fail or not, for situations where 
	 * process routes can branch. Might be a case of me over-engineering again
	 */
	@Override
	public boolean hasRequiredResources(PipelinePackage pp) {
		if (pp.getRnd() != null) return true;
		return false;
	}
}
