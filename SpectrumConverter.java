import java.util.*;

import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.math.*;

import static java.lang.Math.*;

public class SpectrumConverter extends SPModule {
	List<DoubleArray> list = new ArrayList<DoubleArray>();

	public SpectrumConverter() {
	}

	private DoubleArray convertComplexArrayToDoubleArray(ComplexArray compArray) {
		DoubleArray dblArray = MathUtils.createDoubleArray(compArray.length());
		for (int i = 0; i < compArray.length(); i++) {
			dblArray.set(i, sqrt(compArray.getReal(i) * compArray.getReal(i)
					+ compArray.getImag(i) * compArray.getImag(i)));
		}
		return dblArray;
	}
	public DoubleMatrix createDoubleMatrix_1() {
//		/*try {
//			DoubleMatrix matrix = MathUtils.createDoubleMatrix(
//				list.size(), list.get(0).length());
//			for (int i = 0; i < list.size(); i++) {
//				DoubleArray array = list.get(i);
//				for (int j = 0; j < array.length(); j++) {
//					matrix.set(j, i, array.get(j));
//				}
//			}
//			return matrix;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return null;
//		}*/
		try {
	        DoubleMatrix matrix = MathUtils.createDoubleMatrix(
	        		list.size(), list.get(0).length());
	        for (int j = 0; j < list.size(); j++) {
	            DoubleArray array = list.get(j);
	            for (int i = 0; i < array.length(); i++) {
	                matrix.set(j, i, array.get(i));
	            }
	        }
	        return matrix;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
	}
	public DoubleMatrix createDoubleMatrix___() {
//		/*try {
//			DoubleMatrix matrix = MathUtils.createDoubleMatrix(
//				list.size(), list.get(0).length());
//			for (int i = 0; i < list.size(); i++) {
//				DoubleArray array = list.get(i);
//				for (int j = 0; j < array.length(); j++) {
//					matrix.set(j, i, array.get(j));
//				}
//			}
//			return matrix;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return null;
//		}*/
		try {
	        DoubleMatrix matrix = MathUtils.createDoubleMatrix(
	        		list.get(0).length(), list.size());
	        for (int j = 0; j < list.size(); j++) {
	            DoubleArray array = list.get(j);
	            for (int i = 0; i < array.length(); i++) {
	                matrix.set(i, j, array.get(i));
	            }
	        }
	        return matrix;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
	}
	public DoubleMatrix createDoubleMatrix() {
		try {
	        DoubleMatrix matrix = MathUtils.createDoubleMatrix(
	        		list.get(0).length(), list.size());
	        for (int i = 0; i < list.size(); i++) {
	            DoubleArray array = list.get(i);
	            for (int j = 0; j < array.length(); j++) {
	                matrix.set(i, j, array.get(j)); 
	            }
	        }
	        return matrix;
    	} catch(Exception e) {
    		return null;
    	}
		
	}
	public void execute(Object[] src, TimeSeriesCompatible[] dest) throws InterruptedException {
		ComplexArray cmpArray = (ComplexArray)src[0];
		DoubleArray dblArray = this.convertComplexArrayToDoubleArray(cmpArray);
		
		list.add(dblArray);

		list.add(dblArray);

		dest[0].add(dblArray);
	}

	public Class[] getInputClasses() {
		return new Class[] { ComplexArray.class };
	}
	public Class[] getOutputClasses() {
		return new Class[] { DoubleArray.class };
	}

}