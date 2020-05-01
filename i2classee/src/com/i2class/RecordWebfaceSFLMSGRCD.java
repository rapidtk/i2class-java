/*
 * Created on Feb 17, 2005
 *
 */
package com.i2class;

import java.sql.SQLException;

import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;

/**
 * A special record class to deal with subfile message records (those with the SFLPGMQ keyword specified).
 * 
 *
 */
public class RecordWebfaceSFLMSGRCD extends RecordWebfaceSFL {

	private String m_sflpgmq;

	/**
	 * @param fmtName
	 */
	public RecordWebfaceSFLMSGRCD(String fmtName) {
		super(fmtName);
		// TODO Auto-generated constructor stub
	}
	
	public void setSubfileProgramQueueFieldName(String name)
	{
		m_sflpgmq = getWebfaceName(name);
	}

	/** Initialize subfile. */
	public void sflinz()
	{
		sfl_.sflclr();
		Application app = file.app;
		if (app.pgmMsgs != null)
		{
			int count = app.pgmMsgs.size();
			try {
				for (int i=0; i<count; i++)
				{
					Pgmmsg msg = (Pgmmsg)file.app.pgmMsgs.elementAt(i);
					String msgText = msg.m_msg;
					setRRN(i+1);
					setText(1, msgText); // The message data is always the first field in the format
				}
			} 
			catch (SQLException e) {}
			app.pgmMsgs.clear();
		}
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RecordWebface#getEditedFieldValue(java.lang.String, com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition)
	 */
	protected String getEditedFieldValue(
		String fieldName,
		FieldViewDefinition fvd) {
		// The only value ever returned in subfile message records is MSGDATA
		// MSGDATA is the 'fake' (it's not defined by the record format, but instead by webfacing) field
		// that contains the formatted message data.
		return getString(1);
	}


}
