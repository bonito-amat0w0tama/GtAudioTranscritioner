import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jp.crestmuse.cmx.math.*;

public class ComplexSpectrumToAmplitudeSpectrumConverterTest {
	GuitarSpectrumAnalyzer gaa = new GuitarSpectrumAnalyzer();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		try {
			Method m1 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"setAllModules"
			);	
			Method m2 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"connectAllModules"
			);	
			m1.setAccessible(true);
			m1.invoke(gaa);
			m2.setAccessible(true);
			m2.invoke(gaa);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test入力されたスペクトルが複素数のスペクトルかどうかチェック() {


	}

}
