/*
 * Created on Mar 18, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.i2class;

import java.io.*;

/**
 * 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class OffsetDataInputStream extends DataInputStream {
	int offset;

	/**
	 * @param arg0
	 */
	public OffsetDataInputStream(InputStream arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public int readIntOffset() throws IOException
	{
		offset +=2;
		return super.readInt();
	}

}
