import copy
import random
from neural_network import NeuralNetwork
POPULATION_SIZE = 10

def calculate_fitness(individual):
   return individual.layers[0].data[1][1]

def generate_individual():
   return NeuralNetwork(3, [2, 4, 5, 6], 2)

def crossover(parent1, parent2):
    crossover_point = random.randint(0, len(parent1.layers))

    child_layers = []

    for i in range(crossover_point):
        child_layers.append(copy.deepcopy(parent1.layers[i]))

    for i in range(crossover_point, len(parent2.layers)):
        child_layers.append(copy.deepcopy(parent2.layers[i]))

    child = NeuralNetwork.from_layer_list(child_layers)
    return child

def evolve_population():
   population = [generate_individual() for _ in range(POPULATION_SIZE)]

   #fitness_scores = [calculate_fitness(individual) for individual in population]

   for generation in range(1000):
      population.sort(key=calculate_fitness, reverse=True)

      print(f"Generacija {generation}: Najbolja jedinka - Prvi broj u prvom sloju: {calculate_fitness(population[0])}")

      survival_threshold = int(POPULATION_SIZE * 0.2)
      next_generation = population[:survival_threshold]

      while len(next_generation) < POPULATION_SIZE:
         parent1, parent2 = random.choices(population[survival_threshold:], k = 2)
         child = crossover(parent1, parent2)
         child.mutate(p = 0.2, std = 0.3)
         next_generation.append(child)

      population = next_generation


