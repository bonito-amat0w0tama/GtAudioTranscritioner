import java.io.IOException;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.math.*;
import jp.crestmuse.cmx.processing.*;

public abstract class GuitarAudioAnalyzer {
	protected static CMXController _cmx = CMXController.getInstance();
	protected SPExecutor _ex = new SPExecutor();
	protected WAVWrapper _wav;
	protected WindowSlider _winSldr;
	protected STFT _stft;

	protected void readWav(String wavPath) throws IOException {
		try {
            this._wav =  WAVWrapper.readfile(wavPath);
		} catch(IOException e) {
			throw new IOException("Wavファイルの読み込みに失敗しました");
		}
	}
	protected abstract void setupAllModules();
	public abstract DoubleMatrix analyzeGuitarAudio(String wavPath) throws IOException, InterruptedException;

}
