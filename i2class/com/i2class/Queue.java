package com.i2class;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * First-in first-out (FIFO) queue with optional keys and wait time.
 * 
 * 
 *
 */
class Queue {
	/** The actual data stored in the queue. */
	Vector m_queueData = new Vector();
	/** Hash of key data to integer index */
	TreeMap m_keys = new TreeMap();
	// The beginning and end of non-null data
	int m_begin=-1;
	// The smallest timeout value
	long m_timeout=-1;
	// The system timestamp when the waiting began
	long m_beginMillis, m_expiredMillis;
	
	
	class QueueElement
	{
		Object m_data;
		FixedData m_key;
		
		QueueElement(Object data, FixedData key)
		{
			m_data = data;
			if (key!=null)
				key = (FixedData)key.clone();
			m_key = key;
		}
	}

	/** The actual implementation of the data send method.  */
	synchronized private void sendDataQueueElement(QueueElement data)
	{
		// Update beginning and ending positions of non-null data
		int qsize = m_queueData.size();
		if (m_begin==-1)
			m_begin=qsize;
		// Add data to the end of the list
		m_queueData.add(data);
		// Notify any threads that may be waiting for data
		notifyAll();
	}
	
	/** Send data to this queue. */
	public void sendData(FixedData data)
	{
		sendObject(data.clone());
	}
	/** Send and object to this queue. */
	void sendObject(Object data)
	{
		sendDataQueueElement(new QueueElement(data, null));
	}
	
	/** Send data to this queue with the specified key value. */
	public void sendData(FixedData data, FixedData key)
	{
		sendObject(data.clone(), key);
	}
	/** Send data to this queue with the specified key value. */
	public void sendObject(Object data, FixedData key)
	{
		//	Since this is a 0-based index, the size() before entry is the correct value
		Integer newIndex = ShortDecimal.newInteger(m_queueData.size());
		// See if a value already exists for the specified key
		Object index = m_keys.remove(key);
		// If there is one, replace with a Vector entry of values...
		if (index != null)
		{
			Vector v;
			if (!(index instanceof Vector))
				v = new Vector();
			else
				v = (Vector)index;
			v.add(newIndex);
			index = v;
		}
		// ...otherwise, generate a new index entry
		else		
			index = newIndex;
		// Actually send the data
		QueueElement dqe = new QueueElement(data, key);
		// We don't want to put the key itself but rather a clone of the object...
		//m_keys.put(dqe.m_key, index);
		m_keys.put(dqe.m_key.clone(), index);
		sendDataQueueElement(dqe);
	}
	
	/** 
	 * Receive an object from this queue. 
	 * @param wait the number of secods to wait for data: &lt;0=wait forever
	 * @return the object off of the queue 
	 */
	public Object receiveObject(int waitTime)  
	{
		// If no data is available, then wait.  
		// The Java timeout is different than the data queue wait time, 0=wait forever, >0= time in milliseconds 
		QueueElement dqe=null;
		if (m_begin<0)
		{
			if (waitTime != 0)
			{
				long timeout;
				if (waitTime<0)
					timeout=0;
				else
					timeout = waitTime * 1000;
				// Keep looping until data is available
				do
				{
					setTimeout(timeout);
					timeout=waitThread(timeout);
					// Some other thread may grab the data before we get to it, so dqe has to be checked
					if (m_begin>=0)
						dqe = remove(m_begin);
				} while(timeout>=0 && dqe==null);
			}
		}
		else
			dqe = remove(m_begin);
		// If no data is available, just return 0
		if (dqe==null)
			return null;
		return dqe.m_data;
	}
	
	/** 
	 * Receive keyed data from this queue. 
	 * @param wait the number of secods to wait for data: &lt;0=wait forever
	 * @param key the value of the key to search on
	 * @param keyOrder the key order to search (EQ, NE, LE, LT, GE, GT)   
	 * @return the object associated with the key
	 */
	public Object receiveObject(int waitTime, FixedData key, String keyOrder)  
	{
		Integer index=null;
		// Retrieve the index associated with the specified key
		if (m_begin>=0)
			index = removeKey(key, keyOrder);
		QueueElement dqe = null;
		// If no data is available, then wait.  
		// The Java timeout is different than the data queue wait time, 0=wait forever, >0= time in milliseconds 
		if (index==null)
		{
			if (waitTime!=0)
			{
				long timeout;
				if (waitTime<0)
					timeout=0;
				else
					timeout = waitTime * 1000;
				// Keep looping until data is available
				do
				{
					setTimeout(timeout);
					timeout=waitThread(timeout);
					// Retrieve the index associated with the specified key
					if (m_begin>=0)
					{
						index = removeKey(key, keyOrder);
						dqe = remove(index.intValue());
					}
				} while(timeout>=0 && dqe==null);
			}
		}
		else
			dqe = remove(index.intValue());
		// If no data is available, just return 0
		if (dqe==null)
			return null;
			
		return dqe.m_data;
	}
	
	private Integer removeKey(FixedData key, String keyOrder)
	{
		FixedData searchKey=null;
		// Find key with exact value (EQ)		
		if (keyOrder.charAt(0)=='E')
			searchKey=key;
		else
		{
			// Find first entry not equal to key (NE)
			if (keyOrder.charAt(0)=='N')
			{
				Iterator it = m_keys.keySet().iterator();
				while (it.hasNext())
				{
					FixedData f = (FixedData)it.next();
					if (!f.equals(key))
					{
						searchKey = f;
						break;
					}
				}
			}
			// For anything but equal (EQ) comparison, generate a subset of keys and take the first entry
			else 
			{
				FixedData subKey = key;
				// headMap always returns LT and tailMap always returns GE so we have to calculate the successor for LE/GT
				if (keyOrder.compareTo("LE")==0 || keyOrder.compareTo("GT")==0)
				{
					subKey = new FixedChar(key.size()+1);
					subKey.movel(key);
					subKey.move('\0');
				}

				SortedMap sm;
				// LT (less than) or LE (less than equal)
				if (keyOrder.charAt(0)=='L')
					sm=m_keys.headMap(subKey);
				// GT (greater than) or GE (greater than equal)
				else //if (keyOrder.charAt(0)=='G')
					sm=m_keys.tailMap(subKey);
				searchKey = (FixedData)sm.firstKey();
			}
		}
		// Get object corresponding to key (EQ)
		Object index = null;
		if (searchKey != null)
		{
			index=m_keys.remove(searchKey);
			// There may be multiple entries for the same key value, if so the returned object is a Vector
			if (index!=null && index instanceof Vector)
			{
				Vector v = (Vector)index;
				index = v.remove(0);
				// If there are still entries in the vector, then reinsert it back into key
				if (v.size()>0)
					m_keys.put(key, v);
			}
		}
		return (Integer)index;
	}
	
	/** 
	 * Remove data from the queue at the specified index.
	 * @return the data at the specified index
	 */
	synchronized private QueueElement remove(int index)
	{
		QueueElement dqe = null;
		int qsize = m_queueData.size();
		// A thread may get an index into m_queueData that has become invalid since then...
		if (index<qsize)
		{
			dqe = (QueueElement)m_queueData.elementAt(index);
			if (dqe!=null)
			{
				// Set the specified element to null
				m_queueData.setElementAt(null, index);
				boolean keysEmpty = m_keys.isEmpty();
				// It's faster to change the size of the vector than to remove individual elements, 
				// so reset the size of the data here to point to the last non-null entry
				if (index==qsize-1 || keysEmpty)
				{
					for (int i=qsize-1; i>=0; i--) 
					{
						if (m_queueData.elementAt(i)!=null)
						{
							qsize=i+1;
							m_queueData.setSize(qsize);
							break;
						}
					}
				}
				// If all of the keyed entries that point to data in the queue have been removed, 
				// then compress contents of queue. 
				if (keysEmpty)
				{
					m_begin = -1;
					for (int i=0; i<qsize; ) // intentionally no i++, see below
					{
						// If the entry is null, then remove it
						if (m_queueData.elementAt(i)==null)
						{
							m_queueData.removeElementAt(i);
							qsize--;
						}
						else
						{
							// Store indices to beginning and end of non-null data
							if (m_begin<0)
								m_begin=i;
							i++;
						}
					}
				}
				// If the queue can't be compressed, then update the beginning and ending of non-null data
				else
				{
					// Beginning index
					if (index==m_begin)
					{
						for (int i=m_begin+1; i<qsize; i++) 
						{
							if (m_queueData.elementAt(i)!=null)
							{
								m_begin = i;
								break;
							}
						}
					}
				}
			}
		}
		return dqe;
	}
	
	// Wait for other threads
	synchronized private long waitThread(long timeout)
	{
		try {
			wait(timeout);
		} 
		catch (InterruptedException e) {}
		// Reduce the wait time by the amount of milliseconds that have expired since the last wait
		if (timeout > 0)
		{
			long expiredMillis = System.currentTimeMillis()-m_beginMillis;
			timeout -= expiredMillis;
			if (timeout <= 0)
				timeout = -1;
		}
		return timeout;
	}
	
	// If the current timeout is smaller than the previous one, reset it.
	// Remember, though, that timeout=0 means wait forever
	synchronized private void setTimeout(long timeout)
	{
		long currentMillis = System.currentTimeMillis();
		// Reduce the wait time by the amount of milliseconds that have expired since the last wait
		if (m_timeout>=0)
		{
			long expiredMillis = currentMillis-m_beginMillis;
			m_timeout -= expiredMillis;
			if (m_timeout<=0)
				m_timeout=-1;
		}
		m_beginMillis = currentMillis;
		if (timeout>0 && (timeout < m_timeout || m_timeout<0))
		{
			m_timeout = timeout;
			notifyAll();
		}
	}

	/*	
	public static void main(String[] args)
	{
		Queue q = new Queue();
		fixed f = new fixed(10);
		int len;
		/*
		f.assign("ABC");
		q.sendData(f);
		f.assign("DEF");
		q.sendData(f);
		f.assign("GHI");
		q.sendData(f);
		int len = q.receiveData(1, f); // 10 f="ABC"
		len = q.receiveData(-1, f); // 10 f="DEF"
		len = q.receiveData(0, f); // 10 f="GHI"
		len = q.receiveData(0, f); // 0
		* / 
		f.assign("ABC");
		q.sendData(f, f);
		q.sendData(f, f); // Same data, key
		f.assign("DEF");
		q.sendData(f, f);
		f.assign("GHI");
		q.sendData(f, f);
		f.assign("DEF");
		/*
		len = q.receiveData(1, f, f, "EQ"); // 10 f="DEF"
		len = q.receiveData(5, f); // 10 f="ABC"
		len = q.receiveData(1, f, f, "EQ"); // 10 f="ABC"
		len = q.receiveData(5, f, f, "EQ"); // 0
		* /
	}
	*/
}
