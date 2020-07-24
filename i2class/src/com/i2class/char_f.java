package com.i2class;

/**
 * A mutable fixed-length single-character class.
 */
public class char_f extends FixedChar {

	public char_f() {
		super(1);
	}

	public char_f(char c) {
		super(1, c);
	}

	public char_f(FigConst fc) {
		super(1, fc);
	}

	public char_f(FixedPointer overlay) {
		super(1, overlay);
	}

	public char_f(int sz, CharSequence seq) {
		super(1, seq.toString());
	}

}
