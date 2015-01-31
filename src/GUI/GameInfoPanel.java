/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Model.Game;
import Model.Player;
import edu.cvut.vorobvla.bap.GameStateEnum;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Vladimir Vorobyev (vorobvla)
 */
public class GameInfoPanel extends javax.swing.JPanel {

    /**
     * Creates new form StatisticsPanel
     */
    public GameInfoPanel() {
        initComponents();
        /*
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    gameStateLabel.setText(Game.getState().name());
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameInfoPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();*/
    }
    
    public void setupTable(){
        scoreTable.setEnabled(false);
        scoreTable.setModel(new AbstractTableModel() {
            String [] colTitles = {"Player", "Score"};

            Object [] players = Game.getInstance().getPlayers().toArray();
                               
            @Override
            public int getRowCount() {
                return players.length;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int column) {
                return colTitles[column];
            }

            @Override
            public Object getValueAt(int i, int i1) {  
                Object [] players = Game.getInstance().getPlayers().toArray();
                if (i1 == 0) {
                    return ((Player)players[i]).getIdentity();
                } else {
                    return ((Player)players[i]).getScore();
                }      
            }               
        });
    }
    
    public void update(){
        gameStateLabel.setText(Game.getInstance().printState());
        scoreTable.repaint();
    };
  /*  
    public void setStateInfoLabelText(String text){
        gameStateLabel.setText(text);
    }
    */
    
/*
    public JTable getScoreTable() {
        return scoreTable;
    }
    */
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameStateLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoreTable = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Game Info"));
        setMinimumSize(new java.awt.Dimension(200, 200));
        setPreferredSize(new java.awt.Dimension(200, 200));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        gameStateLabel.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        gameStateLabel.setText("jLabel1");
        add(gameStateLabel);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Scores"));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(50, 50));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(50, 50));

        scoreTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Player", "Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scoreTable.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(scoreTable);
        scoreTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (scoreTable.getColumnModel().getColumnCount() > 0) {
            scoreTable.getColumnModel().getColumn(1).setResizable(false);
            scoreTable.getColumnModel().getColumn(1).setPreferredWidth(20);
        }

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel gameStateLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable scoreTable;
    // End of variables declaration//GEN-END:variables
}