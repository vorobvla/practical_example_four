/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import Model.Player;
import Networking.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Vladimir Vorobyev (vorobvla)
 */
public class StartPanel extends javax.swing.JPanel {
    
    
    private class PlayerListModel extends AbstractListModel{

            @Override
            public int getSize() {
                return Player.getAll().size();
            }

            @Override
            public Object getElementAt(int i) {
                return Player.getAll().get(i).getIdentity();
            }
            
            public void update() {
                this.fireContentsChanged(this, 0, Player.getAll().size() - 1);
            }
    }


    /**
     * Creates new form StartPanel
     * player refresh.
     */
    public StartPanel() {
        
        initComponents(); 
        playersList.setEnabled(true);
        playersList.setModel(new PlayerListModel());
         
    }
    
    public void launchPlayerRefresh(ScheduledThreadPoolExecutor scheduler){
        // active waiting -- bad, but unavoidable evil :(
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ((PlayerListModel)playersList.getModel()).update();                
                startGameButton.setEnabled(!Player.getAll().isEmpty());
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);   
    }
    
    public  void updatePlayers(){
        playersList.updateUI();
    }
 

    public JButton getStartGameButton() {
        return startGameButton;
    }

    public JButton getSetupGameButton() {
        return setupGameButton;
    }

    public JButton getSetupSystemButton() {
        return setupSystemButton;
    }

    public JButton getSetupNetworkButton() {
        return setupNetworkButton;
    }
   
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        callPlayersButton = new javax.swing.JButton();
        setupNetworkButton = new javax.swing.JButton();
        setupSystemButton = new javax.swing.JButton();
        startGameButton = new javax.swing.JButton();
        setupGameButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        playersList = new javax.swing.JList();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Start Panel"));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        setLayout(layout);

        callPlayersButton.setText("Call players");
        callPlayersButton.setToolTipText("Call the ready players to connect to the system");
        callPlayersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                callPlayersButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        add(callPlayersButton, gridBagConstraints);

        setupNetworkButton.setText("Setup Network");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        add(setupNetworkButton, gridBagConstraints);

        setupSystemButton.setText("Setup System");
        setupSystemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupSystemButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        add(setupSystemButton, gridBagConstraints);

        startGameButton.setText("Start game");
        startGameButton.setToolTipText("Start game with the connected players");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        add(startGameButton, gridBagConstraints);

        setupGameButton.setText("Setup Game");
        setupGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupGameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        add(setupGameButton, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Connected Players"));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(60, 252));

        playersList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        playersList.setDragEnabled(false);
        playersList.setSelectionInterval(-1, -1);
        playersList.setModel(new javax.swing.AbstractListModel() {
            public int getSize() { return Player.getAll().size(); }
            public Object getElementAt(int i) { return Player.getAll().get(i).getIdentity(); }

        });
        playersList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        playersList.setFocusable(false);
        playersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playersListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(playersList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        this.getParent().dispatchEvent(evt);
    }//GEN-LAST:event_startGameButtonActionPerformed

    private void callPlayersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_callPlayersButtonActionPerformed
        try{
            Networking.getInstance().callPlayers();   
        }
        catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this.getRootPane(), ex.getMessage());
        }
    }//GEN-LAST:event_callPlayersButtonActionPerformed

    private void setupSystemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupSystemButtonActionPerformed
        Player.getAll().add(new Player("test", null));
    }//GEN-LAST:event_setupSystemButtonActionPerformed

    private void playersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playersListMouseClicked
        playersList.updateUI();
        // TODO add your handling code here:
    }//GEN-LAST:event_playersListMouseClicked

    private void setupGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupGameButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setupGameButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton callPlayersButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList playersList;
    private javax.swing.JButton setupGameButton;
    private javax.swing.JButton setupNetworkButton;
    private javax.swing.JButton setupSystemButton;
    private javax.swing.JButton startGameButton;
    // End of variables declaration//GEN-END:variables
}