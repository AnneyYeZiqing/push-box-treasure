//package sociology;

import java.awt.EventQueue;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class PushBoxWrapper extends JFrame {

	public final int FRAMESIZE = 600;
	public final int BTNSPACE = 63;
	public final int HRZSPACE = 8;
	public final int NUMCOLORS = 2;
	
	public PushBoxWrapper() {
        setSize(5*FRAMESIZE/2+HRZSPACE, FRAMESIZE+BTNSPACE+90);
		add(new PushBox(FRAMESIZE, FRAMESIZE, NUMCOLORS));
        setResizable(false);
        setTitle("Treasure Transporter");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PushBoxWrapper go = new PushBoxWrapper();
                go.setVisible(true);
            }
        });
	}
}
