/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.util.StringTokenizer;

/**
 * Scan for String Pattern (QCLSCAN) API
 * 
 */
public class QCLSCAN extends Application {
	
	public QCLSCAN(Application app) throws Exception {
	   super(app);
	}
	
	public void call(FixedChar input, PackedDecimal inputLength, PackedDecimal startPosition, FixedChar pattern, PackedDecimal patternLength,
	 FixedChar translate, FixedChar trim, FixedChar wildCard, PackedDecimal result) throws Exception {
	 	int ilength = inputLength.intValue();
		if (ilength!=input.size())
			input = subst(input, 1, ilength);
		String inputString=input.toString();
		// Translate to upper case, if specified
		if (translate.charAt(0)=='1')
			inputString = inputString.toUpperCase();

		int slength = patternLength.intValue();
		if (slength != pattern.len())
			pattern=subst(pattern,1, slength);
		// Trim trailing blanks from scan string, if specified
		String scanString;
		if (trim.charAt(0)=='1')
			scanString = pattern.trimr();
		else
			scanString = pattern.toString();

		// Each non-wild character string is a token
		int i=0;
		int index=0;
		String wildString = wildCard.toString();
	noMatch:
		do 
		{
			StringTokenizer st = new StringTokenizer(scanString, wildString, true);
			String firstString=st.nextToken();
			int f=inputString.indexOf(firstString, i);
			if (f<0)
				break;
			i=f+1;
			// If the scan string has a wild-card character(s) in it, then check to make sure that there is a match
			while (st.hasMoreTokens())
			{
				String token = st.nextToken();
				int tokenLength = token.length();
				if (token.compareTo(wildString)!=0 && inputString.substring(i, i+tokenLength).compareTo(token)!=0)
					continue noMatch;
				i += tokenLength;
			}
			// If we get here then the strings have matched
			index=f+1;
			break;
		} while(true);
		
		result.assign(index);
	}

}
