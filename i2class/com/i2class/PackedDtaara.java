package com.i2class;

import java.sql.Connection;
/**
 * A class to manipulate packed decimal i.e. TYPE(*DEC) data areas stored on an OS/400 host.
 * @see packed
 *  
 */
import com.ibm.as400.access.*;
import java.math.*;

/**
 * An interface to a packed decimal TYPE(*DEC) data area.
 * @see CharDtaara
 * @see PackedDecimal
 * 
 * 
 */
public class PackedDtaara extends PackedDecimal {

private IDecimalDtaara dtaara;

/** Create a remote packed decimal data area object. */
public PackedDtaara(int sz, int precision, AS400 system, String dtaaraName)
{
	super(sz, precision);
	dtaara = getDtaara(system, dtaaraName);
}
public PackedDtaara(int sz, int precision, I2AS400 system, String dtaaraName)
{
	this(sz, precision, system.getAS400(), dtaaraName);
}
/** Create a local decimal data area object. */
public PackedDtaara(int sz, int precision, Connection host, String dtaaraName)
{
	super(sz, precision);
	dtaara = new DecimalDataAreaFile(dtaaraName);
}
/** Create a packed decimal data area object on a JDBC host. */
public PackedDtaara(int sz, int precision, I2Connection rconn, String dtaaraName)
{
	super(sz, precision);
	try
	{
		dtaara = new DecimalDataAreaJDBC(rconn, dtaaraName);
	}
	catch (Exception e)
	{
		I2Logger.logger.printStackTrace(e);
	}
}

/**
 * Changed a packed value in the specified data area.
 * @param host The OS/400 host where the data area resides.
 * @param dtaara The lib/name of the data area to retrieve data from.
 * @param value The value to set
 */
public static void chgdtaara(AS400 host, String dtaara, double value) throws Exception
{
	DecimalDataArea pda = getDtaara(host, dtaara);
	pda.write(ShortDecimal.newBigDecimal(value));
}
/**
 * Changed a packed value in the specified data area.
 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
 * @param dtaara The directory/name of the data area to retrieve data from.
 * @param value The value to set
 */
public static void chgdtaara(Connection host, String dtaara, double value) throws Exception
{
	String s = Double.toString(value);
	CharDtaara.chgdtaara(host, dtaara, s);
}
/**
 * Get an instance of the specified (packed decimal) data area from the OS/400 host
 * @param host The OS/400 host to connect to
 * @param dtaaraName The name of the data area
 * @return the specified data area instance
 */
private static DecimalDataArea400 getDtaara(AS400 host, String dtaaraName) {
	String path = Application.getPath(dtaaraName, "DTAARA");
	return new DecimalDataArea400(host, path);
}
/** Retrieve the BigDecimal value from the data area into this object. */
public void in() throws Exception
{
	BigDecimal bd=dtaara.read();
	assign(bd);
}
/** Write the BigDecimal value to the data area from this object. */
public void out() throws Exception
{
	BigDecimal bd=toBigDecimal();
	dtaara.write(bd);
}
/**
 * Retrieve a packed decimal value from the specified data area.
 * @param host AS400 The OS/400 host that contains that specified data area.
 * @param dtaara java.lang.String The name of the data area to retrieve data from.
 * @return com.asc.rio.packed The returned data
 */
public static BigDecimal rtvdtaara(AS400 host, String dtaara) throws Exception
{
	DecimalDataArea pda = getDtaara(host, dtaara);
	BigDecimal bd = pda.read();
	return bd;
}
/**
 * Retrieve a packed decimal value from the specified data area.
 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
 * @param dtaara The name of the data area to retrieve data from.
 * @return the returned decimal value
 */
public static BigDecimal rtvdtaara(Connection host, String dtaara) throws Exception
{
	String value = CharDtaara.rtvdtaara(host, dtaara);
	BigDecimal bd = ShortDecimal.newBigDecimal(value);
	return bd;
}
}
