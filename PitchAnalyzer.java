import java.io.IOException;
import java.net.ConnectException;
import java.util.LinkedList;
import java.util.List;

import jp.crestmuse.cmx.amusaj.sp.SPModule;
import jp.crestmuse.cmx.amusaj.sp.TimeSeriesCompatible;
import jp.crestmuse.cmx.math.*;



public class PitchAnalyzer {
//	List[] list = new LinkedList<int[]>();
	List[] list = null; 

	public void execute(Object[] src, TimeSeriesCompatible[] time)
			throws InterruptedException {
		DoubleMatrix inMat = (DoubleMatrix) src[0];
		
		System.out.println(inMat.nrows());
		System.out.println(inMat.ncols());
	}
	
	private static List findePeak(DoubleArray arr, double min) {
		LinkedList<Integer> dest = new LinkedList<Integer>();
		
		for (int i = 0; i < arr.length(); i++) {
			if ((i-1 > 0) && (i+1 < arr.length())) {
				double pre = arr.get(i-1);
				double now = arr.get(i);
				double next = arr.get(i+1);
				if ((pre < now) && (now > next) && now >= min) {
					dest.add(i);
				}	
			}
		}
		return dest;
	}

	public Class[] getInputClasses() {
		return new Class[] {
			DoubleMatrix.class
		};
	}

	public Class[] getOutputClasses() {
		return new Class[] {
		};
	}
	
	public static void main(String[] args) {
        GuitarAllNoteAnalyzer gaa = new GuitarAllNoteAnalyzer();
        SendingCodeGenerator scg = new SendingCodeGenerator();

        DoubleMatrix V = null, SH = null, SW =null, Wp = null;

        try {
            ExternalCodeAdapter eca = 
                new ExternalCodeAdapter("localhost", 11111);
            eca.pushCode(scg.pilotTrans);

            V = (DoubleMatrix)eca.pop();
            SW = (DoubleMatrix)eca.pop();
            SH = (DoubleMatrix)eca.pop();
            Wp = (DoubleMatrix)eca.pop();

            eca.pushEnd();
            eca.close();
        } catch(ConnectException e) {
            System.out.println("Pythonサーバーとのコネクションエラー");
            e.printStackTrace();
            System.exit(-1);
        } catch(IOException e) {
        	e.printStackTrace();
        	System.exit(-1);
        }
        Operations ope = new Operations();
        PitchAnalyzer pa = new PitchAnalyzer();

        System.out.println("rows" + SW.nrows());
        System.out.println("cols" + SW.ncols());

        DoubleArray da = ope.getColumn(SW, 0);
        System.out.println("len" + da.length());
        
        double min = ope.max(da) * 0.1;
        List l = pa.findePeak(da, min);
       	


        System.out.print(l);
	}
}
