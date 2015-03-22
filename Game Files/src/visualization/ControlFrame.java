package visualization;

import gameboard.Gameboard;
import gameboard.GlobalGameboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import antServer.GameRunner;
import antServer.TournamentRunner;

/**
 * 
 * @author Marek Lipczak || lipczak@cs.dal.ca || www.cs.dal.ca/~lipczak/
 * @version 1.0 || 2009-02-11
 *
 */

 /*
  * Whenever you modify the code please describe your contribution here:
  *
 */

public class ControlFrame extends JFrame
{
	JGameboardFrame vizFrame;
	
	private JList list;
    private DefaultListModel listModel;
    
    JRadioButton player1Button;
    JRadioButton player2Button;
    JLabel nrOfRoundsLabel;
    JCheckBox doVisualization;
    
    int[] points = new int[2];
    String[] names = new String[2];
    
    GameRunner gameRunner;
    TournamentRunner tournamentRunner;
	
	public ControlFrame(JGameboardFrame frame)
	{
		vizFrame = frame;
		
		setTitle("Control Window");
        setSize(new Dimension(GlobalGameboard.windowWidthControl, GlobalGameboard.windowHeightControl));
        setLocation(0, 0);
        setResizable(false);
						
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	ControlFrame.this.windowClosed();
                }
            }
        ); 
	   	
		final JLabel otherLabel = new JLabel("Vizualization:");
				
		doVisualization = new JCheckBox("vizualize", false); 
		doVisualization.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent evt)
			{
				if(doVisualization.isSelected()) vizFrame.setVisible(true);
		    	else						     vizFrame.setVisible(false);
			}
		});
								
		listModel = new DefaultListModel();
       
		nrOfRoundsLabel = new JLabel("Nr of Rounds: 0");

		list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(new ListSelectionListener()
		{  
			public void valueChanged(ListSelectionEvent evt)
			{
				int index = evt.getFirstIndex();
												
				if(player1Button.isSelected())
				{
					player1Button.setText("Player1: "+(String)list.getSelectedValue()+" ("+points[0]+")");
					names[0] = (String)list.getSelectedValue();
					tournamentRunner.setCurrentPlayer(0, (String)list.getSelectedValue());
				}
				
				if(player2Button.isSelected())
				{
					player2Button.setText("Player2: "+(String)list.getSelectedValue()+" ("+points[1]+")");
					names[1] = (String)list.getSelectedValue();
					tournamentRunner.setCurrentPlayer(1, (String)list.getSelectedValue());
				}
			}
		});
        list.setVisibleRowCount(20);
        JScrollPane listScrollPane = new JScrollPane(list);

		
		final JLabel currentPlayersLabel = new JLabel("Current players:");
		
		player1Button = new JRadioButton("Player1: empty", false); 
		player1Button.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent evt)
			{
				
			}
		});
		
		player2Button = new JRadioButton("Player2: empty", false); 
		player2Button.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent evt)
			{
				
			}
		});
		
		ButtonGroup typeGroup = new ButtonGroup();
		typeGroup.add(player1Button);
		typeGroup.add(player2Button);
					
		JButton startGameButton = new JButton("START SINGLE GAME");
		startGameButton.addActionListener(new ActionListener()
			{  
				public void actionPerformed(ActionEvent evt)
				{
					gameRunner.startSingleGame();
				}
			});
						
		JButton startTournamentButton = new JButton("RUN TOURNAMENT");
		startTournamentButton.addActionListener(new ActionListener()
			{  
				public void actionPerformed(ActionEvent evt)
				{
					tournamentRunner.startTournament();
				}
			});
		
		JPanel mainPanel = new JPanel();
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addComponent(listScrollPane)  
				      .addComponent(currentPlayersLabel)
				      .addComponent(player1Button)
				      .addComponent(player2Button)
				      .addComponent(nrOfRoundsLabel)
				      .addComponent(otherLabel)
				      .addComponent(doVisualization)
				      .addComponent(startGameButton)
				      .addComponent(startTournamentButton)
				);
		
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				   		  .addComponent(listScrollPane)
				   		  .addComponent(currentPlayersLabel)
					      .addComponent(player1Button)
					      .addComponent(player2Button)
					      .addComponent(nrOfRoundsLabel)
					      .addComponent(otherLabel)
					      .addComponent(doVisualization)
					      .addComponent(startGameButton)
			    		  .addComponent(startTournamentButton))				
				);
		
		
		
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
	
	 protected void windowClosed() {
	    	
	    	// TODO: Check if it is safe to close the application
	    	
	        // Exit application.
	        System.exit(0);
	    }

	public void addGameRunner(GameRunner gameRunner)
	{
		this.gameRunner = gameRunner;
	}
	
	public void addTournamentRunner(TournamentRunner tournamentRunner)
	{
		this.tournamentRunner = tournamentRunner;
	}

	public void addPlayer(String name)
	{
		 listModel.addElement(name);
		 
		 if(listModel.size() == 1)
		 {
			 player1Button.setText("Player1: "+name+" ("+points[0]+")");
			 names[0] = name;
			 tournamentRunner.setCurrentPlayer(0, name);
		 }
		 else if(listModel.size() == 2)
		 {
			 player2Button.setText("Player2: "+name+" ("+points[1]+")");
			 names[1] = name;
			 tournamentRunner.setCurrentPlayer(1, name);
		 }
	}

	public void updateGameboard(Gameboard gameboard)
	{
		vizFrame.updateGameboard(gameboard);		
	}

	public void setPoints(int playerPoints, int playerIndex)
	{
		points[playerIndex] = playerPoints;
		if(playerIndex == 0) player1Button.setText("Player1: "+names[0]+" ("+points[0]+")");
		if(playerIndex == 1) player2Button.setText("Player2: "+names[1]+" ("+points[1]+")");
	}
	
	public void setPlayerName(int index, String name)
	{
		names[index] = name;
	}
	
	public boolean isVizualization()
	{
		return doVisualization.isSelected();
	}

	public void setNrOfRounds(int nrOfRounds)
	{
		nrOfRoundsLabel.setText("Nr of Rounds: "+nrOfRounds);
		repaint();
	}
}
