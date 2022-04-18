package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class EvenlySpacedStructureToneRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private double selectedValueInFloatBars;
	@Getter @Setter private double[] options;
	@Getter @Setter private boolean hasRenderedMu;
	@Getter @Setter private String className;
	
	
	public EvenlySpacedStructureToneRepo deepCopy()
	{
		return EvenlySpacedStructureToneRepo.builder()
				.rndValue(rndValue)
				.selectedValueInFloatBars(selectedValueInFloatBars)
				.options(getCopy(options))
				.hasRenderedMu(hasRenderedMu)
				.className(className)
				.build();
	}

}
