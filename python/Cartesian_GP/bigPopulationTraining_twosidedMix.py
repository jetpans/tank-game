import socket
import json
import CGPModel as CGP
from util import * 
import copy
import random
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("127.0.0.1", 12345))
server_socket.listen(5)
print("Server is listening... on ",12345)
client_socket, client_address = server_socket.accept()
print("Connected to", client_address)


def evaluate_game(type_of_player_1, player_1, type_of_player_2, player_2, level): #Returns String of game ending -- might require parsing
    str1 = f"{type_of_player_1}\n{player_1}\n"
    str2 = f"{type_of_player_2}\n{player_2}\n"
    
    client_socket.send("START\n".encode('utf-8'))
    client_socket.send(str1.encode('utf-8'))
    client_socket.send(str2.encode('utf-8'))
    client_socket.send(f"LEVEL {level}\n".encode('utf-8'))
    
    result = client_socket.recv(1024).decode('utf-8')
    
    return result

def convert_genome_to_json(genome):
    result = copy.deepcopy(genome)
    
    result[0].append(result[1])
    
    return json.dumps(result[0])

# dict = {
#     "winner": str
#     "decisions": int
#     "bullets_1": int
#     "bullets_2": int
#     "way_of_victory": str
# }

def parse_result(result):
    result = list(filter(lambda x: x!= "" and x != "\n", result.replace("\r", "").split("\n")))
    output = {}
    evals = list(map(lambda x: x.split(":")[1].strip(),result ))
    output["winner"] = evals[0]
    output["decisions"] = int(evals[1])
    output["bullets_1"] = int(evals[2])
    output["bullets_2"] = int(evals[3])
    output["way_of_victory"] = evals[4]
    return output

def fitness_from_result(result, player):
    WIN_WEIGHT = 200
    LOSE_WEIGHT = -400
    DRAW_WEIGHT = 0
    MURDER_WEIGHT = 200
    SUICIDE_WEIGHT = -500
    suma = 0
    suma += 300 - result["decisions"] * 300/1000

    if result["winner"] == "DRAW":
        suma += DRAW_WEIGHT
        return suma
    elif result["winner"] == player:
        suma += WIN_WEIGHT
        suma += MURDER_WEIGHT if result["way_of_victory"] == "MURDER" else 0
        return suma
    else:
        suma += LOSE_WEIGHT
        suma += SUICIDE_WEIGHT if result["way_of_victory"] == "SUICIDE" else 0
        return suma
        

POPULATION_SIZE = 12
NUMBER_OF_EXTRA_NODES = 30
NUMBER_OF_INPUT_NODES = 11
ITERATIONS = 50000

global HARDCODE_MULTIPLIER
HARDCODE_MULTIPLIER = 2
NEW_PARENT_PERCENT = 0.5
def main():
    global HARDCODE_MULTIPLIER
    maxFitness = -1e7
    swapped = False
    population = [CGP.construct_random_genome_uniform(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES) for _ in range(POPULATION_SIZE)]
    last_save = 0
    # jedinka = fetch_from_file("beatsHardcodeOnAllLevels.txt")
    # jedinka = [jedinka[0:-1], jedinka[-1]]
    # population = [copy.deepcopy(jedinka) for _ in range(POPULATION_SIZE)]
        
    for i in range(ITERATIONS* 10000):

        fitness = [0 for _ in range(POPULATION_SIZE)]
        for first in range(POPULATION_SIZE-1):
            for second in range(i+1 , POPULATION_SIZE):
                str1 = convert_genome_to_json(population[first])
                str2 = convert_genome_to_json(population[second])
                # print("Sending: ", str1)
                # print("Sending: ", str2)
                game_result = evaluate_game("CGP", str1, "CGP", str2, random.randint(1,5) )
                # print("Result:" ,game_result )
                game_result = parse_result(game_result)
                fitness_first = fitness_from_result(game_result, "Player1")
                fitness_second = fitness_from_result(game_result, "Player2")
                fitness[first] += fitness_first
                fitness[second] += fitness_second
                ############OTHER SIDE

                game_result = evaluate_game("CGP", str2, "CGP", str1, random.randint(1,5) )
                game_result = parse_result(game_result)
                fitness_first = fitness_from_result(game_result, "Player2")
                fitness_second = fitness_from_result(game_result, "Player1")
                fitness[first] += fitness_first
                fitness[second] += fitness_second
                

        for t in range(1,6):
            for first in range(POPULATION_SIZE):
                str1 = convert_genome_to_json(population[first])
                game_result = evaluate_game("CGP", str1, "HCINSTANCE", " ", t)
                game_result = parse_result(game_result)
                fitness_first = fitness_from_result(game_result, "Player1")
                fitness[first] += HARDCODE_MULTIPLIER * fitness_first
                
                #OTHER SIDE
                
                game_result = evaluate_game("HCINSTANCE", " ", "CGP", str1, t)
                game_result = parse_result(game_result)
                fitness_first = fitness_from_result(game_result, "Player2")
                fitness[first] += HARDCODE_MULTIPLIER * fitness_first
                
            
        sorting = list(reversed(sorted(zip(population,fitness), key = lambda x: x[1])))
        fitness = list(map(lambda x: x[1], sorting))
        population = list(map(lambda x: x[0], sorting))
        if i%50 == 0:
            print("Index: ", i)
            alpha = copy.deepcopy(population[0])
            alpha[0].append(alpha[1])
            alpha = alpha[0]
            write_to_file(alpha, f"otherAlpha.txt")
            print("saved %100 alpha with fitness: ", maxFitness)
                 
        if fitness[0] >= maxFitness:
            maxFitness = fitness[0]
            alpha = copy.deepcopy(population[0])
            alpha[0].append(alpha[1])
            alpha = alpha[0]
            write_to_file(alpha, f"trainAlpha.txt")
            print("Saved alpha with fitness: ", maxFitness)
            last_save = i
            
        
        if maxFitness >= 13000:
            HARDCODE_MULTIPLIER = 0.5

        if maxFitness >= 13000:
            alpha = copy.deepcopy(population[0])
            alpha[0].append(alpha[1])
            alpha = alpha[0]
            write_to_file(alpha, f"peakedAlpha.txt")
            print("Saved peak alpha with fitness: ", maxFitness)
            maxFitness = -1e7 
            
                       
        #Make children
        if i-last_save > 300:
            CGP.MUTATION_OF_ELEMENT_PROBABILITY *=1.5
            print("Making mutation probability harder: ",CGP.MUTATION_OF_ELEMENT_PROBABILITY)
            
        parents = copy.deepcopy(population[0:int(POPULATION_SIZE * NEW_PARENT_PERCENT)])
        population = copy.deepcopy(parents)
        
        while len(population) + 2 <= POPULATION_SIZE:
            first = random.choice(parents)
            second = random.choice(parents)
            newChild1, newChild2 = CGP.reproduce_genomes2(first, second)
            newChild1 = CGP.mutate_genome(newChild1)
            newChild2 = CGP.mutate_genome(newChild2)
            population.append(newChild1)
            population.append(newChild2)
        for t in range(POPULATION_SIZE - len(population)):
            population.append(CGP.construct_random_genome_uniform(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES))
        
    return

main()
# Close the sockets
client_socket.close()
server_socket.close()