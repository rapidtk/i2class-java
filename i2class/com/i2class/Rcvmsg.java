/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;


/**
 * RCVPGMMSG (Receive program message) processing.
 * 
 */
public class Rcvmsg extends AbstractCommand {

	private String m_pgmq="*SAME";
	private String m_msgq;
	private String m_msgtype="*ANY";
	private String m_rmv="*YES";
	private int m_wait=0;
	private FixedChar m_msgkey;

	private FixedChar m_keyvar;
	private FixedChar m_msg;
	private INumeric m_msglen;
	private FixedData m_msgdta;
	private INumeric m_msgdtalen;
	private FixedChar m_msgid;
	private FixedChar m_msgf;
	private FixedChar m_msgflib;
	
	private Pgmmsg pgmmsg;
	
	private final static FixedChar FORMAT_NAME=new FixedChar(10, "RCVM0100");
	private final static FixedBinary WAIT_TIME = new FixedBinary(4);
	private final static FixedChar MSG_KEY = new FixedChar(4);
	private final static FixedBinary ERRCOD = new FixedBinary(4);
	
	
	/**
	 * 
	 */
	public Rcvmsg(I2Connection rconn) {
		super(rconn);
	}
	

	static final String[] PARM_NAMES={"PGMQ", "MSGQ", "MSGTYPE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setPgmq(String pgmq) {
		m_pgmq = Application.trimr(pgmq);
	}
	public void setKeyvar(FixedChar keyvar) {
		m_keyvar = keyvar;
	}
	public void setMsgtype(String msgtype) {
		m_msgtype = Application.trimr(msgtype);
	}
	public void setRmv(String rmv) {
		m_rmv = Application.trimr(rmv);
	}
	public void setMsg(FixedChar msg)
	{
		m_msg = msg;
	}
	public void setMsglen(INumeric msglen)
	{
		m_msglen = msglen;
	}
	public void setMsgdta(FixedData msgdta)
	{
		m_msgdta = msgdta;
	}
	public void setMsgdtalen(INumeric msgdtalen)
	{
		m_msgdtalen = msgdtalen;
	}
	public void setMsgid(FixedChar msgid)
	{
		m_msgid = msgid;
	}
	public void setMsgf(FixedChar msgf)
	{
		m_msgf = msgf;
	}
	public void setMsgflib(FixedChar msgflib)
	{
		m_msgflib = msgflib;
	}
	
	public void setMsgq(String msgq)
	{
		m_msgq = Application.trimr(msgq);
	}
	
	public void setWait(int wait)
	{
		m_wait = wait;
	}
	public void setWait(INumeric wait)
	{
		setWait(wait.intValue());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// If no message queue is specified, then receive message off of program message queue
		if (m_msgq == null || m_msgq.compareTo("*PGMQ")==0)
		{
			// Find call stack entry
			Application csapp = Pgmmsg.findCallStack(getApp(), m_pgmq, 1);
			// Find program message
			String msgAction;
			if (m_rmv.compareTo("*YES")==0)
				msgAction="*REMOVE";
			else 
				msgAction="*SAME";
			pgmmsg = Pgmmsg.findPgmmsg(csapp, m_msgkey, m_msgtype, msgAction);
		}
		// If a message queue is specified, receive message directly from message queue
		else
		{
			int i = m_msgq.indexOf('/');
			String msgq;
			if (i<0)
				msgq = m_msgq;
			else
				msgq = m_msgq.substring(i+1);
			Queue q = QueueTable.resolveQueue(getApp(), msgq+".msgq");
			pgmmsg = (Pgmmsg)q.receiveObject(m_wait);
		}
		
		// Set the program message variables with the returned values
		if (pgmmsg != null)
		{
			// The KEYVAR is actually the hash code of the Pgmmsg object
			if (m_keyvar != null)
				m_keyvar.setBinary(pgmmsg.hashCode(), 0, 4);
			// Message (MSG) -- the actual formatted message with substitution values
			if (m_msg != null)
				m_msg.movel(pgmmsg.m_msg);
			// Message length (MSGLEN)
			if (m_msglen != null)
				m_msglen.assign(pgmmsg.m_msg.length());
			// Message data (MSGDTA)
			if (m_msgdta != null)
				m_msgdta.movelFixedData(pgmmsg.m_msgdta);
			// Message data length (MSGDTALEN)
			if (m_msgdtalen != null)
				m_msgdtalen.assign(pgmmsg.m_msgdta.size());
			// Message ID (MSGDID)
			if (m_msgid != null)
				m_msgid.movel(pgmmsg.m_msgid);
			// Message file (MSGF)
			if (m_msgf != null)
				m_msgf.assign(pgmmsg.m_msgf);
			// Message file library -- always *LIBL
			if (m_msgflib != null)
				m_msgf.assign("*LIBL");
		}
	}
}
