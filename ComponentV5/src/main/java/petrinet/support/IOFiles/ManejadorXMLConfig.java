 package petrinet.support.IOFiles;
 
 import java.io.BufferedReader;
 import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.IOException;
 
 public class ManejadorXMLConfig
 {
   public static String obtenerValorConfig(String path, String etiqueta)
     throws FileNotFoundException, IOException, Exception
   {
     String text = null;
     
     BufferedReader bf = new BufferedReader(new FileReader(path));
     while ((text = bf.readLine()) != null) {
       if ((text.startsWith("<" + etiqueta + ">")) && (text.endsWith("</" + etiqueta + ">")))
       {
         text = text.replaceAll("<" + etiqueta + ">", "");
         text = text.replaceAll("</" + etiqueta + ">", "");
         bf.close();
         
         return text;
       }
     }
     bf.close();
     throw new Exception();
   }
 }

