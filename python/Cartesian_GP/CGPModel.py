from operators import operators
import numpy
import random
from util import random_geometric, write_to_file, fetch_from_file
import json
import copy

GENERATION_INDEX_PROBABILITY = 1/20

#Best not to put this bigger than 1/3
MUTATION_OF_ELEMENT_PROBABILITY = 1/16

REPRODUCTION_PROBABILITY = 1/4

#Returns a genome as a list of nodes of which, each is an uređena trojka (in1, in2, operator)
#In1 and In2 are indices in a given genome
#For In1 and In2 at index I, it is True that In1 < I && In2 < I

#Inputs are denoted as negative "indices", each negative number denotes an index + + in input list
def construct_random_genome_geometric(number_of_nodes, number_of_inputs, number_of_outputs = 3, constant_range = (0,0), ):
    genome_result = []
    outputs = []
    for i in range(number_of_inputs):
        genome_result.append([-(i+1), 0 , -1])    
    for i in range(number_of_nodes):
        in1 = number_of_inputs + i -  random_geometric(i + number_of_inputs, GENERATION_INDEX_PROBABILITY)
        in2 = number_of_inputs + i - random_geometric(i + number_of_inputs, GENERATION_INDEX_PROBABILITY)
        operator = random.choice(list(operators.keys()))
        genome_result.append([in1,in2,operator])
    
    outputs = list(random.choices(range(number_of_inputs,len(genome_result)), k = 3))
    outputs = list(range(len(genome_result) - number_of_outputs , len(genome_result)))
    return [genome_result,outputs]

def construct_random_genome_uniform(number_of_nodes, number_of_inputs, number_of_outputs = 3, constant_range = (0,0), ):
    genome_result = []
    outputs = []
    for i in range(number_of_inputs):
        genome_result.append([-(i+1), 0 , -1])    
        random.randint(0, i+number_of_inputs + 1) 
    for i in range(number_of_nodes):
        in1 = number_of_inputs + i - random.randint(1, i+number_of_inputs) 
        in2 = number_of_inputs + i - random.randint(1, i+number_of_inputs) 
        operator = random.choice(list(operators.keys()))
        genome_result.append([in1,in2,operator])
    
    outputs = list(random.choices(range(number_of_inputs,len(genome_result)), k = 3))
    outputs = list(range(len(genome_result) - number_of_outputs , len(genome_result)))
    return [genome_result,outputs]

def evaluate_genome(genome, inputs):
    genome, outputs = genome
    evaluation = [0]*len(genome)
    for i in range(len(genome)):
        in1, in2, operator_key = genome[i]
        
        if in1 < 0:
            evaluation[i] = inputs[(-(in1+1)) % len(inputs)]
            continue
        
        evaluation[i] = operators[operator_key](evaluation[in1],evaluation[in2])
    result = []
    for index in outputs:
        result.append(evaluation[index])
    return result

def mutate_genome(genome):
    genome , outputs = genome
    for i in range(len(genome)):
        if (genome[i][0] < 0):
            continue
        for part in range(3):
                rng = random.random()
                if rng < MUTATION_OF_ELEMENT_PROBABILITY:
                    if part < 2:
                        newInput = i-random_geometric(i, GENERATION_INDEX_PROBABILITY)
                        genome[i][part] = newInput
                    else:
                        newOperator = random.choice(list(operators.keys()))
                        genome[i][part] = newOperator
                        
    return [genome, outputs]
#Heuristika: bolje krizati krajnje elemente genoma

def reproduce_genomes(genome1, genome2):
    genome1, output1 = copy.deepcopy(genome1)
    genome2, output2 = copy.deepcopy(genome2)
    
    if len(genome1) < len(genome2):
        genome1, genome2 = genome2, genome1
        
    c2 = copy.deepcopy(genome2)
    c1 = copy.deepcopy(genome1)
    for i in range(len(genome2)):
        rng = random.random()
        if rng < REPRODUCTION_PROBABILITY / (len(genome2) - i):
            c2[i] = genome1[i]
            c1[i] = genome2[i]
            
    return ([c1, output1],[c2, output2])


def reproduce_genomes2(genome1, genome2):
    genome1, output1 = copy.deepcopy(genome1)
    genome2, output2 = copy.deepcopy(genome2)
    
    if len(genome1) < len(genome2):
        genome1, genome2 = genome2, genome1
    
    cutoff = random.randint(0, len(genome2)-1)
        
    c2 = copy.deepcopy(genome2)
    c1 = copy.deepcopy(genome1)
    
    c1 = c1[:cutoff] + genome2[cutoff:]
    c2 = c2[:cutoff] + genome1[cutoff:]
            
    return ([c1, output1],[c2, output2])


def genomeToString(genome):
    return json.dumps(genome)

