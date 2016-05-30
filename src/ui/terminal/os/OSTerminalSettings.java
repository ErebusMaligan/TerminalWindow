package ui.terminal.os;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 2:05:06 PM 
 */
public interface OSTerminalSettings {
	
	public static int WINDOWS = 2;
	
	public static int LINUX = 1;
	
	public String getCWD();
	
	public String getCWDDelim();
	
	public String getEcho();
	
	public String getPromptChar();
	
	public String getTerminalLaunchCommand();
	
	public String getClear();
	
	public String getPreCommand( String[] input );
	
	public String[] getIgnore();
	
	public String getDirListing();
	
	public String issueCDCommand( String dir );
}
