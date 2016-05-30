package ui.log;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Sep 13, 2013, 2:24:28 AM 
 */
public class LineLimitListener implements DocumentListener {

	private int maximumLines;

	public LineLimitListener( int maximumLines ) {
		this.maximumLines = maximumLines;
	}

	public void insertUpdate( final DocumentEvent e ) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				removeLines( e );
			}
		} );
	}

	private void removeLines( DocumentEvent e ) {
		Document document = e.getDocument();
		Element root = document.getDefaultRootElement();
		while ( root.getElementCount() > maximumLines ) {
			removeFromStart( document, root );
		}
	}

	private void removeFromStart( Document document, Element root ) {
		Element line = root.getElement( 0 );
		try {
			document.remove( 0, line.getEndOffset() );
		} catch ( BadLocationException e ) {
			e.printStackTrace();
		}
	}

	public void removeUpdate( DocumentEvent e ) {}

	public void changedUpdate( DocumentEvent e ) {}
}