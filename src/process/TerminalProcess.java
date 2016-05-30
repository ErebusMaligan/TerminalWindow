package process;

import ui.terminal.os.OSTerminalSettings;
import ui.terminal.os.linux.LinuxTerminalSettings;
import ui.terminal.os.windows.WindowsTerminalSettings;
import ui.terminal.panel.TerminalWindowManager;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 22, 2013, 12:03:49 PM 
 */
public class TerminalProcess extends StandardProcess {
	
	private static OSTerminalSettings osSettings = TerminalWindowManager.getInstance().OS == OSTerminalSettings.WINDOWS ? new WindowsTerminalSettings() : new LinuxTerminalSettings();
	
	public TerminalProcess( String name ) {
		super( name, osSettings.getTerminalLaunchCommand() );
	}
	
	public static OSTerminalSettings getOSSettings() {
		return osSettings;
	}
}
