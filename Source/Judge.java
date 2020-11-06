import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.TreeSet;
public class Judge
{    
    static class Verdict
    {
        public String verdict;
        public String comment;
        public Verdict(String verdict, String comment)
        {
            this.verdict = verdict;
            this.comment = comment;
        }
    };
    static class PartialVerdict extends Verdict
    {
        public PartialVerdict(double points, String comment)
        {
            super("Accepted",points+" points; "+comment);
        }
    };
    static class NotAnIntException extends Exception
    {
        NotAnIntException(String s)
        {
            super(s);
        }
    };
    static class WrongFormatException extends Exception
    {
        WrongFormatException(String s)
        {
            super(s);
        }
    };

    static String INeedAName(int n)
    {
        String name = "";
        for(int i = 0; i < 3; i++, n/=10)
            name += (char)((int)'A'+(n%10));
        return name;
    }
    static int tryParse(String s)throws NotAnIntException
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(Exception e)
        {
            throw new NotAnIntException(s);
        }
    }
    static String join(char[] ar)
    {
        StringBuilder s = new StringBuilder();
        for(char c : ar)
            s.append(c);
        return s.toString();
    }
    static int total = 0;
    static void miss(Player p)throws IOException
    {
        p.tell("MISS");
        total++;
    }
    static Verdict interactor(Player p)
    {
        total = 0;
        ArrayList<String> words = new ArrayList<String>();
        try
        {
            Scanner sc = new Scanner(new File("word_bank.txt"));
            while(sc.hasNextLine())
                words.add(sc.nextLine());
        }
        catch(Exception e)
        {
            return new Verdict("Failed","word_bank.txt is missing or contains incorrect data");
        }

        long startStartTime = System.currentTimeMillis();
        int old_total = 0;
        int T = words.size();
        try
        {
            p.tell(T+"");
            for(int t = 0; t < T; t++)
            {
                long startTime = System.currentTimeMillis();

                String w = words.get(t);
                int n = w.length();
                char[] s = new char[n];
                for(int i = 0; i < n; i++)
                    s[i] = '*';

                boolean[] done = new boolean[26];
                for(int i = 0; i < 26; i++)
                    done[i] = false;

                p.tell(join(s));
                while(true)
                {
                    String[] query = p.askLine().split("\\s+");
                    if(query.length!=2)
                        return new Verdict("Wrong Answer", "An invalid query was made.");

                    if(query[0].equals("ASK"))
                    {
                        if(query[1].length() != 1)
                            return new Verdict("Wrong Answer","You may only ASK one letter at a time");
                        char c = query[1].charAt(0);
                        if(!('a' <= c && c <= 'z'))
                            return new Verdict("Wrong Answer", "You may only ASK a lowercase letter.");

                        boolean miss = true;
                        if(!done[c-'a'])
                        {
                            done[c-'a'] = true;
                            for(int i = 0; i < n; i++)
                                if(w.charAt(i)==c)
                                {
                                    miss = false;
                                    s[i] = c;
                                }
                        }

                        if(miss)
                            miss(p);
                        else
                            p.tell(join(s));
                    }
                    else if(query[0].equals("ANSWER"))
                    {
                        if(w.equals(query[1]))
                        {
                            p.tell("GOOD");
                            break;
                        }
                        else
                            miss(p);
                    }
                    else
                        return new Verdict("Wrong Answer", "Only ASK and ANSWER are allowed.");
                }

                long newTime = System.currentTimeMillis();
                System.out.println("Word "+(t+1)+" solved in "+(newTime-startTime)+"ms with "+(total-old_total)+" misses");

                old_total = total;
            }
        }
        catch(IOException e)
        {
            return new Verdict("Wrong Answer","There was an IOException: "+e.getMessage());
        }

        System.out.println("All "+(T)+" words solved in "+(System.currentTimeMillis()-startStartTime)/1000.0+"s");
        double x = (double)total/T;
        return new Verdict("Accepted","Average is "+(x)+" misses per word");
    }
    public static void main(String[] args) throws IOException
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the name of your executable: ");
        Player p = new Player(sc.nextLine());
        if(p.isAlive())
        {
            Verdict v = interactor(p);
            System.out.println("Verdict: " + v.verdict);
            System.out.println(v.comment);
        }
        else
            System.out.println("There was an error in reading your executable.");
    }
}