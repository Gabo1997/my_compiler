
package compi;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ASUS
 */
public class sintax_sema {
        ArrayList<String> llave_abi = new ArrayList<String>();
    ArrayList<String> errores_sint = new ArrayList<String>();
    ArrayList<String> ERR_SIN = new ArrayList<String>();
    ArrayList<String> token_no_val = new ArrayList<String>();
    ArrayList<String> declaraciones = new ArrayList<String>();
    ArrayList<String> id_no_decl = new ArrayList<String>();
    ArrayList<String> id_pr = new ArrayList<String>();
    ArrayList<String> tipos_no_coi = new ArrayList<String>();
    ArrayList<String> ID_duplicado = new ArrayList<String>();
    ArrayList<String> op_incorrecto = new ArrayList<String>();
     ArrayList<String> variables = new ArrayList<String>();
     ArrayList<String> ERR_SEMA = new ArrayList<String>();
      ArrayList<String> pila_if = new ArrayList<String>();
  
      int conta_llave_ab;   String pivote = "";
    int lineas = 1;           int cont;
    int columnas = 0;
    nodo aux = null;
    nodo raiz;
    boolean ban_llave = false;
   
      public String token(String expre, String prueba, int linea) { //metodo para encontrar palabras reservadas
        String palabras = ""; 
        Pattern patron;   //indicamos la ER
        Matcher mcher;   //compila ER vs Cadena
        
        patron = Pattern.compile(expre); //se compila la expresion reg
        mcher = patron.matcher(prueba); //evaluamos la cadena
          if (linea>1) {
              add_nodo("salto");
          }
         columnas=0;
        while (mcher.find()) { //mientras encuentre..
          
            add_nodo(mcher.group());
            palabras = palabras + mcher.group() + ", "; //se suman las palabras reservadas para despues mostrarlas
        }
        Matcher mcher2;
         patron = Pattern.compile(expre); //se compila la expresion reg
        mcher2 = patron.matcher(prueba); //evaluamos la cadena
           if (mcher2.find()) {
               String err =  mcher2.replaceAll("");
               err = err.trim();
               if (!err.equals("")) {  errores_sint.add(linea+","+err+","+" error lexico");
                   ERR_SEMA.add("E002"+","+linea+","+err);
                   token_no_val.add("token incorrecto "+err+" linea "+linea);
               }
          }
        if (palabras.equals("")) { //si el contador es igual que la longitud de subcadenas...
            return "no se encontro ningun token";
        } else {
            return "se encontraron los tokens: " + palabras;
        } //si no es igual entonces quiere decir que si se encontro al menos un token

    }
      public void incorrectos(String se_espera) throws Exception{
      try{    
      System.out.println("error de sintaxis en linea "+lineas+", columna "+aux.get_nodo_sig().get_indice());
      if (aux.get_nodo_sig().get_indice()==1){se_espera=aux.get_nodo_sig().get_dato();}
       errores_sint.add(lineas+","+aux.get_nodo_sig().get_indice()+","+se_espera); incrementa();
      }catch(NullPointerException e){errores_sint.add(lineas+","+aux.get_indice()+","+se_espera);  }
      }

    public void add_nodo(String dato) {
        
        if (raiz == null) {
            raiz = new nodo(dato);
            columnas++;
        } else {
            aux = raiz;
            while (aux.get_nodo_sig() != null) {
                aux = aux.get_nodo_sig();
            }
           
            aux.set_nodo_sig(new nodo(dato),columnas++);
           
        }
    }
     public void accion(){
     try{
         aux = raiz;
    
     arbol(aux.get_dato());
         
     int ant = 0;
         System.out.println("----------ERRORES------------");
         for (int i = 0; i < errores_sint.size(); i++) {
             String filas_col [] = errores_sint.get(i).split(",");
             
             if (!filas_col[0].equals(String.valueOf(ant))) {
                try{
                 ant  = Integer.parseInt(filas_col[0]);
                    
                 ERR_SEMA.add("E001"+","+filas_col[0]+","+filas_col[1]);
                 ERR_SIN.add(filas_col[0]+","+filas_col[1]+","+filas_col[2]); //DEFINITIVO ARRAY DE ERRORES SINTACTICOS
                 System.out.println("error de sintaxis en linea "+filas_col[0]+" columna "+filas_col[1]+", "+filas_col[2]);
                } catch(NumberFormatException e){ }
                  catch(ArrayIndexOutOfBoundsException e){ ERR_SEMA.add("E001"+","+filas_col[0]+","+filas_col[1]);
                      ERR_SIN.add(filas_col[0]+","+filas_col[1]+","+"salto_linea");
                      System.out.println("error de sintaxis en linea "+filas_col[0]+" columna "+filas_col[1]);}
             }
            
         }
         if (!llave_abi.isEmpty()) {
            
             for (int i = 0; i < llave_abi.size(); i++) {
                 String arreglo [] = llave_abi.get(i).split(",");
                 ERR_SEMA.add("E006"+","+arreglo[1]+","+" ");
                 System.out.println("falto cerrar llave "+arreglo[0]+" en linea "+arreglo[1]);
             }
         }
     }catch(NullPointerException e){System.out.println("no hay tokens");}
     }
    public void regresar() {
        aux = aux.get_nodo_sig();
        if (aux.get_nodo_sig() != null) {
            aux = aux.get_nodo_sig();
            
            arbol(aux.get_dato());
        }
    }           
               boolean ban,bandera;
                public void arbol(String dato){
                    ban = true;
                    String pa_res = "^(print$|input$|if$|else$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|salto$)";
                    if (match(dato,pa_res)==false && match(dato,"(^[A-Za-z][A-Za-z0-9_]*$)")==true) { cont=1;
                       encontrar_id(aux.get_dato(),aux.get_indice()); 
                       if(getTipo(aux.get_dato()).equals("")){ban = false;}
                        dato = "asig";
                        System.out.println("asignacion");
                    }
                    switch(dato){
                    
                        case "if":  bandera = true;
                          try{
                           parentesis_ab();
                           ID_CTEEF();
                           comp();
                           ID_CTEEF();
                           and_or();
                           parentesis_cer();
                           llave_ab(); 
                           pila_if.add("if"); ban_llave=false;
                         // llave_cer();
                          }catch(Exception e){ bandera=false;
                              System.out.println("salto de linea"); 
                             regresar(); //Recursividad del metodo arbol
                                      }
                            break;
                        case "while":
                             try{
                           parentesis_ab();
                           ID_CTEEF();
                           comp();
                           ID_CTEEF();
                           and_or();
                           parentesis_cer();
                           llave_ab();
                           pila_if.add("while"); ban_llave=false;
                        //   llave_cer();
                          }catch(Exception e){
                              System.out.println("salto de linea"); 
                             regresar(); //Recursividad del metodo arbol
                                      }
                            break;
                        case "for":
                              try{ pivote="int"; cont = 1;
                                      ciclo_for();
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "input":
                             try{
                           parentesis_ab();
                           id();
                           parentesis_cer();
                           p_coma();
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "print":
                           try{
                           parentesis_ab();
                           concat();
                           parentesis_cer();
                           p_coma();
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "int": 
                             try{ pivote="int"; cont = 1;
                            id_decl();
                            igual();
                            ID_CTEEF();
                            p_coma();
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "float":
                             try{  pivote="float"; cont = 1;
                            id_decl();
                            igual();
                            ID_CTEEF();
                            p_coma(); 
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "String":
                            try{       pivote = "String"; cont = 1;
                             id_decl();
                             igual();
                             concat();
                            p_coma();
                            }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                        case "asig":
                            try{
                            igual();
                            OPDO();
                            p_coma();
                              }catch(Exception e){System.out.println("salto de linea"); regresar();}
                            break;
                          
                        case "}":
                            try{
                            if (llave_abi.size() >= 1) {
                                
                                llave_abi.remove(llave_abi.size() - 1);                                
                                System.out.println("llaves ok ");                                
                                if (pila_if.get(pila_if.size()-1).equals("if")) {
                                    pila_if.remove(pila_if.size()-1);
                                    if (aux.get_nodo_sig() != null) {
                                         if (aux.get_nodo_sig().get_dato().equals("else")) {
                                             aux = aux.get_nodo_sig();
                                            llave_ab();
                                      
                                        }
                                    }
                                }
                            }
                             }catch(Exception e){}
                            break;
                        case "salto":
                            
                       lineas++;  System.out.println("linea "+lineas);
       
                            break;
                        default:  

                             System.out.println("error de sintaxis en linea "+lineas+" "+aux.get_dato());
                              errores_sint.add(lineas+","+aux.get_indice()+", "+aux.get_dato()); 
                            if (aux.get_nodo_sig()!=null) {
                             aux = aux.get_nodo_sig();
                                arbol(aux.get_dato());
                                }
                   
                    }
                     if (aux.get_nodo_sig() != null) { //si hay otra sentencia, repetir switch
            aux = aux.get_nodo_sig(); 
            arbol(aux.get_dato());
        }
                }
                public void parentesis_ab() throws Exception{
                    try {
                     cont=0;
                     if (aux.get_nodo_sig().get_dato().equals("(")) {
                         aux = aux.get_nodo_sig();
                         System.out.println("parentesis ab : "+aux.get_dato());
                                
                            }else{  //listade errores
                         
                         incorrectos("se esperaba un parentesis ab ("); System.out.println("se esperaba un parentesis ab (");
                     }
                }catch(NullPointerException e){errores_sint.add(lineas+","+aux.get_nodo_sig().get_indice()); } //nulo
                   
                }
    public void ID_CTEEF() throws Exception {     cont++;
        String id = "(^[A-Za-z][A-Za-z0-9_]*$)";
        String cte = "([0-9]+\\.)?[0-9]+";
        try {
           
            if (match(aux.get_nodo_sig().get_dato(), "^(print$|input$|if$|else$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|salto$)") == false ) { 
               
                if (match(aux.get_nodo_sig().get_dato(), id) == true) {  
                    System.out.println("es: " + aux.get_nodo_sig().get_dato());
                    aux = aux.get_nodo_sig(); 
                    encontrar_id(aux.get_dato(),aux.get_indice());
                    if (!pivote.equals("String")) {
                        op(); //posible operador aritmetico
                    }else{ 
                    tipos_no_coi.add(id+","+lineas+","+aux.get_indice());
                       ERR_SEMA.add("E005"+","+lineas+","+aux.get_indice());
                       op();
                    }
                    
                } else {
                     if (match(aux.get_nodo_sig().get_dato(), "-") == true) {
                    aux = aux.get_nodo_sig();
                }
                    if (match(aux.get_nodo_sig().get_dato(), cte) == true) { 
                    aux = aux.get_nodo_sig();
                    float_o_int_o_cad(aux.get_dato(),aux.get_indice());
                        op();  //posible operador aritmetico
                    }else {
                      incorrectos("Se esperaba una cte o un id ");
                        System.out.println("Se esperaba una cte o un id ");
                    }
            }
                
            }else { incorrectos("se esperaba una CTE O ID ");
                id_pr.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice());
            ERR_SEMA.add("E004"+","+lineas+","+aux.get_nodo_sig().get_indice());}
        }catch(NullPointerException e){incorrectos(""); }
    }
                
               public void parentesis_cer() throws Exception{
        try {

            if (aux.get_nodo_sig().get_dato().equals(")")) {
                aux = aux.get_nodo_sig();
                System.out.println("parentesis cer " + aux.get_dato());
            } else {  //listade errores
                incorrectos("se esperaba un parentesis cer )");
                System.out.println("se esperaba un parentesis cer ) ");
            }
        } catch (NullPointerException e) {
            incorrectos("");
        }

    }
                
                public void llave_ab() throws Exception{
        try {

            if (aux.get_nodo_sig().get_dato().equals("{")) {
                aux = aux.get_nodo_sig();
               llave_abi.add(""+conta_llave_ab+++","+lineas);
                System.out.println("llave abierta " + conta_llave_ab + " " + aux.get_dato());
                ban_llave = true;
            } else {  //lista de errores
                incorrectos("se esperaba una llave ab {");
                System.out.println("se esperaba una llave ab { ");
            }
        } catch (NullPointerException e) {   incorrectos("se esperaba { ");  }

    }
                
                public void comp()throws Exception{
                    try { 
                        
                if (match(aux.get_nodo_sig().get_dato(),"(!=|==|<|>|>=|<=)")==true) {
                    aux = aux.get_nodo_sig();
                     System.out.println("comparador: "+aux.get_dato());
             
                    }else{   incorrectos("se esperaba un comparador"); System.out.println("se esperaba un comparador "+aux.get_nodo_sig().get_dato());}
                }catch(NullPointerException e){incorrectos(""); }
                }
                public void op() throws Exception{
                    if (aux.get_nodo_sig()!=null) {
                       if (match(aux.get_nodo_sig().get_dato(),"[\\*\\+\\-\\/]")==true) {
                            aux = aux.get_nodo_sig();
                           ID_CTEEF();
                        }
                        
                    }
                }
                public void suma() throws Exception{
                if (aux.get_nodo_sig()!=null) {
                        if (match(aux.get_nodo_sig().get_dato(),"[\\+]")) {
                            aux = aux.get_nodo_sig();
                           OPDO();
                        }else if(match(aux.get_nodo_sig().get_dato(),"[\\*\\-/]")==true){
                        op_incorrecto.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice());
                        ERR_SEMA.add("E008"+","+lineas+","+aux.get_nodo_sig().get_indice());
                            System.out.println("error sema "+aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice());
                            aux = aux.get_nodo_sig();
                            OPDO();
                        }
                    }
                }
                public void and_or() throws Exception{
                    if (aux.get_nodo_sig()!=null) {
                        if (match(aux.get_nodo_sig().get_dato(),"and$|or$")) {
                            aux = aux.get_nodo_sig();
                            System.out.println(aux.get_dato());
                            ID_CTEEF();
                            comp();
                            ID_CTEEF();
                            and_or(); 
                        }
                    }
                }
                public void id() throws Exception{ cont++;
                try{
                    String id = "(^[A-Za-z][A-Za-z0-9_]*$)";
                    if (match(aux.get_nodo_sig().get_dato(), "^(print$|input$|if$|else$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|salto$)") == false ) {

                if (match(aux.get_nodo_sig().get_dato(), id) == true) {
                    System.out.println("es: " + aux.get_nodo_sig().get_dato());
                    aux = aux.get_nodo_sig();
                    encontrar_id(aux.get_dato(),aux.get_indice());
                }else {
                      incorrectos("Se esperaba un ID ");
                        System.out.println("Se esperaba un id ");
                    } 
                }else { incorrectos("se esperaba un ID "); System.out.println("cdsvf "+aux.get_nodo_sig().get_dato());
                        ERR_SEMA.add("E004"+","+lineas+","+aux.get_nodo_sig().get_indice());
                        id_pr.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice()); System.out.println("Se esperaba un id");   }
                }catch(NullPointerException e){incorrectos("");}
                 
                }
                public void id_decl() throws Exception{
                 try{
                    String id = "(^[A-Za-z][A-Za-z0-9_]*$)";
                    if (match(aux.get_nodo_sig().get_dato(), "^(print$|input$|if$|else$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|salto$)") == false ) {

                if (match(aux.get_nodo_sig().get_dato(), id) == true) {
                    id_repetido(aux.get_nodo_sig().get_dato(),aux.get_nodo_sig().get_indice(),aux.get_dato()); //si un id es duplicado
                    System.out.println("es: " + aux.get_nodo_sig().get_dato());
                    
                    aux = aux.get_nodo_sig();
                }else {
                      incorrectos("Se esperaba un un id ");
                        System.out.println("Se esperaba un id ");
                    } 
                }else { incorrectos("se esperaba un ID ");
                        ERR_SEMA.add("E004"+","+lineas+","+aux.get_nodo_sig().get_indice());
                        id_pr.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice()); System.out.println("Se esperaba un id");   }
                }catch(NullPointerException e){incorrectos("");}
                 
                }
               
                public void igual() throws Exception{  
                 try{
                        if (aux.get_nodo_sig().get_dato().equals("=")) { 
                           aux = aux.get_nodo_sig(); 
                             
                        }else{incorrectos("se esperaba un = ");}
                    }catch(NullPointerException e){incorrectos("");}
                }
          public void OPDO() throws Exception {          cont++;
               String id = "(^[A-Za-z][A-Za-z0-9_]*$)";
               String cte = "([0-9]+\\.)?[0-9]+";
                try {
                    if (match(aux.get_nodo_sig().get_dato(), "^(print$|input$|if$|else$|for$|in$|range$|"
                            + "while$|and$|or$|int$|float$|String$|salto$)") == false) {

                        if (match(aux.get_nodo_sig().get_dato(), id) == true) {  //id (String)
                            System.out.println("es: " + aux.get_nodo_sig().get_dato());
                              
                            aux = aux.get_nodo_sig();
                            encontrar_id(aux.get_dato(),aux.get_indice());
                            if (pivote.equals("String")) {
                                suma();
                            }else{  op_2();  }
                            
                        }else if(match(aux.get_nodo_sig().get_dato(),"(\"[^\"]*\")")==true) { //CTECAD
                              
                            aux = aux.get_nodo_sig();
                            float_o_int_o_cad(aux.get_dato(),aux.get_indice());
                            suma();
                        }else{ 
                            if (match(aux.get_nodo_sig().get_dato(), "-") == true) {
                        aux = aux.get_nodo_sig();
                    }
                    if (match(aux.get_nodo_sig().get_dato(), cte) == true) { 
                    aux = aux.get_nodo_sig();
                    float_o_int_o_cad(aux.get_dato(),aux.get_indice());
                        op_2();  //posible operador aritmetico
                    }else{incorrectos("Se esperaba una cte o un id ");
                            System.out.println("Se esperaba una cte o un id ");}

                    } 
                    }else { incorrectos("se esperaba una CTE O ID ");
                        ERR_SEMA.add("E004"+","+lineas+","+aux.get_nodo_sig().get_indice());
                        id_pr.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice());
                        System.out.println("Se esperaba una cteCAD o un id");
                    }
               
        } catch (NullPointerException e) {
            incorrectos("");
        }
    }
    public void op_2() throws Exception{
        if (aux.get_nodo_sig() != null) {
            if (match(aux.get_nodo_sig().get_dato(), "[\\*\\+\\-\\/]") == true) {
                aux = aux.get_nodo_sig();
                OPDO();
            }

        }
    }
    public void p_coma() throws Exception {
        try {

            if (aux.get_nodo_sig().get_dato().equals(";")) {
                aux = aux.get_nodo_sig();
                if (aux.get_nodo_sig() != null) {
                    aux = aux.get_nodo_sig();

                    arbol(aux.get_dato());
                }

            } else {
                incorrectos("se esperaba ; ");
                aux = aux.get_nodo_sig();
                if (aux.get_nodo_sig() != null) {
                    aux = aux.get_nodo_sig();

                    arbol(aux.get_dato());
                }
            }
        } catch (NullPointerException e) {
            incorrectos("");
        }
    }
    public void ciclo_for() throws Exception { 
        try { pivote = "int"; cont++;
            id();
            
            if (aux.get_nodo_sig().get_dato().equals("in")) {
                aux = aux.get_nodo_sig();

            } else { 
                incorrectos("se esperaba \"in\"");
            }
            if (match(aux.get_nodo_sig().get_dato(),"range$")==true) {
                aux = aux.get_nodo_sig(); 
                parentesis_ab(); cont=1;
                ID_CTEEF();
                if (aux.get_nodo_sig().get_dato().equals(",")) { 
                    aux = aux.get_nodo_sig(); }else{incorrectos("se esperaba una coma ");}
                
            }else{   incorrectos("se esperaba \"range\""); }
            ID_CTEEF();
                parentesis_cer();
                llave_ab();
                pila_if.add("for");   ban_llave=false;
              //  llave_cer();
        } catch (NullPointerException e) { incorrectos(""); }
    }
    public void id_repetido(String id, int columna, String tipo){
        separar_id();
     if (variables.contains(id)==true) { 
                System.out.println("id duplicado "+id+" linea "+lineas);
                ID_duplicado.add(id+","+lineas+","+columna);
                ERR_SEMA.add("E007"+","+lineas+","+columna);
            }else{
         guardar_id(aux.get_dato(),aux.get_nodo_sig().get_dato());   //agregamos variable a tabla de simbolos
     }
     
    }
    public void separar_id (){     //separamos el id de las declaraciones y lo metemos a un arraylist
     if (!declaraciones.isEmpty()) {
         variables.clear();
             for (int i = 0; i < declaraciones.size(); i++) {
                 String arreglo [] = declaraciones.get(i).split(",");
                 variables.add(arreglo[1]);
             }
         }  
    }
     public void encontrar_id(String id,int columna){
           //asignacion, condicion o input
             separar_id(); 
            if (variables.contains(id)==false) {  
                System.out.println("id no declarado "+id+" linea "+lineas);
                id_no_decl.add(id+","+lineas+","+columna);
                 ERR_SEMA.add("E003"+","+lineas+","+columna);
                if(cont==1){ cont=0;} //esto en la condicion, para que a la vuelta el contador se convierta en uno y se haga del mismo tipo
            }else{ //si contiene el id
                if(ban == true){
                  if(cont==1){ pivote = getTipo(id); }
                    if (!pivote.equals(getTipo(id))) {
                        System.out.println("-------- "+pivote+"="+getTipo(id));
                       tipos_no_coi.add(id+","+lineas+","+columna);
                       ERR_SEMA.add("E005"+","+lineas+","+columna);
                       System.out.println("error de tipos "+id+" linea "+lineas+" "+columna);
                    }
            }
            }
    }
     public String getTipo(String id) {
        String tipo = "";
        for (int i = 0; i < declaraciones.size(); i++) {
            String arreglo[] = declaraciones.get(i).split(",");
            if (arreglo[1].equals(id)) {
                tipo = arreglo[0];
            }

        }
        return tipo;
    }
     public void guardar_id(String tipo, String id){
            declaraciones.add(tipo+","+id+","+lineas); //agregamos variable a tabla de simbolos
     }
     public void float_o_int_o_cad(String cte,int columna){
          if(ban == true){  
         if (match(cte,"^[0-9]+$")==true) { 
            if(cont==1){pivote="int";} //si el primer dato en la condicion es de tipo entero
             if (!pivote.equals("int")) { //si el primer numero no es entero entonces hay un error
                 tipos_no_coi.add(cte+","+lineas+","+columna);
                 ERR_SEMA.add("E005"+","+lineas+","+columna);
                       System.out.println("error de tipos "+cte+" linea "+lineas+" "+columna);
             }
         }else if(match(cte,"(\"[^\"]*\")")==true){ 
         if(cont==1){pivote="String";}
         if (!pivote.equals("String")) { //si el primer numero no es entero entonces hay un error
                 tipos_no_coi.add(cte+","+lineas+","+columna);
                 ERR_SEMA.add("E005"+","+lineas+","+columna);
                       System.out.println("error de tipos "+cte+" linea "+lineas+" "+columna);
             }
         }else{ 
             if(cont==1){pivote="float";} 
                if (!pivote.equals("float")) { //si el primer numero no es entero entonces hay un error
                 tipos_no_coi.add(cte+","+lineas+","+columna);
                 ERR_SEMA.add("E005"+","+lineas+","+columna);
                       System.out.println("error de tipos "+cte+" linea "+lineas+" "+columna);
              }
         }
     }
     }
     public void no_suma() {
        if (match(aux.get_dato(), "[\\*\\-/]")) {
            op_incorrecto.add(aux.get_dato() + "," + lineas + "," + aux.get_indice());
            ERR_SEMA.add("E008"+","+lineas+","+aux.get_indice());
            System.out.println("operador incorrecto " + aux.get_dato() + "," + lineas + "," + aux.get_indice());
        }
    }
     public void concat() throws Exception{
     try{
                    String id = "(^[A-Za-z][A-Za-z0-9_]*$)"; String cte = "([0-9]+\\.)?[0-9]+";
                    if (match(aux.get_nodo_sig().get_dato(), "^(print$|input$|if$|else$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|salto$)") == false ) {

                if (match(aux.get_nodo_sig().get_dato(), id) == true) {
                    
                    System.out.println("es: " + aux.get_nodo_sig().get_dato());
                    no_suma();
                    aux = aux.get_nodo_sig();       pivote=getTipo(aux.get_dato());  //para que el pivote siempre sea el tipo de dato que este y asi no marque error de tipos
                    encontrar_id(aux.get_dato(),aux.get_indice());
                    suma_concat();
                }else if(match(aux.get_nodo_sig().get_dato(),"(\"[^\"]*\")")==true) { //CTECAD
                             no_suma();  //si hay operador invalido antes de la ctecad*/-
                            aux = aux.get_nodo_sig();
                            suma_concat();
                        }else{ 
                            if (match(aux.get_nodo_sig().get_dato(), "-") == true) {
                        aux = aux.get_nodo_sig();
                    }
                    if (match(aux.get_nodo_sig().get_dato(), cte) == true) {
                        no_suma();
                    aux = aux.get_nodo_sig();
                        suma_concat();  
                    }else{incorrectos("Se esperaba una cte o un id ");
                            System.out.println("Se esperaba una cte o un id ");}

                    } 
                }else { //incorrectos("se esperaba una CTE O ID ");
                        System.out.println("---- "+aux.get_nodo_sig().get_dato());
                        ERR_SEMA.add("E004"+","+lineas+","+aux.get_nodo_sig().get_indice());
                        id_pr.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice()); System.out.println("Se esperaba un id");   }
                }catch(NullPointerException e){incorrectos("");}
     }
     public void suma_concat()  throws Exception{
     if (aux.get_nodo_sig()!=null) {
                        if (match(aux.get_nodo_sig().get_dato(),"[\\+]")) {
                            aux = aux.get_nodo_sig();
                           concat();
                        }else if(match(aux.get_nodo_sig().get_dato(),"[\\*\\-/]")==true){
                        op_incorrecto.add(aux.get_nodo_sig().get_dato()+","+lineas+","+aux.get_nodo_sig().get_indice());
                           ERR_SEMA.add("E008"+","+lineas+","+aux.get_nodo_sig().get_indice());
                            aux = aux.get_nodo_sig();
                            concat();
                        }
                    }
     }
     public void eliminar_repetidos(){
 
         int ant = 0; int ant2 = 0;
     for (int i = 0; i < ERR_SEMA.size(); i++) {
             String filas_col [] = ERR_SEMA.get(i).split(",");
              try{
             if (filas_col[1].equals(String.valueOf(ant)) && filas_col[2].equals(String.valueOf(ant2))) { 
                //si las filas y columnas son iguales
                    System.out.println("-------- "+filas_col[1]+" "+filas_col[2]);
                    ERR_SEMA.remove(i); i=i-1; //eliminamos y recorremos el indice hacia atras porque se elimino un indice del array
             }else{ //si las filas y columnas son diferentes
                  ant  = Integer.parseInt(filas_col[1]); 
                  ant2  = Integer.parseInt(filas_col[2]); 
             }
                  for (int j = 0; j < ERR_SIN.size(); j++) {
                      String arre [] = ERR_SIN.get(j).split(",");
                      if (arre[0].equals(filas_col[1])) { //si alguna linea de las diferentes listas de errores son iguales
                          System.out.println(arre[0]+" ---- "+filas_col[1]);
                          if (Integer.parseInt(arre[1]) < Integer.parseInt(filas_col[2]) ) {
                              System.out.println(arre[1]+" < "+filas_col[2]);
                              ERR_SEMA.remove(i); i=i-1;
                          }
                      }
                  }
    
             } catch(NumberFormatException e){ }
                  catch(ArrayIndexOutOfBoundsException e){System.out.println("error de sintaxis en linea "+filas_col[0]+" columna "+filas_col[1]);}
            
         }
     }
       public void tablas_errores(){
           pre_generador gen = new pre_generador();
           if (ERR_SEMA.isEmpty()) { //si no hay errores entonces generamos codigo
               gen.generar();
           }else{
           tablas o = new tablas();
           eliminar_repetidos();
           o.llenar_tabla_sintax(ERR_SIN);
           o.llenar_tab_sim(declaraciones);
           o.llenar_tab_ERR(ERR_SEMA);
           }
           
       }
       
       public boolean match(String prueba, String exp) {
        Pattern patron;   //indicamos la ER
        Matcher mcher;   //compila ER vs Cadena

        patron = Pattern.compile(exp);
        mcher = patron.matcher(prueba);
        // mcher obtiene el resultado de la evaluacion

        if (mcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public void incrementa() throws Exception {
        if (aux.get_nodo_sig().get_dato().equals("salto")) {
            lineas++;
            exe();
        }
    }
       public void exe() throws Exception {
        throw new Exception();
    }
}
