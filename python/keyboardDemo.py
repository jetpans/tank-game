import socket
import keyboard
import json
# Create a socket object
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

server_socket.bind(("127.0.0.1", 12345))

server_socket.listen(5)

print("Server is listening... on ",12345)

client_socket, client_address = server_socket.accept()

print("Connected to", client_address)


keyMap = {
    "q":"FIRE,0\n",
    "w":"FORWARD,0\n",
    "a":"LEFT,0\n",
    "s":"BACKWARD,0\n",
    "d":"RIGHT,0\n"
}
lastKeys = set()
activeKeys = set()
while True:
    message = "_"
    lastKeys = set(activeKeys)
    activeKeys = set()
    if keyboard.is_pressed('q'): 
        activeKeys.add('q')
    if keyboard.is_pressed('w'): 
        activeKeys.add('w')
    if keyboard.is_pressed('a'): 
        activeKeys.add('a')
    if keyboard.is_pressed('s'): 
        activeKeys.add('s')
    if keyboard.is_pressed('d'): 
        activeKeys.add('d')
    if keyboard.is_pressed('p'):
        print("Asking for state")
        message = "GET_STATE,0\n"
        client_socket.send(message.encode('utf-8'))
        data=client_socket.recv(1024)
        message = json.loads(data)
        print(message)
        
    for key in activeKeys:
        message = keyMap[key]
        client_socket.send(message.encode('utf-8'))
    if 'w' not in activeKeys and 's' not in activeKeys and ('w' in lastKeys or 's' in lastKeys):
        message = "STOP_LINEAR,0\n"
        client_socket.send(message.encode('utf-8'))
    if 'a' not in activeKeys and 'd' not in activeKeys and ('a' in lastKeys or 'd' in lastKeys):
        message = "STOP_ANGULAR,0\n"
        client_socket.send(message.encode('utf-8'))
    
    
# Close the sockets
client_socket.close()
server_socket.close()