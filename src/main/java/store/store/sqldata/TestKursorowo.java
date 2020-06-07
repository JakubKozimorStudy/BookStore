package store.store.sqldata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


//Demonstracja przegladania tabeli wierszami, bez pobierania calej tabeli, a jedynie po jednym wierszu

public class TestKursorowo 
{

    public static void main(String args[])
            throws ClassNotFoundException, SQLException 
    {
        Class.forName("org.postgresql.Driver");
        Connection conection
                = DriverManager.getConnection("jdbc:postgresql://localhost/SAM1",
                        "postgres", "rzeszow2016");
       
        
        conection.setAutoCommit(false); //Upewnienie się, że nie ma automatycznego commita
        
        //Utworzenie polecenia przygotowanego z parametrem wartosci kolumny ID_PRODUKT
        PreparedStatement statement = conection.prepareStatement("SELECT ID_PRODUKT,MODEL FROM PRODUKTY WHERE ID_PRODUKT = ?");
        
        int columnValue = 10; //Numer poczatkowego wiersza
                        
        while (true)
        {
            statement.setInt(1, columnValue); //Ustawienie wartosci parametru ID_PRODUKT na wartosc columnValue
            //Uwaga: Tutaj mozna ustawiac wartosc takze innych typow (innymi metodami; np. setString) w zaleznosci od typu wartosci w kolumnie
                    
            ResultSet resultSet = statement.executeQuery(); //Wykonanie zapytania 
        
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int liczbaKol = rsmd.getColumnCount();
            
            if (resultSet.next()) //Przechodzimy na pierwszy i jedyny wiersz (jak nie ma wiersza dla ktorego ID_PRODUKT=columnValue, to przechodzi do nstepnego columnValue)
            {
                String linia = new String();

                for (int i = 1; i <= liczbaKol; i++) //Pobranie rekordu 
                {                
                    linia = linia + resultSet.getString(i) + " ";
                }

                System.out.println(linia); //Wypisanie rekordu
            }
            else break;
            
            columnValue = columnValue + 1; //Przejscie do nastepnej wartosci w kolumnie ID_PRODUKT        
        }

        statement.close();
    }

}
