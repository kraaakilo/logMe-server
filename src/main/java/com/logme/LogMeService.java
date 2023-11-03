package com.logme;

import com.logme.file.LogMEGrpc;
import com.logme.file.LogMe;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class LogMeService extends LogMEGrpc.LogMEImplBase {

    private static final String url = "/tmp/logMe.txt";

    @Override
    public void addLog(LogMe.AddLogRequest request, StreamObserver<LogMe.AddLogResponse> responseObserver) {
        try {
            File file = new File(url);
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(
                    String.format("[%s] %s : %s\n",request.getDate(),request.getLevel().toUpperCase(),request.getContent())
            );

            Database database = new Database();
            database.insert(request);

            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Cannot write log as this time.");
            System.exit(0);
        }

        responseObserver.onNext(
                LogMe.AddLogResponse.newBuilder().setSuccess(true).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getLog(LogMe.GetLogRequest request, StreamObserver<LogMe.GetLogResponse> responseObserver) {
        try{
            File file = new File(url);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder logs = new StringBuilder();

            while (bufferedReader.ready()){
                logs.append(bufferedReader.readLine()).append("\n");
            }

            responseObserver.onNext(
                    LogMe.GetLogResponse.newBuilder().setContent(logs.toString()).build()
            );
            responseObserver.onCompleted();
            bufferedReader.close();
        }
        catch (Exception e){
            System.out.println("Cannot read log at this time.");
            System.exit(0);
        }
    }
}