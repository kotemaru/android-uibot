package org.kotemaru.android.uibot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "localhost";
        int port = 9999;
        String cmd = null;
        String file = null;
        for (int i=0; i<args.length;i++) {
            String arg = args[i];
            if ("-h".equals(arg)) {
                host = args[++i];
            } else if ("-p".equals(arg)) {
                port = Integer.valueOf(args[++i]);
            } else if ("-c".equals(arg)) {
                cmd = args[++i];
            } else if ("-f".equals(arg)) {
                file = args[++i];
            } else {
                System.out.println("usage: uibot-client [-h <host>][-p <port>][-c <cmd>][-f <file>]");
                return;
            }
        }

        final Socket sock = new Socket(host, port);
        InputStream source = System.in;
        if (cmd != null) {
            source = new ByteArrayInputStream(cmd.getBytes("utf-8"));
        } else if (file != null) {
            source = new FileInputStream(file);
        }


        //System.err.println("start");

        Thread outThread = new Thread() {
            @Override
            public void run() {
                try {
                    transfer(sock.getInputStream(), System.out, true, false);
                    //System.err.println("close-out");
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                } finally {
                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        };
        outThread.start();
        transfer(source, sock.getOutputStream(), true, false);
        sock.shutdownOutput();
        //System.err.println("close-in");
        outThread.join();
    }

    private static void transfer(InputStream in, OutputStream out, boolean inClose, boolean outClose) throws IOException {
        try {
            byte[] buff = new byte[2048];
            int n;
            while ((n = in.read(buff)) >= 0) {
                //System.err.println("read="+n);
                out.write(buff, 0, n);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (outClose) out.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            if (inClose) in.close();
        }
    }
}
