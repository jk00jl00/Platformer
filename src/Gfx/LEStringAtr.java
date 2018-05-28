package Gfx;

import Listeners.ButtonListener;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

//Class for the text boxes for the StringAttributes of a selected object.
public class LEStringAtr extends JTextField {
    private final String atrName;
    private final int maxNumberOfCharacters = 10;
    private final ButtonListener bl;
    private String defaultValue;

    LEStringAtr(String value, String atrName, ButtonListener bl){
        super();
        this.defaultValue = value;
        this.atrName = atrName;
        System.out.println(defaultValue);
        this.bl = bl;
        this.setAction();
        this.overRideInsert();
        resetValue();
    }

    public String getCurrentValue(){
        return this.getText();
    }

    /**
     * Makes sure only numbers can be typed and only 5.
     */
    private void overRideInsert() {
        this.setDocument(new DefaultStyledDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if ((this.getLength() + str.length()) <= maxNumberOfCharacters) {
                    super.insertString(offs, str, a);
                }
            }});
    }

    /**
     * Sets the actions when pressing enter or escape.
     */
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

    /**
     * Gives itself to the buttonListener to be handled and updates it's value.
     * Called when the user presses enter.
     */
    private void onEnter() {
        if(this.getText().isEmpty()) this.setText("none");
        bl.attributeChange(this);
        this.defaultValue = this.getCurrentValue();
    }

    /**
     * Resets the value of the text field.
     * Called when the user presses escape.
     */
    private void resetValue(){
        this.setText(defaultValue);
    }

    public String getAtrName() {
        return atrName;
    }

    public void setValue(String value) {
        this.setText(String.valueOf(value));
        this.defaultValue = value;
    }
}