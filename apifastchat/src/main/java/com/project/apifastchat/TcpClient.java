package com.project.apifastchat;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient implements ICommLink{

    private String serverMessage;
    public static final String SERVERIP = "10.35.90.162"; //your computer IP address
    public static final int SERVERPORT = 1977;
    private ICommLinkListener mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

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

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {
            mMessageListener.onError(e);
            Log.e("TCP", "C: Error", e);
        }
    }

    @Override
    public void stop() {
        mRun = false;
    }

    @Override
    public void send(@NonNull String str) throws IOException {
        if (out != null && !out.checkError()) {
            out.println(str);
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
