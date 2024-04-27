package grpcjavaaudio.servidor;

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.ByteString;
import com.proto.audio.Audio.DataChunkResponse;
import com.proto.audio.Audio.DownloadFileRequest;
import com.proto.audio.AudioServiceGrpc.AudioServiceImplBase;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends AudioServiceImplBase {
    @Override
    public void downloadAudio(DownloadFileRequest request, StreamObserver<DataChunkResponse> responseObserver){
        String nombreArchivo = "/" + request.getNombre();
        System.out.println("\n\nEnviando el archivo: " + request.getNombre());

        InputStream fileStream = ServidorImpl.class.getResourceAsStream(nombreArchivo);

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length;

        try{
            while((length = fileStream.read(buffer, 0, bufferSize)) != -1){
                DataChunkResponse respuesta = DataChunkResponse.newBuilder()
                        .setData(ByteString.copyFrom(buffer, 0, length))
                        .build();

                        System.out.print(".");

                responseObserver.onNext(respuesta);
            }
            fileStream.close();
        }catch(IOException ex){
            System.out.println("No se pudo obtener el archivo " + nombreArchivo);
        }

        responseObserver.onCompleted();
    }
}
