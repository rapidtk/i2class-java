// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_EventLogID;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import java.util.Hashtable;
import java.util.Iterator;

public class ConversionLogger
    implements ENUM_KeywordIdentifiers, ENUM_EventLogID
{

    public ConversionLogger()
    {
        refExists = false;
    }

    public void checkKeywords(FieldNode fieldnode)
    {
        if(fieldnode == null)
            return;
        setRefExists(fieldnode.getReferenceExists());
        if(isFileLevelREF() && fieldnode.isDBReferenceField() && !isRefExists())
        {
            setAbortRecordConversion(true);
            fieldnode.logEvent(24);
        }
        if(fieldnode.getFieldUsage() == 'M')
            fieldnode.logEvent(28);
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywords();
        checkKeywords(keywordnodeenumeration);
    }

    public void checkKeywords(FileNode filenode)
    {
        if(filenode == null)
        {
            return;
        } else
        {
            setFileLevelREF(false);
            KeywordNodeEnumeration keywordnodeenumeration = filenode.getKeywords();
            checkKeywords(keywordnodeenumeration);
            return;
        }
    }

    private void checkKeywords(KeywordNodeEnumeration keywordnodeenumeration)
    {
        if(keywordnodeenumeration == null)
            return;
        boolean flag = false;
        boolean flag1 = false;
        KeywordNode keywordnode = null;
        while(keywordnodeenumeration.hasMoreElements()) 
        {
            KeywordNode keywordnode1 = keywordnodeenumeration.nextKeyword();
            Integer integer = new Integer(keywordnode1.getKeywordId());
            if(_kwdLogTable.containsKey(integer))
            {
                int i = ((Integer)_kwdLogTable.get(integer)).intValue();
                if(i >= 0)
                    keywordnode1.logEvent(i);
                else
                    switch(i)
                    {
                    case -2: 
                        checkParameters(keywordnode1);
                        break;

                    case -4: 
                        KeywordParm keywordparm = keywordnode1.getFirstParm();
                        switch(keywordnode1.getKeywordId())
                        {
                        case 93: // ']'
                            if(null != keywordparm && keywordparm.getParmValType() == 9)
                            {
                                char c = keywordparm.getVarChar();
                                if(c >= '5' && c <= '9')
                                    keywordnode1.logEvent(20);
                            }
                            break;

                        case 158: 
                            if(null == keywordparm)
                                keywordnode1.logEvent(22);
                            else
                            if(keywordparm.getParmType() == 31)
                                keywordnode1.logEvent(23);
                            break;

                        case 166: 
                            setFileLevelREF(true);
                            break;

                        case 167: 
                            if(!isFileLevelREF() && !isRefExists())
                            {
                                setAbortRecordConversion(true);
                                keywordnode1.logEvent(25);
                            }
                            break;

                        case 226: 
                            flag1 = true;
                            break;

                        case 74: // 'J'
                            flag = true;
                            keywordnode = keywordnode1;
                            break;
                        }
                        break;
                    }
            } else
            {
                keywordnode1.logEvent(1);
            }
        }
        if(flag && flag1)
            keywordnode.logEvent(38);
    }

    public void checkKeywords(RecordNode recordnode)
    {
        if(recordnode == null)
            return;
        setAbortRecordConversion(false);
        Iterator iterator = recordnode.getHelpspecs();
        if(iterator != null)
        {
            HelpspecNode helpspecnode;
            for(; iterator.hasNext(); checkKeywords(helpspecnode))
                helpspecnode = (HelpspecNode)iterator.next();

        }
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywords();
        checkKeywords(keywordnodeenumeration);
    }

    private void checkKeywords(HelpspecNode helpspecnode)
    {
        if(helpspecnode == null)
        {
            return;
        } else
        {
            KeywordNodeEnumeration keywordnodeenumeration = helpspecnode.getKeywords();
            checkKeywords(keywordnodeenumeration);
            return;
        }
    }

    private void checkParameters(KeywordNode keywordnode)
    {
        if(keywordnode == null)
            return;
        int i = 1;
        for(KeywordParm keywordparm = keywordnode.findParameterByPosition(i); keywordparm != null; keywordparm = keywordnode.findParameterByPosition(++i))
            if(keywordparm.getParmValType() == 2)
            {
                Integer integer = new Integer(keywordparm.getVarParmToken());
                if(_parmLogTable.containsKey(integer))
                {
                    int j = ((Integer)_parmLogTable.get(integer)).intValue();
                    if(j >= 0)
                        keywordnode.logEvent(j);
                }
            }

    }

    public static boolean isAbortRecordConversion()
    {
        return abortRecordConversion;
    }

    private boolean isFileLevelREF()
    {
        return fileLevelREF;
    }

    private boolean isRefExists()
    {
        return refExists;
    }

    public static void setAbortRecordConversion(boolean flag)
    {
        abortRecordConversion = flag;
    }

    private void setFileLevelREF(boolean flag)
    {
        fileLevelREF = flag;
    }

    private void setRefExists(boolean flag)
    {
        refExists = flag;
    }

    public static final int FULL_SUPPORT = -1;
    public static final int PARTIAL_PARM_SUPPORT = -2;
    public static final int PARTIAL_SUPPORT_HANDLED_ELSEWHERE = -3;
    public static final int PARTIAL_SUPPORT_HANDLED_HERE = -4;
    public static Hashtable _kwdLogTable;
    public static Hashtable _parmLogTable;
    private static boolean abortRecordConversion = false;
    private static boolean fileLevelREF = false;
    private boolean refExists;

    static 
    {
        _kwdLogTable = new Hashtable();
        _parmLogTable = new Hashtable();
        Integer integer = new Integer(-1);
        Integer integer1 = new Integer(-2);
        Integer integer2 = new Integer(-3);
        Integer integer3 = new Integer(-4);
        Integer integer4 = new Integer(4);
        Integer integer5 = new Integer(5);
        Integer integer6 = new Integer(6);
        Integer integer7 = new Integer(7);
        Integer integer8 = new Integer(8);
        Integer integer9 = new Integer(9);
        Integer integer10 = new Integer(10);
        Integer integer11 = new Integer(11);
        Integer integer12 = new Integer(26);
        _kwdLogTable.put(new Integer(1), integer4);
        _kwdLogTable.put(new Integer(2), integer);
        _kwdLogTable.put(new Integer(3), integer);
        _kwdLogTable.put(new Integer(4), integer);
        _kwdLogTable.put(new Integer(5), integer);
        _kwdLogTable.put(new Integer(6), integer);
        _kwdLogTable.put(new Integer(7), integer9);
        _kwdLogTable.put(new Integer(8), integer9);
        _kwdLogTable.put(new Integer(9), new Integer(16));
        _kwdLogTable.put(new Integer(10), integer);
        _kwdLogTable.put(new Integer(11), integer);
        _kwdLogTable.put(new Integer(12), integer9);
        _kwdLogTable.put(new Integer(13), integer5);
        _kwdLogTable.put(new Integer(14), integer);
        _kwdLogTable.put(new Integer(15), integer);
        _kwdLogTable.put(new Integer(16), integer);
        _kwdLogTable.put(new Integer(17), integer);
        _kwdLogTable.put(new Integer(18), integer);
        _kwdLogTable.put(new Integer(19), integer);
        _kwdLogTable.put(new Integer(20), integer);
        _kwdLogTable.put(new Integer(21), integer);
        _kwdLogTable.put(new Integer(22), integer);
        _kwdLogTable.put(new Integer(23), integer);
        _kwdLogTable.put(new Integer(24), integer);
        _kwdLogTable.put(new Integer(25), integer);
        _kwdLogTable.put(new Integer(26), integer);
        _kwdLogTable.put(new Integer(27), integer);
        _kwdLogTable.put(new Integer(28), integer);
        _kwdLogTable.put(new Integer(29), integer);
        _kwdLogTable.put(new Integer(30), integer);
        _kwdLogTable.put(new Integer(31), integer);
        _kwdLogTable.put(new Integer(32), integer);
        _kwdLogTable.put(new Integer(33), integer);
        _kwdLogTable.put(new Integer(34), integer);
        _kwdLogTable.put(new Integer(35), integer);
        _kwdLogTable.put(new Integer(36), integer);
        _kwdLogTable.put(new Integer(37), integer);
        _kwdLogTable.put(new Integer(38), integer);
        _kwdLogTable.put(new Integer(39), integer);
        _kwdLogTable.put(new Integer(40), integer);
        _kwdLogTable.put(new Integer(41), integer);
        _kwdLogTable.put(new Integer(42), integer);
        _kwdLogTable.put(new Integer(43), integer);
        _kwdLogTable.put(new Integer(44), integer);
        _kwdLogTable.put(new Integer(45), integer);
        _kwdLogTable.put(new Integer(46), integer);
        _kwdLogTable.put(new Integer(47), integer);
        _kwdLogTable.put(new Integer(48), integer);
        _kwdLogTable.put(new Integer(49), integer);
        _kwdLogTable.put(new Integer(50), integer);
        _kwdLogTable.put(new Integer(51), integer);
        _kwdLogTable.put(new Integer(52), integer);
        _kwdLogTable.put(new Integer(53), integer);
        _kwdLogTable.put(new Integer(54), integer);
        _kwdLogTable.put(new Integer(55), integer);
        _kwdLogTable.put(new Integer(56), integer);
        _kwdLogTable.put(new Integer(57), integer);
        _kwdLogTable.put(new Integer(58), integer);
        _kwdLogTable.put(new Integer(59), integer);
        _kwdLogTable.put(new Integer(60), integer);
        _kwdLogTable.put(new Integer(61), integer);
        _kwdLogTable.put(new Integer(62), integer);
        _kwdLogTable.put(new Integer(63), integer9);
        _kwdLogTable.put(new Integer(64), integer4);
        _kwdLogTable.put(new Integer(65), integer5);
        _kwdLogTable.put(new Integer(66), integer4);
        _kwdLogTable.put(new Integer(67), integer4);
        _kwdLogTable.put(new Integer(68), integer);
        _kwdLogTable.put(new Integer(69), integer);
        _kwdLogTable.put(new Integer(70), integer);
        _kwdLogTable.put(new Integer(71), integer5);
        _kwdLogTable.put(new Integer(72), integer6);
        _kwdLogTable.put(new Integer(73), integer);
        _kwdLogTable.put(new Integer(74), integer3);
        _kwdLogTable.put(new Integer(75), integer2);
        _kwdLogTable.put(new Integer(76), integer);
        _kwdLogTable.put(new Integer(77), integer);
        _kwdLogTable.put(new Integer(78), integer2);
        _kwdLogTable.put(new Integer(79), integer);
        _kwdLogTable.put(new Integer(80), integer);
        _kwdLogTable.put(new Integer(81), integer);
        _kwdLogTable.put(new Integer(82), integer);
        _kwdLogTable.put(new Integer(83), integer);
        _kwdLogTable.put(new Integer(84), integer);
        _kwdLogTable.put(new Integer(85), integer);
        _kwdLogTable.put(new Integer(86), integer);
        _kwdLogTable.put(new Integer(87), integer);
        _kwdLogTable.put(new Integer(88), integer1);
        _kwdLogTable.put(new Integer(89), integer2);
        _kwdLogTable.put(new Integer(90), integer5);
        _kwdLogTable.put(new Integer(91), integer);
        _kwdLogTable.put(new Integer(92), integer4);
        _kwdLogTable.put(new Integer(93), integer3);
        _kwdLogTable.put(new Integer(94), integer);
        _kwdLogTable.put(new Integer(95), integer);
        _kwdLogTable.put(new Integer(96), integer4);
        _kwdLogTable.put(new Integer(97), integer);
        _kwdLogTable.put(new Integer(98), integer5);
        _kwdLogTable.put(new Integer(99), integer);
        _kwdLogTable.put(new Integer(100), integer);
        _kwdLogTable.put(new Integer(101), integer);
        _kwdLogTable.put(new Integer(102), integer5);
        _kwdLogTable.put(new Integer(103), integer4);
        _kwdLogTable.put(new Integer(104), integer6);
        _kwdLogTable.put(new Integer(105), integer);
        _kwdLogTable.put(new Integer(106), integer6);
        _kwdLogTable.put(new Integer(107), new Integer(21));
        _kwdLogTable.put(new Integer(108), integer);
        _kwdLogTable.put(new Integer(109), integer);
        _kwdLogTable.put(new Integer(110), integer);
        _kwdLogTable.put(new Integer(111), integer4);
        _kwdLogTable.put(new Integer(112), integer10);
        _kwdLogTable.put(new Integer(113), integer);
        _kwdLogTable.put(new Integer(114), integer4);
        _kwdLogTable.put(new Integer(115), integer);
        _kwdLogTable.put(new Integer(116), integer);
        _kwdLogTable.put(new Integer(117), integer);
        _kwdLogTable.put(new Integer(118), integer);
        _kwdLogTable.put(new Integer(119), integer9);
        _kwdLogTable.put(new Integer(120), integer);
        _kwdLogTable.put(new Integer(121), integer9);
        _kwdLogTable.put(new Integer(122), integer);
        _kwdLogTable.put(new Integer(123), integer);
        _kwdLogTable.put(new Integer(124), integer);
        _kwdLogTable.put(new Integer(125), integer5);
        _kwdLogTable.put(new Integer(126), integer);
        _kwdLogTable.put(new Integer(127), integer);
        _kwdLogTable.put(new Integer(128), integer5);
        _kwdLogTable.put(new Integer(129), integer5);
        _kwdLogTable.put(new Integer(130), integer6);
        _kwdLogTable.put(new Integer(131), integer);
        _kwdLogTable.put(new Integer(132), integer4);
        _kwdLogTable.put(new Integer(133), integer);
        _kwdLogTable.put(new Integer(134), integer);
        _kwdLogTable.put(new Integer(135), integer);
        _kwdLogTable.put(new Integer(136), integer6);
        _kwdLogTable.put(new Integer(137), new Integer(42));
        _kwdLogTable.put(new Integer(138), integer5);
        _kwdLogTable.put(new Integer(139), integer5);
        _kwdLogTable.put(new Integer(140), integer5);
        _kwdLogTable.put(new Integer(141), integer5);
        _kwdLogTable.put(new Integer(142), integer4);
        _kwdLogTable.put(new Integer(143), integer9);
        _kwdLogTable.put(new Integer(144), integer9);
        _kwdLogTable.put(new Integer(145), integer4);
        _kwdLogTable.put(new Integer(146), integer4);
        _kwdLogTable.put(new Integer(147), integer);
        _kwdLogTable.put(new Integer(148), integer);
        _kwdLogTable.put(new Integer(149), integer4);
        _kwdLogTable.put(new Integer(150), integer6);
        _kwdLogTable.put(new Integer(151), integer9);
        _kwdLogTable.put(new Integer(152), integer);
        _kwdLogTable.put(new Integer(153), integer5);
        _kwdLogTable.put(new Integer(154), integer5);
        _kwdLogTable.put(new Integer(155), integer);
        _kwdLogTable.put(new Integer(156), integer);
        _kwdLogTable.put(new Integer(157), integer10);
        _kwdLogTable.put(new Integer(158), integer3);
        _kwdLogTable.put(new Integer(159), integer);
        _kwdLogTable.put(new Integer(160), integer5);
        _kwdLogTable.put(new Integer(161), integer5);
        _kwdLogTable.put(new Integer(162), integer5);
        _kwdLogTable.put(new Integer(163), integer5);
        _kwdLogTable.put(new Integer(164), integer4);
        _kwdLogTable.put(new Integer(165), integer2);
        _kwdLogTable.put(new Integer(166), integer3);
        _kwdLogTable.put(new Integer(167), integer3);
        _kwdLogTable.put(new Integer(168), integer);
        _kwdLogTable.put(new Integer(169), integer);
        _kwdLogTable.put(new Integer(170), integer9);
        _kwdLogTable.put(new Integer(171), integer);
        _kwdLogTable.put(new Integer(172), integer);
        _kwdLogTable.put(new Integer(173), integer);
        _kwdLogTable.put(new Integer(174), integer1);
        _kwdLogTable.put(new Integer(175), integer);
        _kwdLogTable.put(new Integer(176), integer);
        _kwdLogTable.put(new Integer(177), integer);
        _kwdLogTable.put(new Integer(178), integer);
        _kwdLogTable.put(new Integer(179), integer4);
        _kwdLogTable.put(new Integer(180), integer);
        _kwdLogTable.put(new Integer(181), integer4);
        _kwdLogTable.put(new Integer(182), integer);
        _kwdLogTable.put(new Integer(183), integer);
        _kwdLogTable.put(new Integer(184), integer);
        _kwdLogTable.put(new Integer(185), integer);
        _kwdLogTable.put(new Integer(186), integer);
        _kwdLogTable.put(new Integer(187), integer);
        _kwdLogTable.put(new Integer(188), integer);
        _kwdLogTable.put(new Integer(189), integer4);
        _kwdLogTable.put(new Integer(190), integer);
        _kwdLogTable.put(new Integer(191), integer);
        _kwdLogTable.put(new Integer(192), integer);
        _kwdLogTable.put(new Integer(193), integer5);
        _kwdLogTable.put(new Integer(194), integer);
        _kwdLogTable.put(new Integer(195), integer);
        _kwdLogTable.put(new Integer(196), integer);
        _kwdLogTable.put(new Integer(197), integer);
        _kwdLogTable.put(new Integer(198), integer);
        _kwdLogTable.put(new Integer(199), integer);
        _kwdLogTable.put(new Integer(200), integer);
        _kwdLogTable.put(new Integer(201), integer);
        _kwdLogTable.put(new Integer(202), integer);
        _kwdLogTable.put(new Integer(203), integer);
        _kwdLogTable.put(new Integer(204), integer4);
        _kwdLogTable.put(new Integer(205), integer5);
        _kwdLogTable.put(new Integer(206), integer);
        _kwdLogTable.put(new Integer(207), integer);
        _kwdLogTable.put(new Integer(208), integer5);
        _kwdLogTable.put(new Integer(209), integer);
        _kwdLogTable.put(new Integer(210), integer5);
        _kwdLogTable.put(new Integer(211), integer);
        _kwdLogTable.put(new Integer(212), integer);
        _kwdLogTable.put(new Integer(213), integer);
        _kwdLogTable.put(new Integer(214), integer);
        _kwdLogTable.put(new Integer(215), integer);
        _kwdLogTable.put(new Integer(216), integer5);
        _kwdLogTable.put(new Integer(217), integer);
        _kwdLogTable.put(new Integer(218), integer12);
        _kwdLogTable.put(new Integer(219), integer5);
        _kwdLogTable.put(new Integer(220), integer5);
        _kwdLogTable.put(new Integer(221), integer);
        _kwdLogTable.put(new Integer(222), integer);
        _kwdLogTable.put(new Integer(223), integer);
        _kwdLogTable.put(new Integer(224), integer4);
        _kwdLogTable.put(new Integer(225), integer1);
        _kwdLogTable.put(new Integer(226), integer3);
        _kwdLogTable.put(new Integer(227), integer);
        _kwdLogTable.put(new Integer(228), new Integer(1));
        _kwdLogTable.put(new Integer(229), integer4);
        _parmLogTable.put(new Integer(230), integer);
        _parmLogTable.put(new Integer(231), integer);
        _parmLogTable.put(new Integer(232), integer);
        _parmLogTable.put(new Integer(233), integer);
        _parmLogTable.put(new Integer(234), integer);
        _parmLogTable.put(new Integer(235), integer);
        _parmLogTable.put(new Integer(236), integer);
        _parmLogTable.put(new Integer(237), integer);
        _parmLogTable.put(new Integer(238), integer);
        _parmLogTable.put(new Integer(239), integer);
        _parmLogTable.put(new Integer(240), integer);
        _parmLogTable.put(new Integer(241), integer2);
        _parmLogTable.put(new Integer(242), integer2);
        _parmLogTable.put(new Integer(243), integer2);
        _parmLogTable.put(new Integer(244), integer2);
        _parmLogTable.put(new Integer(245), integer);
        _parmLogTable.put(new Integer(246), integer);
        _parmLogTable.put(new Integer(247), integer);
        _parmLogTable.put(new Integer(248), integer);
        _parmLogTable.put(new Integer(249), integer);
        _parmLogTable.put(new Integer(250), integer);
        _parmLogTable.put(new Integer(251), integer);
        _parmLogTable.put(new Integer(252), integer);
        _parmLogTable.put(new Integer(253), integer);
        _parmLogTable.put(new Integer(254), integer);
        _parmLogTable.put(new Integer(255), integer);
        _parmLogTable.put(new Integer(256), integer);
        _parmLogTable.put(new Integer(257), integer);
        _parmLogTable.put(new Integer(258), integer4);
        _parmLogTable.put(new Integer(259), integer);
        _parmLogTable.put(new Integer(260), integer);
        _parmLogTable.put(new Integer(261), integer);
        _parmLogTable.put(new Integer(262), integer);
        _parmLogTable.put(new Integer(263), integer);
        _parmLogTable.put(new Integer(264), integer);
        _parmLogTable.put(new Integer(265), integer);
        _parmLogTable.put(new Integer(266), integer);
        _parmLogTable.put(new Integer(267), integer);
        _parmLogTable.put(new Integer(268), integer);
        _parmLogTable.put(new Integer(269), integer4);
        _parmLogTable.put(new Integer(270), integer);
        _parmLogTable.put(new Integer(271), integer);
        _parmLogTable.put(new Integer(272), integer);
        _parmLogTable.put(new Integer(273), integer);
        _parmLogTable.put(new Integer(274), integer);
        _parmLogTable.put(new Integer(275), integer);
        _parmLogTable.put(new Integer(276), integer);
        _parmLogTable.put(new Integer(277), integer);
        _parmLogTable.put(new Integer(278), integer);
        _parmLogTable.put(new Integer(279), integer);
        _parmLogTable.put(new Integer(280), new Integer(32));
        _parmLogTable.put(new Integer(281), integer2);
        _parmLogTable.put(new Integer(282), integer2);
        _parmLogTable.put(new Integer(283), integer2);
        _parmLogTable.put(new Integer(284), integer2);
        _parmLogTable.put(new Integer(285), integer);
        _parmLogTable.put(new Integer(286), integer);
        _parmLogTable.put(new Integer(287), integer);
        _parmLogTable.put(new Integer(288), integer);
        _parmLogTable.put(new Integer(289), integer4);
        _parmLogTable.put(new Integer(290), integer4);
        _parmLogTable.put(new Integer(291), integer5);
        _parmLogTable.put(new Integer(292), integer5);
        _parmLogTable.put(new Integer(293), integer5);
        _parmLogTable.put(new Integer(294), integer5);
        _parmLogTable.put(new Integer(295), integer);
        _parmLogTable.put(new Integer(296), integer);
        _parmLogTable.put(new Integer(297), integer);
        _parmLogTable.put(new Integer(298), integer);
        _parmLogTable.put(new Integer(299), integer9);
        _parmLogTable.put(new Integer(300), integer4);
        _parmLogTable.put(new Integer(301), integer4);
        _parmLogTable.put(new Integer(302), integer5);
        _parmLogTable.put(new Integer(303), integer5);
        _parmLogTable.put(new Integer(304), integer5);
        _parmLogTable.put(new Integer(305), integer5);
        _parmLogTable.put(new Integer(306), integer5);
        _parmLogTable.put(new Integer(307), integer5);
        _parmLogTable.put(new Integer(308), integer5);
        _parmLogTable.put(new Integer(309), integer5);
        _parmLogTable.put(new Integer(310), integer4);
        _parmLogTable.put(new Integer(311), integer4);
        _parmLogTable.put(new Integer(312), integer5);
        _parmLogTable.put(new Integer(313), integer5);
        _parmLogTable.put(new Integer(314), integer5);
        _parmLogTable.put(new Integer(315), integer5);
        _parmLogTable.put(new Integer(316), integer5);
        _parmLogTable.put(new Integer(317), integer5);
        _parmLogTable.put(new Integer(318), integer5);
        _parmLogTable.put(new Integer(319), integer);
        _parmLogTable.put(new Integer(320), integer5);
        _parmLogTable.put(new Integer(321), integer5);
        _parmLogTable.put(new Integer(322), integer5);
        _parmLogTable.put(new Integer(323), integer5);
        _parmLogTable.put(new Integer(324), integer5);
        _parmLogTable.put(new Integer(325), integer5);
        _parmLogTable.put(new Integer(326), integer5);
        _parmLogTable.put(new Integer(327), integer3);
        _parmLogTable.put(new Integer(328), integer);
        _parmLogTable.put(new Integer(329), integer);
        _parmLogTable.put(new Integer(330), integer5);
        _parmLogTable.put(new Integer(331), integer);
        _parmLogTable.put(new Integer(332), integer4);
        _parmLogTable.put(new Integer(333), integer4);
        _parmLogTable.put(new Integer(334), integer4);
        _parmLogTable.put(new Integer(335), integer5);
        _parmLogTable.put(new Integer(336), integer5);
        _parmLogTable.put(new Integer(337), integer5);
        _parmLogTable.put(new Integer(338), integer5);
        _parmLogTable.put(new Integer(339), integer);
        _parmLogTable.put(new Integer(340), integer);
        _parmLogTable.put(new Integer(341), integer5);
        _parmLogTable.put(new Integer(342), integer5);
        _parmLogTable.put(new Integer(343), integer5);
        _parmLogTable.put(new Integer(344), integer5);
        _parmLogTable.put(new Integer(345), integer5);
        _parmLogTable.put(new Integer(346), integer5);
        _parmLogTable.put(new Integer(347), integer5);
        _parmLogTable.put(new Integer(348), integer5);
        _parmLogTable.put(new Integer(349), integer5);
        _parmLogTable.put(new Integer(350), integer5);
        _parmLogTable.put(new Integer(351), integer5);
        _parmLogTable.put(new Integer(352), integer5);
        _parmLogTable.put(new Integer(353), integer5);
        _parmLogTable.put(new Integer(354), integer5);
        _parmLogTable.put(new Integer(355), integer5);
        _parmLogTable.put(new Integer(356), integer5);
        _parmLogTable.put(new Integer(357), integer5);
        _parmLogTable.put(new Integer(358), integer5);
        _parmLogTable.put(new Integer(359), integer5);
        _parmLogTable.put(new Integer(360), integer);
        _parmLogTable.put(new Integer(361), integer);
        _parmLogTable.put(new Integer(362), integer);
        _parmLogTable.put(new Integer(363), new Integer(30));
        _parmLogTable.put(new Integer(364), new Integer(31));
        _parmLogTable.put(new Integer(365), integer);
        _parmLogTable.put(new Integer(366), new Integer(34));
        _parmLogTable.put(new Integer(367), new Integer(35));
        _parmLogTable.put(new Integer(368), new Integer(36));
        _parmLogTable.put(new Integer(369), new Integer(37));
        _parmLogTable.put(new Integer(370), new Integer(17));
        _parmLogTable.put(new Integer(371), new Integer(29));
        _parmLogTable.put(new Integer(372), integer4);
        _parmLogTable.put(new Integer(373), integer);
        _parmLogTable.put(new Integer(374), integer4);
        _parmLogTable.put(new Integer(375), integer4);
        _parmLogTable.put(new Integer(376), integer4);
    }
}
