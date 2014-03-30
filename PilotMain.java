import java.io.*;

import java.net.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.math.*;
import jp.crestmuse.cmx.processing.*;
import jp.crestmuse.cmx.sound.SMFPlayer;

import org.apache.commons.math3.linear.*;



public class PilotMain {
    public static void main(String[] args) {
        GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();
        SendingCodeGenerator scg = new SendingCodeGenerator();

//        String allPath = "./data/am_0106.wav";
//        DoubleMatrix allNote = gaa.analyzeGuitarAudio(allPath);
        DoubleMatrix V = null, SH = null, SW =null, Wp = null;

        try {
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", 1111);
//            eca.pushDoubleMatrix(allNote);
            // メモリの解放
//            allNote = null;
            eca.pushCode(scg.pilotTrans);

            V = (DoubleMatrix)eca.pop();
            SW = (DoubleMatrix)eca.pop();
            SH = (DoubleMatrix)eca.pop();
            Wp = (DoubleMatrix)eca.pop();

            eca.pushEnd();
            eca.close();
        } catch(ConnectException e) {
            System.out.println("Pythonサーバーとのコネクションエラー");
            e.printStackTrace();
            System.exit(-1);
        } catch(IOException e) {
        	e.printStackTrace();
        	System.exit(-1);
        }
        
        RealMatrix RV = MyUtils.toRealMatrix(V);
        RealMatrix RH = MyUtils.toRealMatrix(SH);
        RealMatrix RW = MyUtils.toRealMatrix(SW);
        RealMatrix RWp = MyUtils.toRealMatrix(Wp);
        
//        CMXController cmx = CMXController.getInstance();
        ModuleManager mana = new ModuleManager();

        mana.manageModuleUser("ai", new AudioInputer());

        AudioInputer ai = (AudioInputer) mana.getModuleUser("ai");
        SMFPlayer smf = null;

		try {
			smf = new SMFPlayer();
			smf.readSMF("./smf/count.mid");
            ai.setupMic(smf); 
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
			System.exit(-1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
        String wavPath = "./data/am_0106.wav";

        try {
            ai.setupWavInput(wavPath, smf);
        } catch (IOException e) {
        	e.printStackTrace();
        	System.exit(-1);
        }

        mana.manageModule("mic", ai.getMic());
        mana.manageModule("winsl", ai.getWavInput());
        mana.manageModule("stft", new STFT(false));
        mana.manageModule("at", new AudioTranscriptioner());
        ((AudioTranscriptioner) mana.getModule("at")).setWp(RWp);

        mana.addModule2Cmx("winsl");
        mana.addModule2Cmx("mic");
        mana.addModule2Cmx("stft");
        mana.addModule2Cmx("at");

//        mana.connectModules("winsl", "stft");
        mana.connectModules("mic", "stft", false);
        mana.connectModules("stft", "at", true);
        smf.play();
        mana.startSP();
	}
}
