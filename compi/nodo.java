
package compi;


public class nodo {
      String nom; nodo sig; int contador;
  nodo siguiente,anterior;
    public nodo(String nombre){
    this.nom=nombre;
    }
    
    public String get_dato(){
    return nom;
    }
    
    public void  set_nodo_sig(nodo n,int cont){
    this.siguiente = n; 
    this.contador = cont; 
    }
     public void  set_nodo_sig(nodo n){
    this.siguiente = n; 
    }
     
    public nodo get_nodo_sig(){
    return siguiente;
    }
    public int get_indice(){
        return contador;
    }
     
}
