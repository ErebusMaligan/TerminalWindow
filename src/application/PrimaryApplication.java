package application;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.terminal.os.OSTerminalSettings;
import ui.terminal.panel.TerminalWindowManager;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 20, 2013, 10:24:34 AM 
 */
public class PrimaryApplication {

	//This isn't really meant to be run, it's just for debugging
	
	public static void main( String args[] ) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e ) {
			System.err.println( "Critical JVM Failure!" );
			e.printStackTrace();
		}
		TerminalWindowManager m = TerminalWindowManager.getInstance();
		Properties p = new Properties();
		try {
			p.load( new FileInputStream( new File( "app.props" ) ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		m.OS = p.getProperty( "OS" ).equals( "WINDOWS" ) ? OSTerminalSettings.WINDOWS : OSTerminalSettings.LINUX;
		String cwd = null;
//		String cwd = "D:\\RIPS\\ISO";
		if ( args.length == 1 ) {
			cwd = args[ 0 ];
		}
		m.createTerminal( "", new Dimension( 800, 800 ), cwd );
//		try {
//			DefaultPropsWriter.writeDefaultProperties();
//		} catch ( ModifyException | NavException | TranscodeException | IOException | ParseException e ) {
//			e.printStackTrace();
//		}
	}
}