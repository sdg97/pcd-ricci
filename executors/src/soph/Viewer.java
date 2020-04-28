package soph;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import soph.Master;

public class Viewer extends JFrame {

	private static final long serialVersionUID = -7360268664871854615L;

	private JPanel panelTop;
	private JPanel panelDown;
	private JButton play;
	private JLabel label;
	private SpinnerModel spModel;
	private JSpinner spinner;
	private JTextField link = new JTextField(70);

	public Viewer(int w, int h, Master master) throws InterruptedException {
		this.setTitle("Viewer");
		this.setSize(w,h);
		this.setResizable(true);

		this.panelTop = new JPanel();
		this.add(panelTop, BorderLayout.NORTH);
		this.panelDown = new JPanel();
		this.add(panelDown, BorderLayout.SOUTH);

		this.label = new JLabel("Insert depth: ");

		this.spModel = new SpinnerNumberModel(1, 1, 15, 1);     
		spinner = new JSpinner(spModel);

		this.play = new JButton("Play");
		this.play.addActionListener((ActionEvent e) -> {
			try {
				master.compute();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		this.panelTop.add(link);
		this.panelTop.add(label);
		this.panelTop.add(spinner);
		this.panelTop.add(play);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
		});
		this.setVisible(true);
	}


}
