package ru.megafon.deer_messenger_backend;

import io.grpc.stub.StreamObserver;

import java.util.HashSet;

public class MessengerImpl extends MessengerGrpc.MessengerImplBase {
    public Integer idMessege = 0;
    public HashSet<DeerMessengerBackend.Message> dataBase = new HashSet<DeerMessengerBackend.Message>();


    @Override
    public void sendMessage(DeerMessengerBackend.SendMessageRequest request,
                         StreamObserver<DeerMessengerBackend.SendMessageResponse> responseObserver)  {
        if(request.hasMessage()) {
            idMessege++;

            //System.out.println(request);

            DeerMessengerBackend.Message message = DeerMessengerBackend.Message.newBuilder()
                    .setCreatedAt(System.currentTimeMillis())
                    .setId(idMessege)
                    .setSender(request.getMessage().getSender())
                    .setText(request.getMessage().getText())
                    .setReceiver(request.getMessage().getReceiver())
                    .build();

            DeerMessengerBackend.SendMessageResponse response = DeerMessengerBackend.SendMessageResponse.newBuilder()
                    .setMessage(message)
                    .build();

            //System.out.println(response);
            dataBase.add(message);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else {
            System.out.println("Error");

        }
    }

    @Override
    public void getMessagesStream(DeerMessengerBackend.GetMessagesStreamRequest request,
                            StreamObserver<DeerMessengerBackend.GetMessagesStreamResponse> responseObserver) {

        System.out.println(request);
        int idSender = request.getMessage().getSender().getId();
        int idReciever = request.getMessage().getReceiver().getId();
        HashSet<DeerMessengerBackend.Message> subBase = new HashSet<DeerMessengerBackend.Message>();

        for (DeerMessengerBackend.Message mes : dataBase){
            if ((mes.getSender().getId()==idSender)&&(mes.getReceiver().getId()==idReciever))
                subBase.add(mes);
            if ((mes.getSender().getId()==idReciever)&&(mes.getReceiver().getId()==idSender))
                subBase.add(mes);
        }


        for(DeerMessengerBackend.Message mes : subBase){

            DeerMessengerBackend.GetMessagesStreamResponse response = DeerMessengerBackend.GetMessagesStreamResponse.newBuilder()
                    .addMessage(mes)
                    .build();

            responseObserver.onNext(response);
        }

       /* for(int i=0; i<3; i++){
            DeerMessengerBackend.Message message = DeerMessengerBackend.Message.newBuilder()
                .setText("Lol, wow #" + i)
                .build();

            DeerMessengerBackend.GetMessagesStreamResponse response = DeerMessengerBackend.GetMessagesStreamResponse.newBuilder()
                    .addMessage(message)
                    .build();

            responseObserver.onNext(response);
        }*/

        responseObserver.onCompleted();
    }
}