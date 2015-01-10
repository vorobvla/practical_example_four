/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Networking.Networking;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vladimir Vorobyev (vorobvla)
 */
public class NatworkPanel extends javax.swing.JPanel {

    /**
     * Creates new form NatworkPanel
     */
    public NatworkPanel() {
        initComponents();
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

        IfceErrLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        interfaceNameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        moderatorPortField = new javax.swing.JTextField();
        PortErrLabel = new javax.swing.JLabel();
        OkButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Networking Settings"));
        setLayout(new java.awt.GridBagLayout());

        PortErrLabel.setVisible(false);
        IfceErrLabel.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 327;
        add(IfceErrLabel, gridBagConstraints);

        jLabel2.setText("Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 150;
        add(jLabel2, gridBagConstraints);

        interfaceNameField.setText(Networking.getInstance().getNetIntfceName());
        interfaceNameField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                interfaceNameFieldCaretUpdate(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 115;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(interfaceNameField, gridBagConstraints);

        jLabel1.setText("Network Intarface name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 10;
        add(jLabel1, gridBagConstraints);

        moderatorPortField.setText(new Integer(Networking.getInstance().getModeratorPort()).toString());
        moderatorPortField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moderatorPortFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 115;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(moderatorPortField, gridBagConstraints);

        PortErrLabel.setText("jLabel2");
        PortErrLabel.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 159;
        add(PortErrLabel, gridBagConstraints);

        OkButton.setText(Constants.BTN_OK);
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(OkButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void interfaceNameFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_interfaceNameFieldCaretUpdate

    }//GEN-LAST:event_interfaceNameFieldCaretUpdate

    private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
        Networking.getInstance().setUpByInfceName(interfaceNameField.getText());
        try {
            Networking.getInstance().setModeratorPort(Integer.parseInt(moderatorPortField.getText()));
            /*      JOptionPane.showMessageDialog(this,
                "Eggs are not supposed to be green.",
                "Inane error",
                JOptionPane.ERROR_MESSAGE);*/
            //        PortErrLabel.setText(Constants.IFCE_NAME_ERR(InterfaceNameField.getText()));
            //        PortErrLabel.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(NetworkFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        Networking.getInstance().callPlayers();
    }//GEN-LAST:event_OkButtonActionPerformed

    private void moderatorPortFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moderatorPortFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moderatorPortFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IfceErrLabel;
    private javax.swing.JButton OkButton;
    private javax.swing.JLabel PortErrLabel;
    private javax.swing.JTextField interfaceNameField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField moderatorPortField;
    // End of variables declaration//GEN-END:variables
}
