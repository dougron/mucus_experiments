package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
//@ToString
@Data
public class PhraseLengthRepo extends RepoSuperclass implements RepoInterface {
	
	private double rndValue;
	private int selectedValue;
	private int[] options;
	private String className;
	
	
	
	public String getShortClassName()
	{
		String[] split = className.split(".");
		if (split.length > 0)
		{
			return split[split.length - 1];
		}
		return "className not set";
	}
		
	
	
	public PhraseLengthRepo deepCopy()
	{
		return PhraseLengthRepo.builder()
				.rndValue(rndValue)
				.selectedValue(selectedValue)
				.options(options == null ? null : getCopy(options))
				.className(className)
				.build();
	}
}
