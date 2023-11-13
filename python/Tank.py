import math as Math
from Level import Level
from Bullet import Bullet
from Wall import Wall
class Tank():
    DEFAULT_LINEAR_VELOCITY = 4
    DEFAULT_ANGULAR_VELOCITY = 0.1
    TANK_SIZE = 85
    BULLET_TIMEOUT = 12
    
    
    def __init__(self, posX, posY, angle, tankId):
        self.tankId = tankId
        self.posX = posX
        self.posY = posY
        self.angle = angle
        self.currentBulletTimeout = 0
        self.spawnX = posX
        self.spawnY = posY
        self.velocity = 0
        self.angularVelocity = 0
        
    def forward(self):
        self.velocity = Tank.DEFAULT_LINEAR_VELOCITY
    def backward(self):
        self.velocity = -Tank.DEFAULT_LINEAR_VELOCITY
    def stop(self):
        self.velocity = 0
    def left(self):
        self.angularVelocity = Tank.DEFAULT_ANGULAR_VELOCITY
    def right(self):
        self.angularVelocity = -Tank.DEFAULT_ANGULAR_VELOCITY
    def angleStop(self):            
        self.angularVelocity = 0
        
    def update(self,timeDelta, level : "Level"):
        deltaX = self.velocity * Math.cos(self.angle) * timeDelta;
        deltaY = self.velocity * Math.sin(self.angle) * timeDelta;
        oldPosX =self.posX;
        oldPosY =self.posY;
        self.posX += deltaX;

        for w in level.walls:
            t = self.getCollisionAxis(self, w)
            if t == "X" or t == "TOTAL":
                self.posX = oldPosX
    
        self.posY += deltaY;
        
        for w in level.walls:
            t = self.getCollisionAxis(self, w)
            if t == "Y" or t == "TOTAL":
                self.posY = oldPosY
        self.angle += self.angularVelocity * timeDelta
        self.currentBulletTimeout -= 1
        
    def getCollisionAxis(self,tank: "Tank", wall : "Wall"):
        lX = tank.posX - Tank.TANK_SIZE/2
        rX = tank.posX + Tank.TANK_SIZE/2
        uY = tank.posY - Tank.TANK_SIZE/2
        bY = tank.posY + Tank.TANK_SIZE/2
        
        if wall.type == "HORIZONTAL":
            Y = wall.y1
            X1 = wall.x1
            X2 = wall.x2
            if ((rX >= X1 and lX <= X1) or (lX <= X2 and rX >= X2)) and uY <= Y and bY >= Y:
                if uY <= Y and bY >= Y and (rX >= X1 and lX <= X2):
                    return "TOTAL"
                return "X"
    
            if uY <= Y and bY >= Y and (rX >= X1 and lX <= X2):
                return "Y"
        else:
            X = wall.x1
            Y1 = wall.y1
            Y2 = wall.y2
            
            
            if ((bY >= Y1 and uY <= Y1) or (bY >= Y2 and uY <= Y2)) and lX <= X and rX >= X:
                if lX <= X and rX >= X and uY <= Y2 and bY >= Y1:
                    return "TOTAL"
                return "Y"

            if lX <= X and rX >= X and uY <= Y2 and bY >= Y1:
                return "X"

        return "NONE"
    
    def fireBullet(self):
        b = Bullet(self.posX, self.posY, self.angle, self)
        if self.currentBulletTimeout > 0:
            b.currentLife = Bullet.MAX_LIFE
            return b
        self.currentBulletTimeout = Tank.BULLET_TIMEOUT
        return b

    def collideWithBullets(self, bullets):
        for b in bullets:
            if b.owner == self and b.currentLife < Bullet.GRACE_PERIOD:
                continue
            if (self.posX - b.posX)**2 + (self.posY-b.posY)**2 <= (Tank.TANK_SIZE/2)**2:
                return b
        return None        
    
    def respawn(self):
        self.posX = self.spawnX
        self.posY = self.spawnY