import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.math.*;
import java.util.*; 
import static java.lang.Math.*;

public class SpectrogramGenerator extends SPModule{
//	List<ComplexArray> list = new ArrayList<ComplexArray>();
//	List<ComplexArray> list = new ArrayList<ComplexArray>();
//	List<ComplexArray> list2 = new LinkedList<ComplexArray>();
    List<float[]> list2 = new LinkedList<float[]>();
    int count = 0;

	public void execute(Object[] src, TimeSeriesCompatible[] dest) {
	    //System.out.println(count);
	    count++;

	    //System.err.println(count);

	    ComplexArray spec = (ComplexArray)src[0];
	    float[] powsec = new float[spec.length()];
	    for (int i = 0; i < spec.length(); i++) {
            powsec[i] = (float)sqrt(spec.getReal(i) * spec.getReal(i) +
                        spec.getImag(i) * spec.getImag(i));
	    }
	    list2.add(powsec);
	    //        list2.add((ComplexArray)src[0]);
	    ////        System.out.println(list2.size());
	    //        System.out.println((ComplexArray)src[0]);
	}

//    // メインで走らせる
//    DoubleMatrix getSpectrogram() {
//    	try {
//	        DoubleMatrix matrix = MathUtils.createDoubleMatrix(
//	        		list.get(0).length(), list.size());
//	        for (int j = 0; j < list.size(); j++) {
//	            ComplexArray array = list.get(j);
//	            for (int i = 0; i < array.length(); i++) {
//	                matrix.set(i, j, sqrt(array.getReal(i) * array.getReal(i) 
//	                		+ array.getImag(i) * array.getImag(i)));
//	            }
//	        }
//	        return matrix;
//    	} catch(Exception e) {
//    		return null;
//    	}
//    }
    // メインで走らせる
    DoubleMatrix getSpectrogram2() {
    	try {
	        DoubleMatrix matrix = MathUtils.createDoubleMatrix(
	        		list2.get(0).length, list2.size());
	        int size = list2.size();
	        for (int j = 0; j < size; j++) {
                float[] array = list2.remove(0);
                for (int i = 0; i < array.length; i++)
                    matrix.set(i, j, array[i]);
//ComplexArray array = list2.get(0);
//for (int i = 0; i < array.length(); i++) {
//matrix.set(i, j, sqrt(array.getReal(i) * array.getReal(i) 
//+ array.getImag(i) * array.getImag(i)));
//}
//list2.remove(0);
	        }
            return matrix;
    	} catch(Exception e) {
    		return null;
    	}
    }
	public Class[] getInputClasses() {
		return new Class[] { ComplexArray.class };
	}
	
	public Class[] getOutputClasses() {
        return new Class[] { };
	}
}
