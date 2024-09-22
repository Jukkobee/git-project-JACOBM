import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Git
{
    public static void main (String [] args)
    {
        initializesGitRepo();
        //should get already exists
        initializesGitRepo();

        checkForAndDelete(gitDirectory);

        initializesGitRepo();

        Git git = new Git();
        try
        {
            git.putFileToObjectsFolder();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            git.putFileToIndex(hash, "plainTextFile.txt");
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    private static File gitDirectory = new File("git");
    private static String hash;
    public static void initializesGitRepo ()
    {
        //create gitDirectory
        
        if (!gitDirectory.exists()) 
        {
            gitDirectory.mkdir();
        }
        else
        {
            System.out.println ("Git Repository already exists");
        }
        //create objects inside gitDirectory
        File objects = new File("git/objects");
        if (!objects.exists())
        {
            objects.mkdir();
        }
        else
        {
            System.out.println ("Git Repository already exists");
        }
        //creating index file
        File index = new File("git/index");
        if (!index.exists())
        {
            try 
            {
                index.createNewFile();
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println ("Git Repository already exists");
        }
    }
    
    private static void checkForAndDelete(File file)
    {
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                checkForAndDelete(f);
                f.delete();
            }
        }
    }
    
    public static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void putFileToObjectsFolder()throws IOException
    {
        File regularFile = new File ("plainTextFile.txt");
        try (FileWriter writer2 = new FileWriter (regularFile)){
            writer2.write("top secret data");
        }
        //reads the contents of the file into the byte array and then  we can generate the hash by using our encryptString method
        String fileContent = new String (Files.readAllBytes(regularFile.toPath()));
        hash = encryptThisString(fileContent);
        File objects = new File ("git/objects");
        if (!objects.exists())
        {
            objects.mkdir();
        }
        File objectFile = new File ("git/objects/" + hash);
        try (FileWriter writer = new FileWriter (objectFile))
        {
            writer.write(fileContent);

        }
    }
    public void putFileToIndex(String hash, String filename)throws IOException
    {
        File index = new File ("git/index");
        //this append mode is used so that we can add to an existing file without altering the og data
        try (FileWriter writer = new FileWriter (index, true))
        {
            writer.write(hash + " " + filename + "\n");
        }
        
    }
}  


