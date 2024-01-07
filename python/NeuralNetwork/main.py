from neural_network import *
from learning import *

if __name__ == '__main__':
    nn = NeuralNetwork(3, [2,4,5,6], 2)
    nn.print()
    evolve_population()

    print(nn.check_dimensions())
