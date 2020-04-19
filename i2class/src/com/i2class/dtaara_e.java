package com.i2class;

import java.sql.Connection;

import com.ibm.as400.access.AS400;

/**
 * An externally-described data area.
 *
 */
public class dtaara_e extends ExternalDtaara {

	public dtaara_e(IRecordFormat rcdFmt, AS400 system, String dtaaraName) {
		super(rcdFmt, system, dtaaraName);
	}
	public dtaara_e(IRecordFormat rcdFmt, I2AS400 system, String dtaaraName) {
		super(rcdFmt, system, dtaaraName);
	}

	public dtaara_e(IRecordFormat rcdFmt, Connection host, String dtaaraName) {
		super(rcdFmt, host, dtaaraName);
	}

	public dtaara_e(IRecordFormat rcdFmt, I2Connection rconn, String dtaaraName) {
		super(rcdFmt, rconn, dtaaraName);
	}

}
