package newtonfractal;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;


public class JProgressBarSetValue extends JDialog {

	private static final long serialVersionUID = 1L;
	private JProgressBar bar;
		
	public JProgressBarSetValue() {
		//setModal(true);	
		setTitle("Progresso...");
		setResizable(false);
		bar = new JProgressBar(0, 100);
		//bar.setMinimum(minimo);	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bar.setStringPainted(true);		
		getContentPane().add(bar);
		setBounds(120, 120, 160, 90);
		setVisible(true);		
	}
	
	
	public void setValueBar(int newValue) {
		bar.setValue(newValue);		
	}


	public void close() {
		dispose();		
	}



	
	
}