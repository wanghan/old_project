package server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;

import utils.Constants;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame window = new ServerFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private ScreenSpyServer screenSpyServer;
	private ChatServer chatServer;
	private FileServer fileServer;

	/**
	 * Create the application.
	 */
	public ServerFrame() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u7F51\u7EDC\u5B9E\u65F6\u76D1\u63A7\u7CFB\u7EDF\u670D\u52A1\u5668");
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
//		Toolkit tool=Toolkit.getDefaultToolkit();
//		try {
//			BufferedImage image=new Robot().createScreenCapture(new Rectangle(tool.getScreenSize()));
//			Image newImage=image.getScaledInstance(800, 600,image.SCALE_SMOOTH);
//			ScreenPanel panel=new ScreenPanel("asdasd");
//			panel.setImage(newImage);
//			tabbedPane.add(panel);
//		} catch (HeadlessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AWTException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			
			JPanel panel = new JPanel();
			frame.getContentPane().add(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{504, 504, 0};
			gbl_panel.rowHeights = new int[]{730, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			
			JPanel panel_console = new JPanel();
			panel_console.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel_console = new GridBagConstraints();
			gbc_panel_console.fill = GridBagConstraints.BOTH;
			gbc_panel_console.insets = new Insets(0, 0, 0, 5);
			gbc_panel_console.gridx = 0;
			gbc_panel_console.gridy = 0;
			panel.add(panel_console, gbc_panel_console);
			GridBagLayout gbl_panel_console = new GridBagLayout();
			gbl_panel_console.columnWidths = new int[]{492, 0};
			gbl_panel_console.rowHeights = new int[]{443, 350, 0};
			gbl_panel_console.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel_console.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			panel_console.setLayout(gbl_panel_console);
			
			JTextArea textArea = new JTextArea();
			textArea.setEditable(false);
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.weighty = 100.0;
			gbc_textArea.weightx = 100.0;
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.insets = new Insets(0, 0, 5, 0);
			gbc_textArea.gridx = 0;
			gbc_textArea.gridy = 0;
			panel_console.add(textArea, gbc_textArea);
			
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.weighty = 100.0;
			gbc_panel_1.weightx = 100.0;
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 0;
			gbc_panel_1.gridy = 1;
			panel_console.add(panel_1, gbc_panel_1);
			panel_1.setLayout(new BorderLayout(0, 0));
			
			JPanel panel_2 = new JPanel();
			panel_1.add(panel_2);
			panel_2.setLayout(new BorderLayout(0, 0));
			
			final JTextArea textArea_message = new JTextArea();
			panel_2.add(textArea_message);
			
			JPanel panel_3 = new JPanel();
			panel_1.add(panel_3, BorderLayout.SOUTH);
			
			JButton btnSend = new JButton("Send");
			
			panel_3.add(btnSend);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
			gbc_tabbedPane.weighty = 100.0;
			gbc_tabbedPane.weightx = 100.0;
			gbc_tabbedPane.fill = GridBagConstraints.BOTH;
			gbc_tabbedPane.gridx = 1;
			gbc_tabbedPane.gridy = 0;
			panel.add(tabbedPane, gbc_tabbedPane);
			
			
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(textArea_message.getText()==null||textArea_message.getText().length()==0){
						JOptionPane.showMessageDialog(frame, "Message is null.");
					}
					else{
						chatServer.setMessage(textArea_message.getText());
						textArea_message.setText("");
					}
				}
			});
			
			screenSpyServer=new ScreenSpyServer(Constants.SCREEN_SERVER_PORT,Constants.POOL_SIZE,tabbedPane);
			screenSpyServer.start(); 
			chatServer=new ChatServer(Constants.CHAT_SERVER_PORT,Constants.POOL_SIZE,textArea);
			chatServer.start();
			fileServer=new FileServer(Constants.FILE_SERV_PORT,Constants.POOL_SIZE,textArea);
			fileServer.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
