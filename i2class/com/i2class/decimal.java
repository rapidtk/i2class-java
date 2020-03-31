/*
 * Created on Aug 12, 2004
 *
 */
package com.i2class;

import java.math.BigDecimal;

/**
 * A mutable fixed-length, scale decimal data type for exact arithmetic. 
 * 
 * @author ANDREWC
 */
public class decimal extends longDecimal {

	public decimal(int length, int scale) {
		super(length, scale);
	}

	/*
	public decimal(int length, int scale, BigDecimal inz) {
		super(length, scale, inz);
	}
	*/

	public decimal(int length, int scale, FigConstNum value) {
		super(length, scale, value);
	}

	public decimal(int length, int scale, double value) throws Exception {
		super(length, scale, value);
	}

}
