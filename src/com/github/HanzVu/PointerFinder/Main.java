package com.github.HanzVu.PointerFinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.github.HanzVu.PointerFinder.PointerFinder;

public class Main extends JFrame implements ActionListener {

	/*GUI GLOBALS*/
	static JFrame frame;
	JPanel labels, textfields, buttons;
	JLabel L_dump1, L_dump2, L_code1, L_code2, L_maxOff, L_DMAlevel;
	JButton loaddump1, loaddump2, start;
	JTextField T_dump1, T_dump2, T_code1, T_code2, T_maxOff, T_DMAlevel;
	JTextArea output;
	JScrollPane outputScroll;
	
	/* */
	private File dump1;
	private File dump2;
	private int code1;
	private int code2;
	private int DMAlevel = 1;
	private int maxOffset = 0x8000;
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                launchGui();
            }
        });

	}

	private JLabel newLabel(JLabel label, String title, int width, int height, int x, int y){
		label = new JLabel(title);
		label.setSize(width, height);
		label.setLocation(x, y);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		labels.add(label);
		return label;
	}
	
	private JTextField newTextField(JTextField textField, String placeHolder, int width, int height, int x, int y, boolean editable){
		textField = new JTextField(placeHolder);
		textField.setSize(width, height);
		textField.setLocation(x, y);
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setEditable(editable);
		textfields.add(textField);
		
		return textField;
	}
	
	private JButton newButton(JButton button, String title, int width, int height, int x, int y){
		button = new JButton(title);
		button.setSize(width, height);
		button.setLocation(x, y);
		button.addActionListener(this);
		buttons.add(button);
		return button;
	}
	
	private JPanel createContent(){
		JPanel gui = new JPanel();
		gui.setLayout(null);
		
		labels = new JPanel();
		labels.setLocation(0, 0);
		labels.setSize(75, 130);
		labels.setLayout(null);
		gui.add(labels);
		
		int L_Width = 75;
		int L_Height = 20;
		L_dump1 = newLabel(L_dump1, "dump1: ",L_Width, L_Height, 0, L_Height*0);
		L_code1 = newLabel(L_code1, "code1: ",L_Width, L_Height, 0, L_Height*1);
		L_dump2 = newLabel(L_dump2, "dump2: ",L_Width, L_Height, 0, L_Height*2);
		L_code2 = newLabel(L_code2, "code1: ",L_Width, L_Height, 0, L_Height*3);
	 	L_maxOff = newLabel(L_maxOff, "max offset: ",L_Width, L_Height, 0, L_Height*4);
		L_DMAlevel = newLabel(L_DMAlevel, "DMA level: ",L_Width, L_Height, 0, L_Height*5);
		
		
		textfields = new JPanel();
		textfields.setLocation(80, 0);
		textfields.setSize(100, 130);
		textfields.setLayout(null);
		gui.add(textfields);
		
		int T_Width = 100;
		int T_Height = L_Height;
		T_dump1 = newTextField(T_dump1, "dump1.ram", T_Width, T_Height, 0, T_Height*0, false);
		T_code1 = newTextField(T_code1, "00000000", T_Width, T_Height, 0, T_Height*1, true);
		T_dump2 = newTextField(T_dump2, "dump2.ram", T_Width, T_Height, 0, T_Height*2, false);
		T_code2 = newTextField(T_code2, "00000000", T_Width, T_Height, 0, T_Height*3, true);
		T_maxOff = newTextField(T_maxOff, "8000", T_Width, T_Height, 0, T_Height*4, true);
		T_DMAlevel = newTextField(T_DMAlevel, "1", T_Width, T_Height, 0, T_Height*5, true);
		
		buttons = new JPanel();
		buttons.setLocation(180, 0);
		buttons.setSize(80, 130);
		buttons.setLayout(null);
		gui.add(buttons);
		
		int B_Width = 80;
		int B_Height = L_Height;
		loaddump1 = newButton(loaddump1, "open", B_Width, B_Height, 0, B_Height*0);
		loaddump2 = newButton(loaddump2, "open", B_Width, B_Height, 0, B_Height*2);
		
		start = new JButton("Start");
		start.setLocation(0, 130);
		start.setSize(75, 20);
		start.addActionListener(this);
		gui.add(start);
		
		output = new JTextArea();
		output.setLocation(0,150);
		output.setSize(270, 220);
		
		outputScroll = new JScrollPane(output);
		outputScroll.setLocation(0,150);
		outputScroll.setSize(270, 220);
		outputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		outputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		gui.add(outputScroll);
		
		return gui;
	}
	
	private static void launchGui(){
		frame = new JFrame("Ponter Finder");
		
		Main gui = new Main();
		frame.setContentPane(gui.createContent());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(280, 400);
        frame.setResizable(false);
        frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == loaddump1){
			String filename = File.separator+"tmp";
			JFileChooser fc = new JFileChooser(new File(filename));

			int result = fc.showOpenDialog(frame);
			if (result == javax.swing.JFileChooser.APPROVE_OPTION){
				dump1 = fc.getSelectedFile();
				T_dump1.setText(dump1.getName());
				
			}
			
			
		} else if (event.getSource() == loaddump2){
			String filename = File.separator+"tmp";
			JFileChooser fc = new JFileChooser(new File(filename));

			int result = fc.showOpenDialog(frame);
			if (result == javax.swing.JFileChooser.APPROVE_OPTION){
				dump2 = fc.getSelectedFile();
				T_dump2.setText(dump2.getName());
			}
			
		} else if (event.getSource() == start){
				Worker<Object, Object> worker = new Worker<>();
				worker.execute();
		}
	}
	
	private class Worker<T,V> extends SwingWorker<T,V>{

		@Override
		protected T doInBackground() throws Exception {
			start.setEnabled(false);
			try { code1 = Integer.parseInt(T_code1.getText(), 16);}
			catch (NumberFormatException e){
				code1 = 0;
				T_code1.setText(String.format("%08X", code1));
				return null;
			}
			
			try { code2 = Integer.parseInt(T_code2.getText(), 16);}
			catch (NumberFormatException e){
				code2 = 0;
				T_code2.setText(String.format("%08X", code2));
				return null;
			}
			
			try { maxOffset = Integer.parseInt(T_maxOff.getText(), 16);}
			catch (NumberFormatException e){
				maxOffset = 0x8000;
				T_maxOff.setText(String.format("%x", maxOffset));
			}
			
			try { DMAlevel = Integer.parseInt(T_DMAlevel.getText());}
			catch (NumberFormatException e){
				DMAlevel = 1;
				T_DMAlevel.setText(String.format("%x", DMAlevel));
			}
			
			if (dump1 != null && dump2 != null && dump1.exists() && dump2.exists()){
				PointerFinder dmp1 = new PointerFinder(dump1, code1, maxOffset);
				PointerFinder dmp2 = new PointerFinder(dump2, code2, maxOffset);
				
				for (int i = 0; i < DMAlevel; i++){
					try {
						dmp1.findPointers();
						dmp2.findPointers();
					} catch (IOException e){
						return null;
					}
				}

				dmp1.comparePointers(dmp2);
				output.setText(dmp1.possiblePaths());
			}
	
			return null;
		}
		
		@Override
		public void done(){
			start.setEnabled(true);
		}
		
	}

}
