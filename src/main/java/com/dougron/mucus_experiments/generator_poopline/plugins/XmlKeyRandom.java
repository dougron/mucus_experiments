package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;

public class XmlKeyRandom extends PlugGeneric implements PooplinePlugin {
	
	
	public static final Logger logger = LogManager.getLogger(XmlKeyRandom.class);

	
	int[] options = new int[] {-5,-4,-3,-2,-1,0,1,2,3,4,5,6};


	private XmlKeyRepo xmlKeyRepo;
	
	public XmlKeyRandom() {
		super(
				new Parameter[] {Parameter.XMLKEY},
				new Parameter[] {}
				);
	}
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info(getInfoLevelPackReceiptMessage(pack));
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.XMLKEY) 
				&& pack.getRepo().get(Parameter.XMLKEY).getClassName().equals(getClass().getName())) {			
		} else {
			double rndValue = pack.getRnd().nextDouble();
			xmlKeyRepo = XmlKeyRepo.builder()
					.rndValue(rndValue)
					.selectedValue(getSelectedValue(rndValue))
					.options(options)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.XMLKEY, xmlKeyRepo);	
		}
		if (pack.getRepo().containsKey(Parameter.XMLKEY) 
				&& pack.getRepo().get(Parameter.XMLKEY).getClassName().equals(getClass().getName())) {
			if (xmlKeyRepo == null) {
				xmlKeyRepo = (XmlKeyRepo)pack.getRepo().get(Parameter.XMLKEY);
			}
	
			pack.getMu().setXMLKey(xmlKeyRepo.getSelectedValue());
		}
		logger.debug(this.getClass().getSimpleName() + ".process() exited");
		return pack;
	}
	
	
//	@Override
//	public PooplinePackage process (PooplinePackage pack) {
//		logger.info("Received " + pack);
//		pack = super.process(pack);
//		for (Parameter parameter: getRenderParameters()) {
//			if (pack.getJson().has(parameter.toString()) 
//					&& pack.getJson().get(parameter.toString()).getAsJsonObject().has("random_seed")) {
//				// don't do anything to generate new values
//			} else {
//				JsonObject jsonEntry = new JsonObject();
//				double rndValue = pack.getRnd().nextDouble();
//				int selectedValue = getSelectedValue(rndValue);
//				
//				jsonEntry.add("random_seed", getNewRandomSeedJsonObject(rndValue));
//				jsonEntry.addProperty("selected_option", selectedValue);
//				jsonEntry.add("options", getOptionsJsonObject());
//				jsonEntry.add("plugin", getPluginJsonObject());			
//				pack.addItemToJson(parameter.toString(), jsonEntry);
//				
//				pack.getMu().setXMLKey(selectedValue);
//			}
//		}
//		return pack;
//	}


	private int getSelectedValue(double rndValue) {
		int index = (int)(options.length * rndValue);
		return options[index];
	}


//	private JsonElement getPluginJsonObject() {
//		JsonObject json = new JsonObject();
//		json.addProperty("class_name", this.getClass().getName());
//		return json;
//	}
//
//
//	private JsonElement getOptionsJsonObject() {
//		JsonObject json = new JsonObject();
//		json.addProperty("type", "int_array");
//		JsonArray array = new JsonArray();
//		for (int i: options) {
//			array.add(i);
//		}
//		json.add("values", array);
//		
//		return json;
//	}
	
	
	public int[] getOptions() {
		return options;
	}
}