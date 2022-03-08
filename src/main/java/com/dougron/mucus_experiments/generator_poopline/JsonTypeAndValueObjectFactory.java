package main.java.com.dougron.mucus_experiments.generator_poopline;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;

public class JsonTypeAndValueObjectFactory {

	
	public static JsonObject getJsonObject(double[] doubleArray) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "double_array");
		JsonArray array = new Gson().toJsonTree(doubleArray).getAsJsonArray();
		json.add("values", array);
		return json;
	}
	
	
	public static JsonObject getJsonObject(String[] stringArray) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "string_array");
		JsonArray array = new Gson().toJsonTree(stringArray).getAsJsonArray();
		json.add("values", array);
		return json;
	}


	public static JsonElement getPluginJsonObject(PooplinePlugin plugin) {
		JsonObject json = new JsonObject();
		json.addProperty("class_name", plugin.getClass().getName());
		return json;
	}
}
