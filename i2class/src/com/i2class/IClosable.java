/*
 * Created on Aug 7, 2006
 *
 */
package com.i2class;

/**
 * An interface to any object (file, dataarea) that is closable by Application.closeAllFiles.
 * 
 *
 */
public interface IClosable
{
	public void close() throws Exception;

}
