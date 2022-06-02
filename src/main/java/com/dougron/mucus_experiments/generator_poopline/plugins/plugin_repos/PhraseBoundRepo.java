package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

@Builder
@ToString

public class PhraseBoundRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private double selectedValue;
	@Getter @Setter private double rangeLow;
	@Getter @Setter private double rangeHigh;
	@Getter @Setter private Parameter parameter;
	@Getter @Setter private String className;
	
	
	public PhraseBoundRepo deepCopy()
	{
		return PhraseBoundRepo.builder()
				.rndValue(rndValue)
				.selectedValue(selectedValue)
				.rangeLow(rangeLow)
				.rangeHigh(rangeHigh)
				.parameter(parameter)
				.className(className)
				.build();
	}
	
}
