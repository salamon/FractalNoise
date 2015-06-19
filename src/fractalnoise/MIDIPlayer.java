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

/**
 *
 * @author Nestor
 */
public class MIDIPlayer {
    
    private Sequence midi_file;
    private Sequencer sequencer;
    
    public MIDIPlayer(String file)
    {
        try {
            midi_file = MidiSystem.getSequence(new File(file));  
            sequencer = MidiSystem.getSequencer();  
            sequencer.setTempoInBPM(120000); // 120 batidas por minuto (tempo utilizado em ms)
            sequencer.open();
            sequencer.setSequence(midi_file);   
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void play(){
        sequencer.start();  
        try {
            Thread.sleep(sequencer.getTickLength());
        } catch (InterruptedException ex) {
            Logger.getLogger(MIDIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        sequencer.stop();
    }
    
    public void stop(){
        sequencer.stop();  
    }
}
