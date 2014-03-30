import java.io.*;

import javax.sound.midi.*;

import jp.crestmuse.cmx.amusaj.filewrappers.*;
import jp.crestmuse.cmx.amusaj.sp.MidiEventWithTicktime;
import jp.crestmuse.cmx.amusaj.sp.SPModule;
import jp.crestmuse.cmx.amusaj.sp.TimeSeriesCompatible;

public class aaa extends SPModule {
	  private int ticksPerBeat;
	  private String filename;
	  private Sequence seq;
	  private Track track;

	  public aaa(String filename, int ticksPerBeat) {
		  try {
	      seq = new Sequence(Sequence.PPQ, ticksPerBeat);
	      track = seq.createTrack();
	    } catch (InvalidMidiDataException e) {
	      e.printStackTrace();
	      throw new IllegalArgumentException();
	    }
	    this.filename = filename;
	    this.ticksPerBeat = ticksPerBeat;
	  }

	  public void execute(Object[] src, TimeSeriesCompatible[] dest)
	    throws InterruptedException {
	    MidiEventWithTicktime e = (MidiEventWithTicktime)src[0];
	    byte[] b = e.getMessageInByteArray();
	    System.err.println(b[0] + " " + b[1]);
	    track.add(new MidiEvent(e.getMessage(), e.music_position));
	  }
      public void stop() {
    	  try {
    		  System.out.println("wirte");
    		  System.out.println(seq);
    		  Track[] t = seq.getTracks();
    		  t[0].get(0);

            int a = MidiSystem.write(seq, 0, new File(filename));
            System.out.println(a);

	    } catch (IOException e) {
    		  System.out.println("wirte");
	    	e.printStackTrace();

	    }
	  }
	  public Class[] getInputClasses() {
	    return new Class[] { MidiEventWithTicktime.class };
	  }
	  public Class[] getOutputClasses() {
	    return new Class[0];
	  }
}
