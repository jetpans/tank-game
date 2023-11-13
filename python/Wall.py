class Wall:
    def __init__(self, x1, y1, x2,y2):
        if x1==x2:
            self.type = "VERTICAL"
        if y1==y2:
            self.type = "HORIZONTAL"
        if x1<x2:
            self.x1 = x1
            self.x2= x2    
        else:
            self.x2 = x1
            self.x1 = x2
        if y1<y2:
            self.y1 = y1
            self.y2 = y2
        else:
            self.y2 = y1
            self.y1 = y2