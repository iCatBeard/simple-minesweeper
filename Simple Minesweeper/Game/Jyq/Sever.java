package Jyq;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Sever {
    private ServerSocket serverSocket;
    private static String name;
    private static String IP;
    private static String LoginTime;
    private static String Time;
    public Sever(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void StartSever() {
        while (!serverSocket.isClosed()) {
            BufferedReader bufferedReader;
            try {
                //开始监听//
                Socket socket = serverSocket.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                name = bufferedReader.readLine();
                LoginTime = bufferedReader.readLine();
                IP = bufferedReader.readLine();
                System.out.println("Name:" + name);
                System.out.println("LoginTime:" + LoginTime);
                System.out.println("IP:" + IP);
                //退出打印信息//
                try {
                    Time = bufferedReader.readLine();
                    System.out.println("GamingTime:" + (999 - Integer.parseInt(Time)));
                    RankingList rankingList = (LazyUtils.ReadObject(Directories.RankingList, RankingList.class));
                    assert rankingList != null;
                    rankingList.addRank(new Rank(IP,  (999 - Integer.parseInt(Time)), name, LoginTime));
                    LazyUtils.WriteObject(Directories.RankingList, rankingList);
                    System.out.println("用户已结束游戏！");
                } catch (SocketException e) {
                    System.out.println("连接已断开，用户没有成功完成游戏！");
                    this.Close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void Close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        //先创建ServerSocket, new Server()//
        ServerSocket serverSocket = new ServerSocket(10024);
        try {
            Sever sever = new Sever(serverSocket);
            sever.StartSever();
            sever.Close();
        }catch (Exception e) {
            serverSocket.close();
        }
    }
}
