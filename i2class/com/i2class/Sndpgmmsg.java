/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.lang.ref.*;
import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * SNDPGMMSG (Send program message) processing.
 * @author ANDREWC
 */
public class Sndpgmmsg extends AbstractCommand {

	private String m_msgid;
	private String m_msgf;
	private FixedData m_msgdta; // *NONE=""
	private String m_topgmq="*PRV";
	private String m_msgtype="*INFO";
	private String m_tomsgq="*TOPGMQ";
	int m_callStack;
	private fixed m_keyvar;
	private String m_msg;
	
	
	Sndpgmmsg() {}
	public Sndpgmmsg(I2Connection conn) 
	{
		super(conn); 
	}
	
	public Sndpgmmsg(Application app) 
	{
		super();
		setApp(app); 
	}

	static final String[] PARM_NAMES={"MSG", "MSGID", "MSGF", "MSGDTA"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setTomsgq(fixed tomsgq) {
		setTomsgq(tomsgq.trimr());
	}
	public void setTomsgq(String tomsgq) {
		m_tomsgq = Application.trimr(tomsgq);
	}
	
	public void setTopgmq(fixed topgmq) {
		setTopgmq(topgmq.trimr());
	}
	public void setTopgmq(String topgmq) {
		m_topgmq = Application.trimr(topgmq);
	}
	
	public void setMsg(String msg) {
		m_msg = msg;
	}
	public void setMsg(fixed msg) {
		setMsgid(msg.trimr());
	}

	public void setMsgid(fixed msgid) {
		setMsgid(msgid.trimr());
	}
	public void setMsgid(String msgid) {
		m_msgid=Application.trimr(msgid);
	}
	
	public void setMsgf(fixed msgf) {
		setMsgf(msgf.toString());
	}
	public void setMsgf(String msgf) {
		m_msgf = qual(msgf);
	}
	
	public void setMsgdta(String msgdta) {
		setMsgdta(new fixed(msgdta.length(), msgdta));
	}
	public void setMsgdta(FixedData msgdta) {
		//m_msgdta = (FixedData)msgdta.clone();
		m_msgdta = msgdta;
	}

	public void setMsgtype(fixed msgtype) {
		setMsgtype(msgtype.trimr());
	}
	public void setMsgtype(String msgtype) {
		m_msgtype = Application.trimr(msgtype);
	}

	public void setMsgq(fixed tomsgq) {
		setMsgq(tomsgq.trimr());
	}
	public void setMsgq(String tomsgq) {
		m_tomsgq = Application.trimr(tomsgq);
	}
	public void setKeyvar(fixed keyvar) {
		m_keyvar = keyvar;
	}

	/** Send the specified program message. */
	public void exec(Object msgid, Object msgf) throws Exception
	{
		setMsgid(msgid.toString());
		setMsgf(msgf.toString());
		exec();
	}
	
	/** Send a completion message. */
	public void completion(String msgid, String msgf)
	{
		setMsgtype("*COMP");
		try {
			exec(msgid, msgf);
		// This should never happen for a completion message
		} catch (Exception e) {}
	}
	/** Send an escape message with the specified message data. */
	public void completion(String msgid, String msgf, FixedData msgdta)
	{
		setMsgdta(msgdta);
		completion(msgid, msgf);
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		Application csapp=null;
		Application app = getApp();
		if (m_topgmq.compareTo("*EXT")!=0)
			csapp=Pgmmsg.findCallStack(app, m_topgmq, m_callStack);
			
		// Create actual program message object
		Pgmmsg pgmmsg;
		// If MSG is specified, this is a program message without any message file, id, etc.
		if (m_msg != null)
		{
			pgmmsg = new Pgmmsg(m_msg);
			pgmmsg.m_msgtype = m_msgtype;
		}
		else
			pgmmsg = new Pgmmsg(m_msgid, m_msgf, m_msgdta, m_msgtype);
		// Add weak reference to program that sent message
		//pgmmsg.m_appRef = new WeakReference(app);
		

		// If we've found the matching Application, then add program message to program's message queue
		if (csapp!=null)
		{
			if (csapp.pgmMsgs==null)
				csapp.pgmMsgs = new Vector();
			csapp.pgmMsgs.add(pgmmsg);
		}
		// The KEYVAR is actually the hash code of the Pgmmsg object
		if (m_keyvar != null)
			m_keyvar.setBinary(pgmmsg.hashCode(), 0, 4);
		/* Messages sent to *EXT should go to console??? We're always doing it right now (see below)
		else
		*/ 
		I2Logger.logger.info(pgmmsg);
		// If this is an escape message, throw error
		if (m_msgtype.compareTo("*ESCAPE")==0)
		{
			I2Logger.logger.printStackTrace(pgmmsg);
			throw pgmmsg;
		}
		
		// If a TOMSGQ is specified, also send to the specified message queue
		if (m_tomsgq.compareTo("*TOPGMQ")!=0)
		{
			StringTokenizer st = new StringTokenizer(m_tomsgq, " ");
			while (st.hasMoreTokens())
			{
				String msgqString = st.nextToken();
				int i = msgqString.indexOf('/');
				if (i>0)
					msgqString = msgqString.substring(i+1);
				// The message key is always the hashCode equivalent of the value
				fixedInt msgKey = new fixedInt(4, pgmmsg.hashCode()); 
				QueueTable.resolveQueue(app, msgqString+".msgq").sendObject(pgmmsg, msgKey);
			}
		}
	}
}
