package com.i2class;

import java.util.*;

/**
 * A 'helper' class used to implement SFL functionality
 * @author Andrew Clark
 * @see RfileWorkstn
 */
class RecordSFL /*implements IRecordSFL*/
{
	Vector fldValuesRRN = new Vector();
	//Vector changedValues = new Vector();
	TreeSet changedRRN_ = new TreeSet();
	/** Values added by sflnxtchg */
	TreeSet changedSFLNXTCHG = new TreeSet();
	//int readcRRN;
	boolean isChanged;
	int topRRN=1;
	int recno;
	int subfileSize;


	/** Get the changed values vector. 
	Vector getChangedValues()
	{
		return changedValues;
	}
	*/
	
	void addChangedRRN(Integer rrn)
	{
		changedRRN_.add(rrn);
	}
	
	void addChangedSFLNXTCHG(Integer rrn)
	{
		changedSFLNXTCHG.add(rrn);
	}
	
	int getReadcRRN()
	{
		/*
		int r=readcRRN;
		readcRRN++;
		return r;
		*/
		if (changedRRN_.size()<=0)
			return -1;
		Integer rrn = (Integer)changedRRN_.first();
		changedRRN_.remove(rrn);
		return rrn.intValue();
	}
	
	final int getRecno()
	{
		return recno;
	}

	int getCurrentSflSize()
	{
		return fldValuesRRN.size();
	}

	public int getTopRRN()
	{
		return topRRN;
	}

	// Again, this shouldn't be here but Java inheritance...
	Vector setRRN_(int rrn, Vector fldValues, Vector fldNames)
	{
		if (rrn != recno && recno > 0)
		{
			int maxrrn = rrn > recno ? rrn : recno;
			if (maxrrn > fldValuesRRN.size())
				fldValuesRRN.setSize(maxrrn);
			fldValuesRRN.setElementAt(fldValues, recno - 1);
			fldValues = (Vector) fldValuesRRN.elementAt(rrn - 1);
			if (fldValues == null)
			{
				fldValues = new Vector();
				int i = fldNames.size();
				fldValues.setSize(i);
			}
		}
		recno = rrn;
		return fldValues;
	}

	/**
	 * Set the top-most relative record number
	 * @version 11/4/2002 2:10:20 PM
	 * @param rrn the relative record number to set
	 */
	void setTopRRN_(int rrn, RfileWorkstn file)
	{
		topRRN = rrn;
		// Set the topmost subfile relative record number
		if (file.infds != null && file.infds.len() >= 379)
		{
			file.infds.setShortAt(377, (short) rrn);
			file.infds.updateThis();
		}
	}

	/** Clear subfile. */
	public int sflclr()
	{
		fldValuesRRN.removeAllElements();
		//changedValues.removeAllElements();
		changedRRN_.clear();
		changedSFLNXTCHG.clear();
		recno = 0;
		return 1;
	}

	/** Initialize subfile. */
	public void sflinz()
	{
		sflclr();
	}
}
