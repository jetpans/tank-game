from neural_network import *
import socket
import random
import json
from util import *
import copy
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("127.0.0.1", 12345))
server_socket.listen(5)
print("Server is listening... on ",12345)
client_socket, client_address = server_socket.accept()
print("Connected to", client_address)
level = 4

def evaluate_game(type_of_player_1, player_1, type_of_player_2,
                  player_2):  # Returns String of game ending -- might require parsing
    str1 = f"{type_of_player_1}\n{player_1}\n"
    str2 = f"{type_of_player_2}\n{player_2}"

    client_socket.send("START\n".encode('utf-8'))
    client_socket.send(str1.encode('utf-8'))
    client_socket.send(str2.encode('utf-8'))
    client_socket.send(f"LEVEL {level}\n".encode('utf-8'))

    result = client_socket.recv(1024).decode('utf-8')

    return result


def convert_genome_to_json(genome):
    result = copy.deepcopy(genome)

    return result.toJSON()


# dict = {
#     "winner": str
#     "decisions": int
#     "bullets_1": int
#     "bullets_2": int
#     "way_of_victory": str
# }

def parse_result(result):
    result = list(filter(lambda x: x != "" and x != "\n", result.replace("\r", "").split("\n")))
    output = {}
    evals = list(map(lambda x: x.split(":")[1].strip(), result))
    output["winner"] = evals[0]
    output["decisions"] = int(evals[1])
    output["bullets_1"] = int(evals[2])
    output["bullets_2"] = int(evals[3])
    output["way_of_victory"] = evals[4]
    output["position"] = evals[5].replace("(", "").replace(")", "").split(", ")
    return output


def fitness_from_result(result, player):
    x, y = result["position"]
    x = int(x)
    y = int(y)
    return x * y


def main():
    maxFitness = 0
    population = [NeuralNetwork(13, [5, 3], 1) for _ in range(10)]

    for i in range( 10000):

        if i % 500 == 0:
             print(i)
        fitness = [0 for _ in range (10)]

        for first in range(10):
            str1 = convert_genome_to_json(population[first])
            game_result = evaluate_game("NN", str1, "DUMMY", "")
            game_result = parse_result(game_result)
            fitness_first = fitness_from_result(game_result, "Player1")
            fitness[first] += fitness_first

        sorting = list(reversed(sorted(zip(population, fitness), key=lambda x: x[1])))
        fitness = list(map(lambda x: x[1], sorting))

        if i % 20 == 0:
            print(fitness)
        num_networks_to_keep = int(0.5 * len(sorting))
        # Extract the top 50% of neural networks
        selected_networks = [pair[0] for pair in sorting[:num_networks_to_keep]]
        while (len(selected_networks)<10):
            newChild = NeuralNetwork.crossover(sorting[0][0], sorting[1][0])
            newChild.mutate(0.15, 1)
            selected_networks.append(newChild)

        population = copy.deepcopy(selected_networks)
        if fitness[0] > maxFitness:
            maxFitness = fitness[0]
            tempPopulation = copy.deepcopy(population)
            k = 0
            write_to_file_nn(tempPopulation[k], f"alphaCorner.txt")
            print("Saved fitness: ", fitness[k])
    return


main()
# Close the sockets
client_socket.close()
server_socket.close()


