package main;

import Client.EmailClientConnection;

public class Main {

    public static void main(String[] args) {
        EmailClientConnection EmailClientConnection = new EmailClientConnection();
        
        EmailClientConnection.ConectionImap();
        
        EmailClientConnection.ConectionSMTP();
        
        EmailClientConnection.CloseConectionImap();
        
        EmailClientConnection.CloseConectionSMTP();

    }
}