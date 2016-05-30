package ui.terminal.panel;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import ui.terminal.history.EntryHistory;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 3:48:26 PM 
 */
public class TerminalWindowManager {
	
	public int OS;
	
	private Map<String, TerminalWindow> windows = new HashMap<String, TerminalWindow>();
	
	private Map<String, EntryHistory> history = new HashMap<String, EntryHistory>();
	
	private static TerminalWindowManager instance;
	
	private TerminalWindowManager() {
	}
	
	public TerminalWindow createTerminal( String name, Dimension size ) {
		return init( name, size, null );
	}
	
	public TerminalWindow createTerminal( String name, Dimension size, String cwd ) {
		return init( name, size, cwd );
	}
	
	public static TerminalWindowManager getInstance() {
		if ( instance == null ) {
			instance = new TerminalWindowManager();
		}
		return instance;
	}
	
	private TerminalWindow init( String name, Dimension size, String cwd ) {
		TerminalWindow ret = new TerminalWindow( name, size, this, cwd );
		if ( !windows.containsKey( name ) ) {
			windows.put( name, ret );
		} else {
			ret = null;
		}
		if ( !history.containsKey( name ) ) {
			history.put( name, new EntryHistory() );
		}
		return ret;
	}
	
	public synchronized EntryHistory getHistory( String name ) {
		if ( history.get( name ) == null ) {
			history.put( name, new EntryHistory() );
		}
		return history.get( name );
	}
	
	/**
	 * @author Daniel J. Rivers
	 *         2013
	 *
	 * Created: Aug 20, 2013, 10:27:15 AM 
	 */
	private class TerminalWindow extends JFrame {

		private static final long serialVersionUID = 1L;

		private TerminalPanel p;
		
		public TerminalWindow( String title, Dimension size, TerminalWindowManager manager, String cwd ) {
			super( title );
			init( title, size, manager, cwd );
		}
		
		private void init( String name, Dimension size, TerminalWindowManager manager, String cwd ) {
			this.setSize( size );
			p = new TerminalPanel( name, cwd );
			this.add( p );
			this.setDefaultCloseOperation( EXIT_ON_CLOSE );
			this.setVisible( true );
		}
	}
}