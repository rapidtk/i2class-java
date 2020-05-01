package com.i2class;

import java.sql.SQLException;
import java.util.Vector;

import com.ibm.as400ad.webfacing.runtime.model.I2OptionIndicators;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.code400.dom.constants.*;

/**
 * A WebFaced subfile class.
 * 
 * @version 4/24/2002 1:15:43 PM
 */
public class RecordWebfaceSFL extends RecordWebface implements IRecordSFL
{

	Vector INRRN = new Vector();
	RecordSFL sfl_ = new RecordSFL();
	public int recno;
	//RecordWebfaceSFLCTL sflctl;


	public RecordWebfaceSFL(String fmtName)
	{
		super(fmtName);
	}
	
	/* Always return -1 because subfiles in webfacing have no fields (they are all contained in the SFLCTL format).
	 * @see com.asc.rio.RecordWebface#getFirstFieldLine()
	 */
	public int getFirstFieldLine() {
		return -1;
	}

	/** Get the changed values vector. 
	public Vector getChangedValues()
	{
		return sfl_.getChangedValues();
	}
	*/

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#addChangedValue(java.lang.Integer)
	 */
	public void addChangedValue(Integer rrn) {
		sfl_.addChangedRRN(rrn);
	}

	public int getReadcRRN()
	{
		return sfl_.getReadcRRN();
	}

	public int getRecno()
	{
		return sfl_.getRecno();
	}

	public int getCurrentSflSize()
	{
		return sfl_.getCurrentSflSize();
	}
	public int getTopRRN()
	{
		return sfl_.getTopRRN();
	}




	// Again, this shouldn't be here but Java inheritance...
	public void setRRN(int rrn)
	{
		int recno = getRecno();
		if (rrn != recno && recno > 0)
		{
			int maxrrn = rrn > recno ? rrn : recno;
			if (maxrrn > INRRN.size())
				INRRN.setSize(maxrrn);
			INRRN.setElementAt(IN, recno - 1);
			IN = (I2OptionIndicators) INRRN.elementAt(rrn - 1);
			if (IN == null)
				IN = new I2OptionIndicators(dataDef.getIndicatorDefinition());
		}
		fldValues=sfl_.setRRN_(rrn, fldValues, fldNames);
		this.recno = rrn;
	}

	public void setTopRRN(int rrn)
	{
		sfl_.setTopRRN_(rrn, (RfileWorkstn)file);
	}

	/** Clear subfile. */
	public void sflclr()
	{
		sfl_.sflclr();
	}

	/** Initialize subfile. */
	public void sflinz()
	{
		sfl_.sflinz();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RecordWebface#getJavascriptName(java.lang.String)
	 */
	protected String javascriptName(String fieldName) {
		return fieldName + "$1";
	}


	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#getSFLCTL()
	public IRecordSFLCTL getSFLCTL() {
		return sflctl;
	}
	 */

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#checkSFLNXTCHG()
	 */
	public void checkSFLNXTCHG() {
		KeywordDefinition kd = dataDef.getKeywordDefinition(ENUM_KeywordIdentifiers.KWD_SFLNXTCHG);
		if (kd != null)
		{
			String expression = kd.getIndicatorExpression();
			if (evaluateIndicatorExpression(expression))
				sfl_.addChangedSFLNXTCHG(ShortDecimal.newInteger(recno));
		}
		
		
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#getSubfileSize()
	 */
	public int getSubfileSize() {
		return sfl_.subfileSize;
	}
}
