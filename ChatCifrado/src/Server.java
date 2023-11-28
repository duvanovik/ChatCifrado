import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor esperando conexiones...");

            try (Socket clientSocket = serverSocket.accept();
                 DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar Diffie-Hellman
                DiffieHellman dh = new DiffieHellman();
                byte[] publicKey = dh.getEncodedPublicKey();

                // Enviar clave pública al cliente
                dos.writeInt(publicKey.length);
                dos.write(publicKey);
                
                //TEST
                System.out.println("Clave publica del servidor (enviada): " + Arrays.toString(publicKey));

                // Recibir clave pública del cliente
                int keyLength = dis.readInt();
                byte[] clientPubKey = new byte[keyLength];
                dis.readFully(clientPubKey);
                
                //TEST
                System.out.println("Clave pública del cliente (recibida): " + Arrays.toString(clientPubKey));


                // Generar clave compartida
                byte[] sharedSecret = dh.generateSharedSecret(clientPubKey);
                AES aes = new AES(sharedSecret);
                
                //TEST
                System.out.println("Clave compartida en el servidor: " + Arrays.toString(sharedSecret));

                
                // Lógica del chat cifrado
                boolean continuar = true;
                while (continuar) {
                    try {
                        // Recibir mensaje cifrado
                        int messageLength = dis.readInt();
                        if (messageLength > 0) {
                            byte[] encryptedMessage = new byte[messageLength];
                            dis.readFully(encryptedMessage);
                            String message = aes.decrypt(encryptedMessage);
                            System.out.println("Mensaje descifrado en el servidor: " + message);


                            // Procesar mensaje
                            System.out.println("Cliente dice: " + message);

                            // Responder al cliente
                            String response = "Mensaje recibido: " + message;
                            byte[] encryptedResponse = aes.encrypt(response);
                            System.out.println("Respuesta cifrada del servidor: " + Arrays.toString(encryptedResponse));
                            dos.writeInt(encryptedResponse.length);
                            dos.write(encryptedResponse);
                        } else {
                            continuar = false;
                        }
                    } catch (IOException e) {
                        System.out.println("Error al recibir mensaje: " + e.getMessage());
                        continuar = false;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error en la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("No se pudo iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
