// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.util.ListResourceBundle;

public class MessageStrings extends ListResourceBundle
{

    public MessageStrings()
    {
    }

    public Object[][] getContents()
    {
        return contents;
    }

    private static final Object contents[][] = {
        {
            "DEFAULT_LOCALE_NOT_SUPPORTED_MSG", "Warning: The system's default locale is not supported (reverting to English)"
        }, {
            "SPECIFIED_LOCALE_NOT_SUPPORTED_MSG", "Specified locale not supported"
        }, {
            "DLL_LOAD_FAILED_NAME_NULL", "Dll load failed since dll name is null"
        }, {
            "ABOUT_TO_FLUSH_SOCKET", "Flushing any remaining socket messages"
        }, {
            "CANNOT_OPEN_LOG_FILE", "Cannot open the JT_LOG file"
        }, {
            "CATCHER_USAGE_MSG", "--Usage:   traceCatch 8xxx language"
        }, {
            "CATCHER_USAGE_DEFAULT", "--Default: traceCatch 8765"
        }, {
            "COPYRIGHT_MSG", "\nIBM Remote Debugger - Java Trace Catcher (TC) Version 8\n- Licensed Material - Property of IBM\n(c) Copyright IBM Corp 1997,2000  All Rights Reserved\nUS Government Users Restricted Rights - Use, duplication\nor disclosure restricted by GSA ADP Schedule Contract\nwith IBM Corp.\n"
        }, {
            "CONNECTION_ESTABLISHED_MSG", "Connection established"
        }, {
            "CONNECT_BUSY_MSG", "Connection port already busy"
        }, {
            "CONNECT_FAILED_MSG", "Connection rejected (******* ALL JT OUTPUT BEING DISCARDED *******)"
        }, {
            "CONNECTION_LOST_MSG", "Connection lost"
        }, {
            "CAUGHT_EXCEPTION", "Caught unpected exception"
        }, {
            "INVALID_MESSG_LEVEL", "Invalid '-DJT_MSG=n' (numeric exception)"
        }, {
            "INVALID_DEBUG_LEVEL", "Invalid '-DJT_DBG=n' (numeric exception)"
        }, {
            "INVALID_ERROR_LEVEL", "Invalid '-DJT_ERR=n' (numeric exception)"
        }, {
            "INVALID_EVENT_LEVEL", "Invalid '-DJT_EVT=n' (numeric exception)"
        }, {
            "INVALID_OUT_LEVEL", "Invalid '-DJT_OUT=n' (numeric exception)"
        }, {
            "INVALID_JT_PORT", "Invalid '-DJT_PORT' port (numeric exception)"
        }, {
            "INVALID_PORT", "Invalid port (typically 8000-8999)"
        }, {
            "JT_CATCHER", "JT_CATCHER:"
        }, {
            "JT_LOGGER", "JT_LOGGER:"
        }, {
            "NO_JT_HOST", "'JT_HOST' is undefined so System.out is not redirected"
        }, {
            "PORT_IN_USE_MSG", "Port is already in use (Catcher already running"
        }, {
            "PROGRAM_DONE_MSG", "Program has run to completion"
        }, {
            "UNKNOWN_HOSTNAME_MSG", "Unknown hostname"
        }, {
            "UNABLE_TO_ASSIGN_SYSTEM_IN_TO_SOCKET", "Unable to assign System.in to socket"
        }, {
            "UNABLE_TO_ASSIGN_SYSTEM_OUT_TO_SOCKET", "Unable to assign System.out to socket"
        }, {
            "UNABLE_TO_CONNECT_TO_JT_CATCHER", "Unable to connect to JT_CATCHER, defaulting to System.out"
        }, {
            "SYSTEM_OUT_NOW_SOCKET", "System.out is now a socket --"
        }, {
            "SYSTEM_OUT_SOCKET_CLOSING", "System.out socket about to be closed"
        }, {
            "SYSTEM_OUT_SOCKET_CLOSED", "System.out socket now closed"
        }, {
            "WAITING_FOR_CONNECTION_ON_PORT_MSG", "Waiting for connection"
        }
    };

}
