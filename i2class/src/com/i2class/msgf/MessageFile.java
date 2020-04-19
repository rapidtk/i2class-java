/*
 * Created on Nov 29, 2004
 */
package com.i2class.msgf;

import java.util.*;

import com.i2class.*;


/**
 * A message file class.
 * 
 */
public class MessageFile extends java.util.ResourceBundle {
	
	//Hashtable m_messages = new Hashtable();
	Hashtable m_messages = new Hashtable();

	/** Add a message description to this message file. */
	public void addMsgd(String msgid, String msg, String seclvl, int severity)
	{
		m_messages.put(msgid, new MessageDescription(msgid, msg, seclvl, severity));
	}

	/* (non-Javadoc)
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	protected Object handleGetObject(String key) throws MissingResourceException {
		return m_messages.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.ResourceBundle#getKeys()
	 */
	public Enumeration getKeys() {
		return m_messages.keys();
	}

}
