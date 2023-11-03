package com.logme;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main{
    public static void main(String[] args) throws InterruptedException, IOException {
        int port;
        try{
            port = Integer.parseInt(args[0]);
        }
        catch (ArrayIndexOutOfBoundsException e){
            port = 50051;
        }

        Server server = ServerBuilder.forPort(port).addService(new LogMeService()).build().start();
        System.out.printf("Server started at port %d", port);
        server.awaitTermination();
    }
}