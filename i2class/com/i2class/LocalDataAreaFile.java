package com.i2class;

import java.io.*;
/**
 * Simulate the local data area (*LDA) by using a temporary local file.
 * @author Andrew Clark
 */
class LocalDataAreaFile extends CharacterDataAreaFile
{
	private static File tmpFile;
	/**
	 * Create a local *LDA data area object.
	 */
	LocalDataAreaFile()
	{
		// Create a temporary file that gets deleted on exit that is shared between all objects in the JVM
		try
		{
			if (tmpFile == null)
			{
				tmpFile = File.createTempFile("LDA", ".dtaara");
				// Delete the file when the JVM exits
				tmpFile.deleteOnExit();
			}
			file = new RandomAccessFile(tmpFile, "rw");
		}
		catch (Exception e)
		{
		}
	}
}
