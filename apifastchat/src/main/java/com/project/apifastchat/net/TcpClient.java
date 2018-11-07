package com.project.apifastchat.net;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TcpClient implements ICommLink {

    public static final String SERVERIP = "192.168.1.104";
    public static final int SERVERPORT = 1979;
    private ICommLinkListener mMessageListener;
    private boolean mRun = false;
    private int m_msgSize = -1;
    private OutputStream out;
    private InputStream in;
    private String encoding = "UTF-8";
    private Socket socket;
    private Context ctx;
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(Context appContext) {
        ctx = appContext;
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

                out = socket.getOutputStream();

                //receive the message which the server sends back
                in = socket.getInputStream();

                if(socket.isConnected()) {
                    if(mMessageListener != null) mMessageListener.onConnect();
                }

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    if(m_msgSize < 0){
                        int av = in.available();
                        if(av < 4)
                            continue;
                        byte[] buff = new byte[4];
                        if(in.read(buff, 0, 4) < 4) continue;
                        ByteBuffer wrapped = ByteBuffer.wrap(buff);
                        m_msgSize = wrapped.getInt();
                    }else{
                        int av = in.available();
                        if(av < m_msgSize)
                            continue;
                        byte[] buff = new byte[m_msgSize];
                        in.read(buff, 0, m_msgSize);
                        String value = new String(Arrays.copyOfRange(buff, 0, buff.length), encoding);
                        if(mMessageListener != null) mMessageListener.messageReceived(value);
                        m_msgSize = -1;
                    }
                }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
                if(mMessageListener != null) mMessageListener.onDisconnect();
            }
        } catch (Exception e) {
            if(mMessageListener != null) mMessageListener.onError(e);
            Log.e("TCP", "C: Error", e);
        }
    }

    @Override
    public void stop(){
        mRun = false;
    }

    @Override
    public void send(@NonNull String str){
        if (out != null) {
            byte[] len = ByteBuffer.allocate(4).putInt(str.length()).array();
            byte[] data = str.getBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            try {
                outputStream.write(len);
                outputStream.write(data);
                out.write(outputStream.toByteArray());
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
                mRun = false;
            }
        }
    }

    @Override
    public void setCommLinkListener(ICommLinkListener iCommLinkListener) {
        mMessageListener = iCommLinkListener;
    }

    @Override
    public boolean isConnected() {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) return true;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnected());
        return isConnected;
    }

    @Override
    public void setConnectionTimeout(int msecs) {

    }

    @Override
    public void setRecvTimeoutMsecs(int msecs) {

    }

}
