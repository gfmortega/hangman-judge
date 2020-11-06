import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Player 
{
    ProcessBuilder playerBuilder;
    Process player;
    BufferedWriter talkTo;
    Scanner listenTo;
    FileWriter log;
    boolean alive = false;
    static String[] command(String filename) throws IOException
    {
        int dotCount = filename.length()-filename.replace(".", "").length();
        if(dotCount==0)
            throw new IOException("No file extension detected.");
        if(dotCount>1)
            throw new IOException("Please use a file name with only one .");

        String[] recognizedOS = {"Windows", "Linux", "Mac"};
        String OS = "?";
        for(String check : recognizedOS)
            if(System.getProperty("os.name").contains(check))
                OS = check;

        ArrayList<String> commands = new ArrayList<String>();

        if(OS.equals("Windows"))
        {
            commands.add("cmd");
            commands.add("/c");
        }

        String fileExtension = filename.split("\\.")[1];
        String[] extensions = {"py","exe","out","class"};
        boolean[] include = {true, true, true, false};
        String[] use = {"python","","","java"};
        
        if(OS.equals("Linux") || OS.equals("Mac"))
            use[0] = "python3";

        for(int i = 0; i < extensions.length; i++)
            if(fileExtension.equals(extensions[i]))
            {
                if(!use[i].equals(""))
                  commands.add(use[i]);
                
                if(OS.equals("Linux") && extensions[i].equals("out"))
                    filename = "./" + filename;
                
                if(include[i])
                    commands.add(filename);
                else
                    commands.add(filename.split("\\.")[0]);

                return commands.toArray(new String[commands.size()]);
            }

        throw new IOException("file extension was not recognized");
    }
    public Player(String filename)throws IOException
    {
        alive = false;
        playerBuilder = new ProcessBuilder(command(filename));
        alive = true;

        player = playerBuilder.start();
        talkTo = new BufferedWriter(new OutputStreamWriter(player.getOutputStream()));
        listenTo = new Scanner(new InputStreamReader(player.getInputStream()));

        playerBuilder.redirectError(new File(filename+"_error.txt"));

        log = new FileWriter("log.txt");
        log.write("JUDGE           PLAYER\n");

    }
    public boolean isAlive()
    {
        return alive;
    }
    public String askLine() throws IOException
    {
        while(!listenTo.hasNextLine());
        String s = listenTo.nextLine();
        log.write("                "+s+"\n");
        log.flush();
        return s;
    }
    public void tell(String message)throws IOException
    {
        log.write(message+"\n");
        log.flush();
        talkTo.write(message+"\n");
        talkTo.flush();
    }
    public void tell(int n)throws IOException{ tell(n+""); }
    public void kill()
    {
        player.destroy();
        alive = false;
    }
};
