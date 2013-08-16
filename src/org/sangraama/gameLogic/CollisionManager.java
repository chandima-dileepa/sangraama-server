package org.sangraama.gameLogic;

import org.jbox2d.dynamics.contacts.Contact;
import org.sangraama.assets.Bullet;
import org.sangraama.assets.Player;
import org.sangraama.assets.Ship;

public enum CollisionManager {

    INSTANCE;
    private Contact collisions;
    private GameEngine gameEngine;

    CollisionManager() {

    }

    public void processCollisions(Contact collisions) {
        Player ship;
        Bullet bullet;
        long bulletID = 0;
        this.collisions = collisions;
        if (this.collisions.getFixtureA().getUserData().getClass() == Ship.class
                && this.collisions.getFixtureB().getUserData().getClass() == Ship.class) {
            System.out.println("Ships are colliding..");
            processShipsCollision();

        } else if ((this.collisions.getFixtureA().getUserData().getClass() == Ship.class && this.collisions
                .getFixtureB().getUserData().getClass() == Bullet.class)
                || (this.collisions.getFixtureA().getUserData().getClass() == Bullet.class && this.collisions
                        .getFixtureB().getUserData().getClass() == Ship.class)) {
            System.out.println("Hittn Bullet..");
            if (this.collisions.getFixtureA().getUserData().getClass() == Bullet.class) {
                ship = (Player) this.collisions.getFixtureB().getUserData();
                bullet = (Bullet) this.collisions.getFixtureA().getUserData();
            } else {
                ship = (Player) this.collisions.getFixtureA().getUserData();
                bullet = (Bullet) this.collisions.getFixtureB().getUserData();
            }
            if (bulletID != bullet.getId()) {
                processBulletShipCollition(ship, bullet);
                bulletID = bullet.getId();
            }
        }
    }

    private void processShipsCollision() {
        // Code to process when ships are collided.
    }

    private void processBulletShipCollition(Player ship, Bullet bullet) {
        System.out.println("Victime ship : " + ship.getUserID());
        System.out.println("Shooter ship : " + bullet.getPlayerId());
        GameEngine.INSTANCE.removeBullet(bullet);

    }
}
