package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

@Builder
@ToString
public class PlaceHolderRepo extends RepoSuperclass  implements RepoInterface {
	
	@Getter @Setter private Parameter parameter;
	@Getter @Setter private String className;
	

	
	public PlaceHolderRepo deepCopy()
	{
		return PlaceHolderRepo.builder()
				.parameter(parameter)
				.className(className)
				.build();
	}
}
