/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.lang.ref.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * A program message class.
 * 
 *
 */
public class Pgmmsg extends Exception {

	/** Formatted message text. */
	String m_msg;
	/** Formatted second level text. */
	String m_seclvl;
	/** Raw message data. */
	FixedData m_msgdta;
	/** 7-character message ID */
	String m_msgid;
	/* Severity level 0-99 */
	short m_sev;
	/** Message file */
	String m_msgf;
	/** 'OLD' flag when message has been received */
	boolean old;
	/** Message type */
	String m_msgtype;
	//TODO date and appRef should be here, but does anybody actually need them?
	/** Date sent */
	//Date m_dateSent = new Date();
	// A reference to the object that sent this message
	//WeakReference m_appRef;
	
	public static final int TYPES_QTDCHAR=1;
	public static final int TYPES_CHAR=2;
	public static final int TYPES_CCHAR=3;
	public static final int TYPES_HEX=4;
	public static final int TYPES_SPP=5;
	public static final int TYPES_DEC=6;
	public static final int TYPES_BIN=7;
	public static final int TYPES_UBIN=8;
	public static final int TYPES_DTS=9;
	public static final int TYPES_SYP=10;
	public static final int TYPES_ITV=11;
	
	/** Build a message from message text (no message id). */
	public Pgmmsg(String msg) {
		m_msg = msg;
		m_msgid="";
		m_msgf="";
	}
	
	/** Build an escape message with no message data. */
	public Pgmmsg(String msgid, String msgf) {
		this(msgid, msgf, null);
	}
	/** Build an escape message with the specfiied message data. */
	public Pgmmsg(String msgid, String msgf, FixedData msgDta) {
		this(msgid, msgf, msgDta, "*ESCAPE");
	}

	public static ResourceBundle resolveResourceBundle(String msgf)
	{
		String bundleName = "com.i2class.msgf." + msgf;
		return ResourceBundle.getBundle(bundleName);
	}
	/** Build error message from specified message file, message id, and message data. */
	public Pgmmsg(String msgid, String msgf, FixedData msgDta, String msgtype) 
	{
		m_msgf=msgf;
		m_msgid=msgid;
		m_msgtype=msgtype;
		if (msgDta != null)
			m_msgdta = (FixedData)msgDta.clone();
		ResourceBundle bundle = resolveResourceBundle(msgf);
		AbstractMessageDescription msgd = (AbstractMessageDescription)bundle.getObject(msgid);
		if (msgd==null)
			throw new Error("Unable to retrieve message id " + msgid + " from " + msgf);
		String msg=msgd.m_msg;
		String seclvl=msgd.m_seclvl;
		// Do variable substitution for 1st and 2nd level text
		m_msg = replaceVars(msgd, msg);
		m_seclvl = replaceVars(msgd, seclvl);
		// Print this message to 'joblog'
		I2Logger.logger.info(toString());
	}
	
	private String replaceVars(AbstractMessageDescription msgd, String s)
	{
		StringBuffer sBuf = new StringBuffer(s);
		if (m_msgdta != null)
		{
			// Replace variables (e.g. &1) with values
			int fmtsSize = msgd.fmts.size();
			int offset=0;
			int adjustment=0;
			for (int sv=0; sv<fmtsSize; sv++)
			{
				AbstractMessageDescription.FMT fmt = (AbstractMessageDescription.FMT)msgd.fmts.elementAt(sv);
				int dataType=fmt.m_dataType;
				int length=fmt.m_length;
				int vbyteOrDec=fmt.m_varbyteOrDec;
				String rplc;
				switch (dataType)
				{
				case TYPES_CCHAR:
				case TYPES_CHAR:
				case TYPES_QTDCHAR:
					rplc = m_msgdta.fixedAt(offset, length).trimr();
					break;
				case TYPES_BIN:
				case TYPES_UBIN:
				{
					long value;
					if (length==2)
						value = m_msgdta.shortAt(offset);
					else if (length==4)
						value = m_msgdta.intAt(offset);
					else /*if (length==8)*/
						value = m_msgdta.longAt(offset);
					rplc = Long.toString(value);
					break;
				}
				case TYPES_DEC:
					PackedDecimal p = m_msgdta.packedAt(offset, length, vbyteOrDec);
					rplc = p.toString();
					length = p.size();
					break;
				default:
					rplc="";
				}
				String ampsv = "&" + (sv+1);
				replaceText(sBuf, ampsv, rplc);
				offset += length;
			}
		}
		// Replace &N
		replaceText(sBuf, "&N", "\n");
		// Replace &P
		replaceText(sBuf, "&B", "\n      ");
		// Replace &B
		replaceText(sBuf, "&P", "\n    ");
		return sBuf.toString();
	}
	
	private void replaceText(StringBuffer sBuf, String find, String rplc)
	{
		String s = sBuf.toString();
		int i=s.indexOf(find);
		int rplcLength2 = rplc.length()-2;
		int adjustment=0;
		while (i>=0)
		{
			// Replace variable (e.g. &1) with value
			sBuf.replace(i+adjustment, i+adjustment+2, rplc);
			adjustment += rplcLength2; 
			i=s.indexOf(find, i+2);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		// Return formatted message string MSGID: 1st text \n 2nd text (e.g. CPF9810: Library XXX not found \n The specified library was not found...)
		String s = m_msgid + ": " + m_msg;
		if (m_seclvl.compareTo("")!=0)
			s = s + '\n' + m_seclvl;
		return s; 
	}
	
	/**
	 * See if this message ID matches the specified message ID.
	 * If the last two/four characters are "00"/"0000" then this is a generic comparison
	 * (i.e. CPF0000 = all CPF messages, CPF8800 all CPF88xx messages)	
	 */
	public boolean matches(String msgid)
	{
		// If the last two/four characters are "00"/"0000" then this is a generic comparison
		// (i.e. CPF0000 = all CPFxxxx messages, CPF8800 all CPF88xx messages)
		int compareLength;
		if (msgid.endsWith("0000"))
			compareLength=3;
		else if (msgid.endsWith("00"))
			compareLength=5;
		else
			compareLength=7;
		return m_msgid.startsWith(msgid.substring(0, compareLength));
	}

	/* Send an escape message. 
	public void escape(String msgid, String msgf) throws Pgmmsg
	{
		setMsgtype("*ESCAPE");
		try {
			exec(msgid, msgf);
		// This should never happen
		} catch (Exception e) {}
	}
	/** Send an escape message with the specified message data. 
	public void escape(String msgid, String msgf, FixedData msgdta) throws Pgmmsg
	{
		setMsgdta(msgdta);
		escape(msgid, msgf);
	}
	*/
	
	// Find the entry on the call-stack, by name
	static Application findCallStack(Application csapp, String pgmq, int callStack)
	{
		StringTokenizer st = new StringTokenizer(pgmq, " ()");
		String relationship=st.nextToken(); // *PRV or *SAME
		if (st.hasMoreTokens())
		{
			String callStackEntry = st.nextToken();
			// Find the entry on the call-stack, by name
			if (callStackEntry.compareTo("*")!=0)
			{
				Application prvApp = csapp.prvApp();
				while (prvApp != null)
				{
					String className = prvApp.getClass().getName();
					className = className.substring(className.lastIndexOf('.')+1);
					if (className.compareTo(callStackEntry)==0)
					{
						csapp = prvApp;
						break;
					}
					prvApp=prvApp.prvApp();
				}
			}
		}
		if (relationship.compareTo("*PRV")==0)
			csapp=csapp.prvApp();
		// Step backwards through call stack (this is a 'special' value set by the QMHSNDPM api)
		if (csapp != null)
		{
			for (int i=1; i<callStack; i++)
				csapp = csapp.prvApp();
		}
		return csapp;
	}

	static Pgmmsg findPgmmsg(Application csapp, FixedChar msgKey, String msgType, String msgAction)
	{
		Pgmmsg pgmmsg = null;
		// If we've found the matching Application, then add program message to program's message queue
		if (csapp!=null && csapp.pgmMsgs!=null)
		{
			int msgCount = csapp.pgmMsgs.size();
			if (msgCount>0)
			{
				// Determine which message to actually read
				int index=-1;
				// First message
				if (msgType.equals("*FIRST") || msgType.equals("*NEXT") && msgKey.equals("*TOP") ||
				 msgType.equals("*ANY") && msgKey.equals(Application.BLANKS))
					index=0;
				// Last message
				else if (msgType.equals("*LAST") || msgType.equals("*PRV") && msgKey.equals("\0\0\0\0"))
					index=msgCount-1;
				// If no key is specified, find the message with the corresponding type
				else if (msgKey.equals(Application.BLANKS))
				{
					for (int i=0; i<msgCount; i++)
					{
						pgmmsg = (Pgmmsg)csapp.pgmMsgs.elementAt(i);
						if (msgType.equals(pgmmsg.m_msgdta))
						{
							index=i;
							break;
						}
					}
				}
				// Find the specified message key
				else
				{
					// TODO find key based upon hash (int) value?
					if (msgType.equals("*NEXT"))
						index++;
					else if (msgType.equals("*PRV"))
						index--;
				}
				// If a message was found, then return it
				if (index>=0)
				{
					if (pgmmsg==null)
						pgmmsg = (Pgmmsg)csapp.pgmMsgs.elementAt(index);
					// Take message action (*SAME means no action)
					if (msgAction.equals("*REMOVE"))
						csapp.pgmMsgs.remove(index);
					else if (msgAction.equals("*OLD"))
						pgmmsg.old=true;
				}
			}
		}
		return pgmmsg;
	}
}
