import java.io.IOException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

import groovy.sql.DataSet;
import jp.crestmuse.cmx.filewrappers.*;
import jp.crestmuse.cmx.filewrappers.SCCDataSet.Part;
import jp.crestmuse.cmx.processing.CMXController;


public class pilotSmf {
	public static void main(String[] args) {
		SCCDataSet dataSet = new SCCDataSet(480);
//		Part part1 = dataSet.addPart(10, 0, 10, 100);
		Part part1 = dataSet.addPart(1, 10, 1, 100);
		dataSet.addHeaderElement(0, "TEMPO", "120");
		int len = 4 * 16;
		
		for (int i = 0; i < len; i++) {
			int onval = i * 480;
			int offval = onval + 480;
			part1.addNoteElement(onval, offval, 32, 100, 0);
//			part1.addNoteElement(onval, offval, 32, 100, 0);
		}
		
		try {
			SCCXMLWrapper scc = dataSet.toWrapper();
			String filePath = "xml/test.xml";
			scc.finalizeDocument();
			scc.writefile(filePath);
			MIDIXMLWrapper midi = scc.toMIDIXML();
			midi.writefileAsSMF("smf/count.mid");
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ファイルの書き出し");
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
