package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

@Builder
@ToString
public class TessituraRepo extends RepoSuperclass  implements RepoInterface {
	
	@Getter @Setter private int highValue;
	@Getter @Setter private int lowValue;
	@Getter @Setter private Parameter parameter;
	@Getter @Setter private String className;
	

	
	public TessituraRepo deepCopy()
	{
		return TessituraRepo.builder()
				.highValue(highValue)
				.lowValue(lowValue)
				.className(className)
				.build();
	}
}
