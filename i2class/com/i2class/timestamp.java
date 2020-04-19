package com.i2class;

/**
 * A mutable timestamp class that represents an IBM i time data type.
 */
/**
 * 
 * An IBM i 26 character YYYY-MM-DD-HH:MM:SS:mmmmmm (accurate to the microsecond) timestamp.
 */
public class timestamp extends FixedTimestamp {

	public timestamp() {
	}

	public timestamp(FixedPointer overlay) {
		super(overlay);
	}

	public timestamp(Loval value) {
		super(value);
	}

	public timestamp(Hival value) {
		super(value);
	}

	public timestamp(FixedTimestamp value) {
		super(value);
	}

	public timestamp(String datfmt, int length, String datfmt400) {
		super(datfmt, length, datfmt400);
	}

}
