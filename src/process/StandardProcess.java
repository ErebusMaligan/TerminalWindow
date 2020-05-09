package process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import process.io.ProcessStreamSiphon;

public class StandardProcess extends WritableProcess {

	protected Process p;

	protected String name;

	protected boolean processKilled = false;
	
	protected String launch;

	protected String[] multiLaunch;
	
	protected Thread pThread = null;
	
	protected boolean terminated = false;
	
	public StandardProcess( String name, String launch ) {
		this.name = name;
		this.launch = launch;
		restartProcess();
	}
	
	public StandardProcess( String name, String[] multiLaunch ) {
		this.multiLaunch = multiLaunch;
		this.name = name;
		restartProcess();		
	}

	public void closeResources() throws IOException {
		try {
			sendCommand( "" );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		writer.close();
		writer = null;
		processKilled = true;
//		while ( pThread != null ) { ///block until this is properly closed - if not, bad things happen like zombie processes
		if ( pThread != null ) {
			try {
				Thread.sleep( 1000 );  //more than 1 second, and it might be forever stuck in a readLine that will never emerge, so just kill it
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
			if ( pThread != null && pThread.isAlive() ) { //somehow i had this not be null up above but must get set null by another thread in the interim so this threw a NPE
				pThread.interrupt();
			}
		}
		ProcessManager.getInstance().removeProcess( name );
		destroyProcess();
	}
	
	private void destroyProcess() {
//		new Thread() {
//			public void run() {
				p.destroy();
//			}
//		}.start();
		try {		//save this block for debugging
//			int ev = p.exitValue();
//			System.out.println( "Process Exited with Code: " + ev );
			terminated = true;
		} catch ( IllegalThreadStateException e ) {
			destroyProcess();
		}
	}

	public void restartProcess() {
		ProcessManager.getInstance().registerProcess( name, this );
		ProcessBuilder builder = new ProcessBuilder( launch );
		if ( multiLaunch != null ) {
			builder = new ProcessBuilder( multiLaunch );
		}
		builder.redirectErrorStream( true );
		try {
			p = builder.start();
			hookProcess();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		processKilled = false;
		terminated = false;
	}

	private void hookProcess() {
		writer = new BufferedWriter( new OutputStreamWriter( p.getOutputStream() ) );
		for ( ProcessStreamSiphon siphon : ProcessManager.getInstance().getSiphons( name ) ) {
			siphon.notifyProcessStarted( name );
		}
		pThread = new Thread() {
			public void run() {
				BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
				String line;
				try {
					while ( !processKilled && ( line = reader.readLine() ) != null ) {
						for ( ProcessStreamSiphon siphon : ProcessManager.getInstance().getSiphons( name ) ) {
							siphon.skimMessage( name, filterASCIICodes( line ) );
						}
						Thread.sleep( 1 );
					}
				} catch ( IOException | InterruptedException e ) {
//					System.err.println( "Thread interrupted, likely due to shutdown" );
				}
				for ( ProcessStreamSiphon siphon : ProcessManager.getInstance().getSiphons( name ) ) {
					siphon.notifyProcessEnded( name );
				}
				try {
					reader.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}	
				pThread = null;
			}
		};
		pThread.start();
	}

	public String getName() {
		return name;
	}

	public void sendCommand( String s ) {
		try {
			writer.write( s + "\n" );
			writer.flush();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public Process getProcess() {
		return p;
	}
	
	public boolean isTerminated() {
		return terminated;
	}
}