package ui.terminal.settings;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import props.PropertyConstants;
import props.utils.ColorUtils;
import xml.XMLExpansion;
import xml.XMLValues;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 2:11:48 PM 
 */
public class TerminalWindowSettings implements XMLValues {
	
	private int lineLimit = 75;
	
	private Color background = Color.decode( "#FFFAC4" ); 
	
	private Color foreground = Color.BLACK;
	
	public Color getBackgroundColor() {
		return background;
	}
	
	public Color getForegroundColor() {
		return foreground;
	}
	
	public void setBackgroundColor( Color background ) {
		this.background = background;
	}
	
	public void setForegroundColor( Color foreground ) {
		this.foreground = foreground;
	}
	
	public int getLineLimit() {
		return lineLimit;
	}
	
	public void setLineLimit( int lineLimit ) {
		this.lineLimit = lineLimit;
	}

	@Override
	public List<XMLValues> getChildNodes() {
		return null;
	}

	@Override
	public void loadParamsFromXMLValues( XMLExpansion e ) {
		Map<String, String[]> values = e.getChild( PropertyConstants.COLORS ).getValues();
		background = ColorUtils.toColor( values.get( PropertyConstants.BACKGROUND )[ 0 ] );
		foreground = ColorUtils.toColor( values.get( PropertyConstants.FOREGROUND )[ 0 ] );
	}

	@Override
	public Map<String, Map<String, String[]>> saveParamsAsXML() {
		Map<String, Map<String, String[]>> ret = new HashMap<String, Map<String, String[]>>();
		Map<String, String[]> values = new HashMap<String, String[]>();
		ret.put( PropertyConstants.COLORS, values );
		values.put( PropertyConstants.BACKGROUND, new String[] { ColorUtils.toHex( getBackgroundColor() ) } );
		values.put( PropertyConstants.FOREGROUND, new String[] { ColorUtils.toHex( getForegroundColor() ) } );
		return ret;
	}
}