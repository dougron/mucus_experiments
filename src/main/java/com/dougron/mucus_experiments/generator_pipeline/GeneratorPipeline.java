package main.java.com.dougron.mucus_experiments.generator_pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Builder;
import lombok.Singular;
import main.java.com.dougron.mucus_experiments.generator_pipeline.plugins.GeneratorPipelinePlugIn;


@Builder
public class GeneratorPipeline {
	
	
	@Singular	private List<GeneratorPipelinePlugIn> plugs = new ArrayList<GeneratorPipelinePlugIn>();
	public static final Logger logger = LogManager.getLogger(GeneratorPipeline.class);


	
	public PipelinePackage getPackage(Random rnd) {
		PipelinePackage p = new PipelinePackage(rnd);
		logger.info("Created: " + p); 
		for (GeneratorPipelinePlugIn plug: plugs) {
			logger.info("Processing " + plug.getClass());
			p = plug.process(p);
		}
		return p;		
	}
}
