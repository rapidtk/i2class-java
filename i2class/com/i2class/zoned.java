package com.i2class;

import java.math.BigDecimal;

/**
 * A mutable, fixed-length zoned decimal data type.
 * 
 */
public class zoned extends ZonedDecimal {

	public zoned(int length) {
		super(length);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale) {
		super(length, scale);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, byte[] array, int offset) {
		super(length, scale, array, offset);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, FixedPointer overlay) {
		super(length, scale, overlay);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, BigDecimal bd) {
		super(length, scale, bd);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, double value) {
		super(length, scale, value);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, String value) {
		super(length, scale, value);
		// TODO Auto-generated constructor stub
	}

	public zoned(int length, int scale, FigConstNum fc) {
		super(length, scale, fc);
		// TODO Auto-generated constructor stub
	}

}
