// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core.example;

import com.ibm.as400ad.webfacing.runtime.core.*;
import java.io.PrintStream;

public class Demo
{

    public Demo()
    {
    }

    public static void main(String args[])
    {
        ElementContainer elementcontainer = new ElementContainer();
        elementcontainer.add(new Element("element"));
        class ListableElement extends Element
            implements IListable
        {

            ListableElement()
            {
                super("listable element");
            }
        }

        elementcontainer.add(new ListableElement());
        class ListOnlyElement extends Element
            implements IListOnly
        {

            ListOnlyElement()
            {
                super("listonly element");
            }
        }

        elementcontainer.add(new ListOnlyElement());
        class ListOnlyObject
            implements IListOnly
        {

            public String toString()
            {
                return new String("listonly object");
            }

            ListOnlyObject()
            {
            }
        }

        elementcontainer.add(new ListOnlyObject());
        String s = new String("keyed string");
        elementcontainer.add(s, new Key("key"));
        ITraverser itraverser = elementcontainer.getTraverser();
        try
        {
            itraverser.forAllDo(new IAction() {

                public void perform(Object obj)
                {
                    System.out.println("target=" + obj);
                }

            });
        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }
    }
}
