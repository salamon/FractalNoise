package newtonfractal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingWorker;


public class JPanelFractal extends JPanel{


	private static final long serialVersionUID = 1L;
	private NewtonFractalJFrame reference;
	private Color[][] cores;
	private int i;
	private ThreadComputaCores thread;
	//private ExecutorService executor = Executors.newFixedThreadPool(1);

	public JPanelFractal(NewtonFractalJFrame reference) {
		this.reference = reference;		
		//computaCores();			
	}

	private class ThreadComputaCores extends SwingWorker<Color[][], Integer>{

		JProgressBarSetValue bar;
		//long time_inicio;
		
		@Override
		protected Color[][] doInBackground() throws Exception {
			//time_inicio = System.currentTimeMillis();
			bar = new JProgressBarSetValue();
			cores = new Color[reference.getPanelExtra().getLargura() + 1] [reference.getPanelExtra().getAltura() + 1];
			//GregorianCalendar inicial = (GregorianCalendar) GregorianCalendar.getInstance();
			RootInformation[] rootInfo = reference.getRootInformation();
			PolynomialFunction polynomial = reference.getPolynomial();
			long maxIterations = reference.getMaxIterations();
			int evaluationForm = reference.getEvaluationForm();
			PlaneLimits planeLimits = reference.getPlaneLimits();
			int width = reference.getPanelExtra().getLargura();
			int height = reference.getPanelExtra().getAltura();			
			boolean alpha = reference.getAlpha();
			boolean darker = reference.getDarker();

			//JProgressBarSetValue progressBar = new JProgressBarSetValue(0, width, this);
			//executor.execute(progressBar);

			//largura do plano
			float xumax_menos_xumin = planeLimits.getMaxReal() - planeLimits.getMinReal();
			//altura do plano, de acordo com o slide de mapeamento universo -> tela
			float yumax_menos_yumin = planeLimits.getMaxImaginary() - planeLimits.getMinImaginary();

			float xtmax = width;
			float ytmax = height;				
			float xt, yt, xu, yu;

			
			//para cada pixel
			for(i = 0; i <= width; i++)
			{
				xt = i;
				//xu = (ponto da tela)*(largura do plano)/(largura da tela) + menor ponto do plano
				xu = xt * xumax_menos_xumin / xtmax + planeLimits.getMinReal();

				for(int k = 0; k <= height; k++)
				{ 
					//para o pixel i,k (linha i, coluna k) da tela descobrir qual ponto do plano complexo				
					yt = k;				

					//yu = 
					yu = (yt - ytmax ) * yumax_menos_yumin / (-ytmax) + planeLimits.getMinImaginary();

					//System.out.println("pixel ("+ i + ", " + k + ") corresponde no plano ao ponto (" + xu.toPlainString() + ", " + yu.toPlainString() + ")");

					//pegar o ponto xu,yu e aplicar newton e ver para qual raiz converge
					Color pixelColor = NewtonSolver.solve(polynomial,
							new ComplexNumber(xu, yu), maxIterations, rootInfo, evaluationForm, alpha, darker);

					cores[i][k] = pixelColor;

				}
				if(i % 20 == 0) {
					//System.out.println("N�mero de colunas prontas: " + i + " de " + width);
					//progressBar.setValueBar(i);
					publish( i*100/width );
				}

			
			}

			//System.out.println("\n\n\nHora:\n");
			//System.out.println(inicial);
			//System.out.println(GregorianCalendar.getInstance());
			bar.close();
			return cores;					
		}


		@Override
		protected void done() {				
			//long time_fim = System.currentTimeMillis();
			//System.out.println("\n\n\n\n");
			//System.out.println("Tempo de execu��o = " + (time_fim - time_inicio)/1000f + "  segundos ");
			//System.out.println("\n\n\n\n");
			
			super.done();
			if(!this.isCancelled()) {
				Graphics g = getGraphics();
				int width = reference.getPanelExtra().getLargura();
				int height = reference.getPanelExtra().getAltura();	
	
				Graphics2D g2d = (Graphics2D) g;
				//para cada pixel
				for(int i = 0; i <= width; i++)
				{			
					for(int k = 0; k <= height; k++)
					{ 			
						g2d.setColor(cores[i][k]);
						g2d.drawRect(i, k, 1, 1);
	
					}
				}				
			}

		}

		@Override
		protected void process(List<Integer> chunks) {			
			super.process(chunks);
			int value = chunks.get(chunks.size() - 1);
			//System.out.println("Atualizando: " + value);
			bar.setValueBar( value );

		}

	}



	public void createThreadComputaCores() {				
		thread = new ThreadComputaCores();			
		thread.execute();		
	}



	public void killThreadComputaCores() {
		try{ 
			thread.cancel(true);		
		}
		catch(Exception e) {}
	}


	@Override
	public void paint(Graphics g) {		
		super.paint(g);		
		if(cores != null) {
			int width = reference.getPanelExtra().getLargura();
			int height = reference.getPanelExtra().getAltura();	
	
			Graphics2D g2d = (Graphics2D) g;
			//para cada pixel
			for(int i = 0; i <= width; i++)
			{			
				for(int k = 0; k <= height; k++)
				{ 			
					g2d.setColor(cores[i][k]);
					g2d.drawRect(i, k, 1, 1);
	
				}
			}			
		}
	}
        
        public NewtonFractalJFrame getReference(){
            return reference;
        }
        
        public void drawAxes(){
            int width = reference.getPanelExtra().getLargura();
            int height = reference.getPanelExtra().getAltura();	
            
            Graphics g = getGraphics();
            Graphics2D g2d = (Graphics2D) g;

            for(int i = 0; i <= width; i++)
            {			
                    for(int k = 0; k <= height; k++)
                    { 			
                            if(i == width/2 || k == height/2){
                                g2d.setColor(Color.red);
                                g2d.drawRect(i, k, 1, 1);
                            }
                    }
            }		
        }
        
        public void drawPoint(ComplexNumber comp){
            drawPoint(comp, Color.BLACK);
        }
        
        public void drawPoint(ComplexNumber comp, Color color){
            PlaneLimits planeLimits = reference.getPlaneLimits();		
            Graphics g = getGraphics();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
           
            float xumax_menos_xumin = planeLimits.getMaxReal() - planeLimits.getMinReal();
            float yumax_menos_yumin = planeLimits.getMaxImaginary() - planeLimits.getMinImaginary();
            float xtmax = reference.getPanelExtra().getLargura();
            float ytmax = reference.getPanelExtra().getAltura();				
            
            float screen_point_x = (comp.getReal() - planeLimits.getMinReal()) / (xumax_menos_xumin / xtmax);
            //float screen_point_y = (comp.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / ytmax);
            float screen_point_y = ((comp.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / -ytmax)) + ytmax;
            
            //Ponto cruz
            g2d.drawRect((int) screen_point_x, (int) screen_point_y, 1, 1); /* Ponto central */
            g2d.drawRect((int) screen_point_x+1, (int) screen_point_y, 1, 1);
            g2d.drawRect((int) screen_point_x, (int) screen_point_y+1, 1, 1);
            g2d.drawRect((int) screen_point_x-1, (int) screen_point_y, 1, 1);
            g2d.drawRect((int) screen_point_x, (int) screen_point_y-1, 1, 1);
            
            System.out.println("\t Screen point - x: " + screen_point_x + ", y:" + screen_point_y);
        }
        
        public ComplexNumber solveStep(ComplexNumber comp){
            RootInformation[] rootInfo = reference.getRootInformation();
            PolynomialFunction polynomial = reference.getPolynomial();
            int evaluationForm = reference.getEvaluationForm();
           
                        
            return NewtonSolver.solveStep(polynomial, comp, rootInfo, evaluationForm);
        
        }
        
        public long getMaxIteration()
        {
            return reference.getMaxIterations();
        }
        
        public void drawLine(ComplexNumber start, ComplexNumber end){
            PlaneLimits planeLimits = reference.getPlaneLimits();		
            Graphics g = getGraphics();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
           
            float xumax_menos_xumin = planeLimits.getMaxReal() - planeLimits.getMinReal();
            float yumax_menos_yumin = planeLimits.getMaxImaginary() - planeLimits.getMinImaginary();
            float xtmax = reference.getPanelExtra().getLargura();
            float ytmax = reference.getPanelExtra().getAltura();				
            
            int screen_point_x_ini = (int) ((start.getReal() - planeLimits.getMinReal()) / (xumax_menos_xumin / xtmax));
            int screen_point_y_ini = (int) (((start.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / -ytmax)) + ytmax);
            int screen_point_x_end = (int) ((end.getReal() - planeLimits.getMinReal()) / (xumax_menos_xumin / xtmax));
            int screen_point_y_end = (int) (((end.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / -ytmax)) + ytmax);
            
            g2d.drawLine(screen_point_x_ini, screen_point_y_ini, screen_point_x_end, screen_point_y_end); /* x_ini, y_ini, x_fim, y_fim */
        }
        
        public void drawArrow(ComplexNumber start, ComplexNumber end) {
            /* Adapted from here:  http://stackoverflow.com/questions/4112701/drawing-a-line-with-arrow-in-java */
            PlaneLimits planeLimits = reference.getPlaneLimits();	    
            Graphics g = getGraphics();
            Graphics2D g2d = (Graphics2D) g;
            int ARR_SIZE = 4;
                
            float xumax_menos_xumin = planeLimits.getMaxReal() - planeLimits.getMinReal();
            float yumax_menos_yumin = planeLimits.getMaxImaginary() - planeLimits.getMinImaginary();
            float xtmax = reference.getPanelExtra().getLargura();
            float ytmax = reference.getPanelExtra().getAltura();				
            
            int screen_point_x_ini = (int) ((start.getReal() - planeLimits.getMinReal()) / (xumax_menos_xumin / xtmax));
            int screen_point_y_ini = (int) (((start.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / -ytmax)) + ytmax);
            int screen_point_x_end = (int) ((end.getReal() - planeLimits.getMinReal()) / (xumax_menos_xumin / xtmax));
            int screen_point_y_end = (int) (((end.getImaginary() - planeLimits.getMinImaginary()) / (yumax_menos_yumin / -ytmax)) + ytmax);

            double dx = screen_point_x_end - screen_point_x_ini, dy = screen_point_y_end - screen_point_y_ini;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(screen_point_x_ini, screen_point_y_ini);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g2d.transform(at);

            g2d.drawLine(0, 0, len, 0);
            g2d.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                            new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
            }


        public int getQtyRoots(){
            return reference.getPanelRoots().getNumRaizes();
        }
}

