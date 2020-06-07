package store.store.sqldata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJDBC {

    public static void main(String args[])
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection conection
                = DriverManager.getConnection("jdbc:postgresql://localhost/hurtownie_danych",
                        "postgres", "postgres");
       
        
        
        Statement statement = conection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ID_PRODUKT,MODEL FROM PRODUKTY WHERE ID_PRODUKT=11");

        ResultSetMetaData rsmd
                = resultSet.getMetaData();
        int liczbaKol = rsmd.getColumnCount();
        while (resultSet.next()) {
            String linia = new String();
            for (int i = 1; i <= liczbaKol; i++) {
                if (i > 1) {
                    linia = linia + " ";
                }
                linia = linia + resultSet.getString(i);
            }
            System.out.println(linia);
        }

        statement.close();
    }

}
