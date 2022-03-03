package main.java.com.dougron.mucus_experiments.generator_pipeline.plugins;

import java.util.function.IntPredicate;

import main.java.com.dougron.mucus_experiments.generator_pipeline.PipelinePackage;

public interface GeneratorPipelinePlugIn {

	PipelinePackage process(PipelinePackage aPackage);

	boolean hasRequiredResources(PipelinePackage pp);

}
