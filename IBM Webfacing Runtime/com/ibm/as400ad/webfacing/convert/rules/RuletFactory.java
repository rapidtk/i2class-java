// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;

import com.ibm.as400ad.code400.dom.ExportSettings;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import org.w3c.dom.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            Rulet, RuletType

public class RuletFactory
{

    private RuletFactory(boolean flag)
    {
        _rulets = new Vector();
        _textConstantOption = 0;
        setRuleFile(flag);
        buildRulets();
        setTextConstantOption();
    }

    void buildRulets()
    {
        _rulets.removeAllElements();
        buildRulet(KEY_PATTERN_CLASS_PATH, "CommandKey");
        buildRulet(DSPF_MENU_PATTERN_CLASS_PATH, "Menu");
    }

    public static Rulet createRulet(String s)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return (Rulet)Class.forName(s).newInstance();
    }

    public Rulet getRulet(RuletType rulettype)
    {
        for(Enumeration enumeration = _rulets.elements(); enumeration.hasMoreElements();)
        {
            Rulet rulet = (Rulet)enumeration.nextElement();
            if(rulet.getType().equals(rulettype))
                return rulet;
        }

        return null;
    }

    static RuletFactory getRuletFactory(boolean flag)
    {
        if(singleton == null)
            singleton = new RuletFactory(flag);
        return singleton;
    }

    public static RuletFactory getRuletFactory()
    {
        String s = ExportSettings.getExportSettings().getRulesFile();
        return getRuletFactory(false);
    }

    public static RuletFactory getRuntimeRuletFactory()
    {
        ExportSettings.initializeExportRuntimeSettings("");
        String s = ExportSettings.getExportSettings().getRuntimeRulesFile();
        resetRuletFactory();
        return getRuletFactory(true);
    }

    public Vector getRulets(RuletType rulettype)
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = _rulets.elements(); enumeration.hasMoreElements();)
        {
            Rulet rulet = (Rulet)enumeration.nextElement();
            if(rulet.getType().equals(rulettype))
                vector.addElement(rulet);
        }

        return vector;
    }

    void setRuleFile(boolean flag)
    {
        try
        {
            if(!flag)
                _rulesDOM = ExportSettings.getExportSettings().getRulesDocument();
            else
                _rulesDOM = ExportSettings.getExportSettings().getRuntimeRulesDocument();
        }
        catch(Exception exception)
        {
            System.out.println("Exception in setRuleFile: " + exception.getClass().getName() + ": " + exception.getMessage());
            _rulesDOM = null;
        }
    }

    public static void resetRuletFactory()
    {
        singleton = null;
    }

    void buildRulet(String s, String s1)
    {
        try
        {
            Rulet rulet = createRulet(s);
            if(_rulesDOM != null)
            {
                NodeList nodelist = _rulesDOM.getElementsByTagName(s1);
                if(nodelist != null)
                {
                    for(int i = 0; i < nodelist.getLength(); i++)
                    {
                        rulet.loadRules((Element)nodelist.item(i));
                        _rulets.addElement(rulet);
                    }

                }
            }
        }
        catch(Exception exception) { }
    }

    public int getTextConstantOption()
    {
        return _textConstantOption;
    }

    private void setTextConstantOption()
    {
        if(_rulesDOM != null)
        {
            NodeList nodelist = _rulesDOM.getElementsByTagName("CommandKey");
            if(nodelist != null)
            {
                Element element = (Element)nodelist.item(0);
                if(element != null)
                {
                    NodeList nodelist1 = element.getElementsByTagName("TextConstant");
                    Element element1 = (Element)nodelist1.item(0);
                    if(element1 != null)
                    {
                        String s = element1.getAttribute("option");
                        if(s.equalsIgnoreCase("blanks"))
                            _textConstantOption = 0;
                        else
                        if(s.equalsIgnoreCase("text"))
                            _textConstantOption = 1;
                        else
                        if(s.equalsIgnoreCase("button"))
                            _textConstantOption = 2;
                        else
                        if(s.equalsIgnoreCase("windowbutton"))
                            _textConstantOption = 3;
                    }
                }
            }
        }
    }

    public boolean isTextConstantOptionButton()
    {
        return _textConstantOption == 2;
    }

    public boolean isTextConstantOptionWindowButton()
    {
        return _textConstantOption == 3;
    }

    public boolean isTextConstantOptionText()
    {
        return _textConstantOption == 1;
    }

    public boolean isTextConstantOptionBlanks()
    {
        return _textConstantOption == 0;
    }

    static final String copyRight = "(c) Copyright IBM Corporation 2000-2003 All rights reserved.";
    public static String KEY_PATTERN_CLASS_PATH = "com.ibm.as400ad.webfacing.convert.rules.KeyPatternRulet";
    public static String DSPF_MENU_PATTERN_CLASS_PATH = "com.ibm.as400ad.webfacing.convert.rules.DSPFMenuPatternRulet";
    private Document _rulesDOM;
    private Vector _rulets;
    private static RuletFactory singleton = null;
    public static final int BLANKS = 0;
    public static final int TEXT = 1;
    public static final int BUTTON = 2;
    public static final int WINDOWBUTTON = 3;
    private int _textConstantOption;

}
