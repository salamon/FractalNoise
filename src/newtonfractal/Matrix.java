package newtonfractal;




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


public class Matrix {
    private int f; //filas
    private int c; //columnas
    private Complex[][] x;
    private String name = null;

    /**
     * It creates a void matrix with "f" rows and "c"  columns
     *
     * @param f int
     * @param c int
     */
    public Matrix(int f, int c) {
        this.f = f;
        this.c = c;
        x = new Complex[f][c];
        for (int i = 0; i < this.f; i++) {
            for (int j = 0; j < c; j++) {
                x[i][j] = new Complex(0.0, 0.0);
            }

        }
    }

    /**
     * It creates a complex matrix with "real" and "img"(imaginary) numbers called "name"
     *
     * @param real double[][]
     * @param img double[][]
     * @param name String
     */
    public Matrix(double[][] real, double[][] img, String name) {

        f = real.length;
        c = real[0].length;
        x = new Complex[f][c];
        for (int i = 0; i < this.f; i++) {
            for (int j = 0; j < c; j++) {
                x[i][j] = new Complex(real[i][j], img[i][j]);
            }
        }

        this.name = name;
    }

    /**
     * It creates a matrix with "real" numbers called "name"
     *
     * @param real double[][]
     * @param name String
     */
    public Matrix(double[][] real, String name) {
        f = real.length;
        c = real[0].length;
        x = new Complex[f][c];
        for (int i = 0; i < this.f; i++) {
            for (int j = 0; j < c; j++) {
                x[i][j] = new Complex(real[i][j], 0.0);
            }
        }

        this.name = name;
    }

    /**
     * It creates a vector Matrix of "real" called "name"
     *
     * @param real double[]
     * @param name String
     */
    public Matrix(double[] real, String name) {
        f = 1;
        c = real.length;
        x = new Complex[f][c];
        for (int i = 0; i < this.c; i++) {
            x[0][i] = new Complex(real[i], 0.0);
        }
        this.name = name;
    }
    /**
     * It creates a complex vector of "real" and "img" numbers called "name"
     *
     * @param real double[]
     * @param img double[]
     * @param name String
     */
    public Matrix(double[] real, double[] img, String name) {
        f = 1;
        c = real.length;
        x = new Complex[f][c];
        for (int i = 0; i < this.c; i++) {
            x[0][i] = new Complex(real[i],img[i]);
        }
        this.name = name;
    }


    /**
     * It creates a single number matrix of "num" called "name"
     *
     * @param num double
     * @param name String
     */
    public Matrix(double num, String name) {
        f = 1;
        c = 1;
        x = new Complex[1][1];
        x[0][0] = new Complex(num, 0.0);
        this.name = name;
    }

    /**
     * It implementes toString() function to print Matrix
     *
     * @return String
     */
    public String toString() {
        String texto = name + " = [";
        //texto+=(double)Math.round(1000*x[0])/1000;
        //texto += x[0][0];
        for (int i = 0; i < f; i++) {
            //texto+=" "+(double)Math.round(1000*x[i])/1000;
            for (int j = 0; j < c; j++) {
                texto += " " + x[i][j].getReal() + " + " + x[i][j].getImg() +
                        "i";
            }
            texto += " ;";

        }
        texto = texto.substring(0, texto.length() - 2);
        texto += "]";
        return texto;
    }

    /**
     * It gets the number of Rows of the matrix
     *
     * @return int
     */
    public int getRows() {
        return f;
    }

    /**
     * It gets the number of Columns of the matrix
     *
     * @return int
     */
    public int getColumns() {
        return c;
    }

    /**
     * It gets the real at ("row","column")
     *
     * @param row int
     * @param column int
     * @return double
     */
    public double getRealAt(int row, int column) {
        return x[row][column].getReal();
    }

    /**
     * It gets the imaginary at ("row","column")
     *
     * @param row int
     * @param column int
     * @return double
     */
    public double getImgAt(int row, int column) {
        return x[row][column].getImg();
    }

    /**
     * It returns true if the matrix is an scalar number
     *
     * @return boolean
     */
    public boolean isScalar() {
        if ((f == 1) && (c == 1)) {
            return true;
        }
        return false;
    }

    /**
     * It returns true if the matrix is a vector
     *
     * @return boolean
     */
    public boolean isVector() {
        if ((f == 1) && (c != 1)) {
            return true;
        }
        return false;
    }

    /**
     * It returns true if it's a standar Matrix
     *
     * @return boolean
     */
    public boolean isMatrix() {
        if ((!isScalar()) && (!isVector())) {
            return true;
        }
        return false;
    }

    /**
     * It returns true if it's a Column Matrix
     *
     * @return boolean
     */
    public boolean isColumnVector() {
        if ((f != 1) && (c == 1)) {
            return true;
        }
        return false;
    }


}
