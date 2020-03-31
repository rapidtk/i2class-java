package com.i2class;

import java.util.*;

/**
 * 
 * A synchronized host pool object. 
 * 
 * @author ANDREWC
 */
class HostPool extends Hashtable {

	boolean hostPoolLocked;
	public HostPool() {
		super();
	}

	/** Get lock to host pool */
	synchronized void lockHostPool() 
	{
		if (hostPoolLocked)
		{
			try {
				wait(60000);
			} 
			catch (InterruptedException e) 
			{
				I2Logger.logger.printStackTrace(e);
			}
		}
		hostPoolLocked=true;
	}

	/** Unlock host pool */
	synchronized void unlockHostPool()
	{
		hostPoolLocked=false;
		notifyAll();
	}

}
