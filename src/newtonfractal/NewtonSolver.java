package newtonfractal;

import java.awt.Color;


public class NewtonSolver {
 
	public static final int FATORADA = 1;
	public static final int HORNER = 0;
	public static final int POTENCIA = 2;
		
	public static Color solve(
			PolynomialFunction polynomial, ComplexNumber guess, long maxIterations,
			RootInformation[] rootInformation,	int evaluationForm, boolean comAlpha, boolean comDarker) 
	{		
		ComplexNumber xi = null;
		ComplexNumber anterior = guess;
		Color result = null;
		PolynomialFunction derivada = polynomial.derivative();
		ComplexNumber fxi_1, flinha_xi_1;
		for(int iteracao = 0; true; iteracao++)
		{
			//verifica se chegou na itera��o limite
			if(iteracao >= maxIterations)
				return Color.BLACK; //se sim retorna preto, indicando que nao convergiu
			
			//descobre xi
			//ComplexNumber xi = xi-1 - f(xi-1)/f'(xi-1)
			if(evaluationForm == FATORADA) {
				fxi_1 = polynomial.evaluateFatorada(anterior);
				flinha_xi_1 = derivada.evaluateFatorada(anterior);
			}
			else if(evaluationForm == POTENCIA) {
				fxi_1 = polynomial.evaluatePotencia(anterior);
				flinha_xi_1 = derivada.evaluatePotencia(anterior);
			}
			else { //por default resolve por HORNER
				fxi_1 = polynomial.evaluateHorner(anterior);
				flinha_xi_1 = derivada.evaluateHorner(anterior);
			} 
			xi = anterior.subtract(fxi_1.divide(flinha_xi_1));
						
			//verifica xi esta dentro da tolerancia de alguma raiz
			result = findColor(xi, rootInformation);
			if(result != null)
			{	
				//System.out.println("Achei uma cor correspondente [2] = " + result.toString());
				
				//com alpha de 50 a 255
				int alpha =(int) ((255 - 50)*(iteracao-1)/(maxIterations-1)) + 50;
				//return new Color(result.getRed(), result.getGreen(), result.getBlue(), alpha);
				
				
				int metadeIteracoes = (int) maxIterations/2;
				Color c = result;
				
										
				if(metadeIteracoes > iteracao)
				{ //deixar mais claro
					if(comDarker) {
						for(; metadeIteracoes > iteracao; iteracao++)
							c = c.brighter();
						result = c;
					}
					if(comAlpha) {
						result = new Color(result.getRed(), result.getGreen(), result.getBlue(), alpha);
						c = result;
					}
					return c;
				}
				else if(metadeIteracoes < iteracao)
				{ //deixar mais escuro
					if(comDarker) {
						for(; metadeIteracoes < iteracao; metadeIteracoes++)
							c = c.darker();
						result = c;
					}
					if(comAlpha) {
						result = new Color(result.getRed(), result.getGreen(), result.getBlue(), alpha);
						c = result;
					}
					return c;
				}
				
				
			}			
			anterior = xi;
		}	
	}
        
        public static ComplexNumber solveStep(
			PolynomialFunction polynomial, ComplexNumber guess, 
			RootInformation[] rootInformation,	int evaluationForm) 
	{		
		ComplexNumber xi = null;
		ComplexNumber anterior = guess;
		Color result = null;
		PolynomialFunction derivada = polynomial.derivative();
		ComplexNumber fxi_1, flinha_xi_1;
		
			//ComplexNumber xi = xi-1 - f(xi-1)/f'(xi-1)
			if(evaluationForm == FATORADA) {
				fxi_1 = polynomial.evaluateFatorada(anterior);
				flinha_xi_1 = derivada.evaluateFatorada(anterior);
			}
			else if(evaluationForm == POTENCIA) {
				fxi_1 = polynomial.evaluatePotencia(anterior);
				flinha_xi_1 = derivada.evaluatePotencia(anterior);
			}
			else { //por default resolve por HORNER
				fxi_1 = polynomial.evaluateHorner(anterior);
				flinha_xi_1 = derivada.evaluateHorner(anterior);
			} 
                    
		return 	 anterior.subtract(fxi_1.divide(flinha_xi_1));

	}
	 
	/**
	 * Retorna null se nao convergiu para nenhuma raiz
	 * @param xi
	 * @param rootInformation
	 * @return
	 */
	private static Color findColor(ComplexNumber xi, RootInformation[] rootInformation) {
		Color color = null;
		for(int i = 0; i < rootInformation.length; i++)
		{			
			if(rootInformation[i].contains(xi))
			{
				//System.out.println("Achei uma cor correspondente = " + rootInformation[i].getColor().toString());
				return rootInformation[i].getColor();				
			}
		}		
		//System.out.println("Estou retornando cor nula");
		return color;		
	}


//	public static ComplexNumber solve(PolynomialFunction p, ComplexNumber guess, long iterationLimit) {
//		return null;
//	}
	
}
 
