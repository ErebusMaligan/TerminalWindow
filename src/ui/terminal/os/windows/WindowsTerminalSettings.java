package ui.terminal.os.windows;

import process.io.ui.UIStreamSiphon;
import ui.terminal.os.OSTerminalSettings;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 2:02:43 PM 
 */
public class WindowsTerminalSettings implements OSTerminalSettings {

	@Override
	public String getCWD() {
		return getEcho() + " " + getCWDDelim() + "%CD%";
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
		return ">";
	}

	@Override
	public String getTerminalLaunchCommand() {
		return "cmd";
	}
	
	@Override
	public  String getPreCommand( String[] input ) {
		return null;
	}
	
	@Override 
	public String[] getIgnore() {
		return new String[] { getPromptChar() + "cd", UIStreamSiphon.UI_TOKEN };
	}

	@Override
	public String getClear() {
		return "cls";
	}
	
	@Override 
	public String getDirListing() {
		return "dir";
	}

	@Override
	public String issueCDCommand( String dir ) {
		return "cd /D" + "\"" +  dir + "\"";  //i think using /D always will work whether the drive letter is a change from current working directory or not
	}
	
	
}