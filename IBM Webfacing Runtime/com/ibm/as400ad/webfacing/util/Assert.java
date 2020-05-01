// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;


// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            AssertionFailedError

public class Assert
{

    protected Assert()
    {
    }

    /*
    public static void assert(String s, boolean flag)
    {
        if(!flag)
            fail(s);
    }

    public static void assert(boolean flag)
    {
        assert(null, flag);
    }
    */

    public static void assertTrue(String s, boolean flag)
    {
        if(!flag)
            fail(s);
    }

    public static void assertTrue(boolean flag)
    {
        if(!flag)
            fail();
    }

    public static void fail(String s)
    {
        throw new AssertionFailedError(s);
    }

    public static void fail()
    {
        fail(null);
    }

    public static void assertEquals(String s, Object obj, Object obj1)
    {
        if(obj == null && obj1 == null)
            return;
        if(obj != null && obj.equals(obj1))
        {
            return;
        } else
        {
            failNotEquals(s, obj, obj1);
            return;
        }
    }

    public static void assertEquals(Object obj, Object obj1)
    {
        assertEquals(((String) (null)), obj, obj1);
    }

    public static void assertEquals(String s, double d, double d1, double d2)
    {
        if(Math.abs(d - d1) > d2)
            failNotEquals(s, new Double(d), new Double(d1));
    }

    public static void assertEquals(double d, double d1, double d2)
    {
        assertEquals(null, d, d1, d2);
    }

    public static void assertEquals(String s, float f, float f1, float f2)
    {
        if(Math.abs(f - f1) > f2)
            failNotEquals(s, new Float(f), new Float(f1));
    }

    public static void assertEquals(float f, float f1, float f2)
    {
        assertEquals(null, f, f1, f2);
    }

    public static void assertEquals(String s, long l, long l1)
    {
        assertEquals(s, new Long(l), new Long(l1));
    }

    public static void assertEquals(long l, long l1)
    {
        assertEquals(((String) (null)), l, l1);
    }

    public static void assertEquals(String s, boolean flag, boolean flag1)
    {
        assertEquals(s, new Boolean(flag), new Boolean(flag1));
    }

    public static void assertEquals(boolean flag, boolean flag1)
    {
        assertEquals(((String) (null)), flag, flag1);
    }

    public static void assertEquals(String s, byte byte0, byte byte1)
    {
        assertEquals(s, new Byte(byte0), new Byte(byte1));
    }

    public static void assertEquals(byte byte0, byte byte1)
    {
        assertEquals(((String) (null)), byte0, byte1);
    }

    public static void assertEquals(String s, char c, char c1)
    {
        assertEquals(s, new Character(c), new Character(c1));
    }

    public static void assertEquals(char c, char c1)
    {
        assertEquals(((String) (null)), c, c1);
    }

    public static void assertEquals(String s, short word0, short word1)
    {
        assertEquals(s, new Short(word0), new Short(word1));
    }

    public static void assertEquals(short word0, short word1)
    {
        assertEquals(((String) (null)), word0, word1);
    }

    public static void assertEquals(String s, int i, int j)
    {
        assertEquals(s, new Integer(i), new Integer(j));
    }

    public static void assertEquals(int i, int j)
    {
        assertEquals(((String) (null)), i, j);
    }

    public static void assertNotNull(Object obj)
    {
        assertNotNull(null, obj);
    }

    public static void assertNotNull(String s, Object obj)
    {
        assertTrue(s, obj != null);
    }

    public static void assertNull(Object obj)
    {
        assertNull(null, obj);
    }

    public static void assertNull(String s, Object obj)
    {
        assertTrue(s, obj == null);
    }

    public static void assertSame(String s, Object obj, Object obj1)
    {
        if(obj == obj1)
        {
            return;
        } else
        {
            failNotSame(s, obj, obj1);
            return;
        }
    }

    public static void assertSame(Object obj, Object obj1)
    {
        assertSame(null, obj, obj1);
    }

    private static void failNotEquals(String s, Object obj, Object obj1)
    {
        String s1 = "";
        if(s != null)
            s1 = s + " ";
        fail(s1 + "expected:<" + obj + "> but was:<" + obj1 + ">");
    }

    private static void failNotSame(String s, Object obj, Object obj1)
    {
        String s1 = "";
        if(s != null)
            s1 = s + " ";
        fail(s1 + "expected same");
    }

    public static final boolean ENABLED = false;
}
