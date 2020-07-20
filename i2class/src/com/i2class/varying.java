package com.i2class;

/**
 * A varying-length string with a fixed-length (maximum) buffer size.
 */
public class varying extends CharVarying {

	public varying(int sz) {
		super(sz);
	}

	public varying(int sz, byte[] array, int index) {
		super(sz, array, index);
	}

	public varying(int sz, char c) {
		super(sz, c);
	}

	public varying(int sz, FigConst fc) {
		super(sz, fc);
	}

	public varying(int sz, String str) {
		super(sz, str);
	}
	public varying(int sz, FixedPointer overlay)
	{
		super(sz, overlay);
	}

}
