package Objects;


import LevelManagment.Level;

import java.awt.*;

public class Gate extends GameObject{
    String startLevel, endLevel;
    int id;

    private static Color defaultColor = Color.ORANGE;

    public static final int GATE_WIDTH = 64;
    public static final int GATE_HEIGHT = 64;
    private static int gateIdNum = 0;

    public Gate(int x, int y, int width, int height){
        super(x, y, width, height);
        this.color = Color.ORANGE;
        this.type = "Gate";
        this.endLevel = "none";
        this.startLevel = "none";
        this.id = gateIdNum++;

        this.stringAttributes.put("startLevel", "none");
        this.stringAttributes.put("endLevel", "none");
    }
    public Gate(int x, int y, int width, int height, String startLevel, String endLevel){
        super(x, y, width, height);
        this.startLevel = startLevel;
        this.endLevel = endLevel;
        this.color = Color.ORANGE;
        this.id = gateIdNum++;

        this.stringAttributes.put("startLevel", startLevel);
        this.stringAttributes.put("endLevel", endLevel);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    public static Color getDefaultColor(){
        return defaultColor;
    }

    public String getEndLeveL() {
        return endLevel;
    }

    @Override
    public String toLevelSave() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.x).append(" ").append(this.y).append(" ").append(this.width).append(" ").append(this.height).
                append(" ").append(startLevel).append(" ").append(endLevel).append(" ");

        return sb.toString();
    }

    @Override
    public void changeStringAttribute(String name, String change) {
        super.changeStringAttribute(name, change);
        this.endLevel = this.stringAttributes.get("endLevel");
        this.startLevel = this.stringAttributes.get("startLevel");
    }

    public void setStartlevel(String startlevel) {
        this.startLevel = startlevel;
        this.stringAttributes.replace("startLevel", startlevel);
    }

    public void setEndlevel(String endlevel) {
        this.endLevel = endlevel;
        this.stringAttributes.replace("endLevel", endlevel);
    }

    public int getId() {
        return id;
    }
}
