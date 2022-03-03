package main.java.com.dougron.mucus_experiments.loose_pipeline;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;

@ToString
public class LoosePackage {
	
	@Getter
	private Mu mu;
	@Getter @Setter
	private JsonObject json;
	
	
	public LoosePackage(String name) {
		mu = new Mu(name);
		json.addProperty("package_name", name);
	}
	
	
	public void addItemToJson(String key, JsonElement element)
	{
		json.add(key, element);
	}

}
