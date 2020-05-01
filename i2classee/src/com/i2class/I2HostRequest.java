package com.i2class;

import java.io.*;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.host.*;

/**
 * Intercept host request for job info.
 * 
 * 
 */
public class I2HostRequest implements IHostRequest {

	/**
	 * Handle host request for data.
	 * @see com.ibm.as400ad.webfacing.runtime.host.IHostRequest#request(com.ibm.as400ad.webfacing.runtime.host.IWFInputBuffer)
	 */
	public InputStream request(IWFInputBuffer inBuf) throws IOException, WebfacingInternalException 
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(inBuf.length());
		inBuf.toStream(byteStream);
		OffsetDataInputStream dataStream = new OffsetDataInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
		// Length of all data
		int length = dataStream.readIntOffset();
		// Number of requests
		int requestCount = dataStream.readIntOffset();
		// Array of offsets to each request
		int offsets[] = new int[requestCount];
		String responses[] = new String[requestCount];
		for (int i=0; i<requestCount; i++)
		{
			offsets[i]=dataStream.readIntOffset();
		}
		// Loop through requests and actually retrieve data
		for (int i=0; i<requestCount; i++)
		{
			//dataStream.skip(offsets[i]-offset);
			int requestType = dataStream.readIntOffset();
			// Skip next two bytes (offset to next value)
			dataStream.readIntOffset();
			// Build response
			String response="";
			switch (requestType)
			{
				case IHJIRequestType.HJI_REQ_JOB_DATFMT:
					response = "MDY";
					break;
				case IHJIRequestType.HJI_REQ_JOB_DATSEP:
					response = "/";
					break;
				case IHJIRequestType.HJI_REQ_JOB_DECFMT:
					response = " ";
					break;
				case IHJIRequestType.HJI_REQ_JOB_TIMFMT:
					response = "HMS";
					break;
				case IHJIRequestType.HJI_REQ_JOB_TIMSEP:
					response = ":";
					break;
			}
			responses[i]=response;
		}
		// Build output stream (which we have to return as an input stream for some reason?!?)
		byteStream.reset();
		DataOutputStream outputStream = new DataOutputStream(byteStream);
		outputStream.writeInt(0); //???
		outputStream.writeInt(0); // total length of data???
		outputStream.writeInt(requestCount);
		int offset=12 + requestCount*4; // 12 = sizeof(int)*3 + sizeof(offsetArray)
		for (int i=0; i<requestCount; i++)
		{
			outputStream.writeInt(offset);
			// Calculate length of entry - int error, int ?, int length (in bytes) + actual data (in bytes)
			offset += 12 + responses[i].length()*2;
		}
		// Write out responses
		for (int i=0; i<requestCount; i++)
		{
			outputStream.writeInt(0); // error
			outputStream.writeInt(0); // ???
			outputStream.writeInt(responses[i].length()*2); // length of data (in bytes)
			outputStream.writeChars(responses[i]);
		}
		// Point data stream to new byte array
		dataStream = new OffsetDataInputStream(new ByteArrayInputStream(byteStream.toByteArray())); 
		return dataStream;
	}

}
