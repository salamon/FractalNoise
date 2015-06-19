package newtonfractal;



import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;




/**
 *
 * <p>Title: JOPAS</p>
 *
 * <p>Description: Java to Octave</p>
 *
 * <p>Copyright: (C) Copyright 2000-2005, by Object Refinery Limited and
 * Contributors.
 *
 * Project Info:  	http://jopas.sourceforge.net
 *                      http://www.eside.deusto.es/grupos/eside_pas
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 * </p>
 *
 * <p>Company: PAS - Universidad de Deusto</p>
 *
 * @author Oscar Lage Serrano - Javier Vicente S�ez
 * @version 1.01
 */
class Parser extends java.lang.Thread {
    private java.io.BufferedReader br;
    private String line;
    
    private JPanelRoots panelRoots;
  

    public Parser(java.io.BufferedReader is, JPanelRoots panelRoots) {
        br = is;    
        this.panelRoots = panelRoots;
    }

    public void run() {

        try {
            //jopas.LoadVersion();
        	LoadingOctave frame = new LoadingOctave();
            line = br.readLine();
            while (!line.contains("JOPASVERSION")) {           
                line = br.readLine();
                System.out.println(line);
                frame.addText(line);
            }            
            frame.fechar();
            while (true) {
                
                line = br.readLine(); 
                System.out.println("#" + line + "#");
                if (line == null){
                    System.exit(0);
                }
                if(line.trim().equals(""))
                	continue;
                if(line.contains("=")) {                	
                	continue;
                }                
                               
                //double vetReal[] = null, vetImag[] = null;               
                ArrayList<Complex> raizes = new ArrayList<Complex>();
                ArrayList<Complex> coeficientes = new ArrayList<Complex>();
                Complex complex = null;
                while(!line.equals("")) {                	
            		line = line.trim();
            		line = line.replace("  ", " ");
                	line = line.replace("  ", " ");
                	line = line.replace(" + ", "+");
                	line = line.replace("+ ", "+");
                	line = line.replace(" +", "+");
                	line = line.replace(" - ", "-");
                	line = line.replace(" -", "-");
                	line = line.replace("- ", "-");
                	line = line.replace("i-", "i -");
                	System.out.println("@" + line + "@");
                	
                	String vet[] = line.split(" ");
                	
                	if(vet.length == 1) { // se nao possui espaço entao eh a resposta
                					
            			line = vet[0];
            			if(line.contains("i")) { //se eh numero imaginario
            				line = line.replace("i", "");
                			if(line.contains("+")) {
                				vet = line.split("\\+");
                				complex = new Complex(Double.parseDouble(vet[0]),
                						Double.parseDouble(vet[1]));
                			}
                			else {
                				if(line.charAt(0) == '-') {
                					line =line.substring(1);
                					vet = line.split("-");
	                				complex = new Complex(-Double.parseDouble(vet[0]),
	                						-Double.parseDouble(vet[1]));
                				}
                				else {
                					vet = line.split("-");
	                				complex = new Complex(Double.parseDouble(vet[0]),
	                						-Double.parseDouble(vet[1]));
                				}
                			}
            			}
            			
            			else { //se nao eh numero imaginario
            				complex = new Complex(Double.parseDouble(line), 0);
            			}
            			
            			raizes.add(complex);      
                	}     
                	
                	else { //se possui espa�o � os coeficientes do polinomio
                		if(vet[0].contains("i")) { //se tem imaginario
                			for(int i = 0; i < vet.length; i++ ) {
                				String pos = vet[i];
                				pos = pos.replace("i", "");   
                				String vet2[];
                				if(pos.contains("+")) {
                					vet2 = pos.split("\\+");
                					complex = new Complex(Double.parseDouble(vet2[0]),
                    						Double.parseDouble(vet2[1]));
                				}
                				else {
                					//verificar se o primeiro tamb�m � negativo
                					if(pos.charAt(0) == '-') {
                						pos =pos.substring(1);
                    					vet2 = pos.split("-");
    	                				complex = new Complex(-Double.parseDouble(vet2[0]),
    	                						-Double.parseDouble(vet2[1]));
                    				}
                    				else {
                    					vet2 = pos.split("-");
    	                				complex = new Complex(Double.parseDouble(vet2[0]),
    	                						-Double.parseDouble(vet2[1]));
                    				}
                				}
                				coeficientes.add(complex);             				
                				
                			}
                		}
                		else { //se nao tem imaginario
                			/*for(int i = 0; i < vet.length; i++ ) {
                				complex = new Complex(Double.parseDouble(vet[i]), 0);
                				coeficientes.add(complex);
                			}*/
                		}
                	}
                	
                	line = br.readLine();
                	System.out.println("#" + line + "#");
                }
                               
               // System.out.println("Inicio raizes...");
                            
                String[][] dados = new String[raizes.size()][2];
                Complex c;
                for(int i = 0; i < raizes.size(); i++) {
                	c = raizes.get(i);
                //	System.out.println(c.getReal() + " " + c.getImg());
                	dados[i][0] = c.getReal() + "";
                	dados[i][1] = c.getImg() + "i";
                	
                }
               // System.out.println("Fim raizes...");
                
                //System.out.println("Inicio coeficientes...");
               // for(Complex z : coeficientes)
               // 	System.out.println(z.getReal() + " " + z.getImg());
               // System.out.println("Fim coeficientes...");
                
                if(raizes.size() > 0)
                {
                	//String[] colunas = {"Parte Real", "Parte Imagin�ria"};                   
                    //JTable tabela = new JTable(dados, colunas);
                	//new JDialogResposta(tabela);    
                	
                	
                	JTable tabelaRaizes = panelRoots.getTable();  
                	
                	Object[] columnNames = {"Parte Real", "Parte Imaginária", "Multiplicidade", "Tolerância", "Cor"};
                	panelRoots.setNumRaizes(dados.length);
                	Object[][] data = generateData(dados);	        		
	        		tabelaRaizes.setModel(new MyTableModel(0, data, columnNames));
	        		tabelaRaizes.getColumnModel().getColumn(1).setPreferredWidth(tabelaRaizes.getColumnModel().getColumn(1).getWidth() + 6);
	        		
	        		//JOptionPane.showMessageDialog(null, "Raizes calculadas com sucesso e colocadas na aba \"Raizes\"");
	        		
                }
                
                
                
                //if(vetReal !=null && vetImag != null)
               // 	JopasQueue.getJopasQueue().push(new Matrix(vetReal, vetImag, var));
                       
                
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private Object[][] generateData(String[][] dados) {
		Object[][] oldData = null;
		if(panelRoots.getTable() != null)
			oldData = ((MyTableModel)panelRoots.getTable().getModel()).getData();
		Object[][] data = new Object[dados.length][5];
		for(int i = 0; i < dados.length; i++) {
			if(oldData != null && oldData.length > i) {
				data[i][0] = Float.parseFloat(dados[i][0]);
				data[i][1] = Float.parseFloat(dados[i][1].replace("i", ""));
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

     /**     c�digo retirado
                line = line.substring(8);
                //System.out.println("**************************");
                //System.out.println("Var:" + line);
                var = line;
                line = br.readLine();
                if (line.contains("complex")) {

                    if (line.contains("scalar")) {
                        //System.out.println("Complex scalar type");
                        line = br.readLine();
                        //System.out.println("valor:" + line);
                        int divisor = line.indexOf(",");
                        String real = line.substring(1, divisor);
                        String img = line.substring(divisor + 1,
                                line.length() - 1);
                        double[][] realArray = { {Integer.parseInt(real)}
                        };
                        double[][] imgArray = { {Integer.parseInt(img)}
                        };
                        JopasQueue.getJopasQueue().push(new Matrix(realArray,
                                imgArray, var));
                    } else if (line.contains("matrix")){
                        //System.out.println("Complex matrix type");
                        line = br.readLine();
                        line = line.substring(8);
                        rows = Integer.parseInt(line);
                        //System.out.println("Rows:" + rows);

                        line = br.readLine();
                        line = line.substring(11);
                        columns = Integer.parseInt(line);
                        //System.out.println("Columns:" + columns);
                        double[][] realArray = new double[rows][columns];
                        double[][] imgArray = new double[rows][columns];
                        for (int i = 0; i < rows; i++) { //lee de fila en fila
                            //por cada fila
                            line = br.readLine();
                            //System.out.println(line);
                            StringTokenizer st = new StringTokenizer(line, " ");

                            for (int c = 0; c < columns; c++) {

                                line = st.nextToken();
                                int divisor = line.indexOf(",");
                                String real = line.substring(1, divisor);
                                String img = line.substring(divisor + 1,
                                        line.length() - 1);

                                realArray[i][c] = Double.valueOf(Double.
                                        parseDouble(real));
                                imgArray[i][c] = Double.valueOf(Double.
                                        parseDouble(img));
                            }

                        }
                        JopasQueue.getJopasQueue().push(new Matrix(realArray,
                                var));

                    }

                } else {
                    if (line.contains("scalar")) {
                        //System.out.println("Scalar type");
                        line = br.readLine();
                        //System.out.println("valor:" + line);
                        double[][] v1 = { {Double.parseDouble(line)}
                        };
                        JopasQueue.getJopasQueue().push(new Matrix(v1, var));
                    } else if (line.contains("matrix")){
                        //System.out.println("Matrix type");
                        line = br.readLine();
                        line = line.substring(8);
                        rows = Integer.parseInt(line);
                        //System.out.println("Rows:" + rows);

                        line = br.readLine();
                        line = line.substring(11);
                        columns = Integer.parseInt(line);
                        //System.out.println("Columns:" + columns);
                        double[][] v1 = new double[rows][columns];
                        for (int i = 0; i < rows; i++) { //lee de fila en fila
                            //por cada fila
                            line = br.readLine();
                            //System.out.println(line);
                            StringTokenizer st = new StringTokenizer(line, " ");

                            for (int c = 0; c < columns; c++) {
                                v1[i][c] = Double.valueOf(Double.parseDouble(st.
                                        nextToken()));
                            }

                        }
                        JopasQueue.getJopasQueue().push(new Matrix(v1, var));

                    }

                }
                //System.out.println();
                //System.out.println();
                line = br.readLine();
            
           
         */
}
