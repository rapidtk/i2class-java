/*
 * Created on Nov 29, 2004
 */
package com.i2class.msgf;

import java.util.*;

import com.i2class.*;

/**
 * The base message file for all OS/400 messages. 
 * 
 */
public class QCPFMSG extends MessageFile
{
	protected void addMessages()
	{
		addMsgd("CPC0904", "Data area &1 created in library &2.", "", 0);
		addMsgd(
			"CPC2196",
			"Library &1 added to library list.",
			"&N Cause . . . . . :   If the ADDLIBLE command was used, &1 was added to the user library list.  If the CHGSYSLIBL command was used, &1 was added to the system portion of "
				+ "the library list.",
			0);
		addMsgd(
			"CPC2197",
			"Library &1 removed from library list.",
			"&N Cause . . . . . :   If the RMVLIBLE command was used, &1 was removed from the user library list.  If the CHGSYSLIBL command was used, &1 was removed from the system portion "
				+ "of the library list.",
			0);
		addMsgd("CPC7305", "Member &1 added to file &2 in &3.", "", 0);
		addMsgd("CPC9801", "Object &2 type *&5 created in library &3.", "", 0);
		addMsgd(
			"CPF0001",
			"Error found on &1 command.",
			"&N Cause . . . . . :   The system detected errors in the command. &N Recovery  . . . :   See the previously listed messages in the job log. Correct the errors and then try "
				+ "the command again.  *N instead of a command name means that the name had not been determined before the error was found.",
			30);
		addMsgd(
			"CPF0006",
			"Errors occurred in command.",
			"&N Cause . . . . . :   If the wrong length was passed on one part of the command, other messages are issued because the wrong part of the command is being analyzed. &N Recovery "
				+ " . . . :   See the previously listed messages in the job log. Correct the errors and then try the command again.",
			30);
		addMsgd(
			"CPF1002",
			"Cannot allocate object &1.",
			"&N Recovery  . . . :   Unable to allocate object &1. Try the ALCOBJ command again when the object is available.",
			40);
		addMsgd(
			"CPF1015",
			"Data area &1 in &2 not found.",
			"&N Recovery  . . . :   Either correct the data area name or change the library name (DTAARA parameter).  Then try the request again.",
			40);
		addMsgd(
			"CPF1023",
			"Data area &1 exists in &2.",
			"&N Cause . . . . . :   Data area &1 already exists in library &2. &N Recovery  . . . :   Either change the data area or the library name (DTAARA parameter) and then try the "
				+ "command again.",
			40);
		addMsgd(
			"CPF2103",
			"Library &1 already exists in library list.",
			"&N Cause . . . . . :   The library &1 already exists in the library search list. The library was not added. &N Recovery  . . . :   If &1 is the library you specified omit "
				+ "the command. If you did not specify library &1 then change the library name (LIB parameter). Then try the command again.",
			40);
		addMsgd(
			"CPF2104",
			"Library &1 not removed from the library list.",
			"&N Cause . . . . . :   Library &1 is not in the user portion (type USR) of the library list and therefore cannot be removed. &N Recovery  . . . :   Display the library list "
				+ "(DSPLIBL command) to determine if the library name specified is correct or if the specified library is in the system portion (type SYS) of the library list.  If the library "
				+ "is a product library (type PRD), the library will be removed when the command completes. If the library is in the current library position (type CUR) of the library list, "
				+ "use the CHGCURLIB command to remove the library from the current library position of the library list.  If the library name is not correct, change the library name and try "
				+ "the command again.",
			40);
		addMsgd(
			"CPF2105",
			"Object &1 in &2 type *&3 not found.",
			"&N Cause . . . . . :   No object was found for the name or type specified. &N Recovery  . . . :   Specify the correct name or type of the object. Then try the request again.",
			40);
		addMsgd(
			"CPF2112",
			"Object &1 in &2 type *&3 already exists.",
			"&N Recovery  . . . :   Do one of the following and try the request again: &P -- Change the object or library name (OBJ parameter). &P -- Delete the existing object. &P -- "
				+ "For the QLIRNMO API, specify '1' for the replace object option to replace the existing object.",
			30);
		addMsgd(
			"CPF2196",
			"Library value cannot be a special value.",
			"&N Cause . . . . . :   Library value must be a unique library name if *ALL is specified for the object name (OBJ) parameter. &N Recovery  . . . :   Do one of the following "
				+ "and try the request again: &P -- Specify a unique library name. &P -- Do not specify the *ALL value for the object name.",
			30);
		addMsgd("CPF2619","Table &1 not found.",
				  "&N Cause . . . . . :   The table &1 does not exist. &N Recovery  . . . :   Change the table name or library name in the calling program.",40);
		addMsgd("CPF2817","Copy command ended because of error.",
				  "&N Cause . . . . . :   An error occurred while the file was being copied. &N Recovery  . . . :   See the messages previously listed.  Correct the errors, and then try the "+
				  "request again.",40);
		addMsgd(
			"CPF3141",
			"Member &2 not found.",
			"&N Cause . . . . . :   Member &2 not found in file &1 in library &3. &N Recovery  . . . :   Specify the TYPE(*MBRLIST) parameter on the DSPFD command to see a list of the "
				+ "members that belong to file &1 in library &3. Change the member name and try the request again.",
			40);
		addMsgd(
			"CPF7306",
			"Member &1 not added to file &2 in &3.",
			"&N Cause . . . . . :   The member was not added to the file because of errors. &N Recovery  . . . :   See the error messages previously listed.  Correct the errors, and then "
				+ "try the request again.",
			40);
		addMsgd(
			"CPF7310",
			"Member &1 not removed from file &2 in &3.",
			"&N Cause . . . . . :   The member was not removed from the file because of errors. &N Recovery  . . . :   See the error messages previously listed.  Correct the errors, and "
				+ "then try the request again.",
			40);
		addMsgd(
			"CPF8350",
			"Commitment definition not found.",
			"&N Cause . . . . . :   A commitment control operation was requested, however a commitment definition could not be found for the requesting program at the activation-group-level "
				+ "nor at the job-level. A commitment definition must be created before commitment control operations can be requested. &N Recovery  . . . :   Use the STRCMTCTL command to create "
				+ "a commitment definition. Then try your request again. &P Specify CMTSCOPE(*JOB) on the STRCMTCTL command if the desired commit scope for the commitment definition should "
				+ "be at the job-level. Otherwise, the default commit scope is at the activation-group-level. &N Technical description . . . . . . . . :   The commitment definition identifier "
				+ "is &2. The activation-group number is &3.",
			40);
		addMsgd(
			"CPF8351",
			"Commitment control already active.",
			"&N Cause . . . . . :   A STRCMTCTL command was run to start commitment control for commitment definition &1 when it was already active. &N Recovery  . . . :   If the commitment "
				+ "definition specified in this message is *JOB and CMTSCOPE(*JOB) was specified on the STRCMTCTL command, then use the ENDCMTCTL command to end commitment control before trying "
				+ "to start commitment control again. &P If the commitment definition specified in this message is *JOB and CMTSCOPE(*ACTGRP) was specified on the STRCMTCTL command, then the "
				+ "application has already performed commitment control activity for the *JOB commitment definition and is not allowed to start commitment control using CMTSCOPE(*ACTGRP). In "
				+ "this case, change the application to not perform any commitment control activity prior to using the STRCMTCTL command with CMTSCOPE(*ACTGRP) specified. &P If the commitment "
				+ "definition specified in this message is not *JOB, then commitment control is already active at the activation group-level. Use the ENDCMTCTL command to end commitment control "
				+ "before trying to start commitment control again. &N Technical description . . . . . . . . :   The commitment definition identifier is &2. The activation-group number is &3.",
			40);
		addMsgd(
			"CPF9801",
			"Object &2 in library &3 not found.",
			"&N Cause . . . . . :   The object &2 in library &3 type *&5 not found.  The object name, library name, or the object type is not correct.  If the library name is not specified, "
				+ "the object may be in a library that is not contained in the library list. &N Recovery  . . . :   Correct the object name, library name, or object type.  If the library name "
				+ "was not specified, specify the library name and try the request again.",
			40);
		addMsgd(
			"CPF9812",
			"File &1 in library &2 not found.",
			"&N Cause . . . . . :   The file name or the library name is not correct.  If the library name was not specified, the file may be in a library that is not contained in the "
				+ "library list. &N Recovery  . . . :   Correct the file or library name, or add the library name to the library list.  Then try the request again.",
			40);
		addMsgd(
			"CPF9815",
			"Member &5 file &2 in library &3 not found.",
			"&N Cause . . . . . :   The member &5 in file &2 in library &3 was not found.  The member specified in the command was not found. If the member name is *N, there are no members "
				+ "in the file. &N Recovery  . . . :   Add the member (ADDLFM command or ADDPFM command) and try the request again.",
			40);
		addMsgd(
			"CPF9841",
			"Override not found at specified level.",
			"&N Cause . . . . . :   Overrides not found are: &2 &3 &4 &5 &6 &N Recovery  . . . :   If the program device names or the file names are not correct, change the program device "
				+ "name or the file name and try the request again. If the program device name or the file name is correct, do not use the Delete Override Device Entry (DLTOVRDEVE) or the Delete "
				+ "Override (DLTOVR) command.",
			40);
		addMsgd(
			"CPF9870",
			"Object &2 type *&5 already exists in library &3.",
			"&N Cause . . . . . :   The specified object &2 type *&5 already exists in library &3. &N Recovery  . . . :   Change the object or library name, or delete the object. Then "
				+ "try the request again.",
			40);
		addMsgd("CPF9898","&1.",
				  "&N Cause . . . . . :   This message is used by application programs as a general escape message.",40);
		addMsgd("CPF9899","Error occurred during processing of command.",
				  "&N Recovery  . . . :   See the previously listed messages in the job log.  Correct the errors and then try the request again.",40);
		addMsgd("CPF9999","Function check. &1 unmonitored by &2 at statement &5, instruction &3.",
				  "&N Cause . . . . . :   An escape exception message was sent to a program which did not monitor for that message. The full name of the program to which the unmonitored message "+
				  "was sent is &6 &8 &12. At the time the message was sent the program was stopped at higher level language statement number(s) &11. If more than one statement number is shown, "+
				  "the program was a bound program. Optimization does not allow a single statement number to be determined. If *N is shown as a value, it means the actual value was not available. "+
				  "&N Recovery  . . . :   See the low level messages previously listed to locate the cause of the function check.  Correct any errors, and then try the request again.",40);
	}
	public QCPFMSG()
	{
		addMessages();
		MessageDescription msgd;
		msgd= (MessageDescription)handleGetObject("CPC0904");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPC2196");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPC2197");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPC7305");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPC9801");
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 7, 0);
		msgd= (MessageDescription)handleGetObject("CPF0001");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF1002");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF1015");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF1023");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF2103");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF2104");
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF2105");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 7, 0);
		msgd= (MessageDescription)handleGetObject("CPF2112");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 7, 0);
		msgd=(MessageDescription)handleGetObject("CPF2619");
		msgd.addFmt("*CHAR",10,0);
		msgd=(MessageDescription)handleGetObject("CPF2817");
		msgd.addFmt("*CHAR",7,0);
		msgd= (MessageDescription)handleGetObject("CPF3141");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF7306");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF7310");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF8350");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*HEX", 10, 0);
		msgd.addFmt("*HEX", 4, 0);
		msgd= (MessageDescription)handleGetObject("CPF8351");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*HEX", 10, 0);
		msgd.addFmt("*HEX", 4, 0);
		msgd= (MessageDescription)handleGetObject("CPF9801");
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 7, 0);
		msgd= (MessageDescription)handleGetObject("CPF9812");
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF9815");
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF9841");
		msgd.addFmt("*BIN", 2, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd= (MessageDescription)handleGetObject("CPF9870");
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 10, 0);
		msgd.addFmt("*CHAR", 0, 0);
		msgd.addFmt("*CHAR", 7, 0);
		msgd=(MessageDescription)handleGetObject("CPF9898");
		msgd.addFmt("*CHAR",512,0);
		msgd=(MessageDescription)handleGetObject("CPF9999");
		msgd.addFmt("*CHAR",7,0);
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*HEX",2,0);
		msgd.addFmt("*CHAR",4,0);
		msgd.addFmt("*CHAR",10,0);
	}
}
