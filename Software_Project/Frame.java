import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultStyledDocument;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	static public JTextPane text_editor;
	static public String text;
	static public DefaultStyledDocument doc;
	// ���� �����忡�� �����ϴ� �κ�
	
	synchronized static public void SetDocument(DefaultStyledDocument thread_doc)
	{
		doc = thread_doc;
		// ������ ����ȭ�� ���� �浹 ����
	}
	
	Frame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 400);
		setLocationRelativeTo(null);
		setTitle("TEST C Editor");
		
		new Highlighter();
		
		text_editor = new JTextPane(doc);
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu menu = new JMenu("����(F)");
		menuBar.add(menu);
		menu.setMnemonic(KeyEvent.VK_F);

		
		JMenuItem menuItem = new JMenuItem("�� ����");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				text_editor.setText("");
			}
		});
		menu.add(menuItem);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));

		JMenuItem menuItem_1 = new JMenuItem("����");
		menuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				File selectedFile = null;

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					selectedFile = chooser.getSelectedFile();

				StringBuffer sb = new StringBuffer();
				Scanner scan = null;
				try {
					scan = new Scanner(selectedFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					sb.append(line);
					sb.append("\n");
				}
				text_editor.setText(sb.toString());
			}
		});
		menu.add(menuItem_1);
		menuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK));

		JMenuItem mntmWw = new JMenuItem("����");
		mntmWw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				String str = null;
				File selectedFile = null;

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					selectedFile = chooser.getSelectedFile();

				FileWriter fw = null;
				try {
					fw = new FileWriter(selectedFile + ".txt");
					BufferedWriter bw = new BufferedWriter(fw);
					str = text_editor.getText();

					bw.write(str);
					bw.newLine();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		menu.add(mntmWw);
		mntmWw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));

		JMenuItem menuItem_2 = new JMenuItem("����");
		menuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(-1);
			}
		});
		menu.add(menuItem_2);
		menuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));

		JMenu menu_1 = new JMenu("�ҽ�(S)");
		menuBar.add(menu_1);
		menu_1.setMnemonic(KeyEvent.VK_S);

		JMenuItem menuItem_3 = new JMenuItem("��ȣ�˻�");
		menuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BraceChecker bc = new BraceChecker();
				if (bc.Checker(text_editor.getText()))
					JOptionPane.showMessageDialog(null, "��ȣ �̻� ����");
				else
					JOptionPane.showMessageDialog(null, bc.GetLineNum() + "��° �ٿ��� ������ ��ȣ�� ������ �ʾҽ��ϴ�.");
			}
		});
		menu_1.add(menuItem_3);
		menuItem_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,Event.CTRL_MASK));
		
		JMenuItem mntmDsadas = new JMenuItem("ã��");
		mntmDsadas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				KeywordFinder kf = new KeywordFinder();
				String find_keyword = JOptionPane.showInputDialog("ã������ �ϴ� �ܾ �Է��Ͻÿ�.");
				kf.finder(find_keyword);
			}
		});
		menu_1.add(mntmDsadas);
		mntmDsadas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,Event.CTRL_MASK));
		
		JScrollPane scrollPane = new JScrollPane(text_editor);
		getContentPane().add(scrollPane);
		this.setVisible(true);
	}
}
