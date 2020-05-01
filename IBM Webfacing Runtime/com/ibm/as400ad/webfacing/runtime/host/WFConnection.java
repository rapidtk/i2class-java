// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WFServerDownLevelException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            WFInvalidSignOnException, WFInvalidSessionException, IHostRequest, IWFInputBuffer

public class WFConnection
    implements IHostRequest
{
    class WFStringBuffer
        implements IWFInputBuffer
    {

        public int length()
        {
            return _impl.length() * 2;
        }

        public void toStream(OutputStream outputstream)
            throws IOException
        {
            DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
            dataoutputstream.writeChars(_impl);
        }

        private String _impl;

        public WFStringBuffer(String s)
        {
            _impl = s;
        }
    }


    public WFConnection(HttpSession httpsession, String s, String s1, String s2)
        throws WFServerDownLevelException, WebfacingInternalException
    {
        this(httpsession, s, "4004", s1, s2);
    }

    public WFConnection(HttpSession httpsession, String s, String s1, String s2, String s3)
        throws WFServerDownLevelException, WebfacingInternalException
    {
        _socket = null;
        _wf_session_id = 0;
        _connected = false;
        _host_data = null;
        _host_data_pos = 0;
        _sessionTimeout = httpsession.getMaxInactiveInterval();
        _host = s;
        _port = s1;
        _user = s2;
        _pwd = s3;
        _cmd = "";
        connect();
        versionHandshake();
        sendSessionTimeout();
        logon();
        saveWFSessionId();
    }

    public WFConnection(HttpSession httpsession, String s, String s1, String s2, String s3, String s4)
        throws WFServerDownLevelException, WebfacingInternalException
    {
        this(httpsession, s, s1, s2, s3);
        _cmd = s4;
        run();
    }

    private void connect()
        throws WebfacingInternalException
    {
        try
        {
            _socket = new Socket(InetAddress.getByName(_host), Integer.parseInt(_port));
            _socket_is = _socket.getInputStream();
            _socket_os = _socket.getOutputStream();
        }
        catch(UnknownHostException unknownhostexception)
        {
            WFSession.getTraceLogger().err(2, unknownhostexception);
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0054"), "&1", _host));
        }
        catch(NumberFormatException numberformatexception)
        {
            WFSession.getTraceLogger().err(2, numberformatexception);
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0055"), "&1", _port));
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0056"), "&1", _host);
            s = WebfacingConstants.replaceSubstring(s, "&2", _port);
            s = WebfacingConstants.replaceSubstring(s, "&3", "'" + ioexception + "'");
            throw new WebfacingInternalException(s);
        }
        _connected = true;
    }

    public void disconnect()
        throws WebfacingInternalException
    {
        try
        {
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(_socket_os, 5);
            DataOutputStream dataoutputstream = new DataOutputStream(bufferedoutputstream);
            dataoutputstream.writeInt(1);
            dataoutputstream.writeByte(25);
            bufferedoutputstream.flush();
            _socket_os.close();
            _socket_is.close();
            _socket.close();
        }
        catch(IOException ioexception) { }
        _connected = false;
    }

    public ByteArrayInputStream getData()
        throws WebfacingInternalException
    {
        if(null == _host_data || 0 == _host_data.available())
            _host_data = getDataImmediate();
        int i = _host_data.read();
        int j = _host_data.read();
        int k = _host_data.read();
        int l = _host_data.read();
        int i1 = (i << 24) + (j << 16) + (k << 8) + (l << 0);
        byte abyte0[] = new byte[i1];
        int j1 = _host_data.read(abyte0, 0, i1);
        if(j1 != i1)
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_resmri.getString("WF0066"));
            paddedstringbuffer.replaceSubstring("&1", "" + i1);
            paddedstringbuffer.replaceSubstring("&2", "" + j1);
            throw new WebfacingInternalException(paddedstringbuffer.toString());
        } else
        {
            return new ByteArrayInputStream(abyte0);
        }
    }

    private ByteArrayInputStream getDataImmediate()
        throws WebfacingInternalException
    {
        reconnect();
        byte abyte0[];
        try
        {
            DataOutputStream dataoutputstream = new DataOutputStream(_socket_os);
            dataoutputstream.writeInt(1);
            dataoutputstream.writeByte(26);
            dataoutputstream.flush();
            DataInputStream datainputstream = new DataInputStream(_socket_is);
            int i = datainputstream.readInt();
            abyte0 = new byte[i];
            datainputstream.readFully(abyte0);
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0059"), "&1", _host);
            s = WebfacingConstants.replaceSubstring(s, "&2", "'" + ioexception + "'");
            throw new WebfacingInternalException(s);
        }
        disconnect();
        return new ByteArrayInputStream(abyte0);
    }

    public void logoff()
        throws WebfacingInternalException
    {
        try
        {
            putData("", 4);
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0057"), "&1", ioexception.toString()));
        }
    }

    private void logon()
        throws WebfacingInternalException
    {
        String s = _user + "/" + _pwd;
        try
        {
            putData(s, 1);
            int i = _socket_is.read();
            if(2 != i)
            {
                if(i == 3)
                {
                    String s1 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0060"), "&1", _host);
                    s1 = WebfacingConstants.replaceSubstring(s1, "&2", _user);
                    throw new WFInvalidSignOnException(s1);
                }
                if(i == 1)
                {
                    String s2 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0123"), "&2", _host);
                    s2 = WebfacingConstants.replaceSubstring(s2, "&1", _user);
                    throw new WFInvalidSignOnException(s2);
                }
                if(i == 4)
                {
                    String s3 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0124"), "&2", _host);
                    s3 = WebfacingConstants.replaceSubstring(s3, "&1", _user);
                    throw new WFInvalidSignOnException(s3);
                }
                if(i == 5)
                {
                    String s4 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0125"), "&2", _host);
                    s4 = WebfacingConstants.replaceSubstring(s4, "&1", _user);
                    throw new WFInvalidSignOnException(s4);
                }
                if(i == 6)
                {
                    String s5 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0126"), "&2", _host);
                    s5 = WebfacingConstants.replaceSubstring(s5, "&1", _user);
                    throw new WFInvalidSignOnException(s5);
                }
                if(i == 7)
                {
                    String s6 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0127"), "&2", _host);
                    s6 = WebfacingConstants.replaceSubstring(s6, "&1", _user);
                    throw new WFInvalidSignOnException(s6);
                }
                if(i == 8)
                {
                    String s7 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0128"), "&2", _host);
                    s7 = WebfacingConstants.replaceSubstring(s7, "&1", _user);
                    throw new WFInvalidSignOnException(s7);
                }
                if(i == 9)
                {
                    String s8 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0129"), "&2", _host);
                    s8 = WebfacingConstants.replaceSubstring(s8, "&1", _user);
                    throw new WFInvalidSignOnException(s8);
                }
            }
        }
        catch(IOException ioexception)
        {
            String s9 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0061"), "&1", _host);
            s9 = WebfacingConstants.replaceSubstring(s9, "&2", _user);
            s9 = WebfacingConstants.replaceSubstring(s9, "&3", ioexception.toString());
            throw new WebfacingInternalException(s9);
        }
    }

    public void putData(IWFInputBuffer iwfinputbuffer)
        throws IOException, WebfacingInternalException
    {
        putData(iwfinputbuffer, 11);
    }

    private void putData(IWFInputBuffer iwfinputbuffer, int i)
        throws IOException, WebfacingInternalException
    {
        reconnect();
        int j = 1 + iwfinputbuffer.length();
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(j + 4);
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        dataoutputstream.writeInt(j);
        bytearrayoutputstream.write(i);
        iwfinputbuffer.toStream(bytearrayoutputstream);
        BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(_socket_os, j + 4);
        bytearrayoutputstream.writeTo(bufferedoutputstream);
        bufferedoutputstream.flush();
    }

    private void putData(String s, int i)
        throws IOException, WebfacingInternalException
    {
        putData(((IWFInputBuffer) (new WFStringBuffer(s))), i);
    }

    private void reconnect()
        throws WebfacingInternalException
    {
        if(_connected)
            return;
        connect();
        BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(_socket_os, 5);
        DataOutputStream dataoutputstream = new DataOutputStream(bufferedoutputstream);
        try
        {
            dataoutputstream.writeByte(24);
            dataoutputstream.writeInt(_wf_session_id);
            bufferedoutputstream.flush();
            DataInputStream datainputstream = new DataInputStream(_socket_is);
            int i = datainputstream.readInt();
            if(i != _wf_session_id)
            {
                dataoutputstream.close();
                datainputstream.close();
                throw new WFInvalidSessionException("<br>" + WebfacingConstants.replaceSubstring(_resmri.getString("WF0062"), "&1", Integer.toString(_sessionTimeout)));
            }
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0063"), "&1", ioexception.toString()));
        }
    }

    public InputStream request(IWFInputBuffer iwfinputbuffer)
        throws IOException, WebfacingInternalException
    {
        putData(iwfinputbuffer, 99);
        return getDataImmediate();
    }

    private void run()
        throws WebfacingInternalException
    {
        run(_cmd);
    }

    public void run(String s)
        throws WebfacingInternalException
    {
        try
        {
            putData(s, 7);
        }
        catch(IOException ioexception)
        {
            String s1 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0058"), "&1", s);
            s1 = WebfacingConstants.replaceSubstring(s1, "&2", _host);
            s1 = WebfacingConstants.replaceSubstring(s1, "&3", _user);
            s1 = WebfacingConstants.replaceSubstring(s1, "&4", ioexception.toString());
            throw new WebfacingInternalException(s1);
        }
    }

    private void saveWFSessionId()
        throws WebfacingInternalException
    {
        try
        {
            DataInputStream datainputstream = new DataInputStream(_socket_is);
            _wf_session_id = datainputstream.readInt();
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0067"), "&1", _host);
            s = WebfacingConstants.replaceSubstring(s, "&2", _port);
            s = WebfacingConstants.replaceSubstring(s, "&3", ioexception.toString());
            throw new WebfacingInternalException(s);
        }
    }

    private void sendSessionTimeout()
        throws WebfacingInternalException
    {
        try
        {
            DataOutputStream dataoutputstream = new DataOutputStream(_socket_os);
            dataoutputstream.writeInt(_sessionTimeout);
            dataoutputstream.flush();
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0068"), "&1", _host);
            s = WebfacingConstants.replaceSubstring(s, "&2", _port);
            s = WebfacingConstants.replaceSubstring(s, "&3", ioexception.toString());
            throw new WebfacingInternalException(s);
        }
    }

    private void versionHandshake()
        throws WFServerDownLevelException, WebfacingInternalException
    {
        byte byte0 = 5;
        BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(_socket_os, 5);
        DataOutputStream dataoutputstream = new DataOutputStream(bufferedoutputstream);
        try
        {
            dataoutputstream.writeByte(0);
            dataoutputstream.writeInt(byte0);
            bufferedoutputstream.flush();
            DataInputStream datainputstream = new DataInputStream(_socket_is);
            int i = datainputstream.readInt();
            if(0 != i)
            {
                dataoutputstream.close();
                datainputstream.close();
                if(2 == i)
                    throw new WFServerDownLevelException(_resmri.getString("WF0107") + "<br>" + _resmri.getString("WF0108"));
                else
                    throw new WFServerDownLevelException(_resmri.getString("WF0064") + "<br>" + _resmri.getString("WF0109"));
            }
        }
        catch(IOException ioexception)
        {
            WFSession.getTraceLogger().err(2, ioexception);
            String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0065"), "&1", ioexception.toString()) + " <br>" + _resmri.getString("WF0110") + "<br>" + _resmri.getString("WF0111");
            s = WebfacingConstants.replaceSubstring(s, "&2", _port);
            s = WebfacingConstants.replaceSubstring(s, "&3", _host);
            throw new WebfacingInternalException(s);
        }
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    private static ResourceBundle _resmri;
    private String _host;
    private String _port;
    private String _user;
    private String _pwd;
    private String _cmd;
    private transient Socket _socket;
    private transient OutputStream _socket_os;
    private transient InputStream _socket_is;
    private int _sessionTimeout;
    private int _wf_session_id;
    private transient boolean _connected;
    private transient ByteArrayInputStream _host_data;
    private int _host_data_pos;
    private static final int _p_versionHandshake = 0;
    private static final int _p_logon = 1;
    private static final int _p_logonPassed = 2;
    private static final int _p_logonFailed = 3;
    private static final int _p_logoff = 4;
    private static final int _p_invocation = 7;
    private static final int _p_invocationFailed = 8;
    private static final int _p_userData = 11;
    private static final int _p_startDebug = 14;
    private static final int _p_endDebug = 15;
    private static final int _p_reconnect = 24;
    private static final int _p_disconnect = 25;
    private static final int _p_ready_for_data = 26;
    private static final int _p_jobInfo = 99;
    private static final int passwordIncorrect = 1;
    private static final int profileDisabled = 4;
    private static final int profileExpired = 5;
    private static final int noPassword = 6;
    private static final int incorrectProfile = 7;
    private static final int noSuchProfile = 8;
    private static final int unableToAllocateProfile = 9;
    private static final int _p_versionNumber = 5;
    private static final int _e_versionMatch = 0;
    private static final int _e_clientVersionDownLevel = 1;
    private static final int _e_serverVersionDownLevel = 2;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
