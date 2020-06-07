package store.store.sqldata;

import java.io.File;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

/**
 *
 * Klasa dostarcza metod importujących tablice do WEKA z relacyjnej bazy danych 
 * 
 * Podaje się nazwe uzytkonika i haslo do bazy danych
 * 
 * Parametr queryText powinien zawierac poprawne zapytanie SQL (BEZ ŚREDNIKA NA KOŃCU!)
 * 
 * Parametr limit ogranicza liczbe wierszy kopiowanych do tablicy z wyniku zapytania 
 * Wiersze sa brane od poczatku wyniku zapytania.
 * Jeśli parametr limit jest mniejszy od 1, to ograniczenie nie jest uwzgledniane
 * 
 * 
 */

public class SQLDataImporter 
{

            
    public static Instances getDataSetFromPostgreSQL(String userName,String password,
                                                     String queryText,int limit) //limit liczby wierszy od początku tabeli                                                     
    throws Exception
    {
        
        InstanceQuery query = new InstanceQuery();
        query.setUsername(userName);
        query.setPassword(password);
        
        // plik ustawien polaczenia z baza danych
        query.setCustomPropsFile(new File("./src/settings/DatabaseUtils.props"));

        if (limit>0) query.setQuery(queryText +" limit "+limit+";");
        else query.setQuery(queryText+";");

        Instances data = query.retrieveInstances();
        
        return data;            
    }
}
