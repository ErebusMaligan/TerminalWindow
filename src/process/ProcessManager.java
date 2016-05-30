package process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import process.io.ProcessStreamSiphon;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Aug 22, 2013, 3:29:09 PM 
 */
public class ProcessManager {
	
	private static ProcessManager instance;
	
	private Map<String, WritableProcess> processMap = new HashMap<String, WritableProcess>();
	
	private Map<String, Vector<ProcessStreamSiphon>> siphons = new HashMap<String, Vector<ProcessStreamSiphon>>();
	
	private ProcessManager() {}
	
	public static ProcessManager getInstance() {
		if ( instance == null ) {
			instance = new ProcessManager();
		}
		return instance;
	}
	
	public synchronized WritableProcess getProcessByName( String name ) {
		return processMap.get( name );
	}
	
	public synchronized void removeAll( String name ) {
		processMap.remove( name );
		siphons.remove( name );
	}
	
	public synchronized void registerProcess( String name, WritableProcess p ) {
		processMap.put( name, p );
		if ( !siphons.containsKey( name ) ) {
			siphons.put( name, new Vector<ProcessStreamSiphon>() );
		}
	}
	
	public synchronized void removeSiphons( String name ) {
		siphons.remove( name );
	}
	
	public synchronized void removeProcess( String name ) {
		processMap.remove( name );
	}
	
	public synchronized int getProcessCount() {
		return processMap.size();
	}
	
	public synchronized void registerSiphon( String name, ProcessStreamSiphon siphon ) {
		Vector<ProcessStreamSiphon> temp = null;
		if ( !siphons.containsKey( name ) ) {
			temp = new Vector<ProcessStreamSiphon>();
		} else {
			temp = siphons.get( name );
		}
		temp.add( siphon );
		siphons.put( name, temp );
	}
	
	public synchronized void removeSiphon( String name, ProcessStreamSiphon siphon ) {
		Vector<ProcessStreamSiphon> temp = null;
		if ( !siphons.containsKey( name ) ) {
			temp = new Vector<ProcessStreamSiphon>();
		} else {
			temp = siphons.get( name );
		}
		temp.remove( siphon );
		siphons.put( name, temp );
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Vector<ProcessStreamSiphon> getSiphons( String name ) {
		Vector<ProcessStreamSiphon> ret = null;
		if ( !siphons.containsKey( name ) ) {
			ret = new Vector<ProcessStreamSiphon>();
		} else {
			ret = siphons.get( name );
		}
		return (Vector<ProcessStreamSiphon>)ret.clone(); //TODO; fixes some kind of concurrency issues at StandardProcess:(92)... 
														//UPDATE: need to syncrhonize around the collection, where it is used.  The get is sync, but the usage after isn't.
	}
	
	public synchronized List<String> getAllAvailableProcessNames() {
		return new ArrayList<String>( processMap.keySet() );
	}
}