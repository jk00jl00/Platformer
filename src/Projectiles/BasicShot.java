package Projectiles;

import Actors.Creature;
import GameController.Game;

import java.awt.*;

public class BasicShot extends Projectile{
    public BasicShot(Creature c, double dx, double dy, Game game) {
        super(c, dx * 15, dy * 15, game);
        this.dmg = 1;
        this.range = 500;
        this.width = 10;
        this.height = 10;
        this.hitBox = new Rectangle(this.x, this.y, this.width, this.height);
    }
}
