import java.io.IOException;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.math.DoubleMatrix;


public class GuitarAllNoteAnalyzer extends GuitarAudioAnalyzer {
    private SpectrogramGenerator _sg;
    public GuitarAllNoteAnalyzer() {
        this.setupAllModules();
    }
    protected void setupAllModules() {
        this._winSldr = new WindowSlider(false);
        this._ex.addSPModule(_winSldr);

        this._cmx.readConfig("data/config.xml");
        this._stft = new STFT(false);
        this._ex.addSPModule(this._stft);

        this._sg = new SpectrogramGenerator();
        this._ex.addSPModule(this._sg);

        this._ex.connect(this._winSldr, 0, this._stft, 0);
        this._ex.connect(this._stft, 0, this._sg, 0);

    }
    public DoubleMatrix analyzeGuitarAudio(String wavPath) throws IOException, InterruptedException{
        try {
            this.readWav(wavPath);
        } catch(IOException e) {
            throw new IOException("wavの読み込みに失敗しました");
        }
        this._winSldr.setInputData(this._wav);
        this._ex.start();

        // 無理矢理な方法
        while (!this._ex.finished()) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
            	throw e;
            }
        }
        DoubleMatrix mat = this._sg.getSpectrogram2();
        return mat;
    }
}
