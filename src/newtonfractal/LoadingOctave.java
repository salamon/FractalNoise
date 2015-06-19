package newtonfractal;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class LoadingOctave extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextArea texto;
	
		
	public LoadingOctave() {
		super("Loading Octave... Please wait");
		texto = new JTextArea();		
		JScrollPane scroll = new JScrollPane(texto);		
		this.add(scroll);
		this.setBounds(100, 100, 600, 400);
				
		setResizable(false);
		setVisible(true);
	}
	
	
	public void  addText(String text){
		texto.setText(texto.getText() + "\n" + text);
		texto.validate();
	}
	
	
	public void fechar() { 
		this.dispose();
	}

}
