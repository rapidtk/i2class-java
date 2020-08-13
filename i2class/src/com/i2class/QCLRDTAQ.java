/*
 * Created on Aug 13, 2020
 *
 */
package com.i2class;

import java.io.IOException;
import java.io.OptionalDataException;
import java.sql.SQLException;

/**
 * Clear data queue API.
 * 
 */
public class QCLRDTAQ extends Application {
	
	public QCLRDTAQ(Application app) throws Exception {
	   super(app);
	}
	
	/**
	 * Clear data from the specified queue.
	 * @param dtaq Data queue fixed(10).  
	 * @param dtaqLib Data queue library fixed(10).  
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws OptionalDataException 
	 */
	public void call(FixedChar dtaq, FixedChar dtaqLib) throws OptionalDataException, ClassNotFoundException, SQLException, IOException
	{
		Queue q = QSNDDTAQ.resolveDataQueue(this, dtaq);
		q.clear();
	 }
	
	public static QCLRDTAQ activate(Application app, QCLRDTAQ instance) throws Exception {
		if (instance == null)
			instance = new QCLRDTAQ(app);
		return instance;
	}

}
