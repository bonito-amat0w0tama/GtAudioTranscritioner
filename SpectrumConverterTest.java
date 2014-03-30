import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.crestmuse.cmx.math.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;

public class SpectrumConverterTest {
	private GuitarAudioAnalyzer gsa = new GuitarSpectrumAnalyzer();
	private SpectrumConverter sc = new SpectrumConverter();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void 複素スペクトルを振幅スペクトルに変換() {
		ComplexArray cmp = MathUtils.createComplexArray(1);
		DoubleArray dbl = null;
		Double real = 2.0;
		Double imag = 3.0;
		cmp.set(0, real, imag);
		
		Method m;
		try {
			m = SpectrumConverter.class.getDeclaredMethod(
					"convertComplexArrayToDoubleArray",
					ComplexArray.class
					);
			m.setAccessible(true);
			dbl = (DoubleArray)m.invoke(sc, cmp);
		} catch(Exception e) {
		}
		Double sq = Math.sqrt(real * real + imag * imag);
		System.out.println("dbl" + dbl.get(0) + "\n" + "sq:" + sq);
		assertThat(dbl.get(0), is(sq));
	}
}
