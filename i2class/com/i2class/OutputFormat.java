package com.i2class;

/**
 * A output file/format pair.
 */
class OutputFormat
{
	RfilePrinter file;
	RecordPrinter record;
	OutputFormat(RfilePrinter file, RecordPrinter record)
	{
		this.file = file;
		this.record = record;
	}
}
