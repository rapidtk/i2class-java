// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;

import java.util.HashMap;

// Referenced classes of package com.ibm.as400ad.code400.dom.constants:
//            IKeywordIdentifiers, ENUM_KeywordIdentifierStrings

public class KeywordIdentifier
    implements IKeywordIdentifiers
{

    public static Long keywordID(String s)
    {
        if(_keywordIdentifiers == null)
        {
            _keywordIdentifiers = new HashMap();
            for(int i = 0; i < ENUM_KeywordIdentifierStrings.TOKEN_STRINGS.length; i++)
                _keywordIdentifiers.put(ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[i], new Long(i));

        }
        Long long1 = (Long)_keywordIdentifiers.get(s);
        if(long1 == null)
            long1 = new Long(0L);
        return long1;
    }

    public static final int MAP(int i)
    {
        return kwdMapping[i];
    }

    public static final int MAPDSPF(int i)
    {
        return kwdIDMapping[0][i];
    }

    public static final int MAPPRTF(int i)
    {
        return kwdIDMapping[1][i];
    }

    public static final int MAPPF(int i)
    {
        return kwdIDMapping[2][i];
    }

    public static final int MAPLF(int i)
    {
        return kwdIDMapping[3][i];
    }

    public static final int MAPICF(int i)
    {
        return kwdIDMapping[4][i];
    }

    protected KeywordIdentifier(int i)
    {
        _code = i;
    }

    private int getCode()
    {
        return _code;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof KeywordIdentifier)
            return getCode() == ((KeywordIdentifier)obj).getCode();
        else
            return false;
    }

    public boolean isDSPF()
    {
        return 0 < getCode() && getCode() < 229;
    }

    public String toString()
    {
        return ENUM_KeywordIdentifierStrings.TOKEN_STRINGS[_code];
    }

    private int _code;
    private static HashMap _keywordIdentifiers = null;
    public static final int kwdMapping[] = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
        10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 
        30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 
        40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 
        50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 
        60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 
        70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 
        80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 
        90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 
        100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 
        110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 
        120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 
        130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 
        140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 
        150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 
        160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 
        170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 
        180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 
        190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 
        200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 
        210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 
        220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 
        230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 
        240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 
        250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 
        260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 
        270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 
        280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 
        290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 
        300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 
        310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 
        320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 
        330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 
        340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 
        350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 
        360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 
        370, 371, 372, 373, 374, 375, 376, 377, 378, 2, 
        380, 381, 382, 68, 70, 75, 386, 78, 82, 83, 
        390, 84, 392, 93, 95, 395, 396, 104, 398, 399, 
        400, 165, 166, 167, 404, 405, 212, 214, 215, 409, 
        410, 222, 412, 413, 228, 415, 416, 417, 418, 419, 
        420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 
        430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 
        440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 
        450, 451, 452, 453, 454, 455, 456, 457, 2, 459, 
        460, 68, 70, 75, 464, 78, 466, 82, 83, 469, 
        470, 471, 93, 95, 474, 475, 104, 477, 478, 479, 
        480, 481, 482, 483, 484, 485, 486, 165, 488, 489, 
        490, 491, 492, 212, 214, 215, 496, 497, 498, 222, 
        500, 501, 228, 503, 504, 505, 506, 2, 508, 13, 
        510, 511, 72, 513, 77, 515, 516, 81, 82, 83, 
        520, 521, 84, 87, 524, 93, 95, 527, 103, 104, 
        530, 531, 532, 533, 124, 535, 536, 537, 126, 127, 
        540, 541, 147, 152, 544, 545, 546, 547, 548, 166, 
        167, 551, 552, 553, 554, 212, 213, 214, 215, 559, 
        560, 561, 228, 563, 564, 565, 566, 567, 568, 569, 
        570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 
        580, 581, 582, 583, 584, 585, 586, 587, 588, 589, 
        590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 
        600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 
        610, 611, 612, 613, 2, 615, 616, 617, 618, 619, 
        620, 621, 622, 623, 104, 625, 626, 105, 126, 127, 
        128, 631, 632, 633, 634, 635, 636, 637, 638, 639, 
        640, 166, 167, 643, 644, 645, 646, 647, 212, 649, 
        650, 228, 652, 653, 654, 655, 656, 657, 658, 659, 
        660
    };
    public static final int kwdIDMapping[][] = {
        {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 
            30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 
            40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 
            50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 
            60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 
            70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 
            80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 
            90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 
            100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 
            110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 
            120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 
            130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 
            140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 
            150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 
            160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 
            170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 
            180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 
            190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 
            200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 
            210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 
            220, 221, 222, 223, 224, 225, 226, 227, 228, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 
            0, 0, 0, 68, 70, 75, 0, 78, 82, 83, 
            0, 84, 0, 93, 95, 0, 0, 104, 0, 0, 
            0, 165, 166, 167, 0, 0, 212, 214, 215, 0, 
            0, 222, 0, 0, 228, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 
            0, 68, 70, 75, 0, 78, 0, 82, 83, 0, 
            0, 0, 93, 95, 0, 0, 104, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 165, 0, 0, 
            0, 0, 0, 212, 214, 215, 0, 0, 0, 222, 
            0, 0, 228, 0, 0, 0, 0, 2, 0, 13, 
            0, 0, 72, 0, 77, 0, 0, 81, 82, 83, 
            0, 0, 84, 87, 0, 93, 95, 0, 103, 104, 
            0, 0, 0, 0, 124, 0, 0, 0, 126, 127, 
            0, 0, 147, 152, 0, 0, 0, 0, 0, 166, 
            167, 0, 0, 0, 0, 212, 213, 214, 215, 0, 
            0, 0, 228, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 104, 0, 0, 105, 126, 127, 
            128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 166, 167, 0, 0, 0, 0, 0, 212, 0, 
            0, 228, 0, 0, 0, 0, 0, 0, 0, 0, 
            0
        }, {
            0, 0, 507, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 509, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 512, 0, 0, 0, 0, 514, 0, 0, 
            0, 517, 518, 519, 522, 0, 0, 523, 0, 0, 
            0, 0, 0, 525, 0, 526, 0, 0, 0, 0, 
            0, 0, 0, 528, 529, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 534, 0, 538, 539, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 542, 0, 0, 
            0, 0, 543, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 549, 550, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 555, 556, 557, 558, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 562, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 507, 
            0, 0, 0, 0, 0, 0, 0, 0, 518, 519, 
            0, 522, 0, 525, 526, 0, 0, 529, 0, 0, 
            0, 0, 549, 550, 0, 0, 555, 557, 558, 0, 
            0, 0, 0, 0, 562, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 507, 0, 
            0, 0, 0, 0, 0, 0, 0, 518, 519, 0, 
            0, 0, 525, 526, 0, 0, 529, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 555, 557, 558, 0, 0, 0, 0, 
            0, 0, 562, 0, 0, 0, 0, 507, 508, 509, 
            510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 
            520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 
            530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 
            540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 
            550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 
            560, 561, 562, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 507, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 529, 0, 0, 0, 538, 539, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 549, 550, 0, 0, 0, 0, 0, 555, 0, 
            0, 562, 0, 0, 0, 0, 0, 0, 0, 0, 
            0
        }, {
            0, 0, 379, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 383, 0, 
            384, 0, 0, 0, 0, 385, 0, 0, 387, 0, 
            0, 0, 388, 389, 391, 0, 0, 0, 0, 0, 
            0, 0, 0, 393, 0, 394, 0, 0, 0, 0, 
            0, 0, 0, 0, 397, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 401, 402, 403, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 406, 0, 407, 408, 0, 0, 0, 0, 
            0, 0, 411, 0, 0, 0, 0, 0, 414, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 378, 379, 
            380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 
            390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 
            400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 
            410, 411, 412, 413, 414, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 378, 379, 0, 
            380, 383, 384, 385, 386, 387, 0, 388, 389, 390, 
            392, 0, 393, 394, 395, 396, 397, 398, 0, 0, 
            0, 0, 0, 0, 399, 400, 0, 401, 0, 404, 
            0, 405, 0, 406, 407, 408, 0, 409, 410, 411, 
            412, 413, 414, 0, 0, 0, 0, 379, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 388, 389, 
            0, 0, 391, 0, 0, 393, 394, 0, 0, 397, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 402, 
            403, 0, 0, 0, 0, 406, 0, 407, 408, 0, 
            0, 0, 414, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 379, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 397, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 402, 403, 0, 0, 0, 0, 0, 406, 0, 
            412, 414, 0, 0, 0, 0, 0, 0, 0, 0, 
            0
        }, {
            0, 0, 458, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 461, 0, 
            462, 0, 0, 0, 0, 463, 0, 0, 465, 0, 
            0, 0, 467, 468, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 472, 0, 473, 0, 0, 0, 0, 
            0, 0, 0, 0, 476, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 487, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 493, 0, 494, 495, 0, 0, 0, 0, 
            0, 0, 499, 0, 0, 0, 0, 0, 502, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 457, 458, 
            460, 0, 0, 461, 462, 463, 464, 465, 467, 468, 
            469, 0, 470, 472, 473, 474, 475, 476, 477, 484, 
            485, 487, 0, 0, 489, 491, 493, 494, 495, 497, 
            498, 499, 500, 501, 502, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 457, 458, 459, 
            460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 
            470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 
            480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 
            490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 
            500, 501, 502, 0, 0, 0, 0, 458, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 467, 468, 
            0, 0, 0, 0, 0, 472, 473, 0, 0, 476, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 493, 0, 494, 495, 0, 
            0, 0, 502, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 458, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 476, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 493, 0, 
            500, 502, 0, 0, 0, 0, 0, 0, 0, 0, 
            0
        }, {
            0, 0, 614, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 624, 627, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 628, 629, 630, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 641, 642, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 648, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 651, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 614, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 624, 0, 0, 
            0, 0, 641, 642, 0, 0, 648, 0, 0, 0, 
            0, 0, 650, 0, 651, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 614, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 624, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 648, 0, 0, 0, 0, 0, 0, 
            650, 0, 651, 0, 0, 0, 0, 614, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 624, 
            0, 0, 0, 0, 0, 0, 0, 0, 628, 629, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 641, 
            642, 0, 0, 0, 0, 648, 0, 0, 0, 0, 
            0, 0, 651, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 614, 615, 616, 617, 618, 619, 
            620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 
            630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 
            640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 
            650, 651, 0, 0, 0, 0, 0, 0, 0, 0, 
            0
        }
    };

}
