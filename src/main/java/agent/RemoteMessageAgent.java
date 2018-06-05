package agent;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class RemoteMessageAgent extends GuiAgent {

	private RemoteMessageAgentGUI remoteMessageGUI;
	private String receiver = "";
	private String content = "";
	private String messagePerformative = "";
	private String filename = "messageHistory.txt";
	private String fullConversationText = "";
	public ArrayList<String> agentList;
	public static int agentCounterInitial = 0;
	public static int agentCounterFinal = 0;

    public RemoteMessageAgentGUI getRemoteMessageGUI() {
        return remoteMessageGUI;
    }

    public void setup() {

		remoteMessageGUI = new RemoteMessageAgentGUI(this);
		remoteMessageGUI.displayGUI();

		System.out.println("Messenger agent " + getAID().getName() + " is ready.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("remote-messenger-agent");
		sd.setName(getLocalName() + "-Messenger agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ReceiveMessage());

	}

	protected void takedown(){

		// Dispose the GUI if it is there
		if (remoteMessageGUI != null) {
			remoteMessageGUI.dispose();
		}

		// Printout a dismissal message
		System.out.println("Agent " + getAID().getName() + " is terminating.");

		try {
			DFService.deregister(this);
			System.out.println("Agent " + getAID().getName() + " has been signed off.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public class SendMessage extends OneShotBehaviour {

		public void action() {
			ACLMessage msg;
			if (messagePerformative.equals("Propose")) {
				msg = new ACLMessage(ACLMessage.PROPOSE);
			} else {
				msg = new ACLMessage(ACLMessage.REQUEST);
			}
			AID r = new AID();
			r.setName("R@Platform2");
			msg.addReceiver(r);
			msg.setLanguage("English");
			msg.setContent(content);
			msg.setConversationId(messagePerformative);
			send(msg);
			fullConversationText += "\nMe: " + msg.getContent();
			remoteMessageGUI.setMessageTextArea(fullConversationText);

			System.out.println(getAID().getName() + " sent a message to " + receiver + "\n"
					+ "Content of the message is: " + msg.getContent());
		}
    }

	public class ReceiveMessage extends CyclicBehaviour {

		private String messagePerformative;
		private String messageContent;
		private String SenderName;
		private String MyPlan;

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {

                messagePerformative = msg.getConversationId();
				messageContent = msg.getContent();
				SenderName = msg.getSender().getLocalName();

				System.out.println("|||| " + getAID().getLocalName() + " received a tweet" + "\n" + "User sender name: "
						+ SenderName + "\n" + "Tweet: " + messageContent + "\n" + "Message Label: "
						+ messagePerformative + "\n");
				System.out.println("||||||||||||||||||||||||||||||||||");

				fullConversationText += "\n" + SenderName + ": " + messageContent;
				remoteMessageGUI.setMessageTextArea(fullConversationText);
			}
		}
	}


	public void getFromGui(final String messageType, final String dest, final String messages) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				messagePerformative = messageType;
				receiver = dest;
				content = messages;
			}
		});
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		addBehaviour(new SendMessage());

	}

}
