import java.net.*;
import java.io.*;
import java.net.*;
import java.util.*;

/*Sources :
https://www.java67.com/2019/05/how-to-convert-hostname-to-ip-address-java-example.html
 */

public class Client
{
    private Socket socket = null;
    private InetAddress inetName = null;
    private String hostIp = null;
    private DataInputStream in = null;
    private DataInputStream socketIn = null;
    private DataOutputStream out = null;
    public Client(String hostName, int port)
    {
        try
        {
            inetName = InetAddress.getByName(hostName);
        }
        catch(UnknownHostException e)
        {
            System.out.println(e);
        }
        hostIp = inetName.getHostAddress();
        try
        {
            socket = new Socket(hostIp, port);
            System.out.println(hostIp);
            System.out.println("Connecté");

            in = new DataInputStream(System.in);
            socketIn = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";

            while(!line.equals("Fini"))
            {
                try
                {
                    line = in.readLine();
                    String command = CheckCommand(line);

                    if (command.contains("GET"))
                    {
                        out.writeUTF(command);
                    }

                    if (command.contains("HEAD"))
                    {
                        out.writeUTF(command);
                    }

                    if (command.contains("PUT"))
                    {
                        out.writeUTF(command);
                    }

                    if (command.contains("DELETE"))
                    {
                        out.writeUTF(command);
                    }
                }
                catch(IOException e)
                {
                    System.out.println(e);
                }

                try
                {
                    String servResp = "";
                    servResp = socketIn.readUTF();
                    if(!servResp.equals(""))
                    {
                        System.out.println(servResp);
                    }
                }
                catch(IOException e)
                {
                    System.out.println(e);
                }
            }

            try
            {
                in.close();
                out.close();
                socket.close();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public String CheckCommand(String line)
    {
        String command = line;
        return command;
    }
}
