from Wall import Wall
from Level import Level
from Tank import Tank
from Bullet import Bullet
from Agent import Agent

#Returns tank id of tank who lost, -1 if it was a tie
#every few ticks agent makes decision
def playAGame(level: 'Level', agent1 = '', agent2= '', maxGameTick = 1000):
    dimX = level.dimX
    dimY = level.dimY
    tanks = [Tank(dimX/3, dimY/3, angle = 0, tankId = 0)
             , Tank(dimX*2/3, dimY*2/3, angle = 0, tankId = 0)]
    bullets = []
    deadBullets = []
    newBullets = []
    for gamelength in range(maxGameTick):
        for b in bullets:
            if b.shouldIDie():
                deadBullets.add(b)
                continue
            b.update(timeDelta = 1, level = level)
        
        for b in deadBullets:
            bullets.remove(b)
        
        deadBullets.clear()
        
        for tank in tanks:
            if tank.collideWithBullets(bullets):
                return tank.tankId
            tank.update(1, level)
        
        if (len(newBullets) != 0):
            bullets += newBullets
            newBullets.clear()

dimX = 800
dimY = 800
walls = [Wall(5, 5, dimX - 5, 5),
            Wall(5, 5, 5, dimY - 5),
            Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            Wall(5, dimY - 5, dimX - 5, dimY - 5),
            Wall(dimX / 2, 150, dimX / 2, 650),
            Wall(150, dimY / 2, 650, dimY / 2)]         
testLevel = Level(walls, dimX, dimY)

playAGame(testLevel, "", "",100)

    