package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString

public class PhraseBoundRepo implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private double selectedValue;
	@Getter @Setter private double rangeLow;
	@Getter @Setter private double rangeHigh;
	@Getter @Setter private String className;
	
}
