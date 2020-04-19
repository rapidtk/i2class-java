package com.i2class;

import java.math.BigDecimal;

/**
 * A mutable, fixed-length packed decimal data type.
 * 
 */
public class packed extends PackedDecimal {

	public packed(int length, int scale) {
		super(length, scale);
	}

	public packed(int length, int scale, FixedPointer overlay) {
		super(length, scale, overlay);
	}

	public packed(int length, int scale, byte[] array, int offset) {
		super(length, scale, array, offset);
	}

	public packed(int length, int scale, double value) {
		super(length, scale, value);
	}

	public packed(int length, int scale, String value) {
		super(length, scale, value);
	}

	public packed(int length, int scale, BigDecimal bd) {
		super(length, scale, bd);
	}

	public packed(int length, int scale, FigConstNum fc) {
		super(length, scale, fc);
	}

}
