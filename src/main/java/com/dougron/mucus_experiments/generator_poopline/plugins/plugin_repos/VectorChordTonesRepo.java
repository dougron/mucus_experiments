package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class VectorChordTonesRepo extends RepoSuperclass implements RepoInterface
{
	@Getter @Setter private int[] selectedVectorArray;
	@Getter @Setter private String className;
	
	
	
	public VectorChordTonesRepo deepCopy()
	{
		return VectorChordTonesRepo.builder()
				.selectedVectorArray(selectedVectorArray == null ? null : getCopy(selectedVectorArray))
				.className(className)
				.build();
	}
}
