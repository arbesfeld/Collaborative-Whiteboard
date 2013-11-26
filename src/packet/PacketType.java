package packet;

enum PacketType {
    // announce that you are a new client in the game   
    PacketTypeNewClient, // client to server and server to client

    // announce that you are disconnecting
    PacketTypeDisconnectClient, // client to server and server to client
    
    // the entire board sent from the server to the client
    // also includes a list of the clients
    PacketTypeGameState, // server to client
        
    // all the boards in the game
    PacketTypeBoards, // server to client
    
    // all the boards on the server
    // a message to draw on a specific pixel of the board
    PacketTypeDrawPixel, // sever to client and client to server
}