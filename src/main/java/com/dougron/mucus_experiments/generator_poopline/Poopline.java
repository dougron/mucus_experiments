package main.java.com.dougron.mucus_experiments.generator_poopline;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.shade.guava.collect.Lists;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

public class Poopline 
{

	// plugins should be processed in reverse order if not using the primaryPlugin processing route
	// so that later added plugins will be processed first, and earlier plugins can also behave as
	// 'fallback' or default options
	@Getter private List<PooplinePlugin> plugins = new ArrayList<PooplinePlugin>();
	@Getter PooplinePlugin primaryPlugin;
	private boolean isValidated;
	public static final Logger logger = LogManager.getLogger(Poopline.class);
	
	
	
	public Poopline() 
	{
		logger.info("Poopline created.");
	}
	
	
	
	public void addPlugin(PooplinePlugin aPlugin) 
	{
		aPlugin.setParent(this);
		plugins.add(aPlugin);
		logger.info("Received " + aPlugin);
		isValidated = validate();
	}

	
	
	private boolean validate() 
	{
		logger.info("Validating required parameters for plugins");
		boolean valid = true;
		for (PooplinePlugin plug: plugins) 
		{
			if (!plug.isValidated()) 
			{
				valid = false;
			}
		}
		logger.info("Validation " + valid);
		return valid;
	}
	
	
	
	public boolean hasPluginThatCanSupply(Parameter aParameter) 
	{
		boolean q = false;
		List<PooplinePlugin> reverseList = Lists.reverse(plugins);	
		for (PooplinePlugin plug: reverseList) 
		{
			if (plug.getRenderParameter() == aParameter)
			{
				logger.debug(plug.getClass() + " can supply Parameter " + aParameter);
				q = true;
			}
		}
		if (!q) 
		{
			logger.debug("No plugin can supply " + aParameter);
		}
		return q;
	}

	
	
	public boolean isValidated()
	{
		return isValidated;
	}


	
	public PooplinePlugin getPluginThatSolves(Parameter aParameter)
	{
		for (int i = plugins.size() - 1; i >= 0; i--)
		{
			PooplinePlugin plug = plugins.get(i);
			if (plug.getRenderParameter() == aParameter) 
			{
				return plug;
			}
		}
		return null;
	}


	
	public PooplinePackage process(PooplinePackage pack) 
	{
		logger.info("Poopline.process() begins. pack=" + pack.getName());
		loadAnyMissingPluginsThatExistInTheJsonButDoNotHaveRandomSeed(pack);
		setAllPluginsToNotExecuted();
		if (primaryPlugin == null) 
		{
			logger.info("primaryPlugin=null: processing in reverse order. Actually no processing code is present, you shoulf probably think about changing this:D"); 
		} 
		else 
		{
			pack = primaryPlugin.process(pack);
		}
		logger.info("process() completed");
		return pack;
	}
	
	
	
	private void setAllPluginsToNotExecuted()
	{
		for (PooplinePlugin plug: plugins) plug.setExecutedThisCycle(false);
	}

	

	private void loadAnyMissingPluginsThatExistInTheJsonButDoNotHaveRandomSeed(PooplinePackage pack) 
	{
		Iterator<Entry<String, JsonElement>> it = pack.getJson().entrySet().iterator();
		while (it.hasNext()) 
		{
			Entry<String, JsonElement> entry = it.next();
			checkIfItIsAParameter(pack, entry.getKey());			
		}
	}


	private void checkIfItIsAParameter(PooplinePackage pack, String key) 
	{
		if (Parameter.valueOf(key) != null) 
		{
			checkIfItHasAPluginClassNameAndNoRandomSeed(pack, key);
		}
	}


	private void checkIfItHasAPluginClassNameAndNoRandomSeed(PooplinePackage pack, String key) 
	{
		JsonObject json = pack.getJson().get(key).getAsJsonObject();
		if (json.has("plugin") && !json.has("random_seed")) 
		{
			addToPluginsIfAnInstanceIsNotAlreadyThere(json);
		}
	}


	private void addToPluginsIfAnInstanceIsNotAlreadyThere(JsonObject json) 
	{
		String className = json.get("plugin").getAsJsonObject().get("class_name").getAsString();
		if (!plugsinsContainsInstanceOfClassName(className)) 
		{
			try 
			{
				plugins.add((PooplinePlugin)Class.forName(className).getConstructors()[0].newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	
	private boolean plugsinsContainsInstanceOfClassName(String className) 
	{
		for (PooplinePlugin plug: plugins) 
		{
			if (plug.getClass().toString().equals(className)) return true;
		}
		return false;
	}


	public void setPrimaryPlugin(PooplinePlugin aPlugin)
	{
		plugins.add(aPlugin);
		aPlugin.setParent(this);
		primaryPlugin = aPlugin;
	}
}
