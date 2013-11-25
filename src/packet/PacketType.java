package packet;

enum PacketType {
    // announce that you are a new client in the game   
    PacketTypeNewClient, // client to server

    // announce that you are disconnecting
    PacketTypeDisconnectClient, // client to server
    
    // the entire board sent from the server to the client
    // also includes a list of the clients
    PacketTypeGameState, // server to client
        
    // a message to draw on a specific pixel of the board
    PacketTypeDraw, // sever to client and client to server

    // a message to erase a specific pixel of the board
    PacketTypeErase // server to client and client to server
}