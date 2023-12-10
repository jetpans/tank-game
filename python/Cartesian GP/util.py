import numpy
def random_geometric(N, p):
    return min(numpy.random.geometric(p), N)