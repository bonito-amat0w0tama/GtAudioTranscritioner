import java.io.IOException;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.WAVWrapper;
import jp.crestmuse.cmx.math.DoubleMatrix;
import jp.crestmuse.cmx.processing.*;

public class GuitarSpectrumAnalyzer extends GuitarAudioAnalyzer {
    private SpectrumConverter _sc = new SpectrumConverter();

    GuitarSpectrumAnalyzer() {
    }

    @Override
    protected void setupAllModules() {
        this._winSldr = new WindowSlider(false);
        this._ex.addSPModule(_winSldr);

        this._cmx.readConfig("data/config.xml");
        this._stft = new STFT(false);
        this._ex.addSPModule(this._stft);

        this._sc = new SpectrumConverter();
        this._ex.addSPModule(_sc);

        this._ex.connect(this._winSldr, 0, this._stft, 0);
        this._ex.connect(this._stft, 0, this._sc, 0);
    }

    @Override
    public DoubleMatrix analyzeGuitarAudio(String wavPath) {
        this.setupAllModules();
        try {
            this.readWav(wavPath);
        } catch(IOException e) {
            // Wavの読み込みに失敗したらNullを返す
            e.printStackTrace();
            return null;
        }

        if (!(this._wav == null)) {
            this._winSldr.setInputData(this._wav);
        } else {
            System.out.println("nullttemasu");
            return null;
        }
        this._ex.start();

        // 無理矢理な方法
        while (!this._ex.finished()) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {

            }
        }
        DoubleMatrix dbl = this._sc.createDoubleMatrix();
        return dbl;
    }
}
