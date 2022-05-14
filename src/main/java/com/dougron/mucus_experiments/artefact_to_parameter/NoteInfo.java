package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

@Builder
@Data
public class NoteInfo
{
	private double positionInQuarters;
	private double positionInFloatBars;
	private BarsAndBeats positionInBarsAndBeats;
	private int pitch;
	@ToString.Exclude Mu relatedMu;	
}