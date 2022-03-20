package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class TempoRepo implements RepoInterface{

	
	@Getter @Setter private double rndValue;
	@Getter @Setter private int selectedTempo;
	@Getter @Setter private int highLimit;
	@Getter @Setter private int lowLimit;
	@Getter @Setter private String className;
}
