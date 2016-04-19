/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Model.Game;
import Model.GameException;
import edu.cvut.vorobvla.bap.GameStateEnum;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Vladimir Vorobyev (vorobvla)
 */
public class ControlPanel extends javax.swing.JPanel {

    /**
     * Creates new form ControlPanel
     */
    public ControlPanel() {
        initComponents(); 
        Game.getInstance().setLog(new JTextOutputStream(logTextArea));
        acceptButton.setEnabled(false);
        denyButton.setEnabled(false);
        
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               proceedButtonActionPerformed();
            }        
        });
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               acceptButtonActionPerformed();
            }        
        });
        
        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               denyButtonActionPerformed();
            }        
        });
    }
    
    private void acceptButtonActionPerformed() {
        try {
            Game.getInstance().acceptAnswer();
        } catch (GameException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void denyButtonActionPerformed() {
        try {
            Game.getInstance().denyAnswer();
        } catch (GameException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void proceedButtonActionPerformed() {
        try {
            Game.proceed();
        } catch (GameException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setAnsweringPlayer() {
        try {
            answeringPlayerLabel.setText("Player " + Game.getAnsweringPlayer().getIdentity()       
                    + " answers");
            acceptButton.setText("Accept " + Game.getAnsweringPlayer().getIdentity() + 
                    "'s answer (+ " + Game.getInstance().getCurrentQuestion().getPrice() + ")");
            denyButton.setText("Deny " + Game.getAnsweringPlayer().getIdentity() + 
                    "'s answer (- " + Game.getInstance().getCurrentQuestion().getPrice() + ")");
        } catch (GameException ex) {
            answeringPlayerLabel.setText("");
            acceptButton.setText("Accept answer");
            denyButton.setText("Deny answer");
        }
    }

    
    
    private void enableAnswerProcessing(boolean opt){
        acceptButton.setEnabled(opt);
        denyButton.setEnabled(opt);
        proceedButton.setEnabled(!opt);
    }
    
    public void update(GameStateEnum state){
        switch(state){
            case START:
                tipLabel.setText("Press \"Proceed\" button to start the game.");
                break;
            case COOSING_QUESTION:
                tipLabel.setText("Press \"Proceed\" button to obtain next question.");
                setAnsweringPlayer();
                enableAnswerProcessing(false);
                finishBtn.setEnabled(true);
                break;
            case READING_QUESTION:
                finishBtn.setEnabled(false);
                tipLabel.setText("Read the question. Then, press \"Proceed\" button "
                        + "and announce that.");
                break;
            case AWAINTING_ANSWER:
                tipLabel.setText("Wait for player's applying");
                setAnsweringPlayer();
                enableAnswerProcessing(false);
                break;
            case ANSWER:
            { 
                try {
                    tipLabel.setText("Wait while the player "+
                        Game.getAnsweringPlayer().getIdentity() +" answers");
                } catch (GameException ex) {
                    Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                proceedButton.setEnabled(false);
                setAnsweringPlayer();
                break;
            case PROCESSING_ANSWER:
            {
                try {
                tipLabel.setText("Accept or deny " +
                        Game.getAnsweringPlayer().getIdentity() + " answer");
                } catch (GameException ex) {
                    Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                enableAnswerProcessing(true);
                break;
            case FINISH:
                tipLabel.setText("Game is finished. Press \"Exit\" button to return to Main menu.");
                finishBtn.setText("Exit");  
                finishBtn.setEnabled(true);
                break;
                    
        }
    }

    public JButton getFinishBtn() {
        return finishBtn;
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

        tipPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tipLabel = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        gameCtrl = new javax.swing.JPanel();
        finishBtn = new javax.swing.JButton();
        pauseBtn = new javax.swing.JButton();
        proceedButton = new javax.swing.JButton();
        answerCtrl = new javax.swing.JPanel();
        answeringPlayerLabel = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        denyButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Controls"));
        setMinimumSize(new java.awt.Dimension(300, 200));
        setPreferredSize(new java.awt.Dimension(300, 200));
        setLayout(new java.awt.GridBagLayout());

        tipPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tip"));
        tipPanel.setMinimumSize(new java.awt.Dimension(180, 100));

        tipLabel.setBackground(new java.awt.Color(254, 162, 70));
        tipLabel.setContentType("Click \"Proceed\" button"); // NOI18N
        tipLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jScrollPane2.setViewportView(tipLabel);

        javax.swing.GroupLayout tipPanelLayout = new javax.swing.GroupLayout(tipPanel);
        tipPanel.setLayout(tipPanelLayout);
        tipPanelLayout.setHorizontalGroup(
            tipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );
        tipPanelLayout.setVerticalGroup(
            tipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tipPanel, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Log"));

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        jScrollPane1.setViewportView(logTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 493;
        gridBagConstraints.ipady = 172;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        gameCtrl.setBorder(javax.swing.BorderFactory.createTitledBorder("Game"));
        gameCtrl.setMinimumSize(new java.awt.Dimension(125, 80));
        gameCtrl.setPreferredSize(new java.awt.Dimension(125, 80));
        java.awt.GridBagLayout gameCtrlLayout = new java.awt.GridBagLayout();
        gameCtrlLayout.columnWidths = new int[] {0, 6, 0};
        gameCtrlLayout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        gameCtrl.setLayout(gameCtrlLayout);

        finishBtn.setText("Finish");
        finishBtn.setMaximumSize(new java.awt.Dimension(54, 30));
        finishBtn.setMinimumSize(new java.awt.Dimension(54, 30));
        finishBtn.setPreferredSize(new java.awt.Dimension(54, 30));
        finishBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gameCtrl.add(finishBtn, gridBagConstraints);

        pauseBtn.setText("Pause");
        pauseBtn.setEnabled(false);
        pauseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gameCtrl.add(pauseBtn, gridBagConstraints);

        proceedButton.setText("Proceed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gameCtrl.add(proceedButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 99;
        gridBagConstraints.ipady = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(gameCtrl, gridBagConstraints);

        answerCtrl.setBorder(javax.swing.BorderFactory.createTitledBorder("Answer"));
        answerCtrl.setToolTipText("");
        answerCtrl.setMinimumSize(new java.awt.Dimension(130, 116));
        answerCtrl.setPreferredSize(new java.awt.Dimension(130, 100));
        answerCtrl.setLayout(new java.awt.GridLayout(3, 1));
        answerCtrl.add(answeringPlayerLabel);

        acceptButton.setBackground(new java.awt.Color(177, 243, 207));
        acceptButton.setText("Accept answer");
        answerCtrl.add(acceptButton);

        denyButton.setBackground(new java.awt.Color(255, 135, 107));
        denyButton.setText("Deny answer");
        answerCtrl.add(denyButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 207;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(answerCtrl, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void finishBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishBtnActionPerformed
        if (Game.getState() != GameStateEnum.FINISH){
            Game.getInstance().finishGame();         
        }
    }//GEN-LAST:event_finishBtnActionPerformed

    private void pauseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseBtnActionPerformed
        //Networking.Networking.getInstance().broadcastGameInfo();
    }//GEN-LAST:event_pauseBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JPanel answerCtrl;
    private javax.swing.JLabel answeringPlayerLabel;
    private javax.swing.JButton denyButton;
    private javax.swing.JButton finishBtn;
    private javax.swing.JPanel gameCtrl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JButton pauseBtn;
    private javax.swing.JButton proceedButton;
    private javax.swing.JTextPane tipLabel;
    private javax.swing.JPanel tipPanel;
    // End of variables declaration//GEN-END:variables
}
