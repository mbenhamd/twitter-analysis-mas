package agent;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import java.security.MessageDigest;

public class MessageAgent extends GuiAgent {
	
	private MessageAgentGui messageGUI;
	private String receiver = "";
	private String content = "";
    private String messagePerformative= "";
	public String fullConversationText = "";


	public	ArrayList<String> agentList;
	public static int agentCounterInitial = 0;
	public static int agentCounterFinal = 0;

    public String getFullConversationText() {
        return fullConversationText;
    }

    protected void setup() {
		// Printout a welcome message
		System.out.println("Messenger agent "+getAID().getName()+" is ready.");
		agentList = new ArrayList();
		refreshActiveAgents();

		messageGUI = new MessageAgentGui(this);
		messageGUI.displayGUI();

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("messenger-agent");
		sd.setName(getLocalName()+"-Messenger agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ReceiveMessage());
	}


	protected void takeDown() {

		if (messageGUI != null) {
			messageGUI.dispose();
		}

		System.out.println("Agent "+getAID().getName()+" is terminating.");

		try {
			DFService.deregister(this);
			System.out.println("Agent "+getAID().getName()+" has been signed off.");
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public class SendMessage extends OneShotBehaviour {

		public void action() {
            AnalyseAgent ag = new AnalyseAgent();
			ACLMessage msg = new ACLMessage();
			msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
			msg.setConversationId(messageGUI.messageType);
			msg.setContent(content);
			send(msg);

			fullConversationText += "\nMe: "+ msg.getContent();
			messageGUI.setMessageTextArea(fullConversationText);


            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            String date = sdf.format(new Date());


			System.out.println(getAID().getName()+" sent a tweet to "+receiver+"\n"+
					"Tweet: "+ msg.getContent());
            agent.Agent a = new Agent(getAID().getLocalName(),content,ag.getSaltString(),date,messagePerformative);
            AnalyseAgent.history.add(a);

		}
	}


	public class ReceiveMessage extends CyclicBehaviour {

		private String messagePerformative;
		private String messageContent;
		private String SenderName;
		private String MyPlan;

		public void action() {
			ACLMessage msg = receive();
			if(msg != null) {
				messagePerformative = msg.getConversationId();
				messageContent = msg.getContent();
				SenderName = msg.getSender().getLocalName();

				System.out.println("|||| " + getAID().getLocalName() + " received a message" +"\n"+
						"Sender name: "+ SenderName+"\n"+
						"Content of the message: " + messageContent + "\n"+
						"Message type: " + msg.getConversationId() + "\n");
                System.out.println("||||||||||||||||||||||||||||||||||");

				fullConversationText += "\n"+SenderName+": "+messageContent;
				messageGUI.setMessageTextArea(fullConversationText);
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
		} );
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		addBehaviour(new SendMessage());

	}

	public void refreshActiveAgents(){

		AMSAgentDescription [] agents = null;

	    try {
	        SearchConstraints c = new SearchConstraints();
	        c.setMaxResults ( new Long(-1) );
	        agents = AMSService.search( this, new AMSAgentDescription (), c );
	    }
	    catch (Exception e) {  }

	    for (int i=0; i<agents.length;i++){
	        AID agentID = agents[i].getName();
	        if(agentID.getLocalName().equals("ams") || agentID.getLocalName().equals("rma") || agentID.getLocalName().equals("df"))
	        	continue;
	        agentList.add(agentID.getLocalName());
	    }

	}

	public void listActiveAgents(){

		for(String agent: agentList)
			System.out.println("Active:"+agent);

	}

}
