/**
 * By Liuzin
 * **/
//библиотеки, которые используем
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    //по сути все тоже самое, что и в клиенте: сокет, сканнер, принтврайтер (для получения и чтения сообщений)
    private Socket s;
    private Scanner in;
    private PrintWriter out;
    private String name;
    public static int CLIENTS_NUM = 0;
    //присвоение клиенту номера
    public ClientHandler(Socket s) {
        CLIENTS_NUM++;
        this.s = s;
        name = "Client #" + CLIENTS_NUM;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(s.getInputStream()); //считываение написанного
            out = new PrintWriter(s.getOutputStream(), true); //написание сообщения от сервера
            out.println("Hello, User!");
            while (true) {
                //переписка
                if (in.hasNext()) {
                    String str = in.nextLine(); //считываение сообщений
                    System.out.println(name + ": " + str); //имя клиента + его сообщение
                    out.println("Echo: " + str); //ответ
                    if (str.equals("end")) //если написали "end", то все заканчивается
                        break;
                }
            }
            s.close(); //закрытие сокета
        } catch (Exception e) { //иииииииисключения
        }
    }
}
