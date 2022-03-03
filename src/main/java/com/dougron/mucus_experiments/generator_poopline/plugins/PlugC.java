package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;

public class PlugC extends PlugGeneric implements PooplinePlugin {

	
	int[] options = new int[] {2,3,4,5,6,7,8};
	
	public PlugC() {
		super(
				new Parameter[] {Parameter.PHRASE_LENGTH},
				new Parameter[] {}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		pack = super.process(pack);
		for (Parameter parameter: getRenderParameters()) {
			if (pack.getJson().has(parameter.toString()) 
					&& pack.getJson().get(parameter.toString()).getAsJsonObject().has("random_seed")) {
				// don't do anything to generate new values
			} else {
				JsonObject phraseLength = new JsonObject();
				double rndValue = pack.getRnd().nextDouble();
				int selectedValue = getSelectedValue(rndValue);
				
				phraseLength.add("random_seed", getNewRandomSeedJsonObject(rndValue));
				phraseLength.addProperty("selected_option", selectedValue);
				phraseLength.add("options", getOptionsJsonObject());
				phraseLength.add("plugin", getPluginJsonObject());			
				pack.addItemToJson(parameter.toString(), phraseLength);
				
				pack.getMu().setLengthInBars(selectedValue);
			}
		}
		return pack;
	}


	private int getSelectedValue(double rndValue) {
		int index = (int)(options.length * rndValue);
		return options[index];
	}


	private JsonElement getPluginJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty("class_name", this.getClass().getName());
		return json;
	}


	private JsonElement getOptionsJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty("type", "int_array");
		JsonArray array = new JsonArray();
		for (int i: options) {
			array.add(i);
		}
		json.add("values", array);
		
		return json;
	}
	
	
	public int[] getOptions() {
		return options;
	}
}