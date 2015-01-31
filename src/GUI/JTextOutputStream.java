/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

/**
 * <p> TODO description of JTextOutputStream
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class JTextOutputStream extends OutputStream{
    
    private JTextArea textArea;

    public JTextOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
    
    @Override
    public void write(int i) throws IOException {
        textArea.append(String.valueOf((char)i));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}
