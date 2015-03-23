 package petrinet.support.IOFiles;
 
 import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.commons.io.FilenameUtils;
 
 public class ManejadorTXT
 {
   public static String[] readFile(String _path)
     throws FileNotFoundException, IOException
   {
     String text = null;
     List<String> lista = new ArrayList<String>();
     BufferedReader bf = new BufferedReader(new FileReader(FilenameUtils.separatorsToSystem(_path)));
     while ((text = bf.readLine()) != null) {
       lista.add(text);
     }
     String[] lineas = new String[lista.size()];
     for (int i = 0; i < lista.size(); i++) {
       lineas[i] = ((String)lista.get(i));
     }
     bf.close();
     
     return lineas;
   }
   
   public static String[][] splitLines(String[] _lines)
   {
     String[] temp = null;
     
 
     List<String[]> filas = new ArrayList<String[]>();
     for (int i = 0; i < _lines.length; i++)
     {
       temp = _lines[i].split(" ");
       
       List<String> lista = new ArrayList<String>();
       for (int j = 0; j < temp.length; j++) {
         if (!temp[j].equals("")) {
           lista.add(temp[j]);
         }
       }
       String[] elementos = new String[lista.size()];
       for (int k = 0; k < elementos.length; k++) {
         elementos[k] = ((String)lista.get(k));
       }
       filas.add(elementos);
     }
     String[][] retorno = new String[filas.size()][((String[])filas.get(0)).length];
     for (int i = 0; i < retorno.length; i++) {
       for (int j = 0; j < retorno[0].length; j++) {
         retorno[i][j] = ((String[])filas.get(i))[j];
       }
     }
     return retorno;
   }
   
   public static int[][] toInt(String[][] _matrix)
     throws NumberFormatException
   {
     int[][] retorno = new int[_matrix.length][_matrix[0].length];
     for (int i = 0; i < retorno.length; i++) {
       for (int j = 0; j < retorno[0].length; j++) {
         retorno[i][j] = Integer.parseInt(_matrix[i][j]);
       }
     }
     return retorno;
   }
   
   public static int[][] getMatrix(String _path)
   {
     String[] texto = null;
     try
     {
       texto = readFile(_path);
     }
     catch (FileNotFoundException ex)
     {
       Logger.getLogger(ManejadorTXT.class.getName()).log(Level.SEVERE, null, ex);
     }
     catch (IOException ex)
     {
       Logger.getLogger(ManejadorTXT.class.getName()).log(Level.SEVERE, null, ex);
     }
     String[][] elementos = splitLines(texto);
     int[][] matrix = toInt(elementos);
     
     return matrix;
   }
   
   public static Object[][] getObjectInt(int[][] matriz)
   {
     Object[][] objetos = new Object[matriz.length][matriz[0].length];
     for (int i = 0; i < matriz.length; i++) {
       for (int j = 0; j < matriz[0].length; j++) {
         objetos[i][j] = new Integer(matriz[i][j]);
       }
     }
     return objetos;
   }
   
   public static int[][] getIntObjectBin(Object[][] matriz)
   {
     int[][] retorno = new int[matriz.length][matriz[0].length];
     for (int i = 0; i < matriz.length; i++) {
       for (int j = 0; j < matriz[0].length; j++) {
         if (((Integer)matriz[i][j]).intValue() > 0) {
           retorno[i][j] = 1;
         } else {
           retorno[i][j] = 0;
         }
       }
     }
     return retorno;
   }
   
   public static int[][] getIntValues(JTable tabla)
   {
     TableModel tableModel = tabla.getModel();
     int columnas = tableModel.getColumnCount();
     int filas = tableModel.getRowCount();
     int[][] retorno = new int[filas][columnas];
     for (int i = 0; i < filas; i++) {
       for (int j = 0; j < columnas; j++) {
         if ((tableModel.getValueAt(i, j) instanceof String)) {
           retorno[i][j] = Integer.parseInt((String)tableModel.getValueAt(i, j));
         } else if ((tableModel.getValueAt(i, j) instanceof Integer)) {
           retorno[i][j] = ((Integer)tableModel.getValueAt(i, j)).intValue();
         } else {
           retorno[i][j] = 0;
         }
       }
     }
     return retorno;
   }
 }

