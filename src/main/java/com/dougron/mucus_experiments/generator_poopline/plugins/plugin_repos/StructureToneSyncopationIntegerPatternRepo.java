package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class StructureToneSyncopationIntegerPatternRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private int[] selectedOption;
	@Getter @Setter private int[][] options;
	@Getter @Setter private String className;
	
	
	public StructureToneSyncopationIntegerPatternRepo deepCopy()
	{
		return StructureToneSyncopationIntegerPatternRepo.builder()
				.rndValue(rndValue)
				.selectedOption(selectedOption)
				.options(options == null ? null : getCopy(options))
				.className(className)
				.build();
	}
}
