package main.java.com.dougron.mucus_experiments.generator_poopline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;


@AllArgsConstructor
@ToString
@Builder
public class PooplinePackage {
	
	@Getter @NonNull private String name;
	@Getter @Setter @NonNull private Random rnd;
//	@Getter private List<Parameter> renderedParameters = new ArrayList<Parameter>();
	@Getter private JsonObject json;// = new JsonObject();
	@Getter ParameterRepository repo;
	@Getter @Setter private Mu mu;
	
	// if debugMode is set to true, PooplinePlugins will not require the presence of Poopline as a parent. 
	// This is to accomodate legacy test code, which should probably be updated at some point
	@Getter @Setter private boolean debugMode = false;	
	
	public static final Logger logger = LogManager.getLogger(PooplinePackage.class);
	
	
	public PooplinePackage(@NonNull String name, @NonNull Random rnd) {
		super();
		this.name = name;
		this.rnd = rnd;
		mu = new Mu(name);
		json = new JsonObject();
		repo = new ParameterRepository();
		logger.info("new PooplinePackage created. name=" + name);
	}
	
	
	
//	public void addParameter(Parameter aParameter) {
//		logger.info("Added Parameter " + aParameter + " to " + this);
//		renderedParameters.add(aParameter);
//	}
//	
//	
//	
//	public boolean hasParameter(Parameter aParameter) {
//		return renderedParameters.contains(aParameter);
//	}
	
	
	
//	public void setPrimaryPlug(PooplinePlugin plug){
//		primaryPlug = plug;
//		plugs.add(plug);
//	}
//	
	
	
	public void addItemToJson(String key, JsonElement element) {
		json.add(key, element);
	}
	
	
	
	public void addItemToJson(String key, String string) {
		json.addProperty(key, string);
	}



	public boolean hasParameterInJson(Parameter p) {
		return json.has(p.toString());
	}

	
	
	public boolean hasParameterInRepo(Parameter p) {
		return repo.containsKey(p);
	}



	public List<Parameter> getRenderedParametersFromJson() {
		List<Parameter> list = new ArrayList<Parameter>();
		Iterator<Entry<String, JsonElement>> it = json.entrySet().iterator();
		while (it.hasNext()) {
			String key = it.next().getKey();
			Parameter parameter = Parameter.valueOf(key);
			if (parameter != null) list.add(parameter);
		}
		return list;
	}



	
}
