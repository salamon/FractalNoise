package newtonfractal;

import java.awt.Color;

public class RootInformation {
 
	private ComplexNumber root;
	 
	private Color color;
	 
	private float tolerance;
	 
	private int multiplicity;
	

	public RootInformation(ComplexNumber root, Color color,
			String tolerance, int multiplicity) {		
		this.root = root;
		this.color = color;
		this.tolerance = Float.parseFloat(tolerance);
		this.multiplicity = multiplicity;		
	}
	
	/**
	 * Este m�todo deve verificar se a raiz est� contida dentro do n�mero complexo passado por par�metro,
	 * de acordo com a tolerancia da raiz
	 * @param xi
	 * @return
	 */
	public boolean contains(ComplexNumber xi) {
		float minReal = root.getReal() - tolerance;
		float maxReal = root.getReal() + tolerance;
		float minImaginary = root.getImaginary() - tolerance;
		float maxImaginary = root.getImaginary() + tolerance;
		
		//verifica se a parte real de xi est� dentro do intervalo da raiz com a toler�ncia
		//se xi.real > minReal e xi.real < maxReal
		if(xi.getReal() > minReal) { //se x1 � maior que o minReal		
			if(xi.getReal() < maxReal) { //se x1 � menor que o maxReal
			//se xi.imag > minImag e xi.imag < maxImag
				if(xi.getImaginary() > minImaginary) { //se x1 � maior que o minImaginary
					if(xi.getImaginary() < maxImaginary) {  //se x1 � menor que o maxImaginary
						return true;				
					}
				}
			}
		}
		return false;
	}
	

	public ComplexNumber getRoot() {
		return root;
	}

	public void setRoot(ComplexNumber root) {
		this.root = root;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

	public int getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}
	
	 
}
 
