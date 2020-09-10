package Haptics;

import gameobjects.*;
import util.GameObjectCollection;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Haptics implements Runnable {
    double totalDistanceVertical;
    double totalDistanceHorizontal;

    private Bomber player;

    GameObject closestObjectUp;
    GameObject closestObjectDown;
    GameObject closestObjectLeft;
    GameObject closestObjectRight;

    double distanceUp;
    double distanceDown;
    double distanceLeft;
    double distanceRight;

    private final GameToHapticsAPI hapticsAPI;
    private volatile boolean paused;

    public Haptics(int mazeSize) {
        this.hapticsAPI = new GameToHapticsIntensity();
        totalDistanceHorizontal = 32 * (mazeSize - 2);
        totalDistanceVertical = 32 * (mazeSize - 2);
    }

    public void generateHaptics() {
        updateClosestObjectsInAllDirections();
        calculateAndUpdateDistances(); // Order up, down, left, right
        updateMotorInformation();
        outputDescriptiveFeedback(getObjectInPlayingDirection());
        outputMotorInformationToArduino();
    }

    public void outputMotorInformationToArduino() {
        hapticsAPI.outputMotorInformationToArduino();
    }

    private void updateMotorInformation() {
        trimDistances();
        hapticsAPI.updateUpIntensity(distanceUp, totalDistanceVertical);
        hapticsAPI.updateDownIntensity(distanceDown, totalDistanceVertical);
        hapticsAPI.updateLeftIntensity(distanceLeft, totalDistanceHorizontal);
        hapticsAPI.updateRightIntensity(distanceRight, totalDistanceHorizontal);
    }

    private void trimDistances() {
        if (distanceUp < 30) {
            distanceUp = 0;
        } else if (distanceDown < 30) {
            distanceDown = 0;
        } else if (distanceLeft < 30) {
            distanceLeft = 0;
        } else if (distanceRight < 30) {
            distanceRight = 0;
        }
    }

    private void outputDescriptiveFeedback(GameObject objectInPlayingDirection) {
        /*if (objectInPlayingDirection instanceof Powerup) {
            hapticsAPI.thumb();
        } else {
            hapticsAPI.turnOffDescriptiveMotors();
        }
        if (objectInPlayingDirection instanceof Wall) {
            if (((Wall) objectInPlayingDirection).isBreakable()) {
                hapticsAPI.thumb();
            } else {
                hapticsAPI.indexFinger();
            }
        } else if (objectInPlayingDirection instanceof Bomb) {
            hapticsAPI.middleFinger();
        } else if (objectInPlayingDirection instanceof Powerup) {
            hapticsAPI.ringFinger();
        } else if (objectInPlayingDirection instanceof Explosion) {
            hapticsAPI.littleFinger();
        }*/
    }

    private GameObject getObjectInPlayingDirection() {
        switch (player.getDirection()) {
            case 0:
                return closestObjectUp;
            case 1:
                return closestObjectDown;
            case 2:
                return closestObjectLeft;
            case 3:
                return closestObjectRight;
        }
        return null;
    }

    private void calculateAndUpdateDistances() {
        distanceUp = player.getColliderCenter().getY() - closestObjectUp.getColliderCenter().getY();
        distanceDown = closestObjectDown.getColliderCenter().getY() - player.getColliderCenter().getY();
        distanceLeft = player.getColliderCenter().getX() - closestObjectLeft.getColliderCenter().getX();
        distanceRight = closestObjectRight.getColliderCenter().getX() - player.getColliderCenter().getX();
    }

    private void updateClosestObjectsInAllDirections() {
        resetClosestObjects();
        for (List<? extends GameObject> allGameObjects : GameObjectCollection.gameObjects) {
            for (GameObject gameObject : allGameObjects) {
                if (gameObject instanceof Explosion) {
                    ArrayList<Explosion> explosions = getExplosions(gameObject);
                    for (Explosion explosion : explosions) {
                        findClosestObjectsInAllDirections(explosion);
                    }
                }
                findClosestObjectsInAllDirections(gameObject);
            }
        }
    }

    private void resetClosestObjects() {
        closestObjectUp = null;
        closestObjectDown = null;
        closestObjectLeft = null;
        closestObjectRight = null;
    }

    private void findClosestObjectsInAllDirections(GameObject gameObject) {
        findClosestObjectUp(gameObject);
        findClosestObjectDown(gameObject);
        findClosestObjectLeft(gameObject);
        findClosestObjectRight(gameObject);
    }

    private ArrayList<Explosion> getExplosions(GameObject explosion) {
        if (explosion instanceof Explosion.Horizontal) {
            return getHorizontalExplosion(explosion);
        } else {
            return getVerticalExplosion(explosion);
        }
    }

    private ArrayList<Explosion> getVerticalExplosion(GameObject verticalExplosion) {
        ArrayList<Explosion> explosions = new ArrayList<>();
        float y = (float) (verticalExplosion.getCollider().getY() + 16);
        float x = (float) verticalExplosion.getCollider().getX() + 16;

        double end = verticalExplosion.getCollider().getY() + verticalExplosion.getCollider().getHeight();

        while (y < end) {
            Explosion explosion = makeExplosiveElement(x, y);
            explosions.add(explosion);
            y += 32;
        }

        return explosions;
    }

    private ArrayList<Explosion> getHorizontalExplosion(GameObject horizontalExplosion) {
        ArrayList<Explosion> explosions = new ArrayList<>();
        float y = (float) (horizontalExplosion.getCollider().getY() + 16);
        float x = (float) (horizontalExplosion.getCollider().getX() + 16);

        double end = horizontalExplosion.getCollider().getX() + horizontalExplosion.getCollider().getWidth();

        while (x < end) {
            Explosion explosion = makeExplosiveElement(x, y);
            explosions.add(explosion);
            x += 32;
        }

        return explosions;
    }

    private Explosion makeExplosiveElement(float x, float y) {
        Explosion explosion = new ExplosionHaptics(new Point2D.Float());
        Rectangle2D.Float collider = new Rectangle2D.Float(x, y, 0, 0);
        explosion.setCollider(collider);
        return explosion;
    }

    private void findClosestObjectUp(GameObject tileObject) {
        if (greaterThanVertical(tileObject, player) &&
                objectIsInSameColumnAsPlayer(tileObject) && closestObjectUp == null)
            closestObjectUp = tileObject;

        if (closestObjectUp != null && greaterThanVertical(tileObject, player) &&
                greaterThanVertical(closestObjectUp, tileObject) &&
                objectIsInSameColumnAsPlayer(tileObject)) {
            closestObjectUp = tileObject;
        }
    }

    private void findClosestObjectDown(GameObject tileObject) {
        if (greaterThanVertical(player, tileObject) &&
                objectIsInSameColumnAsPlayer(tileObject) && closestObjectDown == null)
            closestObjectDown = tileObject;

        if (closestObjectDown != null && greaterThanVertical(player, tileObject) &&
                greaterThanVertical(tileObject, closestObjectDown) &&
                objectIsInSameColumnAsPlayer(tileObject)) {
            closestObjectDown = tileObject;
        }
    }

    private boolean greaterThanVertical(GameObject object1, GameObject object2) {
        return object1.getColliderCenter().getY() < object2.getColliderCenter().getY();
    }

    private boolean objectIsInSameColumnAsPlayer(GameObject object) {
        return Math.abs(player.getColliderCenter().getX() - object.getColliderCenter().getX()) <= 16;
    }

    private void findClosestObjectLeft(GameObject tileObject) {
        if (greaterThanHorizontal(tileObject, player) &&
                objectIsInSameRowAsPlayer(tileObject) && closestObjectLeft == null)
            closestObjectLeft = tileObject;

        if (closestObjectLeft != null && greaterThanHorizontal(tileObject, player) &&
                greaterThanHorizontal(closestObjectLeft, tileObject) &&
                objectIsInSameRowAsPlayer(tileObject)) {
            closestObjectLeft = tileObject;
        }
    }

    private void findClosestObjectRight(GameObject tileObject) {
        if (greaterThanHorizontal(player, tileObject) &&
                objectIsInSameRowAsPlayer(tileObject) && closestObjectRight == null)
            closestObjectRight = tileObject;

        if (closestObjectRight != null && greaterThanHorizontal(player, tileObject) &&
                greaterThanHorizontal(tileObject, closestObjectRight) &&
                objectIsInSameRowAsPlayer(tileObject)) {
            closestObjectRight = tileObject;
        }
    }

    private boolean greaterThanHorizontal(GameObject object1, GameObject object2) {
        return object1.getColliderCenter().getX() < object2.getColliderCenter().getX();
    }

    private boolean objectIsInSameRowAsPlayer(GameObject object) {
        return Math.abs(player.getColliderCenter().getY() - object.getColliderCenter().getY()) <= 16;
    }

    @Override
    public void run() {
        while (true) {

            if (!paused) {
                generateHaptics();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void updatePlayer(Bomber player) {
        this.player = player;
    }

    public void turnOffAllMotors() {
        hapticsAPI.turnOffAllMotors();
    }

    public void turnOnAllMotors() {
        hapticsAPI.turnOnAllMotors();
    }

    public void updateUpIntensity(double value) {
        hapticsAPI.updateUpIntensity(value, totalDistanceVertical);
    }


    public void updateDownIntensity(double value) {
        hapticsAPI.updateDownIntensity(value, totalDistanceVertical);
    }


    public void updateRightIntensity(double value) {
        hapticsAPI.updateRightIntensity(value, totalDistanceHorizontal);
    }


    public void updateLeftIntensity(double value) {
        hapticsAPI.updateLeftIntensity(value, totalDistanceHorizontal);
    }

    public void resetAllMotors() {
        turnOffAllMotors();
        updateUpIntensity(0);
        updateDownIntensity(0);
        updateLeftIntensity(0);
        updateRightIntensity(0);
    }

    public void removeOffTime() {
        hapticsAPI.removeOffTime();
    }

    public void addDefaultOffTime() {
        hapticsAPI.addDefaultOffTime();
    }
}
