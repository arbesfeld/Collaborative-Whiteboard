package client;

public enum ClientState { 
    // Client is loading a board or waiting to load a board.
    ClientStateLoading, 
    
    // Client is playing on a board.
    ClientStatePlaying
}
