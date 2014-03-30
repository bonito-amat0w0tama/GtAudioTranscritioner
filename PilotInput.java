import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.WAVWrapper;
import jp.crestmuse.cmx.processing.CMXController;


public class PilotInput {
    public static void main(String[] args) {
        CMXController cmx = CMXController.getInstance();
        cmx.readConfig("data/config.xml");

        cmx.showAudioMixerChooser(null);
        WindowSlider mic = cmx.createMic(44100);
        
        STFT stft = new STFT(false);
        AudioTranscriptioner at = new AudioTranscriptioner();
		cmx.addSPModule(mic);

		cmx.addSPModule(stft);
		cmx.addSPModule(at);

		cmx.connect(mic,  0, stft, 0);
		cmx.connect(stft,  0, at, 0);

		cmx.startSP();
	}
}
