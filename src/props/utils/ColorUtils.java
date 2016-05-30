package props.utils;

import java.awt.Color;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 21, 2013, 11:40:34 AM 
 */
public class ColorUtils {
	
	public static String toHex( Color c ) {
		return String.format( "%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue() );
	}
	
	public static Color toColor( String hex ) {
		return Color.decode( "#" + hex.toUpperCase() );
	}
	
}
