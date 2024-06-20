package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class Coin1 extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/coin1.png");

    public Coin1(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }

    public int getValue() {
        return 1;
    }
}
