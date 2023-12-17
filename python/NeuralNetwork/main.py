from neural_network import *

if __name__ == '__main__':
    nn = NeuralNetwork(3, [2,4,5,6], 2)
    nn.print()

    print(nn.check_dimensions())
