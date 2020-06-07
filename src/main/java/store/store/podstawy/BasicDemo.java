package store.store.podstawy;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.J48;
import weka.clusterers.EM;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;


public class BasicDemo
{

    
    //Wypisanie typow atrybutow
    public static void infoAttr()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/testowa.arff");

        for (int i = 0; i < data.numAttributes(); i++) //Przegladanie atrybutow
        {
            Attribute attr = data.attribute(i); //Pobranie atrybutu o podanym numerze
            System.out.print("  " + attr.name() + "  Typ:");

            //Odczytanie i wypisanie typu atrybutu
            if (attr.isNominal()) System.out.println("NOMINAL");
            else            
              if (attr.isNumeric()) System.out.println("NUMERIC");
              else
                if (attr.isString())  System.out.println("STRING");
                else 
                    System.out.println("NIEZNANY TYP");
        }
    }

    //Wypisanie na ekran calej tablicy danych (wierszami)
    public static void infoObj()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/testowa.arff");

        for (int i=0; i<data.numInstances(); i++) //Przegladanie obiektow
        {
            System.out.println("Wiersz numer " + i + ":");
            
            Instance instance = data.instance(i); //Pobranie obiektu (wiersza danych) o podanym numerze
            
            for (int j = 0; j < instance.numAttributes(); j++) //Przegladanie atrybutow w obiekcie
            {
                String textValue = instance.toString(j); //Pobranie wartosci atrybutu o podanym numerze (tzn. pobranie tekstowej reprezentacji wartosci)
                System.out.print(textValue + ", ");
            }
            System.out.println();
        }
    }

    //Zmiana wartosci w tablicy
    public static void infoObjZmiana()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/testowa.arff");        
        System.out.println("Zawartosc zbioru danych:");
        System.out.println(data.toString()); //Wypisanie calej tablicy danych
        System.out.println("-----------------------------------------");

        for (int i = 0; i < data.numInstances(); i++) //Przegladanie obiektow
        {
            Instance instance = data.instance(i); //Pobranie obiektu (wiersza danych) o podanym numerze

            double wzrost = instance.value(1); //Pobranie liczbowej wartosci atrybutu o podanym numerze
            instance.setValue(1, wzrost + 10); //Ustawienie nowej liczbowej wartosci tego samego atrybutu

            String imie = instance.stringValue(0); //Pobranie tekstowej wartosci atrybutu o podanym numerze
            instance.setValue(0, "A" + imie); //Ustawienie nowej tekstowej wartosci tego samego atrybutu
        }

        System.out.println("Zawartosc zbioru danych po zmianach:");
        System.out.println(data.toString()); //Wypisanie calej tablicy danych
    }

    //Obliczanie statystyk dla atrybutow
    public static void liczStatystyki()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");

        for (int i = 0; i < data.numAttributes(); i++)
        {
            AttributeStats attributeStats = data.attributeStats(i); //Wyliczenie statystyk atrybutu o podanym numerze
            
            System.out.println("Atrybut:" + data.attribute(i).name()); //Wypisanie nazwy atrybutu

            if (data.attribute(i).isNumeric())
            {
                //Wypisanie statystyk dla atrybutu numerycznego
                System.out.println(" Max=" + attributeStats.numericStats.max); //Maksymalna wartosc
                System.out.println(" Min=" + attributeStats.numericStats.min); //Minimalna wartosc
                System.out.println(" Mean=" + attributeStats.numericStats.mean); //Srednia
                System.out.println(" StdDev=" + attributeStats.numericStats.stdDev); //Odchylenie standardowe
                System.out.println(" Count=" + attributeStats.numericStats.count); //Liczba wartosci atrybutu
            }
            else
            {
                //Wypisanie statystyk dla atrybutu symbolicznego

                System.out.println("Liczba roznych wartosci atrybutu=" + attributeStats.distinctCount);
                
                //Wypisanie liczebnosci obiektow z poszczegolnymi wartosciami atrybutu symbolicznego
                for (int j = 0; j < attributeStats.nominalCounts.length; j++)
                {
                    Attribute attr = data.attribute(i);//Pobranie atrybutu o podanym numerze
                    String attrValue = attr.value(j); //Pobranie wartosci atrybutu o podanym numerze (na liscie wartosci)
                    int attrValueCount = attributeStats.nominalCounts[j]; //Pobranie liczebnosci obiektow z wartoscia atrybutu o podanym numerze
                    System.out.print(attrValue + "(" + attrValueCount + ") ");
                }
                System.out.println();
            }
        }
    }


    //Tworzenie nowej tablicy w pamieci
    public static void nowaTablica()
    throws Exception
    {
        //Beda utworzone trzy atrybuty:
        // 1. Numeryczny (o wartosciach liczbowych),
        // 2. Symboliczny (wartosci: tak i nie),
        // 3. Tekstowy (o wartosciach typu String)

        Attribute numeryczny = new Attribute("Numeryczny"); //Utworzenie atrybutu numerycznego
                        
        ArrayList<String> labels = new ArrayList<String>(); //Utworzenie obiektu kolekcji wartosci nowego atrybutu symbolicznego
        labels.add("tak"); //Wstawienie do kolekcji wartosci "tak"
        labels.add("nie"); //Wstawienie do kolekcji wartosci "nie"        
        Attribute symboliczny = new Attribute("Symboliczny", labels); //Utworzernie atrybutu symbolicznego

        Attribute tekstowy = new Attribute("Tekstowy",(ArrayList<String>) null); //Utworzenie atrybutu o wartosciach tekstowych

        
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(); //utworzenie listy atrybutow dla nowej tablicy
        attributes.add(numeryczny);
        attributes.add(symboliczny);
        attributes.add(tekstowy);

        //Utworzenie pustej tablicy danych
        Instances data = new Instances("Nowa tablica", attributes, 0);

        //Wstawienie czterech pustych obiektow (z wartosciami "?" - czyli "missing")
        for (int i = 0; i < 4; i++)
        {
            DenseInstance instance = new DenseInstance(attributes.size());
            data.add(instance);
        }

        //Wypelnienie tablicy wartosciami (UWAGA NA TYP!!!)
        for (int i = 0; i < data.numInstances(); i++)
        {
            Instance instance = data.instance(i); //Pobranie obiektu o podanym numerze

            instance.setValue(0, i + 1);
            instance.setValue(1, "tak");            
            instance.setValue(2, "Ala" + i);
        }

        BasicTools.saveData(data, "./src/podstawy/data/nowa_tablica.arff"); //Zapis utworzonej tablicy

    }

    //Losowy podzial tablicy wejsciowej na czesc treningowa i testowa
    public static void podzial()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");

        //Tasujemy obiekty, aby wylosowac probki
        data.randomize(new Random());

        //Tworzymy dwie podtablice (w poziomie)
        double percent = 70.0; //70 procent
        
        int trainSize = (int) Math.round(data.numInstances()*percent/100); //Wyliczenie liczby obiektow w tablicy treningowej
        int testSize = data.numInstances() - trainSize;  //Wyliczenie liczby obiektow w tablicy testowej
        
        Instances train = new Instances(data, 0, trainSize); //Do tablicy treningowej bierzemy poczatek tablicy wejsciowej
        Instances test = new Instances(data, trainSize, testSize); //Do tablicy testowej bierzemy koniec tablicy wejsciowej

        //Zapisujemy podtablice
        BasicTools.saveData(train, "./src/podstawy/data/irysy70.arff");
        BasicTools.saveData(test, "./src/podstawy/data/irysy30.arff");

    }

    //Filtrowanie atrybutow z tablicy (wybor podtablicy bez okreslonych atrybutow)
    static public void filtrowanieAtrybutow()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/testowa.arff");
        Remove remove = new Remove();
        int[] attributes = {0, 3, 4}; //Lista numerow atrybutow do usuniecia (numeracja od 0)
        remove.setAttributeIndicesArray(attributes); //Ustawienie listy do usuniecia
        remove.setInputFormat(data);
        Instances newData = Filter.useFilter(data, remove);  // Zastosowanie filtra
        BasicTools.saveData(newData, "./src/podstawy/data/testowa_R.arff"); //Zapis tablicy po filtracji
    }

    
    //Dyskretryzacja danych (metoda zstępująca oparta na minimalizacji entropii)
    public static void dyskretyzacja()
    throws Exception
    {
        //Czytanie tablicy
        Instances data = BasicTools.loadData("./src/podstawy/data/serce.arff");
        data.setClassIndex(data.numAttributes() - 1); //Ustawienie atrybutu decyzyjnego

        //Dyskretyzacja tablicy
        Discretize filter = new Discretize();
        filter.setInputFormat(data); //Tworzenie fltra dyskretyzujacego (ciec)

        Instances dataDiscret = Filter.useFilter(data,filter); //Dyskretyzacja tablicy

        //Zapis tablicy po dyskretyzacji
        BasicTools.saveData(dataDiscret,"./src/podstawy/data/serce_DISCRET.arff");

        //Zapis filtraz\  do pliku binarnego (do pozniejszego wykorzystania)
        SerializationHelper.write("./src/podstawy/data/discretization.bin",filter);

        System.out.println("----------------------------------");

        //DEMONSTRACJA ODCZYTU I ZASTOSOWANIA ODCZYTANEGO FILTRA

        //Odczytanie filtra
        Discretize filter2 = (Discretize)SerializationHelper.read("./src/podstawy/data/discretization.bin");        

        Instances data2 = BasicTools.loadData("./src/podstawy/data/serce.arff"); //Odczytanie tablicy
        data2.setClassIndex(data2.numAttributes() - 1);        

        Instances dataDiscret2 = Filter.useFilter(data2,filter2); //Dyskretyzacja tablicy
        
        BasicTools.saveData(dataDiscret2,"./src/podstawy/data/serce_DISCRET2.arff"); //Powtorny zapis filtra
    }

    
    //DEMONSTRACJA BUDOWY i UZYCIA KLASYFIKATORA na przykladzie C4.5

    //Konstrukcja klasyfikatora C4.5
    static public void buidClassifierJ48()
    throws FileNotFoundException, IOException, Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");

        //Ustawienie atrybutu decyzyjnego (ostatni atrybut)
        data.setClassIndex(data.numAttributes() - 1);

        //OPCJE:
        //-U -> budowa drzewa bez przycinania (ostre liscie)
        //-C -> <wspolczynnik dokladnosci> - ustawienie wspolczynnika dokladnosci dla lisci (default 0.25)
        //-M -> ustawienie minimalnej liczby obiektow w lisciu dla ktorej lisc nie jest dzielony (default 2)

        //Ustalenie opcji
        String[] options = Utils.splitOptions("-U -M 10");

        J48 tree = new J48();    
        tree.setOptions(options); //Ustawienie opcji
        tree.buildClassifier(data);  // Tworzenie klasyfikatora (drzewa)

        System.out.println(tree.toString()); //Wypisanie drzewa w formie tekstowej
    }

    //Test train&test z podzialem losowym tablicy
    static public void trainAndTestJ48_1()
    throws FileNotFoundException, IOException, Exception 
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");
        data.setClassIndex(data.numAttributes() - 1);

        //Losowy podzial tablicy
        data.randomize(new Random());
        double percent = 60.0;
        int trainSize = (int) Math.round(data.numInstances() * percent / 100);
        int testSize = data.numInstances() - trainSize;
        Instances trainData = new Instances(data, 0, trainSize);
        Instances testData = new Instances(data, trainSize, testSize);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();
        tree.setOptions(options);
        tree.buildClassifier(trainData);

        //Testowanie tablicy metoda train&test
        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(tree, testData);
        System.out.println(eval.toSummaryString("Wyniki:", false)); //Wypisanie wyniku testowania
    }

    //Test train&test dla danych dwoch tablic
    static public void trainAndTestJ48_2()
    throws FileNotFoundException, IOException, Exception
    {
        Instances trainData = BasicTools.loadData("./src/podstawy/data/irysy70.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/irysy30.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();
        tree.setOptions(options);
        tree.buildClassifier(trainData);

        //Testowanie pierwszej tablicy
        Evaluation evalTrain = new Evaluation(trainData);
        evalTrain.evaluateModel(tree, trainData);
        System.out.println(evalTrain.toSummaryString("Wyniki dla treningowej:", false));

        //Testowanie drugiej tablicy
        Evaluation evalTest = new Evaluation(trainData);
        evalTest.evaluateModel(tree, testData);
        System.out.println(evalTest.toSummaryString("Wyniki dla testowej:", false));
    }

    //Test train&test dla danych dwoch tablic z konrola wypisania pojedynczych wynikow
    static public void trainAndTestJ48_3()
    throws FileNotFoundException, IOException, Exception
    {
        Instances trainData = BasicTools.loadData("./src/podstawy/data/irysy70.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/irysy30.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();
        tree.setOptions(options);
        tree.buildClassifier(trainData);
        
        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(tree, testData);

        System.out.println("Liczba wszystkich obiektow testowanych="+eval.numInstances());
	System.out.println("Liczba poprawnie sklasyfikowanych="+eval.correct());
        System.out.println("Procent poprawnie sklasyfikowanych="+eval.pctCorrect());
        System.out.println("Liczba niepoprawnie sklasyfikowanych="+eval.incorrect());
        System.out.println("Procent niepoprawnie sklasyfikowanych="+eval.pctIncorrect());
        System.out.println("Liczba niesklasyfikowanych="+eval.unclassified());
        System.out.println("Procent niesklasyfikowanych="+eval.pctUnclassified());
        
        System.out.println("-------------------------------------------------");

        int noDecClasses = trainData.classAttribute().numValues();        
                
        //for (int i=0; i<eval.getNumClasess(); i++)
        for (int i=0; i<noDecClasses; i++)
        {
                String decClassName = trainData.classAttribute().value(i);
        	System.out.println(decClassName+ " ---");
	        System.out.println("precision="+eval.precision(i));
	        System.out.println("falseNegativeRate="+eval.falseNegativeRate(i));
	        System.out.println("falsePositiveRate="+eval.falsePositiveRate(i));
	        System.out.println("trueNegativeRate="+eval.trueNegativeRate(i));
	        System.out.println("truePositiveRate="+eval.truePositiveRate(i));
	        System.out.println("areaUnderROC="+eval.areaUnderROC(i));
	        System.out.println("-----------------");
        }

        //Macierz pomylek
        System.out.println(eval.toMatrixString());

        //Informacja o poszczegolnych klasach
        System.out.println(eval.toClassDetailsString());
    }

    //Test metoda cross-validation
    static public void crossValidationJ48()
    throws FileNotFoundException, IOException, Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");
        data.setClassIndex(data.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();
        tree.setOptions(options);
        tree.buildClassifier(data);

        Evaluation eval = new Evaluation(data);
        //Cross-validation dla 10 foldow
	eval.crossValidateModel(tree,data, 10,new Random()); //Wybor probek losowy
        //eval.crossValidateModel(tree,data, 10, new Random(1)); //Wybor probek deterministyczny

        System.out.println(eval.toSummaryString("Wyniki dla metody CV:", false));

        System.out.println("Liczba wszystkich obiektow testowanych="+eval.numInstances());
	System.out.println("Liczba poprawnie sklasyfikowanych="+eval.correct());
        System.out.println("Procent poprawnie sklasyfikowanych="+eval.pctCorrect());
        System.out.println("Liczba niepoprawnie sklasyfikowanych="+eval.incorrect());
        System.out.println("Procent niepoprawnie sklasyfikowanych="+eval.pctIncorrect());
        System.out.println("Liczba niesklasyfikowanych="+eval.unclassified());
        System.out.println("Procent niesklasyfikowanych="+eval.pctUnclassified());
    }

    //Klasyfikacja nowych obiektow testowych
    public static void klasyfikacjaNowychPrzypadkow()
    throws Exception
    {
        Instances trainData = BasicTools.loadData("./src/podstawy/data/irysy_dla_klasyfikacji_nowych.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);
        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();
        tree.setOptions(options);
        tree.buildClassifier(trainData);

        //Wczytanie tablicy z pustym atrybutem decyzyjnym
        Instances unlabeledData = BasicTools.loadData("./src/podstawy/data/irysy_bez_decyzji.arff");
        unlabeledData.setClassIndex(unlabeledData.numAttributes() - 1);

        //Kopia tablicy nieetykietowanej (do wypenienia)
        Instances labeled = new Instances(unlabeledData);

        for (int i = 0; i < unlabeledData.numInstances(); i++)
        {
            //Klasyfikacja obiektu
            double decision = tree.classifyInstance(unlabeledData.instance(i));

            //Ustawienie wartosci decyzji w obiekcie
            labeled.instance(i).setClassValue(decision);

            //Wypisanie wstawionej decyzji
            System.out.println("Decyzja=" + decision + "  DEC=" + labeled.instance(i).toString(labeled.numAttributes() - 1));
        }

        //Zapis tablicy wypelnionej
        BasicTools.saveData(labeled, "./src/podstawy/data/irysy_bez_decyzji_wypelnione.arff");

    }

    //Zapis i odczyt klasyfikatora do formatu binarnego WEKA
    static public void testZapisuKlasyfikatora()
            throws Exception
    {
        Instances data70 = BasicTools.loadData("./src/podstawy/data/irysy70.arff");
        Instances data30 = BasicTools.loadData("./src/podstawy/data/irysy30.arff");

        data70.setClassIndex(data70.numAttributes() - 1);
        data30.setClassIndex(data30.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree1 = new J48();
        tree1.setOptions(options);
        tree1.buildClassifier(data70);

        //Pierwszy test
        Evaluation eval1 = new Evaluation(data30);
        eval1.evaluateModel(tree1, data30);
        System.out.println(eval1.toSummaryString("Results:", false));

        //Zapis klasyfikatora do pliku tree1.bin
        SerializationHelper.write("./src/podstawy/data/tree1.bin",tree1);

        System.out.println("-------------------------");

        //Odczytanie klasyfikatora
        J48 tree2 = (J48)SerializationHelper.read("./src/podstawy/data/tree1.bin");
        
        //Drugi test
        Evaluation eval2 = new Evaluation(data30);
        eval2.evaluateModel(tree2, data30);
        System.out.println(eval2.toSummaryString("Result:", false));
        

        //Powtorny zapis klasyfikatora do pliku tree2.bin
        SerializationHelper.write("./src/podstawy/data/tree2.bin",tree2);
    }

    //Regulacja czulosci i specyficznosci w oparciu o probki
    static public void testCostSensitiveClassifier()
    throws FileNotFoundException, IOException, Exception
    {
        
        
        Instances trainData = BasicTools.loadData("./src/podstawy/data/serceTrain.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/serceTest.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();        
        tree.setOptions(options);
        
        
        CostMatrix costMatrix = new CostMatrix(2);

        //Ustawienie wartości w macierzy kosztu
        costMatrix.setElement(0, 0, 0d); //Nie popelnil bledu
        costMatrix.setElement(1, 1, 0d); //Nie popelnil bledu
        costMatrix.setElement(0, 1, 1d); //było good, powiedzial bad (nie przyznal kredytu - mala strata)
        costMatrix.setElement(1, 0, 2d); //było bad, powiedzial good (przyznal zly kredyt - DUZA STRATA)
        
        CostSensitiveClassifier costSensitiveClassifier = new CostSensitiveClassifier();
         
        String[] optionsM = Utils.splitOptions("-M");
        //-M -> minimalizacja oczekiwanego kosztu bledow. (default is to reweight training instances according to costs per class) 
        //Bez parametru -M ->  według kosztów w klasie
        costSensitiveClassifier.setOptions(optionsM);
        
        costSensitiveClassifier.setClassifier(tree);
        costSensitiveClassifier.setCostMatrix(costMatrix);
        costSensitiveClassifier.buildClassifier(trainData);

        //Testowanie pierwszej tablicy
        Evaluation evalTrain = new Evaluation(trainData);
        
        
        evalTrain.evaluateModel(costSensitiveClassifier, testData);
        
        //To jest demonstracja, ze dziala takze dla CV
        //evalTrain.crossValidateModel(costSensitiveClassifier,trainData, 5,new Random(123)); //Wybor probek losowy
               
        int noDecClasses = trainData.classAttribute().numValues();        
               
       
        for (int i=0; i<noDecClasses; i++)
        {
                String decClassName = trainData.classAttribute().value(i);
        	System.out.println(decClassName+ " truePositiveRate="+evalTrain.truePositiveRate(i));	        
        }
        
    }

    //===============================================================================
    
    //Regulacja czulosci i specyficznosci w oparciu o mnoznik wagi w tescie TrainAndTest
    static public void testSensitivityEvaluationTrainAndTest()
    throws FileNotFoundException, IOException, Exception
    {        
        
        Instances trainData = BasicTools.loadData("./src/podstawy/data/serceTrain.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/serceTest.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        String[] options = Utils.splitOptions("-U -M 10");
        J48 tree = new J48();        
        tree.setOptions(options);
        tree.buildClassifier(trainData);
               
     
        //Testowanie tablicy testowej tradycyjna metoda (bez regulacji czulosci)(
        Evaluation eval1 = new Evaluation(trainData);
        eval1.evaluateModel(tree, testData);
        System.out.println(eval1.toSummaryString("Result1:", false));
        
        int noDecClasses = trainData.classAttribute().numValues();                       
     
        //Wypisanie czulosci(accuracy, truePositiveRate) dla poszczegolnych klas decyzyjnych
        for (int i=0; i<noDecClasses; i++)
        {
                String decClassName = trainData.classAttribute().value(i);
        	System.out.println(decClassName+ " truePositiveRate="+eval1.truePositiveRate(i));	        
        }
        
        System.out.println("===============================================");
        
        //TESTOWANIE Z MNOZNIKIEM (REGULACJA CZULOSCI USTALONEJ KLASY DECYZYJNEJ: "1")
        
        //Bedziemy testować z regulacja czulości klasy" "1"
        SensitivityEvaluation sEval = new SensitivityEvaluation(trainData,"2");
                            
        double multipier = 5.0; //To onacza, że wagę na wyselekcjonowanej wartosc decyzji zawsze przy klasyfikacji mnozymy przez multipier
        Evaluation eval2 = sEval.evaluateModel(tree, testData,multipier);
        
        System.out.println(eval2.toSummaryString("Result2:", false));
        
        //Wypisanie czulosci(accuracy, truePositiveRate) dla poszczegolnych klas decyzyjnych po regulacji za pomoca mnoznika
        for (int i=0; i<noDecClasses; i++)
        {
                String decClassName = trainData.classAttribute().value(i);
        	System.out.println(decClassName+ " truePositiveRate="+eval2.truePositiveRate(i));	        
        }        
        
    }

    //Regulacja czulosci i specyficznosci w oparciu o mnoznik wagi w tescie CVt
    static public void testSensitivityEvaluationCV()
    throws FileNotFoundException, IOException, Exception
    {        
        
        Instances data = BasicTools.loadData("./src/podstawy/data/serce.arff");
        data.setClassIndex(data.numAttributes() - 1);

               
        int numFolds = 10; //Liczba foldow        
        int numN = 5; //Liczba sasiadow
        long seed = 1234; //Ustalenie ziarna dla generatorow liczb pseudolosowych
       
        //Utworzenie struktury klasyfikatora, ktora bedzie kopiuowana i aktualizowana w trakcie testu CV
        IBk ibk1 = new IBk(numN);
        ibk1.buildClassifier(data);
        
        //Testowanie tablicy testowej standardowa metoda (BEZ REGULACJI CZULOSCI)        
        Evaluation eval1 = new Evaluation(data);
        eval1.crossValidateModel(ibk1,data, 10,new Random(seed)); 
        
        System.out.println(eval1.toSummaryString("Result1:", false));
                
        int noDecClasses = data.classAttribute().numValues();                       
     
        //Wypisanie czulosci(accuracy, truePositiveRate) dla poszczegolnych klas decyzyjnych
        for (int i=0; i<noDecClasses; i++)
        {
                System.out.println("---------------------------------------------------");
                String decClassName = data.classAttribute().value(i);
                System.out.println("Klasa decyzyjna="+decClassName);	                        
                System.out.println("truePositiveRate="+eval1.truePositiveRate(i));
                        	
                System.out.println("precision="+eval1.precision(i));
	        System.out.println("falseNegativeRate="+eval1.falseNegativeRate(i));
	        System.out.println("falsePositiveRate="+eval1.falsePositiveRate(i));
	        System.out.println("trueNegativeRate="+eval1.trueNegativeRate(i));	        
	        System.out.println("areaUnderROC="+eval1.areaUnderROC(i));                
        }
        
        System.out.println("=========================================================================");
        
        //TESTOWANIE Z MNOZNIKIEM (REGULACJA CZULOSCI USTALONEJ KLASY DECYZYJNEJ)
        
        //Bedziemy testować z regulacja czulości klasy" "2"
        
        SensitivityEvaluation sEval = new SensitivityEvaluation(data,"2");
                            
        double multipier = 1.0; //To onacza, że wagę na wyselkcjonowana wartosc decyzji zawsze przy klasyfikacji mnozymy przez multipier
                
        //Utworzenie struktury klasyfikatora, ktora bedzie kopiuowana i aktualizowana w trakcie testu CV
        IBk ibk2 = new IBk(numN);
        ibk2.buildClassifier(data);
        
        Evaluation eval2 = sEval.crossValidateModel(ibk1, data, numFolds, multipier, new Random(seed));
        System.out.println(eval2.toSummaryString("Result2:", false));
                
        //Wypisanie czulosci(accuracy, truePositiveRate) dla poszczegolnych klas decyzyjnych po regulacji za pomoca mnoznika
        for (int i=0; i<noDecClasses; i++)
        {
                System.out.println("---------------------------------------------------");
                String decClassName = data.classAttribute().value(i);
                System.out.println("Klasa decyzyjna="+decClassName);	                        
                System.out.println("truePositiveRate="+eval2.truePositiveRate(i));
        }        
        
    }

    
    // INNE KLASYFIKATORY *************************


    //Test train&test dla klasyfikatora NaiveBayes
    static public void trainAndTestNaiveBayes()
    throws FileNotFoundException, IOException, Exception
    {
        Instances trainData = BasicTools.loadData("./src/podstawy/data/irysy70.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/irysy30.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(trainData);

        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(nb, testData);
        System.out.println(eval.toSummaryString("Wyniki:", false));
    }

    
    //Test train&test dla klasyfikatora k-NN
    static public void trainAndTestKNN()
    throws FileNotFoundException, IOException, Exception
    {
        Instances trainData = BasicTools.loadData("./src/podstawy/data/serce.arff");
        trainData.setClassIndex(trainData.numAttributes() - 1);

        Instances testData = BasicTools.loadData("./src/podstawy/data/serce.arff");
        testData.setClassIndex(testData.numAttributes() - 1);

        IBk ibk = new IBk(3); //Dla k=3

        //Ustawienie odleglosci
        EuclideanDistance distance = new EuclideanDistance(); //euklidesowej
        //ManhattanDistance distance =  new ManhattanDistance(); //miejska              
        LinearNNSearch linearNN = new LinearNNSearch();        
        linearNN.setDistanceFunction(distance); //Ustwaienie odleglosci
        ibk.setNearestNeighbourSearchAlgorithm(linearNN); //ustawienie sposobu szukania sasiadow

        //Tworzenie klasyfikatora
        ibk.buildClassifier(trainData);

        Evaluation eval = new Evaluation(trainData);
        eval.evaluateModel(ibk, testData);
        System.out.println(eval.toSummaryString("Wyniki:", false));
    }


    //Generowanie regul asocjacyjnych
    public static void regulyAsocjacyjne()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/osmolski.arff");
        data.setClassIndex(data.numAttributes() - 1);
        
         //Opcje liczenia regul asocjacyjnych        
        //-N ->Liczba regul do policzenia (standardowo: 10)
        //-C ->Minmalna ufnosc reguly (standardowo: 0.9).        
        String[] options = Utils.splitOptions("-N 20 -C 0.6");
        Apriori apriori = new Apriori();
        apriori.setOptions(options);
        apriori.buildAssociations(data); //Generowanie regul asocjacyjnych

        System.out.println("Liczba regul=" + apriori.getNumRules());

                
        //===== POBRANIE INFORMACJI O REGULACH ========
        
        AssociationRules rules = apriori.getAssociationRules();
        List<AssociationRule> ruleList  = rules.getRules();
                             
        for (int i=0; i<ruleList.size(); i++)         
        {               
            AssociationRule rule = ruleList.get(i); //Pobranie pojedynczej reguly
            
            
            //Pobranie opisu poprzednika reguly
            Collection<Item> poprzednik = rule.getPremise();
            Iterator<Item> iteratorPoprzednik = poprzednik.iterator();
            String poprzednikText = new String();
            while (iteratorPoprzednik.hasNext()) 
            {
                poprzednikText = poprzednikText + "("+iteratorPoprzednik.next().toString()+")";
                if (iteratorPoprzednik.hasNext()) poprzednikText = poprzednikText +"&";
            }
             
            
            //Pobranie opisu nastepnika reguly
            Collection<Item> nastepnik = rule.getConsequence();
            Iterator<Item> iteratorNastepnik = nastepnik.iterator();
            String nastepnikText = new String();
            while (iteratorNastepnik.hasNext()) 
            {
                nastepnikText = nastepnikText + "("+iteratorNastepnik.next().toString()+")";
                if (iteratorNastepnik.hasNext()) nastepnikText = nastepnikText +"&";
            }
                        
            
            //Pobranie wsparcie i obliczenia ufnosci
            int wsparciePoprzednika = rule.getPremiseSupport();            
            int wsparcieCalosci = rule.getTotalSupport();            
            double ufnosc = (double)wsparcieCalosci/wsparciePoprzednika;
          
            
            System.out.print(poprzednikText+"=>"+nastepnikText+", ");
            System.out.print("Wsparcie:"+wsparcieCalosci+", ");
            System.out.println("Ufnosc:"+ufnosc);            
            
        }


       //To jest niezbyt czytelne, ale wiarygodne 
       //System.out.println(apriori.toString()); //Wypisanie informacji o regulach
    }


    //Generowanie regul asocjacyjnych z wczesniejsza dysretyzacja tablicy
    public static void regulyAsocjacyjneDyskret()
    throws Exception
    {
        Instances data = BasicTools.loadData("./src/podstawy/data/irysy.arff");
        data.setClassIndex(data.numAttributes() - 1);

        Discretize filter = new Discretize();

        filter.setInputFormat(data);

        //Dyskretyzacja tablicy
        Instances trainDiscret = Filter.useFilter(data, filter);

        //Opcje liczenia regul asocjacyjnych
        //-N -> Liczba reguł do wyliczenia
        //-C - minmalna ufnosc regul
        String[] options = Utils.splitOptions("-N 10 -C 0.9");

        Apriori apriori = new Apriori();
        apriori.setOptions(options);
        apriori.buildAssociations(trainDiscret);

        System.out.println("Liczba regul=" + apriori.getNumRules());

              
        System.out.println(apriori.toString());

    }
    

    public static void grupowanie()
    throws Exception
    {        
        Instances data1 = BasicTools.loadData("./src/podstawy/data/serceTrain.arff");
        
        ArrayList<String> attrToRemove = new ArrayList<>();
        attrToRemove.add("diagnoza");                
        data1 = BasicTools.removeColumns(data1,attrToRemove); //Usuwam atrybut decyzyjny

        //******** BUDOWA OBIEKTU GRUPUJACEGO DLA METODY kMeans ***********        
        
        /*
        SimpleKMeans clusterer = new SimpleKMeans();        
        //-N -> liczba srodkow
        //-S -> Inicjacja generatora liczb pseudolosowych
        String [] optionsKMeans = Utils.splitOptions("-N 4");// -S 1234");
        clusterer.setOptions(optionsKMeans);

        
        //===== Ustawienie odleglosci        
        clusterer.setDistanceFunction(new EuclideanDistance()); //Odleglosc euklidesowa
        //kMeans.setDistanceFunction(new ManhattanDistance()); //Odleglosc miejska
        
*/        
        //******** BUDOWA OBIEKTU GRUPUJACEGO DLA METODY EM ***********        
        
        
        EM clusterer = new EM();        
        //-I -> Maksymalna liczba iteracji algorytmu
        //-N -> Wymuszenie liczby grup (-1 - dobiera sam)
        //-S -> Inicjacja generatora liczb pseudolosowych
        String [] optionsEM = Utils.splitOptions("-I 100 -N -1");//6 -S 1234");        
        clusterer.setOptions(optionsEM);
        
        
        //===== Grupowanie
        clusterer.buildClusterer(data1);
        
        System.out.println(clusterer.toString()); //Wypisanie opisu grup
        
        System.out.println("Liczba skupien="+clusterer.getNumClusters()); //Dostep do liczby skupien
          
        //Instances centroids = clusterer.getClusterCentroids();        
        //System.out.println(centroids.toString());
        
        System.exit(0);
        
        
        //===== OBLICZANIE STATYSTYK DLA USTALONEGO SKUPIENIA
        int testedCluster = 1; //Uatalamy skupienie
        
        int plexIndex = 1; //Numer atrybutu plec (ustalamy atrybut symboliczny dla obliczania statystyki)
        int cisnienieIndex = 3; //Numer atrybutu cisnienie_krwi_spoczynek (ustalamy atrybut numeryczny dla obliczania statystyki)
        
        MultiSet plecStat = new MultiSet();        
        double min_cisnienie = -1.0; //Bedzie liczona wartosc minimalna atrybutu
        
        for (int i=0; i<data1.numInstances();i++)
        {
            Instance instance = data1.instance(i); //Pobranie wiersza tablicy
            
            int cluster = clusterer.clusterInstance(instance);  //Stwierdzenie do ktorego skupienia nalezy
            
            if (cluster==testedCluster) //Sprawdzanie, czy nalezy do ustalonego skupienia
            {
                String locPlec = instance.toString(plexIndex);                
                plecStat.addElem(locPlec); //Dodanie wasrtosci plci do statystyki
                
                double locCisnienie = instance.value(cisnienieIndex); 
                if (min_cisnienie<0.0) min_cisnienie=locCisnienie; //Wewryfikacja minimalnej waretosci cisnienia
                else
                    if (min_cisnienie>locCisnienie) min_cisnienie=locCisnienie;                
            }            
        }
        
        plecStat.print(); //Wypisanie statystyki dla atrybutu symbolicznego
        System.out.println("Minimalne ciśnienie="+min_cisnienie); //Wypisanie statystyki dla atrybutu numerycznego
        
        
        
        //===== TEST DLA DRUGIM ZBIORZE DANYCH
        
        //Odczytanie drugiego zbioru danych        
        Instances data2 = BasicTools.loadData("./src/podstawy/data/serceTest.arff");
        data2 = BasicTools.removeColumns(data2,attrToRemove); //Usuwam atrybut decyzyjny

                
        //Wypisanie informacji o przynaleznosci obiektow z drugiego zbioru danych do poszczegolnych skupien        
        int sumaKontrolna = 0; //Dla stwierdzenia zroznicowania wynikow przy roznych parametrach
        for (int i=0; i<data2.numInstances();i++)
        {
            Instance instance = data2.instance(i);
            int cluster = clusterer.clusterInstance(instance);            
            System.out.println(i+". Do skupienia: "+cluster);            
            sumaKontrolna = sumaKontrolna + cluster;            
        } 
        
        System.out.println("sumaKontrolna="+sumaKontrolna);
    }
}
