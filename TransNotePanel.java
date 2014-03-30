import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.apache.commons.math3.linear.RealMatrix;

import processing.core.PApplet;
import jp.crestmuse.cmx.amusaj.sp.MidiInputModule;
import jp.crestmuse.cmx.amusaj.sp.MidiOutputModule;
import jp.crestmuse.cmx.amusaj.sp.MidiRecorder;
import jp.crestmuse.cmx.amusaj.sp.STFT;
import jp.crestmuse.cmx.math.DoubleMatrix;
import jp.crestmuse.cmx.processing.CMXApplet;
import jp.crestmuse.cmx.processing.CMXController;
import jp.crestmuse.cmx.sound.SMFPlayer;
import jp.crestmuse.cmx.sound.TickTimer;


public class TransNotePanel extends PApplet {
    int numKey = 73; // 鍵盤の数
    int resMeasure = 8; // 小節の分解能
    float WDivRB;
    float HDivNk;
    float magnification = 1; // ノートを描画する際の倍率
//    magnification = (float) this.getWidth() / (float) (st.getMeasureLength() * 4 + st.getMeasureLength() / 8); // 画面の大きさが変更されても平気なように毎回倍率を計算

    TransData[] td = {
    };

	public void setup() {
        GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();
        SendingCodeGenerator scg = new SendingCodeGenerator();

        DoubleMatrix Wp1 = null, Wp2 = null, Wp3 = null, Wp4 = null, Wp5 = null, Wp6 = null;

        try {
        	int port = 1112;
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", port);

            eca.pushCode(scg.trans0212);

            Wp1 = (DoubleMatrix)eca.pop();
            Wp2 = (DoubleMatrix)eca.pop();
            Wp3 = (DoubleMatrix)eca.pop();
            Wp4 = (DoubleMatrix)eca.pop();
            Wp5 = (DoubleMatrix)eca.pop();
            Wp6 = (DoubleMatrix)eca.pop();

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
        

        RealMatrix RWp1 = MyUtils.toRealMatrix(Wp1);
        RealMatrix RWp2 = MyUtils.toRealMatrix(Wp2);
        RealMatrix RWp3 = MyUtils.toRealMatrix(Wp3);
        RealMatrix RWp4 = MyUtils.toRealMatrix(Wp4);
        RealMatrix RWp5 = MyUtils.toRealMatrix(Wp5);
        RealMatrix RWp6 = MyUtils.toRealMatrix(Wp6);
        
        ModuleManager mana = new ModuleManager();

        mana.manageModuleUser("ai", new AudioInputer());

        AudioInputer ai = (AudioInputer) mana.getModuleUser("ai");
//        MyMidiRecorder mr1 = new MyMidiRecorder("../data/nmf.mid", 480); 
//        MyMidiRecorder mr2 = new MyMidiRecorder("../data/midiGt.mid", 480); 
        MidiRecorder mr1 = new MidiRecorder("../data/nmf.mid", 480); 
        MidiRecorder mr2 = new MidiRecorder("../data/midiGt.mid", 480); 

//        SMFPlayer smf = null; 
        CMXController cmx = mana.cmx;
//        try {
//            mana.cmx.showMidiOutChooser(null);
//        } catch(Exception e) {
//        	System.exit(-1);
//        }
//        mana.cmx.smfread("../data/count4.mid");
        mana.cmx.smfread("../data/8beat.mid");

        try {
//            smf = new SMFPlayer();
            String wavPath = "../data/short/3st_0206s.wav";
            ai.setupWavInput(wavPath, cmx);
//            smf.readSMF("../data/count.mid");
            ai.setupMic(cmx); 
        } catch (IOException e) {
        	e.printStackTrace();
        	System.exit(-1);
        }
        
        mana.cmx.showMidiInChooser(null);
        mana.cmx.showMidiOutChooser(null);
        MidiInputModule mi = cmx.createMidiIn();
        mi.setTickTimer(cmx);

        mana.manageModule("mi", mi);
        mana.manageModule("mic", ai.getMic());
        mana.manageModule("winsl", ai.getWavInput());
        mana.manageModule("stft", new STFT(false));
        mana.manageModule("at", new AudioTranscriptioner());
        mana.manageModule("mr1",  mr1); 
        mana.manageModule("mr2",  mr2); 

        RealMatrix[] Wps = new RealMatrix[6]; 
        Wps[0] = RWp1;
        Wps[1] = RWp2;
        Wps[2] = RWp3;
        Wps[3] = RWp4;
        Wps[4] = RWp5;
        Wps[5] = RWp6;
        ((AudioTranscriptioner) mana.getModule("at")).setWp(Wps);

        mana.addModule2Cmx("mi");
        mana.addModule2Cmx("winsl");
        mana.addModule2Cmx("mic");
        mana.addModule2Cmx("stft");
        mana.addModule2Cmx("at");
        mana.addModule2Cmx("mr1");
//        mana.addModule2Cmx("mr2");

//        mana.connectModules("winsl", "stft", false);
        mana.connectModules("mic", "stft", false, false);
        mana.connectModules("stft", "at", true, false);
        mana.connectModules("at", "mr1", false, false);
//        mana.connectModules("mi", "mr2", false, false);

            
//        MidiRecorder mr2 = new MidiRecorder("../data/gt2_0210.mid", 480); 
        mi.setTickTimer(cmx);
        cmx.addSPModule(mi);
        cmx.addSPModule(mr2);
        cmx.connect(mi, 0, mr2, 0);

        
//        smf.play();
        cmx.playMusic();
        mana.startSP();

		this.size(10, 10);
		this.frameRate(60);
	}	

    public void draw() {
    	WDivRB = (float) this.getWidth() / (float) (resMeasure * 4 + 1);
		HDivNk = (float) this.getHeight() / (float) numKey; 
//		magnification = (float) this.getWidth() / (float) (st.getMeasureLength() * 4 + st.getMeasureLength() / 8); // 画面の大きさが変更されても平気なように毎回倍率を計算
		background(255);
		this.drawKeyboard();
//		this.drawTranDatas(AudioTranscriptioner.td, 105, 175, 242);
//		this.drawNoteList(AudioTranscriptioner.tdList, 105, 175, 242);
    }
    public void keyPressed() {
		if (key == CODED) { 
			if (keyCode == UP) {
				ModuleManager.cmx.stopSP();
//				System.exit(0);
			}
			if (keyCode == DOWN) {
				System.out.println("終了しました");
				System.exit(0);
			}
		}
	}
	public void drawKeyboard() {
//    	int resMeasure = 8; // 小節の分解能
//    	float magnification; // ノートを描画する際の倍率
//    	int changedRiff = -1;
//    	int nowMeasure = -1;
//    	private boolean deleteFlag = false;
//    	private boolean stopFlag = true;
//    	String changeMode = null;
//    	float WDivRB;
//    	float HDivNk;
//        int countOfc = 7;
//        int countOfn = 97;

        int countC = 7;
        int countFn = 97;
        // 鍵盤の描画
        // 白鍵
    		for (int i = 0; i <= numKey; i++) {
    			this.stroke(0);
    			this.fill(255);
    			this.line(0, i * HDivNk, WDivRB, i * HDivNk);
    		}
    		// 黒鍵
    		for (int i = 0; i <= numKey; i++) {
    			if (i % 12 == 11 || i % 12 == 9 || i % 12 == 6 || i % 12 == 4 || i % 12 == 2) {
    				stroke(0);
    				fill(0);
    				rect(0, i * HDivNk, WDivRB / 2, HDivNk);
    			}
    		}

    		for (int i = 0; i <= numKey; i++) {
    			// 1オクターブごとのライン
    			if (i % 12 == 1) {
    				stroke(105, 175, 242);
    				strokeWeight(2);
    				line(0, i * HDivNk, getWidth(), i * HDivNk);
    				strokeWeight(0);
    				fill(255);

    				fill(150);
    				textSize(10);
    				textAlign(RIGHT);
    				text("C" + countC + " " + countFn, WDivRB, i * HDivNk);
    				countC--;
    			}
    			else if (i % 12 == 11 || i % 12 == 9 || i % 12 == 6 || i % 12 == 4 || i % 12 == 2) {
    				stroke(200);
    				fill(200);
    				rect(WDivRB, i * HDivNk, getWidth(), HDivNk);

    				fill(150);
    				textSize(10);
    				textAlign(RIGHT);
    				text(countFn, WDivRB, i * HDivNk);
    			}
    			else {
    				stroke(200);
    				line(WDivRB, i * HDivNk, getWidth(), i * HDivNk);

    				fill(150);
    				textSize(10);
    				textAlign(RIGHT);
    				text(countFn, WDivRB, i * HDivNk);
    			}
    			countFn--;
    		}
    	}
    	void drawMeasureLine() {
    		// 小節線の描画
    		stroke(0);
    		line(WDivRB, 0, WDivRB, getHeight());
    		for (int i = 0; i <= resMeasure* 4 + 1; i++) {

    			if (i % resMeasure == 0) {
    				stroke(105, 175, 242);
    				strokeWeight(2);
    				line(i * getWidth() / (resMeasure * 4 + 1) + WDivRB, 0, i * getWidth() / (resMeasure * 4 + 1) + WDivRB, getHeight()); // i * WDivRBではダメ i * (getWidth() / (resolution)
    																																						// // * 4)でもダメ
    				strokeWeight(0);
    			}
    			else if (i % (resMeasure / 2) == 0) {
    				stroke(200, 175, 242);
    				strokeWeight(2);
    				line(i * getWidth() / (resMeasure * 4 + 1) + WDivRB, 0, i * getWidth() / (resMeasure * 4 + 1) + WDivRB, getHeight()); // i * WDivRBではダメ i * (getWidth() / (resolution)
    																																						// // * 4)でもダメ
    				strokeWeight(0);
    			}
    			else {
    				stroke(150);
    				line(i * getWidth() / (resMeasure * 4 + 1) + WDivRB, 0, i * getWidth() / (resMeasure * 4 + 1) + WDivRB, getHeight());
    			}

    		}
    	}
    		void drawTranDatas(TransData[] td, int fill_1, int fill_2, int fill_3) {
    			try {
                    for (int i = 0; i < td.length; i++) {
    					TransData nowMidievt = td[i]; 
    					byte[] data = nowMidievt.getMessageInByteArray();
    					float note = (float) this.getHeight() - ((data[1] - 23) * HDivNk);

    					if (data[2] > 0) {
                            float length = 120.0F * magnification;
    //                        float position = (float) tmpNoteOnForPrint.get(data[1]) * magnification + WDivRB;
//                            float position = 0;
                            float position = (float)  magnification + WDivRB;
                            fill(fill_1, fill_2, fill_3);
                            stroke(200);
                            rect(position, note, length, HDivNk);
//                            rect(0, 0, length, HDivNk);
    					}
    				}
    			}
    			catch (Exception e) {
    				System.out.println("drawig_note_error {");
    				e.printStackTrace();
    				System.out.println("}");
            }
        }
        void drawNoteList(ArrayList td, int fill_1, int fill_2, int fill_3) {
            try {
                for (int i = 0; i < td.size(); i++) {
                    TransData nowMidievt = (TransData) td.get(i); 
                    byte[] data = nowMidievt.getMessageInByteArray();
                    float note = (float) this.getHeight() - ((data[1] - 23) * HDivNk);

                    if (data[2] > 0) {
                        float length = 120.0F * magnification;
//                        float position = (float) tmpNoteOnForPrint.get(data[1]) * magnification + WDivRB;
//                            float position = 0;
                        float position = (float)  magnification + WDivRB;
                        fill(fill_1, fill_2, fill_3);
                        stroke(200);
                        rect(position, note, length, HDivNk);
//                            rect(0, 0, length, HDivNk);
                    }
                }
            }
            catch (Exception e) {
                System.out.println("drawig_note_error {");
                e.printStackTrace();
                System.out.println("}");
            }
        }
    }