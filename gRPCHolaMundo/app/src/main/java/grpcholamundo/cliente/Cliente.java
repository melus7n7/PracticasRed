package grpcholamundo.cliente;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.OperacionRequest;
import com.proto.saludo.Holamundo.OperacionResponse;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.swing.JOptionPane;


public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        ManagedChannel ch = ManagedChannelBuilder
            .forAddress(host, puerto)
            .usePlaintext()
            .build();

        //Servicio
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);

        //SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("Sulem").build();
        //SaludoResponse respuesta = stub.saludo(peticion);

        while(true){
            String opt = JOptionPane.showInputDialog("Calcular\n" + 
                            "Suma......(1) x + y\n" +
                            "Resta.....(2) x - y\n" +
                            "Multi.....(3) x * y\n" + 
                            "Divis......(4) x / y\n"+
                            "Cancelar para salir");

            if(opt == null){
                
                break;
            }
                
            int x = Integer.parseInt(JOptionPane.showInputDialog("Ingrese x"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Ingrese y"));

            OperacionRequest peticionSuma = OperacionRequest.newBuilder()
                .setNumeroX(x)
                .setNumeroY(y)
                .build();

            OperacionResponse respuesta;

            switch(opt){
                case "1":{
                    respuesta = stub.suma(peticionSuma);
                    JOptionPane.showMessageDialog(null, x + "+" + y + "=" + respuesta.getResultadoNumero());
                    break;
                }
                case "2":{
                    respuesta = stub.resta(peticionSuma);
                    JOptionPane.showMessageDialog(null, x + "-" + y + "=" + respuesta.getResultadoNumero());
                    break;
                }
                case "3":{
                    respuesta = stub.multiplicacion(peticionSuma);
                    JOptionPane.showMessageDialog(null, x + "*" + y + "=" + respuesta.getResultadoNumero());
                    break;
                }
                case "4":{
                    respuesta = stub.division(peticionSuma);
                    JOptionPane.showMessageDialog(null, x + "/" + y + "=" + respuesta.getResultadoNumero());
                    break;
                }
            }
        }
        
        //System.out.println("Respuesta RPC: " + respuesta.getResultado());

        System.out.println("Apagando...");
        ch.shutdown();
    
    }
}