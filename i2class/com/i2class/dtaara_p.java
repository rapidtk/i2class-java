package com.i2class;

import java.sql.Connection;

import com.ibm.as400.access.AS400;

/**
 * An interface to a packed decimal TYPE(*DEC) data area.
 * @see CharDtaara
 * @see PackedDecimal
 */
public class dtaara_p extends PackedDtaara {

	public dtaara_p(int sz, int precision, AS400 system, String dtaaraName) {
		super(sz, precision, system, dtaaraName);
	}

	public dtaara_p(int sz, int precision, Connection host, String dtaaraName) {
		super(sz, precision, host, dtaaraName);
	}

	public dtaara_p(int sz, int precision, I2Connection rconn, String dtaaraName) {
		super(sz, precision, rconn, dtaaraName);
	}

}
