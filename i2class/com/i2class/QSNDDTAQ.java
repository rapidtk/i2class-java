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
 * @author ANDREWC
 */
public class QSNDDTAQ extends Application {
	
	Driver driver = registerDriver("com.ibm.as400.access.AS400JDBCDriver");
	I2Connection host = getConnection("jdbc:as400://ASC406", "ANDREWC", "SP8DS");
	
	public QSNDDTAQ(Application app) throws Exception {
	   super(app);
	}
	
	static QueueTable resolveDataQueue(Application app, fixed dtaq) throws OptionalDataException, SQLException, ClassNotFoundException, IOException
	{
		return QueueTable.resolveQueue(app, dtaq.trimr());
	}
	/**
	 * Send data to the specified data queue.
	 * @param dtaq Data queue fixed(10).  
	 * @param dtaqLib Data queue library fixed(10).  
	 * @param dtaLen Length of data packed(5,0)
	 * @param dta Data to be sent to queue
	 */
	public void call(fixed dtaq, fixed dtaqLib, packed dtaLen, FixedData dta) throws OptionalDataException, SQLException, ClassNotFoundException, IOException {
		Queue q = resolveDataQueue(this, dtaq);
		q.sendData(dta);
	 }
	
	/**
	 * Send keyed data to the specified data queue.
	 * @param dtaq Data queue fixed(10).  
	 * @param dtaqLib Data queue library fixed(10).  
	 * @param dtaLen Length of data packed(5,0)
	 * @param dta Data to be sent to queue
	 * @param keyLen Length of key data packed(3,0)
	 * @param key
	 */
	public void call(fixed dtaq, fixed dtaqLib, packed dtaLen, FixedData dta, packed keyLen, FixedData key) throws OptionalDataException, SQLException, ClassNotFoundException, IOException {
		Queue q = resolveDataQueue(this, dtaq);
		q.sendData(dta, key);
	 }
	 
	/*
	 public static void main(String[] args) throws Exception
	 {
	 	QSNDDTAQ qsnddtaq = new QSNDDTAQ(null);
		qsnddtaq.runMain();
	 }

	 public void runMain() throws Exception
	 {
	 	/*
	 	Crtdtaq crtdtaq = new Crtdtaq(host);
	 	crtdtaq.exec("ANDREWQ");
	 	* /
	 	fixed dtaq = new fixed(10, "ANDREWQ");
	 	fixed dtaqLib = new fixed(10);
	 	fixed dta = new fixed(3, "ABC");
		packed dtaLen = new packed(5,0,dta.size());
		QSNDDTAQ qsnddtaq = new QSNDDTAQ(this);
		qsnddtaq.call(dtaq, dtaqLib, dtaLen, dta);
		dta.assign("DEF");
		qsnddtaq.call(dtaq, dtaqLib, dtaLen, dta);
		fixed key = new fixed(4, "KEY1");
		packed keyLen = new packed(3,0,key.size());
		dta.assign("GHI");
		qsnddtaq.call(dtaq, dtaqLib, dtaLen, dta, keyLen, key);
		dta.assign("JKL");
		qsnddtaq.call(dtaq, dtaqLib, dtaLen, dta);
		
		
		// Receive data from queue
		QRCVDTAQ qrcvdtaq = new QRCVDTAQ(this);
		fixed newdta = new fixed(20);
		packed waitTime = new packed(5,0);
		qrcvdtaq.call(dtaq, dtaqLib, dtaLen, newdta, waitTime);
		fixed keyOrder = new fixed(2, "EQ");
		qrcvdtaq.call(dtaq, dtaqLib, dtaLen, newdta, waitTime, keyOrder, keyLen, key, null, null);
		qrcvdtaq.call(dtaq, dtaqLib, dtaLen, newdta, waitTime);
		qrcvdtaq.call(dtaq, dtaqLib, dtaLen, newdta, waitTime);
		qrcvdtaq.call(dtaq, dtaqLib, dtaLen, newdta, waitTime);
		return;
	 }
	 */
}
