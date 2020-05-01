// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import java.io.Serializable;

public class ClientLock
    implements Serializable
{

    public ClientLock(int i)
    {
        _lastThread = null;
        _lockOwnerThread = null;
        _lockCount = 0;
        _lockTimeOut = 0x493e0;
        if(i > 0x493e0)
            _lockTimeOut = i;
    }

    public synchronized int acquireLockWithWait()
    {
        if(_lockOwnerThread != null && _lockOwnerThread.equals(Thread.currentThread().getName()))
        {
            _lockCount++;
            return LOCKOWNERTHREAD;
        }
        notify();
        _lastThread = Thread.currentThread().getName();
        if(_lockCount == 0)
        {
            lock();
            return FIRSTTHREAD;
        }
        try
        {
            wait(_lockTimeOut);
            if(_lastThread.equals(Thread.currentThread().getName()))
            {
                lock();
                return LASTTHREAD;
            } else
            {
                return INTERMEDTHREAD;
            }
        }
        catch(Exception exception)
        {
            return LASTTHREAD;
        }
    }

    public synchronized void releaseLock()
    {
        if(_lockCount >= 2 && _lockOwnerThread != null && _lockOwnerThread.equals(Thread.currentThread().getName()))
        {
            _lockCount--;
            return;
        } else
        {
            unlock();
            notifyAll();
            return;
        }
    }

    private synchronized void unlock()
    {
        _lockCount = 0;
        _lockOwnerThread = null;
    }

    private synchronized void lock()
    {
        _lockOwnerThread = Thread.currentThread().getName();
        _lockCount = 1;
    }

    public synchronized boolean isThisTheLastThread()
    {
        return _lastThread == null || _lastThread.equals(Thread.currentThread().getName());
    }

    public static int LASTTHREAD = 2;
    public static int FIRSTTHREAD = 1;
    public static int INTERMEDTHREAD = -1;
    public static int LOCKOWNERTHREAD = 3;
    private String _lastThread;
    private String _lockOwnerThread;
    private int _lockCount;
    private int _lockTimeOut;

}
