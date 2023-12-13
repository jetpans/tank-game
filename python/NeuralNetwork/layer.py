import random


class Layer:
    # rows represent output dimension
    # columns represent input dimension
    def __init__(self, rows, cols, random_std):
        self.rows = rows
        self.cols = cols
        self.data = [[random.gauss(0, random_std) for _ in range(cols)] for _ in range(rows)]

    def mutate(self, p, std):
        for i in range(self.rows):
            for j in range(self.cols):
                if random.random() < p:
                    self.data[i][j] += random.gauss(0, std)

    def print(self):
        for i in range(len(self.data)):
            print(self.data[i])

    @classmethod
    def from_list(cls, matrix_list):
        rows = len(matrix_list)
        cols = len(matrix_list[0])
        layer = cls(rows, cols, 0)
        layer.data = matrix_list
        return layer
