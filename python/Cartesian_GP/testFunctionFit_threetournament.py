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
    
    
ITERATIONS = int(5*1e4)

POP_SIZE = int(1e4)
NUMBER_OF_EXTRA_NODES = 10
NUMBER_OF_INPUT_NODES = 1

data = util.fetch_from_file("ulaz.json")

def plot_genome(genome):
    x = PLOT_X
    y = list(map(lambda t: CGP.evaluate_genome(genome, [t])[0], x))
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
    population = [CGP.construct_random_genome(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES, number_of_outputs=1) for _ in range(3)]



    for i in range(ITERATIONS):
        if (i%1000)  == 0:
            print(i)
        fitness = []
        for index in range(3):
            fitness.append(fitness_against_data(population[index]))
            
        sorting = list(sorted(zip(population,fitness), key = lambda x: x[1]))
        
        population = list(map(lambda x: x[0] , sorting))
        
        newChild = CGP.reproduce_genomes2(population[0], population[1])[0]
        newChild = CGP.mutate_genome(newChild)
        population = [population[0], population[1], newChild]
        
    plot_data(data)

    plot_genome(population[0])

    plt.show()
    return

train()