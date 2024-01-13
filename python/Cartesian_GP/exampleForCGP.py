import socket
import json
import CGPModel as CGP
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


def main():
    #...
    while True:
        first_genome = CGP.construct_random_genome(10,3)
        second_genome = CGP.construct_random_genome(10,3)
        
        first_json = convert_genome_to_json(first_genome)
        second_json = convert_genome_to_json(second_genome)
        
        result = evaluate_game("CGP", first_json, "CGP", second_json) 
        
        print(result)
        input()
    ##Write your code here

    #...
    return

main()
# Close the sockets
client_socket.close()
server_socket.close()