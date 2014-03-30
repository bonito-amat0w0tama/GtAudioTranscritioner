
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;

import java.io.IOException;
import java.net.ConnectException;

import jp.crestmuse.cmx.filewrappers.WAVWrapper;
import jp.crestmuse.cmx.math.DoubleMatrix;
import jp.crestmuse.cmx.math.MathUtils;

import org.junit.BeforeClass;
import org.junit.Test;

public class GuitarAllNoteAnalyzerTest {
	private GuitarAudioAnalyzer gaa  = new GuitarAllNoteAnalyzer();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void ギター全音のWavファイルをDoubleMatrixに変換() {
		DoubleMatrix base = gaa.analyzeGuitarAudio("data/zenon.wav");
		assertThat(base, is(any(DoubleMatrix.class)));
	}
	@Test
	public void 変換に失敗した時Nullを返す() {
		DoubleMatrix base = gaa.analyzeGuitarAudio("");
		assertNull(base);
	}
	@Test
	public void TCP通信をしてNMF解析する() {
		DoubleMatrix anMat = gaa.analyzeGuitarAudio("data/zenon.wav");
        String code = "W,H = self.nmfMatrix(self.pop())\nself.push(W)\nself.push(H)";
        
        DoubleMatrix H = null;
        DoubleMatrix W = null;

        // Tcp/ipで飛ばう
        try {
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", 1111);
            eca.pushDoubleMatrix(anMat);
            eca.pushCode(code);
            H = (DoubleMatrix)eca.pop();
            System.out.println("H: " + MathUtils.toString1(H));
            W = (DoubleMatrix)eca.pop();
            System.out.println("W: " + MathUtils.toString1(W));
            eca.close();
        } catch(ConnectException e) {
        	System.out.println("Pythonサーバーとのコネクションエラー");
            e.printStackTrace();
        } catch(IOException e) {
        	System.out.println("IO");
        	e.printStackTrace();
        }
        assertThat(H, is(notNullValue())); 
        assertThat(W, is(notNullValue()));
	}
}
