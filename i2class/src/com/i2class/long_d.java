package com.i2class;

/**
 * A mutable fixed-length, scale decimal data type for exact arithmetic. 
 */
public class long_d extends LongDecimal {

	public long_d(int length, int scale) {
		super(length, scale);
	}

	/*
	public decimal(int length, int scale, BigDecimal inz) {
		super(length, scale, inz);
	}
	*/

	public long_d(int length, int scale, FigConstNum value) {
		super(length, scale, value);
	}

	public long_d(int length, int scale, double value) throws Exception {
		super(length, scale, value);
	}

}
