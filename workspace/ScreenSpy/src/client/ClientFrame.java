package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;

import utils.Constants;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame window = new ClientFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	private ChatClient chatClient;
	private ScreenSpyClient screenSpyClient;
	private boolean isConnect=false;
	private String serverAddr=Constants.SERVER_IP;
	/**
	 * Create the application.
	 */
	public ClientFrame() {
		
		
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u7F51\u7EDC\u5B9E\u65F6\u76D1\u63A7\u7CFB\u7EDF\u5BA2\u6237\u7AEF");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					if(screenSpyClient!=null)
						screenSpyClient.close();
					if(chatClient!=null)
						chatClient.close();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isConnect=false;
				
				System.exit(0);
				
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane(panel_1);
		panel_4.add(scrollPane);
		final JTextArea textArea_console = new JTextArea();
		textArea_console.setEditable(false);
		textArea_console.setWrapStyleWord(true);
		textArea_console.setLineWrap(true);
		
			panel_1.add(textArea_console,BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		
		
		JScrollPane scrollPane_1 = new JScrollPane(panel_3);
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		final JTextArea textArea_message = new JTextArea();
		textArea_message.setWrapStyleWord(true);
		textArea_message.setLineWrap(true);
		panel_3.add(textArea_message);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textArea_message.getText()==null||textArea_message.getText().length()==0){
					JOptionPane.showMessageDialog(frame, "Message is null.");
				}
				else{
					chatClient.sendMessage(textArea_message.getText());
					textArea_console.append("You say: "+textArea_message.getText()+"\n");
					textArea_message.setText("");
				}
			}
		});
		panel_2.add(btnSend, BorderLayout.SOUTH);
		
		
		
		JPanel panel_5 = new JPanel();
		frame.getContentPane().add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		final JButton btnConnect = new JButton(" Connect ");
		
		btnConnect.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnConnect.setIcon(new ImageIcon("icon\\network_links.png"));
		panel_5.add(btnConnect);
		
		final JButton btnDisconnect = new JButton("Disconnect");
		
		btnDisconnect.setEnabled(false);
		btnDisconnect.setIcon(new ImageIcon("icon\\remove.png"));
		btnDisconnect.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		panel_5.add(btnDisconnect);
		
		final JButton btnSetting = new JButton(" Setting ");
		btnSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//打开输入对话框
				String addr=JOptionPane.showInputDialog(null, "Please input server address(xxx.xxx.xxx.xxx)", "Server address", JOptionPane.OK_CANCEL_OPTION);
				if(addr!=null){
					StringTokenizer st=new StringTokenizer(addr,".");
					try{
						int ip1=Integer.parseInt(st.nextToken());
						int ip2=Integer.parseInt(st.nextToken());
						int ip3=Integer.parseInt(st.nextToken());
						int ip4=Integer.parseInt(st.nextToken());
						if(0<ip1&&ip1<255&&0<=ip2&&ip2<255&&0<=ip3&&ip3<255&&0<=ip4&&ip4<255){
							serverAddr=addr;
							textArea_console.append("Set Server ip to: "+serverAddr+"\n");
						}
						else{
							JOptionPane.showMessageDialog(null, "format error, modification invalid", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
					catch (Exception e) {
						// TODO: handle exception
						JOptionPane.showMessageDialog(null,  "format error, modification invalid", "Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		final JButton btnSendFile = new JButton("Send File");
		btnSendFile.setEnabled(false);
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser=new JFileChooser();
				int flag=fileChooser.showOpenDialog(frame);
				if(flag==JFileChooser.APPROVE_OPTION){
					
					File file=fileChooser.getSelectedFile();
					String remotePath=Constants.SERVER_PATH+file.getName();
					textArea_console.append("Send File: "+file.getAbsolutePath());
					new FileTransporter().sendFile(serverAddr, Constants.FILE_SERV_PORT, file, remotePath);
				}
			}
		});
		btnSendFile.setIcon(new ImageIcon("icon\\favorits.png"));
		btnSendFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		panel_5.add(btnSendFile);
		btnSetting.setIcon(new ImageIcon("icon\\settings.png"));
		btnSetting.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		panel_5.add(btnSetting);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				textArea_console.append("Connect to Server :"+serverAddr+"\n");
				screenSpyClient=new ScreenSpyClient(serverAddr);
				chatClient=new ChatClient(textArea_console,serverAddr);
				try {
					chatClient.connect();
					screenSpyClient.connect();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isConnect=true;
				btnConnect.setEnabled(false);
				btnDisconnect.setEnabled(true);
				btnSendFile.setEnabled(true);
			}
		});
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					screenSpyClient.close();
					chatClient.close();
					textArea_console.append("Disconnect to Server :"+serverAddr+"\n");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isConnect=false;
				btnConnect.setEnabled(true);
				btnDisconnect.setEnabled(false);
				btnSendFile.setEnabled(false);
			}
		});
	}

}
