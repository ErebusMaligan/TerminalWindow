package process;

import process.io.ProcessStreamSiphon;


/**
 * This is a non-real process.  One you invent in code and just want to hook the standard read/write stuff into
 * 
 * I can't explain it...
 * 
 * This really isn't a writable process and should probably be refactored, it's more a readable process that sends to siphons
 * 
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 25, 2013, 6:44:32 PM
 */
public abstract class NonStandardProcess extends WritableProcess {

	protected String name;


	public NonStandardProcess( String name ) {
		this.name = name;
		ProcessManager.getInstance().registerProcess( name, this );   //MAKE SURE YOU HANDLE REMOVING THIS PROCESS AT THE END OF THE EXECUTE BLOCK; NOT DOING SO LEADS TO ZOMBIES
	}
	
	public abstract void execute();
	
	
	public void sendMessage( String s ) {
		for ( ProcessStreamSiphon siphon : ProcessManager.getInstance().getSiphons( name ) ) {
			siphon.skimMessage( name, s );
		}
	}

	public String getName() {
		return name;
	}

	public void sendCommand( String s ) {
	}
}