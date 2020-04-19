/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * RCLRSC (Reclaim resources) processing.
 * 
 */
public class Rclrsc extends AbstractCommand {

	private String m_lvl="*";
	
	public Rclrsc() {}
	public Rclrsc(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LVL"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setLvl(String lvl) {
		m_lvl = Application.trimr(lvl);
	}
	public void setLvl(FixedChar lvl) {
		setLvl(lvl.toString());
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		Application app, rclapp=null;
		app=getApp();
		// If LVL(*CALLER) is specified, then reclaim resources for previous object in stack
		if (m_lvl.equals("*CALLER"))
			rclapp=app.prvApp();
		// If LVL(*) is specified (or if *CALLER is null), then reclaim resources for this object
		if (rclapp==null)
			rclapp=getApp();
		rclapp.rclrsc();
	}
}
