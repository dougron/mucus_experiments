package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

@Builder
@ToString
public class TimeSignatureRepo implements RepoInterface{
	
	@Getter @Setter private double rndValue;
	@Getter @Setter private TimeSignature selectedValue;
	@Getter @Setter private TimeSignature[] options;
	@Getter @Setter private String className;
	

}
