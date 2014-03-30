import java.io.*;
import java.nio.*;
import java.net.*;
import jp.crestmuse.cmx.processing.*;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.sound.*;
import jp.crestmuse.cmx.amusaj.sp.*;
import javax.sound.sampled.*;
import jp.crestmuse.cmx.processing.*;
import jp.crestmuse.cmx.math.*;

public class CopyOfGuitarAllNoteAnalyzer {
	private WAVWrapper wav;
	private SPExecutor ex = new SPExecutor();
	// wavを読み込むためにメンバ変数である必要あり
	private WindowSlider winSldr;
	private SpectrogramGenerator sg;
	private STFT stft;
	private CMXController cmx;
	private DoubleMatrix allNoteWav;

	CopyOfGuitarAllNoteAnalyzer() {
		cmx = CMXController.getInstance();
	}
	private void readWav(String wavPath) throws IOException {
		try {
            this.wav =  WAVWrapper.readfile(wavPath);
		} catch(IOException e) {
			//System.out.println("Wavファイルの読み込みミス");
			//e.printStackTrace();
			throw new IOException("Wavファイルの読み込みに失敗しました");
		}
	}
	private void setSPModuleToSPExecutor(ProducerConsumerCompatible mdl) {
		this.ex.addSPModule(mdl);
	}

	private void setWindowSlider() {
		this.winSldr = new WindowSlider(false);
		this.setSPModuleToSPExecutor(winSldr);
	}
	private void setInputDataToWindowSlider() throws NullPointerException {
		this.winSldr.setInputData(this.wav);
	}
	private void setSTFT() {
		// STFTのための設定の読み込み
       	this.cmx.readConfig("./data/config.xml");	
       	this.stft = new STFT(false);	
		this.setSPModuleToSPExecutor(this.stft);
	}
	private void setSpectrogramGenerator() {
		this.sg = new SpectrogramGenerator();
		this.setSPModuleToSPExecutor(this.sg);
	}
	private void connectAllModules() {
		this.ex.connect(winSldr, 0, stft, 0);
		this.ex.connect(stft, 0, sg, 0);
	}

	private void setAllModules() {
		this.setWindowSlider();
		this.setSTFT();
		this.setSpectrogramGenerator();
	}
	
	private void startSPModules() {
		this.ex.start();
	}
	public DoubleMatrix analyzeAllNote(String wavPath) {
		try {
			this.readWav(wavPath);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}

		this.setAllModules();
		this.connectAllModules();

		// ヌルポが出たらWavの読み込みに失敗しているので終了
		try {
			this.setInputDataToWindowSlider();
		} catch(NullPointerException e) {
			System.out.println("nulupo");
			e.printStackTrace();
			return null;
		}
		this.startSPModules();
		
	    // 無理矢理な方法
		while (!ex.finished()) {
		    try {
                Thread.currentThread().sleep(100);
		    } catch (InterruptedException e) {

		    }
		}
		this.allNoteWav = sg.getSpectrogram();
		
		return allNoteWav;
	}
}
