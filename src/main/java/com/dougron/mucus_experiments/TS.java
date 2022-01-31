package main.java.com.dougron.mucus_experiments;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class TS {

	
	private String name;
	private int numerator = 0;
	private int denominator = 0;
	private boolean isComplete = false;
	public TimeSignature ts;

	public TS(String name, TimeSignature ts) {
		this.name = name;
		this.ts = ts;
		numerator = ts.getNumerator();
		denominator = ts.getDenominator();
		isComplete = true;
	}
	
	public TS() {
		
	}
	


	public String getName() {
		return name;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public void setName(String aName) {
		name = aName;
		testForComplete();
	}
	
	public void setNumerator(int aInt) {
		numerator = aInt;
		testForComplete();
	}
	
	public void setDenominator(int aInt) {
		denominator = aInt;
		testForComplete();
	}

	private void testForComplete() {
		if (!isComplete) {
			if (name == null || numerator == 0 || denominator == 0) {
				isComplete = false;
			} else {
				isComplete = true;
				// potential problem here with the new TimeSignature and the empty Object array
				// cant actually remember if TS is still used :/
				
				// commented out to avoid error. TS gets used in Mu001_blahblah for something or the other
				// unlikely to persist as important
//				ts = new TimeSignature(numerator, denominator, new Object[] {});
			}
		}
		
	}
}
