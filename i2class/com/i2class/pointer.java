package com.i2class;

/**
 * A pointer to fixed-length data.
 */
public class pointer extends FixedPointer {

	public pointer() {
	}

	public pointer(FixedData overlaidField) {
		super(overlaidField);
	}

	public pointer(FixedData overlaidField, int offset) {
		super(overlaidField, offset);
	}

	public pointer(byte[] array, int offset) {
		super(array, offset);
	}

	public pointer(FixedPointer overlay) {
		super(overlay);
	}

}
