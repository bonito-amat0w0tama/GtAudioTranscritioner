import java.applet.Applet;
import java.awt.*;

import javax.swing.JFrame;

import processing.core.PApplet;
import jp.crestmuse.cmx.processing.CMXApplet;

public class TransNoteDisplay extends JFrame {
	private int width = 1000;
	private int height = 500;

	public TransNoteDisplay() {
		this.init();
//		this.processingPanelInit();

		// 内部のコンポーネントからサイズを決める
		this.getContentPane().setPreferredSize(new Dimension(width, height));
		this.pack();
	}

	private void init() {
		this.setTitle("TransNote");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setPanel(PApplet ap) { 
		ap.init();
		Container panel = this.getContentPane();
		panel.add(ap);
	}

	public static void main(String[] args) {
        TransNoteDisplay frame = new TransNoteDisplay();
        frame.setPanel(new TransNotePanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
