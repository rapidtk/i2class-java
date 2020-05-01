// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.code400.dom.constants.DSPSIZConstants;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldWebSettings, FieldOnRow

public class FieldArea
{

    public FieldArea(IFieldOutput ifieldoutput)
    {
        _rectangles = new ArrayList();
        _beginAttributeRects = new ArrayList();
        _endAttributeRects = new ArrayList();
        _beginAttrRow = -1;
        _pointA = null;
        _pointB = null;
        _pointC = null;
        _pointD = null;
        _largestRectangle = null;
        _fieldOutput = ifieldoutput;
        _isContinuedField = ifieldoutput.getFieldNode().isContinuedField();
        int i = ifieldoutput.getColumn();
        int j = ifieldoutput.getRow();
        int k = ifieldoutput.getHeight();
        int l = ifieldoutput.getWidth();
        int i1 = DSPSIZConstants.getScreenWidth(ifieldoutput.getDisplaySizeIndex());
        if(!ifieldoutput.isWrapped())
        {
            if(!isContinuedField())
            {
                if(i > 1)
                    _beginAttributeRects.add(new Rectangle(i - 1, j, 1, 1));
                else
                    _beginAttributeRects.add(new Rectangle(i1, j - 1, 1, 1));
            } else
            if(i > 2)
            {
                _beginAttributeRects.add(new Rectangle(i - 2, j, 2, k));
            } else
            {
                _beginAttributeRects.add(new Rectangle(i1, j - 1, 1, k));
                _beginAttributeRects.add(new Rectangle(i - 1, j, 1, k));
            }
            int j1 = (i + l) - 1;
            if(j1 < i1)
            {
                if(!isContinuedField())
                    _endAttributeRects.add(new Rectangle(j1 + 1, j, 1, k));
                else
                    _endAttributeRects.add(new Rectangle(j1 + 1, j, 2, k));
                _endAttrRow = (j + k) - 1;
            } else
            {
                _endAttributeRects.add(new Rectangle(1, j + 1, 1, k));
                _endAttrRow = j + k;
            }
            _rectangles.add(new Rectangle(i, j, l, k));
            _pointA = new Point(i, j);
            _pointD = new Point(i + l, j + k);
            _height = k;
            _leftmostColumn = i;
            _width = l;
        } else
        {
            int k1 = l;
            _height = 0;
            if(i > 1)
                _beginAttributeRects.add(new Rectangle(i - 1, j, 1, 1));
            else
                _beginAttributeRects.add(new Rectangle(i1, j - 1, 1, 1));
            int l1 = (i1 - i) + 1;
            _rectangles.add(new Rectangle(i, j, l1, 1));
            k1 -= l1;
            _height++;
            int i2 = k1 / i1;
            if(i > 1)
            {
                _pointA = new Point(i, j);
                _pointB = new Point(1, j + 1);
            } else
            {
                _pointA = new Point(1, j);
            }
            if(i2 > 0)
            {
                _rectangles.add(new Rectangle(1, j + 1, i1, i2));
                k1 -= i2 * i1;
                _height += i2;
            }
            if(k1 > 0)
            {
                _rectangles.add(new Rectangle(1, j + i2 + 1, k1, 1));
                _height++;
                _pointC = new Point(i1, j + i2);
                _pointD = new Point(k1, j + i2 + 1);
            } else
            {
                _pointD = new Point(i1, j + i2);
            }
            _endAttrRow = j + i2 + 1;
            _endAttributeRects.add(new Rectangle(k1 + 1, _endAttrRow, 1, 1));
            _leftmostColumn = 1;
            _width = i1;
        }
        if(ifieldoutput.useLargestRectangle())
            initLargestRectangle();
    }

    private static Rectangle createRectangle(Point point, Point point1)
    {
        if(point == null || point1 == null)
            return null;
        else
            return new Rectangle(point.x, point.y, (point1.x - point.x) + 1, (point1.y - point.y) + 1);
    }

    java.util.List getBeginAttributeRects()
    {
        return _beginAttributeRects;
    }

    int getBeginAttrRow()
    {
        if(-1 == _beginAttrRow)
            if(_fieldOutput.getColumn() > 1 || _fieldOutput.getFieldNode().getParentRecord().isWindow())
            {
                _beginAttrRow = getStartingRow();
            } else
            {
                _beginAttrRow = getStartingRow() - 1;
                if(0 == _beginAttrRow)
                {
                    FieldWebSettings fieldwebsettings = _fieldOutput.getFieldWebSettings();
                    if(null != fieldwebsettings && 1 == fieldwebsettings.getRow() && 1 == fieldwebsettings.getColumn())
                        _beginAttrRow = 1;
                }
            }
        return _beginAttrRow;
    }

    int getColumn(int i)
    {
        Rectangle rectangle = getRectangle(i);
        if(rectangle != null)
            return rectangle.x;
        else
            return 0;
    }

    FieldOnRow getCurrentFieldOnRow()
    {
        return _currentFieldOnRow;
    }

    int getElementColumn(int i)
    {
        Rectangle rectangle = getElementRectangle(i);
        if(rectangle != null)
            return rectangle.x;
        else
            return 0;
    }

    int getElementHeight()
    {
        if(getLargestRectangle() != null)
            return getLargestRectangle().height;
        else
            return _fieldOutput.getHeight();
    }

    private Rectangle getElementRectangle(int i)
    {
        if(_largestRectangle != null)
        {
            if(i >= _largestRectangle.y && i < _largestRectangle.y + _largestRectangle.height)
                return _largestRectangle;
            else
                return null;
        } else
        {
            return getRectangle(i);
        }
    }

    int getElementStartingRow()
    {
        if(_largestRectangle != null)
            return _largestRectangle.y;
        else
            return getStartingRow();
    }

    int getElementWidth()
    {
        if(getLargestRectangle() != null)
            return getLargestRectangle().width;
        else
            return _fieldOutput.getWidth();
    }

    int getElementWidth(int i)
    {
        if(_fieldOutput.isSingleDHTMLElement())
            return getElementWidth();
        Rectangle rectangle = getElementRectangle(i);
        if(rectangle != null)
            return rectangle.width;
        else
            return 0;
    }

    java.util.List getEndAttributeRects()
    {
        return _endAttributeRects;
    }

    int getEndAttrRow()
    {
        return _endAttrRow;
    }

    IFieldOutput getFieldOutput()
    {
        return _fieldOutput;
    }

    public int getHeight()
    {
        return _height;
    }

    private Rectangle getLargestRectangle()
    {
        return _largestRectangle;
    }

    public int getLastRow()
    {
        return (getStartingRow() + getHeight()) - 1;
    }

    public int getLeftmostColumn()
    {
        return _leftmostColumn;
    }

    private Rectangle getRectangle(int i)
    {
        for(Iterator iterator = getRectangles().iterator(); iterator.hasNext();)
        {
            Rectangle rectangle = (Rectangle)iterator.next();
            if(i >= rectangle.y && i < rectangle.y + rectangle.height)
                return rectangle;
        }

        return null;
    }

    java.util.List getRectangles()
    {
        return _rectangles;
    }

    public int getStartingRow()
    {
        return _fieldOutput.getRow();
    }

    public int getWidth()
    {
        return _width;
    }

    private void initLargestRectangle()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(createRectangle(_pointA, _pointC));
        arraylist.add(createRectangle(_pointB, _pointD));
        arraylist.add(createRectangle(_pointB, _pointC));
        arraylist.add(createRectangle(_pointA, _pointD));
        Rectangle rectangle = null;
        int i = 0;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
        {
            Rectangle rectangle1 = (Rectangle)iterator.next();
            if(rectangle1 != null)
            {
                int j = rectangle1.width * rectangle1.height;
                if(j > i)
                {
                    i = j;
                    rectangle = rectangle1;
                }
            }
        }

        _largestRectangle = rectangle;
    }

    static boolean intersects(java.util.List list, java.util.List list1)
    {
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Rectangle rectangle = (Rectangle)iterator.next();
            for(Iterator iterator1 = list1.iterator(); iterator1.hasNext();)
                if(rectangle.intersects((Rectangle)iterator1.next()))
                    return true;

        }

        return false;
    }

    boolean isContinuedField()
    {
        return _isContinuedField;
    }

    boolean isWrapped()
    {
        return getFieldOutput().isWrapped();
    }

    boolean overlaps(FieldArea fieldarea)
    {
        java.util.List list = getRectangles();
        java.util.List list1 = fieldarea.getRectangles();
        if(intersects(list, list1))
            return true;
        boolean flag = isContinuedField() && fieldarea.isContinuedField();
        int i = getLeftmostColumn();
        int j = fieldarea.getLeftmostColumn();
        if(flag)
        {
            int k = getStartingRow();
            int l = fieldarea.getStartingRow();
            if(k > l && i < j && intersects(getEndAttributeRects(), fieldarea.getBeginAttributeRects()) || k < l && i > j && intersects(getBeginAttributeRects(), fieldarea.getEndAttributeRects()))
                return false;
        }
        if(intersects(getEndAttributeRects(), list1) || intersects(list, fieldarea.getEndAttributeRects()))
            return true;
        return (!flag || i != 2) && intersects(getBeginAttributeRects(), list1) || (!flag || j != 2) && intersects(list, fieldarea.getBeginAttributeRects());
    }

    void setCurrentFieldOnRow(FieldOnRow fieldonrow)
    {
        _currentFieldOnRow = fieldonrow;
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    private FieldOnRow _currentFieldOnRow;
    private IFieldOutput _fieldOutput;
    private java.util.List _rectangles;
    private java.util.List _beginAttributeRects;
    private java.util.List _endAttributeRects;
    private int _height;
    private int _width;
    private int _leftmostColumn;
    private boolean _isContinuedField;
    private int _endAttrRow;
    private int _beginAttrRow;
    private Point _pointA;
    private Point _pointB;
    private Point _pointC;
    private Point _pointD;
    private Rectangle _largestRectangle;

}
