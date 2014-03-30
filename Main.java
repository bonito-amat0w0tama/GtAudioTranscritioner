import java.io.*;
import java.net.*;


import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.math.*;
import jp.crestmuse.cmx.processing.*;

import org.apache.commons.math.linear.*;


public class Main {
    public static void main(String[] args) {
        GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();
        SendingCodeGenerator scg = new SendingCodeGenerator();

        DoubleMatrix allNote = gaa.analyzeGuitarAudio("./data/zenon_0107.wav");

        DoubleMatrix V = null, SH = null, SW =null, Wp = null;
        // Tcp/ipで飛ばう
        try {
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", 1111);
            eca.pushDoubleMatrix(allNote);
            // メモリの解放
            allNote = null;
            eca.pushCode(scg.nmfZenon);

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
