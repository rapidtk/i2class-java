/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * Send data queue API.
 * 
 */
public class QRCVDTAQ extends Application {
	
	Driver driver = registerDriver("com.ibm.as400.access.AS400JDBCDriver");
	I2Connection host = getConnection("jdbc:as400://ASC406", "ANDREWC", "SP8DS");
	
	public QRCVDTAQ(Application app) throws Exception {
	   super(app);
	}
	
	/**
	 * Receive data from the specified queue.
	 * @param dtaq Data queue fixed(10).  
	 * @param dtaqLib Data queue library fixed(10).  
	 * @param rtnDtaLen Length of data returned
	 * @param rtnDta Buffer to return data into
	 * @param waitTime the length of time (in seconds) to wait for the data
	 */
	public void call(FixedChar dtaq, FixedChar dtaqLib, PackedDecimal rtnDtaLen, FixedData rtnDta, PackedDecimal waitTime) throws OptionalDataException, SQLException, ClassNotFoundException, IOException
	{
		Queue q = QSNDDTAQ.resolveDataQueue(this, dtaq);
		int wt = waitTime.intValue();
		int receiveLength=0;
		FixedData f = (FixedData)q.receiveObject(wt);
		if (f!=null)
		{
			receiveLength=f.size();
			rtnDta.movel(f);
		}
		rtnDtaLen.assign(receiveLength);
	 }

	/**
	 * Receive keyed data from the specified data queue.
	 * @param dtaq Data queue fixed(10).  
	 * @param dtaqLib Data queue library fixed(10).  
	 * @param dtaLen Length of data returned
	 * @param rtnDta Buffer to return data into
	 * @param waitTime the length of time (in seconds) to wait for the data
	 * @param keyOrder the comparison criteria between keys (GT, LT, NE, EQ, GE, LE) fixed(2)
	 * @param keyLen Length of key data packed(3,0)
	 * @param key
	 * @param sndInfLen Length of sender information to be returned packed(3,0)
	 * @param rtnSndInf sender info returned (output)
	 */
	public void call(FixedChar dtaq, FixedChar dtaqLib, PackedDecimal rtnDtaLen, FixedData rtnDta, PackedDecimal waitTime,
	 FixedChar keyOrder, PackedDecimal keyLen, FixedData key, PackedDecimal sndInfLen, FixedData rtnSndInf) throws OptionalDataException, SQLException, ClassNotFoundException, IOException
	{
		Queue q = QSNDDTAQ.resolveDataQueue(this, dtaq);
		int wt = waitTime.intValue();
		FixedData f = (FixedData)q.receiveObject(wt, key, keyOrder.toString());
		int receiveLength=0;
		if (f!=null)
		{
			rtnDta.movel(f);
			receiveLength=f.size();
		}
		rtnDtaLen.assign(receiveLength);
	 }
	
	public static QRCVDTAQ activate(Application app, QRCVDTAQ instance) throws Exception {
		if (instance == null)
			instance = new QRCVDTAQ(app);
		return instance;
	}

}
