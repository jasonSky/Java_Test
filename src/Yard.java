import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class Yard extends Frame {	
	private static final long serialVersionUID = 1L;
	private static final int ROWS = 100;
	private static final int COLS = 100;
	private static final int BLOCK_SIZE = 5;
	
	public void launch(){
		this.setLocation(0,0);
		this.setSize( COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		
	}
	public static void main(String[] args) {
		new Yard().launch();
	}
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		g.setColor(c);
	}

}