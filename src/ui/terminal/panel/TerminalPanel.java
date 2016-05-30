package ui.terminal.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import process.ProcessManager;
import process.TerminalProcess;
import process.io.ProcessStreamSiphon;
import props.PropertyConstants;
import ui.log.LogPanel;
import ui.terminal.settings.TerminalWindowSettings;
import xml.XMLExpansion;
import xml.XMLValues;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 11:15:14 AM 
 */
public class TerminalPanel extends LogPanel implements ProcessStreamSiphon, XMLValues {
	
	private static final long serialVersionUID = 1L;
	
	private TerminalEntryArea entry = new TerminalEntryArea( this );
	
	private TerminalProcess p;
	
	public TerminalPanel( String name, String cwd ) {
		super( name );
		area.setFont( area.getFont().deriveFont( 10.0f ).deriveFont( Font.BOLD ) );
		area.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent e ) {
				entry.setFocus();
			}
		} );
		this.add( entry, BorderLayout.SOUTH );
		p = (TerminalProcess)ProcessManager.getInstance().getProcessByName( name );
		if ( p == null ) {
			p = new TerminalProcess( name );
		}
		if ( cwd != null ) {
			entry.setCWD( cwd );
			entry.forceCWD( cwd, name );
		}
		setColors();
	}
	
	public void setColors() {
		area.setBackground( windowSettings.getBackgroundColor() );
		area.setForeground( windowSettings.getForegroundColor() );
		entry.setColors();
	}

	@Override
	public void skimMessage( String name, String line ) {  //name ignored for this
		if ( line.contains( TerminalProcess.getOSSettings().getCWDDelim() ) ) {
			if ( !line.contains( "echo" ) ) {
				entry.setCWD( line.substring( TerminalProcess.getOSSettings().getCWDDelim().length() ) );
			}
		} else {
			boolean ignore = false;
			for ( String s : TerminalProcess.getOSSettings().getIgnore() ) {
				if ( line.contains( s ) ) {
					ignore = true;
					break;
				}
			}
			if ( !ignore ) {
				append( line + "\n" );
			}
		}
	}
	
	public TerminalWindowSettings getWindowSettings() {
		return windowSettings;
	}
	
	public String getProcessName() {
		return this.getName();
	}
	
	public void sendCommands( List<String> commands, long millis ) {
		for ( String s : commands ) {
			sendCommand( s );
			try {
				Thread.sleep( millis );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendCommand( String command ) {
		entry.sendCommand( command );
	}
	
	public String getCWD() {
		return entry.getCWD();
	}

	@Override
	public void notifyProcessStarted( String name ) {
		entry.processStarted( name );
		TerminalProcess.getOSSettings().getCWD();
	}

	public void notifyProcessEnded( String name ) {
		append( "\nProcess Terminated by User\n" );
	}

	@Override
	public List<XMLValues> getChildNodes() {
		List<XMLValues> ret = new ArrayList<XMLValues>();
		ret.add( windowSettings );
//		ret.add( TerminalWindowManager.getInstance().getHistory( p.getName() ) );
		return ret;
	}

	@Override
	public void loadParamsFromXMLValues( XMLExpansion e ) {
		XMLExpansion val = e.getChild( PropertyConstants.TERMINAL );
		for ( XMLValues v : getChildNodes() ) {
			v.loadParamsFromXMLValues( val );
		}
		setColors();
	}

	@Override
	public Map<String, Map<String, String[]>> saveParamsAsXML() {
		Map<String, Map<String, String[]>> ret = new HashMap<String, Map<String, String[]>>();
		ret.put( PropertyConstants.TERMINAL, null );
		return ret;
	}
}