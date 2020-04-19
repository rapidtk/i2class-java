package com.i2class;

/**
 * A mutable time class that represents an IBM i time data type.
 */
public class time extends FixedTime {

	public time() {
		// TODO Auto-generated constructor stub
	}

	public time(String timfmt400) {
		super(timfmt400);
		// TODO Auto-generated constructor stub
	}

	public time(String timfmt400, Hival value) {
		super(timfmt400, value);
		// TODO Auto-generated constructor stub
	}

	public time(String timfmt400, Loval value) {
		super(timfmt400, value);
		// TODO Auto-generated constructor stub
	}

	public time(String datfmt, int length, String datfmt400) {
		super(datfmt, length, datfmt400);
		// TODO Auto-generated constructor stub
	}

	public time(String timfmt400, FixedTime value) {
		super(timfmt400, value);
		// TODO Auto-generated constructor stub
	}

	public time(FixedPointer overlay) {
		super(overlay);
		// TODO Auto-generated constructor stub
	}

	public time(String datfmt400, String inzval) {
		super(datfmt400, inzval);
		// TODO Auto-generated constructor stub
	}

}
