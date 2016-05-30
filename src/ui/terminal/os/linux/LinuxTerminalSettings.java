package ui.terminal.os.linux;

import process.io.ui.UIStreamSiphon;
import ui.terminal.os.OSTerminalSettings;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 2:07:27 PM 
 */
public class LinuxTerminalSettings implements OSTerminalSettings {
	
	@Override
	public String getCWD() {
		return getEcho() + " " + getCWDDelim() + "$(pwd)";
	}
	
	@Override
	public String getCWDDelim() {
		return "~CWD~";
	}

	@Override
	public String getEcho() {
		return "echo";
	}

	@Override
	public String getPromptChar() {
		return " ~ $";
	}
	
	@Override
	public String getTerminalLaunchCommand() {
		return "/bin/bash";
	}
	
	@Override
	public String getPreCommand( String[] input ) {
		return getEcho() + " \"" + input[ 0 ] + getPromptChar() + "\" " + "\"" + input[ 1 ] + "\""; 
	}
	
	@Override 
	public String[] getIgnore() {
		return new String[] { UIStreamSiphon.UI_TOKEN, "<STAT>" };
	}

	@Override
	public String getClear() {
		return "clear";
	}
	
	@Override 
	public String getDirListing() {
		return "ls";
	}
	
	@Override
	public String issueCDCommand( String dir ) {
		return "cd " + "\"" +  dir + "\"";
	}
}