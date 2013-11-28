package packet;

public enum PacketType {
    // announce that you are a new client in the game   
    PacketTypeNewClient, // client to server
    
    // announce the creation of a new board
    PacketTypeNewBoard, // client to server
    
    // announce that a client has just joined a board
    PacketTypeJoinBoard, // client to server and server to client

    // announce that a client has just exited a board
    PacketTypeExitBoard, // client to server and server to client
    
    // the entire board sent from the server to the client
    // also includes a list of the clients
    PacketTypeBoardModel, // server to client
    
    // all the boards in the game
    PacketTypeBoardIdentifierList, // server to client
    
    // all the boards on the server
    // a message to draw something on a board
    PacketTypeDrawCommand, // sever to client and client to server
}