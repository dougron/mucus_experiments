package main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.Mu;

class RhythmOffset_Tests {

	@Test
	void test() {
		RhythmOffset ro = new RhythmOffset(0,0,0,0,0);
		assertThat(ro).isNotNull();
	}
	
	
	@Test
	void given_mu_is_parent_when_argument_is_0_0_0_0_0_then_rhythmOffset_returns_position_of_0() throws Exception {
		RhythmOffset ro = new RhythmOffset(0,0,0,0,0);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(0);
	}
	
	
	@Test
	void given_mu_is_parent_with_4_4_timesignature_when_argument_is_1_0_0_0_0_then_rhythmOffset_returns_position_of_4_0() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(1,0,0,0,0);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(4.0);
	}
	
	
	@Test
	void given_mu_is_parent_with_4_4_timesignature_when_argument_is_0_1_0_0_0_then_rhythmOffset_returns_position_of_2_0() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,1,0,0,0);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(2.0);
	}
	
	
	@Test
	void given_mu_is_parent_with_4_4_timesignature_when_argument_is_0_0_1_0_0_then_rhythmOffset_returns_position_of_1_0() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,1,0,0);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(1.0);
	}
	
	
	@Test
	void given_mu_is_parent_with_4_4_timesignature_when_argument_is_0_0_0_1_0_then_rhythmOffset_returns_position_of_0_5() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,0,1,0);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(0.5);
	}
	
	
	@Test
	void given_mu_is_parent_with_4_4_timesignature_when_argument_is_0_0_0_0_1_then_rhythmOffset_returns_position_of_0_25() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,0,0,1);
		Mu mu = new Mu("mu");
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(0.25);
	}
	
	
	@Test
	void given_mu_is_not_parent_with_4_4_timesignature_and_position_1_bar_when_argument_is_minus1_0_0_0_0_then_rhythmOffset_returns_position_of_minus4() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(-1,0,0,0,0);
		Mu parent = new Mu("parent");
		Mu mu = new Mu("mu");
		parent.addMu(mu, 1);
		assertThat(mu.getPositionInQuarters()).isEqualTo(4.0);
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(-4.0);	// offset from mu, not global position
	}
	
	
	@Test
	void given_mu_is_not_parent_with_4_4_timesignature_and_position_1_bar_when_argument_is_0_minus1_0_0_0_then_rhythmOffset_returns_position_of_minus2() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,-1,0,0,0);
		Mu parent = new Mu("parent");
		Mu mu = new Mu("mu");
		parent.addMu(mu, 1);
		assertThat(mu.getPositionInQuarters()).isEqualTo(4.0);
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(-2.0);	// offset from mu, not global position
	}
	
	
	@Test
	void given_mu_is_not_parent_with_4_4_timesignature_and_position_1_bar_when_argument_is_0_0_minus1_0_0_then_rhythmOffset_returns_position_of_minus1() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,-1,0,0);
		Mu parent = new Mu("parent");
		Mu mu = new Mu("mu");
		parent.addMu(mu, 1);
		assertThat(mu.getPositionInQuarters()).isEqualTo(4.0);
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(-1.0);	// offset from mu, not global position
	}
	
	
	@Test
	void given_mu_is_not_parent_with_4_4_timesignature_and_position_1_bar_when_argument_is_0_0_0_minus1_0_then_rhythmOffset_returns_position_of_minus0_5() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,0,-1,0);
		Mu parent = new Mu("parent");
		Mu mu = new Mu("mu");
		parent.addMu(mu, 1);
		assertThat(mu.getPositionInQuarters()).isEqualTo(4.0);
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(-0.5);	// offset from mu, not global position
	}
	
	
	@Test
	void given_mu_is_not_parent_with_4_4_timesignature_and_position_1_bar_when_argument_is_0_0_0_0_minus1_then_rhythmOffset_returns_position_of_minus0_25() throws Exception {
		// 4/4 is default.....
		RhythmOffset ro = new RhythmOffset(0,0,0,0,-1);
		Mu parent = new Mu("parent");
		Mu mu = new Mu("mu");
		parent.addMu(mu, 1);
		assertThat(mu.getPositionInQuarters()).isEqualTo(4.0);
		assertThat(ro.getOffsetInQuarters(mu)).isEqualTo(-0.25);	// offset from mu, not global position
	}

}
