import jp.crestmuse.cmx.amusaj.sp.*;
import jp.crestmuse.cmx.math.*;
import java.util.*; 
import static java.lang.Math.*;

public class InputChecker {
	public void execute(Object[] src, TimeSeriesCompatible[] dest) {
        System.out.println(src[0].getClass());
    }
	public Class[] getInputClasses() {
		return new Class[] { Object.class };
	}
	public Class[] getOutputClasses() {
		return new Class[] { };
	}
}
