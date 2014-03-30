import javax.sound.midi.Receiver;
import javax.sound.midi.Track;

import jp.crestmuse.cmx.sound.SequenceGeneratable;


public class pilotSeq implements SequenceGeneratable {

	public boolean changeMeasure(Track arg0, long arg1) {
		return false;
	}

	public void sendInitializingMessages(Receiver arg0) {
		
	}
	
	
}
