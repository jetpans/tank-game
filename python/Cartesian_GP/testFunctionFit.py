import CGPModel as CGP
import util
import matplotlib.pyplot as plt
import copy
import random

global PLOT_X1
global PLOT_X2
global PLOT_X
PLOT_X1 = -10
PLOT_X2 = 10
PLOT_X  = []
for i in range(PLOT_X1*10, PLOT_X2*10):
    PLOT_X.append(i/10)
    
    
ITERATIONS = int(15)

POP_SIZE = int(1e4)
NUMBER_OF_EXTRA_NODES = 10
NUMBER_OF_INPUT_NODES = 1

data = util.fetch_from_file("ulaz.json")

def plot_genome(genome):
    x = PLOT_X
    y = list(map(lambda t: CGP.evaluate_genome(genome, [t]), x))
    print(len(set(x)) - len(x))
    plt.plot(x,y,linestyle = '-')
def plot_data(data):
    x = list(map(lambda x: x[0],data))
    y = list(map(lambda x: x[1],data))
    plt.plot(x,y,marker='o',linestyle = '-')
    
    
def fitness_against_data(jedinka):
    suma = 0
    for t in data:
        x,y = t
        try:
            suma += (CGP.evaluate_genome(jedinka, [x])[0] - y) ** 2
        except:
            suma += 1e5
    
    return suma
def train():
    population = [CGP.construct_random_genome(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES) for _ in range(POP_SIZE)]



    for i in range(ITERATIONS):
        print(i)
        # print(i)
        fitness = []
        for index in range(POP_SIZE):
            fitness.append(fitness_against_data(population[index]))
            
        sorting = list(sorted(zip(population,fitness), key = lambda x: x[1]))
        
        population = list(map(lambda x: x[0] , sorting))
        new_population = copy.deepcopy(population[:int(POP_SIZE / 4)])
        for k in range(int((POP_SIZE-len(new_population) ) / 2)):
            first = random.choice(population[:int(POP_SIZE/4)])
            second = random.choice(population[:int(POP_SIZE/4)])
            newChildren = CGP.reproduce_genomes2(first, second)
            new_population.append(CGP.mutate_genome(newChildren[0]))
            new_population.append(CGP.mutate_genome(newChildren[1]))
        for t in range(int(POP_SIZE - len(new_population))):
            new_population.append(CGP.construct_random_genome(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES))
    plot_data(data)

    plot_genome(population[0])

    plt.show()
    return

train()