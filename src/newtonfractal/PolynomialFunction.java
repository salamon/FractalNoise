package newtonfractal;



public class PolynomialFunction{
 
	private ComplexNumber[] coefficients;
	private PolynomialFunction derivative = null;
	 
	
	/**
     * The coefficients of the polynomial, ordered by degree -- i.e.,
     * coefficients[0] is the constant term and coefficients[n] is the
     * coefficient of x^n where n is the degree of the polynomial.
    */
	public PolynomialFunction(ComplexNumber[] coefficients) {
		this.coefficients = coefficients;	 
	}
	 		 
	public int getDegree() {
		return coefficients.length - 1;
	}
	 	
	//adaptado da classe PolynomialFunction.java do apache commons math
	public PolynomialFunction derivative() {
		if(derivative != null)
			return derivative;
		int n = coefficients.length;
	       if (n < 1) {
	       	throw new IllegalArgumentException("Erro. Polin�mio sem coeficientes.");
	       }
	       if (n == 1) {
	    	   ComplexNumber[] coeficientes = new ComplexNumber[]{new ComplexNumber("0", "0")};
	           return new PolynomialFunction( coeficientes );
	       }
	       ComplexNumber[] result = new ComplexNumber[n - 1];
	       ComplexNumber complexI = null;
	       for (int i = n - 1; i  > 0; i--) {
	    	   complexI = new ComplexNumber(i, 0);
	           result[i - 1] = coefficients[i].multiply(complexI);
	           // result[i - 1] = i * coefficients[i];
	       }	    
		derivative = new PolynomialFunction(result);
		return derivative;
	}
	
	//M�todo de Horner:   2x3 + 3x2 + 4x + 5  =  ((2x + 3)x + 4)x + 5)
	//adaptado da classe PolynomialFunction.java do apache commons math
	public ComplexNumber evaluateHorner(ComplexNumber argument_Z) {		
		int n = coefficients.length;		
        if (n < 1) {
            throw new IllegalArgumentException("Erro. Polin�mio sem coeficientes.");
        }
        ComplexNumber result = coefficients[n - 1];
        for (int j = n - 2; j >= 0; j--) {
        	//result = argument * result + coefficients[j];
            result = (argument_Z.multiply(result)).add(coefficients[j]);
        }
        return result;		
	}
	 
	/**
	 * 
	 * @param z
	 * @param mathContext
	 * @return
	 */
	public ComplexNumber evaluatePotencia(ComplexNumber z) {
		return null;
	}
	 
	/**
	 * 
	 * @param z
	 * @param mathContext
	 * @return
	 */
	public ComplexNumber evaluateFatorada(ComplexNumber z) {
		return null;
	}
	 
}
 
