/**
 * By Liuzin
 * **/
//библиотеки подключенные
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//создание окна
public class Client extends JFrame {

    Socket s; // Сокет
    PrintWriter out; // Для записи в поток
    Scanner in; // Для чтения из потока
    JTextArea jta; // Основное текстовое поле чата куда будут падать все сообщения

    public Client() {
        //создание КРАСИВОГО окна
        setTitle("JClient"); // Заголовок окна
        setLocation(1000, 300); // Положение окна
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Завершение программы по закрытию окна
        setSize(400, 500); // Размер окна
        jta = new JTextArea(); // Создаем текстовое поле
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setAutoscrolls(true);
        add(jsp, BorderLayout.CENTER);
        jta.setBackground(Color.black);
        jta.setForeground(Color.green);
        jta.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        JPanel jpBottom = new JPanel();
        JButton jbSEND = new JButton("SEND");
        JTextField jtf = new JTextField();
        jpBottom.setLayout(new BorderLayout());
        jpBottom.add(jbSEND, BorderLayout.EAST);
        jpBottom.add(jtf, BorderLayout.CENTER);
        add(jpBottom, BorderLayout.SOUTH);
        //слушатель для кнопочки (отправка по щелчку)
        jbSEND.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(jtf.getText());
                jtf.setText("");
            }
        });
        //слушатель для закрытия окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosing(e);
                try { // Если окно закрыли
                    out.println("End"); // Посылаем серверу команду завершения сеанса
                    in.close(); // закрываем все открытые потоки  и соединения
                    out.close();
                    s.close();
                } catch (IOException ex1) { //иииисключения
                    System.out.println("Error");
                }
            }
        });

        startClient(); // Подключаемся к серверу
        setVisible(true);
    }

    public void startClient() {
        try { // Пытаемся подключиться
            s = new Socket("localhost", 8080); // Подключаемся к локалхосту на порт 8180
            out = new PrintWriter(s.getOutputStream(), true); // создаем написалку для исходящего потока/stream
            in = new Scanner(s.getInputStream()); // вешаем считывалку на входящий поток/stream
            new Thread(new Runnable() { // Запускает новый поток/thread
                @Override
                public void run() {
                    try { // если разрыв соединения с сервером (произошла ошибка чтения) - мы завершаем работу
                        while (true) { // если все хорошо, бесконечно читаем входящий поток
                            if (in.hasNextLine()) {
                                String s = in.nextLine();
                                jta.append(s + '\n');
                            }
                        }
                    } catch (Exception e) { // если что-то произошло, пишем сообщение о потере соединения
                        e.printStackTrace();
                        System.out.println("Connection Lost");
                    }
                }
            }).start();
        } catch (IOException e) { //иииисключения

        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    } // Отсылка сообщения серверу(в исходящий поток клиента)
}
