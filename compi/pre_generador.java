
package compi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author ASUS
 */
public class pre_generador {
    
    genera_cod ob = new genera_cod();
    
    public void generar(){
    extraccion_datos();
    ob.empezar();
    }
         public String expresiones(){
     String token_comillas = "(\"[^\"]*\")"; //expresion para identificar lo que hay dentro de comillas
            String alfa = "^([A-Za-z0-9\\s\\(\\)\\{\\}\\*\\+\\-/\\._,:=<>!;]|"+token_comillas+")*$"; //mi alfabeto con la expresion regular de las comillas
               String token_cad = "[A-Za-z][A-Za-z0-9_]*";
               String token_llaves= "[\\(\\)\\{\\};]";
               String token_op = "[\\*\\+\\-/]";
               String token_comparador = "(!=|==|<|>|>=|<=)";
               String cte = "(([0-9]+\\.)?[0-9]+)";
               
        String tokens = "(print$|input$|if$|otherwise$|for$|in$|range$|"
                     + "while$|and$|or$|int$|float$|String$|"+token_comillas
                +"|"+token_llaves+"|"+token_cad+"|"+token_op+"|"+token_comparador+"|"+cte+"|=|,)"; //mis tokens
       return tokens;            
    
    }
      public void extraccion_datos(){
     String ruta = "src\\compi"; //ruta del archivo de texto
            String nombre = "\\mi_lenguaje.txt"; //nombre del archivo de texto
            File archivo = new File(ruta+nombre);  //instanciacion de la clase File 
            
             try { 
           // archivo.createNewFile(); //creacion del archivo de texto
             if (archivo.exists()) {  //si el archivo exixte..
                FileReader fr = new FileReader(archivo);   //instanciacion de la clase FileReader
                BufferedReader br = new BufferedReader(fr);  //instanciacion de la clase BufferedReader
                String leer = "";   //variable auxiliar para leer los renglones del archivo de texto
                try {
                    while(leer != null){  //mientras leer no sea nulo..
                       
                       leer = br.readLine();  //pasar lo que haya en una linea del archivo de texto a la variable leer
                        if (leer!=null) { 
                            System.out.println(leer);
                             ob.encontrar_tokens(expresiones(), leer); 
                        }
                    }  
                } catch (IOException ex) { //capturamos el error si es que lo hay
                    System.out.println(ex);
                }
               br.close(); //terminamos el archivo de texto
              
            }else{
                System.err.println("El archivo no existe");
                
            }
        } catch (IOException ex) { //capturamos el error si es que lo hay
            System.out.println(ex);
        }
        
    }
}
