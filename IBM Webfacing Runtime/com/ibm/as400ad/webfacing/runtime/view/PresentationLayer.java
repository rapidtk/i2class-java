// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle, RecordViewBean, SubfileControlRecordViewBean, IDeviceLayer, 
//            ConceptualLayer, CursorPosition, LocationOnDevice, ClearedLines

class PresentationLayer
    implements IDeviceLayer, Cloneable
{
    private class RecordsIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            if(_peekAhead != null)
                return true;
            if(_rectangles.hasNext())
            {
                do
                    _peekAhead = _rectangles.next();
                while((_peekAhead instanceof ClearedLines) && _rectangles.hasNext());
                if(_peekAhead != null && !(_peekAhead instanceof ClearedLines))
                    return true;
            }
            return false;
        }

        public Object next()
        {
            Object obj = null;
            if(_peekAhead != null)
            {
                obj = _peekAhead;
                _peekAhead = null;
            } else
            {
                do
                    obj = _rectangles.next();
                while(obj instanceof ClearedLines);
            }
            return obj;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        private Iterator _rectangles;
        private Object _peekAhead;

        RecordsIterator()
        {
            _peekAhead = null;
            _rectangles = _presentationLayerRectangles.iterator();
        }
    }


    public PresentationLayer(ConceptualLayer conceptuallayer, int i, VisibleRectangle visiblerectangle)
        throws WebfacingInternalException
    {
        _CLRLWindow = false;
        _windowTitle = "";
        _windowTitleAlignment = "";
        _presentationLayerRectangles = new LinkedList();
        _windowLayer = false;
        _name = null;
        _focusCapable = true;
        _conceptualLayer = conceptuallayer;
        _type = i;
        add(visiblerectangle);
    }

    public void add(VisibleRectangle visiblerectangle)
        throws WebfacingInternalException
    {
        boolean flag = false;
        for(int i = 0; i < _presentationLayerRectangles.size() && !flag; i++)
            if(((VisibleRectangle)_presentationLayerRectangles.get(i)).getFirstFieldLine() > visiblerectangle.getFirstFieldLine())
            {
                _presentationLayerRectangles.add(i, visiblerectangle);
                flag = true;
            }

        if(!flag)
            _presentationLayerRectangles.add(visiblerectangle);
        visiblerectangle.setPresentationLayer(this);
        if((visiblerectangle instanceof RecordViewBean) && _name == null)
        {
            RecordViewBean recordviewbean = (RecordViewBean)visiblerectangle;
            if(recordviewbean.isWindowed())
            {
                _windowLayer = true;
                _conceptualLayer.setWindowLayer(true);
                if(recordviewbean.isWdwREF())
                {
                    _name = recordviewbean.getWdwRefName();
                } else
                {
                    _name = recordviewbean.getRecordName();
                    _windowTitle = recordviewbean.getWindowTitle();
                    _windowTitleAlignment = recordviewbean.getWindowTitleAlignment();
                }
            } else
            {
                _windowLayer = false;
                _conceptualLayer.setWindowLayer(false);
                _name = recordviewbean.getRecordName();
                if(_type == 2)
                {
                    _CLRLWindow = recordviewbean.hasWindowTitle();
                    if(_CLRLWindow)
                        _windowTitle = recordviewbean.getWindowTitle();
                }
            }
        }
    }

    public Object clone()
    {
        PresentationLayer presentationlayer = null;
        try
        {
            presentationlayer = (PresentationLayer)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        presentationlayer._presentationLayerRectangles = new LinkedList();
        int i = _presentationLayerRectangles.size();
        for(int j = 0; j < i; j++)
        {
            VisibleRectangle visiblerectangle = (VisibleRectangle)((VisibleRectangle)_presentationLayerRectangles.get(j)).clone();
            visiblerectangle.setPresentationLayer(presentationlayer);
            presentationlayer._presentationLayerRectangles.add(visiblerectangle);
        }

        return presentationlayer;
    }

    public VisibleRectangle getFirst()
    {
        return (VisibleRectangle)_presentationLayerRectangles.getFirst();
    }

    public int getFirstColumn()
    {
        if(_windowLayer)
            return ((RecordViewBean)getFirst()).getWdwFirstColumn();
        if(_type == 2)
            return ((RecordViewBean)getFirst()).getFirstColumn();
        else
            return 1;
    }

    public int getFirstRow()
    {
        if(_windowLayer)
            return ((RecordViewBean)getFirst()).getWdwFirstLine();
        if(_type == 0)
            return 1;
        else
            return getFirst().getFirstFieldLine();
    }

    public int getLastColumn()
    {
        if(_type == 2)
            return ((RecordViewBean)getFirst()).getLastColumn();
        else
            return 132;
    }

    public int getLastRow()
    {
        return ((VisibleRectangle)_presentationLayerRectangles.getLast()).getLastFieldLine();
    }

    public Iterator getRecords()
    {
        return new RecordsIterator();
    }

    public LinkedList getRectangles()
    {
        return _presentationLayerRectangles;
    }

    public Iterator getRectanglesIterator()
    {
        return _presentationLayerRectangles.iterator();
    }

    public int getType()
    {
        return _type;
    }

    public String getWindowTitle()
    {
        return _windowTitle;
    }

    public String getWindowTitleAlignment()
    {
        return _windowTitleAlignment;
    }

    public boolean hasNoRecordOverlappedBy(int i, int j)
    {
        for(int k = 0; k < _presentationLayerRectangles.size(); k++)
        {
            VisibleRectangle visiblerectangle = (VisibleRectangle)_presentationLayerRectangles.get(k);
            if(visiblerectangle.getFirstFieldLine() <= j && visiblerectangle.getLastFieldLine() >= i)
                return false;
        }

        return true;
    }

    public boolean isCLRLWindow()
    {
        return _CLRLWindow;
    }

    public boolean isEmpty()
    {
        return _presentationLayerRectangles.isEmpty();
    }

    public boolean isFocusCapable()
    {
        return _focusCapable;
    }

    public boolean isVerticallyPositioned()
    {
        return _windowLayer || _type == 1 || _type == 2;
    }

    public boolean isWindowed()
    {
        return _windowLayer;
    }

    public String name()
    {
        return _name;
    }

    public void remove(VisibleRectangle visiblerectangle)
    {
        _presentationLayerRectangles.remove(visiblerectangle);
    }

    public void setConceptualLayer(ConceptualLayer conceptuallayer)
    {
        _conceptualLayer = conceptuallayer;
    }

    public void setFocusCapable(boolean flag)
    {
        _focusCapable = flag;
    }

    public void setType(int i)
    {
        _type = i;
    }

    LocationOnDevice getFirstFocusCapableField()
    {
        Object obj = null;
        for(Iterator iterator = getRecords(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            LocationOnDevice locationondevice = recordviewbean.getFirstFocusCapableField();
            if(locationondevice != null)
                return locationondevice;
        }

        return null;
    }

    public RecordViewBean[] getFirstRollEnabledRecords()
    {
        RecordViewBean recordviewbean = null;
        RecordViewBean recordviewbean1 = null;
        for(Iterator iterator = getRecords(); iterator.hasNext() && (recordviewbean == null || recordviewbean1 == null);)
        {
            RecordViewBean recordviewbean2 = (RecordViewBean)iterator.next();
            if((recordviewbean2 instanceof SubfileControlRecordViewBean) && ((SubfileControlRecordViewBean)recordviewbean2).isScrollbarShown())
            {
                if(recordviewbean == null)
                    recordviewbean = recordviewbean2;
                if(recordviewbean1 == null)
                    recordviewbean1 = recordviewbean2;
            }
            if(recordviewbean == null && recordviewbean2.isKeywordActive(156L))
                recordviewbean = recordviewbean2;
            if(recordviewbean1 == null && recordviewbean2.isKeywordActive(155L))
                recordviewbean1 = recordviewbean2;
        }

        RecordViewBean arecordviewbean[] = {
            recordviewbean, recordviewbean1
        };
        return arecordviewbean;
    }

    LocationOnDevice getNamedFieldAt(CursorPosition cursorposition)
    {
        RecordViewBean recordviewbean = getRecordAt(cursorposition);
        if(recordviewbean != null)
        {
            LocationOnDevice locationondevice = recordviewbean.getNamedFieldAt(cursorposition);
            if(locationondevice != null)
                return locationondevice;
        }
        return null;
    }

    LocationOnDevice getLocationOnDeviceAt(CursorPosition cursorposition)
    {
        if(isInScope(cursorposition.getScopeQualifier()))
        {
            RecordViewBean recordviewbean = getRecordAt(cursorposition);
            if(recordviewbean != null)
            {
                LocationOnDevice locationondevice = recordviewbean.getLocationOnDeviceAt(cursorposition);
                if(locationondevice != null)
                    return locationondevice;
            }
            Iterator iterator = getRecords();
            if(iterator.hasNext())
            {
                LocationOnDevice locationondevice1 = ((RecordViewBean)iterator.next()).getLocationOnDevice(cursorposition);
                locationondevice1.setIsValidForRTNCSRLOC(false);
                return locationondevice1;
            }
        }
        return null;
    }

    RecordViewBean getRecordAt(CursorPosition cursorposition)
    {
        for(Iterator iterator = getRecords(); iterator.hasNext();)
        {
            RecordViewBean recordviewbean = (RecordViewBean)iterator.next();
            if(recordviewbean.containLocation(cursorposition.getRow(), cursorposition.getColumn()))
                return recordviewbean;
        }

        return null;
    }

    private boolean isInScope(String s)
    {
        if(s == null)
            return true;
        Iterator iterator = getRecords();
        if(iterator.hasNext())
            return ((RecordViewBean)iterator.next()).isInScope(s);
        else
            return false;
    }

    public static final int NORMALLY_POSITIONED = 0;
    public static final int VERTICALLY_POSITIONED = 1;
    private boolean _CLRLWindow;
    private String _windowTitle;
    private String _windowTitleAlignment;
    public static final int FULLY_POSITIONED = 2;
    private int _type;
    private LinkedList _presentationLayerRectangles;
    private boolean _windowLayer;
    private String _name;
    private boolean _focusCapable;
    private ConceptualLayer _conceptualLayer;

}
