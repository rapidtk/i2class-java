/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * ADDPFM (add physical file member) processing.
 * 
 */
public class Addpfm extends AbstractCommand {

	private String m_file, m_mbr;
	
	public Addpfm() {}
	public Addpfm(I2Connection rconn) {
		super(rconn);
	}
	
	static final String[] PARM_NAMES={"FILE", "MBR"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setFile(String file) {
		m_file = Application.trimr(file);
	}

	public void setMbr(String mbr) {
		m_mbr = Application.trimr(mbr);
	}
	
	/** Add a member to the specified file. */
	public void exec(Object file, Object mbr) throws Exception {
		setFile(file.toString());
		setMbr(mbr.toString());
		exec();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		I2Connection rconn = getRconn();
		QfileName likeFile = rconn.qfileName(m_file);
		QfileName q = new QfileName(likeFile.libName, likeFile.schemaName, '(' + likeFile.fileName + ')');

		FixedChar msgdta = new FixedChar(30);
		msgdta.setFixedAt(0, new FixedChar(10, m_mbr));
		msgdta.setFixedAt(10, new FixedChar(10, q.fileName));
		msgdta.setFixedAt(20, new FixedChar(10, q.libName));
		try {
			rconn.createTableLike(q, likeFile);
		} catch (SQLException e) {
			I2Logger.logger.info(e);
			// CPF7306 -- Member &1 not added to file &2 in &3
			throw new Pgmmsg("CPF7306", "QCPFMSG", msgdta);
		} 
		// CPC7305 -- Member &1 added to file &2 in &3
		new Sndpgmmsg(getApp()).completion("CPC7305", "QCPFMSG", msgdta);
	}
	

}
