package com.i2class;

import java.math.BigDecimal;

/**
 * A mutable, fixed-length zoned decimal data type.
 * 
 */
public class zoned extends ZonedDecimal {

	public zoned(int length) {
		super(length);
	}

	public zoned(int length, int scale) {
		super(length, scale);
	}

	public zoned(int length, int scale, byte[] array, int offset) {
		super(length, scale, array, offset);
	}

	public zoned(int length, int scale, FixedPointer overlay) {
		super(length, scale, overlay);
	}

	public zoned(int length, int scale, BigDecimal bd) {
		super(length, scale, bd);
	}

	public zoned(int length, int scale, double value) {
		super(length, scale, value);
	}

	public zoned(int length, int scale, String value) {
		super(length, scale, value);
	}

	public zoned(int length, int scale, FigConstNum fc) {
		super(length, scale, fc);
	}
	
	public zoned(int length, int scale, InitialValue inz) {
		super(length, scale, inz);
	}

}
