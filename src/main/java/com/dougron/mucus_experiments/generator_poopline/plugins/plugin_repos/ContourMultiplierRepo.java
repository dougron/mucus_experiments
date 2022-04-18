package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Builder
@ToString
public class ContourMultiplierRepo extends RepoSuperclass  implements RepoInterface {
	
	
	@Getter @Setter private double rndValue;
	@Getter @Setter int multiplier;
	@Getter @Setter private int highValue;
	@Getter @Setter private int lowValue;
	@Getter @Setter private String className;
	

	public ContourMultiplierRepo deepCopy()
	{
		return ContourMultiplierRepo.builder()
				.rndValue(rndValue)
				.multiplier(multiplier)
				.highValue(highValue)
				.lowValue(lowValue)
				.className(className)
				.build();
	}
}