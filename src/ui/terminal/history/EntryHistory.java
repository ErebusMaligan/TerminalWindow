package ui.terminal.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import props.PropertyConstants;
import xml.XMLExpansion;
import xml.XMLValues;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 9:21:45 PM 
 */
public class EntryHistory implements XMLValues {
	
	private static String delim = "~:~";
	
	private int max = 200;
	
	private ArrayList<String> hist = new ArrayList<String>( 200 );
	
	public int lastIndex = -1;
	
	public int traverse = -1;
	
	private Vector<HistoryListener> listeners = new Vector<HistoryListener>();
	
	public List<String> getHistory() {
		return hist;
	}
	
	public void addListener( HistoryListener l ) {
		if ( !listeners.contains( l ) ) {
			listeners.add( l );
		}
	}
	
	public void removeListener( HistoryListener l ) {
		listeners.remove( l );
	}
	
	public void append( String entry ) {
		lastIndex++;
		if ( lastIndex == max ) {
			lastIndex = 0;
		}
		traverse = lastIndex + 1;
		hist.add( lastIndex, entry );
		for ( HistoryListener l : listeners ) {
			l.append( entry );
		}
	}
	
	public String up() {
		String ret = null;
		if ( lastIndex > -1 ) {
			traverse--;
			if ( traverse < 0 ) {
				traverse = 0;
			}
			ret = hist.get( traverse );
		}
		return ret;
	}
	
	public String down() {
		String ret = null;
		if ( lastIndex > -1 ) {
			if ( traverse != lastIndex ) {
				traverse++;
			}
			ret = hist.get( traverse );
		}
		return ret;
	}
	
	public void clear() {
		hist.clear();
	}
	
	public static String serializeContent( List<String> hist ) {
		String ret = "";
		for ( String s : hist ) {
			ret += s + delim;
		}
		return ret;
	}
	
	public static void deserializeContent( String in, List<String> hist ) {
		for ( String entry : in.split( delim ) ) {
			hist.add( entry );
		}
	}

	@Override
	public List<XMLValues> getChildNodes() {
		return null;
	}

	@Override
	public void loadParamsFromXMLValues( XMLExpansion e ) {
		if ( e.getChild( PropertyConstants.HISTORY ) != null ) {
			Map<String, String[]> values = e.getChild( PropertyConstants.HISTORY ).getValues();
			traverse = Integer.parseInt( values.get( PropertyConstants.TRAVERSE )[ 0 ] );
			lastIndex = Integer.parseInt( values.get( PropertyConstants.LAST )[ 0 ] );
			deserializeContent( values.get( PropertyConstants.CONTENT )[ 0 ], hist );
		}		
	}

	@Override
	public Map<String, Map<String, String[]>> saveParamsAsXML() {
		Map<String, Map<String, String[]>> ret = new HashMap<String, Map<String, String[]>>();
		Map<String, String[]> values = new HashMap<String, String[]>();
		ret.put( PropertyConstants.HISTORY, values );
		values.put( PropertyConstants.LAST, new String[] { "" + lastIndex } );
		values.put( PropertyConstants.TRAVERSE, new String[] { "" + traverse } );
		values.put( PropertyConstants.CONTENT, new String[] { serializeContent( hist ) } );
		return ret;
	}

}