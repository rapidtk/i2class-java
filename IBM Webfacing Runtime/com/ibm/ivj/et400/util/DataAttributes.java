// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataAttributes.java

package com.ibm.ivj.et400.util;

import java.awt.Color;
import java.beans.*;
import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

// Referenced classes of package com.ibm.ivj.et400.util:
//            Attributes, DataAttributesChangeListener, DataAttributesChangeEvent

public class DataAttributes
    implements Attributes, PropertyChangeListener, VetoableChangeListener, Serializable
{

    public DataAttributes()
    {
        dataLength = 10;
        decimalPlaces = 0;
        dataType = 0;
        decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        reverseImageColor = Color.red;
        autoAdvance = false;
        errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
    }

    public DataAttributes(int dataType, int dataLength)
    {
        this.dataLength = 10;
        decimalPlaces = 0;
        this.dataType = 0;
        decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        reverseImageColor = Color.red;
        autoAdvance = false;
        errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        try
        {
            setDataType(dataType);
            setDataLength(dataLength);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public DataAttributes(int dataType, int dataLength, int decimalPlaces, char decimalSymbol)
    {
        this.dataLength = 10;
        this.decimalPlaces = 0;
        this.dataType = 0;
        this.decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        reverseImageColor = Color.red;
        autoAdvance = false;
        errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        try
        {
            setDataType(dataType);
            setDataLength(dataLength);
            setDecimalPlaces(decimalPlaces);
            setDecimalSymbol(decimalSymbol);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public DataAttributes(int dataType, int dataLength, int decimalPlaces, char decimalSymbol, Color reverseImageColor)
    {
        this.dataLength = 10;
        this.decimalPlaces = 0;
        this.dataType = 0;
        this.decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        this.reverseImageColor = Color.red;
        autoAdvance = false;
        errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        try
        {
            setDataType(dataType);
            setDataLength(dataLength);
            setDecimalPlaces(decimalPlaces);
            setDecimalSymbol(decimalSymbol);
            setReverseImageColor(reverseImageColor);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public DataAttributes(boolean autoAdvance, int dataType, int dataLength, int decimalPlaces, char decimalSymbol, Color reverseImageColor)
    {
        this.dataLength = 10;
        this.decimalPlaces = 0;
        this.dataType = 0;
        this.decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        this.reverseImageColor = Color.red;
        this.autoAdvance = false;
        errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        try
        {
            setAutoAdvance(autoAdvance);
            setDataType(dataType);
            setDataLength(dataLength);
            setDecimalPlaces(decimalPlaces);
            setDecimalSymbol(decimalSymbol);
            setReverseImageColor(reverseImageColor);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public DataAttributes(boolean autoAdvance, int dataType, int dataLength, int decimalPlaces, char decimalSymbol, Color reverseImageColor, boolean errorBeep)
    {
        this.dataLength = 10;
        this.decimalPlaces = 0;
        this.dataType = 0;
        this.decimalSymbol = (new DecimalFormatSymbols()).getDecimalSeparator();
        this.reverseImageColor = Color.red;
        this.autoAdvance = false;
        this.errorBeep = true;
        propertyChange = new PropertyChangeSupport(this);
        vetoPropertyChange = new VetoableChangeSupport(this);
        dataAttributesListeners = new Vector();
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);
        try
        {
            setAutoAdvance(autoAdvance);
            setDataType(dataType);
            setDataLength(dataLength);
            setDecimalPlaces(decimalPlaces);
            setDecimalSymbol(decimalSymbol);
            setReverseImageColor(reverseImageColor);
            setErrorBeep(errorBeep);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void addDataAttributesChangeListener(DataAttributesChangeListener l)
    {
        dataAttributesListeners.addElement(l);
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChange.addPropertyChangeListener(l);
    }

    public void addVetoableChangeListener(VetoableChangeListener l)
    {
        vetoPropertyChange.addVetoableChangeListener(l);
    }

    public static String Copyright()
    {
        return "(C) Copyright IBM Corporation 1997, 2002. All Rights Reserved.";
    }

    public void fireDataAttributesChangeEvent(DataAttributesChangeEvent evt)
    {
        Vector currentListeners = null;
        synchronized(this)
        {
            currentListeners = (Vector)dataAttributesListeners.clone();
        }
        for(int i = 0; i < currentListeners.size(); i++)
        {
            DataAttributesChangeListener listener = (DataAttributesChangeListener)currentListeners.elementAt(i);
            listener.dataAttributesChanged(evt);
        }

    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
    }

    public void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
        throws PropertyVetoException
    {
        getVetoPropertyChange().fireVetoableChange(propertyName, oldValue, newValue);
    }

    public boolean getAutoAdvance()
    {
        return autoAdvance;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getDataType()
    {
        return dataType;
    }

    public int getDecimalPlaces()
    {
        return decimalPlaces;
    }

    public char getDecimalSymbol()
    {
        return decimalSymbol;
    }

    public boolean getErrorBeep()
    {
        return errorBeep;
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
            propertyChange = new PropertyChangeSupport(this);
        return propertyChange;
    }

    public Color getReverseImageColor()
    {
        if(reverseImageColor == null)
            reverseImageColor = Color.red;
        return reverseImageColor;
    }

    protected VetoableChangeSupport getVetoPropertyChange()
    {
        if(vetoPropertyChange == null)
            vetoPropertyChange = new VetoableChangeSupport(this);
        return vetoPropertyChange;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        if(e.getPropertyName().equals("autoAdvance") || e.getPropertyName().equals("dataLength") || e.getPropertyName().equals("dataType") || e.getPropertyName().equals("decimalSymbol") || e.getPropertyName().equals("reverseImage") || e.getPropertyName().equals("decimalPlaces"))
            fireDataAttributesChangeEvent(new DataAttributesChangeEvent(this));
    }

    public synchronized void removeDataAttributesChangeListener(DataAttributesChangeListener l)
    {
        dataAttributesListeners.removeElement(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChange.removePropertyChangeListener(l);
    }

    public void removeVetoableChangeListener(VetoableChangeListener l)
    {
        vetoPropertyChange.removeVetoableChangeListener(l);
    }

    public void setAutoAdvance(boolean value)
    {
        boolean oldValue = autoAdvance;
        autoAdvance = value;
        propertyChange.firePropertyChange("autoAdvance", new Boolean(oldValue), new Boolean(autoAdvance));
    }

    public void setDataLength(int len)
        throws PropertyVetoException
    {
        int oldValue = dataLength;
        vetoPropertyChange.fireVetoableChange("dataLength", new Integer(oldValue), new Integer(len));
        dataLength = len;
        propertyChange.firePropertyChange("dataLength", new Integer(oldValue), new Integer(dataLength));
    }

    public void setDataType(int type)
        throws PropertyVetoException
    {
        int oldValue = dataType;
        vetoPropertyChange.fireVetoableChange("dataType", new Integer(oldValue), new Integer(type));
        dataType = type;
        propertyChange.firePropertyChange("dataType", new Integer(oldValue), new Integer(dataType));
    }

    public void setDecimalPlaces(int decPlaces)
        throws PropertyVetoException
    {
        int oldValue = decimalPlaces;
        vetoPropertyChange.fireVetoableChange("decimalPlaces", new Integer(oldValue), new Integer(decPlaces));
        decimalPlaces = decPlaces;
        propertyChange.firePropertyChange("decimalPlaces", new Integer(oldValue), new Integer(decimalPlaces));
    }

    public void setDecimalSymbol(char dec)
    {
        char oldValue = decimalSymbol;
        decimalSymbol = dec;
        propertyChange.firePropertyChange("decimalSymbol", new Character(oldValue), new Character(decimalSymbol));
    }

    public void setErrorBeep(boolean value)
    {
        boolean oldValue = errorBeep;
        errorBeep = value;
        propertyChange.firePropertyChange("errorBeep", new Boolean(oldValue), new Boolean(errorBeep));
    }

    public void setReverseImageColor(Color c)
    {
        Color oldValue = reverseImageColor;
        reverseImageColor = c;
        propertyChange.firePropertyChange("reverseImageColor", oldValue, c);
    }

    public void vetoableChange(PropertyChangeEvent e)
        throws PropertyVetoException
    {
        int value = ((Integer)e.getNewValue()).intValue();
        if(e.getPropertyName().equals("dataType"))
        {
            if(value != 1 && value != 0)
                throw new PropertyVetoException("invalid dataType, 0 - Numeric, 1 - Character", e);
        } else
        if(e.getPropertyName().equals("decimalPlaces"))
        {
            if(value > getDataLength())
                throw new PropertyVetoException("decimalPlaces > dataLength", e);
            if(value < 0)
                throw new PropertyVetoException("decimalPlaces < 0", e);
        } else
        if(e.getPropertyName().equals("dataLength"))
        {
            if(value < getDecimalPlaces())
                throw new PropertyVetoException("dataLength < decimalPlaces", e);
            if(value <= 0)
                throw new PropertyVetoException("dataLength <= 0", e);
        }
    }

    public static final int DATATYPE_CHARACTER = 0;
    public static final int DATATYPE_NUMERIC = 1;
    public static final char MINUS_SIGN = 45;
    public static final char PLUS_SIGN = 43;
    private int dataLength;
    private int decimalPlaces;
    private int dataType;
    private char decimalSymbol;
    private Color reverseImageColor;
    private boolean autoAdvance;
    private boolean errorBeep;
    protected transient PropertyChangeSupport propertyChange;
    protected transient VetoableChangeSupport vetoPropertyChange;
    private Vector dataAttributesListeners;
}
