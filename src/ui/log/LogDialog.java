package ui.log;

import java.awt.Frame;

import javax.swing.JDialog;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 25, 2013, 3:03:15 AM 
 */
public class LogDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private LogPanel lp;
	
	public LogDialog( Frame f, String name, boolean modal ) {
		super( f, name, modal );
		this.setSize( 500, 500 );
		lp = new LogPanel( name );
		this.add( lp );
		this.setVisible( true );
	}
	
	public LogPanel getLogPanel() {
		return lp;
	}
}