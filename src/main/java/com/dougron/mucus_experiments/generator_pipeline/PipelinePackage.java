package main.java.com.dougron.mucus_experiments.generator_pipeline;

import java.util.Random;

import org.json.JSONObject;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;

/*
 * replacement for the MucusInteractionData class in the generator_pipeline 
 * 
 * package gets passed down the generation chain and gets added to on the way
 * 
 */

@RequiredArgsConstructor
@ToString
public class PipelinePackage {

	@NonNull	@Getter 	@Setter	private Random rnd;
	@Getter 	@Setter	private Mu mu = new Mu("PipelinePackage_initial_mu");
	@Getter		@Setter	private JSONObject rndContainer = new JSONObject();
	
	
	public boolean rndContainerHas(String key) {
		if (rndContainer.has(key)) return true;
		return false;
	}
}
