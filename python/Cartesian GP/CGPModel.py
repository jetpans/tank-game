from operators import operators
import numpy
import random
from util import random_geometric

GENERATION_INDEX_PROBABILITY = 1/2

#Best not to put this bigger than 1/3
MUTATION_OF_ELEMENT_PROBABILITY = 1/36

REPRODUCTION_PROBABILITY = 1/4

#Returns a genome as a list of nodes of which, each is an uređena trojka (in1, in2, operator)
#In1 and In2 are indices in a given genome
#For In1 and In2 at index I, it is True that In1 < I && In2 < I

#Inputs are denoted as negative "indices", each negative number denotes an index + + in input list
def construct_random_genome(number_of_nodes, number_of_inputs, constant_range):
    genome_result = []
    
    for i in range(number_of_inputs):
        genome_result.append((-(i+1), 0 , -1))    
        
    for i in range(number_of_nodes):
        in1 = number_of_inputs + i - random_geometric(i + number_of_inputs, GENERATION_INDEX_PROBABILITY)
        in2 = number_of_inputs + i - random_geometric(i + number_of_inputs, GENERATION_INDEX_PROBABILITY)
        operator = random.choice(list(operators.keys()))
        genome_result.append((in1,in2,operator))
        
    
    return genome_result

def evaluate_genome(genome, inputs):
    evaluation = [0]*len(genome)
    for i in range(len(genome)):
        in1, in2, operator_key = genome[i]
        
        if in1 < 0:
            in1 = inputs[(-(in1+1)) % len(inputs)]
        
        evaluation[i] = operators[operator_key](in1,in2)
        

    return evaluation

def mutate_genome(genome):
    for i in range(len(genome)):
        for part in range(3):
                rng = random.random()
                if rng < MUTATION_OF_ELEMENT_PROBABILITY:
                    if part < 2:
                        newInput = i-random_geometric(i, GENERATION_INDEX_PROBABILITY)
                        genome[i][part] = newInput
                    else:
                        newOperator = random.choice(list(operators.keys()))
                        genome[i][part] = newOperator

#Heuristika: bolje krizati krajnje elemente genoma
def reproduce_genomes(genome1, genome2):
    if len(genome1) < len(genome2):
        genome1, genome2 = genome2, genome1
        
    c2 = genome2[::]
    c1 = genome1[::]
    for i in range(len(genome2)):
        rng = random.randoom()
        if rng < REPRODUCTION_PROBABILITY / (len(genome2) - i):
            c2[i] = genome1[i]
            c1[i] = genome2[i]
            
    return (c1,c2)

myGenome = construct_random_genome(6, 2, 15)

evaluation = evaluate_genome(myGenome, [1,3])

print(myGenome)
print(evaluation)
