
package com.i2class;

/**
 * A comparable array class.  Loops through index of array and compares to identical element of another array
 * @author ANDREWC
 */
final class ComparableArray implements Comparable {
	Object array[];
	int length;
	boolean partialMatch;
	ComparableArray(boolean partialMatch)
	{
		this.partialMatch=partialMatch;
	}
	ComparableArray(Object array[])
	{
		this.array = new Object[array.length];
		System.arraycopy(array, 0, this.array, 0, array.length);
		length = array.length;
	}
	
	/**
	 * Compare to another comparable array object.
	 */
	public int compareTo(Object value)
	{
		ComparableArray ca = (ComparableArray)value;
		return compareTo(ca.array, ca.length);
		/*
		int minLength = Math.min(array.length, ca.array.length);
		int diff;
		for (int i=0; i<minLength; i++)
		{
			Comparable arrayi = (Comparable)array[i];
			Object caarrayi = ca.array[i];
			if (arrayi==null || caarrayi==null)
				break;
			/*
			if (arrayi==null)
			{
				if (caarrayi==null)
					diff=0;
				else
					diff=-1;
			}
			else
			* /
			diff = arrayi.compareTo(caarrayi);
			if (diff != 0)
				return diff;
		}
		diff = length - ca.length;
		return diff;
		*/
	}
	
	/**
	 * Compare to another array.
	 */
	public int compareTo(Object caarray[], int caarraylength)
	{
		int diff;
		int minLength = Math.min(array.length, caarray.length);
		for (int i=0; i<minLength; i++)
		{
			Comparable arrayi = (Comparable)array[i];
			Object caarrayi = caarray[i];
			if (arrayi==null || caarrayi==null)
				break;
			/*
			if (arrayi==null)
			{
				if (caarrayi==null)
					diff=0;
				else
					diff=-1;
			}
			else
			*/
				diff = arrayi.compareTo(caarrayi);
			if (diff != 0)
				return diff;
		}
		diff = length - caarraylength;
		return diff;
	}
	
	public String toString() {
		return array.toString();
	}


}
