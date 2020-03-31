/* Created on Aug 25, 2004 */
package com.i2class;

/**
 * Exception thrown by numeric classes if value being assigned would cause significant digits to be lost.
 * 
 * @author ANDREWC
 */
public class SignificantDigitsLostException extends ArithmeticException {
	public SignificantDigitsLostException(int length, int scale, String value) {
		super("The numeric " + length + ',' + scale + " variable is too small to hold the value " + value);
	}
	public SignificantDigitsLostException(AbstractNumeric field, String value) {
		this(field.len(), field.Scale, value);
	}
}
