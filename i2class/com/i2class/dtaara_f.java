package com.i2class;

import java.sql.Connection;

import com.ibm.as400.access.AS400;

/** 
 * An interface to a character TYPE(*CHAR) data area.
 */
public class dtaara_f extends CharDtaara {

	public dtaara_f(int sz, AS400 system, String dtaaraName) {
		super(sz, system, dtaaraName);
	}
	public dtaara_f(int sz, I2AS400 system, String dtaaraName) {
		super(sz, system, dtaaraName);
	}

	public dtaara_f(int sz, Connection host, String dtaaraName) {
		super(sz, host, dtaaraName);
	}

	public dtaara_f(int sz, I2Connection rconn, String dtaaraName) {
		super(sz, rconn, dtaaraName);
	}

}
