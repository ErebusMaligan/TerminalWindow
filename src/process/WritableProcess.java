package process;

import java.io.BufferedWriter;

import process.io.ProcessStreamInjector;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 22, 2013, 2:47:39 PM 
 */
public abstract class WritableProcess implements ProcessStreamInjector {
	
	protected BufferedWriter writer;
	
	public BufferedWriter getWriter() {
		return writer;
	}
	
	@Override
	public abstract void sendCommand( String s );

}