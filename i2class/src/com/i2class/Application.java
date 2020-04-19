package com.i2class;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.QSYSObjectPathName;
/**
 * An I2-translated application that adds OS/400 application attributes to a Java executable thread.
 * Specifically, this class adds support for RPG 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508405.htm#HDRRPGWORD">special values</a> (for example *LOVAL, UDATE), 
 * built-in-functions (<a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508429.htm#HDRBIFS">%BIFs</a>), 
 * and <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508415.htm#HDRDTDF">data types</a> (for example indicators).
 * <p>
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508408.htm#HDRINDICA9">Indicators</a> are like boolean values in Java and C++.  
 * Instead of true and false, however, indicators hold the character values '1' (*ON) and '0' (*OFF).  
 * All RPG programs have implied declarations for the variables *IN01 (indicator 01) - *IN99 that can be used 
 * for general-purpose programming; these values can also be addressed as an array by using the special array *IN() (for example *IN(1) is equivalent to *IN01).  
 * There are other indicators that have special meaning, the most important of these *INLR is essentially used 
 * to tell the OS that the program has completed and that all resources should be cleaned up 
 * (similar to setting an object variable to null or using the delete operator).  
 * The values *INOA - *INOG and *INOV are used to indicate ‘overflow’ conditions when records of data are 
 * written to green-bar style reports.  
 * The indicators *INKA-*INKN and *INKP-*INKZ represent the 24 function keys that can be pressed on a 5250 terminal.
 * 
 * 
 */
public class Application implements Runnable, Serializable
{
	/**
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> 
	 * *BLANKS - all blanks.  For example, when applied to a fixed-length CHAR 3 field, 
	 * BLANKS would mean '   ' (three blank characters).  
	 * When applied to a CHAR 5 field, it would mean '     ' (five blank characters).
	 */
	public static final FigConst BLANKS = new FigConst(' ');
	/** 
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *OFF - all zeros.  
	 * When applied to an indicator (usually the only place that they are used), *OFF means '0' (false)
	 */
	public static final boolean OFF = false;
	/** 
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *ON - all ones.  
	 * When applied to an indicator (usually the only place that they are used), *ON means '1' (true)
	 */
	public static final boolean ON = true;
	/**
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *HIVAL - the largest value that a field can hold.  
	 * For example, when applied to a fixed-length CHAR 3 field, *HIVAL would mean 0xFFFFFF.  
	 * When applied to a zoned decimal 3,0 number, *HIVAL would mean 999.
	 */
	public static final Hival HIVAL = new Hival();
	/**
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *LOVAL - the smallest value that a field can hold.  
	 * For example, when applied to a fixed-length CHAR 3 field, *LOVAL would mean 0x000000.  
	 * When applied to a zoned decimal 3,0 number, *LOVAL would mean -999.
	 */
	public static final  Loval LOVAL = new Loval();
	/**
	 * The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *ZEROS - all zeros.  
	 * For example, when applied to a fixed-length CHAR 3 field, *ZEROS would mean '000'.  
	 * When applied to a numeric field, *ZEROS means 0.
	 */
	public static final Zeros ZEROS = new Zeros();
	/**
	* The <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508413.htm#HDRSPFIG">figurative constant</a> *ONES - all ones.  
	* For example, when applied to a fixed-length CHAR 3 field, *ONES would mean '111'.  
	* When applied to a numeric field, *ONES means 1.
	*/
	static final FigConst ONES = new FigConst('1');


	// File constants
	/** Open a file for read-only access (no updates are allowed). */
	public static final int READ_ONLY = /*com.ibm.as400.access.AS400File.READ_ONLY*/0;
	/** Open a file for read-write access (reads and updates are allowed). */
	public static final int READ_WRITE =
		/*com.ibm.as400.access.AS400File.READ_WRITE*/1;
	/** Open a file for write-only access (no reads are allowed). */
	public static final int WRITE_ONLY =
		/*com.ibm.as400.access.AS400File.WRITE_ONLY*/2;
		
	/** Do not open the file under commitment control. */
	public static final int COMMIT_LOCK_LEVEL_NONE =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_NONE*/3;
	/** Open a file under the default commitment control level (this is typically none). */
	public static final int COMMIT_LOCK_LEVEL_DEFAULT =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_DEFAULT*/4;
		
	/**
	 * Constant indicating a commit lock level of *CHANGE, the lowest level of transaction isolation. 
	 * Every record read for update is locked. 
	 * If a record is updated, added, or deleted, that record remains locked until the transaction is committed or rolled back. 
	 * Records that are accessed for update but are released without being updated are unlocked.
	 * <p>
	 * Uncommitted changes from other processes (dirty reads) are visible.
	 * The contents of the same record read more than once (repeatable-read) may not be the same.
	 * Inserts by other processes (phantom reads) are visible.   
	 *  
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED 
	 */
	public static final int COMMIT_LOCK_LEVEL_CHANGE =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_ALL*/1;
		
	/**
	 * Constant indicating a commit lock level of *CS. 
	 * Every record accessed is locked. 
	 * Records that are not updated or deleted are locked only until a different record is accessed. 
	 * Records that are updated, added, or deleted are locked until the transaction is committed or rolled back. 
	 * <p>
	 * Uncommitted changes from other processes (dirty reads) are not visible.
	 * The contents of the same record read more than once (repeatable-read) may not be the same.
	 * Inserts by other processes (phantom reads) are visible.   
	 * 
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED 
	 */	
	public static final int COMMIT_LOCK_LEVEL_CURSOR_STABILITY =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_ALL*/2;
		
	/** 
	 * Constant indicating a commit lock level of *ALL, the highest isolation level available to non-SQL programs. 
	 * Every record accessed in the file is locked until the transaction is committed or rolled back.
	 * <p>
	 * Uncommitted changes from other processes (dirty reads) are not visible.
	 * The contents of the same record read more than once (repeatable-read) is always the same.
	 * Inserts by other processes (phantom reads) are visible.   
	 * 
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ 
	 */
	public static final int COMMIT_LOCK_LEVEL_ALL =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_ALL*/0;

		
	/** 
	 * The highest level of commitment control in a Java process.
	 * Every record accessed in the file is locked until the transaction is committed or rolled back.
	 * Tables opened at this level are locked so that no inserts or updates can be made by another process.
	 * <p>
	 * Uncommitted changes from other processes (dirty reads) are not visible .
	 * The contents of the same record read more than once (repeatable-read) is always the same.
	 * Inserts by other processes (phantom reads) are not visible.
	 *    
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE 
	 * */
	public static final int COMMIT_LOCK_LEVEL_SERIALIZABLE =
		/*com.ibm.as400.access.AS400File.COMMIT_LOCK_LEVEL_NONE*/10;
	


	// Date constants
	/** The defautlt OS/400 date style. */
	protected static String _DATFMT = "*ISO";
	/** The 'Java-style' equivalent of _DATFMT. */	
	static String dateFormat="yyyy-MM-dd"; 
	private String udateFormat, longUdateFormat, utimestampFormat, timestampFormat;

	/** The defautlt OS/400 time style. */
	protected static String _TIMFMT = "*ISO";
	/** The 'Java-style' equivalent of _TIMFMT. */	
	static String timeFormat="HH.mm.ss";
	/* Default OS/400 decimal edit. */  
	protected String _DECEDIT = ".";
	/** Default OS/400 currency symbol. */
	protected char CURSYM = '$';
	
	/** 
	 * The 8-digit representation of the current date (for example 20031231 is the last day of the year 2003). 
	 * The format of the date depends upon the date format that this Application object was created with. 
	 */
	/* The RPG value *MS that represents the microsecond portion of a time value. */
	static final public int MSECONDS = Calendar.MILLISECOND;
	static final public int SECONDS = Calendar.SECOND;
	static final public int MINUTES = Calendar.MINUTE;
	static final public int HOURS = Calendar.HOUR_OF_DAY;
	static final public int DAYS = Calendar.DAY_OF_MONTH;
	static final public int MONTHS = Calendar.MONTH;
	static final public int YEARS = Calendar.YEAR;
	// These have to be public because inner classes (e.g. output()) don't have access to protected data
	//static public date TIMESTAMP /*= new FmtDate(14, "HHmmssMMddyyyy")*/;
	
	/** The 6-digit HHMMSS military representation of the current time. */
	static final public FmtTime TIME = new FmtTime(6, "HHmmss");
	//static public date UTIMESTAMP /*= new FmtDate(12, "HHmmssMMddyy")*/;
	
	/** 
	 * The 14-digit representation of the current time and date (TIME + DATE).
	 * @see #TIME
	 * @see #DATE
	 */
	public FmtTime TIMESTAMP /*= new FmtDate(14, "HHmmssMMddyyyy")*/;
	/** 
	 * The 12-digit representation of the current time and date (TIME + UDATE).
	 * @see #TIME
	 * @see #UDATE
	 */
	public FmtTime UTIMESTAMP /*= new FmtDate(12, "HHmmssMMddyy")*/;

	static public final String DEFAULT_UDATE_FORMAT = "MMddyy";
	static public final String DEFAULT_TIME_FORMAT = "HHmmss";

	static public final FmtDate JOB_DATE = new FmtDate(6, DEFAULT_UDATE_FORMAT);
	
	/*static*/ public FmtDate DATE /* = new FmtDate(8, "MMddyyyy")*/;
	/** 
	 * The 6-digit representation of the current date (for example 031231 is the last day of the year 2003). 
	 * The format of the date depends upon the date format that this Application object was created with. 
	 */
	/*static*/ public FmtDate UDATE /*= new FmtDate(6, "MMddyy")*/;
	/** The 2-digit representation of the current dat. */
	static final public FmtDate UDAY = new FmtDate(2, "dd");
	/** The 2-digit representation of the current month. */
	static final public FmtDate UMONTH = new FmtDate(2, "MM");
	/** The 2-digit representation of the current year. */
	static final public FmtDate UYEAR = new FmtDate(2, "yy");
	/** The 4-digit representation of the current year. */
	static final public FmtDate YEAR = new FmtDate(4, "yyyy");
	
	// Rounding constants 
	/** Round down */
	static final public int ROUND_DOWN = BigDecimal.ROUND_DOWN;
	static final public int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;

	// %BIF constants
	/** *ASTFILL (asterik fill) special value for editc() bif */
	public static final char ASTFILL='*';

	/** Indicates whether the last file record access resulted in an eof or bof condition. */
	boolean eof; // changed from eof to EOF to avoid name conflict, %bif error() is used instead
	/** 
	 * Indicates whether the last file record access, character scan or array lookup was successful.
	 */
	boolean found;
	/** 
	 * Indicates whether the last file record access or array lookup found an exact match.
	 */
	boolean equal;
	/** 
	 * Indicates whether the last opcode caused an error (when a (E) opcode extender is specified).
	 */
	protected boolean ERROR; // changed from error to ERROR to avoid name conflict, %bif error() is used instead



	// Constants for lookup
	private static final int EQ = 0;
	private static final int GE = 1;
	private static final int GT = 2;
	private static final int LE = 3;
	private static final int LT = 4;

	private static GregorianCalendar calendar;
	static int hostCount;
	private static int hostApplicationCount;
	/** All hosts across all Applications */
	static HostPool hostPool = new HostPool(); 
	/** Host connections in this Application */
	Vector appRHosts = new Vector(1);  
	private static ResourceBundle i2properties;
	
	//private static int keyCacheSize;
	/** Boolean flag so that class doesn't get finalized more than once. */
	private boolean finalized;

	
	// Cycle variables
	RfileCycle cycleFile; // The current file for the cycle to read from
	private int cycleFileIndex;
	Vector cycleFiles;
	private Vector cycleFormats;
	private boolean m_firstCycle = true;
	private Vector detailFormats, totalFormats;
	
	/** Open files in this program instance. */
	Vector openFiles;
	/** Map of file ids to overridden file names */
	public Hashtable ovrFiles;
	
	/** PSDS reference */
	FixedChar psds;
	
	// Indicators

	// 'Global' function key indicators
	/*static*/ public boolean INKA,
		INKB,
		INKC,
		INKD,
		INKE,
		INKF,
		INKG,
		INKH,
		INKI,
		INKJ,
		INKK,
		INKL,
		INKM,
		INKN;
	/*static*/ public boolean INKP,
		INKQ,
		INKR,
		INKS,
		INKT,
		INKU,
		INKV,
		INKW,
		INKX,
		INKY,
		INKZ;
	// Job switch indicators
	public boolean INU1, INU2, INU3, INU4, INU5, INU6, INU7, INU8;

	public final class IN_ARRAY extends FixedChar
	{
		private int I;
		IN_ARRAY()
		{
			super(99);
		}
		@Override
		protected void readSubfields()
		{
			I = 0;
			rIN(IN01);
			rIN(IN02);
			rIN(IN03);
			rIN(IN04);
			rIN(IN05);
			rIN(IN06);
			rIN(IN07);
			rIN(IN08);
			rIN(IN09);
			rIN(IN10);
			rIN(IN11);
			rIN(IN12);
			rIN(IN13);
			rIN(IN14);
			rIN(IN15);
			rIN(IN16);
			rIN(IN17);
			rIN(IN18);
			rIN(IN19);
			rIN(IN20);
			rIN(IN21);
			rIN(IN22);
			rIN(IN23);
			rIN(IN24);
			rIN(IN25);
			rIN(IN26);
			rIN(IN27);
			rIN(IN28);
			rIN(IN29);
			rIN(IN30);
			rIN(IN31);
			rIN(IN32);
			rIN(IN33);
			rIN(IN34);
			rIN(IN35);
			rIN(IN36);
			rIN(IN37);
			rIN(IN38);
			rIN(IN39);
			rIN(IN40);
			rIN(IN41);
			rIN(IN42);
			rIN(IN43);
			rIN(IN44);
			rIN(IN45);
			rIN(IN46);
			rIN(IN47);
			rIN(IN48);
			rIN(IN49);
			rIN(IN50);
			rIN(IN51);
			rIN(IN52);
			rIN(IN53);
			rIN(IN54);
			rIN(IN55);
			rIN(IN56);
			rIN(IN57);
			rIN(IN58);
			rIN(IN59);
			rIN(IN60);
			rIN(IN61);
			rIN(IN62);
			rIN(IN63);
			rIN(IN64);
			rIN(IN65);
			rIN(IN66);
			rIN(IN67);
			rIN(IN68);
			rIN(IN69);
			rIN(IN70);
			rIN(IN71);
			rIN(IN72);
			rIN(IN73);
			rIN(IN74);
			rIN(IN75);
			rIN(IN76);
			rIN(IN77);
			rIN(IN78);
			rIN(IN79);
			rIN(IN80);
			rIN(IN81);
			rIN(IN82);
			rIN(IN83);
			rIN(IN84);
			rIN(IN85);
			rIN(IN86);
			rIN(IN87);
			rIN(IN88);
			rIN(IN89);
			rIN(IN90);
			rIN(IN91);
			rIN(IN92);
			rIN(IN93);
			rIN(IN94);
			rIN(IN95);
			rIN(IN96);
			rIN(IN97);
			rIN(IN98);
			rIN(IN99);
		}
		void rIN(boolean b)
		{
			if (b)
				getOverlay()[I] = (byte) '1';
			else
				getOverlay()[I] = (byte) '0';
			I++;
		}
		@Override
		protected void updateSubfields()
		{
			
			byte[] overlay = getOverlay();
			IN01 = (overlay[0] == '1');
			IN02 = (overlay[1] == '1');
			IN03 = (overlay[2] == '1');
			IN04 = (overlay[3] == '1');
			IN05 = (overlay[4] == '1');
			IN06 = (overlay[5] == '1');
			IN07 = (overlay[6] == '1');
			IN08 = (overlay[7] == '1');
			IN09 = (overlay[8] == '1');
			IN10 = (overlay[9] == '1');
			IN11 = (overlay[10] == '1');
			IN12 = (overlay[11] == '1');
			IN13 = (overlay[12] == '1');
			IN14 = (overlay[13] == '1');
			IN15 = (overlay[14] == '1');
			IN16 = (overlay[15] == '1');
			IN17 = (overlay[16] == '1');
			IN18 = (overlay[17] == '1');
			IN19 = (overlay[18] == '1');
			IN20 = (overlay[19] == '1');
			IN21 = (overlay[20] == '1');
			IN22 = (overlay[21] == '1');
			IN23 = (overlay[22] == '1');
			IN24 = (overlay[23] == '1');
			IN25 = (overlay[24] == '1');
			IN26 = (overlay[25] == '1');
			IN27 = (overlay[26] == '1');
			IN28 = (overlay[27] == '1');
			IN29 = (overlay[28] == '1');
			IN30 = (overlay[29] == '1');
			IN31 = (overlay[30] == '1');
			IN32 = (overlay[31] == '1');
			IN33 = (overlay[32] == '1');
			IN34 = (overlay[33] == '1');
			IN35 = (overlay[34] == '1');
			IN36 = (overlay[35] == '1');
			IN37 = (overlay[36] == '1');
			IN38 = (overlay[37] == '1');
			IN39 = (overlay[38] == '1');
			IN40 = (overlay[39] == '1');
			IN41 = (overlay[40] == '1');
			IN42 = (overlay[41] == '1');
			IN43 = (overlay[42] == '1');
			IN44 = (overlay[43] == '1');
			IN45 = (overlay[44] == '1');
			IN46 = (overlay[45] == '1');
			IN47 = (overlay[46] == '1');
			IN48 = (overlay[47] == '1');
			IN49 = (overlay[48] == '1');
			IN50 = (overlay[49] == '1');
			IN51 = (overlay[50] == '1');
			IN52 = (overlay[51] == '1');
			IN53 = (overlay[52] == '1');
			IN54 = (overlay[53] == '1');
			IN55 = (overlay[54] == '1');
			IN56 = (overlay[55] == '1');
			IN57 = (overlay[56] == '1');
			IN58 = (overlay[57] == '1');
			IN59 = (overlay[58] == '1');
			IN60 = (overlay[59] == '1');
			IN61 = (overlay[60] == '1');
			IN62 = (overlay[61] == '1');
			IN63 = (overlay[62] == '1');
			IN64 = (overlay[63] == '1');
			IN65 = (overlay[64] == '1');
			IN66 = (overlay[65] == '1');
			IN67 = (overlay[66] == '1');
			IN68 = (overlay[67] == '1');
			IN69 = (overlay[68] == '1');
			IN70 = (overlay[69] == '1');
			IN71 = (overlay[70] == '1');
			IN72 = (overlay[71] == '1');
			IN73 = (overlay[72] == '1');
			IN74 = (overlay[73] == '1');
			IN75 = (overlay[74] == '1');
			IN76 = (overlay[75] == '1');
			IN77 = (overlay[76] == '1');
			IN78 = (overlay[77] == '1');
			IN79 = (overlay[78] == '1');
			IN80 = (overlay[79] == '1');
			IN81 = (overlay[80] == '1');
			IN82 = (overlay[81] == '1');
			IN83 = (overlay[82] == '1');
			IN84 = (overlay[83] == '1');
			IN85 = (overlay[84] == '1');
			IN86 = (overlay[85] == '1');
			IN87 = (overlay[86] == '1');
			IN88 = (overlay[87] == '1');
			IN89 = (overlay[88] == '1');
			IN90 = (overlay[89] == '1');
			IN91 = (overlay[90] == '1');
			IN92 = (overlay[91] == '1');
			IN93 = (overlay[92] == '1');
			IN94 = (overlay[93] == '1');
			IN95 = (overlay[94] == '1');
			IN96 = (overlay[95] == '1');
			IN97 = (overlay[96] == '1');
			IN98 = (overlay[97] == '1');
			IN99 = (overlay[98] == '1');
		}
		// Set the indicator at the specified value
		public void setCharAt(int index, int value)
		{
			char c;
			if (value==1)
				c='1';
			else
				c='0';
			super.setCharAt(index, c);
		}
	}
	public IN_ARRAY IN = new IN_ARRAY();

	//protected boolean IN[] = new boolean[99];
	public boolean IN01, IN02, IN03, IN04, IN05, IN06, IN07, IN08, IN09, IN10;
	public boolean IN11, IN12, IN13, IN14, IN15, IN16, IN17, IN18, IN19, IN20;
	public boolean IN1P = ON; // First page cycle indicator
	public boolean IN21, IN22, IN23, IN24, IN25, IN26, IN27, IN28, IN29, IN30;
	public boolean IN31, IN32, IN33, IN34, IN35, IN36, IN37, IN38, IN39, IN40;
	public boolean IN41, IN42, IN43, IN44, IN45, IN46, IN47, IN48, IN49, IN50;
	public boolean IN51, IN52, IN53, IN54, IN55, IN56, IN57, IN58, IN59, IN60;
	public boolean IN61, IN62, IN63, IN64, IN65, IN66, IN67, IN68, IN69, IN70;
	public boolean IN71, IN72, IN73, IN74, IN75, IN76, IN77, IN78, IN79, IN80;
	public boolean IN81, IN82, IN83, IN84, IN85, IN86, IN87, IN88, IN89, IN90;
	public boolean IN91, IN92, IN93, IN94, IN95, IN96, IN97, IN98, IN99;
	// Control level indicators
	public boolean INL0, INL1, INL2, INL3, INL4, INL5, INL6, INL7, INL8, INL9;
	// Last record/return indicators
	public boolean INLR, INRT;
	// Overflow indicators
	public boolean INOA, INOB, INOC, INOD, INOE, INOF, INOG, INOV;
	// Halt indicators
	public boolean INH1, INH2, INH3, INH4, INH5, INH6, INH7, INH8, INH9;

	public ThreadLock threadLock;
	I2Job appJob;
	/** The previous (the one that called this one) app on the call stack */
	//public Application prvApp;
	public WeakReference prvAppRef;
	/** All of the apps that this app has called. */
	Vector calledApps;
	/** Program messages associated with this program */
	Vector pgmMsgs;
	
	// The NULL application used by webfacing
	//static Application nullApp;
	

	protected Application()
	{
		this(null);
	}
	/**
	 * A default Application object with *MDY date edit.
	 */
	public Application(Application app)
	{
		this(app, "*MDY");
	}
	/**
	 * Construct this I2 application with the specified date edit
	 * @version 9/27/2002 5:53:56 PM
	 * @param datfmt java.lang.String date Date format (*MDY, *YMD, *DMY, *JUL)
	 */
	public Application(Application app, String datedit)
	{
		this(app, datedit, "*ISO");
	}
	
	/**
	 * Construct this I2 application with the specified date format, date separator. 
	 * @version 9/27/2002 5:53:56 PM
	 */
	public Application(Application app, String datedit, String datfmt)
	{
		hostApplicationCount++;
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Creating " + this + ' ' + hostApplicationCount);
		//setThreadLock(app);
		/* Add this program to the call stack */
		addToCallStack(app);

		// If this is the first program in the call stack, then create application-level job object. */
		if (app==null)
			appJob = new I2Job();
		else
			appJob = app.appJob;
		// Set switches
		INU1 = (appJob.jobSws.charAt(0)=='1');
		INU2 = (appJob.jobSws.charAt(1)=='1');
		INU3 = (appJob.jobSws.charAt(2)=='1');
		INU4 = (appJob.jobSws.charAt(3)=='1');
		INU5 = (appJob.jobSws.charAt(4)=='1');
		INU6 = (appJob.jobSws.charAt(5)=='1');
		INU7 = (appJob.jobSws.charAt(6)=='1');
		INU8 = (appJob.jobSws.charAt(7)=='1');
		
		// Set udate format
		udateFormat = DEFAULT_UDATE_FORMAT;
		longUdateFormat = "MMddyyyy";

		if (datedit.compareTo("*YMD") == 0)
		{
			udateFormat = "yyMMdd";
			longUdateFormat = "yyyyMMdd";
		}
		else if (datedit.compareTo("*DMY") == 0)
		{
			udateFormat = "ddMMyy";
			longUdateFormat = "ddMMyyyy";
		}
		else if (datedit.compareTo("*JUL")==0)
		{
			dateFormat="yyDDD";
			longUdateFormat="yyyyDDD";
		}

		// If the date formats are the same, then we can just do assignment
		if (app==null || udateFormat.compareTo(app.udateFormat)!=0)
		{
			// Create date special values
			UDATE = new FmtDate(6, udateFormat);
			DATE = new FmtDate(8, longUdateFormat);
			utimestampFormat = "HHmmss" + udateFormat;
			UTIMESTAMP = new FmtTime(12, utimestampFormat);
			//UTIMESTAMP = new date("HHMMSS" + udateFormat, -1);
			timestampFormat = "HHmmss" + longUdateFormat;
			TIMESTAMP = new FmtTime(14, timestampFormat);
		}
		else
		{
			UDATE = app.UDATE;
			DATE = app.DATE;
			UTIMESTAMP = app.UTIMESTAMP;
			TIMESTAMP = app.TIMESTAMP;
		}

		// Save default date format
		_DATFMT = datfmt;
		dateFormat = FixedDate.getDateFormat(datfmt);
		
	}
	
	/** A dummy constructor used to set the thread lock for WebFacing. */
	Application(Application app, ThreadLock lock)
	{
		this.threadLock = lock;
	}
	
	// Clean up any resources associated with this application
	@Override
	protected void finalize() throws Throwable
	{
		
		if (!finalized)
		{		
		// If this object hasn't already been deactivated, clean it up here
			try
			{
				if (I2Logger.logger.isDetailable())
					I2Logger.logger.detail("Finalizing " + this);
				INLR=ON;
				// Close the connection to all hosts 
				while (appRHosts.size()>0)
					Rreturn((IRHost)appRHosts.remove(0));
				hostApplicationCount--;
				if (I2Logger.logger.isDetailable())
					I2Logger.logger.detail("Finalized " + this + ' ' + hostApplicationCount);
			}
			catch (Throwable e)
			{
				I2Logger.logger.printStackTrace(e);
				throw e;
			}
			finally
			{
				super.finalize();
				finalized=true;
			}
		}
	}
	

	
	/**  the absolute value of a number. */
	static public double abs(double value)
	{
		return java.lang.Math.abs(value);
	}
	/** Return the absolute value of a number. */
	static public double abs(INumeric value)
	{
		return abs(value.doubleValue());
	}
	
	/** Return the address of a field */
	static public FixedPointer addr(FixedData field)
	{
		return field.m_ptr;
	}
	
	/**
	 * Return the figurative constant equivalent of *ALL'x'.
	 */
	public static FigConst ALL(char value)
	{
		FigConst fc = new FigConst(value);
		return fc;
	}
	/**
	 * Return a string representing the date in the specified format.
	 * @param dat the date to format
	 * @param dateFormat400 the 400 style date string (e.g. "*MDY/", "*ISO")
	 */
	public static FixedChar Char(FixedDate dat, String datfmt400)
	{
		return dat.Char(datfmt400);
	}
	/**
	 * Return a string representation of the number.
	 */
	public static String Char(INumeric value)
	{
		return value.toString();
	}
	/**
	 * Return a string representation of the number.
	 */
	public static String Char(long value)
	{
		return Long.toString(value);
	}
	
	/**
	 * Return the first position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see check(fixed, String) 
	 */
	public static int check(char comparator, String base)
	{
		return new FixedChar(1, base).check(comparator);
	}
	/**
	 * Return the first position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see check(fixed, String) 
	 */
	public static int check(char comparator, FixedChar base)
	{
		return base.check(comparator);
	}
	public static int check(FixedChar comparator, FixedChar base)
	{
		return base.check(comparator);
	}
	
	/**
	 * Return the first position of the string <code>base</code> that contains a character that 
    * does not appear in string <code>comparator</code>. 
    * If all of the characters in <code>base</code> also appear in <code>comparator</code>, the function returns 0.
	 */
	public static int check(String comparator, FixedChar base)
	{
		return base.check(comparator);
	}
	/**
	 * Return the first position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see check(fixed, String) 
	 */
	public static int check(String comparator, char base)
	{
		return new FixedChar(1, base).check(comparator);
	}
	/**
	 * Return the first position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>. 
	 * @see check(fixed, String) 
	 */
	public static int check(String comparator, String base)
	{
		return new FixedChar(base.length(), base).check(comparator);
	}

	/**
	 * Return the last position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see checkr(fixed, String) 
	 */
	public static int checkr(char comparator, FixedChar base)
	{
		return base.checkr(comparator);
	}
	
	/**
	* Return the last position of the string <code>base</code> that contains a character that 
	* does not appear in string <code>comparator</code>. 
	* If all of the characters in <code>base</code> also appear in <code>comparator</code>, the function returns 0.
	*/
	public static int checkr(String comparator, FixedChar base)
	{
		return base.checkr(comparator);
	}
	/**
	 * Return the last position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see checkr(fixed, String) 
	 */
	public static int checkr(char comparator, String base)
	{
		return new FixedChar(base.length(), base).checkr(comparator);
	}
	/**
	 * Return the last position of the string <code>base</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see checkr(fixed, String) 
	 */
	public static int checkr(String comparator, String base)
	{
		return new FixedChar(base.length(), base).checkr(comparator);
	}
	/**
	 * Return the last position of the string <code>base</code> 
	 * before <code>begin</code> that contains a character that 
	 * does not appear in string <code>comparator</code>.
	 * @see checkr(fixed, String) 
	 */
	public static int checkr(String comparator, char base, int start)
	{
		return new FixedChar(1, base).checkr(comparator,start);
	}

	/** Return the current system date. */
	static public FixedDate date()
	{
		FixedDate d = new FixedDate();
		d.assign(new java.util.Date());
		return d;
	}

	/**
	 * Return the date representation of a fixed string value.
	 * @param fStr the value to format
	 * @param dateFormat400 the 400 style date string (e.g. "*MDY/", "*ISO")
	 */
	public static FixedDate date(FixedChar fStr, String datfmt400) throws Exception
	{
		return date(fStr.toString(), datfmt400);
	}

	/**
	 * Return the date representation of an integer value.
	 * @param value the value to format
	 * @param dateFormat400 the 400 style date string (e.g. "*MDY/", "*ISO")
	 */
	public static FixedDate date(int value, String datfmt400) throws Exception
	{
		// Make sure that the date format ends with a '0' so that no separators get used
		datfmt400 = numericDateFormat(datfmt400);
		String datfmt = FixedDate.getDateFormat(datfmt400);
		ZonedDecimal z = new ZonedDecimal((datfmt.length()), 0, value);
		// Just use the raw zoned data instead so we avoid problems with leading 0s getting chopped
		//date d = new date(datfmt400, z.toString());
		String digits = z.toNumericString();
		return date(digits, datfmt400);
	}
	/**
	 * Return the date representation of a Numeric (e.g. zoned, packed) value
	 * @param value the value to format
	 * @param dateFormat400 the 400 style date string (e.g. "*MDY/", "*ISO")
	 */
	public static FixedDate date(INumeric value, String datfmt400) throws Exception
	{
		datfmt400 = numericDateFormat(datfmt400);
		//return date(value.toString(), datfmt400);
		String digits = value.toNumericString();
		return date(digits, datfmt400);
	}
	/**
	 * Return the date representation of a string.
	 * The string is assumed to be in the global (Application._DATFMT) date format.
	 */
	public static FixedDate date(String str) throws Exception
	{
		String datfmt400 = Application._DATFMT;
		return date(str, datfmt400);
	}
	/**
	 * Return the date representation of a fixed string value.
	 * @see date(String)
	 */
	public static FixedDate date(String str, String datfmt400) throws Exception
	{
		FixedDate d = new FixedDate(datfmt400, str);
		// Make sure that the date is valid
		d.toDate();
		return d;
	}

	/* Return the 6-digit numeric HMS equivalent of the current system time. 
	static public FmtDate time6()
	{
		FmtDate t6 = new FmtDate(6, "Hms");
		return t6;
	}
	*/
	/* Return the 12-digit numeric HMSDDD equivalent of the current system time. 
	public FmtDate time12()
	{
		FmtDate t12 = new FmtDate(12, utimestampFormat);
		return t12;
	}
	*/
	/* Return the 14-digit numeric HMSDDD equivalent of the current system time. 
	public FmtDate time14()
	{
		FmtDate t14 = new FmtDate(14, timestampFormat);
		return t14;
	}
	*/

	/** Return the current system time. */
	static public FixedTime time()
	{
		FixedTime t = new FixedTime();
		t.assign(new java.util.Date());
		return t;
	}
	
	/**
	 * Return the time representation of a string.
	 * The string is assumed to be in iso date format.
	 */
	public static FixedTime time(AbstractNumeric value) throws Exception
	{
		FixedTime t = new FixedTime("*ISO0");
		t.move(value);
		return t;
	}

	/**
	 * Return the time representation of a string.
	 * The string is assumed to be in iso date format.
	 */
	public static FixedTime time(String str) throws Exception
	{
		FixedTime t = new FixedTime("*ISO");
		t.assign(str);
		return t;
	}

	/**
	 * Return the time representation of a string.
	 * The string is assumed to be in iso date format.
	 */
	public static FixedTime time(FixedChar str) throws Exception
	{
		return time(str.toString());
	}

	/**
	 * Return the time representation of a string in the specified date format.
	 */
	public static FixedTime time(FixedChar str, String datfmt400) throws Exception
	{
		return time(str.toString(), datfmt400); 
	}
	/**
	 * Return the time representation of a string in the specified date format.
	 */
	public static FixedTime time(String str, String datfmt400) throws Exception
	{
		FixedTime t = new FixedTime(datfmt400, str); 
		return t;
	}

	/**
	 * Return the time representation of a Numeric (e.g. zoned, packed) value
	 * @param value the value to format
	 * @param dateFormat400 the 400 style date string (e.g. "*HMS0", "*ISO")
	 */
	public static FixedTime time(INumeric value, String datfmt400) throws Exception
	{
		datfmt400 = numericDateFormat(datfmt400);
		//return date(value.toString(), datfmt400);
		String digits = value.toNumericString();
		return time(digits, datfmt400);
	}


	/** Return a DAY duration */
	static public Duration days(int days)
	{
		return new Duration(days, DAYS);
	}
	/** Return a DAY duration */
	static public Duration days(INumeric days)
	{
		return days(days.intValue());
	}
	
	/** Return the packed decimal equivalent of a long value. */
	static public PackedDecimal dec(long value)
	{
		int length = (int)Application.log10(value)+1;
		return new PackedDecimal(length, 0, value);
	}
	/** Return the packed decimal equivalent of a double value. */
	static public PackedDecimal dec(double value)
	{
		int length = (int)Application.log10(value)+1;
		int scale = ShortDecimal.newBigDecimal(value).scale();
		return new PackedDecimal(length+scale, scale, value);
	}
	/** Return the packed decimal equivalent of a value. */
	static public PackedDecimal dec(double value, int length, int scale)
	{
		return new PackedDecimal(length, scale, value);
	}
	/** Return the packed decimal equivalent of a numeric value. */
	static public PackedDecimal dec(INumeric value)
	{
		PackedDecimal p = new PackedDecimal(value.len(), value.scale());
		p.assign(value);
		return p;
	}
	/** Return the packed decimal equivalent of a value. */
	static public PackedDecimal dec(Object value, int length, int scale)
	{
		return new PackedDecimal(length, scale, Double.parseDouble(value.toString()));
	}
	
	/** Return the number of decimal positions (scale) of a value. */
	static public int decpos(INumeric value)
	{
		return value.scale();
	}
	
	/**
	 * Return the <code>durationCode</code> kind of difference between two dates.
	 * @param beginDate
	 * @param endDate
	 * @param durationCode the duration code: (Application.)MSECONDS, SECONDS, MINUTES, HOURS, MONTHS, DAYS, YEARS
	 */
	static public int diff(FixedDate beginDate, FixedDate endDate, int durationCode) throws Exception 
	{
		// The number of milliseconds between the two dates
		long ms = endDate.toDate().getTime() - beginDate.toDate().getTime();
		int duration = 0;
		// Offset from 01/01/0001 so that the date that we are left with is MONTHDIF/day/YEARDIF
		if (durationCode == YEARS || durationCode == MONTHS)
		{
			int multiplier = 1;
			// If the difference is negative, change to positive and then reconvert at the ned
			if (ms < 0)
			{
				ms = -ms;
				multiplier = -1;
			}
			ms += FixedDate.LOVAL_DATE.getTime();
			// Create a new date with the specified milliseconds
			java.util.Date d = new java.util.Date(ms);
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(d);
			// Extract the appropriate duration from the 'date'
			duration = calendar.get(durationCode);
			// The month, day, and year are going to be off by one because we started with 01/01/0001 (not 00/00/0000).
			// But, for whatever reason, the MONTH is 0-11 instead of 1-12 so we don't need to worry about them.
			if (durationCode == YEARS /*|| durationCode==DAYS*/
				)
				duration--;
			duration *= multiplier;
		}
		else
			switch (durationCode)
			{
				// Convert milliseconds to days
				case (DAYS) :
					duration = (int) (ms / (24 * 60 * 60 * 1000));
					break;
				// Convert milliseconds to hours
				case (HOURS) :
					duration = (int) (ms / (60 * 60 * 1000));
					break;
				// Convert milliseconds to minutes
				case (MINUTES) :
					duration = (int) (ms / (60 * 1000));
					break;
				// Convert milliseconds to seconds
				case (SECONDS) :
					duration = (int) (ms / (1000));
					break;
				// Convert milliseconds to microseconds
				case (MSECONDS):
					duration = (int)(ms*1000 + beginDate.getMicrorem()-endDate.getMicrorem()); // Convert milliseconds to microseconds
			}
		return duration;
	}
	
	/** Return integer portion of quotient */
	public static int div(long value1, long value2)
	{
		return (int)(value1 / value2);
	}
	/** Return integer portion of quotient */
	public static int div(long value1, INumeric value2)
	{
		return div(value1, value2.longValue());
	}
	/** Return integer portion of quotient */
	public static int div(INumeric value1, long value2)
	{
		return div(value1.longValue(), value2);
	}
	
	/** Return integer remainder */
	public static int rem(long value1, long value2)
	{
		return (int)(value1 % value2);
	}
	/** Return integer remainder */
	public static int rem(long value1, INumeric value2)
	{
		return div(value1, value2.longValue());
	}
	/** Return integer remainder */
	public static int rem(INumeric value1, long value2)
	{
		return div(value1.longValue(), value2);
	}
	
	/** Return a string edited according to the edit code. */
	static public String editc(INumeric value, char edtCde)
	{
		return editc(value, edtCde, ' ');		
	}
	/** Return a string edited according to the edit code. */
	static public String editc(INumeric value, char edtCde, char fillChar)
	{
		String edtwrd = RrecordPrint.getEdtWrd(value, edtCde, fillChar);
		return editw(value, edtwrd);
	}
	/** Return a string edited according to the edit code. */
	static public String editc(long value, char edtCde)
	{
		return editc(value, edtCde, ' ');
	}
	/** Return a string edited according to the edit code. */
	static public String editc(long value, char edtCde, char fillChar)
	{
		ZonedDecimal z = new ZonedDecimal(19,0,value);
		return editc(z, edtCde, fillChar);
	}
	
	/** Return the floating-point formatted value of a number. */
	static public String editflt(double value)
	{
		return Double.toString(value);
	}
	/** Return the floating-point formatted value of a number. */
	static public String editflt(INumeric value)
	{
		return editflt(value.doubleValue());
	}

	/** Return a string edited according to the edit word. */
	static public String editw(INumeric value, String edtwrd)
	{
		return RrecordPrint.editNumeric(value, edtwrd);
	}
	/** Return a string edited according to the edit word. */
	static public String editw(long value, String edtwrd)
	{
		ZonedDecimal z = new ZonedDecimal(19,0,value);
		return editw(z, edtwrd);
	}
	
	/** Return the number of elements in an array. */	
	static public int elem(Object array)
	{
		return Array.getLength(array);
	}

	/**
	 * Return the last eof condition..
	 */
	public boolean eof()
	{
		return eof;
	}

	/**
	 * Return the eof (eof | bof) condition for a file.
	 */
	public static boolean eof(Rfile file)
	{
		return (file.bof | file.eof);
	}
	
	/** Return the floating point equivalent of the value. */
	public static double Float(Object value)
	{
		return Double.parseDouble(value.toString());
	}

	/**
	 * Return the last equal condition.
	 */
	public boolean equal()
	{
		return equal;
	}

	/**
	 * Return the last error condition.
	 */
	public boolean error()
	{
		return ERROR;
	}

	/**
	 * Return the equal condition for a file set if the last SETLL found an exact match.
	 */
	public static boolean equal(RfileCycle file)
	{
		return file.m_equal;
	}

	/**
	 * Return the found condition for a file set if the last SETLL, CHAIN, DELETE found an exact match.
	 */
	public static boolean found(Rfile file)
	{
		return file.found;
	}
	
	/** Return the last found condition */
	public boolean found()
	{
		return found;
	}
	
	/**
	 * Return a default AS400 object.
	 */
	public I2AS400 getAS400()
	{
		return getAS400("*PROPERTY", "*PROPERTY", "*PROPERTY");
	}
	/**
	 * Return an AS400 object corresponding to the specified system name.
	 */
	public I2AS400 getAS400(String hostName)
	{
		return getAS400(hostName, "*PROPERTY", "*PROPERTY");
	}
	/**
	 * Return an AS400 object corresponding to the specified system name, userid, and password
	 */
	public I2AS400 getAS400(String hostName, String usrid, String password)
	{
		I2AS400 ras400 = null;
		try
		{
			// If no host/userid/password is specified, then try to load from properties file
			if (hostName.equals("*PROPERTY")
				|| usrid.equals("*PROPERTY")
				|| password.equals("*PROPERTY"))
			{
				// First, try to load from I2.properties
				{
					ResourceBundle prop = getResourceBundle();
					if (password.equals("*PROPERTY"))
						password = prop.getString("HostPWD");
					if (usrid.equals("*PROPERTY"))
						usrid = prop.getString("HostUID");
					if (hostName.equals("*PROPERTY"))
						hostName = prop.getString("HostName");
				}
			}

			// If no property entry exists, then set to *CURRENT
			if (hostName.equals("*PROPERTY") || hostName.equals("*LOCAL"))
				hostName = "localhost";
			if (usrid.equals("*PROPERTY"))
				usrid = "*CURRENT";
			if (password.equals("*PROPERTY"))
				password = "*CURRENT";
			
			ras400 = (I2AS400)retrieveI2Host(hostName, usrid, password);
			// If that doesn't work, then try to load the WFApp Properties
			// Skip this, because the new WF doesn't include usrid/password in the file.
			/*
			catch (Exception e)
			{
				WFAppProperties wfprop = WFAppProperties.getWFAppProperties();
				if (password.equals("*PROPERTY"))
					password = wfprop.getPassword();
				if (usrid.equals("*PROPERTY"))
					usrid = wfprop.getUserID();
				if (hostName.equals("*PROPERTY")) 
					hostName = wfprop.getHostName();
			}*/
		}
		catch (Exception e)
		{
			I2Logger.logger.printStackTrace(e);
		}
		return ras400;
	}

	/* 
	 * Set the global key cache size for any keyed database files. 
	 * @see RIndexJDBC#loadKeyCache
	public static int getKeyCacheSize() {
		return Application.keyCacheSize;
	}
	*/

	/** Return the OS/400 /QSYS.LIB/... path for a file name */	
	public static String getPath(String fileName) //throws java.beans.PropertyVetoException
	{
		return getPath(fileName, "FILE");
	}
	/** Return the OS/400 /QSYS.LIB/... path for a specified name and object type. */	
	public static String getPath(
		String objectName,
		String objectType) //throws java.beans.PropertyVetoException
	{
		if (!objectName.toUpperCase().startsWith("/QSYS.LIB/"))
		{
			String library;
			int i = objectName.indexOf('/');
			if (i < 1)
				library = "%LIBL%";
			else
			{
				library = objectName.substring(0, i);
				objectName = objectName.substring(i + 1, objectName.length());
			}
			objectName =
				QSYSObjectPathName.toPath(library, objectName, objectType);
		}
		return objectName;
	}

	/**
	 * Get the i2class.properties file.
	 * @return java.util.ResourceBundle
	 */
	static ResourceBundle getResourceBundle()
	{
		if (i2properties == null)
			i2properties = ResourceBundle.getBundle("i2class");
		return i2properties;
	}

	/* * Retrieve a connection from the connection pool. 
	static protected IHost retrieveI2Host(String url, String usrid, String password, Application app)
	{
		IHost host=null;
		hostPool.lockHostPool();
		// Ouch!!! Can't just cache based upon URL, but must also use user id so that authority isn't shared
		String key = usrid + '\t' + url;
		// See if this connection is already in the connection pool
		host = (IHost)hostPool.get(key);
		// If the connection doesn't exist, then add it to the pool
		if (host==null)
		{
			// If the URL begins with 'jdbc:', then this is a JDBC request...
			if (url.startsWith("jdbc:"))
				host = new I2Connection(app, url, usrid, password);
			//...otherwise, this is an AS400 request.
			else
				host = new I2AS400(new AS400(url, usrid, password), app);
			// Add the host to the host pool
			hostPool.put(key, host);
			app.appHosts.add(host);
		}
		hostCount++;
		hostPool.unlockHostPool();
		return host;
	}
	/ ** Retrieve a host object (either Connection or AS400) from the connection pool. * /
	protected Object retrieveHost(String url, String usrid, String password) throws Exception
	{
		IHost host = retrieveI2Host(url, usrid, password, this);
		Object o = host.getHost();
		if (o==null)
		{
			// If the URL begins with 'jdbc:', then this is a JDBC request...
			if (url.startsWith("jdbc:"))
			{
				// If no property entry exists, then try to use defaults
				if (usrid.equals("*PROPERTY") || password.equals("*PROPERTY"))
					o = DriverManager.getConnection(url);
				else
					o = DriverManager.getConnection(url, usrid, password);
			}
			//...otherwise, this is an AS400 request.
			else
				o = new AS400(url, usrid, password);
			host.setHost(o);
		}
		return o;
	}
	*/
	/** Retrieve a host object (Connection or AS400). */
	protected Object retrieveHost(String url, String usrid, String password) throws SQLException 
	{
		if (I2Logger.logger.isDetailable())
			I2Logger.logger.detail("Retrieving host " + url);
		Object host;
		try
		{
			// If the URL begins with 'jdbc:', then this is a JDBC request...
			if (url.startsWith("jdbc:"))
			{
				// If no property entry exists, then try to use defaults
				if (usrid.equals("*PROPERTY") || password.equals("*PROPERTY"))
					host = DriverManager.getConnection(url);
				else
					host = DriverManager.getConnection(url, usrid, password);
			}
			//...otherwise, this is an AS400 request.
			else
				host = new AS400(url, usrid, password);
			if (I2Logger.logger.isDetailable())
				I2Logger.logger.detail("Succesfully created host " + host);
		}
		catch (SQLException e)
		{
			I2Logger.logger.severe("Unable to retrieve host");
			I2Logger.logger.printStackTrace(e);
			throw e;
		}
		return host;
	}

	/** Retrieve a host object (Connection or AS400) from the connection pool. */
	protected Object retrievePooledHost(String url, String usrid, String password) throws SQLException
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Retrieving pooled host " + url);
		hostPool.lockHostPool();
		// Ouch!!! Can't just cache based upon URL, but must also used user id so that authority isn't shared
		String key = usrid + '\t' + url;
		// See if this connection is already in the connection pool
		Object host = hostPool.get(key);
		
		try
		{
			// Verify that a connection is valid.
			// Using getMetaData is fairly lame, but it seems to be the recommended way to verify that the 
			// network connection has not been dropped.
			if (host !=null && url.startsWith("jdbc:"))
			{
				Connection conn = (Connection)host;
				if (I2Logger.logger.isDetailable())
					I2Logger.logger.detail("Using cached connection " + conn);
				try
				{
					conn.createStatement();
					I2Logger.logger.trace("Cached connection verified with createStatement()");
				}
				catch (SQLException e)
				{
					I2Logger.logger.detail("Cached connection invalid");
					I2Logger.logger.printStackTrace(e);
					//TODO have to track usage count by connection
					hostCount=1;
					removeHost(conn);
					host=null;
				}
			}
	
			// If the connection doesn't exist, then add it to the pool
			if (host==null)
			{
				host = retrieveHost(url, usrid, password);
				// Add the host to the host pool
				hostPool.put(key, host);
			}
		}
		finally
		{
			hostPool.unlockHostPool();
		}
		return host;
	}
	
	/** Retrieve a I2 host object (either I2Connection or I2AS400) from the connection pool. */
	protected IRHost retrieveI2Host(String url, String usrid, String password) throws Exception
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Retrieving I2 host " + url);
		// Use the connection from the previous application object, if it is compatible
		IRHost rhost;
		if (url.startsWith("jdbc:"))
			rhost = new I2Connection(this, url, usrid, password);
		//...otherwise, this is an AS400 request.
		else
			rhost = new I2AS400((AS400)retrievePooledHost(url, usrid, password), this);
		appRHosts.add(rhost);
		hostCount++;
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Host count " + hostCount);
		return rhost;
	}
	
	/** Remove a I2 host from the connection pool. */
	protected void removeI2Host(IRHost rhost)
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Removing I2 host " + rhost);
		removeHost(rhost.getHost());
		appRHosts.remove(rhost);
		try
		{
			rhost.invalidate();
		}
		catch (Throwable e1) {}
	}
	
	/** Remove a host from the connection pool. */
	protected void removeHost(Object host)
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Removing host " + host);
		hostPool.lockHostPool();
		try
		{ 
			Enumeration e=null;
			// Return the Connection object to the connection pool
			if (hostCount>0)
			{
				hostCount--;
				if (I2Logger.logger.isTraceable())
					I2Logger.logger.trace("Host count " + hostCount);
				if (hostCount == 0)
					e = hostPool.elements();
			}
			// This should only happen if retrieveHost is overridden and the connection is not cached
			else if (host != null)
			{
				Vector v = new Vector(1);
				v.addElement(host);
				e=v.elements();
			}
			if (e!=null) while (e.hasMoreElements())
			{
				host = e.nextElement();
				if (I2Logger.logger.isDetailable())
					I2Logger.logger.detail("Closing host " + host);
				try {
					if (host instanceof Connection)
						((Connection)host).close();
					else if (host instanceof AS400)
						((AS400)host).disconnectAllServices();
				} catch (SQLException e1) {
					I2Logger.logger.printStackTrace(e1);
				}
				// Remove the connection from the list of connections associated with this object
				//appHosts.remove(host);
			}
			if (hostCount==0)
				hostPool.clear();
		}
		finally
		{
			hostPool.unlockHostPool();
		}
	}

	/** The default (first) connection associated with this Application object. */
	I2Connection defaultI2Connection()
	{
		return (I2Connection)appRHosts.elementAt(0);				
	}
	
	/** Return a HOUR duration */
	static public Duration hours(int hours)
	{
		return new Duration(hours, HOURS);
	}
	/** Return a HOUR duration */
	static public Duration hours(INumeric hours)
	{
		return hours(hours.intValue());
	}
	
	/** Return the integer equivalent of the value. */
	static public long Int(Object value)
	{
		return (long)Double.parseDouble(value.toString());
	}
	/** Return the half-adjusted integer equivalent of the value. */
	static public long inth(double value)
	{
		return Math.round(value);
	}
	/** Return the half-adjusted integer equivalent of the value. */
	static public long inth(Object value)
	{
		return inth(Double.parseDouble(value.toString()));
	}

	/**
	 * Return the length of the specified string
	 */
	public static int len(String parm)
	{
		return parm.length();
	}
	/**
	 * Return the length of the specified character string.
	 */
	public static int len(FixedChar parm)
	{
		return parm.len();
	}

	// Return the log10 value of a number
	private static final double LOG10 = Math.log(10.0);
	public static double log10(double d)
	{
		//		The reason this routine is needed is that simply taking the
		// log and dividing by log10 yields a result which may be off
		// by 1 due to rounding errors.  For example, the native log10
		// of 1.0e300 taken this way is 299, rather than 300.
		double log10 = Math.log(d) / LOG10;
		int ilog10 = (int)Math.floor(log10);
		// Positive logs could be too small, e.g. 0.99 instead of 1.0
		if (log10 > 0 && d >= Math.pow(10, ilog10 + 1))
			log10 = ++ilog10;
		// Negative logs could be too big, e.g. -0.99 instead of -1.0
		else if (log10 < 0 && d < Math.pow(10, ilog10))
			log10 = --ilog10;
		return log10;
	}
	
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(char arg, char ary[])
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(char arg, char ary[], int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(FigConst arg, char ary[], int startIndex)
	{
		return lookup(arg.charAt(0), ary, startIndex);
	}
	
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(char arg, FixedChar fStr)
	{
		return lookup(arg, fStr, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(char arg, FixedChar fStr, int startIndex)
	{
		return scanEqual(lookupxx(arg, fStr, startIndex, EQ));
	}

	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, Comparable[] ary)
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, Comparable[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}

	/** Lookup up a value and return the index of the first exact match. */
	int lookup(Comparable arg, double[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, int[] ary)
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, int[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, long[] ary)
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(Comparable arg, long[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(double arg, double[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(double arg, INumeric[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}

	/**
	 * Compare 2 fixed types.
	 * Some DIM(1) arrays get turned into fixed, so we need to have this here.
	 */
	public int lookup(FixedChar fStr1, FixedChar fStr2, int startIndex)
	{
		if (fStr1.equals(fStr2))
			return 1;
		return -1;
	}

	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(int arg, int[] ary)
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(int arg, int[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}
	
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(long arg, long[] ary)
	{
		return lookup(arg, ary, 1);
	}
	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(long arg, long[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}

	/** Lookup up a value and return the index of the first exact match. */
	public int lookup(short arg, short[] ary, int startIndex)
	{
		return scanEqual(lookupxx(arg, ary, startIndex, EQ));
	}

	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(char arg, char ary[], int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(FigConst arg, char ary[], int startIndex)
	{
		return lookupge(arg.charAt(0), ary, startIndex);
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(char arg, FixedChar fStr, int startIndex)
	{
		return scanFound(lookupxx(arg, fStr, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(Comparable arg, Comparable[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(Comparable arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(Comparable arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(Comparable arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(double arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(double arg, INumeric[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(int arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(long arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >=. */
	public int lookupge(short arg, short[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GE));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(char arg, char ary[], int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(FigConst arg, char ary[], int startIndex)
	{
		return lookupgt(arg.charAt(0), ary, startIndex);
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(char arg, FixedChar fStr, int startIndex)
	{
		return scanFound(lookupxx(arg, fStr, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(Comparable arg, Comparable[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(Comparable arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(Comparable arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(Comparable arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(double arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(double arg, INumeric[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(int arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(long arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is >. */
	public int lookupgt(short arg, short[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, GT));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(char arg, char ary[], int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(FigConst arg, char ary[], int startIndex)
	{
		return lookuple(arg.charAt(0), ary, startIndex);
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(char arg, FixedChar fStr, int startIndex)
	{
		return scanFound(lookupxx(arg, fStr, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(Comparable arg, Comparable[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(Comparable arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(Comparable arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(Comparable arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(double arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(double arg, INumeric[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(int arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(long arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <=. */
	public int lookuple(short arg, short[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LE));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(char arg, char ary[], int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(FigConst arg, char ary[], int startIndex)
	{
		return lookuplt(arg.charAt(0), ary, startIndex);
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(char arg, FixedChar fStr, int startIndex)
	{
		return scanFound(lookupxx(arg, fStr, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(Comparable arg, Comparable[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(Comparable arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(Comparable arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(Comparable arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(double arg, double[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(double arg, INumeric[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(int arg, int[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(long arg, long[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	/** Lookup up a value and return the index of the first match that is <. */
	public int lookuplt(short arg, short[] ary, int startIndex)
	{
		return scanFound(lookupxx(arg, ary, startIndex, LT));
	}
	static int lookupxx(char arg, char[] array, int startIndex, int compare)
	{
		int numElems = array.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (array[startIndex-1] == arg);
			else if (compare == GT)
				bcomp = (array[startIndex-1] > arg);
			else if (compare == LT)
				bcomp = (array[startIndex-1] < arg);
			else if (compare == GE)
				bcomp = (array[startIndex-1] >= arg);
			else //if (compare==LE)
				bcomp = (array[startIndex-1] <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	
	/** This is where the lookup actually happens for lookupxx */
	static int lookupxx(char arg, FixedChar fStr, int startIndex, int compare)
	{
		int numElems = fStr.size();
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (fStr.charAt(startIndex - 1) == arg);
			else if (compare == GT)
				bcomp = (fStr.charAt(startIndex - 1) > arg);
			else if (compare == LT)
				bcomp = (fStr.charAt(startIndex - 1) < arg);
			else if (compare == GE)
				bcomp = (fStr.charAt(startIndex - 1) >= arg);
			else //if (compare==LE)
				bcomp = (fStr.charAt(startIndex - 1) <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(
		Comparable arg,
		Comparable[] ary,
		int startIndex,
		int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			//int i = arg.compareTo(ary[startIndex - 1]);
			int i = ary[startIndex - 1].compareTo(arg);
			if (compare == EQ)
				bcomp = (i == 0);
			else if (compare == GT)
				bcomp = (i > 0);
			else if (compare == LT)
				bcomp = (i < 0);
			else if (compare == GE)
				bcomp = (i >= 0);
			else //if (compare==LE)
				bcomp = (i <= 0);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(
		Comparable arg,
		double[] ary,
		int startIndex,
		int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			// We have to compare 'backwards' here so that we can use the arg (usually a fixed data type) 
			// method instead of Double.compareTo(), which only works when comparing agains Double objects. 
			int i = -arg.compareTo(new Double(ary[startIndex - 1]));
			if (compare == EQ)
				bcomp = (i == 0);
			else if (compare == GT)
				bcomp = (i > 0);
			else if (compare == LT)
				bcomp = (i < 0);
			else if (compare == GE)
				bcomp = (i >= 0);
			else //if (compare==LE)
				bcomp = (i <= 0);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	
	static int lookupxx(Comparable arg, int[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			// We have to compare 'backwards' here so that we can use the arg (usually a fixed data type) 
			// method instead of Integer.compareTo(), which only works when comparing agains Integer objects. 
			int i = -arg.compareTo(ShortDecimal.newInteger(ary[startIndex - 1]));
			if (compare == EQ)
				bcomp = (i == 0);
			else if (compare == GT)
				bcomp = (i > 0);
			else if (compare == LT)
				bcomp = (i < 0);
			else if (compare == GE)
				bcomp = (i >= 0);
			else //if (compare==LE)
				bcomp = (i <= 0);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(Comparable arg, long[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			// We have to compare 'backwards' here so that we can use the arg (usually a fixed data type) 
			// method instead of Long.compareTo(), which only works when comparing agains Long objects. 
			int i = -arg.compareTo(new Long(ary[startIndex - 1]));
			if (compare == EQ)
				bcomp = (i == 0);
			else if (compare == GT)
				bcomp = (i > 0);
			else if (compare == LT)
				bcomp = (i < 0);
			else if (compare == GE)
				bcomp = (i >= 0);
			else //if (compare==LE)
				bcomp = (i <= 0);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(
		Comparable arg,
		short[] ary,
		int startIndex,
		int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			// We have to compare 'backwards' here so that we can use the arg (usually a fixed data type) 
			// method instead of Short.compareTo(), which only works when comparing agains Short objects. 
			int i = -arg.compareTo(new Short(ary[startIndex - 1]));
			if (compare == EQ)
				bcomp = (i == 0);
			else if (compare == GT)
				bcomp = (i > 0);
			else if (compare == LT)
				bcomp = (i < 0);
			else if (compare == GE)
				bcomp = (i >= 0);
			else //if (compare==LE)
				bcomp = (i <= 0);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(double arg, double[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (ary[startIndex - 1] == arg);
			else if (compare == GT)
				bcomp = (ary[startIndex - 1] > arg);
			else if (compare == LT)
				bcomp = (ary[startIndex - 1] < arg);
			else if (compare == GE)
				bcomp = (ary[startIndex - 1] >= arg);
			else //if (compare==LE)
				bcomp = (ary[startIndex - 1] <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(
		double arg,
		INumeric[] ary,
		int startIndex,
		int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			//int i=new Double(arg).compareTo(ary[startIndex-1]);
			//int i=ary[startIndex-1].compareTo(new Double(arg))*-1;
			double d = ary[startIndex - 1].doubleValue();
			if (compare == EQ)
				bcomp = (d == arg);
			else if (compare == GT)
				bcomp = (d > arg);
			else if (compare == LT)
				bcomp = (d < arg);
			else if (compare == GE)
				bcomp = (d >= arg);
			else //if (compare==LE)
				bcomp = (d <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(int arg, int[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (ary[startIndex - 1] == arg);
			else if (compare == GT)
				bcomp = (ary[startIndex - 1] > arg);
			else if (compare == LT)
				bcomp = (ary[startIndex - 1] < arg);
			else if (compare == GE)
				bcomp = (ary[startIndex - 1] >= arg);
			else //if (compare==LE)
				bcomp = (ary[startIndex - 1] <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(long arg, long[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (ary[startIndex - 1] == arg);
			else if (compare == GT)
				bcomp = (ary[startIndex - 1] > arg);
			else if (compare == LT)
				bcomp = (ary[startIndex - 1] < arg);
			else if (compare == GE)
				bcomp = (ary[startIndex - 1] >= arg);
			else //if (compare==LE)
				bcomp = (ary[startIndex - 1] <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	static int lookupxx(short arg, short[] ary, int startIndex, int compare)
	{
		int numElems = ary.length;
		for (; startIndex <= numElems; startIndex++)
		{
			boolean bcomp;
			if (compare == EQ)
				bcomp = (ary[startIndex - 1] == arg);
			else if (compare == GT)
				bcomp = (ary[startIndex - 1] > arg);
			else if (compare == LT)
				bcomp = (ary[startIndex - 1] < arg);
			else if (compare == GE)
				bcomp = (ary[startIndex - 1] >= arg);
			else //if (compare==LE)
				bcomp = (ary[startIndex - 1] <= arg);
			if (bcomp)
				return startIndex;
		}
		return 0;
	}
	
	/** Return a MICROSECOND duration */
	static public Duration mseconds(int mseconds)
	{
		return new Duration(mseconds, MSECONDS);
	}
	/** Return a MICROSECOND duration */
	static public Duration mseconds(INumeric mseconds)
	{
		return seconds(mseconds.intValue());
	}
	
	/** Return a MINUTE duration */
	static public Duration minutes(int minutes)
	{
		return new Duration(minutes, MINUTES);
	}
	/** Return a MINUTE duration */
	static public Duration minutes(INumeric minutes)
	{
		return minutes(minutes.intValue());
	}
	
	/** Return a MONTH duration */
	static public Duration months(int months)
	{
		return new Duration(months, MONTHS);
	}
	/** Return a MONTH duration */
	static public Duration months(INumeric months)
	{
		return months(months.intValue());
	}
	/** Move the boolean value to the character receiver. */
	static public char move(boolean source, char dest)
	{
		if (source)
			return '1';
		return '0';
	}

	/**
	 * Move a character value to the right-most position of an integer.
	 */
	static public int move(char source, int dest)
	{
		return ((ZonedDecimal) (new ZonedDecimal(9, 0, dest).move(source))).intValue();
	}
	/**
	 * Move the right-most byte of a fixed-length value to a boolean (Indicator)
	 */
	static public boolean move(FixedChar source, boolean dest)
	{
		if (move(source, ' ') == '1')
			return true;
		return false;
	}
	/** Move the right-most characters of a value to a character destination. */
	static public char move(IFixed source, char dest)
	{
		return new FixedChar(1, dest).move(source).charAt(0);
	}
	/** Move the right-most characters of a value to a integer destination. */
	static public int move(IFixed source, int dest)
	{
		return ((ZonedDecimal) (new ZonedDecimal(9, 0, dest).move(source))).intValue();
	}
	/** Move the right-most characters of a value to a long destination. */
	static public long move(IFixed source, long dest)
	{
		return ((ZonedDecimal) (new ZonedDecimal(18, 0, dest).move(source))).longValue();
	}
	/** Move the right-most characters of a value to a Numeric destination. */
	static public double move(IFixed source, AbstractNumeric dest)
	{
		return ((INumeric) dest.move(source)).doubleValue();
	}
	/** Move the right-most characters of a value to a short destination. */
	static public short move(IFixed source, short dest)
	{
		return ((ZonedDecimal) (new ZonedDecimal(4, 0, dest).move(source))).shortValue();
	}
	/** Move the right-most characters of a value to a character destination. */
	static public char move(long source, char dest)
	{
		return new FixedChar(1, dest).move(new ZonedDecimal(18, 0, source)).charAt(0);
	}
	/** Move the right-most characters of a value to a character destination. */
	static public char move(String source, char dest)
	{
		return new FixedChar(1, dest).move(source).charAt(0);
	}
	/**
	 * Move a String value to the right-most position of an integer.
	 */
	static public int move(String source, int dest)
	{
		return ((ZonedDecimal) (new ZonedDecimal(9, 0, dest).move(source))).intValue();
	}

	/* * 
	 * Copy a boolean (indicator?) array to a fixed-length string
	 * @param a1 The boolean array to copy from
	 * @param i1 The offset (1-based) to copy from
	 * @param a2 The fixed length string to copy to
	 * @param i2 The offset (1-based) into the fixed length string
	static public void MOVEA(boolean[] a1, int i1, fixed a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - i1, a2.size() - i2);
		for (int i = 0; i < elems; i++)
		{
			if (a1[i1])
				a2.overlay[i2] = (byte) '1';
			else
				a2.overlay[i2] = (byte) '0';
			i1++;
			i2++;
		}
	}
	*/

	/** 
	 * Copy a boolean (indicator?) array to a character array
	 * @param a1 The boolean array to copy from
	 * @param i1 The offset (1-based) to copy from
	 * @param a2 The character array to copy to
	 * @param i2 The offset (1-based) into the fixed length string
	*/
	static public void MOVEA(boolean[] a1, int i1, char[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
		{
			char bool='0';
			if (a1[i1++])
				bool = '1';
			a2[i2++] = bool;
		}
	}

	/** 
	 * Copy a fixed string to a character array
	 * @param value The string to copy from
	 * @param i1 The offset (1-based) into the string
	 * @param a2 The character array to copy to
	 * @param i2 The offset (1-based) into the character array
	*/
	static public void MOVEA(FixedChar value, int i1, char[] a2, int i2)
	{
		MOVEA(value.toCharArray(), i1, a2, i2);
	}
	/** Copy a character array to a fixed-string. */
	static public void MOVEA(char []a1, int i1, FixedChar value, int i2)
	{
		MOVEA(a1, i1, value.toCharArray(), i2);
	}
	/** Copy one character array to another. */
	static public void MOVEA(char []a1, int i1, char []a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		System.arraycopy(a1, i1, a2, i2, elems);
	}
	
	/** Copy arrays of the same type from 1-based index i1 to 1-based index i2 */
	static public void MOVEA(FixedChar []a1, int i1, FixedChar []a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++].assign(a1[i1++]);
	}
	/** Copy numeric arrarys. */ 
	static public void MOVEA(int a1[], int i1, INumeric[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++].assign(a1[i1++]);
	}

	/* * 
	 * Copy a fixed-length string to a integer (zoned) array
	 * @param a1 The fixed length string to copy
	 * @param i1 The offset (1-based) into the fixed length string
	 * @param a2 The boolean array to copy to
	 * @param i2 The offset (1-based) to copy into
	static public void MOVEA(zoned a1, int i1, int[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.size() - i1, a2.length - i2);
		for (int i = 0; i < elems; i++)
		{
			a2[i2] = a1.charAt(i1) - '0';
			i1++;
			i2++;
		}
	}
	*/

	/** Copy numeric arrarys. */ 
	static public void MOVEA(INumeric a1[], int i1, int[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++] = a1[i1++].intValue();
	}
	/** Copy numeric arrarys. */ 
	static public void MOVEA(INumeric a1[], int i1, INumeric[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++].assign(a1[i1++]);
	}
	/** Copy numeric arrarys. */ 
	static public void MOVEA(INumeric a1[], int i1, long[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++] = a1[i1++].longValue();
	}
	/** Copy numeric arrarys. */ 
	static public void MOVEA(long a1[], int i1, INumeric[] a2, int i2)
	{
		int elems = java.lang.Math.min(a1.length - --i1, a2.length - --i2);
		for (int i = 0; i < elems; i++)
			a2[i2++].assign(a1[i1++]);
	}
	
	/** Copy arrays of the same type from 1-based index i1 to 1-based index i2 */
	static public void MOVEA(Object a1, int i1, Object a2, int i2)
	{
		int elems = java.lang.Math.min(Array.getLength(a1) - --i1,	Array.getLength(a2) - --i2);
		System.arraycopy(a1, i1, a2, i2, elems);
	}

	/** 
	 * Copy a string to a character array
	 * @param value The string to copy from
	 * @param a2 The character array to copy to
	 * @param i2 The offset (1-based) into the character array
	*/
	static public void MOVEA(String value, char[] a2, int i2)
	{
		MOVEA(value.toCharArray(), 1, a2, i2);
	}
	
	/** Fill all elements of array (beginning at 1-based index) with a character value. **/
	static public void MOVEALL(char c, char[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = c;
	}
	/** Fill all elements of array (beginning at 1-based index) with a double value. **/
	static public void MOVEALL(double value, double[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = value;
	}
	/** Copy a numeric value to all elements of a Numeric array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(double value, INumeric[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1].assign(value);
	}
	/* *
	 * Copy a numeric value to all elements of a Numeric array beginning at 1-based <code>index</code>.
	static public void MOVEALL(double value, INumeric[] array, int index)
	{
		for (int i = index; i < array.length; i++)
			array[i - 1].assign(value);
	}
	 */

	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(FigConst fc, char[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = fc.charAt(0);
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */ 
	static public void MOVEALL(FigConst fc, FixedChar[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1].assign(fc);
	}
	
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(FigConstNum fc, double[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = fc.doubleValue();
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(FigConstNum fc, int[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = fc.intValue();
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(FigConstNum fc, INumeric[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1].assign(fc);
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(FigConstNum fc, long[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = fc.longValue();
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(int value, int[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = value;
	}
	/** Copy a numeric value to all elements of a Numeric array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(INumeric value, INumeric[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1].assign(value);
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(long value, long[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = value;
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(short value, short[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = value;
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */
	static public void MOVEALL(boolean value, boolean[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1] = value;
	}
	/** Copy a value to all elements of an array beginning at 1-based <code>index</code>. */ 
	static public void MOVEALL(String str, FixedChar[] array, int index)
	{
		for (int i = index; i <= array.length; i++)
			array[i - 1].movel(str);
	}
	
	/**
	 * Move the left-most byte of a fixed-length value to a boolean (Indicator)
	 */
	static public boolean movel(FixedChar source, boolean dest)
	{
		if (movel(source, ' ') == '1')
			return true;
		return false;
	}
	/** Move the left-most characters of a value to a character destination. */
	static public char movel(FixedChar source, char dest)
	{
		return new FixedChar(1, dest).movel(source).charAt(0);
	}
	/** Move the left-most characters of a value to a Numeric destination. */
	static public double movel(FixedChar source, AbstractNumeric dest)
	{
		return ((INumeric) dest.movel(source)).doubleValue();
	}

	/**
	 * Return an OS/400-style date format string that will force the resulting value to 
	 * have no date separators in it.
	 */
	private static String numericDateFormat(String datfmt400) throws Exception
	{
		// Make sure that the date format ends with a '0' so that no separators get used
		if (datfmt400.charAt(datfmt400.length() - 1) != '0')
			datfmt400 = datfmt400 + "0";
		return datfmt400;
	}
	
	/** Return the occurrence of a multi-occur data structure. */
	public static int occur(DS value)
	{
		return value.Occur;
	}
	
	/** Returns whether the file is opened or not. */
	public static boolean open(Rfile file)
	{
		return file.opened; 
	}
	
	/** Return the value raised to the specified power. */
	static public double pow(double value, double power)
	{
		return java.lang.Math.pow(value, power);
	}
	
	/** Return the value raised to the specified power. */
	static public double pow(double value, INumeric power)
	{
		return pow(value, power.doubleValue());
	}
	
	/** Return the value raised to the specified power. */
	static public LongDecimal pow(INumeric value, double power)
	{
		double result = pow(value.doubleValue(), power);
		BigDecimal bd = ShortDecimal.newBigDecimal(result);
		LongDecimal raised = new LongDecimal(31,bd.scale(),bd);
		return raised;
	}
	
	/**
	 * Set the 'global' INKx values based upon cmdKey
	 * @param cmdKey "INKA"-"INKN", "INKP"-"INKY"
	 */
	/*static*/ void processCmdKey(String cmdKey)
	{
		int i = 0;
		try
		{
			i = Integer.parseInt(cmdKey.substring(2, 4));
		}
		catch (Exception e)
		{
		}
		INKA = (i == 1);
		INKB = (i == 2);
		INKC = (i == 3);
		INKD = (i == 4);
		INKE = (i == 5);
		INKF = (i == 6);
		INKG = (i == 7);
		INKH = (i == 8);
		INKI = (i == 9);
		INKJ = (i == 10);
		INKK = (i == 11);
		INKL = (i == 12);
		INKM = (i == 13);
		INKN = (i == 14);
		// There's a gap here for whatever reason (no INKO)
		INKP = (i == 15);
		INKQ = (i == 16);
		INKR = (i == 17);
		INKS = (i == 18);
		INKT = (i == 19);
		INKU = (i == 20);
		INKV = (i == 21);
		INKW = (i == 22);
		INKX = (i == 23);
		INKY = (i == 24);
	}

	/**
	 * Register the default driver
	 */
	public Driver registerDriver()
	{
		return registerDriver("*PROPERTY");
	}
	/**
	 * Register a driver for the specified class name.
	 */
	// This was made non-static so that classes that derive from Application can override this behavior.
	public Driver registerDriver(String className)
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Registering driver " + className);
		if (className.equals("*PROPERTY"))
		{
			// First, try to load from I2.properties
			ResourceBundle prop = getResourceBundle();
			className = prop.getString("DriverClass");
		}
		Class driverClass=null;
		try
		{
			driverClass= Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			I2Logger.logger.printStackTrace(e);
		}
		return registerDriver(driverClass);
	}
	/**
	 * Register a driver for the specified class. 
	 */
	public Driver registerDriver(Class driverClass)
	{
		try
		{
			Driver driver = (Driver) (driverClass.newInstance());
			return registerDriver(driver);
		}
		catch (Exception e)
		{
			I2Logger.logger.severe(e);
			return null;
		}
	}
	/**
	 * Register a driver for the specified driver.
	 */
	public Driver registerDriver(Driver driver)
	{
		return Application.registerI2Driver(driver);
	}
	
	/**
	 * Register a driver for the specified class name.
	 */
	static public Driver registerI2Driver(Driver driver)
	{
		try
		{
			DriverManager.registerDriver(driver);
			return driver;
		}
		catch (Exception e)
		{
			I2Logger.logger.severe(e);
			return null;
		}
	}

	/** 
	 * Returns the character string produced by inserting a replacement string 
	 * into the source string
	 */
	static public FixedChar replace(char replacementString, Object sourceString, int startPosition, int sourceLengthToReplace)
	{
		return replace(new Character(replacementString).toString(), sourceString, startPosition, sourceLengthToReplace);
	}
	/** 
	 * Returns the character string produced by inserting a replacement string 
	 * into the source string
	 */
	static public FixedChar replace(Object replacementString, Object sourceString, int startPosition, int sourceLengthToReplace)
	{
		String ss = sourceString.toString();
		int ssLength = ss.length();
		StringBuffer sb = new StringBuffer();
		// Build beginning of string
		if (startPosition>1)
			sb.append(ss.substring(0, startPosition-1));
		// Replace string
		String rs = replacementString.toString();
		sb.append(rs);
		// If sourceLengthToReplace > rsLength then pad with blanks
		int rsLength = sourceLengthToReplace - rs.length();
		for (int i=0; i<rsLength; i++)
			sb.append(' ');
		// Build end of string
		startPosition += sourceLengthToReplace;
		if (startPosition<=ssLength)
			sb.append(ss.substring(startPosition-1));
		return new FixedChar(sb.length(), sb.toString());
	}

	/** Scan for the first ocurrence of searchArgument, beginning at 1-based start. **/
	public int scan(char searchArgument, Object str) throws Exception
	{
		return scan(searchArgument, str, 1);
	}
	/** Scan for the first ocurrence of searchArgument, beginning at 1-based start. **/
	public int scan(char searchArgument, Object str, int start)
		throws Exception
	{
		int offset;
		if (str instanceof FixedChar)
			offset = ((FixedChar) str).scan(searchArgument);
		else if (str instanceof String)
			offset = ((String) str).indexOf(searchArgument, start - 1) + 1;
		else if (str instanceof char[])
		{
			char[] ca = (char[]) str;
			int caLength = ca.length;
			offset = 0;
			for (int i = 0; i < caLength; i++)
			{
				if (ca[i] == searchArgument)
				{
					offset = i + 1;
					break;
				}
			}
		}
		else
			throw new Exception("Unable to scan specified object type");
		return scanFound(offset);
	}
	/** Scan for the first ocurrence of searchArgument. **/
	public int scan(FixedChar searchArgument, Object str) throws Exception
	{
		return scan(searchArgument, str, 1);
	}
	/** Scan for the first ocurrence of searchArgument, beginning at 1-based start. **/
	public int scan(FixedChar searchArgument, Object str, int start)
		throws Exception
	{
		int offset;
		if (str instanceof FixedChar)
			offset = ((FixedChar) str).scan(searchArgument);
		else
		{
			// If this is a char[] array, then treat like a String
			if (str instanceof char[])
				str = new String((char[]) str);
			if (str instanceof String)
				offset =
					((String) str).indexOf(
						searchArgument.toFixedString(),
						start - 1)
						+ 1;
			else
				throw new Exception("Unable to scan specified object type");
		}
		return scanFound(offset);
	}
	/** Scan for the first ocurrence of searchArgument. **/
	public int scan(String searchArgument, Object str) throws Exception
	{
		return scan(searchArgument, str, 1);
	}
	/** Scan for the first ocurrence of searchArgument, beginning at 1-based start. **/
	public int scan(String searchArgument, Object str, int start)
		throws Exception
	{
		int offset;
		if (str instanceof FixedChar)
			offset = ((FixedChar) str).scan(searchArgument);
		else
		{
			// If this is a char[] array, then treat like a String
			if (str instanceof char[])
				str = new String((char[]) str);
			if (str instanceof String)
				offset = ((String) str).indexOf(searchArgument, start - 1) + 1;
			else
				throw new Exception("Unable to scan specified object type");
		}
		return scanFound(offset);
	}

	/**
	 * Update the global EQUAL value after a scan is performed.
	 */
	int scanEqual(int offset)
	{
		equal = (scanFound(offset)>0);
		return offset;
	}

	/**
	 * Update the global FOUND value after a scan is performed.
	 */
	int scanFound(int offset)
	{
		// Offset is 0-based instead of 1-based, so adjust here
		// offset++;
		found = (offset > 0);
		return offset;
	}
	
	/** Return a SECOND duration */
	static public Duration seconds(int seconds)
	{
		return new Duration(seconds, SECONDS);
	}
	/** Return a SECOND duration */
	static public Duration seconds(INumeric seconds)
	{
		return seconds(seconds.intValue());
	}

	/** 
	 * Set the key cache size for certain keyed database files opened by this application.
	 * @see RseekJDBC#loadKeyCache
	 * @deprecated
	 */
	protected static void setKeyCacheSize(int keyCacheSize) {
		//Application.keyCacheSize = keyCacheSize;
	}
	
	/** Return the size (in bytes) of a fixed-length variable. */
	public static int size(FixedData value)
	{
		return value.size();
	}

	/** Sort the elements of an array. */
	static public void SORTA(Comparable[] array)
	{
		java.util.Arrays.sort(array);
	}
	/** Sort the elements of an array. */
	static public void SORTA(double[] array)
	{
		java.util.Arrays.sort(array);
	}
	/** Sort the elements of an array. */
	static public void SORTA(int[] array)
	{
		java.util.Arrays.sort(array);
	}
	/** Sort the elements of an array. */
	static public void SORTA(long[] array)
	{
		java.util.Arrays.sort(array);
	}
	/** Sort the elements of an array. */
	static public void SORTA(short[] array)
	{
		java.util.Arrays.sort(array);
	}

	/**
	 * Retrieve a portion of a date (MONTHS, DAYS, YEARS).
	 * @param duration date
	 * @param durationCode the duration code: (Application.)SECONDS, MINUTES, HOURS, MONTHS, DAYS, YEARS
	 * @return int
	 */
	static public int subdt(FixedDate value, int durationCode) throws Exception
	{
		java.util.Date d = value.toDate();
		// Create a new Calendar and use the returned date to extract the specified portion
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		int duration = calendar.get(durationCode);
		// For whatever reason, the MONTH is 0-11 instead of 1-12
		if (durationCode == Application.MONTHS)
			duration++;
		// Translate microseconds to milliseconds (Application.MICROSECONDS=Calendar.MILLISECOND)
		else if (durationCode == Application.MSECONDS)
			duration = duration*1000 + value.getMicrorem();
		return duration;
	}

	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, int start)
	{
		String s = str.toString();
		return subst(s, start, s.length()-start+1);
	}
	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting 1-based position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, int start, int length)
	{
		String s = str.toString();
		//return new varying(length, s.substring(start-1, start+length));
		start--;
		return new FixedChar(length, s.substring(start, start+length));
	}
	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, int start, INumeric length)
	{
		return subst(str, start, length.intValue());
	}
	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, INumeric start)
	{
		return subst(str, start.intValue());
	}
	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, INumeric start, int length)
	{
		return subst(str, start.intValue(), length);
	}
	/**
	 * Return a substring of the specified String.
	 * @param str The Java String to substring
	 * @param start The starting position of the String to substring
	 * @param length The length of the substring to return
	 * @return the specified substring
	 */
	static public FixedChar subst(Object str, INumeric start, INumeric length)
	{
		return subst(str, start.intValue(), length.intValue());
	}
	
	/** 
	 * Test to see if a value is valid numeric data.
	 * @return 0 if blank, 1 if valid zone ('A'-'R'), -1 otherwise 
	 */
	static public int testn(char c)
	{
		if (c == ' ')
			return 0;
		if (java.lang.Character.isDigit(c) || (c > 'A' && c < 'R'))
			return 1;
		return -1;
	}
	/** 
	 * Test to see if a value is valid numeric data.
	 * @return 0 if blank, 1 if all numeric, >1 if all numeric with leading blanks, -1 otherwise 
	 */
	static public int testn(FixedChar fStr)
	{
		// A 1-character fixed result does a slightly different check
		if (fStr.len()==1)
			return testn(fStr.charAt(0));
		// Loop through the string and return the index of the first valid numeric digit
		int numIndex = 0;
		for (int i = 0; i < fStr.size(); i++)
		{
			char c = fStr.charAt(i);
			if (c != ' ')
			{
				if (!java.lang.Character.isDigit(c))
					return -1;
				if (numIndex==0)
					numIndex = i+1;
			}
			// There cannot be blanks imbedded in the numeric string
			else if (numIndex>0)
				return -1;
		}
		return numIndex;
	}
	
	/** 
	 * Test the zone portion of a character value.
	 * @return 1 if 'positive' ('A'-'I'), -1 if negative ('J'-'R'), 0 otherwise 
	 */
	static public int testz(char c)
	{
		if (c == '&' || (c >= 'A' && c <= 'I'))
			return 1;
		if (c == '-' || (c >= 'J' && c <= 'R'))
			return 1;
		return 0;
	}
	/** 
	 * Test the zone portion of a value.
	 * @see testz(char)
	 */
	static public int testz(FixedChar fStr)
	{
		return testz(fStr.charAt(0));
	}
	
	/** Return the current system timestamp */
	static public FixedTimestamp timestamp()
	{
		FixedTimestamp t = new FixedTimestamp();
		t.assign(new java.util.Date());
		return t;
	}
	
	/** 

	/**
	 * Trim blanks from both edges of a fixed-length string
	 * @param fStr The fixed-length string to trim
	 * @return java.lang.String the trimmed string
	 */
	static public String trim(FixedChar fStr)
	{
		return fStr.trim();
	}
	/**
	 * Trim blanks from the both ends of a string
	 */
	static public String trim(String value)
	{
		return value.trim();
	}
	/**
	 * Trim blanks from the left edge of a fixed-length string
	 * @param fStr The fixed-length string to trim
	 * @return java.lang.String the trimmed string
	 */
	static public String triml(FixedChar fStr)
	{
		return fStr.triml();
	}
	/**
	 * Trim blanks from the left edge of a string
	 */
	static public String triml(String value)
	{
		int valueLength = value.length();
		int i = 0;
		while (i < valueLength && value.charAt(i) == ' ')
			i++;
		if (i > 0)
			value = value.substring(i);
		return value;
	}
	/**
	 * Trim blanks from the right edge of a fixed-length string
	 * @param fStr The fixed-length string to trim
	 * @return java.lang.String the trimmed string
	 */
	static public String trimr(FixedChar fStr)
	{
		return fStr.trimr();
	}
	/**
	 * Trim blanks from the right edge of a fixed-length string
	 * @param fStr The fixed-length string to trim
	 * @return java.lang.String the trimmed string
	 */
	static public String trimr(String value)
	{
		int valueLength = value.length();
		int i = valueLength - 1;
		while (i >= 0 && value.charAt(i) == ' ')
			i--;
		i++;
		if (i < valueLength)
			value = value.substring(0, i);
		return value;
	}

	static public String trimr(char value)
	{
		return new Character(value).toString();
	}

	/** Return the fixed equivalent of a character value. */
	public static FixedChar fixed(char value)
	{
		return new FixedChar(1, value);
	}
	/** Return the fixed equivalent of a string. */
	public static FixedChar fixed(String value)
	{
		return new FixedChar(value.length(), value);
	}
	/** Return the fixed equivalent of a character array. */
	public static FixedChar fixed(char[] value)
	{
		return fixed(new String(value));
	}
	
	/** Return the sum all the elements of a numeric array. */
	static public double xfoot(double[] array)
	{
		/*
		double total = 0;
		for (int i = 0; i < array.length; i++)
			total += array[i];
		*/
		BigDecimal bd = ShortDecimal.newBigDecimal(array[0]);
		for (int i = 1; i < array.length; i++)
		{
			double d = array[i];
			if (d!=0)
				bd = bd.add(ShortDecimal.newBigDecimal(d));
		}
		return bd.doubleValue();
	}
	/** Return the sum all the elements of a numeric array. */
	static public double xfoot(int[] array)
	{
		double total = 0;
		for (int i = 0; i < array.length; i++)
			total += array[i];
		return total;
	}
	/** Return the sum all the elements of a numeric array. */
	static public double xfoot(INumeric[] array)
	{
		/*
		double total = 0;
		for (int i = 0; i < array.length; i++)
			total += array[i].doubleValue();
		return total;
		*/
		//BigDecimal bd = new BigDecimal(Double.toString(array[0].doubleValue()));
		BigDecimal bd = array[0].toBigDecimal();
		for (int i = 1; i < array.length; i++)
		{
			//double d = array[i].doubleValue();
			//if (array[i].intValue!=0)
				bd = bd.add(array[i].toBigDecimal());
		}
		return bd.doubleValue();
	}
	/** Return the sum all the elements of a numeric array. */
	static public double xfoot(long[] array)
	{
		double total = 0;
		for (int i = 0; i < array.length; i++)
			total += array[i];
		return total;
	}
	/** Return the sum all the elements of a numeric array. */
	static public double xfoot(short[] array)
	{
		double total = 0;
		for (int i = 0; i < array.length; i++)
			total += array[i];
		return total;
	}
	
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public String xlate(char from, char to, Object source)
		throws Exception
	{

		return xlate(from, to, source, 1);
	}
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public String xlate(char from, char to, Object source, int start)
		throws Exception
	{

		String xlt = source.toString().substring(start - 1).replace(from, to);
		return xlt;
	}
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public char xlate(char from, char to, char source)
		throws Exception
	{

		return xlate(from, to, new Character(source).toString(), 1).charAt(0);
	}
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public String xlate(char from, char to, Object source, INumeric start)
		throws Exception
	{

		return xlate(from, to, source, start.intValue());
	}
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public String xlate(char from, Object to, Object string)
		throws Exception
	{
		return xlate(from, to, string, 1);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(char from, Object to, Object string, int start)
		throws Exception
	{
		return xlate(new Character(from).toString(), to, string, start);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(
		char from,
		Object to,
		Object string,
		INumeric start)
		throws Exception
	{
		return xlate(from, to, string, start.intValue());
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(Object from, char to, Object string)
		throws Exception
	{
		return xlate(from, to, string, 1);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(Object from, char to, Object string, int start)
		throws Exception
	{
		return xlate(from, new Character(to).toString(), string, start);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(
		Object from,
		char to,
		Object string,
		INumeric start)
		throws Exception
	{
		return xlate(from, to, string, start.intValue());
	}
	/** Translate all the characters in string <code>source</code> from <code>from</code> to <code>to</code>.*/
	static public String xlate(Object from, Object to, Object string)
		throws Exception
	{
		return xlate(from, to, string, 1);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(Object from, Object to, Object string, int start)
		throws Exception
	{
		if (from instanceof char[])
			from = new String((char[]) from);
		if (to instanceof char[])
			to = new String((char[]) to);
		if (string instanceof char[])
			string = new String((char[]) string);
		return xlate(from.toString(), to.toString(), string.toString(), start);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(
		Object from,
		Object to,
		Object string,
		INumeric start)
		throws Exception
	{
		return xlate(from, to, string, start.intValue());
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(String from, String to, String source, int start)
		throws Exception
	{

		String resultString = source.substring(start - 1);
		int fromLength = from.length();
		for (int i = 0; i < fromLength; i++)
			resultString = resultString.replace(from.charAt(i), to.charAt(i));
		resultString = source.substring(0, start) + resultString;
		return resultString;
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public String xlate(String from, String to, String source)
		throws Exception
	{
		return xlate(from, to, source, 1);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public char xlate(String from, String to, char source, int start)
		throws Exception
	{
		return xlate(from, to, new Character(source).toString(), start).charAt(0);
	}
	/** Translate all the characters in string <code>source</code> beginning at the 1-based index <code>start</code> 
	 * from <code>from</code> to <code>to</code>. */
	static public char xlate(String from, String to, char source)
		throws Exception
	{
		return xlate(from, to, source, 1);
	}
	
	/** Return a YEAR duration */
	static public Duration years(int years)
	{
		return new Duration(years, YEARS);
	}
	/** Return a YEAR duration */
	static public Duration years(INumeric years)
	{
		return years(years.intValue());
	}
	
	/**
	 * For RPG Cycle processing, add a detail file/format pair. 
	 */
	public void addDetailFormat(RfilePrint file, RrecordPrint record)
	{
		detailFormats = addOutputFormat(detailFormats, file, record);
	}
	/**
	 * For RPG Cycle processing, add a output file and its associated overflow indicator.
	 */
	public void addOutputFile(RfilePrint file, String overflowIndicator)
	{
		file.setOverflowIndicator(overflowIndicator);
	}
	/**
	 * For RPG Cycle processing, add a total file/format pair. 
	 */
	private Vector addOutputFormat(
		Vector v,
		RfilePrint file,
		RrecordPrint record)
	{
		if (v == null)
			v = new Vector();
		v.addElement(new OutputFormat(file, record));
		return v;
	}
	/**
	 * For RPG Cycle processing, add a secondary file.
	 */
	public void addSecondaryFile(Rfile file, IRecord record)
	{
		cycleFiles.add(file);
		cycleFormats.add(record);
	}
	/** Add a file to the open file list. */
	//void addOpenFile(Rfile file)
	void addOpenFile(IClosable file)
	{
		boolean alreadyOpen = false;
		if (openFiles == null)
			openFiles = new Vector();
		else
			alreadyOpen = openFiles.contains(file);
		if (!alreadyOpen)
			openFiles.add(file);
	}
	
	/** Remove a closed file from the list of open files. */ 
	void removeClosedFile(Rfile file)
	{
		if (openFiles != null)
			openFiles.remove(file);
	}
	
	/**
	 * For RPG Cycle processing, add a total file/format pair. 
	 */
	public void addTotalFormat(RfilePrint file, RrecordPrint record)
	{
		totalFormats = addOutputFormat(totalFormats, file, record);
	}
	/**
	 * Cycle processing just before detail calculations are performed.
	 */
	public void detailCycle() throws Exception
	{
		// Make sure that the correct record format is set
		force(cycleFile);
		cycleFile.readx();
	}
	/**
	 * Force the next cycle to read from this file.
	 */
	public void force(RfileCycle file) throws Exception
	{
		if (cycleFile != file)
		{
			cycleFile = file;
			//IRecord cycleRecord = (IRecord) cycleFiles.get(file);
			cycleFileIndex = cycleFiles.indexOf(file);
		}
		IRecord cycleRecord = (IRecord)cycleFormats.elementAt(cycleFileIndex);
		cycleFile.setFormat(cycleRecord);
	}
	/**
	 * Return a default connection object.
	 */
	public I2Connection getConnection()
	{
		return getConnection("*PROPERTY", "*PROPERTY", "*PROPERTY");
	}
	/**
	 * Get a connection to the specified URL
	 * Creation date: (3/13/2002 8:56:34 AM)
	 */
	public I2Connection getConnection(String URL)
	{
		return getConnection(URL, "*PROPERTY", "*PROPERTY");
	}
	/**
	 * Get a connection object using the specified URL, user id, and password.
	 */
	public I2Connection getConnection(
		String url,
		String usrid,
		String password)
	{
		if (I2Logger.logger.isTraceable())
			I2Logger.logger.trace("Getting connection " + url);
		I2Connection rconn=null;
		try
		{
			// If no host/userid/password is specified, then try to load from properties file
			if (url.equals("*PROPERTY")
				|| usrid.equals("*PROPERTY")
				|| password.equals("*PROPERTY"))
			{
				// First, try to load from I2.properties
				ResourceBundle prop = getResourceBundle();
				if (password.equals("*PROPERTY"))
					password = prop.getString("HostPWD");
				if (usrid.equals("*PROPERTY"))
					usrid = prop.getString("HostUID");
				if (url.equals("*PROPERTY"))
					url = prop.getString("HostURL");
			}
			// Get the connection from the cache
			rconn = (I2Connection)retrieveI2Host(url, usrid, password);
		}
		catch (Exception e)
		{
			I2Logger.logger.printStackTrace(e);
		}
		return rconn;
	}
	
	/**
	 * Write out all cycle formats for the specified vector.
	 */
	private void outputFormats(Vector formats) throws Exception
	{
		// Write out all total formats
		if (formats != null)
		{
			int detailCount = formats.size();
			for (int i = 0; i < detailCount; i++)
			{
				OutputFormat of = (OutputFormat) formats.elementAt(i);
				of.file.setFormat(of.record);
				boolean overflow = of.file.write();
				// Set on appropriate overflow indicator 
				// don't turn OFF if !overflow? 
				if (of.file.overflowIndicator != null) 
				{
					if (of.file.overflowIndicator.equals("OA"))
						INOA = overflow;
					else if (of.file.overflowIndicator.equals("OB"))
						INOB = overflow;
					else if (of.file.overflowIndicator.equals("OC"))
						INOC = overflow;
					else if (of.file.overflowIndicator.equals("OD"))
						INOD = overflow;
					else if (of.file.overflowIndicator.equals("OE"))
						INOE = overflow;
					else if (of.file.overflowIndicator.equals("OF"))
						INOF = overflow;
					else if (of.file.overflowIndicator.equals("OG"))
						INOG = overflow;
					else if (of.file.overflowIndicator.equals("OV"))
						INOV = overflow;
				}
			}
		}
	}
	
	/** End the job that this program is associated with. */
	public void endjob() 
	{
		// Find the bottom-most application on the stack
		Application app=this;
		Application prvApp = app.prvApp();
		while (prvApp!=null)
		{
			app=prvApp;
			prvApp = app.prvApp();
		}
		try
		{
			app.finalize();
		}
		catch (Throwable e)
		{
			I2Logger.logger.printStackTrace(e);
		}
	}

	/** Reclaim all of the resources for this program (deactivate all programs that 
	 * this program has called that haven't returned with INLR=ON) */
	public void rclrsc()
	{
		// Deactivate any programs that this program has called
		if (calledApps != null)
		{
			int calledAppsSize = calledApps.size();
			// The elements get removed as they are deactivated, so do this backwards
			for (int i=calledAppsSize-1; i>=0; i--)
			{
				Application capp = (Application)calledApps.elementAt(i);
				if (!capp.INLR)
				{
					try
					{
						RETURNThread.deactivate(capp);
					}
					catch (Throwable e)
					{
						I2Logger.logger.printStackTrace(e);
					}
				}
			}
			calledApps.clear();
			//calledApps=null;
		}
	}
	
	// Can't just do RETURN(IHost) because previous version of I2 actually used Connection, not I2Connection
	public void RETURN(IRHost host) throws Pgmmsg
	{
		Rreturn(host);
	}
	
	protected void Rreturn(IRHost rhost) throws Pgmmsg
	{
		// Set job switches
		StringBuffer sws = new StringBuffer("00000000");
		if (INU1) sws.setCharAt(0,'1');
		if (INU2) sws.setCharAt(1,'1');
		if (INU3) sws.setCharAt(2,'1');
		if (INU4) sws.setCharAt(3,'1');
		if (INU5) sws.setCharAt(4,'1');
		if (INU6) sws.setCharAt(5,'1');
		if (INU7) sws.setCharAt(6,'1');
		if (INU8) sws.setCharAt(7,'1');
		appJob.jobSws = sws.toString();
		
		// Check to see if halt indicators are on
		try
		{
			checkHalt();
		}
		finally
		{
			if (INLR==ON)
			{
					// Deactivate this program
					RETURNThread.rreturn(this);

					// Remove the connection
					removeI2Host(rhost);
			}
		}
	}
	
	/** 
	 * Close all open files
	 * @return <code>true</code> if an error occurs, <code>false</code> otherwise 
	 * */ 
	public boolean closeAllFiles()
	{
		boolean closeError=false;
		if (openFiles != null)
		{
			int openFileCount = openFiles.size();
			// Start from the end and go backwards so that remove objects don't mess with count
			for (int i=openFileCount-1; i>=0; i--)
			{
				try
				{
					//Rfile file = (Rfile)openFiles.elementAt(i);
					IClosable file = (IClosable)openFiles.elementAt(i);
					file.close();
					/*
					// Remove references to app object
					if (file.irecord != null)
						file.irecord.setApp(null);
					file.app = null;
					*/
				}
				catch (Throwable x) 
				{
					closeError=true;
				}
			}
			openFiles.clear();
		}
		return closeError;
	}
	
	/**
	 * For RPG Cycle processing, set the primary file.
	 */
	public void setPrimaryFile(RfileCycle file, IRecord record) throws Exception
	{
		cycleFiles = new Vector();
		cycleFormats = new Vector();
		addSecondaryFile(file, record);
		force(file);
	}

	/**
	 * Add this application to the call stack.
	 */
	void addToCallStack(Application prvApp)
	{
		// Add this to previous apps listed of called programs
		if (prvApp!=null)
		{
			// Add link to previous app
			this.prvAppRef = new WeakReference(prvApp);
			if (prvApp.calledApps==null)
				prvApp.calledApps = new Vector();
			prvApp.calledApps.add(this);
			threadLock = prvApp.threadLock;
		}
		/*
		else
			prvAppRef = null;
		*/
	}

	/** Check to see if any halt indicators are on. */
	private void checkHalt() throws Pgmmsg
	{
		char h='0';
		if (INH1)
			h='1';
		else if (INH2)
			h='2';
		else if (INH3)
			h='3';
		else if (INH4)
			h='4';
		else if (INH5)
			h='5';
		else if (INH6)
			h='6';
		else if (INH7)
			h='7';
		else if (INH8)
			h='8';
		else if (INH9)
			h='9';
		// If a halt indicator is on, exit from program
		if (h!='0')
		{
			INLR=ON;
			FixedChar msgDta = new FixedChar(50);
			msgDta.setCharAt(49, h);
			throw new Pgmmsg("RPG0233", "QRPGMSGE", msgDta);
		}
	}

	/**
	 * Cycle processing just before total calculations.
	 */
	public void totalCycle() throws Exception
	{
		// Write out all detail/heading formats
		outputFormats(detailFormats);
		// On first print, IN1P=ON, all other IN1P=OFF
		IN1P = OFF;
		
		// Check to see if any halt indicators are on
		checkHalt();
		
		// Set off all control level indicators
		INL1 = OFF;
		INL2 = OFF;
		INL3 = OFF;
		INL4 = OFF;
		INL5 = OFF;
		INL6 = OFF;
		INL7 = OFF;
		INL8 = OFF;
		INL9 = OFF;
		// If this is the first cycle, then read a record from each secondary file
		/* The RPG doc says to do this, but the programs don't behave this way???
		int cycleSize = cycleFiles.size();
		if (m_firstCycle)
		{
			m_firstCycle=false;
			for (int i=1; i<cycleSize; i++)
			{
				force((RfileCycle)cycleFiles.elementAt(i));
				INLR = !cycleFile.readCycle();
				if (!INLR)
					cycleFile.irecord.control();
			}
			// Reset to primary file
			cycleFile = (RfileCycle)cycleFiles.elementAt(0);
			cycleFileIndex=0;
		}
		*/
		// Make sure that the correct record format is set
		force(cycleFile);
		// Get input record
		INLR = !cycleFile.readCycle();
		// If INLR is on, then see if other (secondary) files need to be processed
		if (INLR)
		{
			int cycleSize = cycleFiles.size();
			do
			{
				if (cycleFileIndex+1>=cycleSize)
					break;
				cycleFileIndex++;
				force((RfileCycle)cycleFiles.elementAt(cycleFileIndex));
				INLR = !cycleFile.readCycle();
			} while(INLR);
		}
		// Set on control level indicators
		if (INLR)
			INL9 = ON;
		else
			cycleFile.irecord.control();
		if (INL9 == ON)
			INL8 = ON;
		if (INL8 == ON)
			INL7 = ON;
		if (INL7 == ON)
			INL6 = ON;
		if (INL6 == ON)
			INL5 = ON;
		if (INL5 == ON)
			INL4 = ON;
		if (INL4 == ON)
			INL3 = ON;
		if (INL4 == ON)
			INL3 = ON;
		if (INL3 == ON)
			INL2 = ON;
		if (INL2 == ON)
			INL1 = ON;
		INL0 = ON;
	}
	/**
	 * Perform total output for cycle processing.
	 */
	public void totalOutput() throws Exception
	{
		// Write out all total formats
		outputFormats(totalFormats);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
	}
	
	/** Create a new thread instance of this object and run it. */
	public void start()
	{
		new Thread(this).start();
	}
	
	/** Return the previous app in the call stack. */
	public Application prvApp()
	{
		Application p=null;
		if (prvAppRef!=null)
			p = (Application)prvAppRef.get();
		return p;
	}
	
	/** Return the request object (HttpServletRequest, if any) associated with this application. */
	public Object getRequest()
	{
		HttpServletRequest request = null;
		if (threadLock instanceof ThreadLockServlet)
			request = ((ThreadLockServlet)threadLock).getRequest();
		return request;
	}
	
	public FixedPointer overlay(FixedData overlaidField, int offset)
	{
		return new FixedPointer(overlaidField, offset-1);
	}
	
	public FixedPointer overlay(FixedData overlaidField)
	{
		return overlay(overlaidField, 1);
	}
	
	// Set the Program Status data structure
	public void setPSDS(FixedChar psds)
	{
		this.psds=psds;
		int pLength = psds.len();
		// Set program name
		if (pLength>=10)
		{
			FixedChar f1 = new FixedChar(10);
			String className = this.getClass().getName();
			int i=className.lastIndexOf('.');
			if (i>0)
				className = className.substring(i+1);
			f1.assign(className);
			psds.setFixedAt(0, f1);
			// Set Date
			if (pLength>=198)
			{
				psds.setFixedAt(190, DATE);
				// Set 2-digit year
				if (pLength>=200)
				{
					psds.setFixedAt(198, YEAR);
					
					// Set job name (always 'I2')
					if (pLength>=253)
					{
						Rtvjoba rtvjoba=new Rtvjoba(defaultI2Connection());
						f1.assign(rtvjoba.job());
						psds.setFixedAt(243, f1);
						
						// Set user name
						if (pLength>=263)
						{
							try
							{
								f1.assign(rtvjoba.curuser());
								psds.setFixedAt(253, f1);
							}
							catch (Pgmmsg e)
							{
								I2Logger.logger.printStackTrace(e);
							}

							// Set job number
							if (pLength>=269)
							{
								ZonedDecimal z1 = new ZonedDecimal(6,0);
								z1.assign(rtvjoba.nbr());
								psds.setFixedAt(263, z1);

								// Set UDATE
								if (pLength>=275)
								{
									psds.setFixedAt(269, UDATE);

									// System DATE (fudge with UDATE)
									if (pLength>=281)
									{
										psds.setFixedAt(275, UDATE);

										// Set TIME
										if (pLength>=287)
										{
											psds.setFixedAt(281, TIME);
								
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	boolean initialCall=true;
	/** Call INZSR on initial call, only. */
	public boolean isInitialCall()
	{
		boolean b = initialCall;
		initialCall=false;
		return b;
	}
	
	// Return number of parameters passed to routine
	int parms;
	public int setParms(int parms)
	{
		this.parms=parms;
		return parms;
	}
	public int parms()
	{
		return parms;
	}
	
	/* This is an aborted attempt to make the file access generic so that JDBC vs 400 access could be 
	 * decided at run-time, instead of at conversion time; class structure really doesn't support it...
	public IRHost getDefaultHost() {
		ResourceBundle prop = getResourceBundle();
		String hostType = prop==null ? "JDBC" : prop.getString("HostType");
		if ("AS400".equals(hostType)) 
			return getAS400();
		return getConnection();
	}
	
	public IDiskFile newDiskFile(AS400 as400, String fileName) {
		return new RfileDB400(as400, fileName);
	}
	public IDiskFile newDiskFile(Connection connection, String fileName) {
		return new RfileJDBC(connection, fileName);
	}
	public IDiskFile newDiskFile(IRHost host, String fileName) {
		Object hostObject = host.getHost();
		if (host instanceof I2AS400)
			return newDiskFile((AS400)hostObject, fileName);
		else if (host instanceof I2Connection)
			return newDiskFile((Connection)hostObject, fileName);
		return null;
	}
	
	public IKeyedFile newKeyedFile(AS400 as400, String fileName) {
		return new Rindex400(as400, fileName);
	}
	public IKeyedFile newKeyedFile(Connection connection, String fileName) {
		return new RindexJDBC(connection, fileName);
	}
	public IKeyedFile newKeyedFile(IRHost host, String fileName) {
		Object hostObject = host.getHost();
		if (host instanceof I2AS400)
			return newKeyedFile((AS400)hostObject, fileName);
		else if (host instanceof I2Connection)
			return newKeyedFile((Connection)hostObject, fileName);
		return null;
	}
	*/
	
	
	/** Return a temporary zoned object that can be used by print(), etc. */
	/* After further consideration, the performance gain (very slight) here is
	 * offset by the fact that this is not thread safe.  Ditch it.
	public zoned zoned(int length, int scale, double value)
	{
		// If a temporary zoned object doesn't already exist, then create it now 
		if (_zonedValue==null)
			_zonedValue = new zoned(length, scale);
		// If it does exist, then see if the overlay is the correct length
		else
		{
			if (_zonedValue.overlay.length != length)
			{
				// if an overlay of the correct length doesn't already exist, then create it
				if (_zonedOverlays[length]==null)
					_zonedOverlays[length] = new byte[length];
				_zonedValue.overlay = _zonedOverlays[length];
			}
			_zonedValue.setScale(scale);
		}
		_zonedValue.assign(value);
		return _zonedValue;
	}
	 */
	 
	/*
	static public void main(String[] args) throws Exception
	{
		Application app = new Application(null);
		long l = app.TIME.intValue();
		l = app.UTIMESTAMP.longValue();
		l=app.TIMESTAMP.longValue();
		zoned z = new zoned(12,0);
		z.assign(app.UTIMESTAMP);
		packed p = new packed(14,0);
		p.assign(app.TIMESTAMP);
		date date1 = new date();
		date1.assign(timestamp());
		time time1 = new time();
		time1.assign(timestamp());
		timestamp ts1 = new timestamp();
		ts1.assign(timestamp());
	}
	*/
}
