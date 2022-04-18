package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class StructureToneSyncopationRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private int[] selectedOption;
	@Getter @Setter private int[][] options;
	@Getter @Setter private String className;
	
	
	public StructureToneSyncopationRepo deepCopy()
	{
		return StructureToneSyncopationRepo.builder()
				.rndValue(rndValue)
				.selectedOption(selectedOption)
				.options(getCopy(options))
				.className(className)
				.build();
	}
}
