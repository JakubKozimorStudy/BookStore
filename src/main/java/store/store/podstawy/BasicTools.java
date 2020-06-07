package store.store.podstawy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * Podstawowe narzedzia ulatwiajace wykorzystywanie API WEKA
 */

public class BasicTools 
{
    
    //Odczytanie tablicy danych z dysku w formacie ARFF
    public static Instances loadData(String fileName)
    throws IOException
    {
        ArffLoader loader = new ArffLoader(); //Utworzenie obiektu czytajacego dane z formatu ARFF
        loader.setFile(new File(fileName)); //Ustawienie pliku do odczytania
        return loader.getDataSet(); //Odczytanie danych z pliku
    }

    //Importowanie danych z formatu CSV do formatu ARFF
    public static void importCSVtoARFF(String fileNameCSV,String fileNameARFF)
    throws IOException
    {
        CSVLoader loader = new CSVLoader(); //Utworzenie obiektu czytajacego dane z formatu CSV
        loader.setSource(new File(fileNameCSV)); //Ustawienie pliku do odczytania
        Instances data = loader.getDataSet(); //Odczytanie danych z pliku
        saveData(data,fileNameARFF); //Zapis tablicy do pliku w romacie ARFF
    }

    //Zapis zbioru danych do formatu ARFF
    public static void saveData(Instances data,String fileName)
    throws IOException
    {
        ArffSaver saver = new ArffSaver(); //Utworzenie obiektu zapisujacego dane
        saver.setFile(new File(fileName)); //Ustawienie nazwy pliku do zapisu
        saver.setInstances(data);        
        saver.writeBatch(); //Zapis do pliku
    }

    //--------------------------------------------------
    
    
    //Obliczenie obszaru pozytywnego tablicy decyzyjnej (zakłada się, że ostatni atrybut to atrybut decyzyjny)
    public static double positiveRegion(Instances data) 
    {
        if (data.numInstances() == 0) {
            throw new IllegalArgumentException("There are not rows in the data set!");
        }
        if (data.numAttributes() == 0) {
            throw new IllegalArgumentException("There are not attributes in the data set!");
        }

        int decAttrIndex = data.classIndex();
        if (decAttrIndex < 0) {
            throw new IndexOutOfBoundsException("The index of decision attribute is not set!");
        }

        int noPosObj = 0;

        for (int i = 0; i < data.numInstances(); i++) 
        {
            boolean isInPosReg = true;

            Instance instance1 = data.instance(i);
            double decVal1 = instance1.classValue();
            

            for (int j = 0; j < data.numInstances(); j++) 
            {
                if (i == j) continue;
                
                Instance instance2 = data.instance(j);
                double decVal2 = instance2.classValue();

                boolean isEqual = true;
                for (int k = 0; k < data.numAttributes(); k++) 
                {
                    if (k == decAttrIndex) continue;

                    double val1 = instance1.value(k);
                    double val2 = instance2.value(k);

                    if (Double.compare(val1, val2) != 0) 
                    {
                        isEqual = false;
                        break;
                    }
                }

                if ((isEqual) && (Double.compare(decVal1, decVal2) != 0)) 
                {
                    isInPosReg = false;
                    break;
                }
            }

            if (isInPosReg) {
                noPosObj++;
            }
        }

        return (double) noPosObj / data.numInstances();
    }
    
    
    //Wyciecie podtablicy skladajacej sie tylko z wybranych kolumn (atrybutow)
    static public Instances extractColumns(Instances data,ArrayList<String> selectedAttrList) throws Exception 
    {
        for (int i = 0; i < selectedAttrList.size(); i++) //Przegladanie atrybutow do selekcji
        {
            String name = selectedAttrList.get(i);
            boolean isInTable = false;
            for (int j = 0; j < data.numAttributes(); j++) //Przegladanie atrybutow w danych
            {
                Attribute attr = data.attribute(j); //Pobranie atrybutu o podanym numerze
                String attrName = attr.name();
                if (attrName.equals(name)) {
                    isInTable = true;
                    break;
                }
            }

            if (!isInTable) {
                throw new IllegalArgumentException("Cannot find the attribute " + name + " in data set! (extracting attributes)");
            }
        }

        ArrayList<String> attrToRemove = new ArrayList<String>(); //Atrybuty do usuniecia
        for (int i = 0; i < data.numAttributes(); i++) //Przegladanie atrybutow
        {
            Attribute attr = data.attribute(i); //Pobranie atrybutu o podanym numerze
            String attrName = attr.name();
            if (!selectedAttrList.contains(attrName)) {
                attrToRemove.add(attrName); //ten mabyc usuniety
            }
        }

        if (attrToRemove.isEmpty()) {
            return data;
        }
        
        return removeColumns(data,attrToRemove);

    }

    
    //Wyciecie podtablicy bez podanych kolumn (atrybutow)
    static public Instances removeColumns(Instances data,ArrayList<String> selectedAttrList) throws Exception 
    {
        for (int i = 0; i < selectedAttrList.size(); i++) //Przegladanie atrybutow do selekcji
        {
            String name = selectedAttrList.get(i);
            boolean isInTable = false;
            for (int j = 0; j < data.numAttributes(); j++) //Przegladanie atrybutow w danych
            {
                Attribute attr = data.attribute(j); //Pobranie atrybutu o podanym numerze
                String attrName = attr.name();
                if (attrName.equals(name)) //ten atrybut jest w danych
                {
                    isInTable = true;
                    break;
                }
            }

            if (!isInTable) {
                throw new IllegalArgumentException("Cannot find the attribute " + name + " in data set! (removing attributes)");
            }
        }

        ArrayList<Integer> attrToRemove = new ArrayList<Integer>(); //Numery atrybutow do usuniecia
        
        for (int i = 0; i < data.numAttributes(); i++) //Przegladanie atrybutow
        {
            Attribute attr = data.attribute(i); //Pobranie atrybutu o podanym numerze
            String attrName = attr.name();
            if (selectedAttrList.contains(attrName)) {
                attrToRemove.add(new Integer(i)); //ten ma byc usuniety
            }
        }

        int[] attributes = new int[attrToRemove.size()];

        for (int i = 0; i < attrToRemove.size(); i++) {
            attributes[i] = attrToRemove.get(i).intValue();
        }

        Remove remove = new Remove();

        remove.setAttributeIndicesArray(attributes); //Ustawienie listy do usuniecia
        remove.setInputFormat(data);
        Instances newData = Filter.useFilter(data, remove);

        return newData;
    }

    
}
