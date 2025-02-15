package ui.terminal.panel;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import process.ProcessManager;
import process.TerminalProcess;
import ui.terminal.history.EntryHistory;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 2:09:29 PM 
 */
public class TerminalEntryArea extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	private JTextField text = new JTextField();
	private JLabel cwdLbl = new JLabel();
	private String cwd;
	private JPanel lblPanel;

	private TerminalPanel parent;
	
	public TerminalEntryArea( TerminalPanel parent ) {
		this.parent = parent;
		cwdLbl.setOpaque( true );
		cwdLbl.setHorizontalAlignment( SwingConstants.LEFT );
		this.setLayout( new BorderLayout() );
		lblPanel = new JPanel( new BorderLayout() );
		lblPanel.add( cwdLbl );
		this.add( lblPanel, BorderLayout.NORTH );
		this.add( text, BorderLayout.CENTER );
		text.addKeyListener( this );
		text.setFont( text.getFont().deriveFont( 14.0f ) );
		cwdLbl.setFont( cwdLbl.getFont().deriveFont( 10.0f ) );
		setColors();
	}
	
	public void setColors() {
		text.setBorder( BorderFactory.createLineBorder( parent.getWindowSettings().getForegroundColor() ) );
		cwdLbl.setForeground( parent.getWindowSettings().getForegroundColor() );
		cwdLbl.setBackground( parent.getWindowSettings().getBackgroundColor() );
		text.setCaretColor( parent.getWindowSettings().getForegroundColor() );
		text.setBackground( parent.getWindowSettings().getBackgroundColor() );
		lblPanel.setBackground( parent.getWindowSettings().getBackgroundColor() );
	}
	
	public String getCWD() {
		return cwd;
	}
	
	public void setFocus() {
		text.requestFocus();
	}
	
	public void processStarted( String name ) {
		if ( cwd == null ) {
			ProcessManager.getInstance().getProcessByName( name ).sendCommand( TerminalProcess.getOSSettings().getCWD() );
		} else {
			forceCWD( cwd, name );
		}
	}
	
	private void requestCWD( String name ) {
		ProcessManager.getInstance().getProcessByName( name ).sendCommand( TerminalProcess.getOSSettings().getCWD() );
	}
	
	public void forceCWD( String cwd, String name ) {
		ProcessManager.getInstance().getProcessByName( name ).sendCommand( TerminalProcess.getOSSettings().issueCDCommand( cwd ) );
		requestCWD( name );

	}
	
	public void setCWD( String s ) {
		cwd = s;
		cwdLbl.setText( s + TerminalProcess.getOSSettings().getPromptChar() );
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		if ( ( e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK ) != 0 ) {
			if ( e.getKeyCode() == KeyEvent.VK_C ) {
				try {
					TerminalProcess p = ( (TerminalProcess)ProcessManager.getInstance().getProcessByName( parent.getProcessName() ) ); //needs to be stored locally otherwise restart will fail because process is no longer known by manager
					p.closeResources();
					p.restartProcess();
				} catch ( IOException e1 ) {
					e1.printStackTrace();
				}
			}
		} else {
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				String command = text.getText();
				sendCommand( command );
			} else if ( e.getKeyCode() == KeyEvent.VK_UP ) {
				EntryHistory history = TerminalWindowManager.getInstance().getHistory( parent.getProcessName() );
				if ( history != null ) {
					String t = history.up();
					if ( t != null ) {
						text.setText( t );
					}
				}
			} else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
				EntryHistory history = TerminalWindowManager.getInstance().getHistory( parent.getProcessName() );
				if ( history != null ) {
					String t = history.down();
					if ( t != null ) {
						text.setText( t );
					}
				}
			}
		} 
	}
	
	public void sendCommand( String command ) {
		if ( command.equals( TerminalProcess.getOSSettings().getClear() ) ) {
			parent.clear();
		} else {
			String pre = TerminalProcess.getOSSettings().getPreCommand( new String[] { cwd, command } );
			parent.addBlankLine();
			if ( pre != null ) {
				ProcessManager.getInstance().getProcessByName( parent.getProcessName() ).sendCommand( pre );
				try {
					Thread.sleep( 50 );
				} catch ( InterruptedException e1 ) {
					e1.printStackTrace();
				}
				parent.addBlankLine();
			}
		}
		EntryHistory history = TerminalWindowManager.getInstance().getHistory( parent.getProcessName() );
		if ( history != null && !command.trim().equals( "" ) ) {
			history.append( command );
		}
		ProcessManager.getInstance().getProcessByName( parent.getProcessName() ).sendCommand( command );
		text.setText( "" );
		if ( command.startsWith( "cd" ) ) {
			requestCWD( parent.getProcessName() );
		}
	}
	
	@Override
	public void keyReleased( KeyEvent arg0 ) {}
	
	@Override
	public void keyTyped( KeyEvent arg0 ) {}
}