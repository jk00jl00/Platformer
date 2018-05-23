package Gfx;

import Listeners.ButtonListener;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class LEIntAtr extends JTextField{
    int defaultValue;
    String atrName;
    int maxNumberOfCharacters = 5;
    ButtonListener bl;

    public int getCurrentValue(){
        return Integer.parseInt(this.getText());
    }

    LEIntAtr(int value, String atrName, ButtonListener bl){
        super();
        this.defaultValue = value;
        this.atrName = atrName;
        System.out.println(defaultValue);
        this.bl = bl;
        this.setAction();
        this.overRideInsert();
        resetValue();
    }

    private void overRideInsert() {
        this.setDocument(new DefaultStyledDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if ((this.getLength() + str.length()) <= maxNumberOfCharacters && str.matches("[0-9]*")) {
                    super.insertString(offs, str, a);
                }
            }});
    }

    private void setAction() {
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        String enterBinding = enter.toString();
        String escapeBinding = escape.toString();

        InputMap inputMap = this.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(enter, enterBinding);
        inputMap.put(escape, escapeBinding);
        actionMap.put(escapeBinding, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetValue();
            }
        });
        actionMap.put(enterBinding, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEnter();
            }
        });
    }

    private void onEnter() {
        if(this.getText().isEmpty()) this.setText(String.valueOf(1));
        bl.attributeChange(this);
        this.defaultValue = this.getCurrentValue();
    }

    private void resetValue(){
        this.setText(String.valueOf(defaultValue));
    }

    public String getAtrName() {
        return atrName;
    }
}
