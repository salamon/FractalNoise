package newtonfractal;

import java.math.BigDecimal;

public class ComplexNumber {
 
	private float real;
	 
	private float imaginary;
	 
	public ComplexNumber(BigDecimal xu, BigDecimal yu) {
		this.real = xu.floatValue();
		this.imaginary = yu.floatValue();
	}
	
	public ComplexNumber(float xu, float yu) {
		this.real = xu;
		this.imaginary = yu;
	}
	
	public ComplexNumber(String real, String imaginary) {
		this.real = Float.parseFloat(real);
		this.imaginary = Float.parseFloat(imaginary);
	}
	 
	public ComplexNumber add(ComplexNumber aNumber) {
		float realPart = real + aNumber.getReal();
		float imaginaryPart = imaginary + aNumber.getImaginary();
		return new ComplexNumber(realPart, imaginaryPart);
	}
	 
	public ComplexNumber subtract(ComplexNumber aNumber) {
		float realPart = real - aNumber.getReal();
		float imaginaryPart = imaginary - aNumber.getImaginary();
		return new ComplexNumber(realPart, imaginaryPart);
	}
	 
	public ComplexNumber multiply(ComplexNumber aNumber) {
		float ac = real * aNumber.getReal();
		float bd = imaginary * aNumber.getImaginary();		
		float bc = imaginary * aNumber.getReal();
		float ad = real * aNumber.getImaginary();		
		return new ComplexNumber(ac - bd, bc + ad);
	}
	 
	public ComplexNumber divide(ComplexNumber aNumber) {
		try{
			float ac = real * aNumber.getReal();
			float bd = imaginary * aNumber.getImaginary();		
			float bc = imaginary * aNumber.getReal();
			float ad = real  * aNumber.getImaginary();		
			float c2 = aNumber.getReal() * aNumber.getReal();		
			float d2 = aNumber.getImaginary() * aNumber.getImaginary();	
			float realPart = (ac + bd) / (c2 + d2);
			float imaginaryPart = (bc - ad) / (c2 + d2);
			
			return new ComplexNumber(realPart, imaginaryPart);
		}
		catch(Exception e){
			return new ComplexNumber("0","0");
		}
	}
	 
	 
	public float getReal() {
		return real;
	}
	 
	public float getImaginary() {
		return imaginary;
	}
	 
}
 
