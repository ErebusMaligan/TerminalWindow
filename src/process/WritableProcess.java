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
	
	public String filterASCIICodes( String line ) {
//		String ESC = "\\x1B\\x5B";
//		String intermediate = line.replaceAll( "\\\\x1B\\[..;..[m]" + "|\\\\x1B\\[.{0,2}[m]" + "|\\(Page \\d+\\)" + "|\\x1B\\\\[[K]" + "|\\x1B.*[\\x40-\\x7E]{1}", "" ); //    |\u001B|\u000F
//		String intermediate = line.replaceAll( ESC + "(([\\x40-\\x5F]{1})++)" + "|" + ESC + "(([\\x30-\\x3F]*[\\x20-\\x2F]*[\\x40-\\x7E]{1})++)" + "|[\\x00-\\x1A]|[\\x1C-\\x1F]", "" );
//		return intermediate;
		return line;
	}
}