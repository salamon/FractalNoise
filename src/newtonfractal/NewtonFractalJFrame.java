package newtonfractal;

import fractalnoise.MainPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class NewtonFractalJFrame extends JFrame implements ChangeListener, ActionListener{
 
	private static final long serialVersionUID = 1L;

	
	private int width = 482;
	private int height = 460;
	
	private JTabbedPane tabbedPane;
	private JPanelCoefficients panelCoef;
	//private JPanelFractal panelFractal;
	private JPanelExtra panelExtra;
	private JPanelRoots panelRoots;
	
	private JMenuItem sobre, desenharA, desenharN, help;
	
		 
	public NewtonFractalJFrame() {	
		
		setTitle("Newton Fractal");				
		setBounds(160, 120, width, height);
		
		panelCoef = new JPanelCoefficients(this);
		panelRoots = new JPanelRoots(this);
		panelExtra = new JPanelExtra(this);
		//panelFractal = new JPanelFractal(this);
				
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Coeficientes", panelCoef);
		tabbedPane.addTab("Raizes", panelRoots);
		tabbedPane.addTab("Parametros Extras", panelExtra);
		//tabbedPane.addTab("Desenhar Newton Fractal!", panelFractal);
							
		tabbedPane.addChangeListener(this);			
		add(tabbedPane);	
		
		JMenuBar menuBar;
		JMenu menu, menu2;
		menuBar = new JMenuBar();
		menu = new JMenu(" Desenhar Newton Fractal ");
		menu2 = new JMenu(" Ajuda ");
		help = new JMenuItem("Ajuda");
		sobre = new JMenuItem("Sobre...");
		menu2.add(help);
		menu2.add(sobre);
		desenharA = new JMenuItem("Desenhar Newton Fractal!");
		menu.add(desenharA);
                desenharN = new JMenuItem("Desenhar Newton Fractal com Análise Sonora");
		menu.add(desenharN);
		
		sobre.addActionListener(this);
		help.addActionListener(this);
		desenharA.addActionListener(this);
                desenharN.addActionListener(this);
		menuBar.add(menu);
		menuBar.add(menu2);
		setJMenuBar(menuBar);
		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if(tabbedPane.getSelectedIndex() == 3)
		{																	
			//new RootInformation(new ComplexNumber("1", "0"), new Color(0f,0f,1f,1f), "0.075", 1); 
			//new RootInformation(new ComplexNumber("-0.5", "0.8660254037844386"), Color.RED, "0.075", 1); 
			//new RootInformation(new ComplexNumber("-0.5", "-0.8660254037844386"), Color.GREEN, "0.075", 1); 
								
			//desenhar						
			//panelFractal.createThreadComputaCores();
			
		}	
		else {
			//panelFractal.killThreadComputaCores();
		}
	}
	
	
		
	public PlaneLimits getPlaneLimits() {
		return panelExtra.getPlaneLimits();
	}

	public PolynomialFunction getPolynomial() {
		return panelCoef.getPolinomio();
	}

	public RootInformation[] getRootInformation() {
		return panelRoots.getRootInformation();
	}

	public long getMaxIterations() {
		return panelExtra.getLimiteIteracoes();
	}

	public int getEvaluationForm() {
		return panelExtra.getEvaluationForm();
	}



	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JPanelCoefficients getPanelCoef() {
		return panelCoef;
	}

	//public JPanelFractal getPanelFractal() {
	//	return panelFractal;
	//}

	public JPanelExtra getPanelExtra() {
		return panelExtra;
	}

	public JPanelRoots getPanelRoots() {
		return panelRoots;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean getAlpha() {	
		return panelExtra.getAlpha();
	}
	
	public boolean getDarker() {	
		return panelExtra.getDarker();
	}
	
		
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == desenharA) {
			JFrame fractal = new JFrame("Fractal!");
			JPanelFractal panelFractal = new JPanelFractal(this);
			fractal.setBounds(100, 100,panelExtra.getLargura(), panelExtra.getAltura());
			fractal.setContentPane(panelFractal);
			fractal.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			fractal.setResizable(false);
			fractal.setVisible(true);			
			panelFractal.createThreadComputaCores();			
		}else if(e.getSource() == desenharN) {
                        JPanelFractal panelFractal = new JPanelFractal(this);
                        MainPanel fractal_trace = new MainPanel(panelExtra.getLargura(), panelExtra.getAltura(), panelFractal);
			fractal_trace.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			fractal_trace.setResizable(false);
			fractal_trace.setVisible(true);			
			panelFractal.createThreadComputaCores();
                }
		else if(e.getSource() == help) {
			JOptionPane.showMessageDialog(null, "Esse software realiza o desenho " +
					"do fractal resultante \nda aplicação do método de newton no" +
					" plano complexo. \nConfigure os parâmetros nas abas e solicite o desenho!");
		}				
		else if(e.getSource() == sobre) {
			JOptionPane.showMessageDialog(null, "Software desenvolvido como parte do " +
					"Trabalho de\nConclusão do curso de Ciência de Computaço na PUCRS." +
					"\nAluno: Artur Freitas. Orientação: Beatriz Franciosi.");
		}		
	}
		
	
}





 
