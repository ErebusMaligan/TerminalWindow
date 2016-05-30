package process.io.ui;

import java.util.NoSuchElementException;

import process.io.ProcessStreamSiphon;


/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 22, 2013, 11:28:24 AM 
 */
public class UIStreamSiphon implements ProcessStreamSiphon {

	public static String UI_TOKEN = "<~UIN~>";
	
	public static String PAIR_DELIM = "<~:~>";
	
	private TerminalProcessUIComponent ui;
	
	public UIStreamSiphon( TerminalProcessUIComponent ui ) {
		this.ui = ui;
	}
	
	public static String formMessage( String key, String value ) {
		String ret = UI_TOKEN;
		ret += key + PAIR_DELIM + value;
		return ret;		
	}
	
	@Override
	public void skimMessage( String name, String s ) {
		try {
    		if ( s.startsWith( UI_TOKEN ) ) {
    			String content = s.split( UI_TOKEN )[ 1 ];
				String[] values = content.split( PAIR_DELIM );
				ui.notifyUIStreamMessage( name, values[ 0 ], values[ 1 ] );	
    		}
		} catch ( NoSuchElementException e ) {
			System.err.println( "INVALID UI STRING FORMAT: " + s );
		}
	}

	//not used here
	
	@Override
	public void notifyProcessStarted( String name ) {  
	}
	
	@Override
	public void notifyProcessEnded( String name ) {
	}
	
}