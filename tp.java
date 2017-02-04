import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import org.kohsuke.args4j.*;

class Encryptor {
  public static String encrypt(String input, int key)
  {
    char[] chars = input.toCharArray();

    StringBuffer hex = new StringBuffer();

    for (int i = 0; i < chars.length; i++) {
        int value = (int) chars[i];
        int newValue = value + key;
        hex.append(Integer.toHexString(newValue));
    }

    return hex.toString();
  }
}

class Decryptor {
  public static String decrypt(String input, int key) 
  {
     StringBuffer sb = new StringBuffer();
     StringBuffer temp = new StringBuffer();

     for (int i = 0; i < input.length() - 1; i += 2) {
         String output = input.substring(i, (i + 2));
         int decimal = Integer.parseInt(output, 16);

          decimal = decimal - key;
          sb.append((char) decimal);

          temp.append(decimal);
      }

      return sb.toString();
  }
}

public class TP {
   @Option(name="-key", usage="the key, default is H2HYESBANK")
   private String key = "H2HYESBANK";

   @Option(name="-data", usage="the data to encrypt")
   private String data = "Hello World !!!!";

   public static void main(String[] argv) {
      new TP().doMain(argv);
   }

   public void doMain(String[] argv) {
      CmdLineParser parser = new CmdLineParser(this);

      try {
          // parse the arguments.
          parser.parseArgument(argv);

          System.out.println("Data :" + data);
          System.out.println("Key :" + key);

          String md5OfKey = md5(key);
          int intKey = (int) md5OfKey.charAt(md5OfKey.length() - 3);
          System.out.println("MD5 of Key :" + md5OfKey);
          System.out.println("ASCII value of 3rd last character to be used for enc/dec :" + Integer.toString(intKey));


          String encrypted = Encryptor.encrypt(data, intKey);
          System.out.println("Encrypted :" + encrypted);
          System.out.println("Decrypted :" + Decryptor.decrypt(encrypted, intKey));

      } catch( CmdLineException e ) {
          parser.printUsage(System.err);
          System.err.println();
          return;
      }

   }

   public String md5(String key) {
     try {
        byte[] keyBytes = key.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdbytes = md.digest(keyBytes);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
     }
     catch (Exception e) {
        return null;
     }
   }
}

