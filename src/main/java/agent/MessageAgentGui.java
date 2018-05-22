package agent;



import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MessageAgentGui extends JFrame {
	private MessageAgent messageAgent;
	public String messageType = "Positive";
	String title = "";

	JTextField messageContent;
	JTextArea messageViewer;
	JComboBox messageTypes, receivers;

	JFrame mainFrame;
	JLabel headerLabel;
	JLabel statusLabel;
	JPanel controlPanel;
	JLabel msglabel;
	JButton sendMessageBtn;
	JButton finishMessageBtn;

	JLabel contentTFLabel , messagesTALabel , messageTypeLable, receiverLabel;

	ArrayList<String> messageTypesList;
	ArrayList<String> receiversList;

	public MessageAgentGui(MessageAgent a) {
		super(a.getLocalName());

		messageAgent = a;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				messageAgent.doDelete();
			}
		} );

		messageTypesList = new ArrayList();
		receiversList = new ArrayList();

		messageTypesList.add("Positive");
        messageTypesList.add("Neutral");
		messageTypesList.add("Negative");


		for(String agentName : messageAgent.agentList){
			if(messageAgent.getLocalName().equals(agentName) || receiversList.contains(agentName))
				continue;
			receiversList.add(agentName);
		}

		messageContent = new JTextField();
		messageContent.setPreferredSize(new Dimension(400, 30));

		messageViewer = new JTextArea(15, 30);
		messageViewer.setEditable(true);
		JScrollPane scrollPane = new JScrollPane(messageViewer);

		messageTypes = new JComboBox(messageTypesList.toArray());
		messageTypes.setPreferredSize(new Dimension(400,20));

		messageTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
                    messageType = "Positive";
				Object selected = messageTypes.getSelectedItem();
				if(selected.toString().equals("Positive"))
					messageType = "Positive";
                else if(selected.toString().equals("Neutral"))
                    messageType = "Neutral";
				else if(selected.toString().equals("Negative"))
					messageType = "Negative";

			}
		});

		updateReceiverCombo();


		messageTypeLable  = new JLabel("Sentiment Label: ");
		messageTypeLable.setPreferredSize(new Dimension(400, 20));

		receiverLabel = new JLabel("Beneficiary: ");
		receiverLabel.setPreferredSize(new Dimension(400, 20));

		contentTFLabel = new JLabel("Tweet: ");
		contentTFLabel.setPreferredSize(new Dimension(400, 20));

		messagesTALabel = new JLabel("Full Tweet History: ");
		messagesTALabel.setPreferredSize(new Dimension(400, 20));

		sendMessageBtn = new JButton("Send");
		sendMessageBtn.setPreferredSize(new Dimension(200, 50));

		finishMessageBtn = new JButton("Finish Conversation");
		finishMessageBtn.setPreferredSize(new Dimension(200, 50));

		headerLabel = new JLabel("",JLabel.CENTER);
		statusLabel = new JLabel("",JLabel.CENTER);

		controlPanel = new JPanel();

		controlPanel.add(messageTypeLable);
		controlPanel.add(messageTypes);
		controlPanel.add(messageContent);
		controlPanel.add(receiverLabel);
		controlPanel.add(receivers);
		controlPanel.add(contentTFLabel);
		controlPanel.add(messageContent);
		controlPanel.add(messagesTALabel);
		controlPanel.add(finishMessageBtn);
		controlPanel.add(scrollPane);

        finishMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("\n");
                AnalyseAgent a = new AnalyseAgent();
                a.main();
            }
        });

		sendMessageBtn.addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent ae) {
				try {
					String content = messageContent.getText().trim();
					messageAgent.getFromGui(
							messageType, receivers.getSelectedItem().toString(), content
					);

					messageContent.setText("");

					GuiEvent guiEvent = new GuiEvent(this, 1);
					messageAgent.postGuiEvent(guiEvent);
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(MessageAgentGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} );

		controlPanel.add(sendMessageBtn);

		Container contentPane = getContentPane();
		contentPane.setPreferredSize(new Dimension(400, 550));
		getContentPane().add(controlPanel, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				messageAgent.doDelete();
			}
		} );
	}

	public void setMessageTextArea(String text){
		messageViewer.setText(text);
	}
	public void displayGUI() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}

	public void updateReceiverCombo(){

		receivers = new JComboBox(receiversList.toArray());
		receivers.setPreferredSize(new Dimension(400, 30));
	}
}
