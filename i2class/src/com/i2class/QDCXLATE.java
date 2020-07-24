/*
 * Created on May 4, 2005
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

/**
 * Convert data API
 * 
 */
public class QDCXLATE extends Application {
	
	public QDCXLATE(Application app) throws Exception {
	   super(app);
	}
	
	/**
	 * Translates data using a translation table.  This API only currently supports translation of lower to 
	 * upper case through the use of the QSYSTRNTBL table.
	 * @param dataLength the packed(5,0) length of data to translate
	 * @param data the actual data to translate (both input and output)
	 * @param tableName the name of the table used for translation.  Only "QSYSTRNTBL" is allowed.
	 * @throws Pgmmsg
	 */
	public void call(PackedDecimal dataLength, FixedChar data, FixedChar tableName) throws Pgmmsg  {
	 	String s = data.subst(1, dataLength.intValue());
	 	// Translate data
	 	if (tableName.compareTo("QSYSTRNTBL")==0)
	 		s = s.toUpperCase();
	 	else
	 		throw new Pgmmsg("CPF2619", "QCPFMSG", tableName);
	 	data.movel(s);
	}

	public static QDCXLATE activate(Application app, QDCXLATE instance) throws Exception {
		if (instance == null)
			instance = new QDCXLATE(app);
		return instance;
	}

}
