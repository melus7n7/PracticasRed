package grpcjavaaudio.cliente;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.proto.audio.AudioServiceGrpc;
import com.proto.audio.Audio.DownloadFileRequest;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;
        String nombre;

        ManagedChannel ch = ManagedChannelBuilder.forAddress(host, puerto).usePlaintext().build();

        /*
        //Reproducción mientras es descargado
        nombre = "anyma.wav";
        streamWav(ch, nombre, 48000F);

        System.out.println("Apagando...");
        ch.shutdown();
        */
        
        //Reproducción ya descargado previamente
        nombre = "KirbyDreamLand3_intro.mp3";
        ByteArrayInputStream streamMP3 = downloadFile(ch, nombre);
        playMp3(streamMP3, nombre);
        try {
            streamMP3.close();
        } catch (IOException e) {
            // TODO: handle exception
        }

        /*
        nombre = "KirbyDreamLand3_intro.wav";
        ByteArrayInputStream streamWav = downloadFile(ch, nombre);
        playWav(streamWav, nombre);
        try {
            streamWav.close();
        } catch (IOException e) {
            // TODO: handle exception
        }*/

    }

    public static void streamWav(ManagedChannel ch, String nombre, float sampleRate){
        try{
            AudioFormat newFormat = new AudioFormat(sampleRate, 16, 2, true, false);
            SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(newFormat);
            sourceDataLine.open(newFormat);
            sourceDataLine.start();
    
            AudioServiceGrpc.AudioServiceBlockingStub stub = AudioServiceGrpc.newBlockingStub(ch);
            DownloadFileRequest peticion = DownloadFileRequest.newBuilder().setNombre(nombre).build();
            
            int bufferSize = 1024;
            System.out.println("Reproduciendo el archivo: " + nombre);
    
            stub.downloadAudio(peticion).forEachRemaining(respuesta ->{
                try {
                    sourceDataLine.write(respuesta.getData().toByteArray(), 0, bufferSize);
                    System.out.print(".");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            });
    
            System.out.println("\nRecepción de datos correcta.");
            System.out.println("Reproducción terminada.\n\n");
    
            sourceDataLine.drain();
            sourceDataLine.close();
        }catch(LineUnavailableException e){
            System.out.println(e.getMessage());
        }
        
    }

    public static ByteArrayInputStream downloadFile(ManagedChannel ch, String nombre){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        AudioServiceGrpc.AudioServiceBlockingStub stub = AudioServiceGrpc.newBlockingStub(ch);
        DownloadFileRequest peticion = DownloadFileRequest.newBuilder().setNombre(nombre).build();
        System.out.println("Recibiendo el archivo: " + nombre);

        stub.downloadAudio(peticion).forEachRemaining(respuesta ->{
            try {
                stream.write(respuesta.getData().toByteArray());
                System.out.print(".");
            } catch (IOException e) {
                System.out.println("No se pudo obtener el archivo");
            }
        });

        System.out.println("\nRecepción de datos correcta\n\n");
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public static void playWav(ByteArrayInputStream inStream, String nombre){
        try{
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(inStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            System.out.println("Reproduciendo el archivo: " + nombre + "...\n\n");
            clip.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clip.stop();
            
        }catch(UnsupportedAudioFileException | IOException e){
            e.printStackTrace();
        }catch(LineUnavailableException e){
            e.printStackTrace();
        }
    }

    public static void playMp3(ByteArrayInputStream inStream, String nombre){
        try{
            System.out.println("Reproduciendo el archivo: " + nombre + "....\n\n");
            Player player = new Player(inStream);
            player.play();
        }catch(JavaLayerException e){

        }
    }
}
