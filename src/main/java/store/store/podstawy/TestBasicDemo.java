package store.store.podstawy;


import weka.core.Instances;

/**
 * * Klasa testuje podstawowe funkcjonalności z klasy BasicDemo
 * 
 */
public class TestBasicDemo 
{
 public static void main(String[] args)
    {
        System.out.println("Test WEKA");
        try
        {
            String fileName = "./src/podstawy/data/irysy.arff"; //Lokalizacja pliku z danymi
            Instances dataSet = BasicTools.loadData(fileName);
            int noObj = dataSet.numInstances(); //Pobranie informacji o liczbie obiektow
            int noAttr = dataSet.numAttributes(); //Pobranie informacji o liczbie atrybutow
            System.out.println("Liczba obiektow=" + noObj);
            System.out.println("Liczba atrybutow="+noAttr);             
                        
//            System.out.println("import z csv do arff...");
//            importCSVtoARFF("./src/data/irysy.csv","./src/data/irysy3.arff");


//            System.out.println("infoObj...");
//            BasicDemo.infoObj();
//            System.out.println("infoAttr...");
//            BasicDemo.infoAttr();
//            System.out.println("infoObjZmiana...");
//            BasicDemo.infoObjZmiana();
//            System.out.println("liczStatystyki...");
//            BasicDemo.liczStatystyki();
//            System.out.println("nowaTablica...");
//            BasicDemo.nowaTablica();
//            System.out.println("podzial...");
//            BasicDemo.podzial();
//            System.out.println("filtrowanieAtrybutow...");
//            BasicDemo.filtrowanieAtrybutow();
            
                            
//            BasicDemo.dyskretyzacja();


//            BasicDemo.buidClassifierJ48();
//            BasicDemo.trainAndTestJ48_1();
//            BasicDemo.trainAndTestJ48_2();
//            BasicDemo.trainAndTestJ48_3();
//            BasicDemo.crossValidationJ48();
//            BasicDemo.klasyfikacjaNowychPrzypadkow();            
//            BasicDemo.testZapisuKlasyfikatora();
                       


//            BasicDemo.trainAndTestNaiveBayes(); 
            
//            BasicDemo.trainAndTestKNN();            



//            BasicDemo.regulyAsocjacyjne();
           
//            BasicDemo.regulyAsocjacyjneDyskret();
            
            
//            BasicDemo.grupowanie();
            


//            BasicDemo.testSensitivityEvaluationTrainAndTest();
            
//            BasicDemo.testSensitivityEvaluationCV();
            
                      
            
        }
        catch (Exception e)
        {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println("KONIEC");
    }
   
}
