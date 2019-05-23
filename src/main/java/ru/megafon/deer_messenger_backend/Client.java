package ru.megafon.deer_messenger_backend;

import io.grpc.*;
import io.grpc.stub.*;

public class Client {
    public static void main( String[] args ) throws Exception
    {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext(true)
                .build();

        // Replace the previous synchronous code with asynchronous code.
        // This time use an async stub:
        final MessengerGrpc.MessengerStub stub = MessengerGrpc.newStub(channel);

        DeerMessengerBackend.Message message = DeerMessengerBackend.Message.newBuilder()
                .setText("Terminator 40000")
                .build();

        // Construct a request
        DeerMessengerBackend.SendMessageRequest request =
                DeerMessengerBackend.SendMessageRequest.newBuilder()
                        .setMessage(message)
                        .build();

        // SEND MESSAGES

        stub.sendMessage(request, new StreamObserver<DeerMessengerBackend.SendMessageResponse>() {
            public void onNext(DeerMessengerBackend.SendMessageResponse response) {
                System.out.println(response);
            }
            public void onError(Throwable throwable) {
            }
            public void onCompleted() {

                DeerMessengerBackend.GetMessagesStreamRequest emptyRequest =
                        DeerMessengerBackend.GetMessagesStreamRequest.newBuilder()
                                .build();

                // GET MESSAGES AFTER SENDING

                stub.getMessagesStream(emptyRequest, new StreamObserver<DeerMessengerBackend.GetMessagesStreamResponse>() {
                    public void onNext(DeerMessengerBackend.GetMessagesStreamResponse response) {
                        System.out.println(response);
                    }
                    public void onError(Throwable throwable) {
                    }
                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });

            }
        });
    }
}
