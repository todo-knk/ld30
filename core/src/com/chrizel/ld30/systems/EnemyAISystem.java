package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.chrizel.ld30.components.*;

import java.util.Random;

@Wire
public class EnemyAISystem extends EntityProcessingSystem {
    ComponentMapper<EnemyAI> amm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<MovementComponent> mm;
    ComponentMapper<AttackComponent> am;
    Random random = new Random();

    public EnemyAISystem() {
        super(Aspect.getAspectForAll(EnemyAI.class, PositionComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(Entity e) {
        EnemyAI amc = amm.get(e);
        PositionComponent position = pm.get(e);
        MovementComponent movement = mm.get(e);

        amc.resetState += Gdx.graphics.getDeltaTime();

        if (amc.resetState >= amc.resetTime || ((Math.abs(position.x - amc.nextX) < 3) && (Math.abs(position.y - amc.nextY) < 3))) {
            amc.resetState = 0;
            amc.nextX = position.x + ((random.nextFloat() * amc.width) - (amc.width / 2));
            amc.nextY = position.y + ((random.nextFloat() * amc.height) - (amc.height / 2));

            float diffX = position.x - amc.nextX;
            float diffY = position.y - amc.nextY;
            movement.velocityX = diffX == 0 ? 0 : (diffX > 0 ? 16 : -16);
            movement.velocityY = diffY == 0 ? 0 : (diffY > 0 ? 16 : -16);

            AttackComponent attack = am.getSafe(e);
            if (attack != null) {
                attack.isAttacking = true;
            }

            amc.inProgress = true;
        }
    }
}
