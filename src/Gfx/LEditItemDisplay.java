package Gfx;

import javax.swing.*;
import java.awt.*;

public class LEditItemDisplay extends JLabel implements ListCellRenderer<String>{
    private String name;
    private Image icon;


    public LEditItemDisplay(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }


        setText(value);

        return this;
    }
}
