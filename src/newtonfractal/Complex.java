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

public class Complex {

    /**
     * The values of Complex number
     */
    private double real = 0.0;
    private double img = 0.0;

    /**
     * Constructor with real and imaginary values.
     *
     * @param real double
     * @param img double
     */
    public Complex(double real, double img) {
        this.real = real;
        this.img = img;
    }

    /**
     * Returns the real value of the complex number
     *
     * @return double
     */
    public double getReal() {
        return this.real;
    }

    /**
     * Returns the imaginary value of the complex number
     *
     * @return double
     */
    public double getImg() {
        return this.img;
    }

    /**
     * Sets the real value of the complex number
     *
     * @param real double
     */
    public void setReal(double real) {
        this.real = real;
    }

    /**
     * Sets the imaginary value of the complex number
     *
     * @param img double
     */
    public void setImg(double img) {
        this.img = img;
    }
}
