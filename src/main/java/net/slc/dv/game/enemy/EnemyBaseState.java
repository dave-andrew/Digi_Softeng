package net.slc.dv.game.enemy;

import net.slc.dv.game.Enemy;
import net.slc.dv.view.offlinegame.OfflineGameView;

public abstract class EnemyBaseState {

    protected Enemy enemy;

    public EnemyBaseState(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, OfflineGameView game);
}
