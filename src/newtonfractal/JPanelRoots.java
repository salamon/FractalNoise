package newtonfractal;




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class JPanelRoots extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private JTable table;
	private JSpinner numRaizes;
	private JButton sortearCores;	
		
	public JPanelRoots(NewtonFractalJFrame reference) {			
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel superior2 = new JPanel(new GridLayout(1, 2));		
		superior2.add((new JLabel("                                                                       ")));						
		add(superior2);
		
		numRaizes = new JSpinner(new SpinnerNumberModel(3, 1, 64, 1));
		JFormattedTextField tf =((JSpinner.DefaultEditor)numRaizes.getEditor()).getTextField();
		tf.setEditable(false);		
		tf.setBackground(Color.white);		
		numRaizes.getModel().addChangeListener(new ChangeListener() {
	         public void stateChanged(ChangeEvent e) {
	        	 try{
	        		 Object[] columnNames = {"Parte Real", "Parte Imaginária", "Multiplicidade", "Tolerância", "Cor"};
	        		 Object[][] data = generateData(Integer.parseInt(numRaizes.getValue()+""));
		             table.setModel(new MyTableModel(0, data, columnNames));
		             table.getColumnModel().getColumn(1).setPreferredWidth(table.getColumnModel().getColumn(1).getWidth() + 6);
	        	 }
	        	 catch (Exception e1) {
	        		 System.out.println(e1);
	        	 }
	         }
	    });

		setLayout(new FlowLayout(FlowLayout.LEFT));		
		JPanel superior = new JPanel(new GridLayout(1, 2));		
		superior.add((new JLabel("Número de raizes diferentes:                   ")));						
		superior.add(numRaizes);
		add(superior);		
		Object[] columnNames = {"Parte Real", "Parte Imaginária", "Multiplicidade", "Tolerância", "Cor"};
		Object[][] data = generateData(3);
					
		table = new JTable(new MyTableModel(0, data, columnNames));		
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//table.setFillsViewportHeight(true);	
		table.getColumnModel().getColumn(1).setPreferredWidth(table.getColumnModel().getColumn(1).getWidth() + 6);
		add(new JLabel("Informe as raizes do polinômio: "));
		add(scrollPane);	
		
		scrollPane.setPreferredSize(new Dimension(reference.getWidth()-18, reference.getHeight()-200));
		scrollPane.revalidate();
		
		table.setDefaultRenderer(Color.class, new ColorRenderer(true));
		table.setDefaultEditor(Color.class, new ColorEditor());
		
		JPanel southPanel = new JPanel();		
		sortearCores = new JButton("Sortear Cores!");
		sortearCores.addActionListener(this);			
		southPanel.add(sortearCores);
		add(southPanel, BorderLayout.SOUTH);	
		
	}
	
	private Object[][] generateData(int numLinhas) {
		Object[][] oldData = null;
		if(table != null)
			oldData = ((MyTableModel)table.getModel()).getData();
		Object[][] data = new Object[numLinhas][5];
		for(int i = 0; i < numLinhas; i++) {
			if(oldData != null && oldData.length > i) {
				data[i][0] = oldData[i][0];
				data[i][1] = oldData[i][1];
				data[i][2] = oldData[i][2];	
				data[i][3] = oldData[i][3];	
				data[i][4] = oldData[i][4];					
			}
			else {
				data[i][0] = new Double(0);
				data[i][1] = new Double(0);
				data[i][2] = new Integer(1);
				data[i][3] = new Double(0.1);
				data[i][4] = new Color(0, 0, 0);
			}
		}		
		return data;
	}
	
	private Object[][] generateData(int numLinhas, Color[] cores) {
		Object[][] oldData = null;
		if(table != null)
			oldData = ((MyTableModel)table.getModel()).getData();
		Object[][] data = new Object[numLinhas][5];
		for(int i = 0; i < numLinhas; i++) {
			if(oldData != null && oldData.length > i) {
				data[i][0] = oldData[i][0];
				data[i][1] = oldData[i][1];
				data[i][2] = oldData[i][2];	
				data[i][3] = oldData[i][3];	
				data[i][4] = cores[i];					
			}
			else {
				data[i][0] = new Double(0);
				data[i][1] = new Double(0);
				data[i][2] = new Integer(1);
				data[i][3] = new Double(0.1);
				data[i][4] = new Color(0, 0, 0);
			}
		}		
		return data;
	}
	
	
	
	public RootInformation[] getRootInformation() {
		RootInformation[] roots = new RootInformation[Integer.parseInt(numRaizes.getValue()+"")];
		ComplexNumber number;
		MyTableModel model = (MyTableModel) table.getModel();
		for(int i = 0; i < roots.length; i++) {
			number = new ComplexNumber(model.getValueAt(i, 0)+"", model.getValueAt(i, 1)+"");
			roots[i] = new RootInformation(number, (Color)model.getValueAt(i, 4), model.getValueAt(i, 3)+"", Integer.parseInt(model.getValueAt(i, 2)+""));
		}
		return roots;
	}

	public JTable getTable() {		
		return table;
	}

	public void setNumRaizes(int length) {		
		numRaizes.setValue(length);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color[] cores = new Color[Integer.parseInt(numRaizes.getValue() + "")];
		Random random = new Random(System.currentTimeMillis());
		for(int i = 0; i < cores.length; i++) {
			cores[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
		}
		
		Object[] columnNames = {"Parte Real", "Parte Imaginária", "Multiplicidade", "Tolerância", "Cor"};
		Object[][] data = generateData(Integer.parseInt(numRaizes.getValue()+""), cores);
        table.setModel(new MyTableModel(0, data, columnNames));
        table.getColumnModel().getColumn(1).setPreferredWidth(table.getColumnModel().getColumn(1).getWidth() + 6);
	}
	
	
	public int getNumRaizes(){
            return (Integer) numRaizes.getValue();
        }

}
