package store.store.sqldata;

import java.util.Random;

import store.store.podstawy.BasicTools;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;


/**
 *
 * Klasa testuje mozliwosc tworzenia klasyfikaora metoda C4.5 na danych popranych z bazy danych
 * 
 */


public class TestClassifierForSQLData 
{
        
    static public void main(String [] args)
    {
        try 
        {
            String username = "postgres";
            String password = "postgres";
             
            
            //UWAGA: Zapytanie podajemy bez średnika na końcu
            String query = 
                "SELECT imie, miasto, aktywny FROM KLIENCI";
            
            Instances data = SQLDataImporter.getDataSetFromPostgreSQL(username,password,query,1000);//Tylko 1000 pierwszych wierszy

            String fileName = "./src/data/from_postgresql.arff"; //Lokalizacja pliku z danymi
            BasicTools.saveData(data, fileName); //Zapis tablicy do pliku w fromacie ARFF

            data.setClassIndex(data.numAttributes() - 1);  // ustawienie atrybutu decyzyjnego (ostatni atrybut)
                                                           // Będziemy badac czy na podstawie imion i miast klientow 
                                                           // mozna stwierdzic, ze sa aktywni lub nie

            //Losowy podzial tablicy
            data.randomize(new Random(12345));
            double percent = 60.0; // 60 procent danych do treningu, reszta do testu
            int trainSize = (int) Math.round(data.numInstances() * percent / 100);
            int testSize = data.numInstances() - trainSize; 
            Instances trainData = new Instances(data, 0, trainSize);
            Instances testData = new Instances(data, trainSize, testSize);

            String[] options = Utils.splitOptions("-U -M 10");
            J48 tree = new J48();
            tree.setOptions(options);
            tree.buildClassifier(trainData);

            System.out.println(tree.toString()); //Wypisanie drzewa w formie tekstowej

            //Testowanie tablicy metoda train&test
            Evaluation eval = new Evaluation(trainData);
            eval.evaluateModel(tree, testData);
            System.out.println(eval.toSummaryString("Wyniki:", false)); //Wypisanie wyniku testowania
            
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR: " + e.getMessage());
            //e.printStackTrace();
        }
        
        System.out.println("KONIEC");
    }    
}
