package newtonfractal;


import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public class JDialogResposta extends JDialog {

	private static final long serialVersionUID = 1L;

	
	
	public JDialogResposta(JTable tabela) {
				
		JScrollPane scroll = new JScrollPane(tabela);
		add(scroll);		
		setModal(true);
		setBounds(220,220,400, 250);						
		setResizable(false);
		setTitle("Raízes: ");		
		setVisible(true);
	}

}
