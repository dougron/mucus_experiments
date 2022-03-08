package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class TessituraRepo  implements RepoInterface {
	
	@Getter @Setter private int highValue;
	@Getter @Setter private int lowValue;
	@Getter @Setter private String className;
	

}
