import java.util.*;


import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import jp.crestmuse.cmx.amusaj.sp.MidiEventWithTicktime;
import jp.crestmuse.cmx.amusaj.sp.SPModule;
import jp.crestmuse.cmx.amusaj.sp.TimeSeriesCompatible;
import jp.crestmuse.cmx.filewrappers.MusicXMLWrapper.Note;
import jp.crestmuse.cmx.math.ComplexArray;
import jp.crestmuse.cmx.amusaj.sp.*;
import static java.lang.Math.*;

public class AudioTranscriptioner extends SPModule{
	private RealMatrix[] Wp = new RealMatrix[6];
	private boolean[] isOn = new boolean[128]; 
	private long[] mpLen = new long[128];
	private MidiEventWithTicktime[] onMidis = new MidiEventWithTicktime[128]; 
	private Note[] onNotes = new Note[128];
	static final int CH = 0;

//	static final double THRESHOLD = 0.8;
//	static final double THRESHOLD = 0.25;
	static final double THRESHOLD = 0.6;
//	static final double THRESHOLD = 0.2;
//	static final double THRESHOLD = 1.2;
	static final double[] valTHs = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2};
	
//	static final long lenTH = 100;
	static final long lenTH = 0;
	
    static final String[] st1 = {"E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#"};
    static final String[] st2 = {"B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#"};
    static final String[] st3 = {"G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#"};
    static final String[] st4 = {"D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#",};
    static final String[] st5 = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    static final String[] st6 = {"E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#"};

    final int[] st1mn = this.createNoteNumList(1); 
    final int[] st2mn = this.createNoteNumList(2); 
    final int[] st3mn = this.createNoteNumList(3); 
    final int[] st4mn = this.createNoteNumList(4); 
    final int[] st5mn = this.createNoteNumList(5); 
    final int[] st6mn = this.createNoteNumList(6); 
    final int[][] stmn = {st1mn, st2mn, st3mn, st4mn, st5mn, st6mn};

	public void execute(Object[] src, TimeSeriesCompatible[] dest) 
			throws InterruptedException {
	    ComplexArray spec = (ComplexArray)src[0];

	    long music_position = 0;
	    if (spec instanceof ComplexArrayWithTicktime) {
	    	music_position = ((ComplexArrayWithTicktime)spec).music_position;
//	    	System.out.println("mp:" + music_position);
	    }

	    // パワースペクトルグラムに変換
	    // RealMatrix型に変換するために、double[][]型に
	    double[][] powsec = new double[spec.length()][1];
	    for (int i = 0; i < spec.length(); i++) {
            powsec[i][0] = (double)sqrt(spec.getReal(i) * spec.getReal(i) +
                        spec.getImag(i) * spec.getImag(i));
	    }

	    // 入力された演奏
	    RealMatrix in = MatrixUtils.createRealMatrix(powsec);

//	    RealVector actVec6 = this.Wp[6].multiply(in).getColumnVector(0);
	    Note[] notelist = this.transcription(in);

	    for (int i = 0; i < 128; i++) {
	    	if (!isOn[i] && notelist[i] != null) {
	    		MidiEventWithTicktime midi = MidiEventWithTicktime.createNoteOnEvent(music_position, CH, i, notelist[i].vel);

	    		System.out.println("=================");
	    		System.out.println("NN:" + midi.data1());
	    		System.out.println("val:" + notelist[i].val);
	    		System.out.println("count" + notelist[i].counter);
	    		System.out.println("mp:" + midi.music_position);
	    		System.out.println("=================");

//	    		dest[0].add(midi);
	    		isOn[i] = true;
	    		mpLen[i] = midi.music_position;
	    		onMidis[i] = midi;
	    		onNotes[i] = notelist[i];

	    	} else if (isOn[i] && notelist[i] == null) {
//	    		MidiEventWithTicktime onMidi = MidiEventWithTicktime.createNoteOnEvent(mpLen[i], CH, i, 100);
	    		MidiEventWithTicktime onMidi = onMidis[i]; 
	    		MidiEventWithTicktime offMidi= MidiEventWithTicktime.createNoteOffEvent(music_position, CH, i, 0);
	    		
	    		Note onNote = onNotes[i];
//	    		long len = offMidi.music_position - mpLen[i];
	    		long len = offMidi.music_position - onMidi.music_position;

	    		if (len > lenTH) {
                    System.out.println("-----------------");
                    System.out.println("NN:" + offMidi.data1());
                    System.out.println("mp:" + offMidi.music_position);
                    System.out.println("val:" + onNote.val);
                    System.out.println("count" + onNote.counter);
                    System.out.println("onMp:" + onMidi.music_position);
                    System.out.println("mp_length:" + len);
                    System.out.println("-----------------");

                    MidiEventWithTicktime[] midiSet = {onMidi, offMidi};
                    dest[0].add(onMidi);
                    dest[0].add(offMidi);
//                    dest[0].add(midiSet);
	    		}
	    		isOn[i] = false;
	    		mpLen[i] = 0;
	    		onNotes[i] = null;
	    	}
        }
	}

	private class Note {
		int noteNum;
		int vel;
		double val ;
		int counter;

		private Note(int noteNum, int vel, double val, int counter) {
			this.noteNum = noteNum;
			this.vel = vel;
			this.val = val;
			this.counter = counter;
		}
	}


//	private Note[] transcription(RealVector vec) {
////        String[] noteList = {"c", "d", "e", "f", "g", "a", "b", "c"};
////        String[] AmNoteList = {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "a4", "b4", "c4", "d4", "e4", "f4", "g4", "a5", "b5", "c5"};
//        int[] AmNote = {45, 47, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72};
//
//        Note[] notelist = new Note[128];
////        System.out.println(AmNote.length);
////        System.out.println(vec.getDimension());
//        		
//        for (int i = 0; i < vec.getDimension(); i++) {
//        	double val = vec.getEntry(i);
//        	if (val >= THRESHOLD) {
//        		notelist[AmNote[i]] = new Note(AmNote[i], 100, val);
//        	}
//        }
//        return notelist;
//	}


	private Note[] transcription(RealMatrix in) {
	    RealVector actVec1 = this.Wp[0].multiply(in).getColumnVector(0);
	    RealVector actVec2 = this.Wp[1].multiply(in).getColumnVector(0);
	    RealVector actVec3 = this.Wp[2].multiply(in).getColumnVector(0);
	    RealVector actVec4 = this.Wp[3].multiply(in).getColumnVector(0);
	    RealVector actVec5 = this.Wp[4].multiply(in).getColumnVector(0);
	    RealVector actVec6 = this.Wp[5].multiply(in).getColumnVector(0);
	    RealVector[] actVecs = {actVec1, actVec2, actVec3, actVec4, actVec5, actVec6};

//        Note[] notelist1 = new Note[23];
//        Note[] notelist2 = new Note[23];
//        Note[] notelist3 = new Note[23];
//        Note[] notelist4 = new Note[23];
//        Note[] notelist5 = new Note[23];
//        Note[] notelist6 = new Note[23];

        Note[] notelist1 = new Note[128];
        Note[] notelist2 = new Note[128];
        Note[] notelist3 = new Note[128];
        Note[] notelist4 = new Note[128];
        Note[] notelist5 = new Note[128];
        Note[] notelist6 = new Note[128];
        Note[][] notelists = {notelist1, notelist2, notelist3, notelist4, notelist5, notelist6};
        		
        for (int stNum = 0; stNum < actVecs.length; stNum++) {
//        for (int stNum = 0; stNum < 2; stNum++) {
        	RealVector nowVec = actVecs[stNum]; 
        	Note[] nowNotelist = notelists[stNum]; 
        	int[] nowStmn = stmn[stNum];

//        	System.out.println(nowVec.getDimension());
            for (int i = 0; i < nowVec.getDimension(); i++) {
                double val = nowVec.getEntry(i);
                if (val >= THRESHOLD) {
                    try {
                    	nowNotelist[nowStmn[i]] = new Note(nowStmn[i], 100, val, -1);
                    } catch(Exception e) {
                        System.out.println("out" + i);
                    }
                }
            }
        }
        
        Note[] dest = new Note[128];
        int[] noteCounter = new int[128];
        double[] vals = new double[128];

        for (int i = 0; i < notelists.length; i++) {
//        	System.out.println("noteli:" + i);
        	Note[] nowList= notelists[i]; 
        	for (int j = 0; j < nowList.length; j++) {
        		if (nowList[j] != null) {
                    noteCounter[nowList[j].noteNum] += 1;
                    vals[nowList[j].noteNum] += nowList[j].val;
        		}
        	}
        }
        
        for (int i = 0; i < noteCounter.length; i++) {
        	if (noteCounter[i] > 0) {
        		// valの平均
        		double val = vals[i] / noteCounter[i];
        		dest[i] = new Note(i, 100, val, noteCounter[i]);
        	}
        }

        return dest;
	}
	
	private void printNoteCounter(int[] noteCounter) {
        for (int i = 0; i < noteCounter.length; i++) {
        	if (noteCounter[i] > 0) {
                System.out.println(i + ":" + noteCounter[i]);
        	}
        }
	}
	private int[] createNoteNumList(int num) {
		int[] dest = new int[23];
		
		int start = 0;
		
		switch (num) {
            case 1: 
            	start = 64;
            	break;
            case 2: 
            	start = 59;
            	break;
            case 3: 
            	start = 55;
            	break;
            case 4: 
            	start = 50;
            	break;
            case 5: 
            	start = 45;
            	break;
            case 6: 
            	start = 40;
            	break;
            default: 
            	System.out.println("error");
            	break;
		}
			
		for (int i = 0; i < 23; i++) {
			dest[i] = start + i;
		}
		System.out.println(dest.length);
		return dest;
	}

	public Class[] getInputClasses() {
		return new Class[] { 
				ComplexArray.class,
				ComplexArray.class,
				ComplexArray.class
				};
	}
	
//	public Class[] getOutputClasses() {
//        return new Class[] { MidiEventWithTicktime[].class };
//	}
	public Class[] getOutputClasses() {
        return new Class[] { MidiEventWithTicktime.class };
	}
	
	public void setWp(RealMatrix[] argWp) {
		for (int i = 0; i < Wp.length; i++) {
			this.Wp[i] = argWp[i];
		}
	}
	
	public static void main(String[] args) {
//		int i = 48;
//		System.out.println(st5[i%12] + i/12);
		AudioTranscriptioner at = new AudioTranscriptioner();
		int[] aaa = at.createNoteNumList(6);
		
		for (int i = 0; i < aaa.length; i++) {
			System.out.println(aaa[i]);
		}
	}
}