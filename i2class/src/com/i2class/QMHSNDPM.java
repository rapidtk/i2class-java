/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * SNDPGMMSG (Send program message) API.
 * 
 */
public class QMHSNDPM extends Application {
	
	public QMHSNDPM(Application app) throws Exception {
	   super(app);
	}
	
	/**
	 * @param msgid Message identifier fixed(7).  
	 * @param msgf Message file fixed(20)
	 * @param msgdta Message data
	 * @param msgLength Message length
	 * @param msgType Message Type 
	 * @param callStackEntry Call Stack Entry
	 * @param callStackCounter Call Stack Counter
	 * @param msgKey Message Key
	 * @param errcod Error code
	 * @throws Exception
	 */
	public void call(FixedChar msgid, FixedChar msgf, FixedChar msgdta, FixedBinary msgLength, FixedChar msgType, FixedChar callStackEntry,
	 FixedBinary callStackCounter, FixedChar msgKey, FixedData errcod) throws Exception {
	 	Sndpgmmsg sndpgmmsg = new Sndpgmmsg();
	 	sndpgmmsg.setApp(prvApp());
	 	sndpgmmsg.setMsgid(msgid);
		sndpgmmsg.setMsgf(msgf);
		sndpgmmsg.setMsgdta(msgdta);
		sndpgmmsg.setMsgtype(msgType);
		sndpgmmsg.m_callStack=callStackCounter.intValue();
		sndpgmmsg.exec();
	 }
	 
	/*
	 public static void main(String[] args)
	 {
	 	fixed msgid=new fixed(10,"CPF9810");
	 	fixed msgf=new fixed(20, "QCPFMSG");
	 	fixed msgdta=new fixed(10, "APLSAMPLE");
	 	fixedBin msgLength = new fixedBin(4, 10);
	 	fixed msgType = new fixed(10, "*ESCAPE");
	 	fixed callStackEntry=new fixed(10, "*");
	 	fixedBin callStackCounter = new fixedBin(4, 0);
	 	fixed msgKey = new fixed(4);
	 	fixedBin errcod=new fixedBin(4, 0);
	 	try {
			new QMHSNDPM(new Application(null)).call(msgid, msgf, msgdta, msgLength, msgType, callStackEntry, callStackCounter, msgKey, errcod);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	 }
	 */
}
