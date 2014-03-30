import java.io.IOException;
import java.net.ConnectException;

import jp.crestmuse.cmx.math.DoubleMatrix;

public class pilot {
	public static void main(String[] args) {
        SendingCodeGenerator scg = new SendingCodeGenerator();
        String te = scg.makeSaveMatCode("'te'", true);
        String save = scg.makeSaveCode("'te'", true);
        String pop = scg.makePopCode();
        GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();
        DoubleMatrix allNote = gaa.analyzeGuitarAudio("./data/doremifa.wav");
        DoubleMatrix V = null, SH = null, SW =null, Wp = null;

        // Tcp/ipで飛ばう
        try {
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", 5555);
            eca.pushDoubleMatrix(allNote);
            // メモリの解放
            allNote = null;
//            eca.pushCode(pop);
            //eca.pushCode(save);
            eca.pushCode("print globals()");

    //            V = (DoubleMatrix)eca.pop();
    //            System.out.println("H: " + MathUtils.toString1(SH));
    //            SW = (DoubleMatrix)eca.pop();
    //            SH = (DoubleMatrix)eca.pop();
    //            System.out.println("W: " + MathUtils.toString1(SW));
    //            Wp = (DoubleMatrix)eca.pop();
    //            System.out.println("Wp: " + MathUtils.toString1(Wp));

            eca.pushEnd();
            eca.close();
        } catch(ConnectException e) {
            System.out.println("Pythonサーバーとのコネクションエラー");
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
