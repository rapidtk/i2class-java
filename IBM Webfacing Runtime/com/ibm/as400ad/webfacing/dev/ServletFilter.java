// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;


// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            FilterCode, QdspmnuClientScriptFilter, QdspmnuFilter

public class ServletFilter extends FilterCode
{

    public ServletFilter(String s)
    {
        _importsDone = false;
        _className = null;
        _serviceMethodFound = false;
        _write = true;
        _foundLastLineBeforeGeneratingCode = false;
        _packageStatementDone = false;
        _filterCode = null;
        _packageFile = null;
        _className = s;
    }

    private String endOfCode()
    {
        String s = "" + System.getProperty("line.separator");
        String s1 = "} catch (Throwable t) {" + s + "     ErrorHandler errHandler = new ErrorHandler(getServletConfig().getServletContext(), request, response, WFSession.getTraceLogger(), true);" + s + "     errHandler.handleError(new Exception(t.toString()), \"Error in system servlet.\");" + s + "} finally {" + s + "     out.flush();" + s + "}" + s + "}" + s + "}" + s;
        return s1;
    }

    public String filterLine(String s)
    {
        if(_filterCode != null)
            s = _filterCode.filterLine(s);
        if(!_importsDone)
        {
            if(!_packageStatementDone && s.indexOf("package") >= 0)
            {
                _packageStatementDone = true;
                _packageFile = s.substring(s.lastIndexOf(".") + 1).toLowerCase();
                String s1 = "package com.ibm.as400ad.webfacing.runtime.qsys.servlets." + _packageFile;
                if(_packageFile.indexOf("qdspmnu") >= 0)
                    if(_className.indexOf("JavaScript") >= 0)
                        _filterCode = new QdspmnuClientScriptFilter();
                    else
                        _filterCode = new QdspmnuFilter();
                return s1;
            }
            if(s.indexOf("import ") >= 0 && s.indexOf("jsp") >= 0)
                return null;
            if(s.indexOf("public class") >= 0)
            {
                String s2 = "import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;" + System.getProperty("line.separator") + "import java.io.PrintWriter;" + System.getProperty("line.separator") + "import java.io.IOException;" + System.getProperty("line.separator") + "import com.ibm.as400ad.webfacing.runtime.controller.WFSession;" + System.getProperty("line.separator") + System.getProperty("line.separator") + System.getProperty("line.separator");
                _write = false;
                _importsDone = true;
                s2 = s2 + "public class " + _className + " extends HttpServlet {" + System.getProperty("line.separator");
                s2 = s2 + System.getProperty("line.separator") + "public " + _className + "(){}" + System.getProperty("line.separator");
                s2 = s2 + System.getProperty("line.separator") + "public final void init(){}" + System.getProperty("line.separator");
                return s2;
            }
        } else
        if(!_serviceMethodFound)
        {
            if(s.indexOf("public void _jspService") >= 0)
            {
                _serviceMethodFound = true;
                return getBeginningOfServiceMethod();
            }
        } else
        if(_serviceMethodFound && !_foundLastLineBeforeGeneratingCode)
        {
            if(s.indexOf("out = pageContext.getOut();") >= 0)
            {
                _foundLastLineBeforeGeneratingCode = true;
                _write = true;
                return null;
            }
        } else
        if(_foundLastLineBeforeGeneratingCode)
        {
            if(s.indexOf("pageContext.getAttribute(") >= 0)
            {
                String s3 = null;
                int i = s.indexOf("\"");
                int k = s.lastIndexOf("\"");
                s3 = s.substring(i + 1, k);
                return "\t\t\tsession.getAttribute(\"" + s3 + "\");";
            }
            if(s.indexOf("pageContext.setAttribute(") >= 0)
            {
                String s4 = null;
                int j = s.indexOf("\"");
                int l = s.lastIndexOf("\"");
                s4 = s.substring(j + 1, l);
                return "\t\t\tsession.setAttribute(\"" + s4 + "\", " + s4 + ");";
            }
            if(s.indexOf("} catch (Exception ex) {") >= 0)
            {
                _write = false;
                return endOfCode();
            }
        }
        if(_write)
            return s;
        else
            return null;
    }

    private String getBeginningOfServiceMethod()
    {
        String s = "" + System.getProperty("line.separator");
        String s1 = "public void service(HttpServletRequest request, HttpServletResponse  response)" + s + "throws IOException, ServletException {" + s + s + "HttpSession session = request.getSession();" + s + "PrintWriter out = response.getWriter();" + s + s + "try {" + s + "\ttry" + s + "\t{" + s + "\t\tresponse.setContentType(\"text/html\");" + s + "    }" + s + "    catch (IllegalStateException ws_jsp_ise)" + s + "    {" + s + "    }" + s;
        return s1;
    }

    private boolean _importsDone;
    private String _className;
    private boolean _serviceMethodFound;
    private boolean _write;
    private boolean _foundLastLineBeforeGeneratingCode;
    private boolean _packageStatementDone;
    private FilterCode _filterCode;
    private String _packageFile;
}
