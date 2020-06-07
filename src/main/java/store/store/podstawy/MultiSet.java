package store.store.podstawy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//Klasa realizuje multizbiór (zbiór, gdzie elementy mogą wystąpić wielokrotnie)

public class MultiSet 
{    
    private HashMap<String,Integer> multiSet;
    
    public MultiSet()
    {
        multiSet = new HashMap<>();
    }
    
    
    //Dodanie elementu
    
    public boolean addElem(String elem)
    {        
        if (multiSet.isEmpty() || !multiSet.containsKey(elem))
        {
            multiSet.put(elem,new Integer(1)); //Jeszcze tego elementu nie bylo
            return true;
        }
        else
        {
            Integer oldVal = multiSet.get(elem);
            Integer newVal = new Integer(oldVal.intValue()+1); //Element juz był - zwiększamy liczbe jego wystapienia (krotnosc) 
            multiSet.put(elem,newVal);
            return false;
        }
    }
    
    //Pobranie krotnosci dla elementu
    
    public int getCount(String elem)
    {
        if (multiSet.containsKey(elem))
        {
            Integer val = multiSet.get(elem);
            return val.intValue();
        }
        else
        {
            return -1; //Nie ma takiego elementu
        }
    }
    
    
    public int size()
    {
        return multiSet.size();
    }
    
    public Iterator<String> getIterator()
    {
        Set<String> keySet = multiSet.keySet();
        return keySet.iterator();        
    }
    
    public void print()
    {
        Iterator<String> iterator = getIterator();
        
        while (iterator.hasNext())
        {
            String elem = iterator.next();
            int v = getCount(elem);
            System.out.print(elem+"("+v+"), ");
        }
        System.out.println();
    }
    
    
    public static void main(String[] args)
    {
        MultiSet multiSet = new MultiSet();
        
        
        multiSet.addElem("A");
        multiSet.addElem("B");
        multiSet.addElem("A");
        multiSet.addElem("B");
        multiSet.addElem("D");
        multiSet.addElem("A");
        
        Iterator<String> iterator = multiSet.getIterator();
        
        while (iterator.hasNext())
        {
            String elem = iterator.next();
            int v = multiSet.getCount(elem);
            System.out.println(elem+" "+v);
        }
            
        System.out.println("KONIEC");
    }
    
}
