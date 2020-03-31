/*
 * Created on Mar 14, 2005
 */
package com.i2class;

import java.io.Serializable;

/**
 * A pointer to fixed-length data.
 * @author ANDREWC
 */
final public class pointer implements Serializable
{
	byte[] 	m_overlay;
	int		m_offset;

	pointer()
	{
	}
	pointer(FixedData overlaidField)
	{
		this(overlaidField, 0);
	}
	pointer(FixedData overlaidField, int offset)
	{
		this(overlaidField.getOverlay(), overlaidField.getOffset()+offset);
	}
	pointer(byte[] array, int offset)
	{
		m_overlay=array;
		m_offset=offset;
	}

	public pointer(pointer overlay)
	{
		assign(overlay);
	}
	
	/** Assign another pointer to this one. */
	public void assign(pointer ptr)
	{
		m_overlay=ptr.m_overlay;
		m_offset=ptr.m_offset;
	}
	
	/** Return a new pointer that is offset by the specified value. */
	public pointer plus(int offset)
	{
		return new pointer(m_overlay, m_offset+offset);
	}
	
	/** Add the specified offset to this pointer. */
	public void add(int offset)
	{
		m_offset += offset;
	}
	
	/** Return a new pointer that is offset by the specified value. */
	public pointer minus(int offset)
	{
		return plus(-offset);
	}
	
	/** Subtract the specified offset from this pointer. */
	public void sub(int offset)
	{
		add(-offset);
	}
	
}
