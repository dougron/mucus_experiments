package main.java.com.dougron.mucus_experiments.generator_poopline.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.SingleTimeSignature;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;



public class TimeSignatureSingleRandom extends PlugGeneric {
	
	public static final Logger logger = LogManager.getLogger(TimeSignatureSingleRandom.class);
	
	TimeSignature[] options = new TimeSignature[] 
			{
					TimeSignature.FIVE_EIGHT_32,
					TimeSignature.SIX_EIGHT,
					TimeSignature.SEVEN_EIGHT_322, 
					TimeSignature.SEVEN_EIGHT_223,
					TimeSignature.NINE_EIGHT_333,
					TimeSignature.TEN_EIGHT_3322,
					TimeSignature.ELEVEN_EIGHT_3332,
					TimeSignature.TWELVE_EIGHT,
					
					TimeSignature.TWO_FOUR,
					TimeSignature.THREE_FOUR,
					TimeSignature.FOUR_FOUR, 
					TimeSignature.TWO_TWO,
					TimeSignature.FIVE_FOUR,
					TimeSignature.SIX_FOUR_33,
					TimeSignature.SIX_FOUR_222,
					TimeSignature.SEVEN_FOUR_322,
					TimeSignature.EIGHT_FOUR,
					TimeSignature.THIRTEEN_FOUR_CLAPHAM,
										
			};
	
	private TimeSignatureRepo timeSignatureRepo;

	public TimeSignatureSingleRandom() {
		super(
				new Parameter[] {Parameter.TIME_SIGNATURE},
				new Parameter[] {}
				);
	}
	
	
	
	@Override
	public PooplinePackage process (PooplinePackage pack) {
		logger.info("Received " + pack);
		pack = super.process(pack);
		if (pack.getRepo().containsKey(Parameter.TIME_SIGNATURE) 
				&& pack.getRepo().get(Parameter.TIME_SIGNATURE).getClassName().equals(getClass().getName())) {			
		} else {
			double rndValue = pack.getRnd().nextDouble();
			timeSignatureRepo = TimeSignatureRepo.builder()
					.rndValue(rndValue)
					.selectedValue(getSelectedValue(rndValue))
					.options(options)
					.className(getClass().getName())
					.build();
			pack.getRepo().put(Parameter.TIME_SIGNATURE, timeSignatureRepo);	
		}
		if (pack.getRepo().containsKey(Parameter.TIME_SIGNATURE) 
				&& pack.getRepo().get(Parameter.TIME_SIGNATURE).getClassName().equals(getClass().getName())) {
			if (timeSignatureRepo == null) {
				timeSignatureRepo = (TimeSignatureRepo)pack.getRepo().get(Parameter.TIME_SIGNATURE);
			}
	
			pack.getMu().setTimeSignatureGenerator(new SingleTimeSignature(timeSignatureRepo.getSelectedValue()));
		}
		return pack;
	}
	
	
	private TimeSignature getSelectedValue(double rndValue) {
		int index = (int)(options.length * rndValue);
		return options[index];
	}


	public TimeSignature[] getOptions() {
		return options;
	}
}
