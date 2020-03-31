/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CALL processing.
 * Used only through QCMDEXC
 * @author ANDREWC
 */
public class Call extends CALL {

	public Call() {}
	public Call(I2Connection rconn) {
		super(rconn);
		// TODO Auto-generated constructor stub
	}
}
