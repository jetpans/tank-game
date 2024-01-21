import datetime

from neural_network import *
import socket
import random
import json
from util import *
import copy
import math
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
    str2 = f"{type_of_player_2}\n{player_2}\n"

    client_socket.send("START\n".encode('utf-8'))
    client_socket.send(str1.encode('utf-8'))
    client_socket.send(str2.encode('utf-8'))
    client_socket.send(f"LEVEL {level}\n".encode('utf-8'))

    result = client_socket.recv(1024).decode('utf-8')

    return result


def convert_genome_to_json(genome):
    result = copy.deepcopy(genome)

    return result.toJSON()


def parse_result(result):
    result = list(filter(lambda x: x != "" and x != "\n", result.replace("\r", "").split("\n")))
    output = {}
    evals = list(map(lambda x: x.split(":")[1].strip(), result))
    output["winner"] = evals[0]
    output["decisions"] = int(evals[1])
    output["bullets_1"] = int(evals[2])
    output["bullets_2"] = int(evals[3])
    output["way_of_victory"] = evals[4]
    output["distance"] = float(evals[6])
    return output


def fitness_from_result(result):
    winner = result["winner"]
    decisions = result["decisions"]
    bullets_1 = result["bullets_1"]
    bullets_2 = result["bullets_2"]
    way_of_victory = result["way_of_victory"]

    fitness_scores = [0, 0]

    if winner == "Player1":
        fitness_scores[0] += 5
        fitness_scores[1] -= (5 + 1/decisions)
        if way_of_victory == "MURDER":
            fitness_scores[0] += (5 - bullets_1*0.6)
        elif way_of_victory == "SUICIDE":
            fitness_scores[1] -= (15 + bullets_2/50)
    elif winner == "Player2":
        fitness_scores[1] += 5
        fitness_scores[0] -= (5 + 1/decisions)
        if way_of_victory == "MURDER":
            fitness_scores[1] += (5 - bullets_2*0.6)
        elif way_of_victory == "SUICIDE":
            fitness_scores[0] -= (15 + bullets_1/50)
    else:
        if bullets_1 > 0:
            fitness_scores[0] += 0.05
        if bullets_2 > 0:
            fitness_scores[1] += 0.05
        if bullets_1 > bullets_2:
            fitness_scores[0] += 0.1
        if bullets_2 > bullets_1:
            fitness_scores[1] += 0.1

    return fitness_scores


def fitness_distance(result):
    distance = result["distance"]
    return [1/distance, 1/distance]


def generate_groups(x, y):
    # Check if x is divisible by y
    if x % y != 0:
        print(f"{x} is not divisible by {y}. Please choose a valid pair.")
        return

    # Generate a list of numbers from 0 to x-1
    numbers = list(range(x))

    # Shuffle the list to randomize the order
    random.shuffle(numbers)

    # Divide the shuffled list into groups of 5
    groups = [numbers[i:i+y] for i in range(0, len(numbers), y)]

    return groups


def create_small_group(original_list, group):
    new_list = [copy.deepcopy(original_list[i]) for i in group]
    return new_list


def generate_unique_pairs(y):
    pairs_set = set()

    for i in range(y):
        for j in range(i + 1, y):
            pair = (i, j) if i < j else (j, i)
            pairs_set.add(pair)

    return list(pairs_set)


def calculate_percentages(input_array):
    total = sum(input_array)
    if total == 0:
        return [1/len(input_array) for _ in range(len(input_array))]
    percentages = [element / total for element in input_array]
    return percentages

def map_to_target_sum(arr, target_sum):
    # Calculate the sum of the positive array
    current_sum = sum(arr)
    # Check if the sum is zero to avoid division by zero
    if current_sum == 0:
        return [0] * len(arr)
    # Calculate scaling factor to make the new sum equal to the target sum
    scaling_factor = target_sum / current_sum
    # Map each element in the positive array to the new array
    new_array = [num * scaling_factor for num in arr]
    return new_array

def get_random_index_from_array(arr):
    rand =  random.uniform(0, 1)
    stat = 0
    for i in range(len(arr)):
        stat+=arr[i]
        if rand < stat:
            return i

group_size = 5
#biger groups require smaller percantages
percentage_of_group_to_keep = 0.5
#pls make it dividable with group_size
generation_size = 100
#zadnji mora biti 3
nn_model = [3]
level = 2
max_generations = 1000

def main():
    maxFitness = 0
    population = [NeuralNetwork(13, nn_model, 1) for _ in range(generation_size)]

    for i in range(max_generations+1):


        groups = generate_groups(generation_size,group_size)
        newPopulation = []
        for j in range(len(groups)):
            group = groups[j]
            fitness = [0 for _ in range(group_size)]
            small_population = create_small_group(population, group)
            unique_pairs = generate_unique_pairs(group_size)
            for pair in unique_pairs:

                str1 = convert_genome_to_json(small_population[pair[0]])
                str2 = convert_genome_to_json(small_population[pair[1]])
                game_result = evaluate_game("NN", str1, "NN", str1)

                game_result = parse_result(game_result)
                if i<200:
                    fitness_pair = fitness_distance(game_result)
                else:
                    fitness_pair = fitness_from_result(game_result)
                fitness[pair[0]] += fitness_pair[0]
                fitness[pair[1]] += fitness_pair[1]

            sorting = list(reversed(sorted(zip(small_population, fitness), key=lambda x: x[1])))
            fitness = list(map(lambda x: x[1], sorting))
            if i % 20 == 0:
                print(i, ": ", datetime.datetime.now(),"  ",fitness," ",[new[0] for new in sorting])
            if i % 50 == 0:
                prvi = "progress/generation"+str(i)+"/group"+str(j)+"/prvi.txt"
                drugi = "progress/generation"+str(i)+ "/group"+str(j)+ "/drugi.txt"
                zadnji = "progress/generation"+str(i)+ "/group"+str(j)+ "/zadnji.txt"
                write_to_path_file_nn(sorting[0][0], prvi)
                write_to_path_file_nn(sorting[1][0], drugi)
                write_to_path_file_nn(sorting[-1][0], zadnji)


            num_networks_to_keep = int(percentage_of_group_to_keep * len(sorting))
            selected_networks = [pair[0] for pair in sorting[:num_networks_to_keep]]

            fitness = [-0.0001 if num == 0 else num for num in fitness]
            positive_numbers = [num for num in fitness if num > 0]
            total_percentages = map_to_target_sum(positive_numbers,8)
            negative_numbers = [num for num in fitness if num <= 0]
            if len(negative_numbers)!=0:
                min_value = min(negative_numbers)
                negative_numbers = [num - min_value for num in negative_numbers]
                total_percentages = total_percentages + map_to_target_sum(negative_numbers,2)
            total_percentages = calculate_percentages(total_percentages)
            while (len(selected_networks) < group_size):
                newChild = NeuralNetwork.crossover(sorting[get_random_index_from_array(total_percentages)][0], sorting[get_random_index_from_array(total_percentages)][0])
                newChild.mutate(0.25, 2)
                selected_networks.append(newChild)
            tempPopulation = copy.deepcopy(selected_networks)
            newPopulation+=tempPopulation
        population = copy.deepcopy(newPopulation)

    return


main()
# Close the sockets
client_socket.close()
server_socket.close()


