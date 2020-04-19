package com.i2class;

/**
 * A mutable, fixed-length date class that represents an IBM i date data type e.g. YYYY-MM-DD.
 */
public class date extends FixedDate {

	public date() {
	}

	public date(String datfmt400) {
		super(datfmt400);
	}

	public date(String datfmt400, Loval value) {
		super(datfmt400, value);
	}

	public date(String datfmt400, Hival value) {
		super(datfmt400, value);
	}

	public date(String datfmt400, FixedDate value) {
		super(datfmt400, value);
	}

	public date(FixedPointer overlay) {
		super(overlay);
	}

	public date(String datfmt, String datfmt400, FixedPointer overlay) {
		super(datfmt, datfmt400, overlay);
	}

	public date(String datfmt, int length, String datfmt400) {
		super(datfmt, length, datfmt400);
	}

	public date(String datfmt400, String inzval) {
		super(datfmt400, inzval);
	}

}
