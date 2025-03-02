package utilities;

import com.jcraft.jsch.*;
import org.openqa.selenium.By;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class LinuxUtil {

    public Session linuxSession(String hostName, Integer portNumber) {
        JSch linux = new JSch();
        Session session;
        String userName = System.getProperty("user.name");
        String privateKeyPath = "Users/" + userName + "/.ssh/id_rsa";
        try {
            linux.addIdentity(privateKeyPath);
            session = linux.getSession(userName, hostName, portNumber);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

        } catch (JSchException e) {
            throw new RuntimeException("Failed to create JSch Session ocject", e);
        }
        return session;
    }


    public Session linuxSession(String hostName, Integer portNumber, String userName, String password) {
        JSch linux = new JSch();
        Session session;

        try {
            session = linux.getSession(userName, hostName, portNumber);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

        } catch (JSchException e) {
            throw new RuntimeException("Failed to create JSch Session ocject", e);
        }
        return session;
    }

    public Channel linuxExecChannel(Session session) {
        String command = "pwd";
        Channel channel;

        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setPty(false);
            channel.connect();
        } catch (JSchException e) {
            throw new RuntimeException("Error during command execution " + command);
        }
        return channel;
    }

    public Channel linuxShellChannel(Session session, ArrayList<String> commands) {
        Channel channel = null;
        String output = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            channel = session.openChannel("shell");
            channel.setOutputStream(out);
            PrintStream shellStream = new PrintStream(channel.getOutputStream());
            channel.connect(6000);
            InputStream in = channel.getInputStream();
            for (String command : commands) {
                shellStream.println(command);
                shellStream.flush();
//                System.out.println(getChannelOutput(channel, in));
//                Thread.sleep(500);
            }
            shellStream.close();
            Thread.sleep(2000);
        } catch (JSchException js) {
            System.out.println("Error while opening Channel: " + js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }


    public ChannelSftp linuxSftpChannel(Session session, String path, String destination) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.put(path, destination);
            Thread.sleep(2000);
        } catch (JSchException | SftpException js) {
            throw new RuntimeException("Error while opening the channel: " + js.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return channelSftp;
    }

    public void linuxSftpChannel(ChannelSftp channelSftp, String localpath, String destination, String sftpType) {
        try {
            if (channelSftp.isConnected()) {
                if (sftpType.equalsIgnoreCase("put"))
                    channelSftp.put(localpath, destination);
                else if (sftpType.equalsIgnoreCase("get"))
                    channelSftp.get(destination, localpath);
                else
                    throw new RuntimeException(new Exception("Invalid type of sftp"));
            }
            Thread.sleep(2000);
        } catch (SftpException sf) {
            throw new RuntimeException("Error while creating sftpchannel: " + sf.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private String getChannelOutput(Channel channel, InputStream in) throws IOException {

        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();

        String line = "";
        while (true) {
            while (in.available() > 0) {
                int i = in.read(buffer, 0, 1024);
                if (i < 0)
                    break;

                strBuilder.append(new String(buffer, 0, i));
                System.out.println(line);
            }

            if (line.contains(""))
                break;
            if (channel.isClosed())
                break;
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }

        return strBuilder.toString();
    }

    public void disconnectLinux(Session session, Channel channel, ChannelSftp channelSftp) {
        if (channelSftp.isConnected())
            channelSftp.disconnect();

        if (channel.isConnected())
            channel.disconnect();

        if (session.isConnected())
            session.disconnect();
    }

    public void  disconnectChannel(Channel channel){
        if(channel.isConnected())
            channel.disconnect();
    }

    public void  disconnectSftp(ChannelSftp channel){
        if(channel.isConnected())
            channel.disconnect();
    }

    public void  disconnectChannel(Session session){
        if(session.isConnected())
            session.disconnect();
    }

}
