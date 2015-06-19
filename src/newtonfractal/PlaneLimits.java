package newtonfractal;


public class PlaneLimits {
 
	private float minReal;
	 
	private float maxReal;
	 
	private float minImaginary;
	 
	private float maxImaginary;


	public PlaneLimits(String minReal, String minImaginary, 
			 String maxReal, String maxImaginary) {
		this.minReal = Float.parseFloat(minReal.replace(",", "."));
		this.maxReal =  Float.parseFloat(maxReal.replace(",", "."));
		this.minImaginary = Float.parseFloat(minImaginary.replace(",", "."));
		this.maxImaginary = Float.parseFloat(maxImaginary.replace(",", "."));
	}

	public float getMinReal() {
		return minReal;
	}

	public void setMinReal(float minReal) {
		this.minReal = minReal;
	}

	public float getMaxReal() {
		return maxReal;
	}

	public void setMaxReal(float maxReal) {
		this.maxReal = maxReal;
	}

	public float getMinImaginary() {
		return minImaginary;
	}

	public void setMinImaginary(float minImaginary) {
		this.minImaginary = minImaginary;
	}

	public float getMaxImaginary() {
		return maxImaginary;
	}

	public void setMaxImaginary(float maxImaginary) {
		this.maxImaginary = maxImaginary;
	}
	
	
	
	
	
	 
}
 
