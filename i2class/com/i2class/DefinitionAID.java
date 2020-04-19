package com.i2class;

/**
 * An AID class for things like CF03 and PAGEDOWN.
 * 
 */
class DefinitionAID extends DefinitionKeyword {
	int responseIndicator;
	DefinitionAID(int AID, int responseIndicator, String indicatorExpression)
	{
		super(AID, indicatorExpression);
		this.responseIndicator = responseIndicator;
	}
}
