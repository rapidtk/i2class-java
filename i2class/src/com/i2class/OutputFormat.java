package com.i2class;

/**
 * A output file/format pair.
 */
class OutputFormat
{
	RfilePrint file;
	RrecordPrint record;
	OutputFormat(RfilePrint file, RrecordPrint record)
	{
		this.file = file;
		this.record = record;
	}
}
