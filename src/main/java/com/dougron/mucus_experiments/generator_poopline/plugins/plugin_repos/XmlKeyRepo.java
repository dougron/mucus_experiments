package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class XmlKeyRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double rndValue;
	@Getter @Setter private int selectedValue;
	@Getter @Setter private int[] options;
	@Getter @Setter private String className;
	
	

	public XmlKeyRepo deepCopy()
	{
		return XmlKeyRepo.builder()
				.rndValue(rndValue)
				.selectedValue(selectedValue)
				.options(options == null ? null : getCopy(options))
				.className(className)
				.build();
	}

}
