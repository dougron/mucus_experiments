package test.java.com.dougron.mucus_experiments;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.completed.Mu028_AnticipationOfEscape;

class Mu028_Test
{

	@Test
	final void flip_boolean_really_does_flip_a_boolean()
	{
		assertTrue(Mu028_AnticipationOfEscape.flipBoolean(false));
		assertFalse(Mu028_AnticipationOfEscape.flipBoolean(true));
	}
	
	@Test
	void given_please_attitude_added_escape_tone_only_anticipation_is_index_zero_then_returns_true() throws Exception
	{
		List<MuTag> list = Arrays.asList(
					MuTag.IS_ANTICIPATION,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE
				);
		boolean please = true;
		MuTag addedTag = MuTag.IS_ESCAPE_TONE;
		assertTrue(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
	}
	
	
	@Test
	void given_provoke_attitude_added_anticipation_only_anticipation_is_index_zero_then_returns_true() throws Exception
	{
		List<MuTag> list = Arrays.asList(
					MuTag.IS_ANTICIPATION,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE,
					MuTag.IS_ESCAPE_TONE
				);
		boolean please = false;
		MuTag addedTag = MuTag.IS_ANTICIPATION;
		assertTrue(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
	}
	
	
	@Test
	void given_please_attitude_added_anticipation_only_anticipation_is_index_zero_then_returns_false() throws Exception
	{
		List<MuTag> list = Arrays.asList(
				MuTag.IS_ANTICIPATION,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE
			);
		boolean please = true;
		MuTag addedTag = MuTag.IS_ANTICIPATION;
		assertFalse(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
	}
	
	
	@Test
	void given_provoke_attitude_added_escape_tone_only_anticipation_is_index_zero_then_returns_false() throws Exception
	{
		List<MuTag> list = Arrays.asList(
				MuTag.IS_ANTICIPATION,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE,
				MuTag.IS_ESCAPE_TONE
			);
		boolean please = false;
		MuTag addedTag = MuTag.IS_ESCAPE_TONE;
		assertFalse(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
	}
	
	
//	@Test
//	void given_please_attitude_added_escape_tone_more_than_one_anticipation_then_returns_false() throws Exception
//	{
//		List<MuTag> list = Arrays.asList(
//				MuTag.IS_ANTICIPATION,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE
//			);
//		boolean please = true;
//		MuTag addedTag = MuTag.IS_ESCAPE_TONE;
//		assertFalse(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
//	}
	
	
//	@Test
//	void given_provoke_attitude_added_anticipation_more_than_one_anticipation_then_returns_false() throws Exception
//	{
//		List<MuTag> list = Arrays.asList(
//				MuTag.IS_ANTICIPATION,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE,
//				MuTag.IS_ESCAPE_TONE
//			);
//		boolean please = false;
//		MuTag addedTag = MuTag.IS_ANTICIPATION;
//		assertFalse(Mu028_AnticipationOfEscape.theOnlyWayToPleaseOrProvokeIsToAddAnEscapeToneOnIndexZero(list, please, addedTag));
//	}
	
	
	

}
