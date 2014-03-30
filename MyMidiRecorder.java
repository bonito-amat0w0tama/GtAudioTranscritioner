import java.io.*;

import javax.sound.midi.*;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import jp.crestmuse.cmx.amusaj.filewrappers.*;
import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.filewrappers.SCCDataSet.Part;

public class MyMidiRecorder extends SPModule {

//  private int ticksPerBeat;
//  private String filename;
//  private Sequence seq;
//  private Track track1;
//  private Track track2;

//    dataSet.addHeaderElement(0, "TEMPO", "120");
    
 
    SCCDataSet dataSet; 
    Part part1;


    public MyMidiRecorder(String filename, int ticksPerBeat) {
        SCCDataSet dataSet = new SCCDataSet(480);
        Part part1 = dataSet.addPart(1, 10, 1, 100);
        dataSet.addHeaderElement(0, "TEMPO", "120");
    }

      public void execute(Object[] src, TimeSeriesCompatible[] dest)
        throws InterruptedException {
    	  	MidiEventWithTicktime[] in = (MidiEventWithTicktime[])src[0]; 
    	  	MidiEventWithTicktime onMidi = in[0];
    	  	MidiEventWithTicktime offMidi= in[1];
			part1.addNoteElement((int)onMidi.music_position, (int)offMidi.music_position, onMidi.data1(), 100, 0);
      }

  public void stop() {
		try {
			SCCXMLWrapper scc = dataSet.toWrapper();
			String filePath = "xml/tes.xml";
			scc.finalizeDocument();
			scc.writefile(filePath);
			MIDIXMLWrapper midi = scc.toMIDIXML();
			midi.writefileAsSMF("smf/countter.mid");
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ファイルの書き出し");
		} catch (SAXException e) {
			e.printStackTrace();
		}
  }

  public Class[] getInputClasses() {
    return new Class[] { 
    		MidiEventWithTicktime[].class,
        };
  }

  public Class[] getOutputClasses() {
    return new Class[0];
  }
}
              