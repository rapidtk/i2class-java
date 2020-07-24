/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

/**
 * RCVMSG (Receive program message) API.
 * 
 */
public class QMHRCVPM extends Application {
	
	public QMHRCVPM(Application app) throws Exception {
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
	public void call(FixedData msgInfo, FixedBinary msgInfoLen, FixedChar fmtName, FixedChar callStackEntry,
	 FixedBinary callStackCounter, FixedChar msgType, FixedChar msgKey, FixedBinary waitTime, FixedChar msgAction, FixedData errcod) {
	 	// Find call stack
		Application csapp=Pgmmsg.findCallStack(prvApp(), callStackEntry.toString(), callStackCounter.intValue());
		// Find program message
		Pgmmsg pgmmsg = Pgmmsg.findPgmmsg(csapp, msgKey, msgType.toString(), msgAction.toString());
		if (pgmmsg != null)
		{
			// Fill in msgInfo structure
			int bytPrv = msgInfoLen.intValue(); // Bytes provided
			// The different (RCVM0100, RCVM0200, RCVM0300) formats use different offsets for some of the info...
			int msgdtaOffset, offsetCCSID, lengthOffset;
			// Offsets to all bin(4) values that need to be zero
			int[] zeroOffsets;
			if (fmtName.compareTo("RCVM0100")==0)
			{
				offsetCCSID=36;
				lengthOffset=40;
				msgdtaOffset=47;
				int[] z={32};
				zeroOffsets = z;
			}
			else
			{
				if (fmtName.compareTo("RCVM0200")==0)
				{
					offsetCCSID=148;
					lengthOffset=152;
					msgdtaOffset=176;
					int[] z={127, 131, 160, 164, 168, 172};
					zeroOffsets=z;
					/* Date sent
					if (bytPrv>=104)
					{
						date dateSent = new date("*CYMD0");
						dateSent.assign(pgmmsg.m_dateSent);
						msgInfo.setFixedAt(97, dateSent);
						// Time sent
						if (bytPrv>=110)
						{
							time timeSent = new time("*HMS0");
							timeSent.assign(pgmmsg.m_dateSent);
							msgInfo.setFixedAt(104, timeSent);
						}
					}
					*/
				}
				else //if (fmtName.compareTo("RCVM0300")==0)
				{
					offsetCCSID=72;
					lengthOffset=80;
					msgdtaOffset=112;
					int[] z={64, 68, 88, 92, 96, 100, 104, 108};
					zeroOffsets=z;
				}
				// Message file
				if (bytPrv>=35)
				{
					msgInfo.setFixedAt(25, new FixedChar(10, pgmmsg.m_msgf));
					// Message file library specified 
					if (bytPrv>=45)
					{
						FixedChar libl = new FixedChar(10, "*LIBL");
						msgInfo.setFixedAt(35, libl);
						// Message file library used
						if (bytPrv>=55)
							msgInfo.setFixedAt(45, libl);
					}
				}
			}
			 
			// Bytes available
			int msgdtaSize=0;
			if (pgmmsg.m_msgdta != null)
				msgdtaSize = pgmmsg.m_msgdta.size();
			int bytAvl = 47 + msgdtaSize;
			int bytRtn = Math.min(bytPrv, bytAvl); // Bytes returned
			
			msgInfo.setBinary(bytRtn, 0, 4); 
			msgInfo.setBinary(bytAvl, 4, 4);
			if (bytPrv>=12)
			{
				msgInfo.setBinary(pgmmsg.m_sev, 8, 4); // Message severity
				if (bytPrv>=19)
				{
					msgInfo.setFixedAt(12, new FixedChar(7, pgmmsg.m_msgid)); // MSGID
					// Build message type
					if (bytPrv>=21)
					{
						FixedChar msgtyp = new FixedChar(2);
						if (pgmmsg.m_msgtype.compareTo("*ESCAPE")==0)
							msgtyp.assign("15"); // Completion
						else if (pgmmsg.m_msgtype.compareTo("*COMP")==0)
							msgtyp.assign("01"); // Completion
						else if (pgmmsg.m_msgtype.compareTo("*DIAG")==0)
							msgtyp.assign("02"); // Completion
						else if (pgmmsg.m_msgtype.compareTo("*INFO")==0)
							msgtyp.assign("04"); // Informational
						msgInfo.setFixedAt(19, msgtyp);
						// Message key
						if (bytPrv >= 25)
						{
							msgInfo.setBinary(pgmmsg.hashCode(), 21, 4);
							
							// CCSID of replacement data
							if (bytPrv>=offsetCCSID+4)
							{
								msgInfo.setBinary(819, offsetCCSID, 4); // 819=ISO 8859-1 Latin?
								// Length of message data returned
								if (bytPrv>=lengthOffset+4)
								{
									msgInfo.setBinary(bytRtn-msgdtaOffset, lengthOffset, 4);
									// Length of message data available
									if (bytPrv>=lengthOffset+8)
									{
										msgInfo.setBinary(bytPrv-msgdtaOffset, lengthOffset+4, 4);
										// Message data
										if (bytPrv>msgdtaOffset)
											msgInfo.assign(pgmmsg.m_msgdta.getOverlay(), pgmmsg.m_msgdta.getOffset()+msgdtaOffset);
									}
								}
							}
						}
					}
				}
			}
			// Zero out all of the values in zeroOffsets
			for (int i=0; i<zeroOffsets.length; i++)
			{
				int zoi = zeroOffsets[i];
				// Make sure that there is room in the provided info structure (the offset+4 bytes for int)
				if ((zoi+4)>bytPrv)
					break;
				msgInfo.setBinary(0, zoi, 4);
			}
		}
	}

	public static QMHRCVPM activate(Application app, QMHRCVPM instance) throws Exception {
		if (instance == null)
			instance = new QMHRCVPM(app);
		return instance;
	}

}
