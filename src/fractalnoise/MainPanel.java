/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractalnoise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import newtonfractal.ComplexNumber;
import newtonfractal.JPanelFractal;
import newtonfractal.PlaneLimits;

/**
 *
 * @author Nestor
 */
public class MainPanel extends javax.swing.JFrame implements WindowListener{
    /* Botoes de controle */
    private javax.swing.JButton btnAxes;
    private javax.swing.JButton btnStep;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSolve;
    private javax.swing.JButton btnSolveTogether;
    private javax.swing.JButton btnAnalyse1;
    private javax.swing.JButton btnAnalyse2;
    private javax.swing.JPanel canvas;
    private javax.swing.JCheckBox chkMultiValue;
    private javax.swing.JCheckBox chkClicknPlay;
    private javax.swing.JCheckBox chkDrawLines;
    private javax.swing.JCheckBox chkMultiInstruments;
    private javax.swing.JCheckBox chkChords;
    private JSlider solveVelocity;
            
    /* Dimensoes dos menus */
    private int border_height_size = 120;
    private int border_width_size = 35;
    
    /* Dimensoes do plano */
    private float plan_min_real;
    private float plan_max_real;
    private float plan_min_img; 
    private float plan_max_img;
    
    //largura do plano
    private float xumax_menos_xumin;
    private float yumax_menos_yumin;

    private float xtmax = 0;
    private float ytmax = 0;
    
    /* Ultimos valores clicados */
    private int clicked_x = 0;
    private int clicked_y = 0;
    private ComplexNumber lastPoint;
    private ArrayList<ComplexNumber> clickedPoints;
    
    private JPanelFractal panelFractal;
    boolean axes = false;
    boolean repaint = false;
    
    private int count_it = 1;
    private float erroMin = (float) 0.0001;
    
    private Music music;
    private final int notes_per_line = 12; /* de uma nota ateh sua nova ocorrencia uma oitava acima*/
    private int first_note = 21;
    private int last_note = 104;
    private int qty_notes = (last_note - first_note) + 1; 
    
    /* Sequenciador .mid */
    private String output_file = "Teste.mid"; /* arquivo para sequenciar o som na analyse 2*/
    private MIDISequencer mid_seq;
    private MIDIPlayer mid_player;
    
    private ComplexNumber temp = null; /* Ajuste quando repetido o primeiro valor no step */
    
    private float salt = (float) 0.1; /* 10^-1 -  valor a ser deslocado quando inicializada uma analise em ponto que não converge */
    private float salt_acum = 0;
    
    public MainPanel(int w, int h, final JPanelFractal panelFractal) {
        this.panelFractal = panelFractal;
        initComponents();
        
        /*Inicializa player */
        music = new Music();
        
        /*Seta tamano janela */        
        setCanvasBorders(w, h);
        panelFractal.killThreadComputaCores();
        
        PlaneLimits planeLimits = panelFractal.getReference().getPlaneLimits();	
        /* Largura do plano */
        xumax_menos_xumin = planeLimits.getMaxReal() - planeLimits.getMinReal();
        yumax_menos_yumin = planeLimits.getMaxImaginary() - planeLimits.getMinImaginary();
        /* Limites do plano */
        plan_min_real = planeLimits.getMinReal();
        plan_max_real = planeLimits.getMaxReal();
        plan_min_img = planeLimits.getMinImaginary();
        plan_max_img = planeLimits.getMaxImaginary();
        
        /* Atualiza limites da tela de acordo com canvas */
        ytmax = canvas.getHeight();
        xtmax = canvas.getWidth();
        
        clickedPoints = new ArrayList<ComplexNumber>();
        
        
        setTitle("FractalNoise - v0.2.1");
        
        canvas.setMinimumSize(new Dimension());
        canvas.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                btnStep.setEnabled(true); /* habilitar o solve by step */
                btnSolve.setEnabled(true); /* habilitar o solve all */
                btnSolveTogether.setEnabled(true); /* habilitar o solve together */
                
                clicked_x = e.getX();
                clicked_y = e.getY();
                System.out.println("(" + clicked_x + ", " + e.getY() + ")");
                System.out.println(count_it + " - " + "(" + convertScreenPointToPlanePointX(clicked_x) + ", "
                        + convertScreenPointToPlanePointY(clicked_y) + ")");
                lastPoint = new ComplexNumber(convertScreenPointToPlanePointX(clicked_x), convertScreenPointToPlanePointY(clicked_y));

                if(chkMultiValue.isSelected())
                {
                   clickedPoints.add(lastPoint);
                   repaint = false;
                }
                else
                {
                    canvas.repaint();
                    clickedPoints.clear();
                    clickedPoints.add(lastPoint);
                    count_it = 1;    
                    repaint = true;
                }
                
                if(chkClicknPlay.isSelected()) {
                    int nota = 0;
                    if(chkChords.isSelected()){
                        int quadrant = getQuadrant(lastPoint.getReal(), lastPoint.getImaginary());
                        nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary(), quadrant);
                    }else{
                        nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary());
                    }
                    System.out.println("Tocar nota: " + nota);
                    music.playNote(nota);
                } 
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                panelFractal.drawPoint(lastPoint); /* marcar ponto clicado */ 
                if(axes && repaint) btnAxesActionPerformed(null); /* se tinha eixo, desenha novamente */
            }
        }); 
    }
    
    private void setCanvasBorders(int w, int h)
    {
        canvas.setSize(w, h);
        this.setSize(w + border_width_size, h + border_height_size); /* Somado do tamanho dos menus e borda lateral */
    }
   
    public float convertScreenPointToPlanePointX(float x)
    {
       float xu = x * xumax_menos_xumin / xtmax + plan_min_real;
       return xu;
    }
    
    public float convertScreenPointToPlanePointY(float y)
    {
        float yu = (y - ytmax ) * yumax_menos_yumin / (-ytmax) + plan_min_img;
        return yu;
    }
    
    public int getQuadrant(float x, float y)
    {
        if(x > 0 && y > 0 ) return 1;
        if(x < 0 && y > 0 ) return 2;
        if(x < 0 && y < 0 ) return 3;
        if(x > 0 && y < 0 ) return 4;
        
        else return 0;
    }

    public int getNoteByPlanePoint(float x, float y)
    {
        return  (int) (convertPlanePointToNoteX(x) + (convertPlanePointToNoteY(y) * notes_per_line));
    }
    
    /* Usa a definicao do quadrante para permitir acordes */
    public int getNoteByPlanePoint(float x, float y, int quadrante)
    {
        return  (int) (convertPlanePointToNoteX(x, quadrante) + (convertPlanePointToNoteY(y) * notes_per_line));
    }
    
    /* Retorna a nota */
    public double convertPlanePointToNoteX(float x, int quadrante)
    {
        /* Ideia: notas deslocadas pra formar um acorde */
        float prop_x = notes_per_line / plan_max_real;
        float first_note_q = first_note;
        switch(quadrante) {
            case 1: first_note_q = 21;
                break;
            case 2: first_note_q = 25;
                break;
            case 3: first_note_q = 28;
                break;
            case 4: first_note_q = 32;
                break;
            default: first_note_q = 21;
                break;
        }
        
        float ret = (Math.abs(x) * prop_x) + first_note_q;
        return Math.floor(ret) ;
    }
    
    /* Retorna a nota */
    public double convertPlanePointToNoteX(float x)
    {
        /* Ideia: a mesma ocorrencia de notas em todos os quadrantes */
        float prop_x = notes_per_line / plan_max_real;
        float ret = (Math.abs(x) * prop_x) + first_note;
        return Math.floor(ret) ;
    }
    
    /* Retorna a oitava */
    public double convertPlanePointToNoteY(float y)
    {
        /* Ideia: a mesma ocorrencia de notas em todos os quadrantes */
        int qty_oitavas = qty_notes / notes_per_line;
        float prop_y =  qty_oitavas / plan_max_img;
        
        float ret =  (Math.abs(y) * prop_y);
        return Math.floor(ret) ;
    }
    
    public double getDistanceBetweenPoint(ComplexNumber a, ComplexNumber b)
    {
        float x1 = a.getReal();
        float y1 = a.getImaginary();
        float x2 = b.getReal();
        float y2 = b.getImaginary();

        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow( (y1 - y2), 2));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() 
    {
        btnStep = new javax.swing.JButton();
        btnAxes = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnSolve = new javax.swing.JButton();
        btnSolveTogether = new javax.swing.JButton();
        btnAnalyse1 = new javax.swing.JButton();
        btnAnalyse2 = new javax.swing.JButton();
        chkMultiValue = new javax.swing.JCheckBox();
        chkClicknPlay = new javax.swing.JCheckBox();
        chkDrawLines = new javax.swing.JCheckBox();
        chkMultiInstruments = new javax.swing.JCheckBox();
        chkChords = new javax.swing.JCheckBox();
        solveVelocity = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        canvas = panelFractal;

        /* JSlider setSize is broken */
        JPanel panelSlider = new JPanel();
        panelSlider.add(solveVelocity);
        
        
        addWindowListener(this);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnStep.setText("Step");
        btnStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStepActionPerformed(evt);
            }
        });
        btnStep.setEnabled(false);

        btnAxes.setText("Draw Axes");
        btnAxes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAxesActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        chkMultiValue.setText("Allow multi selection"); 
        chkClicknPlay.setText("Click n Play"); 
        chkDrawLines.setText("Draw lines"); 
        chkMultiInstruments.setText("Allow multi instruments"); 
        chkChords.setText("Allow Chords"); 
        
        btnSolve.setText("Solve Selected");
        btnSolve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolveActionPerformed(evt);
            }
        });
        btnSolve.setEnabled(false);
        
        btnSolveTogether.setText("Solve Together");
        btnSolveTogether.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolveTogetherActionPerformed(evt, false);
            }
        });
        btnSolveTogether.setEnabled(false);
        
        btnAnalyse1.setText("Analyse 1");
        btnAnalyse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalyse1ActionPerformed(evt);
            }
        });
        
        btnAnalyse2.setText("Analyse 2");
        btnAnalyse2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalyse2ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAxes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSolve)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkMultiValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkClicknPlay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelSlider)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSolveTogether)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnalyse1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnalyse2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkDrawLines)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkMultiInstruments)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkChords)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    )
            )
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)                    
                    .addComponent(btnAxes)
                    .addComponent(btnSolve)    
                    .addComponent(btnStep)
                    .addComponent(chkMultiValue)
                    .addComponent(chkClicknPlay)
                    .addComponent(panelSlider)
                 )
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSolveTogether)                    
                    .addComponent(btnAnalyse1)  
                    .addComponent(btnAnalyse2)  
                    .addComponent(chkDrawLines)  
                    .addComponent(chkMultiInstruments)  
                    .addComponent(chkChords)  
                 )
                .addGap(18, 18, 18)
                .addComponent(canvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
            )
        );

        pack();
    }

    private void btnStepActionPerformed(java.awt.event.ActionEvent evt) {
        int nota = 0;
        ComplexNumber old_value;
        
        old_value = lastPoint;
        if(count_it == 1) temp = lastPoint; /* Ajuste da reta quando eh necessario repetir o primeiro ponto */
        
        if(chkClicknPlay.isSelected()) /* Começa na segunda nota/segundo ponto */
        {
            count_it++;
            lastPoint = panelFractal.solveStep(lastPoint);
            panelFractal.drawPoint(lastPoint);
            if(chkDrawLines.isSelected()) panelFractal.drawArrow(old_value, lastPoint); /* Desenha linha */
            System.out.println(count_it + " - (" + lastPoint.getReal() + ", " + lastPoint.getImaginary() + ")");
            
            if(chkChords.isSelected()){
                int quadrant = getQuadrant(lastPoint.getReal(), lastPoint.getImaginary());
                nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary(), quadrant);
            }
            else{
                nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary());
            }
            System.out.println("Tocar nota: " + nota);
            music.playNote(nota); 
        }
        else /* repete o primeiro ponto */
        {           
            panelFractal.drawPoint(lastPoint);
            if(chkDrawLines.isSelected() && count_it > 1) panelFractal.drawArrow(temp, lastPoint); /* Desenha linha */
            System.out.println(count_it + " - (" + lastPoint.getReal() + ", " + lastPoint.getImaginary() + ")");
            if(chkChords.isSelected()){
                int quadrant = getQuadrant(lastPoint.getReal(), lastPoint.getImaginary());
                nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary(), quadrant);
            }else{
                nota = getNoteByPlanePoint(lastPoint.getReal(), lastPoint.getImaginary());
            }
            System.out.println("Tocar nota: " + nota);
            music.playNote(nota); 
            
            temp = lastPoint;
            lastPoint = panelFractal.solveStep(lastPoint);
            count_it++;
        }
        
        /* Se diff menor que o erro mínimo, bloqueia prox passo */
        if((Math.abs(lastPoint.getReal() - old_value.getReal()) <= erroMin) && (Math.abs(lastPoint.getImaginary() - old_value.getImaginary()) <= erroMin)){
             /* Limpa */
            clickedPoints.clear();
            btnSolve.setEnabled(false);
            btnSolveTogether.setEnabled(false);
            btnStep.setEnabled(false);
            count_it = 1;
        }
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        canvas.repaint();
        axes = false;
        
        /* Limpa */
        clickedPoints.clear();
        btnSolve.setEnabled(false);
        btnStep.setEnabled(false);
        btnSolveTogether.setEnabled(false);
    }

    private void btnAxesActionPerformed(java.awt.event.ActionEvent evt) {
        panelFractal.drawAxes();
        axes = true;
    }
    
    private void btnSolveActionPerformed(ActionEvent evt) { /* Solve Selected */
        ComplexNumber solving, old;
        int note, time;
        for(int i = 0; i < clickedPoints.size(); i++)
        {
            /* Sempre toca a primeira nota repetida, pra caso tenha vários pontos. */
            solving = clickedPoints.get(i);
            
            count_it = 1;
            panelFractal.drawPoint(solving, Color.RED);
            /* Resolve até que a diferença entre as raizes encontradas seja < 0.0001 (erro < 0.0001) */
            do
            {
                old = solving;
                solving = panelFractal.solveStep(solving);
                
                if(chkChords.isSelected()){
                    int quadrant = getQuadrant(old.getReal(), old.getImaginary());
                    note = getNoteByPlanePoint(old.getReal(), old.getImaginary(), quadrant);
                }else{
                    note = getNoteByPlanePoint(old.getReal(), old.getImaginary());
                }
                
                /* Nota dura a distancia dela e a próxima */
                time = (int) (getDistanceBetweenPoint(solving, old) * (10000 / solveVelocity.getValue())); /* Distancia 0,5  = 5 segundos / velocidade  // TESTE */
                System.out.println("Tocar nota: " + note + " por " + (time+1) + " u.t.");
                music.playTimedNote(note, time); /* Toca a nota old */
                
                panelFractal.drawPoint(solving, Color.RED); /* Desenha a nota nova */
                System.out.println("Point Number: " + (i+1) + " Iteracao:" + count_it + " - (" + solving.getReal() + ", " + solving.getImaginary() + ")");
                if(chkDrawLines.isSelected()) panelFractal.drawArrow(old, solving); /* Desenha linha */
                
                count_it++;
            }while((Math.abs(solving.getReal() - old.getReal()) > erroMin) || (Math.abs(solving.getImaginary() - old.getImaginary()) > erroMin));
            
            
            if(chkChords.isSelected()){
                int quadrant = getQuadrant(solving.getReal(), solving.getImaginary());
                note = getNoteByPlanePoint(solving.getReal(), solving.getImaginary(), quadrant);
            }else{
                note = getNoteByPlanePoint(solving.getReal(), solving.getImaginary());
            }
            if(i == clickedPoints.size()-1) /* Se é a ultima nota do vetor - nao tem mais sequencias esperando */
                music.playTimedNote(note, 1); /* Toca a ultima nota na menor unidade de tempo - referente ao erroMin*/
            else
                music.playTimedNote(note, 2000); /* Toca com um pqno intervalo pra separar a nova sequencia */
        }
        
        /* Limpa depois que resolveu */
        clickedPoints.clear();
        btnSolve.setEnabled(false);
        btnSolveTogether.setEnabled(false);
        btnStep.setEnabled(false);
    }
    
    private void btnSolveTogetherActionPerformed(ActionEvent evt, boolean analyse1) {
        ComplexNumber solving, old;
        int max_iterations = (int) panelFractal.getMaxIteration();
        int note, time, column;
        int inst_num = music.getCurrentInstrumentNumber();
        ComplexNumber matrix[][] = new ComplexNumber[clickedPoints.size()][max_iterations]; 
        
        /* Para cada ponto clicado, resolve até o fim */
        for(int i = 0; i < clickedPoints.size(); i++)
        {
            column = 0;
            solving = clickedPoints.get(i);
            matrix[i][column] = solving;
            
            do
            {
                column ++;
                
                /* Checa se um ponto inicial da abordagem não é um valor que não converge */
                if(column >= max_iterations)
                {
                    if(analyse1) {
                        int response = JOptionPane.showConfirmDialog(this, "Este fractal não pode ser analizado por esta abordagem."
                                + "\nHá um ponto selecionado em que o método não converge com o número especificado de iterações. "
                                + "\nDeseja deslocar em " + salt + " os pontos e continuar? (já deslocado " + salt_acum + ")");

                        switch(response){
                            case 0:
                                shiftPoints(); /* Se evento eh de uma analyse one, vai shiftar os 4 pontos */
                                btnSolveTogetherActionPerformed(null, analyse1);
                                return;
                            default:
                                //this.dispose();
                                clickedPoints.clear();
                                btnSolve.setEnabled(false);
                                btnStep.setEnabled(false);
                                btnSolveTogether.setEnabled(false);
                                salt_acum = 0;
                                return;
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Ao menos um dos pontos selecionados está em uma região de não convergência\n"
                                + "com o número de iterações especificado. \n"
                                + "Selecione novos pontos e reinicie a análise", "FractalNoise", 1);
                        clickedPoints.clear();
                        canvas.repaint();
                        btnSolve.setEnabled(false);
                        btnStep.setEnabled(false);
                        btnSolveTogether.setEnabled(false);
                        return;
                    }
                }
                
                old = solving;
                solving = panelFractal.solveStep(solving);
                matrix[i][column] = solving;
                
            }while((Math.abs(solving.getReal() - old.getReal()) > erroMin) || (Math.abs(solving.getImaginary() - old.getImaginary()) > erroMin));
            /* Resolve até que a diferença entre as raizes encontradas seja < 0.0001 (erro < 0.0001) */
        }
        
        /* Lê linha por linha 
        System.out.println("Linha por linha:");
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] != null){
                    System.out.print("m[" + i + "," + j + "] = ");
                    System.out.print("(" + matrix[i][j].getReal() + ", " + matrix[i][j].getImaginary() + ")\n");
                }
            }
        }*/
        
        /* Lê coluna por coluna - invertidos os indexadores da matriz */
        System.out.println("Coluna por coluna:");
        for (int i = 0; i < max_iterations-1; i++)
        {
            for (int j = 0; j < clickedPoints.size(); j++){
                if(matrix[j][i] != null){
                    System.out.print("m[" + j + "," + i + "] = "
                                        + "(" + matrix[j][i].getReal() + ", " 
                                        + matrix[j][i].getImaginary() + ")\n");
                
                    if(chkChords.isSelected()){ //allow chords
                        int quadrant = getQuadrant(matrix[j][i].getReal(), matrix[j][i].getImaginary());
                        note = getNoteByPlanePoint(matrix[j][i].getReal(), matrix[j][i].getImaginary(), quadrant);
                    }else{
                        note = getNoteByPlanePoint(matrix[j][i].getReal(), matrix[j][i].getImaginary());
                    }

                    if(matrix[j][i+1] != null){
                        /* Nota dura a distancia dela e a próxima raiz (do mesmo ponto inicial) */
                        /* Distancia 0,5  = 5 segundos / velocidade  */   
                        time = (int) (getDistanceBetweenPoint(matrix[j][i+1], matrix[j][i]) * (10000 / solveVelocity.getValue())); 
                    }
                    else 
                    {
                        time = 1; /* Toca a ultima nota na menor unidade de tempo - referente ao erroMin*/
                    }


                    panelFractal.drawPoint(matrix[j][i], Color.RED); /* Desenha a nota nova */
                    if(i > 0 && chkDrawLines.isSelected()) panelFractal.drawArrow(matrix[j][i-1], matrix[j][i]); /* Desenha linha */

                    if(chkMultiInstruments.isSelected()) 
                    {
                        inst_num = j <= music.getQtyInstruments() ? j * 5 : 0; 
                        music.setInstrument(inst_num);
                    }

                    System.out.println("Tocar nota: " + note + " por " + (time+1) + " u.t." + " Instrumento: " + music.getCurrentInstrument().getName() + " (" + inst_num + ")");
                    music.playTimedNote(note, time); /* Toca a nota old */
                }
            }
        }
        
        /* Limpa depois que resolveu */
        clickedPoints.clear();
        btnSolve.setEnabled(false);
        btnStep.setEnabled(false);
        btnSolveTogether.setEnabled(false);
        music.setInstrument(0);
    }
    
    private void btnAnalyse1ActionPerformed(ActionEvent evt) {
        //btnResetActionPerformed(evt);
        //canvas.repaint();
        
        createInitialPointsToAnalyse();
        btnSolveTogetherActionPerformed(evt, true);
        
    }
    
    public void createInitialPointsToAnalyse(){
        /* Começa resolucao pelo ponto central de cada quadrante */
        
        clickedPoints.clear();
        clickedPoints.add(new ComplexNumber((float) plan_max_real/2 , (float) plan_max_img/2));
        clickedPoints.add(new ComplexNumber((float) plan_min_real/2 , (float) plan_max_img/2));
        clickedPoints.add(new ComplexNumber((float) plan_min_real/2 , (float) plan_min_img/2));
        clickedPoints.add(new ComplexNumber((float) plan_max_real/2 , (float) plan_min_img/2));
        salt_acum = 0;
    }
    
    private void btnAnalyse2ActionPerformed(ActionEvent evt) {
        createInitialPointsToAnalyse();
        
        mid_seq = new MIDISequencer();
        
        int ok = 0;
        while(ok == 0){
            ok = generateMIDIFile(mid_seq);
        }
        
        if(ok == 1){
            /* Exporta arquivo externo */
            mid_seq.exportMIDIFile(output_file);

            mid_player = new MIDIPlayer(output_file);
            mid_player.play();
        
            /* Limpa depois que resolveu */
            clickedPoints.clear();
            btnSolve.setEnabled(false);
            btnStep.setEnabled(false);
            btnSolveTogether.setEnabled(false);
        }
    }
    
    public int generateMIDIFile(MIDISequencer mid_seq){
        ComplexNumber solving, old;
        int max_iterations = (int) panelFractal.getMaxIteration();
        int note, time, column;
        ComplexNumber matrix[][] = new ComplexNumber[clickedPoints.size()][max_iterations]; 
        ArrayList<Integer> current_time = new ArrayList<Integer>(clickedPoints.size());
        
        for(int i = 0; i < clickedPoints.size(); i++)
        {
            column = 0;
            solving = clickedPoints.get(i);
            matrix[i][column] = solving;
            
            do
            {
                column ++;
                if(column >= max_iterations)
                {
                    int response = JOptionPane.showConfirmDialog(this, "Este fractal não pode ser analizado por esta abordagem."
                            + "\nHá um ponto selecionado em que o método não converge com o número especificado de iterações. "
                            + "\nDeseja deslocar em " + salt + " os pontos e continuar? (já deslocado " + salt_acum + ")");
                    
                    switch(response){
                        case 0:
                            shiftPoints();
                            return 0;
                        default:
                            //this.dispose();
                            clickedPoints.clear();
                            btnSolve.setEnabled(false);
                            btnStep.setEnabled(false);
                            btnSolveTogether.setEnabled(false);
                            salt_acum = 0;
                            return 2;
                    }
                    
                }
                
                old = solving;
                solving = panelFractal.solveStep(solving);
                matrix[i][column] = solving;
            }while((Math.abs(solving.getReal() - old.getReal()) > erroMin) || (Math.abs(solving.getImaginary() - old.getImaginary()) > erroMin));
         
            current_time.add(i, 0); /* Adiciona zero no primeiro valor de cada ponto */
        }
        
        for (int i = 0; i < max_iterations-1; i++)
        {
            for (int j = 0; j < clickedPoints.size(); j++){
                if(matrix[j][i] != null){

                    if(chkChords.isSelected()){ //allow chords
                        int quadrant = getQuadrant(matrix[j][i].getReal(), matrix[j][i].getImaginary());
                        note = getNoteByPlanePoint(matrix[j][i].getReal(), matrix[j][i].getImaginary(), quadrant);
                    }else{
                        note = getNoteByPlanePoint(matrix[j][i].getReal(), matrix[j][i].getImaginary());
                    }

                    if(matrix[j][i+1] != null){  
                        time = (int) (getDistanceBetweenPoint(matrix[j][i+1], matrix[j][i]) * (10000 / solveVelocity.getValue())); 
                    }
                    else{ time = 1; }

                    panelFractal.drawPoint(matrix[j][i], Color.RED); /* Desenha a nota nova */
                    if(i > 0 && chkDrawLines.isSelected()) panelFractal.drawArrow(matrix[j][i-1], matrix[j][i]); /* Desenha linha */

                    if(chkMultiInstruments.isSelected()) 
                    {
                        mid_seq.addSequence(mid_seq.setInstrument((j) * 5));
                        //mid_seq.addSequence(mid_seq.setInstrument(30));
                        //@TODO Se sobrar tempo, adicionar multi tracks
                    }
                    
                    mid_seq.addSequence(mid_seq.noteOn(note, current_time.get(j)));
                    current_time.set(j, (current_time.get(j) + time)); /* Atualiza tempo acumulado */
                    mid_seq.addSequence(mid_seq.noteOff(note, current_time.get(j)));
                    if(time > 10) //@TODO Remover - Usado pra imprimir só qdo uma nota durar mais q 10ms
                        System.out.println("Tocar nota: " + note + " de " + (current_time.get(j) - time) + " a " + current_time.get(j) + " com (" + j + ")");
                    //if(chkMultiInstruments.isSelected())  System.out.print(" com (" + (j + 1) * 15 + ")");
                    /* Teoricamente, ultimo ponto não é tocado, o tempo é mto pequeno. o menor possível */
                }
            }
        } 
        return 1;
    }
    
    private void shiftPoints(){        
        salt_acum += salt;
        
        clickedPoints.clear();
        clickedPoints.add(new ComplexNumber((float) plan_max_real/2 + salt_acum, (float) plan_max_img/2 + salt_acum));
        clickedPoints.add(new ComplexNumber((float) plan_min_real/2 - salt_acum, (float) plan_max_img/2 + salt_acum));
        clickedPoints.add(new ComplexNumber((float) plan_min_real/2 - salt_acum, (float) plan_min_img/2 - salt_acum));
        clickedPoints.add(new ComplexNumber((float) plan_max_real/2 + salt_acum, (float) plan_min_img/2 - salt_acum));
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        music.stop();
        if(mid_player != null) mid_player.stop();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
    
}
