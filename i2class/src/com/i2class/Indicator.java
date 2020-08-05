package com.i2class;

/**
 * A mutable fixed-length single-character class that considers the special values *ON ('1') and *OFF ('0') as boolean values
 */
public class Indicator extends FixedChar {

	public Indicator() {
		super(1);
	}

	public Indicator(char c) {
		super(1, c);
	}

	public Indicator(boolean value) {
		super(1, value ? '1' : '0');
	}

	public Indicator(FixedPointer overlay) {
		super(1, overlay);
	}
	
	//@Override
	public void assign(boolean value) {
		assign(value ? '1' : '0');
	}
}
