package com.i2class;

/**
 * A mutable fixed-length character string class.
 */
public class fixed extends FixedChar {

	public fixed() {
	}

	public fixed(int sz) {
		super(sz);
	}

	public fixed(int sz, byte[] array, int index) {
		super(sz, array, index);
	}

	public fixed(int sz, char c) {
		super(sz, c);
	}

	public fixed(int sz, FigConst fc) {
		super(sz, fc);
	}

	public fixed(int sz, FixedPointer overlay) {
		super(sz, overlay);
	}

	public fixed(int sz, String str) {
		super(sz, str);
	}
	
	public fixed(int sz, InitialValue inz)
	{
		super(sz, inz);
	}

}
