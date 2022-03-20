package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;

@Builder
@ToString
public class DurationPatternRepo implements RepoInterface {
	

	@Getter @Setter private DurationType[] durationPattern;
	@Getter @Setter private int staccatoDurationInMilliseconds;
	@Getter @Setter private String className;

}
