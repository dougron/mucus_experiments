package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class LoopModelRepo extends RepoSuperclass implements RepoInterface {

public enum LoopModel {LOOP, CONTINUOUS}
	
	@Getter @Setter private LoopModel selectedLoopModel;
	@Getter @Setter private String className;
	
	
	
	public LoopModelRepo deepCopy()
	{
		return LoopModelRepo.builder()
				.selectedLoopModel(selectedLoopModel)
				.className(className)
				.build();
	}

}
