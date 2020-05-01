// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            Rule, Property, WorkUnit, Unit, 
//            RuletFactory, ResultContainer

public class StringPatternRule extends Rule
{

    public StringPatternRule()
    {
    }

    public Object apply(Object obj, ResultContainer resultcontainer, Vector vector)
    {
        String s = (String)obj;
        String s1 = getPrefix();
        if(s1 == null)
            return s;
        String s2 = getSeparator();
        if(s2 == null)
            return s;
        String s3;
        WorkUnit workunit;
        for(s3 = s; s != null; s3 = workunit.getReplacedString())
        {
            workunit = getUnit(s3, s, s1, s2, getAllSeparators(vector));
            if(workunit == null)
                break;
            resultcontainer.add(workunit.unit);
            s = workunit.remains;
        }

        return s3;
    }

    protected Vector getAllSeparators(Vector vector)
    {
        Vector vector1 = new Vector();
        for(int i = 0; i < vector.size(); i++)
        {
            Hashtable hashtable = (Hashtable)vector.elementAt(i);
            Property property = (Property)hashtable.get(Property.KEY_SEPARATOR);
            if(property != null)
                vector1.addElement(property.value);
        }

        return vector1;
    }

    public String getPrefix()
    {
        Property property = (Property)super.properties.get(Property.KEY_PREFIX);
        if(property == null)
            return null;
        else
            return property.value;
    }

    public String getSeparator()
    {
        Property property = (Property)super.properties.get(Property.KEY_SEPARATOR);
        if(property == null)
            return null;
        else
            return property.value;
    }

    protected WorkUnit getUnit(String s, String s1, String s2, String s3, Vector vector)
    {
        Enumeration enumeration = vector.elements();
        int i = s1.lastIndexOf(s3);
        int j = i;
        if(i == -1)
            return null;
        int k = i + s3.length();
        int l = s1.length();
        int i1 = l;
        while(enumeration.hasMoreElements()) 
        {
            String s4 = (String)enumeration.nextElement();
            if(s1.indexOf(s4, k) >= 0)
            {
                i1 = s1.indexOf(s4, k);
                for(i1--; s1.charAt(i1) == ' '; i1--);
                int k1 = s1.substring(k, i1 + 1).lastIndexOf(" ");
                if(k1 >= 0)
                    i1 = k + k1;
            }
            if(l > i1)
                l = i1;
        }
        int j1;
        for(j1 = i - 1; j1 >= 0 && s1.charAt(j1) == ' '; j1--);
        int l1;
        for(l1 = j1; l1 >= 0 && Character.isDigit(s1.charAt(l1)); l1--);
        if(++l1 > j1)
            return null;
        int i2 = l1 - 1;
        if(s2.length() > 0)
        {
            int j2 = i2;
            i2 = j2 - s2.length();
            boolean flag = false;
            do
                flag = s1.regionMatches(false, i2 + 1, s2, 0, s2.length());
            while(!flag && i2-- >= 0 && j2 >= 0 && s1.charAt(j2--) == ' ');
            if(!flag)
                return null;
        }
        String s5 = null;
        if(i2 >= 0)
            s5 = s1.substring(0, i2 + 1);
        try
        {
            int k2 = Integer.parseInt(s1.substring(l1, j1 + 1));
            StringBuffer stringbuffer = new StringBuffer(s);
            if(s2.length() > 0 && !RuletFactory.getRuletFactory().isTextConstantOptionText() && k2 >= 1 && k2 <= 24)
            {
                if(j1 - l1 > 1)
                    return null;
                for(int l2 = j; l2 < j + s3.length(); l2++)
                {
                    stringbuffer.delete(l2, l2 + 1);
                    stringbuffer.insert(l2, ' ');
                }

                for(int i3 = l1; i3 < j1 + 1; i3++)
                {
                    stringbuffer.delete(i3, i3 + 1);
                    stringbuffer.insert(i3, ' ');
                }

                for(int j3 = k; j3 < l; j3++)
                {
                    stringbuffer.delete(j3, j3 + 1);
                    stringbuffer.insert(j3, ' ');
                }

                int k3 = s.substring(0, l1).lastIndexOf(s2);
                for(int l3 = k3; l3 < k3 + s2.length(); l3++)
                {
                    stringbuffer.delete(l3, l3 + 1);
                    stringbuffer.insert(l3, ' ');
                }

            }
            return new WorkUnit(new Unit(k2, s1.substring(k, l).trim()), s5, stringbuffer.toString());
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    public void setProperties(String s, String s1)
    {
        super.setProperty(Property.KEY_PREFIX, s);
        super.setProperty(Property.KEY_SEPARATOR, s1);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");

}
