package com.i2class;

import java.math.*;
/**
 * Simulate functionality of an RPG table (a variable that begins with 'tab').
 * RPG tables retain the index of a lookup comparison.
 * @author: Andrew Clark
 */
public class Table extends ZonedDecimal
{
	static final int EQ = 0;
	static final int GE = 1;
	static final int GT = 2;
	static final int LE = 3;
	static final int LT = 4;
	private int scale;
	/** The current occurence. */
	public int Occur = 1;
	protected ZonedDecimal buffer[];

	/**
	 * Create a numeric table with the specified length, scale and number of elements.
	 * @param sz Total number of digits.
	 * @param scale Scale of value (number of digits to the right of the decimal point.
	 * @param elem Total number of elements 
	 */
	public Table(int sz, int scale, int elem)
	{
		super(sz, scale);
		buffer = new ZonedDecimal[elem];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = new ZonedDecimal(sz, scale);
		// Set aside room for swap buffer
		//buffer0 = new fixed(sz);
		setOverlay(buffer[0].getOverlay());
	}

	/* *
	 * Assign a character result to this occurrance of a table.
	public void assign(char value)
	{
		super.assign(value);
	}
	 */
	 
	/** Clone this table object. */
	public Object clone()
	{
		Table cloned = (Table)super.clone();
		cloned.buffer = new ZonedDecimal[buffer.length];
		System.arraycopy(buffer, 0, cloned.buffer, 0, buffer.length);
		return cloned;
	}

	/**
	 * Assign a double result to this occurrance of a table.
	 */
	public void /*table*/ assign(double d)
	{
		//zoned znd = new zoned(len(), scale, d);
		//assign(znd);
		buffer[Occur - 1].assign(d);
		//return this;
	}
	/**
	 * Assign a int result to this occurrance of a table.
	 */
	public void assign(int value)
	{
		assign((long)value);
	}
	/**
	 * Assign a long result to this occurrance of a table.
	 */
	public void assign(long value)
	{
		buffer[Occur - 1].assign(value);
	}
	/**
	 * Assign a Java BigDecimal result to this occurrance of a table.
	 */
	public void /*table*/
	assign(BigDecimal bd)
	{
		//zoned znd = new zoned(len(), scale, bd);
		//assign(znd);
		buffer[Occur - 1].assign(bd);
		//return this;
	}
	

	/**
	 * Look up a value, beginning at the specified 1-based index
	 */
	public int lookup(double arg, int startIndex)
	{
		return lookupxx(arg, startIndex, EQ);
	}
	/**
	 * Look up a value, beginning at the specified 1-based index
	 */
	public int lookup(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, EQ);
	}
	public int lookupge(double arg, int startIndex)
	{
		return lookupxx(arg, startIndex, GE);
	}
	public int lookupge(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, GE);
	}
	public int lookupgt(double arg, int startIndex)
	{
		return lookupxx(arg, startIndex, GT);
	}
	public int lookupgt(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, GT);
	}
	public int lookuple(double arg, int startIndex)
	{
		return lookupxx(arg, startIndex, LE);
	}
	public int lookuple(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, LE);
	}
	public int lookuplt(double arg, int startIndex)
	{
		return lookupxx(arg, startIndex, LT);
	}
	public int lookuplt(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, LT);
	}
	private int lookupxx(double arg, int startIndex, int compare)
	{
		int i = Application.lookupxx(arg, buffer, startIndex, compare);
		occur(i > 0 ? i : 1);
		return i;
	}
	private int lookupxx(Comparable arg, int startIndex, int compare)
	{
		int i = Application.lookupxx(arg, buffer, startIndex, compare);
		occur(i > 0 ? i : 1);
		return i;
	}
	
	/** Set this table to the specified occurrence. */
	public void occur(int lOccur)
	{
		setOverlay(buffer[lOccur - 1].getOverlay());
		Occur = lOccur;
	}
}
