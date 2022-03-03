package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;

public class RandomXMLKey implements GeneratorPipelinePlugIn {
	
	
	private int[] keyOptions = new int[] {-5,-4,-3,-2,-1,0,1,2,3,4,5,6};
	public static final Logger logger = LogManager.getLogger(RandomXMLKey.class);

	
	@Override
	public PipelinePackage process(PipelinePackage aPackage) {
		double rndValue = aPackage.getRnd().nextDouble();
		int selection = keyOptions[(int)(keyOptions.length * rndValue)];
		aPackage.getMu().setXMLKey(selection);
		aPackage.getRndContainer().put("xml_key", getJsonRecord(rndValue));
		logger.info("Set mu.xmlKey=" + selection + " rndContainer('xml_key')=" + rndValue + " for " + aPackage);
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
		optionList.put("option_list", keyOptions);
		optionList.put("type", "int");
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
		return true;
	}

}
