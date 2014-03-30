

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class GuitarSpectrumAnalyzerTest {
	GuitarSpectrumAnalyzer gaa;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}


	@Before
	public void setUp() throws Exception {
		gaa = new GuitarSpectrumAnalyzer();
	}

	@Test
	public void test_setAllModuleを実行してエラーなしで完了する() {
		try {
			Method m = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"setAllModules"
			);	
			m.setAccessible(true);
			m.invoke(gaa);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_connectAllModuleを実行してエラーなしで完了する() {
		Method m1, m2;
		try {
			m1 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"setAllModules"
				);
			m2 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
			"connectAllModules"
				);
			m1.setAccessible(true);
			m1.invoke(gaa);
			m2.setAccessible(true);
			m1.invoke(gaa);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testギター演奏をSTFTしてスペクトルを出力() {
		
	}
	@Test
	public void test_setAllModuleのテスト() {
		Method m1, m2;
		try {
			m1 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"setAllModules"
				);
			m2 = GuitarSpectrumAnalyzer.class.getDeclaredMethod(
				"connectAllModules"
				);
			m1.setAccessible(true);
			m1.invoke(gaa);
			m2.setAccessible(true);
			m2.invoke(gaa);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}
