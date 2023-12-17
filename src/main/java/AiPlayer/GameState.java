package AiPlayer;

public class GameState {
    Double angleAtEnemy;
    Double canSeeEnemy;
    //sees enemy>>1, obstacle in way>>-1
    Double distanceToEnemy;
    // relative to length of box

    Double distanceFront;
    Double distanceBack;
    Double distanceLeft;
    Double distanceRight;
    //relative to length of box

    Double seesFront;
    Double seesBack;
    Double seesLeft;
    Double seesRight;
    //-1 or 1


    Double angleSeesObstacle;
    Double EnemySeesObstacle;

    Integer bulletsFront=0;
    Integer bulletsBack=0;
    Integer bulletsLeft=0;
    Integer bulletsRight=0;

    public GameState() {
    }

    public GameState(Double angleAtEnemy, Double canSeeEnemy, Double distanceToEnemy, Double distanceFront, Double distanceBack, Double distanceLeft, Double distanceRight, Double seesFront, Double seesBack, Double seesLeft, Double seesRight, Double angleSeesObstacle, Double enemySeesObstacle, Integer bulletsFront, Integer bulletsBack, Integer bulletsLeft, Integer bulletsRight) {
        this.angleAtEnemy = angleAtEnemy;
        this.canSeeEnemy = canSeeEnemy;
        this.distanceToEnemy = distanceToEnemy;
        this.distanceFront = distanceFront;
        this.distanceBack = distanceBack;
        this.distanceLeft = distanceLeft;
        this.distanceRight = distanceRight;
        this.seesFront = seesFront;
        this.seesBack = seesBack;
        this.seesLeft = seesLeft;
        this.seesRight = seesRight;
        this.angleSeesObstacle = angleSeesObstacle;
        this.EnemySeesObstacle = enemySeesObstacle;
        this.bulletsFront = bulletsFront;
        this.bulletsBack = bulletsBack;
        this.bulletsLeft = bulletsLeft;
        this.bulletsRight = bulletsRight;
    }

    public Double getAngleAtEnemy() {
        return angleAtEnemy;
    }

    public void setAngleAtEnemy(Double angleAtEnemy) {
        this.angleAtEnemy = angleAtEnemy;
    }

    public Double getCanSeeEnemy() {
        return canSeeEnemy;
    }

    public void setCanSeeEnemy(Double canSeeEnemy) {
        this.canSeeEnemy = canSeeEnemy;
    }

    public Double getDistanceToEnemy() {
        return distanceToEnemy;
    }

    public void setDistanceToEnemy(Double distanceToEnemy) {
        this.distanceToEnemy = distanceToEnemy;
    }

    public Double getDistanceFront() {
        return distanceFront;
    }

    public void setDistanceFront(Double distanceFront) {
        this.distanceFront = distanceFront;
    }

    public Double getDistanceBack() {
        return distanceBack;
    }

    public void setDistanceBack(Double distanceBack) {
        this.distanceBack = distanceBack;
    }

    public Double getDistanceLeft() {
        return distanceLeft;
    }

    public void setDistanceLeft(Double distanceLeft) {
        this.distanceLeft = distanceLeft;
    }

    public Double getDistanceRight() {
        return distanceRight;
    }

    public void setDistanceRight(Double distanceRight) {
        this.distanceRight = distanceRight;
    }

    public Double getSeesFront() {
        return seesFront;
    }

    public void setSeesFront(Double seesFront) {
        this.seesFront = seesFront;
    }

    public Double getSeesBack() {
        return seesBack;
    }

    public void setSeesBack(Double seesBack) {
        this.seesBack = seesBack;
    }

    public Double getSeesLeft() {
        return seesLeft;
    }

    public void setSeesLeft(Double seesLeft) {
        this.seesLeft = seesLeft;
    }

    public Double getSeesRight() {
        return seesRight;
    }

    public void setSeesRight(Double seesRight) {
        this.seesRight = seesRight;
    }

    public Double getAngleSeesObstacle() {
        return angleSeesObstacle;
    }

    public void setAngleSeesObstacle(Double angleSeesObstacle) {
        this.angleSeesObstacle = angleSeesObstacle;
    }

    public Double getEnemySeesObstacle() {
        return EnemySeesObstacle;
    }

    public void setEnemySeesObstacle(Double enemySeesObstacle) {
        EnemySeesObstacle = enemySeesObstacle;
    }

    public Integer getBulletsFront() {
        return bulletsFront;
    }

    public void setBulletsFront(Integer bulletsFront) {
        this.bulletsFront = bulletsFront;
    }

    public Integer getBulletsBack() {
        return bulletsBack;
    }

    public void setBulletsBack(Integer bulletsBack) {
        this.bulletsBack = bulletsBack;
    }

    public Integer getBulletsLeft() {
        return bulletsLeft;
    }

    public void setBulletsLeft(Integer bulletsLeft) {
        this.bulletsLeft = bulletsLeft;
    }

    public Integer getBulletsRight() {
        return bulletsRight;
    }

    public void setBulletsRight(Integer bulletsRight) {
        this.bulletsRight = bulletsRight;
    }
}
