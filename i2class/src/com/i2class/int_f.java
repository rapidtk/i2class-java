package com.i2class;

/**
 * A mutable class that represents a java int stored in fixed-length data.
 */
public class int_f extends FixedBinary {

	public int_f(int sz) {
		super(sz);
	}

	public int_f(int sz, FixedPointer overlay) {
		super(sz, overlay);
	}

	public int_f(int sz, long value) {
		super(sz, value);
	}

	public int_f(int sz, FigConstNum fc) {
		super(sz, fc);
	}

}
