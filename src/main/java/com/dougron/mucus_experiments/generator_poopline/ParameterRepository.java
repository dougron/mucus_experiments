package main.java.com.dougron.mucus_experiments.generator_poopline;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.RepoInterface;

/*
 * serves as a container for all parameter information for a PooplinePackage
 * 
 * each plugin will have a dedicated type of parameter object that it can adjust 
 * inside this class.
 * 
 * this class and its dependents just save data so it can easily be serialized/desrialized 
 * to/from json, 
 */

public class ParameterRepository extends HashMap<Parameter, RepoInterface>{

	public static final Logger logger = LogManager.getLogger(ParameterRepository.class);
	
	public RepoInterface put(Parameter key, RepoInterface repo)
	{
		String str = "null";
		if (repo != null) str = repo.toString();
		logger.info("ParameterRepository added key=" + key + " value=" + str);
		return super.put(key, repo);
	}
}
