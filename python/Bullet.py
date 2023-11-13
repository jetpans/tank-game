import math as Math
from Level import Level
from Wall import Wall
class Bullet:
    MAX_LIFE = 300
    GRACE_PERIOD = 30
    VELOCITY = 10
    SIZE = 10
    def __init__(self, posX, posY, angle, owner):
        self.posX = posX
        self.posY = posY
        self.velY = (Bullet.VELOCITY * Math.sin(angle))
        self.velX = (Bullet.VELOCITY * Math.cos(angle))
        self.owner = owner
        self.currentLife = 0
    
    
    
    def update(self, timeDelta, level : "Level"):
        self.currentLife += timeDelta
        
        deltaX = self.velX * timeDelta;
        deltaY = self.velY * timeDelta;
        newX = self.posX + deltaX;
        newY = self.posY + deltaY;
        for w in level.walls:
            if w.type == "HORIZONTAL":
                if self.posX >= w.x1 and self.posX <= w.x2:
                    if self.posY < w.y1 and newY >= w.y1:
                        self.velY = -self.velY
                        break
                    elif self.posY > w.y1 and newY < w.y1:
                        self.velY = -self.velY
                        break
            else:
                if (self.posY >= w.y1 and self.posY <= w.y2):
                    if (self.posX < w.x1 and newX>= w.x1):
                        self.velX = - self.velX
                        break
                    elif self.posX >= w.x1 and newX <= w.x1:
                        self.velX = -self.velX
                        break
        deltaX = self.velX * timeDelta
        deltaY = self.velY * timeDelta
        self.posX += deltaX
        self.posY += deltaY
        
    
    def shouldIDie(self):
        return self.currentLife >= Bullet.MAX_LIFE