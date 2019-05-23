package ru.megafon.deer_messenger_backend;

import io.grpc.stub.StreamObserver;

public class MessengerImpl extends MessengerGrpc.MessengerImplBase {
    @Override
    public void sendMessage(DeerMessengerBackend.SendMessageRequest request,
                         StreamObserver<DeerMessengerBackend.SendMessageResponse> responseObserver) {

        System.out.println(request);

        DeerMessengerBackend.Message message = DeerMessengerBackend.Message.newBuilder()
                .setText("Hello there, " + request.getMessage().getText())
                .build();

        DeerMessengerBackend.SendMessageResponse response = DeerMessengerBackend.SendMessageResponse.newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void getMessagesStream(DeerMessengerBackend.GetMessagesStreamRequest request,
                            StreamObserver<DeerMessengerBackend.GetMessagesStreamResponse> responseObserver) {

        System.out.println(request);

        for(int i=0; i<3; i++){
            DeerMessengerBackend.Message message = DeerMessengerBackend.Message.newBuilder()
                .setText("Lol, wow #" + i)
                .build();

            DeerMessengerBackend.GetMessagesStreamResponse response = DeerMessengerBackend.GetMessagesStreamResponse.newBuilder()
                    .addMessage(message)
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}
