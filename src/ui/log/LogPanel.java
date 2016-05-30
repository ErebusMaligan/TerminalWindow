package ui.log;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import process.ProcessManager;
import process.io.ProcessStreamSiphon;
import ui.terminal.settings.TerminalWindowSettings;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 11:15:14 AM 
 */
public class LogPanel extends JPanel implements ProcessStreamSiphon {
	
	private static final long serialVersionUID = 1L;

	protected JTextArea area = new JTextArea();
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SSS");
	
	protected BlockingQueue<String> queue;
	
	protected TerminalWindowSettings windowSettings = new TerminalWindowSettings();
	
	protected BufferedWriter logOut;
	
	protected File logFile;
	
	protected LineLimitListener lll = new LineLimitListener( windowSettings.getLineLimit() );
	
	protected ProcessStreamSiphon skimReplace;
	
	protected Thread runner;
	
	protected boolean kill = false;
	
	protected JScrollPane scroll;
	
	public LogPanel( String name ) {
		this.setName( name );
		area.setEditable( false );
		area.setLineWrap( true );
		area.setFont( new Font( area.getFont().getName(), area.getFont().getStyle(), 12 ) );
		area.getDocument().addDocumentListener( lll );
		this.setLayout( new BorderLayout() );
		scroll = new JScrollPane( area );
		this.add( scroll, BorderLayout.CENTER );
		queue = new LinkedBlockingQueue<String>();
		runner = new Thread() {
			public void run() {
				try {
					while( !kill ) {
//						while ( !queue.isEmpty() ) {
//							if ( queue.peek() == null ) {
							String content = queue.take();
							area.append( content );
							if ( logOut != null ) {
								try {
									logOut.write( content );
									logOut.flush();
								} catch ( IOException e ) {
									e.printStackTrace();
								}
							}
							SwingUtilities.invokeLater( new Runnable() { 
								public void run() {
									try {
										area.setCaretPosition( area.getLineEndOffset( area.getLineCount() - 1 ) );
									} catch ( Exception e ) {
//										e.printStackTrace();  Swallow this error, because it doesn't really break anything it just doesn't work
									}
								} 
							} );
//						}
//						}
//						Thread.sleep( 5 );
//						SwingUtilities.invokeLater( new Runnable() { 
//							public void run() {
//								try {
//									area.setCaretPosition( area.getLineEndOffset( area.getLineCount() - 1 ) );
//								} catch ( BadLocationException e ) {
//									e.printStackTrace();
//								}
//							} 
//						} );
					}					
					//		pane.getVerticalScrollBar().setValue( pane.getVerticalScrollBar().getMaximum() );
				} catch ( InterruptedException e ) {
					//IF THIS HAPPENS:  it's probably at shutdown, and i don't care, so don't print errors about it
				}
			}
		};
		runner.start();
		area.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent e ) {
				if ( e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1 ) {
					JPopupMenu menu = new LogMenu( LogPanel.this );
					menu.show( area, e.getX(), e.getY() );
				}
			}
		} );
		ProcessManager.getInstance().registerSiphon( name, this );
	}
	
	public void shutDown() {
		kill = true;
	}
	
	public JScrollPane getScrollPane() {
		return scroll;
	}
	
	public void setSkimReplace( ProcessStreamSiphon skimReplace ) {
		this.skimReplace = skimReplace;
	}

	/**
	 * Appends the given string to the text area.
	 * 
	 * @param str String to append.
	 */
	public void append( String str ) {
		queue.add( str );
	}
	
	public void setLineLimit() {
		area.getDocument().removeDocumentListener( lll );
		lll = new LineLimitListener( windowSettings.getLineLimit() );
		area.getDocument().addDocumentListener( lll );
	}
	
	public void skimMessage( String name, String line ) {  //name ignored for this
		if ( skimReplace != null ) {
			skimReplace.skimMessage( name, line );
		} else {
			if ( line.endsWith( "%" ) ) {
				append( line );
			} else {
				appendWithTime( line + "\n" );
			}
		}
	}
	
	public void appendWithTime( String str ) {
		append( "[" + sdf.format( new Date( System.currentTimeMillis() ) ) + "]:  " + str );
	}

	public void addBlankLine() {
		append( "\n" );
	}
	
	public void clear() {
		area.setText( "" );
	}

	public String getText() {
		return area.getText();
	}
	
	public TerminalWindowSettings getWindowSettings() {
		return windowSettings;
	}
	
	public void setColors() {
		area.setBackground( windowSettings.getBackgroundColor() );
		area.setForeground( windowSettings.getForegroundColor() );
	}
	
	public JTextArea getArea() {
		return area;
	}
	
	public void setlogFile( File logFile ) {
		try {
			this.logFile = logFile;
			this.logOut = new BufferedWriter( new FileWriter( logFile ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyProcessStarted( String name ) {
		if ( logFile != null ) {
			try {
				logOut = new BufferedWriter( new FileWriter( logFile ) );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void notifyProcessEnded( String name ) {
		if ( logOut != null ) {
			try {
				logOut.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
	}
}