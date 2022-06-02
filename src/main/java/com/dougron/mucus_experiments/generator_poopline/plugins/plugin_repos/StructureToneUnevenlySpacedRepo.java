package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class StructureToneUnevenlySpacedRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private double[] selectedValuesInFloatBars;
	@Getter @Setter private double[] options;
	@Getter @Setter private boolean hasRenderedMu;
	@Getter @Setter private String className;
	
	
	public StructureToneUnevenlySpacedRepo deepCopy()
	{
		return StructureToneUnevenlySpacedRepo.builder()
				.rndValue(rndValue)
				.selectedValuesInFloatBars(selectedValuesInFloatBars)
				.options(options == null ? null : getCopy(options))
				.hasRenderedMu(hasRenderedMu)
				.className(className)
				.build();
	}

}
