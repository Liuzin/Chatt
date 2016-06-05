/**
 * By Liuzin
 * **/
//библиотеки, которые используются
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainClass {
    public static void main(String[] args) {
        try {
            int x = 0;
            ServerSocket server = new ServerSocket(8080); //создание сокета и освобождение порта, прослушка
            System.out.println("Port 8080 Listening. Waiting for Connection...");
            while (true) {
                Socket s = server.accept();
                new Thread(new ClientHandler(s)).start(); //подключение клиента, создание потока для него
                System.out.println("Client connected");
                x++;
                if (x > 100) break; //ломается все, если клиентов больше 100 :(
            }
            System.out.println("Connection closed");
            server.close(); //отключение сервера
        } catch (IOException e) { //ииииииисключения
            e.printStackTrace();
        }

    }
}
