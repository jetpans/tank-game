import socket
import keyboard
import json
from CGPModel import *
# Create a socket object
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

server_socket.bind(("127.0.0.1", 12345))

server_socket.listen(5)

print("Server is listening... on ",12345)

client_socket, client_address = server_socket.accept()

print("Connected to", client_address)


while True:
    
    genome1 = construct_random_genome(10, 3)
    genome2 = construct_random_genome(10, 3)
    
    
    genome1[0].append(genome1[1])
    genome2[0].append(genome2[1])
    str1 = "CGP\n" + json.dumps(genome1[0]) + "\n"
    str2 = "CGP\n" + json.dumps(genome2[0]) + "\n"
    print(str1)
    print(str2)
    client_socket.send("START\n".encode('utf-8'))
    client_socket.send(str1.encode('utf-8'))
    client_socket.send(str2.encode('utf-8'))

    result = client_socket.recv(1024).decode('utf-8')
    if result:
        print(result)
    input()
    
    
# Close the sockets
client_socket.close()
server_socket.close()