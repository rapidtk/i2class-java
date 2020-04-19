/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * DLTF (delete file) processing.
 * 
 */
public class Dltobj extends AbstractCommand {

	private String m_obj, m_objtype;
	private Statement m_stmt;
	
	private static final String[] TABLE_TYPES={"TABLE", "VIEW"};
	
	private Sndpgmmsg sndpgmmsg;
	private FixedChar m_msgdta;
	
	/**
	 * 
	 */
	public Dltobj(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ", "OBJTYPE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setObj(String obj) {
		m_obj = Application.trimr(obj);
	}
	public void setObjtype(String objtype) {
		m_objtype = Application.trimr(objtype);
	}

	
	/** Delete the specified obj. */
	public void exec(String obj, String objtype) throws Pgmmsg {
		setObj(obj);
		setObjtype(objtype);
		exec();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Pgmmsg {
		// Extract schema, obj name
		String schemaPattern,schemaName, objPattern;
		int i = m_obj.indexOf('/');
		if (i>0)
		{
			schemaPattern = m_obj.substring(0, i);
			schemaName = schemaPattern + '.';
			objPattern=m_obj.substring(i+1);
		}
		else
		{
			schemaPattern=null;
			schemaName="";
			objPattern=m_obj;
		}
		if (objPattern.equals("*ALL"))
			objPattern="*";
	
		// Create message object to use for all commands
		sndpgmmsg = new Sndpgmmsg(getApp());
		m_msgdta=new FixedChar(27);
		m_msgdta.setFixedAt(0, new FixedChar(10, objPattern));
		m_msgdta.setFixedAt(10, new FixedChar(10, schemaPattern));
		m_msgdta.setFixedAt(20, new FixedChar(7, m_objtype.substring(1))); // Don't include '*' with object type
		sndpgmmsg.setMsgdta(m_msgdta);
		// Create statement to use for DELETEs
		try
		{
			m_stmt = getStatement();
			// Build list of objs to delete
			boolean generic = (objPattern.indexOf('*')>=0);
			if (generic)
				objPattern = objPattern.replace('*','%');
			// Delete the *FIRST member (obj name without ())
			else
			{
				deleteFile(schemaPattern, objPattern);
				// Delete any addtional members of obj
				objPattern = objPattern + "(%";
			}
			// Create object suffix (e.g. ".msgf" for message file (*MSGF) objects)
			boolean allTypes=m_objtype.equals("*ALL");
			// TODO drop views first before files for OBJTYPE(*ALL)?
			boolean fileType=m_objtype.equals("*FILE");
			if (!allTypes && !fileType)
				objPattern = objPattern + '.' + m_objtype.substring(1).toLowerCase();;
			DatabaseMetaData dbmd = getConnection().getMetaData();
			ResultSet rsTables = dbmd.getTables(null, schemaPattern, objPattern, TABLE_TYPES);
			try
			{
				if (rsTables.next())
				{
					do 
					{
						String tableName = rsTables.getString(3);
						// Make sure to only delete the specified object type
						if (generic && fileType)
						{
							int j = tableName.lastIndexOf('.');
							// If there is a object type suffix, it will be in lower case
							if (j>0)
							{
								String suffix = tableName.substring(j+1);
								// If the types don't match (this is an object type other than *FILE), then skip object
								if (!suffix.equals(suffix.toUpperCase()))
									continue;
							}
						}
						deleteFile(rsTables.getString(2), rsTables.getString(3));
					} while (rsTables.next());
				}
				else
				{
					// CPF2125 -- No objects deleted
					if (generic)
						sndpgmmsg.exec("CPF2125", "QCPFMSG");
					// CPF2105 -- Object &1 in &2 type *&3 not found.
					else
						sndpgmmsg.exec("CPF2105", "QCPFMSG");
				}
			}
			finally
			{
				rsTables.close();
			}
		}
		catch (Exception e)
		{
		}
	}
	
	// Delete a single file
	private void deleteFile(String schemaPattern, String tableName) throws Exception
	{
		m_msgdta.setFixedAt(0, new FixedChar(10, tableName));
		m_msgdta.setFixedAt(10, new FixedChar(10, schemaPattern));
		String schemaName = schemaPattern + '.';
		//String ddl = "DROP TABLE " + schemaName + ".\"" + tableName + '"';
		try {
			//m_stmt.execute(ddl);
			getRconn().deleteTable(new QfileName(null, schemaName, tableName));
		// Error occurred deleting specific file
		// CPF3219 -- Cannot delete file or member of file &1 in &2
		} catch (SQLException e) {
			I2Logger.logger.info(e);
			String sqlState = e.getSQLState();
			if (sqlState.compareTo("42704")==0) // An undefined object or constraint name was detected
				sndpgmmsg.exec("CPF2105", "QCPFMSG");
			else
				throw e;
		}
		// If we get here then the file has been deleted
		// CPC2191 - Object &1 in &2 type *&3 deleted
		sndpgmmsg.exec("CPC2191", "QCPFMSG");
	}
}
