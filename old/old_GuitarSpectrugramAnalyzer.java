import java.io.IOException;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.WAVWrapper;
import jp.crestmuse.cmx.processing.*;

public class old_GuitarSpectrugramAnalyzer {
	private SPExecutor ex;
	private WindowSlider winsl;
	private STFT stft;
	private SpectrumConverter csasc;
	private WAVWrapper wav;
	private CMXController cmx;
	
	private SpectrogramGenerator sg;

	old_GuitarSpectrugramAnalyzer() {
		ex = new SPExecutor();
		cmx = cmx.getInstance();
	}

	private void readWav(String wavPath) {
		try {
            this.wav =  WAVWrapper.readfile(wavPath);
		} catch(IOException e) {
			System.out.println("Wavファイルの読み込みミス");
			e.printStackTrace();
		}
	}
	private void setupSPModuleToSPExecutor(ProducerConsumerCompatible mdl) {
		this.ex.addSPModule(mdl);
	}

	private void setupWindowSlider() {
		winsl = new WindowSlider(false);
		this.setupSPModuleToSPExecutor(winsl);
	}
	
	private void setInputDataToWindowSlider() throws NullPointerException {
		this.winsl.setInputData(this.wav);
	}

	private void setupSTFT() {
       	this.cmx.readConfig("./data/config.xml");	
		this.stft = new STFT(false);
		this.setupSPModuleToSPExecutor(this.stft);
	}
	private void setupSpectrumConverter() {
		this.csasc = new SpectrumConverter();
		this.setupSPModuleToSPExecutor(this.csasc);
	}
	private void setSpectrogramGenerator() {
		this.sg = new SpectrogramGenerator();
		this.setupSPModuleToSPExecutor(this.sg);
	}
	private void setupAllModules() {
		this.setupWindowSlider();
		this.setupSTFT();
		this.setupSpectrumConverter();
		//this.setSpectrogramGenerator();
	}
	
	private void connectAllModules() {
		this.ex.connect(winsl, 0, stft, 0);
		this.ex.connect(stft, 0, csasc, 0);
	}
	
	private void startSPModules() {
		this.ex.start();
	}
	public static void main(String args[]) {
		old_GuitarSpectrugramAnalyzer gaa = new old_GuitarSpectrugramAnalyzer();
		gaa.readWav("data/zenon.wav");
		gaa.setupAllModules();
		gaa.connectAllModules();
		gaa.setInputDataToWindowSlider();
		gaa.startSPModules();

	}
}

