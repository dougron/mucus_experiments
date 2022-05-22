package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

class DurationModel_Tests
{

	@Test
	void When_durationModel_is_BarsAndBeats_then_mu_length_is_the_same_as_the_DurationModel()
	{
		DurationModel dm = new DurationInBarsAndBeats(BarsAndBeats.at(1, 2.0));
		Mu mu = new Mu("mu");
		dm.setDuration(mu);
		assertThat(mu.getLengthInBarsAndBeats().getBarPosition()).isEqualTo(1);
		assertThat(mu.getLengthInBarsAndBeats().getOffsetInQuarters()).isEqualTo(2.0);
	}

	
	@Test
	void When_durationModel_is_quarters_then_mu_length_is_the_DurationModel()
	{
		DurationModel dm = new DurationInQuarters(2.0);
		Mu mu = new Mu("mu");
		dm.setDuration(mu);
		assertThat(mu.getLengthInQuarters()).isEqualTo(2.0);
	}
	
	
	@Test
	void When_durationModel_is_legato_then_mu_length_is_the_DurationModel()
	{
		DurationModel dm = new DurationLegato(new DurationInBarsAndBeats(BarsAndBeats.at(1, 0)));
		Mu mu = new Mu("mu");
		Mu mu1 = new Mu("mu1");
		mu1.addMuNote(64, 64);
		Mu mu2 = new Mu("mu2");
		mu2.addMuNote(64, 64);
		mu.addMu(mu1, 1);
		mu.addMu(mu2, 2);
		mu.makePreviousNextMusWithNotes();
		dm.setDuration(mu1);
		assertThat(mu1.getLengthInQuarters()).isEqualTo(4.0);
	}

}
