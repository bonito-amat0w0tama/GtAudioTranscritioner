import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import jp.crestmuse.cmx.amusaj.sp.MidiEventWithTicktime;

public class TransData extends MidiEventWithTicktime {
	private double tVal;

    public TransData(MidiMessage msg, long tick, long position, double val) {
		super(msg, tick, position);
		this.tVal = val;
	}

    public static TransData createTransData(long position, int nn, int vel, double tVal, boolean on) {
    	int ch = 0;
    	MidiEventWithTicktime midi = null;
    	if (on) {
            midi = MidiEventWithTicktime.createNoteOnEvent(position, ch, nn, vel); 
    	} else { 
            midi = MidiEventWithTicktime.createNoteOffEvent(position, ch, nn, vel); 
    	}
    	TransData dest = new TransData(midi.getMessage(), midi.getTick(), midi.music_position, tVal);
    	return dest;
    }

    public double gettVal() {
    	return this.tVal;
    }
    private void settVal(double val) {
    	this.tVal = val;
    }

    public static void main(String[] args) {
    	TransData t = TransData.createTransData(0, 60, 120, 0.9, true);
    	System.out.println(t);
    }
}
