package com.i2class;

import java.util.*;

/**
 * Summary description for VectorIterator.
 */
public class VectorIterator implements Iterator
{
	private Vector vector;
	private int position=0;
	private int size;

	public VectorIterator(Vector vector)
	{
		this.vector = vector;
		size = vector.size();
	}
	
	public boolean hasNext()
	{
		return position < size;
	}

	public Object next()
	{
		return vector.elementAt(position++);
	}

	public void remove()
	{
	}

}
