import socket
import json


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


def main():
    #...
    
    ##Write your code here

    #...
    return



main()
# Close the sockets
client_socket.close()
server_socket.close()