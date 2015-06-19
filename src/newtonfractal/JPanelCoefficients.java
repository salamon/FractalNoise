package newtonfractal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class JPanelCoefficients extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JSpinner grau;
	private NewtonFractalJFrame reference;
	
	private JButton calcularRaizesOctave, sortearCoeficientes;	
	private Jopas jopas;
	
	public JPanelCoefficients(NewtonFractalJFrame reference) {
		setLayout(new GridLayout(2, 1));
		this.reference = reference;
		JPanel superior2 = new JPanel(new GridLayout(1, 3));		
		superior2.add((new JLabel("                                                  ")));						
		add(superior2);	
		
		setLayout(new FlowLayout(FlowLayout.LEFT));		
		JPanel superior = new JPanel(new GridLayout(1, 2));
		grau = new JSpinner(new SpinnerNumberModel(3, 1, 64, 1));
		JFormattedTextField tf =((JSpinner.DefaultEditor)grau.getEditor()).getTextField();
		tf.setEditable(false);		
		tf.setBackground(Color.white);		
		grau.getModel().addChangeListener(new ChangeListener() {
	         public void stateChanged(ChangeEvent e) {
	        	 try{
	        		 Object[] columnNames = {"Coeficientes","Parte Real", "Parte Imaginária"};
		        	 Object[][] data = generateData(Integer.parseInt(grau.getValue()+""));
		             table.setModel(new MyTableModel(1, data, columnNames));		             	             
	        	 }
	        	 catch (Exception e1) {
	        		 System.out.println(e1);
	        	 }
	         }
	    });
		
		superior.add((new JLabel("Informe o grau do polinômio:                    ")));
		superior.add(grau);
				
		add(superior);		
		Object[] columnNames = {"Coeficientes","Parte Real", "Parte Imaginária"};
		Object[][] data = generateData(3);
					
		table = new JTable(new MyTableModel(1, data, columnNames));	
		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//table.setFillsViewportHeight(true);
		add(new JLabel("Informe os Coeficientes do Polinômio:"));
		add(scrollPane);	
		
		scrollPane.setPreferredSize(new Dimension(reference.getWidth()-18,reference.getHeight()-200));
		scrollPane.revalidate();
		
		
		JPanel southPanel = new JPanel();		
		sortearCoeficientes = new JButton("Sortear coeficientes");
		calcularRaizesOctave = new JButton("Calcular as raizes com o Octave!");
		calcularRaizesOctave.addActionListener(this);	
		sortearCoeficientes.addActionListener(this);
		southPanel.add(sortearCoeficientes);
		southPanel.add(calcularRaizesOctave);		
		add(southPanel, BorderLayout.SOUTH);				
				
	}

	private Object[][] generateData(int numLinhas) {
		Object[][] oldData = null;
		if(table != null)
			oldData =  ((MyTableModel)table.getModel()).getData();
		Object[][] data = new Object[numLinhas + 1][3];
		for(int i = 0; i < numLinhas + 1; i++) {
			data[i][0] = "a" + i ;
			if(oldData != null && oldData.length > i) {
				data[i][1] = oldData[i][1];
				data[i][2] = oldData[i][2];	
			}
			else {
				data[i][1] = new Double(0);
				data[i][2] = new Double(0);
			}
		}		
		return data;
	}
	
	private Object[][] generateData(int numLinhas, float coef[][]) {				
		Object[][] data = new Object[numLinhas + 1][3];
		for(int i = 0; i < numLinhas + 1; i++) {
			data[i][0] = "a" + i ;			
			data[i][1] = coef[i][0];
			data[i][2] = coef[i][1];				
		}		
		return data;
	}
	
	
	public PolynomialFunction getPolinomio() {
		ComplexNumber[] coef = new ComplexNumber[Integer.parseInt(grau.getValue() + "") + 1];
		MyTableModel model = (MyTableModel) table.getModel();
		for(int i = 0; i < coef.length; i++)
			coef[i] = new ComplexNumber(model.getValueAt(i, 1)+"", model.getValueAt(i, 2)+"");		
		return new PolynomialFunction(coef);
	}
	
	public JTable getTableCoef() {
		return table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sortearCoeficientes) {
			float coef[][] = new float[Integer.parseInt(grau.getValue() + "") + 1][2];
			
			Random random = new Random(System.currentTimeMillis());
			for(int i = 0; i < coef.length; i++) {
				coef[i][0] = random.nextInt(random.nextInt(40) + 1);
				coef[i][1] = random.nextInt(random.nextInt(40) + 1);
				if(random.nextBoolean())
					coef[i][0] += random.nextFloat();
				if(random.nextBoolean())
					coef[i][1] += random.nextFloat();
				if(random.nextBoolean())
					coef[i][0] += random.nextInt(random.nextInt(40) + 1);
				if(random.nextBoolean())
					coef[i][1] += random.nextInt(random.nextInt(40) + 1);
				if(random.nextBoolean())
					coef[i][0] = -coef[i][0];
				if(random.nextBoolean())
					coef[i][1] = -coef[i][1];	
				
				if(random.nextBoolean() && random.nextBoolean())
					coef[i][0] = 0;
				if(random.nextBoolean() && random.nextBoolean())
					coef[i][1] = 0;				
			}
			
			Object[] columnNames = {"Coeficientes","Parte Real", "Parte Imaginária"};
			Object[][] data = generateData(Integer.parseInt(grau.getValue()+""), coef);
            table.setModel(new MyTableModel(1, data, columnNames));	
		}
		else if(e.getSource() == calcularRaizesOctave) {
			try{	
				if(jopas == null)
					jopas = new Jopas(reference.getPanelRoots());
	
				MyTableModel dados = (MyTableModel) table.getModel();		
				double[] cR = new double[dados.getRowCount()];
				int index = dados.getRowCount() - 1;
				for(int i = 0; i < dados.getRowCount(); i++)
					cR[index--] = Double.parseDouble(dados.getValueAt(i, 1) + "");
	
				index = dados.getRowCount() - 1;
				double[] cI = new double[dados.getRowCount()];
				for(int i = 0; i < dados.getRowCount(); i++)
					cI[index--] = Double.parseDouble(dados.getValueAt(i, 2) + "");		
	
	
				Matrix mC = new Matrix (cR, cI, "coefic");
				jopas.Load(mC);		
				jopas.Execute("roots(coefic)");		
				//JOptionPane.showMessageDialog(null, "Ra�zes calculadas com sucesso e colocadas na aba \"Raizes\"");
	
			}
			catch(Exception exc) {}
		}
	}

}
