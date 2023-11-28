import java.net.Socket;
import java.util.Arrays;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        String host = "localhost"; // Cambia a la dirección IP del servidor
        int port = 12345;

        try (Socket socket = new Socket(host, port);
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor");

            // Iniciar Diffie-Hellman
            DiffieHellman dh = new DiffieHellman();

            // Recibir clave pública del servidor
            int keyLength = dis.readInt();
            byte[] serverPubKey = new byte[keyLength];
            dis.readFully(serverPubKey);
            
            //TEST
            System.out.println("Clave pública del servidor (recibida): " + Arrays.toString(serverPubKey));


            // Enviar clave pública al servidor
            byte[] publicKey = dh.getEncodedPublicKey();
            dos.writeInt(publicKey.length);
            dos.write(publicKey);
            
            //TEST
            System.out.println("Clave pública del cliente (enviada): " + Arrays.toString(publicKey));

            
            // Generar clave compartida
            byte[] sharedSecret = dh.generateSharedSecret(serverPubKey);
            AES aes = new AES(sharedSecret);
            System.out.println("Clave compartida en el cliente: " + Arrays.toString(sharedSecret));

            // Lógica del chat cifrado
            boolean continuar = true;
            while (continuar) {
                System.out.print("Escribe un mensaje (o 'salir' para finalizar): ");
                String messageToSend = consoleInput.readLine();

                if ("salir".equalsIgnoreCase(messageToSend)) {
                    continuar = false;
                } else {
                    // Enviar mensaje cifrado
                    byte[] encryptedMessage = aes.encrypt(messageToSend);
                    System.out.println("Mensaje cifrado del cliente: " + Arrays.toString(encryptedMessage));
                    dos.writeInt(encryptedMessage.length);
                    dos.write(encryptedMessage);

                    // Recibir y descifrar respuesta
                    int responseLength = dis.readInt();
                    byte[] encryptedResponse = new byte[responseLength];
                    dis.readFully(encryptedResponse);
                    String response = aes.decrypt(encryptedResponse);
                    System.out.println("Respuesta descifrada en el cliente: " + response);
                    System.out.println("Servidor responde: " + response);
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
