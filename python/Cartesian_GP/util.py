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
        
def fetch_from_file(filename):
    with open(filename, "r",encoding = "UTF-8") as read_file:
        return json.load(read_file)