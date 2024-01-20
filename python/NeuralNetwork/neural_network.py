from layer import Layer
import json
import random
import copy


class NeuralNetwork:
    def toJSON(self):
        return (json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)).replace("\n", "")
    def genomeToString(genome):
        return json.dumps(genome)
    def __init__(self, input_dim, layer_outputs, random_std):
        output_dim = layer_outputs[len(layer_outputs) - 1]
        self.input_dim = input_dim
        self.output_dim = output_dim
        self.layers = [Layer(0, 0, 0) for _ in range(len(layer_outputs))]
        self.layers[0] = Layer(layer_outputs[0], input_dim + 1, random_std)
        for i in range(1, len(layer_outputs)):
            self.layers[i] = Layer(layer_outputs[i], layer_outputs[i - 1] + 1, random_std)

    def check_dimensions(self):
        if self.layers[0].cols != self.input_dim + 1:
            return False
        if self.layers[len(self.layers) - 1].rows != self.output_dim:
            return False
        for i in range(1, len(self.layers)):
            prev_layer = self.layers[i - 1]
            current_layer = self.layers[i]

            if prev_layer.rows + 1 != current_layer.cols:
                return False

        return True

    def mutate(self, p, std):
        for i in range(len(self.layers)):
            self.layers[i].mutate(p, std)

    def print(self):
        for i in range(0, len(self.layers)):
            if i != 0:
                print("||||||||||")
                print("VVVVVVVVVV")
            self.layers[i].print()

    @classmethod
    def from_layer_list(cls, layer_list):
        network = cls(layer_list[0].cols-1, [element.rows for element in layer_list], 0)
        network.layers = layer_list
        return network
    @classmethod
    def crossover(cls,parent1, parent2):
        crossover_point = random.randint(0, len(parent1.layers))

        child_layers = []

        for i in range(crossover_point):
            child_layers.append(copy.deepcopy(parent1.layers[i]))

        for i in range(crossover_point, len(parent2.layers)):
            child_layers.append(copy.deepcopy(parent2.layers[i]))

        child = NeuralNetwork.from_layer_list(child_layers)
        return child
