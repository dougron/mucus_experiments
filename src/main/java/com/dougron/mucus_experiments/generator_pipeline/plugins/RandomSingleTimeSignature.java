package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.SingleTimeSignature;
import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class RandomSingleTimeSignature implements GeneratorPipelinePlugIn {

	TimeSignature[] options = new TimeSignature[] 
			{
					TimeSignature.FIVE_EIGHT_32,
					TimeSignature.SIX_EIGHT,
					TimeSignature.SEVEN_EIGHT_322, 
					TimeSignature.SEVEN_EIGHT_223,
					TimeSignature.NINE_EIGHT_333,
					TimeSignature.TEN_EIGHT_3322,
					TimeSignature.ELEVEN_EIGHT_3332,
					TimeSignature.TWELVE_EIGHT,
					
					TimeSignature.TWO_FOUR,
					TimeSignature.THREE_FOUR,
					TimeSignature.FOUR_FOUR, 
					TimeSignature.TWO_TWO,
					TimeSignature.FIVE_FOUR,
					TimeSignature.SIX_FOUR_33,
					TimeSignature.SIX_FOUR_222,
					TimeSignature.SEVEN_FOUR_322,
					TimeSignature.EIGHT_FOUR,
					TimeSignature.THIRTEEN_FOUR_CLAPHAM,
					
					
			};
	
	public static final Logger logger = LogManager.getLogger(RandomSingleTimeSignature.class);

	
	@Override
	public PipelinePackage process(PipelinePackage aPackage) {
		double rndValue = aPackage.getRnd().nextDouble();
		TimeSignature selection = options[(int)(options.length * rndValue)];
		aPackage.getMu().setTimeSignatureGenerator(new SingleTimeSignature(selection));
//		aPackage.getRndContainer().put("time_signature", rndValue);
		aPackage.getRndContainer().put("time_signature", getJsonRecord(rndValue));
		logger.info("Set mu.TimeSignature=" + selection + " rndContainer('time_signature')=" + rndValue + " for " + aPackage);
		return aPackage;
	}
	
	
	private JSONObject getJsonRecord(double rndValue) {
		JSONObject json = new JSONObject();
		
		addRandomSeedToJson(rndValue, json);		
		addOptionListToJson(json);
		
		return json;
	}


	private void addOptionListToJson(JSONObject json) {
		JSONObject optionList = new JSONObject();
		JSONArray list = makeListOfTimeSignatureOptionsForJson();
		optionList.put("option_list", list);
		optionList.put("type", "preset or serialized TimeSignature");
		json.put("options", optionList);
	}


	private JSONArray makeListOfTimeSignatureOptionsForJson() {
		JSONArray list = new JSONArray();
		for (TimeSignature ts: options) {
			JSONObject tsJson = new JSONObject();
			if (TimeSignature.searchableMap.containsKey(ts.getName())) {
				addPresetTimeSignatureNameToJson(ts, tsJson);
			} else {
				addFullSerializedTimeSignatureToJson(ts, tsJson);
			}
			list.put(tsJson);
		}
		return list;
	}


	private void addFullSerializedTimeSignatureToJson(TimeSignature ts, JSONObject tsJson) {
		tsJson.put("type", "unique");
		tsJson.put("object", ts);
	}


	private void addPresetTimeSignatureNameToJson(TimeSignature ts, JSONObject tsJson) {
		tsJson.put("type", "preset");
		tsJson.put("name", ts.getName());
	}


	private void addRandomSeedToJson(double rndValue, JSONObject json) {
		JSONObject randomSeed = new JSONObject();
		randomSeed.put("type", "double");
		randomSeed.put("value", rndValue);
		json.put("random_seed", randomSeed);
	}

	@Override
	public boolean hasRequiredResources(PipelinePackage pp) {
		if (pp.getRnd() != null) return true;
		return false;
	}

}
