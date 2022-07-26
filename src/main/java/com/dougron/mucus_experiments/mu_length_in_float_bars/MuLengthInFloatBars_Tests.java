package main.java.com.dougron.mucus_experiments.mu_length_in_float_bars;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

class MuLengthInFloatBars_Tests
{

	@Test
	void when_length_in_bars_is_1_then_length_in_float_bars_is_1_as_double()
	{
		Mu mu = new Mu("x");
		mu.setLengthInBars(1);
		assertThat(MuLengthInFloatBars.getLengthInFloatBars(mu)).isEqualTo(1.0);
	}
	
	
	
	@Test
	void when_length_is_1_bar_and_2_beats_in_4_4_time_then_length_in_float_bars_is_1pnt5() throws Exception
	{
		Mu mu = new Mu("x");
		mu.setLengthInBarsAndBeats(BarsAndBeats.at(1, 2.0));
		assertThat(MuLengthInFloatBars.getLengthInFloatBars(mu)).isEqualTo(1.5);
	}
	
	
	@Test
	void when_length_is_2_quarters_within_a_bar_then_length_in_float_bars_is_0_5() throws Exception
	{
		Mu mu = new Mu("x");
		mu.setLengthInBarsAndBeats(BarsAndBeats.at(0, 2.0));
		assertThat(MuLengthInFloatBars.getLengthInFloatBars(mu)).isEqualTo(0.5);
		
	}

	
	@Test
	void when_length_is_2_quarters_across_a_bar_line_then_length_in_float_bars_is_above_1_0() throws Exception
	{
		Mu parent = new Mu("parent");
		Mu mu = new Mu("x");
		mu.setLengthInQuarters(2.0);
		parent.addMu(mu, BarsAndBeats.at(0, 3.0));
		assertThat(MuLengthInFloatBars.getLengthInFloatBars(mu)).isEqualTo(1.25);
		
	}

}
