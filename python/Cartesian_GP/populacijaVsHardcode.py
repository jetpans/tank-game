import socket
import json
import CGPModel as CGP
from util import * 
import copy
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("127.0.0.1", 12345))
server_socket.listen(5)
print("Server is listening... on ",12345)
client_socket, client_address = server_socket.accept()
print("Connected to", client_address)


def evaluate_game(type_of_player_1, player_1, type_of_player_2, player_2): #Returns String of game ending -- might require parsing
    str1 = f"{type_of_player_1}\n{player_1}\n"
    str2 = f"{type_of_player_2}\n{player_2}\n"
    
    client_socket.send("START\n".encode('utf-8'))
    client_socket.send(str1.encode('utf-8'))
    client_socket.send(str2.encode('utf-8'))
    
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
    LOSE_WEIGHT = -100
    DRAW_WEIGHT = 0
    WEIGHT_PER_BULLET = -5
    MURDER_WEIGHT = 100
    SUICIDE_WEIGHT = -1000
    
    myBullets = result["bullets_1"] if player.endswith("1") else result["bullets_2"]
    
    suma = 0
    
    # suma += myBullets * WEIGHT_PER_BULLET
    # suma += -1 * result["decisions"] ** 0.5

    if result["winner"] == "DRAW":
        ##Nagrada za ne gubljenje, kazna za ispaljene metke
        suma += DRAW_WEIGHT
        return suma
    elif result["winner"] == player:
        #Nagrada za pobjedu, kazna po metku, nagrada za MURDER, kazna za trajanje igre
        suma += WIN_WEIGHT
        suma += MURDER_WEIGHT if result["way_of_victory"] == "MURDER" else 0
        return suma
    else:
        suma += LOSE_WEIGHT
        suma += SUICIDE_WEIGHT if result["way_of_victory"] == "SUICIDE" else 0
        return suma
        

NUMBER_OF_EXTRA_NODES = 30
NUMBER_OF_INPUT_NODES = 11
ITERATIONS = 50000
def main():

    
    
    population = [CGP.construct_random_genome(NUMBER_OF_EXTRA_NODES , NUMBER_OF_INPUT_NODES) for _ in range(3)]
    for p in population:
        print(p)
    fitness = [0,0,0]
    for i in range(ITERATIONS* 10000):

        if i%500 == 0:
            tempPopulation = copy.deepcopy(population)
            for k in range(len(tempPopulation)):
                tempPopulation[k][0].append(tempPopulation[k][1])
                write_to_file(tempPopulation[k][0], f"result{k}.txt")
            print(i)
        fitness[2] = fitness[1] * 0.8
        
        for first in range(3):
            str1 = convert_genome_to_json(population[first])
            game_result = evaluate_game("CGP", str1, "HCINSTANCE", " " )
            game_result = parse_result(game_result)
            fitness_first = fitness_from_result(game_result, "Player1")
            fitness[first] += fitness_first
        
        sorting = list(reversed(sorted(zip(population,fitness), key = lambda x: x[1])))
        fitness = list(map(lambda x: x[1], sorting))
        if i%50 == 0:
            print(fitness)
        newChild = CGP.reproduce_genomes2(sorting[0][0], sorting[1][0])[0]
        newChild = CGP.mutate_genome(newChild)
        
        population = [sorting[0][0], sorting[1][0], newChild]
        
    return

main()
# Close the sockets
client_socket.close()
server_socket.close()