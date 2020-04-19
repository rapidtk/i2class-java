/*
 * Created on Nov 29, 2004
 */
package com.i2class.msgf;

import java.util.*;

import com.i2class.*;

/**
 * Run-time messages issued by RPG programs. 
 * 
 */
public class QRPGMSGE extends MessageFile
{
	protected void addMessages()
	{
		addMsgd("RPG0233","Program stopped with halt indicator H&5 on",
				  "",40);
		addMsgd("RPG1211","I/O operation was applied to closed file &5.",
				  "&N Cause . . . . . :   The program attempted to use file &5 while the file was closed. &N Recovery  . . . :   Change the program "+
				  "so that the file is opened before the I/O operation is performed.",99);
 	}
	public QRPGMSGE()
	{
		addMessages();
		MessageDescription msgd;
		msgd=(MessageDescription)handleGetObject("RPG0233");
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",19,0);
		msgd.addFmt("*CHAR",1,0);
		msgd=(MessageDescription)handleGetObject("RPG1211");
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",10,0);
		msgd.addFmt("*CHAR",19,0);
		msgd.addFmt("*CHAR",10,0);
	}
}
