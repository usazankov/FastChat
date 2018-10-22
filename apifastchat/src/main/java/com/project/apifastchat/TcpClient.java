package com.project.apifastchat;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TcpClient implements ICommLink{

    public static final String SERVERIP = "10.35.90.162"; //your computer IP address
    public static final int SERVERPORT = 1977;
    private ICommLinkListener mMessageListener;
    private boolean mRun = false;
    private int m_msgSize = -1;
    private PrintWriter out;
    private InputStream in;
    private String encoding = "UTF-8";
    private Socket socket;
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(ICommLinkListener listener) {
        mMessageListener = listener;
    }


    @Override
    public void run() {

        mRun = true;
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            SocketAddress sockaddr = new InetSocketAddress(serverAddr, SERVERPORT);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            socket = new Socket();
            socket.connect(sockaddr, 30000);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receive the message which the server sends back
                in = socket.getInputStream();

                if(socket.isConnected()) mMessageListener.onConnect();

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    if(m_msgSize < 0){
                        int av = in.available();
                        if(av / 2 < 4)
                            continue;
                        byte[] buff = new byte[4];
                        if(in.read(buff, 0, 4) < 4) continue;
                        ByteBuffer wrapped = ByteBuffer.wrap(buff);
                        m_msgSize = wrapped.getInt();
                    }else{
                        int av = in.available();
                        if(av / 2 < m_msgSize)
                            continue;
                        byte[] buff = new byte[m_msgSize];
                        in.read(buff, 0, m_msgSize);
                        String value = new String(Arrays.copyOfRange(buff, 0, buff.length), encoding);
                        mMessageListener.messageReceived(value);
                        m_msgSize = -1;
                    }
                }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
                mMessageListener.onDisconnect();
            }
        } catch (Exception e) {
            mMessageListener.onError(e);
            Log.e("TCP", "C: Error", e);
        }
    }

    @Override
    public void stop(){
        mRun = false;
    }

    @Override
    public void send(@NonNull String str) throws IOException {
        if (out != null && !out.checkError()) {
            byte[] bytes = ByteBuffer.allocate(4).putInt(str.length()).array();
            out.print(new String(bytes));
            out.println(str);
            if(out.checkError()){
                stop();
            }
            out.flush();
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void setConnectionTimeout(int msecs) {

    }

    @Override
    public void setRecvTimeoutMsecs(int msecs) {

    }

}
