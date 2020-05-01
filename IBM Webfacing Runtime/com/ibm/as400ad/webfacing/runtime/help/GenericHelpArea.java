// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpArea

public class GenericHelpArea
    implements ENUM_KeywordIdentifiers, Serializable
{
    public class Cursor
        implements Serializable
    {

        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(obj != null && (obj instanceof Cursor))
            {
                Cursor cursor = (Cursor)obj;
                return _row == cursor.getRow() && _column == cursor.getColumn();
            } else
            {
                return false;
            }
        }

        public int getColumn()
        {
            return _column;
        }

        public int getRow()
        {
            return _row;
        }

        public int hashCode()
        {
            return _row + _column;
        }

        public String toString()
        {
            return getRow() + ", " + getColumn();
        }

        private int _column;
        private int _row;

        public Cursor(int i, int j)
        {
            _column = 0;
            _row = 0;
            _row = i;
            _column = j;
        }
    }


    public GenericHelpArea()
    {
        _topLeft = null;
        _bottomRight = null;
        _topLeft = new Cursor(-1, -1);
        _bottomRight = new Cursor(-1, -1);
    }

    public GenericHelpArea(int i, int j, int k, int l)
    {
        _topLeft = null;
        _bottomRight = null;
        _topLeft = new Cursor(i, j);
        _bottomRight = new Cursor(k, l);
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj != null && (obj instanceof HelpArea))
        {
            HelpArea helparea = (HelpArea)obj;
            return isWithin(helparea);
        } else
        {
            return false;
        }
    }

    public Cursor getBottomRight()
    {
        return _bottomRight;
    }

    public Cursor getTopLeft()
    {
        return _topLeft;
    }

    public int hashCode()
    {
        return _topLeft.hashCode() + _bottomRight.hashCode();
    }

    public boolean isWithin(int i, int j)
    {
        return i >= _topLeft.getRow() && i <= _bottomRight.getRow() && j >= _topLeft.getColumn() && j <= _bottomRight.getColumn();
    }

    public boolean isWithin(Cursor cursor)
    {
        return cursor.getRow() >= _topLeft.getRow() && cursor.getRow() <= _bottomRight.getRow() && cursor.getColumn() >= _topLeft.getColumn() && cursor.getColumn() <= _bottomRight.getColumn();
    }

    public boolean isWithin(HelpArea helparea)
    {
        return _topLeft.getRow() >= helparea.getTopLeft().getRow() && _bottomRight.getRow() <= helparea.getBottomRight().getRow() && _topLeft.getColumn() >= helparea.getTopLeft().getColumn() && _bottomRight.getColumn() <= helparea.getBottomRight().getColumn();
    }

    public int getBottom()
    {
        return _bottomRight.getRow();
    }

    public int getTop()
    {
        return _topLeft.getRow();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private Cursor _topLeft;
    private Cursor _bottomRight;

}
