package newtonfractal;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JOptionPane;




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
public class Jopas {
    private PrintWriter pw = null;
    private Parser parser = null;
    //private Plotter plotter = null;

    /**
     * The constructor of the class, it connect and run Octave.
     */
    public Jopas(JPanelRoots panelRoots) throws Exception {

        Process p = null;
        Runtime r = Runtime.getRuntime();
        try {
            //ResourceBundle jopasProperties = ResourceBundle.getBundle("JOPAS");
            //String[] cmdAry = {jopasProperties.getString("Octave_path")};
        	
            String octave_path = JOptionPane.showInputDialog("Informe o caminho completo do octave.exe");
            //String octave_path = "C:\\Octave\\Octave3.4.3_gcc4.5.2\\Octave3.4.3_gcc4.5.2\\bin\\octave-3.4.3.exe"; 
        	String[] cmdAry = {octave_path};
        	
            p = r.exec(cmdAry);

        } catch (java.lang.Exception e) {
            //System.err.println("Octave's execution ERROR:");
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Octave's execution ERROR", JOptionPane.ERROR_MESSAGE);
            throw new Exception(e);
        }
        

        try {
        	BufferedReader is = new BufferedReader(new InputStreamReader(p.
                    getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(p.
                    getOutputStream())));
            parser = new Parser(is, panelRoots);
            this.LoadVersion();
            parser.start();
        }

        catch (Exception e) {
            //System.out.println("Error writing to output stream " + e);
            //e.printStackTrace();
        	JOptionPane.showMessageDialog(null, e.toString(), "Octave's execution ERROR", JOptionPane.ERROR_MESSAGE);
        	throw new Exception(e);
        }

       

    }

    /**
     * Load the Matrix in Octave
     *
     * @param vector Matrix
     */
    public synchronized void Load(Matrix vector) {
        pw.println(vector.toString() + "");
        //pw.println(vector.toString() + ";");
        pw.flush();
        System.out.println("Loading: " + vector);
    }

    /**
     * Load the variable "name" with de number "num"
     *
     * @param num double
     * @param name String
     */
    public synchronized void Load(double num, String name) {
        Matrix matrix = new Matrix(num, name);
        this.Load(matrix);
    }

    /**
     * Return the value of the variable "var" like a Matrix
     *
     * @param var String
     * @return Matrix
     */
  //  public synchronized Matrix Save(String var) {
   //     pw.println("save -ascii - " + var + " ");
   //     pw.flush();
   //     System.out.println("Saving: " + var);
   //     return JopasQueue.getJopasQueue().pull();
   // }

    /**
     * Executes the code "code" in Octave
     *
     * @param code String
     */
    public synchronized void Execute(String code) {
        pw.println(code + "");
        pw.flush();
        System.out.println("Executing: " + code);
    }

    /**
     * Private function to initializate the parser
     */
    public synchronized void LoadVersion() {
        pw.println("JOPASVERSION = '1.01'");
        pw.flush();       
    }

    /**
     * Function to draw a new form that plots the variable "var"
     *
     * @param var String
     */
    public void plot(String var) {
     //   new Plot(var, "PLOTTER", this);
    }

}
