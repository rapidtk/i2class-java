/*
 * Created on Mar 14, 2005
 */
package com.i2class;

import java.io.Serializable;
import java.util.Vector;

/**
 * A pointer to fixed-length data.
 * 
 * 
 */
public class FixedPointer implements Serializable
{
	//TODO: do we really need m_overlay anymore since we have overlaidField now??
	byte[] 	m_overlay;
	int		m_offset;
	FixedData overlaidField;

	FixedPointer()
	{
	}
	FixedPointer(FixedData overlaidField)
	{
		this(overlaidField, 0);
	}
	FixedPointer(FixedData overlaidField, int offset)
	{
		this(overlaidField.getOverlay(), overlaidField.getOffset()+offset);
		this.overlaidField = overlaidField;
	}
	FixedPointer(byte[] array, int offset)
	{
		m_overlay=array;
		m_offset=offset;
	}

	public FixedPointer(FixedPointer overlay)
	{
		assign(overlay);
	}
	
	/** Assign another pointer to this one. */
	public void assign(FixedPointer ptr)
	{
		m_overlay=ptr.m_overlay;
		m_offset=ptr.m_offset;
	}
	
	/** Return a new pointer that is offset by the specified value. */
	public FixedPointer plus(int offset)
	{
		return new FixedPointer(m_overlay, m_offset+offset);
	}
	
	/** Add the specified offset to this pointer. */
	public void add(int offset)
	{
		m_offset += offset;
	}
	
	/** Return a new pointer that is offset by the specified value. */
	public FixedPointer minus(int offset)
	{
		return plus(-offset);
	}
	
	/** Subtract the specified offset from this pointer. */
	public void sub(int offset)
	{
		add(-offset);
	}
	
	void addOverlayField(FixedData overlayField) {
		if (overlaidField!=null)
			overlaidField.addOverlayField(overlayField);
	}
	
}
