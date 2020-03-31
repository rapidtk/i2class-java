package com.i2class;

import java.sql.SQLException;

/**
 * An interface to a generic host (either AS400 or JDBC Connection) object
 * @author ANDREWC
 *
 */
public interface IHost {
	// Close the connection to the host
	public void close() throws SQLException;
}
