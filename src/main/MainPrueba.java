package main;

import client.EmailClientConnection;

public class MainPrueba {

    public static void main(String[] args) {
        EmailClientConnection EmailClientConnection = new EmailClientConnection();
        
        EmailClientConnection.ConectionImap();
        
        EmailClientConnection.ConectionSMTP();
        
        EmailClientConnection.CloseConectionImap();
        
        EmailClientConnection.CloseConectionSMTP();

    }
}