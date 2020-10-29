<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
  <% /*
  The @page directive sets the charset to be UTF-8.
  This is required for WebFacing as all data is supplied to the browser in UTF-8 encoding.
  */ %>
<%@ page contentType="text/html; charset=utf-8" %>
<jsp:useBean id="screenbuilder" scope="session" type="com.i2class.ThreadLockWebface" />
<html>
<head>
<% String ctxPath = (String)session.getAttribute("ServletContextPath"); %>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <% /*
  The next line load the style sheet which will give this page it's colour and images.
  The prefix scriptlet to the href part of the tag will resolve to the relative webapp
  name.
  The first line is used by the editor and is commented out at runtime.
  */ %>
<% /* %>
<link rel="stylesheet" type="text/css" href="../avenue.css">
<% */ %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/styles/chrome/avenue.css">

<META name="GENERATOR" content="IBM WebSphere Studio">
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" frame="box" height="100%" width="100%">
  <tbody>
  <tr>
            <td class=headl rowspan=1 width="10%" height="40">
      &nbsp
    </td>
            <td class=headc colspan=4 rowspan=1 width="80%" valign="top" height="40">
      <% /*
        From the session you can get a text string that represents the title of the application.
      */ %>
      <%=session.getValue("ApplicationTitle")%>
      </td>
            <td class=headr colspan=1 rowspan=1 width="10%" height="40">
      &nbsp
    </td>
        </tr>
  <tr>
            <td class=thesidecurve rowspan=1 width="155">
      &nbsp
    </td>
            <td class=theapp colspan=5 rowspan=2 width="665" valign="top" align="left" height="468">

        <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/styles/apparea/apparea.css">
        <% /*********************************************************************************/
           /* Build valid command key names and do JavaScript setup */
        %> 
        <%@ include file="i2Setup.jspinclude" %>
        <% /*********************************************************************************/ %>

        <% /***********************************************************************************/
           /* Include format.jsp formats */
        %>
        <%@ include file="i2ScreenBuilder.jspinclude" %>
        <% /*********************************************************************************/ %>
    </td>
        </tr>
  <tr>
            <td class=theside colspan=1 rowspan=1 width="155" valign="top" align="center">
       <A href="http://www.ibm.com/software/ad/wdt400"><IMG src="<%=ctxPath%>/images/IBM/WebFacing.gif" width="53" height="31" border="0" align="middle"></A>

          <% /*
          Here is the servlet that will insert the active command keys.  You can specify the
          amount of space that will be allocated for each key and the count of keys that will
          be placed horizontally together.  A count of 1 means a vertical stack.

          height=nnn    (height of each "key" specified in pixels, default=30)
          width=nnn    (width of each "key" specified in pixels, default=100)
          count=nn     (number of keys that should be placed together horizontally, default=1)
          title=yes/no  (should the keys have fly-over title,default=false)
          */ %>
           <%-- jsp:include page="/WFCmdKeysBuilder?height=18&width=145&count=1&title=yes" flush="true"/ --%>
           <% /*********************************************************************************/
              /* Build command key buttons  */
            final int KEY_WIDTH=145;
            final String KEY_HEIGHT="18";
            final int KEY_COUNT=1;
           %>
           <%@ include file="i2CmdKeys.jspinclude" %>
           <% /*********************************************************************************/ %>

            </td>
        </tr>
  <tr>
            <td class=theside colspan=1 rowspan=1 width="155" height="60%" valign="bottom" align="center">
      <A href="https://rapidbi.tech/i2class"><IMG src="<%=ctxPath%>/i2web/i2class.gif" border="0" align="middle"></A>
    </td>
            <td class=theapp colspan=5 rowspan=1 width="665" height="60%" valign="top">
      &nbsp
    </td>
  </tr>
  </tbody>
</table>
</body>
</html>