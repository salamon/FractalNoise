package newtonfractal;


import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class JPanelExtra extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JFormattedTextField minimoX, minimoY, maximoX, maximoY; 
	private JSpinner maxIterations;
	private JComboBox formaAvaliacao;
	private JCheckBox alpha, darker;
	
	private JFormattedTextField larguraTela;
	private JFormattedTextField alturaTela;
		
	public JPanelExtra(NewtonFractalJFrame reference) {		
		setLayout(new GridLayout(12,1));
		
		maxIterations = new JSpinner(new SpinnerNumberModel(15, 1, 100000, 1));
		
		minimoX = new JFormattedTextField(new Double(-2.5));
		minimoY = new JFormattedTextField(new Double(-1.88));
		maximoX = new JFormattedTextField(new Double(2.5));
		maximoY = new JFormattedTextField(new Double(1.88));
		larguraTela = new JFormattedTextField(new Integer(800));
		alturaTela = new JFormattedTextField(new Integer(600));
		
		//formaAvaliacao = new JComboBox(new Object[]{"Horner", "Fatorada", "Pot�ncia"});
		formaAvaliacao = new JComboBox(new Object[]{"Horner"});
		
		add(new JLabel(""));
		
		JPanel panel1 = new JPanel(new GridLayout(1,3));
		panel1.add(new JLabel("  Limite de Iterações: "));
		panel1.add(maxIterations);
		panel1.add(new JLabel(""));
		add(panel1);
		
		add(new JLabel(""));
			
		JPanel panelCheckBoxes = new JPanel(new GridLayout(1,3));
		alpha = new JCheckBox(" Aplicar Alpha", true);
		darker = new JCheckBox(" Aplicar Darker e Brighter", false);
		panelCheckBoxes.add(alpha);		
		panelCheckBoxes.add(darker);
		add(panelCheckBoxes);
			
		JPanel panelTela = new JPanel(new GridLayout(1,3));
		panelTela.add(new JLabel("  Largura e Altura da tela:"));
		panelTela.add(larguraTela);		
		panelTela.add(alturaTela);
		add(panelTela);
						
		add(new JLabel(""));
		
		JPanel panel3 = new JPanel(new GridLayout(1,3));
		panel3.add(new JLabel("  Limites do Plano: "));		
		panel3.add(new JLabel("Valor Mínimo: "));		
		panel3.add(new JLabel("Valor Máximo: "));
		add(panel3);
		
		JPanel panel4 = new JPanel(new GridLayout(1,3));
		panel4.add(new JLabel("  Parte Real (eixo x): "));
		panel4.add(minimoX);
		panel4.add(maximoX);
		add(panel4);
		
		JPanel panel5 = new JPanel(new GridLayout(1,3));
		panel5.add(new JLabel("  Parte Imaginária (eixo y): "));
		panel5.add(minimoY);
		panel5.add(maximoY);
		add(panel5);
		
		add(new JLabel(""));
		
		JPanel panel6 = new JPanel(new GridLayout(1,2));
		panel6.add(new JLabel("  Forma de avaliação polinomial: "));
		panel6.add(formaAvaliacao);
		add(panel6);
		
	}
	
		
	public PlaneLimits getPlaneLimits() { 
		return new PlaneLimits(minimoX.getText(), minimoY.getText(), maximoX.getText(), maximoY.getText());
	}
	
	public long getLimiteIteracoes() {
		return Integer.parseInt(maxIterations.getValue()+"");
	}

	public int getEvaluationForm() {				
		return formaAvaliacao.getSelectedIndex();
	}



	public boolean getAlpha() {
		return alpha.isSelected();
	}
	
	public boolean getDarker() {
		return darker.isSelected();
	}
	
	public int getAltura() {
		return Integer.parseInt(alturaTela.getText().replace(".", ""));
	}
	
	public int getLargura() {
		return Integer.parseInt(larguraTela.getText().replace(".", ""));
	}
	
 

}
