import java.io.IOException;
import java.net.ConnectException;

import jp.crestmuse.cmx.math.*;
import jp.crestmuse.cmx.math.*;

public class JunkTCP {
	private static DoubleMatrix createAllOneMatrix(int rows, int cols) {
		DoubleMatrix ret = MathUtils.createDoubleMatrix(rows, cols);
		for (int i = 0; i < ret.nrows(); i++) {
			for (int j = 0; j < ret.ncols(); j++) {
				ret.set(i, j, 1);
			}
		}
		return ret;
	}
	
	public static void main(String args[]) {
        //String code = "W,H = self.nmfMatrix(self.pop())\nself.push(W)\nself.push(H)";
		String code = 
//				"Zero = self.createZeroMatrix(4,3)\n" +
				//"self.push(Zero)\n" +
//				"self.pushMatrix(self.pop())";

//				"print self.pop()\n" +
//				"self.printStack()\n";
				
				"W,H = self.nmfMatrix(self.pop())\n" +
				"self.push(W)\n" +
				"self.push(H)\n" +
				
				"self.pushMatrix(self.pop())\n" + 
				"self.pushMatrix(self.pop())";

		DoubleMatrix sm1 = createAllOneMatrix(100, 333);
//		DoubleMatrix sm2 = createAllOneMatrix(200, 4000);
//		DoubleMatrix sm3 = createAllOneMatrix(4000, 100);
//		DoubleMatrix sm4 = createAllOneMatrix(4000, 100);

		
	    // Tcp/ipで飛ばう
        try {
	        ExternalCodeAdapter eca = 
	            new ExternalCodeAdapter("localhost", 1111);
            eca.pushDoubleMatrix(sm1);
//			eca.pushDoubleMatrix(sm2);
//            eca.pushDoubleMatrix(sm3);
            //eca.pushDoubleMatrix(V);
            //eca.pushDoubleMatrix(V);
            eca.pushCode(code);
            DoubleMatrix H = (DoubleMatrix)eca.pop();
			System.out.println(H);
//            DoubleMatrix ss3 = (DoubleMatrix)eca.pop();
            //System.out.println("H: " + MathUtils.toString1(H));
            DoubleMatrix W = (DoubleMatrix)eca.pop();
            System.out.println("W: " + MathUtils.toString1(W));
			eca.pushEnd();
            eca.close();
        } catch(ConnectException e) {
        	System.out.println("=== エラー発生 ===");
        	System.out.println("Pythonサーバーにつながりません");
            //e.printStackTrace();
        } catch(IOException e) {
        	System.out.println("=== エラー発生 ===");
        	e.printStackTrace();
        } finally {
        }
	}

}
