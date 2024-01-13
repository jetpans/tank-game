import random
import math
import json
x1 = -10
x2 = 10
samples = 21
# Define the function f(x)
def write_to_file(data, filename):
    with open(filename, "w", encoding = "UTF-8") as write_file:
        json.dump(data, write_file)
def fetch_from_file(filename):
    with open(filename, "r",encoding = "UTF-8") as read_file:
        return json.load(read_file)
    
    
def f(x):
    return x**2 * math.sin(x) + x - 4

# Generate 15 random (x, y) tuples
random_tuples = []
for i in range(samples):
    random_tuples.append((x1 + (x2-x1)/samples * i, f(x1 + (x2-x1)/samples * i)))

# Print the generated tuples
print(random_tuples)
write_to_file(random_tuples,"ulaz.json")