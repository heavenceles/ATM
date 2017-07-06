
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


class ATMPanel extends  JPanel
{

    JButton   button1 ;
    JButton   button2;
    public ATMPanel( ATMWindow dos)
    {
        super();
        this.setLayout(null);
        button1 = new JButton("修改密码");
        button2 = new JButton("取钱");
        button1.setBounds(0,0,100,40);
        button2.setBounds(0,50,100,40);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1.setVisible(false);
                dos.con.removeAll();
                dos.con.add(dos.pan2);
                dos.repaint();
            }
        });
        this.add(button1);
        this.add(button2);
    }




}
class ATMEntrance extends JPanel
{
    private BufferedImage pic;
    JButton Button1;
    JButton Button2;
    JTextField UserName;
    JPasswordField PassWord;
    JLabel Label;
    JLabel Label2;
    public ATMEntrance (ATMWindow dos)
    {
          super();
          this.setLayout(null);
          Label =new JLabel("用户名:",SwingConstants.CENTER);
          Label2=new JLabel("密码:",SwingConstants.CENTER);
          Button1=new JButton("确认");
          Button2=new JButton("退出");
          UserName=new JTextField();
          PassWord=new JPasswordField();
          Label.setBounds(180,150,60,40);

          Label2.setBounds(180,190,60,40);

          UserName.setBounds(240,150,200,40);
          PassWord.setBounds(240,190,200,40);
          Button1.setBounds(240,230,100,40);
          Button2.setBounds(340,230,100,40);

          Button1.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  String name=UserName.getText();
                  String code=new String(PassWord.getPassword());

                  while (true) {
                      Socket socket = null;
                      try {
                          //创建一个流套接字并将其连接到指定主机上的指定端口号
                          socket = new Socket(dos.IP_ADDR, dos.PORT);
                          //读取服务器端数据
                          DataInputStream input = new DataInputStream(socket.getInputStream());
                          //向服务器端发送数据
                          DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                          out.writeUTF(name);
                          out.writeUTF(code);

                          String ret = input.readUTF();
                          System.out.println("服务器端返回过来的是: " + ret);
                          // 如接收到 "OK" 则断开连接
                          if ("OK".equals(ret)) {
                              System.out.println("客户端将关闭连接");
                              out.close();
                              input.close();
                                  dos.con.removeAll();
                                  dos.con.add(dos.pan);
                                  dos.con.validate();
                                  dos.repaint();
                                  UserName.setText("");
                                  PassWord.setText("");
                              break;
                           }
                           else{
                              out.close();
                              input.close();
                              UserName.setText("");
                              PassWord.setText("");
                              break;
                          }



                      } catch (Exception e3) {
                          System.out.println("客户端异常:" + e3.getMessage());
                          System.exit(1);
                      } finally {
                          if (socket != null) {
                              try {
                                  socket.close();
                              } catch (IOException e2) {
                                  socket = null;
                                  System.out.println("客户端 finally 异常:" + e2.getMessage());
                              }
                          }
                      }
                  }





              }
          });

          Button2.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  System.exit(0);
              }
          });

          this.add(Button1);
          this.add(Button2);
          this.add(UserName);
          this.add(PassWord);
          this.add(Label);
          this.add(Label2);
    }




    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        try {
            pic=ImageIO.read(new File(System.getProperty("user.dir")+"\\src\\win.jpg"));
        } catch (IOException e) {
            System.out.println("NO File");
        }
        g.drawImage(pic,0,0,640,480,this);
    }

}
class ATMWindow extends JFrame {
    private final int ATMWidth =640;
    private final int ATMHeight =480;
    public static final String IP_ADDR = "localhost";
    public static final int PORT = 11144;
    Container con;
    ATMPanel pan=new ATMPanel(this);
    ATMEntrance pan2=new ATMEntrance(this);

    public ATMWindow(String s){

        super(s);
        this.setSize(ATMWidth,ATMHeight);
        con = this.getContentPane();
        con.setLayout(new BorderLayout());
        this.setLocation(500,200);

        con.add(pan2,BorderLayout.CENTER);
        this.setVisible(true);
        //this.pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }





    public Dimension getPreferredSize(){
        return new Dimension(500,300);
    }
}


public class Main{
    public static void main(String[] args){

        ATMWindow myWindow = new ATMWindow("ATM");


    }
}
