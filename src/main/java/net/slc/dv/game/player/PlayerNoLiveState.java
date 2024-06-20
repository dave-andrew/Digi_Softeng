package net.slc.dv.game.player;

import javafx.scene.layout.Pane;
import net.slc.dv.game.Player;

public class PlayerNoLiveState extends PlayerBaseState {
    public PlayerNoLiveState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {}

    @Override
    public void onUpdate(double deltaTime, Pane root) {}

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {}
}
