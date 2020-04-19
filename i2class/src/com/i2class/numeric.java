package com.i2class;

/**
 * A mutable fixed-length, scale (<=18 digits) decimal data type for exact arithmetic.
 * 
 * 
 *
 */
public class numeric extends ShortDecimal {

	public numeric() {
		// TODO Auto-generated constructor stub
	}

	public numeric(int length, int scale) {
		super(length, scale);
		// TODO Auto-generated constructor stub
	}

	public numeric(int length, int scale, FigConstNum value) {
		super(length, scale, value);
		// TODO Auto-generated constructor stub
	}

	public numeric(int length, int scale, double value) throws Exception {
		super(length, scale, value);
		// TODO Auto-generated constructor stub
	}

}
