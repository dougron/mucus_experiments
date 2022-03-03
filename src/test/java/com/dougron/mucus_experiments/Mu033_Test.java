package test.java.com.dougron.mucus_experiments;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus_experiments.completed.Mu033_InteractionVersionTwo;

class Mu033_Test
{

	@Test
	void padded_zero_number_returns_correct_string()
	{
		assertEquals("001", Mu033_InteractionVersionTwo.getZeroPaddedNumber(1, 3));
	}

}
