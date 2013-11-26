package client;

public enum ClientState { 
    // Client has not yet joined a board.
    ClientStateIdle, 
    
    // Client is loading a board.
    ClientStateLoading, 
    
    // Client is playing on a board.
    ClientStatePlaying
}
