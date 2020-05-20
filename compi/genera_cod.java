
package compi;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class genera_cod {
     nodo aux = null;
    nodo raiz;
    int cont_llave=0;
    int cont_print=0; int cont_id=0; 
    ArrayList<String> llave = new ArrayList<String>();
    ArrayList<String> DATA = new ArrayList<String>(); 
    ArrayList<String> CODE = new ArrayList<String>(); 
     ArrayList<String> MACROS = new ArrayList<String>(); 
    ArrayList<String> decl = new ArrayList<String>();
    ArrayList<String> pila_while = new ArrayList<String>();
    ArrayList<String> pila_for = new ArrayList<String>();
    ArrayList<String> bloque_else = new ArrayList<String>();
 
     ArrayList<String> pila_if_else = new ArrayList<String>();
    
   int activador = 0;
   boolean ban_while = false; 
   boolean ban_else = false;
   
    public void encontrar_tokens(String expre, String prueba){
    Pattern patron;   //indicamos la ER
        Matcher mcher;   //compila ER vs Cadena
        
        patron = Pattern.compile(expre); //se compila la expresion reg
        mcher = patron.matcher(prueba); //evaluamos la cadena
          
        while (mcher.find()) { //mientras encuentre..
          
            add_nodo(mcher.group());
            if (mcher.group().equals("if")||mcher.group().equals("else") ||mcher.group().equals("}")
                    ||mcher.group().equals("while")||mcher.group().equals("for")) {
                pila_if_else.add(mcher.group());
            }
        }
   
   
    }
    
    public void add_nodo(String dato) {
        
        if (raiz == null) {
            raiz = new nodo(dato);
            
        } else {
            aux = raiz;
            while (aux.get_nodo_sig() != null) {
                aux = aux.get_nodo_sig();
            }
           
            aux.set_nodo_sig(new nodo(dato));
           
        }
    }
    public void hacer_lista(){
        
        int item = 0; 
        //sacar for y while (con sus llaves) de la pila, para que solo quede if,else,}
             int c_llave=0; //contador llave 
        int c_sen=0; //contador sentencia 
            for (int i = 0; i < pila_if_else.size(); i++) {
                System.out.println(" sentencia "+pila_if_else.get(i));
             if (pila_if_else.get(i).equals("while")||pila_if_else.get(i).equals("for")) {
                item = i; System.out.println("indice a eliminar "+i+" sen "+pila_if_else.get(i));
                break;
            }
        }
                 for (int j = item; j < pila_if_else.size(); j++) {
            if (!pila_if_else.get(j).equals("}")) { //si es sentencia
                c_sen++;
            }else{ //si es llave
             c_llave++;
            }
                     
            if (c_llave == c_sen) { //si la cantidad de llaves es igual a las sentencias, encontramos la llave
                System.out.println("llave "+c_llave+"  senten "+c_sen);
                System.out.println("> "+pila_if_else.get(j));   pila_if_else.remove(j);  //eliminamos llave 
                System.out.println("> "+pila_if_else.get(item)); pila_if_else.remove(item);  //eliminamos sentencia
                break;
            }
        }
           

    }
    public void empezar(){
    try{
        int veces = 0; //son las veces que aparece una sentencia for o while
        for (int i = 0; i < pila_if_else.size(); i++) {
            if (pila_if_else.get(i).equals("while")||pila_if_else.get(i).equals("for")) {
                veces++;
            }
        }
        for (int i = 0; i < veces; i++) {
            
             hacer_lista(); 
        }
        System.out.println(" lista final ");
         for (int k = 0; k < pila_if_else.size(); k++) {
            System.out.println("              "+pila_if_else.get(k));
        }
        aux = raiz;
   
     S(aux.get_dato());
    
     escritura();
     }catch(NullPointerException e){System.out.println(" no hay datos ");}
    }
    public void S(String dato){
       
        String pa_res = "(print$|input$|if$|otherwise$|for$|in$|range$|"
                    + "while$|and$|or$|int$|float$|String$|else$)";
                    if (match(dato,pa_res)==false && match(dato,"(^[A-Za-z][A-Za-z0-9_]*$)")==true) { 
                        dato = "asig";
                    }
                    
                           switch(dato){
                   
                        case "if":  
                          try{   cont_llave++;  activador++;
                          llave.add(""+cont_llave);
                         // pila_else.remove(0); //sacamos el if de la cola
                          if (activador == 1) {
                                    macro_hexa();
                                    convert_dec();
                                }
                          aux = aux.get_nodo_sig().get_nodo_sig(); //OPDO
                          String comparador = aux.get_nodo_sig().get_dato();
                          String A = aux.get_dato();
                          String B = aux.get_nodo_sig().get_nodo_sig().get_dato();
                              switch(comparador){
                              
                                  case "<":
                                      comp("JB", A, B, cont_llave);
                                  
                                      get_else("if");  //eliminamos if
                                      if (ban_else==true) { //si hay else
                                          get_else("else"); //eliminamos el else
                                          CODE.add("JNB BLOQUEe"+cont_llave);
                                          bloque_else.add("BLOQUEe"+cont_llave+":");
                                      }
                                      CODE.add("JMP FIN"+cont_llave); 
                                      break;
                                  case ">":
                                      comp("JA", A, B, cont_llave);                                      
                                               get_else("if");  //eliminamos if
                                      if (ban_else==true) { //si hay else
                                          get_else("else"); //eliminamos el else
                                          CODE.add("JNA BLOQUEe"+cont_llave);
                                          bloque_else.add("BLOQUEe"+cont_llave+":");
                                      }                        
                                      CODE.add("JMP FIN"+cont_llave);                      
                                      break;
                                  case "==":
                                      comp("JE", A, B, cont_llave);
                                        get_else("if");  //eliminamos if
                                      if (ban_else==true) { //si hay else
                                          get_else("else"); //eliminamos el else
                                          CODE.add("JNE BLOQUEe"+cont_llave);
                                          bloque_else.add("BLOQUEe"+cont_llave+":");
                                      }
                                      CODE.add("JMP FIN"+cont_llave);
                                      break;
                              }
                             ban_else=false;
                              CODE.add("BLOQUE"+cont_llave+":");
                              aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig();
                          }catch(Exception e){ }
                          
                            break;
                        case "while":
                             try{
                                 ban_while = true;
                            cont_llave++;  activador++;
                          llave.add(cont_llave+"a");
                          if (activador == 1) {
                                    macro_hexa();
                                    convert_dec();
                                }
                          aux = aux.get_nodo_sig().get_nodo_sig(); //OPDO
                          String comparador = aux.get_nodo_sig().get_dato();
                          String A = aux.get_dato();
                          String B = aux.get_nodo_sig().get_nodo_sig().get_dato();
                              switch(comparador){
                              
                                  case "<":
                                      comp("JB", A, B, cont_llave);
                                       CODE.add("JMP FIN"+cont_llave+"a");
                                      break;
                                  case ">":
                                      comp("JA", A, B, cont_llave);
                                       CODE.add("JMP FIN"+cont_llave+"a");
                                      break;
                                  case "==":
                                      comp("JE", A, B, cont_llave);
                                       CODE.add("JMP FIN"+cont_llave+"a");
                                      break;
                              }
                              
                              CODE.add("BLOQUE"+cont_llave+":");
                              aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig();
                          }catch(Exception e){
                              
                                      }
                            break;
                        case "for":
                              try{ 
                                  cont_llave++;  activador++;
                          llave.add(cont_llave+"p");
                          if (activador == 1) {
                                    macro_hexa();
                                    convert_dec();
                                }
                                  aux = aux.get_nodo_sig(); // id incrementador
                                  String id = aux.get_dato();
                                  aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig();
                                  String min = aux.get_dato();
                                  aux = aux.get_nodo_sig().get_nodo_sig();
                                  String max = aux.get_dato();
                                  
                                  para(id, min, max, cont_llave);
                                  
                                  aux = aux.get_nodo_sig().get_nodo_sig(); // {
                            }catch(Exception e){}
                            break;
                        case "input":
                             try{
                                 aux = aux.get_nodo_sig().get_nodo_sig(); //id
                                 //CODE
                                   CODE.add("mov ah,0Ah");
                                   CODE.add("lea dx, "+aux.get_dato());
                                   CODE.add(" int 21h ");
                            aux = aux.get_nodo_sig().get_nodo_sig(); //punto y coma
                            //ahora un salto de linea
                            CODE.add("mov Dx, Offset SALTO");
                               CODE.add("mov Ah, 9");
                               CODE.add("int 21h");
                            }catch(Exception e){}
                            break;
                        
                        case "print": 
                            cont_print++;
                           try{
                               aux = aux.get_nodo_sig().get_nodo_sig(); // OPDO
                               if (get_tipo(aux.get_dato()).equals("int")||get_tipo(aux.get_dato()).equals("String")) { //si es un id 
                                   cont_id++;
                                   if(cont_id == 1){  macro_impr_id();  }
                                   CODE.add("impr_id "+aux.get_dato());
                               }else{  impr_cte_cad(); } //si es una cadena o una cte entera 
                               CODE.add("mov Dx, Offset SALTO");
                               CODE.add("mov Ah, 9");
                               CODE.add("int 21h");
                            
                               aux = aux.get_nodo_sig().get_nodo_sig(); //punto y coma
                               
                            }catch(Exception e){System.out.println("error");}
                            break;
                        case "int": 
                             try{ 
                                  decl.add(aux.get_dato()+","+aux.get_nodo_sig().get_dato()
                                        +","+aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_dato());
                                  
                                 aux = aux.get_nodo_sig(); //variable
                                 String valor = aux.get_nodo_sig().get_nodo_sig().get_dato();
                                 int restante = 4-valor.length();
                                  DATA.add(aux.get_dato()+" db "+"4,"+valor.length()+", '"+valor+"',"+restante+" dup(0) \n dw 0");
                            aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig(); //punto y coma
                            }catch(Exception e){}
                            break;
                        
                        case "String":
                            try{  
                                decl.add(aux.get_dato()+","+aux.get_nodo_sig().get_dato()
                                        +","+aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_dato());
                               aux = aux.get_nodo_sig(); //variable
                               String valor = aux.get_nodo_sig().get_nodo_sig().get_dato();
                                 int restante = 30-valor.length();
                              DATA.add(aux.get_dato()+" db "+"30,"+valor.length()+", '"+valor+"',"+restante+" dup(0)");
                            aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig(); //punto y coma 
                            }catch(Exception e){}
                            break;
                            
                        case "asig": activador++;
                            try{
                                if (activador == 1) {
                                    macro_hexa();
                                    convert_dec();
                                }
                                String A = aux.get_dato(); 
                                String B = aux.get_nodo_sig().get_nodo_sig().get_dato();
                                //ASIGNACION SIMPLE, SIN OPERADORES ARITMETICOS
                                if (aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_dato().equals(";")) {
                                   //si es de tipo A = B; o A = cte;
                                    if (match(B, "(^[A-Za-z][A-Za-z0-9_]*$)")) {
                                        //si B es un ID
                                        CODE.add("hexa_convert "+B);
                                        CODE.add("mov ax, w.["+B+"+6] "); //Obtener hexa en ax
                                        CODE.add("convert_dec "+A); //convertir a decimal desde ax
                                    }else{ //si B es un numero
                                    CODE.add("mov ax, "+B);
                                    CODE.add("convert_dec "+A);
                                    }
                                    aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig(); // punto y coma
                                }else{ //ASIGNACION COMPUESTA
                                     String C = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig().get_dato(); 
                                   String operador = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_dato();
                                   
                            if (!match(B, "(^[A-Za-z][A-Za-z0-9_]*$)") && (operador.equals("+") || operador.equals("*"))) { //si es numero
                                                String auxiliar = B;
                                                    B = C; C= auxiliar;
                                    }
                                                
                                   switch(operador){
                                   
                                        case "+":
                                               aritmetica("add",A,B,C);
                                            break;
                                        case "-":
                                              aritmetica("sub",A,B,C);
                                            break;
                                        case "*":
                                            aritmetica("mul",A,B,C);
                                            break;
                                        case "/":
                                            aritmetica("div",A,B,C);
                                            break;
                                   } 
                                aux = aux.get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig().get_nodo_sig(); // punto y coma
                                }
                              }catch(Exception e){}
                            break;
                        case "else":
                            try{
                            
                            aux = aux.get_nodo_sig(); //{
                                System.out.println("bloque "+bloque_else.get(bloque_else.size()-1));
                            CODE.add(bloque_else.get(bloque_else.size()-1)); //agregamos la etiqueta al codigo
                            bloque_else.remove(bloque_else.size()-1); //sacamos la etiqueta else de la pila
                                }catch(Exception e){}    
                            break;
                                   
                        case "}": 
                            try{
                               
                            //llave de cierre }
                            if (llave.get(llave.size()-1).contains("a")) { //si es llave del while
                               CODE.add(pila_while.get(pila_while.size()-1)); 
                            }
                            if(llave.get(llave.size()-1).contains("p")){ //si es llave del for
                               
                                   CODE.add("pop cx");
                                CODE.add("loop BLOQUE"+llave.get(llave.size()-1)); 
                                llave.remove(llave.size()-1); //eliminamos el par de llaves del array
                            }else{ //si es llave del if o else
                                try{ 
                                    if (aux.get_nodo_sig() != null) {
                                        if (!aux.get_nodo_sig().get_dato().equals("else")) { //si es un if sencillo (sin else) o es un else
                                            CODE.add("JMP FIN" + llave.get(llave.size() - 1));
                                            CODE.add("FIN" + llave.get(llave.size() - 1) + ":");
                                            llave.remove(llave.size() - 1); //eliminamos el par de llaves del array
                                        } else { //si es una llave de if y ese if tiene else
                                            CODE.add("JMP FIN" + llave.get(llave.size() - 1));
                                        }
                                    }else{
                                        CODE.add("JMP FIN" + llave.get(llave.size() - 1));
                                            CODE.add("FIN" + llave.get(llave.size() - 1) + ":");
                                    }

                            }catch(NullPointerException e){
                             CODE.add("JMP FIN"+llave.get(llave.size()-1));
                                    CODE.add("FIN"+llave.get(llave.size()-1)+":");
                                    llave.remove(llave.size()-1); //eliminamos el par de llaves del array
                            }
                            }
                           }catch(ArrayIndexOutOfBoundsException e){System.out.println(e.getMessage());}
                            break;
                    }
                          
                         if (aux.get_nodo_sig() != null) { //si hay otra sentencia, repetir switch
            aux = aux.get_nodo_sig(); 
            S(aux.get_dato());
        }
    }
   
    
    public void impr_cte_cad(){
             //DATA
            DATA.add("Mensaje"+cont_print+" DB '"+aux.get_dato().replaceAll("\"", "")+"','$'");
      
        //CODE
        CODE.add("mov Dx, Offset Mensaje"+cont_print);
        CODE.add("mov Ah, 9");
        CODE.add("int 21h");
    }
   
   public String get_tipo(String var){
   String tipo = "";
       for (int i = 0; i < decl.size(); i++) {
           String arre[] = decl.get(i).split(",");
           if (var.equals(arre[1])) { //si la variable es igual a la variable del array
               tipo = arre[0];
           }
       }
       return tipo;
   }
   public void para(String id, String min, String max, int eti){
       
       aritmetica("add",id,min,"0"); // id = min +0;  //esto es para darle el valor minimo al incrementador
     if (match(min, "(^[0-9]*$)") == true && match(max, "(^[0-9]*$)") == true) {
            //si es de tipo  num > num;
           CODE.add("mov ax, "+min);
           CODE.add("mov bx, "+max);
           CODE.add("sub bx, ax");
           CODE.add("mov cx, bx");
                   
        } else if (match(min, "(^[A-Za-z][A-Za-z0-9_]*$)") == true && match(max, "(^[0-9]*$)") == true) {
            //si es de tipo  A > num;
            CODE.add("hexa_convert " + min);
            CODE.add("mov ax, w.[" + min + "+6] "); //Obtener hexa en ax
            CODE.add("mov bx, "+max);
            CODE.add("sub bx, ax");
            CODE.add("mov cx, bx");
           
        } else if(match(min, "(^[0-9]*$)") == true){ //solo para mul y div
            //si es de tipo num > B
             CODE.add("hexa_convert " + max);
             CODE.add("mov bx, w.[" + max + "+6] "); //Obtener hexa en bx
             CODE.add("mov ax, "+min);
              CODE.add("sub bx, ax");
              CODE.add("mov cx, bx");
   
        } else{ //si es de tipo B > C;
            CODE.add("hexa_convert " + min);
            CODE.add("hexa_convert " + max);
            CODE.add("mov ax, w.[" + min + "+6] "); //Obtener hexa en ax
            CODE.add("mov bx, w.[" + max + "+6] "); //bx = B
            CODE.add("sub bx, ax");
            CODE.add("mov cx, bx");
        }
    
        CODE.add("BLOQUE"+eti+"p:");
        CODE.add("push cx");
        aritmetica("add",id,id,"1");
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
    public void escritura( ){ 
        try
        {
            FileWriter fichero = null;
        PrintWriter pw = null;
        
             String ruta = "src\\compi"; //ruta del archivo de texto
            String nombre = "\\cod_ensam.txt"; //nombre del archivo de texto
             fichero = new FileWriter(ruta+nombre);
      
            pw = new PrintWriter(fichero);
             pw.println(".MODEL SMALL ");
             pw.println(".DATA ");    //DECLARACION
             pw.println("SALTO DB '',10,13,'$'");
             for (int i = 0; i < DATA.size(); i++){
                pw.println(DATA.get(i));
             }
             pw.println(".CODE  ");
             for (int i = 0; i < MACROS.size(); i++){  //MACROS
                pw.println(MACROS.get(i));
             }
             pw.println("EMPIEZA: ");
             pw.println("mov Ax, @Data");  //CODIGO
             pw.println("mov Ds, Ax");
            for (int i = 0; i < CODE.size(); i++){
                pw.println(CODE.get(i));
             }
            pw.println("mov Ax, 4C00h  ");
             pw.println("int 21h   ");
             pw.println(".STACK   ");
             pw.println("END EMPIEZA ");
             
             pw.flush();

        } catch (Exception e) { 
            e.printStackTrace();
        }
    }
    public void get_else(String sentence){
         int item = 0; 
        
        int c_llave=0; //contador llave 
        int c_sen=0; //contador sentencia 
            for (int i = 0; i < pila_if_else.size(); i++) {
                System.out.println(" sentencia "+pila_if_else.get(i));
             if (pila_if_else.get(i).equals(sentence)) { //buscamos el indice de la sentencia
                item = i; System.out.println("indice a eliminar "+i+" sen "+pila_if_else.get(i));
                break;
            }
        }
                 for (int j = item; j < pila_if_else.size(); j++) {
            if (!pila_if_else.get(j).equals("}")) { //si es sentencia
                c_sen++;
            }else{ //si es llave
             c_llave++;
            }
                     
            if (c_llave == c_sen) { //si la cantidad de llaves es igual a las sentencias, encontramos la llave
                System.out.println("llave "+c_llave+"  senten "+c_sen);
                if (pila_if_else.size()-1 != j) { //si no es el ultimo elemento de la lista (para evitar error de Array index)
                    
              if (pila_if_else.get(j+1).equals("else")) { // si el elemento que sigue de la llave if es un else
                        System.out.println("tiene else ");
                        ban_else = true; //entonces vuala! encontramos un if compuesto
                    }
                }
                System.out.println("> "+pila_if_else.get(j));   pila_if_else.remove(j);  //eliminamos llave 
                System.out.println("> "+pila_if_else.get(item)); pila_if_else.remove(item);  //eliminamos sentencia
                break;
            }
        }
    }
    public void comp(String operando, String A, String B, int indice){
    if (match(A, "(^[0-9]*$)") == true && match(B, "(^[0-9]*$)") == true) {
            //si es de tipo  num > num;
           CODE.add("mov ax, "+A);
           CODE.add("mov bx, "+B);
           CODE.add("CMP ax, bx");
           
        } else if (match(A, "(^[A-Za-z][A-Za-z0-9_]*$)") == true && match(B, "(^[0-9]*$)") == true) {
            //si es de tipo  B > num;
            CODE.add("hexa_convert " + A);
            CODE.add("mov ax, w.[" + A + "+6] "); //Obtener hexa en ax
            CODE.add("mov bx, "+B);
            CODE.add("CMP ax, bx");
           if(ban_while==true){
            pila_while.add("hexa_convert " + A + " \n "
                    + "mov Ax, w.[" + A + "+6] \n"
                    + "mov Bx , "+B+" \n"
                            + "CMP ax, bx \n"
                            + operando+" BLOQUE"+indice);   }
        } else if(match(A, "(^[0-9]*$)") == true){ //solo para mul y div
            //si es de tipo num > B
             CODE.add("hexa_convert " + B);
             CODE.add("mov ax, w.[" + B + "+6] "); //Obtener hexa en ax
             CODE.add("mov bx, "+A);
             CODE.add("CMP bx, ax");
             if(ban_while==true){
             pila_while.add("hexa_convert " + B + " \n "
                     + "mov bx, w.[" + B + "+6] \n"
                    + "mov ax , "+A+" \n"
                            + "CMP ax, bx \n"
                            + operando+" BLOQUE"+indice);  }
        } else{ //si es de tipo B > C;
            CODE.add("hexa_convert " + A);
            CODE.add("hexa_convert " + B);
            CODE.add("mov ax, w.[" + A + "+6] "); //Obtener hexa en ax
            CODE.add("mov bx, w.[" + B + "+6] "); //ax = B
            CODE.add("CMP ax, bx");
            if(ban_while==true){
            pila_while.add("hexa_convert " + A + " \n "
                    + "hexa_convert " + B + " \n "
                    + "mov Ax, w.[" + A + "+6] \n"
                    + "mov Bx , w.[" + B + "+6] \n"
                            + "CMP ax, bx \n"
                            + operando+" BLOQUE"+indice);   }
        }
        CODE.add(operando+" BLOQUE"+indice); 
        ban_while = false;
    }
    public void aritmetica(String op, String A, String B, String C){
        if (match(B, "(^[0-9]*$)") == true && match(C, "(^[0-9]*$)") == true) {
            //si es de tipo A = num + num;
            CODE.add("mov ax, " + B);
                if (op.equals("add") || op.equals("sub")) { //suma y resta
                CODE.add(op+" ax, " + C);
            }else{ //mul y div
                CODE.add("mov bx, "+C);
                CODE.add(op+" bx");
                }
            CODE.add("convert_dec " + A);
        } else if (match(B, "(^[A-Za-z][A-Za-z0-9_]*$)") == true && match(C, "(^[0-9]*$)") == true) {
            //si es de tipo A = B + num;
            CODE.add("hexa_convert " + B);
            CODE.add("mov ax, w.[" + B + "+6] "); //Obtener hexa en ax
             if (op.equals("add") || op.equals("sub")) { //suma y resta
                CODE.add(op+" ax, " + C);
            }else{ //mul y div
                CODE.add("mov bx, "+C);
                CODE.add(op+" bx");
                }
            CODE.add("convert_dec " + A); //convertir a decimal desde ax
        } else if(match(B, "(^[0-9]*$)") == true){ //solo para mul y div
            //si es de tipo A = num + B
            CODE.add("hexa_convert " + C);
            CODE.add("mov bx, w.[" + C + "+6] "); //Obtener hexa en bx
            CODE.add("mov ax, "+B);
            CODE.add(op+" bx");
            CODE.add("convert_dec " + A); //convertir a decimal desde ax
        } else{ //si es de tipo A = B + C;
            CODE.add("hexa_convert " + C);
            CODE.add("hexa_convert " + B);
             CODE.add("mov bx, w.[" + C + "+6] "); //Obtener hexa en ax
            CODE.add("mov ax, w.[" + B + "+6] "); //ax = B
            if (op.equals("add") || op.equals("sub")) { //suma y resta
                CODE.add(op+" ax, bx"); // sumamos B+C
            }else{   CODE.add(op+" bx"); } //mul y div
            CODE.add("convert_dec " + A); //convertir a decimal desde ax
        }
    }
    public void macro_hexa(){
    MACROS.add("hexa_convert macro variable\n" +
"    local ciclo\n" +
"    local ciclo2  \n" +
"    local term\n" +
"     ;borrar el valor hexadecimal que ya estaba guardado        \n" +
"     mov w.[variable+6],w.0\n" +
"    \n" +
"    ;convertir a hexadecimal\n" +
"    mov w.[variable+6],w.0\n" +
"    mov cl,[variable+1]\n" +
"    mov ch,0\n" +
"    mov si,2\n" +
"    mov al,6\n" +
"    mov ah,0\n" +
"    mov di,ax\n" +
"    add di,offset variable\n" +
"    ciclo:\n" +
"    push cx\n" +
"    dec cx\n" +
"    mov Ax,1 \n" +
"    cmp cx,0\n" +
"    je term\n" +
"    ciclo2:\n" +
"    mov bx,0Ah\n" +
"    mul bx\n" +
"    loop ciclo2\n" +
"    term:\n" +
"    pop cx\n" +
"    mov bh,0\n" +
"    mov bl,[variable+si]\n" +
"    sub bl,30h\n" +
"    mul bx\n" +
"    add [di],ax\n" +
"    inc si\n" +
"    loop ciclo\n" +
"    \n" +
"endm  ");
    }
    public void convert_dec(){
    MACROS.add("convert_dec macro variable\n" +
"     ;convertir a decimal desde ax\n" +
"      local ciclo3 \n" +
"      local ciclo4 \n" +
"    mov cx,0\n" +
"    mov bx,0Ah\n" +
"    mov dx,0\n" +
"     ciclo3:\n" +
"    div bx\n" +
"    push dx\n" +
"    mov dx,0 \n" +
"    inc cx\n" +
"    cmp ax,0\n" +
"    jne ciclo3\n" +
"    mov di,2\n" +
"    mov [variable+1],cl\n" +
"     ciclo4:     \n" +
"    pop dx \n" +
"    add dl,30h\n" +
"    mov [variable+di],dl \n" +
"    inc di\n" +
"    loop ciclo4  \n" +
"    \n" +
"endm ");
    }
    public void macro_impr_id(){
    MACROS.add("impr_id macro variable\n" +
"        \n" +
"         mov ah,40h\n" +
"        mov bx,1\n" +
"         mov cl,[variable+1]\n" +
"         mov ch,0\n" +
"         lea dx, [variable+2]\n" +
"         int 21h \n" +
"endm");
    }
}
