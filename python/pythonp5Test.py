from p5 import *
import keyboard

from Wall import Wall
from Level import Level
from Tank import Tank
from Bullet import Bullet
from Agent import Agent

#Returns tank id of tank who lost, -1 if it was a tie

dimX = 800
dimY = 800


global testLevel
global tanks
global activeKeys
activeKeys = set()
global bullets

global deadBullets
global newBullets
def setup():
    global bullets
    global tanks
    global newBullets
    global deadBullets
    global testLevel
    
    size(dimX,dimY)
    walls = [Wall(5, 5, dimX - 5, 5),
            Wall(5, 5, 5, dimY - 5),
            Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            Wall(5, dimY - 5, dimX - 5, dimY - 5),
            Wall(dimX / 2, 150, dimX / 2, 650),
            Wall(150, dimY / 2, 650, dimY / 2)]         
    testLevel = Level(walls, dimX, dimY)
    tanks = [Tank(dimX/3, dimY/3, angle = 0, tankId = 0)
            , Tank(dimX*2/3, dimY*2/3, angle = 0, tankId = 0)]
    bullets = []
    deadBullets = []
    newBullets = []
    

def draw():
    global bullets
    global tanks
    global newBullets
    global deadBullets
    global testLevel
    background(255,255,255)
    for b in bullets:
        if b.shouldIDie():
            deadBullets.append(b)
            continue
        b.update(timeDelta = 1, level=testLevel)
        showBullet(b)
        
    for b in deadBullets:
        bullets.remove(b)
    
    deadBullets.clear()
        
    for tank in tanks:
        if tank.collideWithBullets(bullets):
            return tank.tankId
        tank.update(1, testLevel)
        showTank(tank)
        
    for w in testLevel.walls:
        showWall(w)
    if (len(newBullets) != 0):
        bullets += newBullets
        newBullets.clear()


def showTank(t:'Tank'):
    rectMode(CENTER)
    fill(126,112,55)

    translate(t.posX, t.posY);
    rotate(t.angle)

    strokeWeight(0)
    rect(0, 0, t.TANK_SIZE, t.TANK_SIZE)

    fill(4, 233, 200)
    strokeWeight(10)
    line(0, 0, 100, 0)

    rotate(-t.angle)
    translate(-t.posX, -t.posY)
    
def showBullet(b: 'Bullet'):
    strokeWeight(0)
    fill(0)
    ellipse(b.posX, b.posY, 20, 20)

def showWall(w : 'Wall'):
    strokeWeight(5)
    line(w.x1, w.y1, w.x2, w.y2)


def key_pressed():

    activeKeys.add(str(key))
    if key == 'w':
        tanks[0].forward()
    elif key == 'a':
        tanks[0].left()
    elif key == 's':
        tanks[0].backward()
    elif key == 'd':
        tanks[0].right()
    elif key == 'q':
        bullets.append(tanks[0].fireBullet())


def key_released():
    if not keyboard.is_pressed('w'):
        activeKeys.discard('w')
    if not keyboard.is_pressed("s"):
        activeKeys.discard("s")
    if not keyboard.is_pressed("a"):
        activeKeys.discard("a")
    if not keyboard.is_pressed("d"):
        activeKeys.discard("d")
                
    if 'w' not in activeKeys and 's' not in activeKeys:
       tanks[0].stop()
    if 'a' not in activeKeys and 'd' not in activeKeys:
        tanks[0].angleStop()

run()