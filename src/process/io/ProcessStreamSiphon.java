package process.io;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 22, 2013, 11:23:47 AM 
 */
public interface ProcessStreamSiphon {
	
	public void skimMessage( String name, String s );
	
	public void notifyProcessStarted( String name );
	
	public void notifyProcessEnded( String name );
}