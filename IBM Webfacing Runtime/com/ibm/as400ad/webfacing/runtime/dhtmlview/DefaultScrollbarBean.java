// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.VersionTable;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean;
import com.ibm.as400ad.webfacing.runtime.view.IBuildSFLCTLViewBean;
import com.ibm.as400ad.webfacing.util.MathUtil;
import java.io.PrintStream;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            IScrollbarBean

public class DefaultScrollbarBean
    implements IScrollbarBean
{

    public DefaultScrollbarBean()
    {
        _scrollbarJavascriptID = null;
        _preferredSegments = -1;
        _horizontalPosition = 400;
        _oneCharWidth = 12;
    }

    private int[] addSegmentForPagedown(int ai[])
    {
        if(ai[ai.length - 1] > 1 && _subfileControl.enablePageDown())
        {
            int ai1[] = new int[ai.length + 1];
            int i;
            for(i = 0; i < ai.length - 1; i++)
                ai1[i] = ai[i];

            ai1[i] = ai[i] - 1;
            ai1[i + 1] = 1;
            return ai1;
        } else
        {
            return ai;
        }
    }

    public static int[] allocateOneSideOfPages(int i, int j)
    {
        if(j == 0)
            throw new IllegalArgumentException(_resmri.getString("WF0007"));
        int ai[] = new int[j];
        for(int k = 0; k < j; k++)
            ai[k] = 0;

        int l = i;
        if(l == 0)
            return ai;
        double d = MathUtil.positiveIntegral(1.0D, 1.5D, 0.0D, j);
        double d1 = (double)l / d;
        double d2 = 0.0D;
        for(int i1 = 0; i1 < j; i1++)
        {
            int j1 = 0;
            double d3 = MathUtil.positiveIntegral(d1, 1.5D, i1, i1 + 1);
            int k1 = (int)d3;
            double d4 = d3 - (double)k1;
            d2 += d4;
            j1 = k1;
            if(d2 > 1.0D)
            {
                int l1 = MathUtil.getWholePartOf(d2);
                j1 += l1;
                d2 -= l1;
            }
            ai[i1] += j1;
            l -= j1;
        }

        if(l > 0)
        {
            ai[j - 1]++;
            l--;
        }
        return ai;
    }

    public static int[] allocatePages(int i, int j, int k)
    {
        if(i < j)
            throw new IllegalArgumentException(_resmri.getString("WF0008"));
        if(k >= i)
            throw new IllegalArgumentException(_resmri.getString("WF0009"));
        int ai[] = new int[j];
        for(int l = 0; l < j; l++)
            ai[l] = 1;

        int i1 = i - j;
        if(i1 == 0)
            return ai;
        if(j < 3)
            throw new IllegalArgumentException(_resmri.getString("WF0010"));
        int j1 = k;
        int k1 = i - (k + 1);
        int l1 = calculateCurrentSegment(i, j, k);
        int i2 = l1;
        int j2 = j - (l1 + 1);
        int k2 = j1 - i2;
        int l2 = k1 - j2;
        if(i2 > 0)
        {
            int ai1[] = allocateOneSideOfPages(k2, i2);
            for(int i3 = 1; i3 <= i2; i3++)
                ai[l1 - i3] += ai1[i3 - 1];

        }
        if(j2 > 0)
        {
            int ai2[] = allocateOneSideOfPages(l2, j2);
            for(int j3 = 1; j3 <= j2; j3++)
                ai[l1 + j3] += ai2[j3 - 1];

        }
        return ai;
    }

    protected void buildTitlesAndCommands(int i, int j)
    {
        for(int k = 0; k < _segmentTitles.length; k++)
        {
            int l = _segmentStartRRN[k];
            int i1 = l;
            int j1 = (l + _segmentNumberOfRecords[k]) - 1;
            _segmentTitles[k] = new String(l + " - " + j1);
            if(k == getCurrentSegmentIndex())
            {
                _segmentCommands[k] = new String("");
            } else
            {
                if(!_subfileControl.enablePageDown() && k == _segmentTitles.length - 1 && _segmentsOfPages[k] > 1)
                    i1 += (_segmentsOfPages[k] - 1) * j;
                if(_subfileControl.enablePageDown() && i1 > _subfileControl.getLastRecordNumber())
                {
                    _segmentCommands[k] = getBuildBufferCall("PAGEDOWN");
                    if(_subfileControl.isSFLENDSpecified() && !_subfileControl.isSFLENDActive())
                        _segmentTitles[k] = "More...";
                } else
                if(_subfileControl.enablePageDown() && _subfileControl.isSFLENDScrollBar() && (i1 + _subfileControl.getPageSize()) - 1 > _subfileControl.getLastRecordNumber())
                {
                    _segmentCommands[k] = getBuildBufferCall("PAGEDOWN");
                    if(_subfileControl.isSFLENDSpecified() && !_subfileControl.isSFLENDActive())
                        _segmentTitles[k] = i1 + "+";
                } else
                {
                    _segmentCommands[k] = getChangePageCall(i1);
                }
            }
        }

    }

    public static int calculateCurrentSegment(int i, int j, int k)
    {
        int l = calculateCurrentSegment(i, j, k, 3);
        int i1 = k;
        int j1 = i - (k + 1);
        int k1 = l;
        int l1 = j - (l + 1);
        if(k1 > i1 || l1 > j1)
            l = calculateCurrentSegment(i, j, k, 1);
        return l;
    }

    public static int calculateCurrentSegment(int i, int j, int k, int l)
    {
        int i1 = l;
        if(k == 0)
            return 0;
        if(k == i - 1)
        {
            return j - 1;
        } else
        {
            double d = 1.0D - (double)j / 2D;
            double d1 = Math.pow(1.0D - (double)i / 2D, i1);
            double d2 = d / d1;
            double d3 = d2 * Math.pow((double)k - (double)i / 2D, i1) + (double)j / 2D;
            return (int)d3;
        }
    }

    private int calculateRecordsRepresented(int i, int j, int k)
    {
        int l;
        if(_subfileControl.enablePageDown() && _subfileControl.isSFLENDSpecified() && !_subfileControl.isSFLENDActive() && !_subfileControl.isPageEqualToSize())
        {
            int i1 = _subfileControl.getSubfileSize();
            int j1 = (j - i) % k;
            if(j < i1)
            {
                l = i1;
                if(j1 != 0 && j + (k - j1) >= l)
                    l += k - j1;
            } else
            {
                l = j;
                if(j1 != 0)
                    l += k - j1;
                l++;
            }
        } else
        {
            l = j;
        }
        return l;
    }

    private String getBuildBufferCall(String s)
    {
        return "validateAndSubmit(\"" + s + "\",false);";
    }

    private String getChangePageCall(int i)
    {
        return new String("changePage(\"" + _subfileControl.getRecordName() + "\", " + i + ");");
    }

    protected int getCurrentSegmentIndex()
    {
        int i = _subfileControl.getRRN();
        for(int j = 1; j < _segmentStartRRN.length; j++)
            if(_segmentStartRRN[j] > i)
                return j - 1;

        return _segmentNumberOfRecords.length - 1;
    }

    protected String getDownArrowOnClickCommand()
    {
        int i = getCurrentSegmentIndex() + 1;
        if(i < _segmentCommands.length)
            if(i == _segmentCommands.length - 1 && _segmentsOfPages[i] > 1)
                return getChangePageCall(_segmentStartRRN[i]);
            else
                return _segmentCommands[i];
        if(_subfileControl.enablePageDown())
            return getBuildBufferCall("PAGEDOWN");
        else
            return "";
    }

    public String getHTMLSource()
    {
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            if(!_subfileControl.isScrollbarShown())
                return "";
            initialize();
            String s = getScrollbarJavascriptID() == null ? "" : "id='" + getScrollbarJavascriptID() + "' ";
            stringbuffer.append("<table " + s + "style='position:relative; border:0; visibility:hidden; z-index:0;' border='0' cellspacing='0' cellpadding='0'>\n");
            String s1 = getUpArrowOnClickCommand();
            stringbuffer.append("<tr " + (s1.equals("") ? "" : " title='Page Up'") + "><td id='" + getScrollbarJavascriptID() + "UpArrow' valign='top' onClick='" + s1 + "' class='" + "scrollbarUpArrow" + "' ><IMG src='" + "styles/transparent.gif' width='16' height='16'></td></tr>\n");
            stringbuffer.append("<tr height='100%'><td>\n");
            stringbuffer.append("<table class='scrollbarBackground' border='0' cellspacing='0' cellpadding='0' height='100%' width='100%'>\n");
            int i = getCurrentSegmentIndex();
            for(int j = 0; j < _segmentCommands.length; j++)
            {
                String s2 = j != i ? "" : "class='scrollbarSlider'";
                stringbuffer.append("<tr " + s2 + " height='" + _segmentSizes[j] + "%' title = '" + _segmentTitles[j] + "'><td id='" + getScrollbarJavascriptID() + "Segment" + j + "' onClick = '" + _segmentCommands[j] + "'></td></tr>\n");
            }

            stringbuffer.append("</table>\n");
            stringbuffer.append("</td></tr>\n");
            String s3 = getDownArrowOnClickCommand();
            stringbuffer.append("<tr " + (s3.equals("") ? "" : " title='Page Down'") + "><td id='" + getScrollbarJavascriptID() + "DownArrow' valign = 'bottom' onClick = '" + s3 + "' class='" + "scrollbarDownArrow" + "' ><IMG src = '" + "styles/transparent.gif' width='16' height='16' ></td></tr>\n");
            stringbuffer.append("</table>\n");
        }
        catch(Throwable throwable)
        {
            System.out.println("Error in DefaultScrollbarBean.getHTMLSource() : " + throwable);
            throwable.printStackTrace(System.out);
            throw new RuntimeException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0011"), "&1", throwable.toString()));
        }
        return stringbuffer.toString();
    }

    public String getPositioningHTMLSource()
    {
        return "";
    }

    public String getPositioningHTMLSource(int i)
    {
        return "";
    }

    public String getScrollbarJavascriptID()
    {
        return _scrollbarJavascriptID;
    }

    protected String getSegmentSize()
    {
        int i = 100 / _segmentTitles.length;
        if(i < 1)
            i = 1;
        return Integer.toString(i);
    }

    protected String getUpArrowOnClickCommand()
    {
        int i = getCurrentSegmentIndex() - 1;
        if(i >= 0)
            if(_segmentsOfPages[i] > 1)
                return getChangePageCall(_segmentStartRRN[i + 1] - _subfileControl.getVisibleRecordSize());
            else
                return _segmentCommands[i];
        if(_subfileControl.enablePageUp())
            return getBuildBufferCall("PAGEUP");
        else
            return "";
    }

    protected void initialize()
    {
        int i = _subfileControl.getVisibleRecordSize();
        int j = _subfileControl.getLastRecordNumber();
        int k = _subfileControl.getRRN();
        int l = _subfileControl.getRecordsInFirstPage();
        int i1 = calculateRecordsRepresented(l, j, i);
        int j1 = _subfileControl.pageNumberFor(i1);
        int k1 = j1 <= 20 ? j1 : 20;
        int l1 = _subfileControl.pageNumberFor(k);
        _segmentsOfPages = allocatePages(j1, k1, l1 - 1);
        _segmentsOfPages = addSegmentForPagedown(_segmentsOfPages);
        k1 = _segmentsOfPages.length;
        _segmentTitles = new String[k1];
        _segmentCommands = new String[k1];
        _segmentStartRRN = new int[k1];
        _segmentNumberOfRecords = new int[k1];
        int i2 = 1;
        int j2 = 0;
        if(l != 0)
        {
            _segmentStartRRN[0] = 1;
            _segmentNumberOfRecords[0] = l;
            if(_segmentsOfPages[0] > 1)
                _segmentNumberOfRecords[0] += i * (_segmentsOfPages[0] - 1);
            j2 = 1;
            i2 = _segmentNumberOfRecords[0] + 1;
        }
        for(; j2 < k1 - 1; j2++)
        {
            _segmentStartRRN[j2] = i2;
            _segmentNumberOfRecords[j2] = i * _segmentsOfPages[j2];
            i2 += _segmentNumberOfRecords[j2];
        }

        if(j2 == 0)
        {
            _segmentStartRRN[j2] = k;
            _segmentNumberOfRecords[j2] = (i1 - k) + 1;
        } else
        {
            _segmentStartRRN[j2] = i2;
            _segmentNumberOfRecords[j2] = (i1 - l) % i;
            if(_segmentNumberOfRecords[j2] == 0)
                _segmentNumberOfRecords[j2] = i;
            if(_segmentsOfPages[j2] > 1)
                _segmentNumberOfRecords[j2] += i * (_segmentsOfPages[j2] - 1);
        }
        _segmentSizes = MathUtil.divideIntoPieces(100, k1);
        buildTitlesAndCommands(i1, i);
    }

    public void setContext(HttpSession httpsession)
    {
        try
        {
            _oneCharWidth = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
        }
        catch(Throwable throwable)
        {
            _oneCharWidth = 0;
        }
        if(_oneCharWidth < 6 || _oneCharWidth > 30)
            _oneCharWidth = 12;
    }

    public void setControlRecordViewBean(IBuildSFLCTLViewBean ibuildsflctlviewbean)
    {
        _subfileControl = ibuildsflctlviewbean;
    }

    public void setProperties()
    {
        _horizontalPosition = 400;
        if(_subfileControl.isWindowed())
            _horizontalPosition = (_horizontalPosition * _subfileControl.getWdwWidth()) / _subfileControl.getMaxColumn();
    }

    public void setScrollbarJavascriptID(int i, String s)
    {
        _scrollbarJavascriptID = s;
        if(_subfileControl.getVersionDigits() >= VersionTable.V5R1M0SP4c)
            _scrollbarJavascriptID = "l" + i + "_" + s;
    }

    public void setScrollbarJavascriptID(String s)
    {
        _scrollbarJavascriptID = s;
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved");
    private static ResourceBundle _resmri;
    protected String _segmentTitles[];
    protected String _segmentCommands[];
    protected int _segmentNumberOfRecords[];
    protected int _segmentStartRRN[];
    protected IBuildSFLCTLViewBean _subfileControl;
    protected int _segmentSizes[];
    protected int _segmentsOfPages[];
    private String _scrollbarJavascriptID;
    protected int _preferredSegments;
    protected int _horizontalPosition;
    private static final String SCROLLBAR_BACKGROUND_STYLE_CLASS = "scrollbarBackground";
    private static final String SCROLLBAR_DOWN_ARROW_STYLE_CLASS = "scrollbarDownArrow";
    private static final String SCROLLBAR_SLIDER_STYLE_CLASS = "scrollbarSlider";
    private static final String SCROLLBAR_UP_ARROW_STYLE_CLASS = "scrollbarUpArrow";
    private static final int DEFAULT_ONE_CHARACTER_WIDTH = 12;
    protected int _oneCharWidth;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
