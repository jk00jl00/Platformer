package States.CreatureStates;

import Actors.Creature;
import Objects.GameObject;
import Utilities.Util;

import java.awt.*;

public class BasicEnemyBehavior extends CreatureBehavior{
    private boolean movingRight = true;

    public BasicEnemyBehavior(Creature creature) {
        super(creature);
    }

    @Override
    public void update() {
        if(creature.getHealth() <= 0) return;
        if(isFalling()) {
            this.dx = 0;
            this.dy += Creature.grav;
        } else{
            this.checkMovement();
        }
        this.move((int)dx,(int)dy);
        creature.collide();
        super.update();
    }

    private void checkMovement() {
        dx = 0;
        if(movingRight){
            doLoop:
            do{
                dx += 0.2;
                Rectangle nextPosition = new Rectangle(creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());

                nextPosition.x += Math.round(this.dx);
                for(GameObject o: creature.getGos())
                    if (o.isSolid()) {
                        if((!Util.collide(o.getHitBox(), new Rectangle(nextPosition.x + nextPosition.width + 1, nextPosition.y + nextPosition.height + 1, 1, 1))&&
                                o.getHitBox().intersects(new Rectangle(creature.getX(), creature.getY() + 10, creature.getWidth(), creature.getHeight()))) ||
                                Util.collide(o.getHitBox(), new Rectangle(nextPosition.x + nextPosition.width + 1, nextPosition.y, 1, creature.getHeight()))) {
                            this.dx -= 0.2;
                            movingRight = false;
                            break doLoop;
                        }
                    }

            } while (dx < 1);
        } else{
            doLoop:
            do{
                dx -= 0.2;
                Rectangle nextPosition = new Rectangle(creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());

                nextPosition.x += Math.round(this.dx);
                for(GameObject o: creature.getGos())
                    if (o.isSolid()) {
                        if((!Util.collide(o.getHitBox(), new Rectangle(nextPosition.x - 1, nextPosition.y + nextPosition.height + 1, 1, 1))&&
                                o.getHitBox().intersects(new Rectangle(creature.getX(), creature.getY() + 1, creature.getWidth(), creature.getHeight()))) ||
                                Util.collide(o.getHitBox(), new Rectangle(nextPosition.x  - 2, nextPosition.y, 1, creature.getHeight()))) {
                            this.dx += 0.2;
                            movingRight = true;
                            break doLoop;
                        }
                    }

            } while (dx > -1);
        }
    }

    private boolean isFalling(){
        this.dy += Creature.grav;

        Rectangle currentPosition = creature.getHitBox();
        Rectangle nextPosition = new Rectangle(creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());

        nextPosition.y += Math.round(this.dy);
        for(GameObject o: creature.getGos())
            if (o.isSolid()) {
                if(Util.collide(o.getHitBox(), nextPosition)) {
                    this.dy = 0;
                    return false;
                }
            }

        return true;
    }

    @Override
    public void move(int x, int y) {
        super.move(x, y);
    }
}
