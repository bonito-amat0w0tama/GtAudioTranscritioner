import java.io.IOException;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

import jp.crestmuse.cmx.amusaj.sp.SPExecutor;
import jp.crestmuse.cmx.amusaj.sp.SPModule;
import jp.crestmuse.cmx.amusaj.sp.STFT;
import jp.crestmuse.cmx.amusaj.sp.TimeSeriesCompatible;
import jp.crestmuse.cmx.amusaj.sp.WindowSlider;
import jp.crestmuse.cmx.filewrappers.WAVWrapper;
import jp.crestmuse.cmx.math.ComplexArray;
import jp.crestmuse.cmx.math.MathUtils;
import jp.crestmuse.cmx.processing.CMXController;
import jp.crestmuse.cmx.sound.SMFPlayer;
import jp.crestmuse.cmx.sound.TickTimer;

public class AudioInputer implements ModuleUser {
    private CMXController cmx = null; 
    private WindowSlider mic = null;
    private WindowSlider wavWinsl = null; 

	public AudioInputer() {
	}

	public void setCmx(CMXController _cmx) {
		this.cmx = _cmx;
	}

	public void setupMic(TickTimer tm) {
		System.out.println("in:" +this.cmx);
		this.cmx.showAudioMixerChooser(null);
        this.cmx.readConfig("data/config.xml");
		this.mic = cmx.createMic(44100);
		mic.setTickTimer(tm);
	}

	public void setupWavInput(String path, TickTimer smf) throws IOException{
		wavWinsl = new WindowSlider(false);
		try {
			WAVWrapper wav = WAVWrapper.readfile(path);
			wavWinsl.setInputData(wav);
			wavWinsl.setTickTimer(smf);
		} catch (IOException e) {
			throw new IOException("wavファイルが読み込めません");
		}
	}

	public WindowSlider getMic() {
		return this.mic;
	}

	public WindowSlider getWavInput() {
		return this.wavWinsl;
	}

}