/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fractalnoise;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Nestor
 */
public final class Music {

    //private int octave = 2;
    private Synthesizer syn;
    private MidiChannel[] mid;
    private Instrument[] instruments;
    private int current_instr;
    private int channel;
    private int velocity = 100;
    private Soundbank soundbank;

    public Music(){
        try {
            syn = MidiSystem.getSynthesizer();
            syn.open();
            mid = syn.getChannels();
        } catch (MidiUnavailableException ex) {
            popUpError("We've got a Problem getting the synthesizer!");
        }
            
        String filePath = "soundbank-min.gm";
        File f = new File(filePath);
        
        /*try {
            soundbank = MidiSystem.getSoundbank(f);
        } catch (InvalidMidiDataException ex) {
            popUpError("We've got a problem getting the soundbank!");
        } catch (IOException ex) {
            popUpError("We've got a problem oppening the soundbank file!");
        }*/
       
        soundbank = syn.getDefaultSoundbank();
        instruments = soundbank.getInstruments();
        
        if(soundbank == null || instruments == null) popUpError("Could not load the instruments.");
        
        setInstrument(0); /* Default PIANO */
        //setInstrument(30); /* Default Distortion Guitar */
        setChannel(0);
    }

    public void setInstrument(int i){
        current_instr = i;
        //syn.loadInstrument(instruments[current_instr]);
        mid[channel].programChange(instruments[current_instr].getPatch().getBank(), instruments[current_instr].getPatch().getProgram());
    }
    
    public Instrument getCurrentInstrument(){
        return instruments[current_instr];
    }
    public int getCurrentInstrumentNumber(){
        return current_instr;
    }

    public void setChannel(int i){
        channel = i;
    }

    public void playNote(int note){
        mid[channel].noteOn(note, velocity); /* channel default */
    }

    
    public void playNote(int note, int ch){
        if(ch == 0)
            mid[channel].noteOn(note, velocity); /* channel default */
        else
            mid[ch].noteOn(note, velocity);
    }

    public void stop(){
        for(int i = 0; i < mid.length; i++){
            mid[i].allNotesOff();
        }
    }
    
    public void playTimedNote(int note, int duration){
        mid[channel].noteOn(note, velocity);
        try
        {
            Thread.sleep(duration);
        }
        catch (InterruptedException e)
        {
            
        }
        mid[channel].noteOff(note);
    }
    
    public int getQtyInstruments(){
        return 190;  /* Até 190 sao os que tocam no soundbank minimal (padrao sdk 1.2.2+*/
    }
    
    private void popUpError(String msg)
    {
        //System.out.println(msg);
        JOptionPane.showMessageDialog(null, msg + "\nThe Application will be closed", "Fractal Trace Error", 0);
        System.exit(0);
    }
   
}
