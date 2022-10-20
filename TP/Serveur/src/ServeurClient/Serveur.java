package ServeurClient;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.util.Date;
import java.util.Scanner;

//COMMANDES---------------------------------------------------------------------------------
//IMPORTANT : (nom du fichier) ne doit pas avoir de paranthèses mais (text) oui
//exemple :  PUT /Fichiers/newFichier.txt (ceciEstDuTexte) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n
//GET /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

//HEAD /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

//PUT /Fichiers/(nom du fichier) (text) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

//DELETE /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n
public class Serveur {
    private Socket socket = null;

    private ServerSocket server = null; //Permet d'ouvrir la connection, d'écouter si quelqu'un veut se connecter et
    // va accepter lorsqu'une demande de connexion est faite
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Serveur(int port){

        try{
            server = new ServerSocket(port);
            System.out.println("Waiting for connection");
            socket = server.accept();
            System.out.println("Connecté");

            in = new DataInputStream((socket.getInputStream()));
            out = new DataOutputStream((socket.getOutputStream()));

            String line = "";

            while(!line.equals("Fini")){
                try{
                    line = in.readUTF();
                    System.out.println(line);

                    if(line.contains("GET"))
                    {
                        CommandGET(line);
                    } else if (line.contains("HEAD"))
                    {
                        CommandHEAD(line);
                    } else if (line.contains("PUT"))
                    {
                        String text = GetTextPUT(line);
                        CommandPUT(line, text);

                    } else if (line.contains("DELETE"))
                    {
                        CommandDELETE(line);
                    }
                }
                catch(IOException i){
                    System.out.println(i);
                }
            }
            System.out.println("Connexion fermée");

            socket.close();
            in.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public String GetPath(String line)
    {
        String path = "";
        char c = ' ';
        boolean b = false;
        int cnt = 0;
        for(int i = 0; i < line.length(); i++)
        {
            c = line.charAt(i);
            if(c == '/' && cnt == 0)
            {
                b = true;
                cnt += 1;
                continue;
            } else if (c == ' ' && b)
            {
                b = false;
                break;
            }
            if(b)
            {
                path += c;
            }
        }
        return path;
    }

    public String GetTextPUT(String text)
    {
        String txt = "";
        char c = ' ';
        boolean b = false;
        int cnt = 0;
        for(int i = 0; i < text.length(); i++)
        {
            c = text.charAt(i);
            if(c == '(' && cnt == 0)
            {
                b = true;
                cnt += 1;
                continue;
            } else if (c == ')' && b)
            {
                b = false;
                break;
            }
            if(b)
            {
                txt += c;
            }
        }
        System.out.println(txt);
        return txt;
    }
    public void CommandGET(String line) {
        String path = GetPath(line);
        System.out.println(path);
        File f = new File(path);
        if (f.exists()) {
            try {
                String info ="";

                Scanner scanFile = new Scanner(f);
                while (scanFile.hasNextLine())
                {
                    info += scanFile.nextLine();
                }
                scanFile.close();
                out.writeUTF("Name : " + f.getName() + "\r\n" + "Size : " + f.length() + "\r\n" + "Date: " + new Date() + "\r\n\r\n" + info + "\r\n");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void CommandHEAD(String line)
    {
        String path = GetPath(line);
        System.out.println(path);
        File f = new File(path);
        if (f.exists())
        {
            try {
                out.writeUTF("Name : " + f.getName() + "\r\n" + "Size : " + f.length() + "\r\n" + "Date: " + new Date() + "\r\n\r\n");
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
    }

    public void CommandPUT(String line, String text)
    {
        try
        {
            String path = GetPath(line);
            FileWriter wFile = new FileWriter(path);
            wFile.write(text);
            wFile.close();
            CommandGET(line);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void CommandDELETE(String line)
    {
        {
            String path = GetPath(line);
            File f = new File(path);
            f.delete();
        }
    }//COMMANDES---------------------------------------------------------------------------------
    //IMPORTANT : (nom du fichier) ne doit pas avoir de paranthèses mais (text) oui
    //exemple :  PUT /Fichiers/newFichier.txt (ceciEstDuTexte) HTTP/1.1\r\nHost:192.168.0.193\r\n\r\n
    //GET /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

    //HEAD /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

    //PUT /Fichiers/(nom du fichier) (text) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n

    //DELETE /Fichiers/(nom du fichier) HTTP/1.1\r\nHost:206.41.87.6\r\n\r\n
}