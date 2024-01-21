import os

import numpy
import json

def random_geometric(N, p):
    return min(numpy.random.geometric(p), N)

def fetch_from_file(filename):
    with open(filename, "r",encoding = "UTF-8") as read_file:
        return json.load(read_file)

def write_to_file(data, filename):
    with open(filename, "w", encoding = "UTF-8") as write_file:
        write_file.write("CGP\n")
        json.dump(data, write_file)

def write_to_file_nn(data, filename):
    with open(filename, "w", encoding="UTF-8") as write_file:
        write_file.write("NN\n")
        write_file.write(data.toJSON())

def write_to_path_file_nn(data, filename):
    # Extract the directory path from the filename
    directory = os.path.dirname(filename)

    # If the directory doesn't exist, create it
    if not os.path.exists(directory):
        os.makedirs(directory)

    with open(filename, "w", encoding="UTF-8") as write_file:
        write_file.write("NN\n")
        write_file.write(data.toJSON())

def fetch_from_file(filename):
    with open(filename, "r",encoding = "UTF-8") as read_file:
        read_file.readline()
        return json.load(read_file)