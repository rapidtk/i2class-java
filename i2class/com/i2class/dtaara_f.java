package com.i2class;

import java.sql.Connection;
import com.ibm.as400.access.*;

/** 
 * Fixed-length data stored in a data area.
 * A data area is like a lightweight file that contains a single field and record.
 * They can be used to store data that is easily manipulated without the
 * overhead associated with a database file.
 */
public class dtaara_f extends fixed
{

	protected ICharacterDtaara dtaara;
	/** Create a remote data area object. */
	public dtaara_f(int sz, AS400 system, String dtaaraName)
	{
		super(sz);
		dtaara = getDtaara(system, dtaaraName);
	}
	/** Create a local data area object. */
	public dtaara_f(int sz, Connection host, String dtaaraName)
	{
		super(sz);
		dtaara = getDtaara(dtaaraName);
	}
	/** Create a remote (JDBC) data area object. */
	public dtaara_f(int sz, I2Connection rconn, String dtaaraName)
	{
		super(sz);
		dtaara = getDtaara(rconn, dtaaraName);
	}

	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		AS400 host,
		String dtaara,
		int start,
		int length,
		char value)
		throws Exception
	{
		String s = String.valueOf(value);
		chgdtaara(host, dtaara, start, length, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		AS400 host,
		String dtaara,
		int start,
		int length,
		fixed value)
		throws Exception
	{
		String s = value.toFixedString();
		chgdtaara(host, dtaara, start, length, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		AS400 host,
		String dtaara,
		int start,
		int length,
		String value)
		throws Exception
	{
		ICharacterDtaara cda = getDtaara(host, dtaara);
		// If the specified String is longer than the specified length, then substring
		if (value.length() > length)
			value = value.substring(0, length);
		cda.write(value, start - 1);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param value The value to set
	 */
	public static void chgdtaara(AS400 host, String dtaara, fixed value)
		throws Exception
	{
		String s = value.toFixedString();
		chgdtaara(host, dtaara, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param value The value to set
	 */
	public static void chgdtaara(AS400 host, String dtaara, String value)
		throws Exception
	{
		ICharacterDtaara cda = getDtaara(host, dtaara);
		cda.write(value);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		Connection host,
		String dtaara,
		int start,
		int length,
		char value)
		throws Exception
	{
		String s = String.valueOf(value);
		chgdtaara(host, dtaara, start, length, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		Connection host,
		String dtaara,
		int start,
		int length,
		fixed value)
		throws Exception
	{
		String s = value.toFixedString();
		chgdtaara(host, dtaara, start, length, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param start The 1-based start position of the substring to return
	 * @param length The length of the substring to return
	 * @param value The value to set
	 */
	public static void chgdtaara(
		Connection host,
		String dtaara,
		int start,
		int length,
		String value)
		throws Exception
	{
		ICharacterDtaara daf = getDtaara(dtaara);
		// If the specified String is longer than the specified length, then substring
		if (value.length() > length)
			value = value.substring(0, length);
		daf.write(value, start - 1);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param value The value to set
	 */
	public static void chgdtaara(Connection host, String dtaara, fixed value)
		throws Exception
	{
		String s = value.toFixedString();
		chgdtaara(host, dtaara, s);
	}
	/**
	 * Change a fixed-length value in the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param value The value to set
	 */
	public static void chgdtaara(Connection host, String dtaara, String value)
		throws Exception
	{
		ICharacterDtaara daf = getDtaara(dtaara);
		daf.write(value);
	}
	/**
	 * Get an instance of the specified data area from the OS/400 host
	 * @param host The OS/400 host to connect to
	 * @param dtaaraName The name of the data area
	 * @return the specified data area instance
	 */
	private static ICharacterDtaara getDtaara(AS400 host, String dtaaraName)
	{
		ICharacterDtaara cda;
		if (dtaaraName.compareTo("*LDA") == 0)
			cda = new LocalDataArea400(host);
		else
		{
			String path = Application.getPath(dtaaraName, "DTAARA");
			cda = new CharacterDataArea400(host, path);
		}
		return cda;
	}
	/**
	 * Get an instance of the specified data area from the OS/400 host
	 * @param dtaaraName The name of the data area
	 * @return the specified data area instance
	 */
	private static ICharacterDtaara getDtaara(String dtaaraName)
	{
		ICharacterDtaara daf;
		if (dtaaraName.compareTo("*LDA") == 0)
			daf = new LocalDataAreaFile();
		else
			daf = new CharacterDataAreaFile(dtaaraName);
		return daf;
	}

	/**
	 * Get an instance of the specified data area from a JDBC database.
	 * @param host The JDBC host to connect to
	 * @param dtaaraName The name of the data area
	 * @return the specified data area instance
	 */
	private static ICharacterDtaara getDtaara(I2Connection rconn, String dtaaraName)
	{
		ICharacterDtaara cda=null;
		try
		{
			cda = new CharacterDataAreaJDBC(rconn, dtaaraName);
		}
		catch (Exception e)
		{
			I2Logger.logger.printStackTrace(e);
		}
		return cda;
	}

	/**
	 * Read data from the remote data area and store into this object.
	 */
	public void in() throws Exception
	{
		String s = dtaara.read();
		movel(s);
	}

	/**
	 * Write data from this object to the remote data area.
	 */
	public void out() throws Exception
	{
		String s = toFixedString();
		dtaara.write(s);
	}

	/**
	 * Retrieve a fixed-length value from the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @return fixed The returned data
	 */
	public static String rtvdtaara(AS400 host, String dtaara) throws Exception
	{
		ICharacterDtaara cda = getDtaara(host, dtaara);
		String value = cda.read();
		return value;
	}

	/**
	 * Retrieve a fixed-length value substring from the specified data area.
	 * @param host The OS/400 host where the data area resides.
	 * @param dtaara The lib/name of the data area to retrieve data from.
	 * @param start The 1-based start position of the substring to return
	 * @param length The length of the substring to return
	 * @return com.asc.rio.fixed The returned data
	 */
	public static String rtvdtaara(
		AS400 host,
		String dtaara,
		int start,
		int length)
		throws Exception
	{
		ICharacterDtaara cda = getDtaara(host, dtaara);
		String value = cda.read(start - 1, length);
		return value;
	}
	/**
	 * Retrieve a fixed-length value from the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @return the returned data
	 */
	public static String rtvdtaara(Connection host, String dtaara)
		throws Exception
	{
		ICharacterDtaara daf = getDtaara(dtaara);
		String s = daf.read();
		return s;
	}
	/**
	 * Retrieve a fixed-length value substring from the specified data area.
	 * @param host The JDBC host where the data area resides.  Null is an acceptable value.
	 * @param dtaara The directory/name of the data area to retrieve data from.
	 * @param start The start position of the substring to return
	 * @param length The length of the substring to return
	 * @return the returned data
	 */
	public static String rtvdtaara(
		Connection host,
		String dtaara,
		int start,
		int length)
		throws Exception
	{
		ICharacterDtaara daf = getDtaara(dtaara);
		String s = daf.read(start - 1, length);
		return s;
	}
}
