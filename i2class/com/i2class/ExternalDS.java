package com.i2class;

import java.math.*;
/**
 * Simulate RPG multi-occur data structure.
 * A multi-occurrence data structure is like an array with an 
 * implied 1-based index (occurrence).
 *  
 */
public class ExternalDS extends FixedExternal
{
	/** The current occurence. */
	public int Occur;
	protected FixedChar buffer[];
	//private int m_sz;

	/**
	 * Construct a multi-occur data structure.
	 * @param sz The size of each occurrence of the DS
	 * @param elem The number of elements in this DS
	 */
	public ExternalDS(IRecordFormat rcdFmt, int elem)
	{
		// Create a fixed data type, but don't actually allocate space for it -- setOccurrence will set the pointer 
		// to the correct buffer[]
		super(rcdFmt, new FixedPointer());
		buffer = new FixedChar[elem];
		
		// Change this so that we allocate the other buffers only as needed
		//for (int i = 0; i < buffer.length; i++)
		//	buffer[i] = new fixed(sz);
		
		// Set aside room for swap buffer
		//buffer0 = new fixed(sz);
		
		//overlay = buffer[0].overlay;
		// Do setOccurrence instead of occur so that the work fields don't need to be initialized the first time
		setOccurrence(1);
	}
	/* *
	 * Assign a character result to this occurrance of a table.
	public void assign(char value)
	{
		super.assign(value);
	}
	 */
	 
	/** Clone this DS object. */
	public Object clone()
	{
		ExternalDS cloned = (ExternalDS)super.clone();
		cloned.buffer = new FixedChar[buffer.length];
		System.arraycopy(buffer, 0, cloned.buffer, 0, buffer.length);
		return cloned;
	}

	// This is where the work for occur is actually done.
	public void setOccurrence(int lOccur)
	{
		// Create multi-occur buffer if it doesn't already exist
		FixedChar bufOccur = buffer[lOccur - 1];
		if (bufOccur==null)
		{
			bufOccur=new FixedChar(size());
			buffer[lOccur-1]=bufOccur;
		}
		setOverlay(bufOccur.getOverlay());
		Occur = lOccur;
	}
	/**
	 * Set this DS to the specified occurrence.
	 */
	// A mult-occur DS is implemented as a Fixed with a buffer for all occurrences
	// except the current one.  buffer[Occur-2] holds the value of DS[0] and
	// overlay is the current (Occur) occurrence.
	public void occur(int lOccur)
	{
		/*
		if (Occur!=lOccur)
		{
			readSubfields();
			//memcpy(buffer0, overlay, sz);
			buffer0.movel(this);
			if (lOccur==1 || Occur==1)
			{
				int i;
				if (lOccur==1)
					i=Occur-2;
				else
					i=lOccur-2;
				//memcpy(overlay, buffer[i].overlay, sz);
				movel(buffer[i]);
				//memcpy(buffer[i].overlay, buffer0, sz);
				buffer[i].movel(buffer0);
			}
			else
			{
				//memcpy(overlay, buffer[lOccur-2].overlay, sz);
				movel(buffer[lOccur-2]);
				//memcpy(buffer[lOccur-2].overlay, buffer[Occur-2].overlay, sz);
				buffer[lOccur-2].movel(buffer[Occur-2]);
				//memcpy(buffer[Occur-2].overlay, buffer0, sz);
				buffer[Occur-2].movel(buffer0);
			}
			Occur=lOccur;
		}
		*/
		// Only change if the occurrence changes 
		if (Occur != lOccur)
		{
			// Save the values in the current occurrence
			rSubfields();
			setOccurrence(lOccur);
			updateThis();
		}
	}

	/** Set to the specified occurrence. **/
	public void occur(INumeric lOccur)
	{
		occur(lOccur.intValue());
	}

}
