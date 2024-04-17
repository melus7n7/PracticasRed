package grpcholamundo.servidor;

import com.proto.saludo.Holamundo.OperacionRequest;
import com.proto.saludo.Holamundo.OperacionResponse;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import com.proto.saludo.SaludoServiceGrpc.SaludoServiceImplBase;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends SaludoServiceImplBase{
    @Override
    public void saludo(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver){
        SaludoResponse respuesta = SaludoResponse.newBuilder().setResultado("Hola " + request.getNombre()).build();

        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }

    @Override
    public void suma(OperacionRequest request, StreamObserver<OperacionResponse> responseObserver){
        int numeroX = request.getNumeroX();
        int numeroY = request.getNumeroY();
        int resultadoNumero = numeroX + numeroY;
        OperacionResponse respuestaSuma = OperacionResponse.newBuilder().setResultadoNumero(resultadoNumero).build();

        responseObserver.onNext(respuestaSuma);
        responseObserver.onCompleted();
    }

    @Override
    public void resta(OperacionRequest request, StreamObserver<OperacionResponse> responseObserver){
        int numeroX = request.getNumeroX();
        int numeroY = request.getNumeroY();
        int resultadoNumero = numeroX - numeroY;
        OperacionResponse respuestaSuma = OperacionResponse.newBuilder().setResultadoNumero(resultadoNumero).build();

        responseObserver.onNext(respuestaSuma);
        responseObserver.onCompleted();
    }

    @Override
    public void multiplicacion(OperacionRequest request, StreamObserver<OperacionResponse> responseObserver){
        int numeroX = request.getNumeroX();
        int numeroY = request.getNumeroY();
        int resultadoNumero = numeroX * numeroY;
        OperacionResponse respuestaSuma = OperacionResponse.newBuilder().setResultadoNumero(resultadoNumero).build();

        responseObserver.onNext(respuestaSuma);
        responseObserver.onCompleted();
    }

    @Override
    public void division(OperacionRequest request, StreamObserver<OperacionResponse> responseObserver){
        int numeroX = request.getNumeroX();
        int numeroY = request.getNumeroY();
        int resultadoNumero = numeroX / numeroY;
        OperacionResponse respuestaSuma = OperacionResponse.newBuilder().setResultadoNumero(resultadoNumero).build();

        responseObserver.onNext(respuestaSuma);
        responseObserver.onCompleted();
    }
}
