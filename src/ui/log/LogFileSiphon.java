package ui.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import process.ProcessManager;
import process.io.ProcessStreamSiphon;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 25, 2013, 5:11:04 AM 
 */
public class LogFileSiphon implements ProcessStreamSiphon {
	
	protected BufferedWriter fstream;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:SSS");
	
	public LogFileSiphon( String processName, String filePath ) {
		try {
			ProcessManager.getInstance().registerSiphon( processName, this );
			fstream = new BufferedWriter( new FileWriter( filePath ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
	}

	public void skimMessage( String name, String s ) {
		try {
			fstream.write( "[" + sdf.format( new Date( System.currentTimeMillis() ) ) + "]:  " + s );
			fstream.newLine();
			fstream.flush();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void notifyProcessEnded( String name ) {
		try {
			fstream.close();
			ProcessManager.getInstance().removeSiphon( name, this );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void notifyProcessStarted( String name ) {}
}
