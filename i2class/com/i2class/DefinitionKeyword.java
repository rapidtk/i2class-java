package com.i2class;

/**
 * A keyword definition (for example SFLCLR).
 * 
 */
class DefinitionKeyword {
	int keyword;
	String indicatorExpression;

	DefinitionKeyword(int keyword, String indicatorExpression)
	{
		this.keyword=keyword;
		this.indicatorExpression = indicatorExpression;
	}
}
