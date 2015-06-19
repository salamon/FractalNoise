/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractalnoise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

/**
 *
 * @author Nestor
 */
public class MIDISequencer {
    
    private int velocity = 100;
    private int channel = 0;
    private Sequence sequence = null;
    private Track track;
    
    public MIDISequencer()
    {   
        try {
            sequence = new Sequence(Sequence.PPQ, 1);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(MIDISequencer.class.getName()).log(Level.SEVERE, null, ex);
        }
        track = sequence.createTrack();
    }
    
    public MidiEvent noteOn(int note, int tick){
        ShortMessage message = new ShortMessage();
        try{
                message.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
        }catch (InvalidMidiDataException e){

        }
        return new MidiEvent(message,  tick);
    }
    
    public MidiEvent setInstrument(int instrument){
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
        } catch (InvalidMidiDataException ex) {

        }
        return new MidiEvent(message,  0);
    }
    
    public MidiEvent noteOff(int note, int tick){
        ShortMessage message = new ShortMessage();
        try{
                message.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
        }catch (InvalidMidiDataException e){

        }
        return new MidiEvent(message,  tick);
    }
    
    public void addSequence(MidiEvent evt){
        track.add(evt);
    }
    
    public void exportMIDIFile(String output){
        //output = "Teste.mid";
        File outputFile = new File(output);
        
        try {
            MidiSystem.write(sequence, 0, outputFile);
        } catch (IOException ex) {
            Logger.getLogger(MIDISequencer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    /* Sempre utilizar no fim de um arquivo midi (?)
     track.add(createNoteOnEvent(0, last_time));
     track.add(createNoteOffEvent(0, last_time));
     */
}
