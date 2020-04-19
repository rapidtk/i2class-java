/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.i2class;

/**
 * A multi-occur table.
 * 
 *
 */
public class tableDS extends DS {

	/**
	 * Construct a multi-occur data structure.
	 * @param sz The size of each occurrence of the DS
	 * @param elem The number of elements in this DS
	 */
	public tableDS(int sz, int elem)
	{
		super(sz, elem);
	}
	/**
	 * Look up a value, beginning at the specified 1-based index
	 */
	public int lookup(char arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.EQ);
	}

	/**
	 * Look up a value, beginning at the specified 1-based index
	 */
	public int lookup(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.EQ);
	}

	public int lookupge(char arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.GE);
	}

	public int lookupge(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.GE);
	}

	public int lookupgt(char arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.GT);
	}

	public int lookupgt(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.GT);
	}

	public int lookuple(char arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.LE);
	}

	public int lookuple(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.LE);
	}

	public int lookuplt(char arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.LT);
	}

	public int lookuplt(Comparable arg, int startIndex)
	{
		return lookupxx(arg, startIndex, Table.LT);
	}

	private int lookupxx(char arg, int startIndex, int compare)
	{
		FixedChar f = new FixedChar(1, arg);
		int i = Application.lookupxx(f, buffer, startIndex, compare);
		occur(i > 0 ? i : 1);
		return i;
	}

	private int lookupxx(Comparable arg, int startIndex, int compare)
	{
		int i = Application.lookupxx(arg, buffer, startIndex, compare);
		occur(i > 0 ? i : 1);
		return i;
	}

	static public void main(String[] args)
	{
		DS ds = new DS(20, 10);
		ds.occur(1);
		ds.assign("ABCDEF");
		ds.occur(2);
		ds.assign("GHIJKL");
		tableDS tds = new tableDS(20, 10);
		tds.assign(ds);
		tds.occur(2);
		tds.assign("MNOP");
		ds.assign(tds);
	}	

}
