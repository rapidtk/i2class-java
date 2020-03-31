package com.i2class;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * A queue that is backed by a SQL table containing its serialized form.
 * 
 * @author ANDREWC
 *
 */
public class QueueTable extends Queue implements Serializable {

	
	private String m_queueName;
	private transient ResultSet m_rs;
	static private HashMap m_cachedQueues = new HashMap();
	
	/** Create a queue object that represents the data queue on the specified host. */
	public QueueTable(String queueName, ResultSet rs)
	{
		m_queueName=queueName;
		m_rs=rs;
	}
	
	/** Flush the contents of this (in-memory) data queue to disk. */
	public void flush() throws SQLException, IOException
	{	
		PipedOutputStream pos = new PipedOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(pos);
		PipedInputStream pis = new PipedInputStream(pos);
		m_rs.updateBinaryStream(1, pis, 1000000);
		m_rs.updateRow();
		// Since we've now flushed the data, reset to first (only) record
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		flush();
		m_rs.close();
		super.finalize();
	}

	static QueueTable resolveQueue(Application app, String qFile) throws OptionalDataException, SQLException, ClassNotFoundException, IOException
	{
		Connection conn = app.prvApp().defaultI2Connection();
		return resolve(conn, qFile);
	}

	/** Create a data queue object that represents the data queue on the specified host. */
	public static QueueTable resolve(Connection conn, String queueFileName) throws SQLException, OptionalDataException, ClassNotFoundException, IOException
	{
		// See if this queue has already been cached
		QueueTable dq=(QueueTable)QueueTable.m_cachedQueues.get(queueFileName);
		if (dq==null)
		{
			Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery("SELECT * FROM \"" + queueFileName + '"');
			rs.next();
			InputStream is = rs.getBinaryStream(1);
			if (is!=null)
			{
				ObjectInputStream in = new ObjectInputStream(is);
				dq = (QueueTable)in.readObject();
				dq.m_rs = rs;
			}
			else
				dq = new QueueTable(queueFileName, rs);
			// Add data queue to cache
			QueueTable.m_cachedQueues.put(queueFileName, dq);
		}
		return dq;
	}
	
}
