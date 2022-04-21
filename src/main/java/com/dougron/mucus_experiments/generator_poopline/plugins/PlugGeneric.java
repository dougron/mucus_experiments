package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;


@ToString
@RequiredArgsConstructor
public class PlugGeneric implements PooplinePlugin {


	@Getter @Setter Poopline parent;
	@NonNull @Getter private Parameter[] renderParameters;// = new Parameter[] {Parameter.CHORD_LIST_GENERATOR};
	@NonNull @Setter @Getter private Parameter[] requiredParameters;// = new Parameter[] {Parameter.PHRASE_LENGTH};
	public static final Logger logger = LogManager.getLogger(PlugGeneric.class);

	
	
	
	
	@Override
	public PooplinePackage process(PooplinePackage aPackage) {
		String muMess = getMusAndMuNotesDebugMessage(aPackage);
		logger.debug(renderParameters[0].name() + " pack.mu.mus=" + muMess);
		boolean okay = checkForRequiredParameters(aPackage);
		
		if (parent != null) 
		{
			if (!okay) 
			{
				String str = "Plugin with no renderParemeters ";
				if (renderParameters.length > 0)
				{
					str = renderParameters[0].name();
				}
				logger.info(str + " cannot process required plugins");
			}
			else
			{
				aPackage = processPluginsForRequiredParametersFromParent(aPackage);
			}
		}
		return aPackage;
		// if the required parameters cannot be completed, then process() will fail in the 
		// class extending this
	}



	private String getMusAndMuNotesDebugMessage(PooplinePackage aPackage)
	{
		StringBuilder sb = new StringBuilder();
		List<Mu> mus = aPackage.getMu().getMus();
		if (mus == null)
		{
			sb.append("mus are null");
		}
		else
		{
			int sizz = mus.size();
			sb.append("size=" + sizz);
			if (sizz > 0)
			{
				Mu firstMu = mus.get(0);
				List<MuNote> muNotes = firstMu.getMuNotes();
				if (muNotes == null)
				{
					sb.append(" muNotes is null"); 
				}
				else
				{
					sb.append(" muNotes.size()=" + muNotes.size());
				}
			}
			
		}
		String muMess = sb.toString();
		return muMess;
	}



	private PooplinePackage processPluginsForRequiredParametersFromParent(PooplinePackage aPackage) {
		Set<PooplinePlugin> set = new LinkedHashSet<PooplinePlugin>();
		for (Parameter parameter: requiredParameters) {
			PooplinePlugin plug = parent.getPluginThatSolves(parameter);
			if (plug != null) set.add(plug);
		}
		for (PooplinePlugin plug: set) {
			aPackage = plug.process(aPackage);
		}
		return aPackage;
	}



//	private void addRenderedParameters(PooplinePackage aPackage) {
//		for (Parameter p: renderedParameters) {
//			aPackage.addParameter(p);
//		}
//	}



	protected boolean checkForRequiredParameters(PooplinePackage aPackage) {
		boolean okay = true;
		for (Parameter p: requiredParameters) {
			if (!aPackage.hasParameterInRepo(p)) {
				okay = false;
				logger.info("pack.repo does not have required parameter " + p);
			}
		}
		return okay;
	}



	@Override
	public boolean isValidated() {
		if (requiredParameters.length == 0) {
			return true;
		} 
		if (parent == null) {
			return false;
		} else {
			boolean b = true;
			for (Parameter parameter: requiredParameters) {
				if (!parent.hasPluginThatCanSupply(parameter)) {
					b = false;
				}
			}
			return b;
		}
	}



	@Override
	public boolean canSupplyParameter(Parameter aParameter) {
		for (Parameter p: renderParameters) {
			if (p == aParameter) {
				return true;
			}
		}
		return false;
	}
	
	
	public JsonObject getNewRandomSeedJsonObject(double rndValue) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "double");
		json.addProperty("value", rndValue);
		return json;
	}
	
	
	public JsonObject getNewRandomSeedJsonObject(double[] rndValueArray) {
		JsonObject json = new JsonObject();
		json.addProperty("type", "double_array");
		JsonArray jsonArray = new Gson().toJsonTree(rndValueArray).getAsJsonArray();
		json.add("values", jsonArray);
		return json;
	}
	
	
	public String getInfoLevelPackReceiptMessage(PooplinePackage pack)
	{
		return "Received " 
				+ pack.getName() 
				+ " " 
				+ pack.getRepo().size() 
				+ ". items in repo. mu=" 
				+ pack.getMu().getName() 
				+ ". lengthInBars=" 
				+ pack.getMu().getLengthInBars();
	}
	
	
	public Parameter getPrimaryRenderParameter()
	{
		if (renderParameters.length == 0)
		{
			return null;
		}
		else
		{
			return renderParameters[0];
		}
	}
	

}
