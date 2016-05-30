package ui.log;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import statics.GU;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Oct 13, 2013, 1:45:14 AM 
 */
public class LogMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public LogMenu( final LogPanel log ) {
		JMenuItem select = new JMenuItem( "Select All" );
		select.addActionListener( new ActionListener() { 
			public void actionPerformed( ActionEvent e ) {
				JTextArea text = log.area;
				text.requestFocus();
				text.setSelectionStart( 0 );
				text.setSelectionEnd( text.getText().length() );
			}
		} );
		this.add( select );
		JMenuItem copy = new JMenuItem( "Copy" );
		copy.addActionListener( new ActionListener() { 
			public void actionPerformed( ActionEvent e ) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection( log.area.getSelectedText() ), null );
			}
		} );
		this.add( copy );
		JMenuItem setColors = new JMenuItem( "Set Colors" );
		setColors.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				ColorChooserDialog d = new ColorChooserDialog( log.windowSettings.getForegroundColor(), log.windowSettings.getBackgroundColor() );
				d.setVisible( true );
				if ( d.getResult() == ColorChooserDialog.OK ) {
					log.windowSettings.setBackgroundColor( d.getBack() );
					log.windowSettings.setForegroundColor( d.getFore() );
					log.setColors();
				}
			}
		} );
		this.add( new JSeparator() );
		this.add( setColors );
	}
	
	private class ColorChooserDialog extends JDialog {
		
		private static final long serialVersionUID = 1L;

		public static final int OK = 1;
		
		public static final int CANCEL = 0;
		
		private int result;
		
		private JLabel back = new JLabel();
		
		private JLabel fore = new JLabel();
		
		private JColorChooser choose = new JColorChooser();
		
		public ColorChooserDialog( Color f, Color b ) {
			super( new JFrame(), "Color Select", true );
			this.setLocationRelativeTo( null );
			this.setModal( true );
			this.setSize( 340, 125 );
			back.setOpaque( true );
			back.setBackground( b );
			back.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			back.addMouseListener( new MouseAdapter() {
				public void mouseClicked( MouseEvent e ) {
					if ( e.getButton() == MouseEvent.BUTTON1 ) {
						JColorChooser.createDialog( back, "Background", true, choose,
							new ActionListener() { 
								public void actionPerformed( ActionEvent e ) {
									back.setBackground( choose.getColor() );
									back.setForeground( choose.getColor() );
								} 
							},
							null ).setVisible( true );
					}
				}
			} );
			fore.setOpaque( true );
			fore.setBackground( f );
			fore.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
			fore.addMouseListener( new MouseAdapter() {
				public void mouseClicked( MouseEvent e ) {
					if ( e.getButton() == MouseEvent.BUTTON1 ) {
						JColorChooser.createDialog( fore, "Foreground", true, choose,
							new ActionListener() { 
								public void actionPerformed( ActionEvent e ) {
									fore.setBackground( choose.getColor() );
									fore.setForeground( choose.getColor() );
								} 
							},
							null ).setVisible( true );
					}
				}
			} );
			JPanel p = new JPanel();
			p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
			GU.hp( p, new JLabel( "Foreground: " ), fore );
			GU.spacer( p );
			GU.hp( p, new JLabel( "Background: " ), back );
			GU.spacer( p );
			JButton ok = new JButton( "Ok" );
			ok.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					ok();
				}
			} );
			JButton cancel = new JButton( "Cancel" );
			cancel.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					cancel();
				}
			} );
			GU.hp( p, ok, cancel );
			this.add( p );
		}
		
		private void ok() {
			this.result = OK;
			this.dispose();
		}
		
		private void cancel() {
			this.result = CANCEL;
			this.dispose();
		}
		
		public int getResult() {
			return result;
		}
		
		public Color getFore() {
			return fore.getBackground();
		}
		
		public Color getBack() {
			return back.getBackground();
		}
	}
}