from Wall import Wall
class Level:
    def __init__(self, walls:"Wall", dimX, dimY):
        self.dimX = dimX
        self.dimY = dimY
        self.walls = walls
