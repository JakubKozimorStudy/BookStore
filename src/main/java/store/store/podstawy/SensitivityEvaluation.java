package store.store.podstawy;

import java.util.HashMap;
import java.util.Random;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

public class SensitivityEvaluation 
{    
    private int selectedDecisionValueIndex; //Tutaj przechowujemy numer wartości atrybutu decyzyjnego z kolekcji takich wartosci 
    private double [] doubleDecValues; //Tutaj przechowujemy wartoscvi atrybutu decyzyjnego
    private Instances trainData; //Tutaj przechowujemy tablice treningowa

    public SensitivityEvaluation(Instances trainData, //Tablica treningowa
                                 String selectedDecValue //Wyselekcjonowana wartosc decyzji (do regulacji czulosci)
                                 )  throws Exception
    {    
        this.trainData = trainData; //Zapamietanie tablicy        
        
        //===== Tworzenie slownika wartosci decyzji i kodow tych wartosci typu double
        
        HashMap<String,Double> dictio = new HashMap<>(); //Slownika na wartosci decyzji
            
        
        int decAttrIndex = trainData.classIndex(); //Pobranie numeru atrybutu decyzyjnego w tablicy          
        
        for (int i=0; i<trainData.numInstances(); i++) //Przegladanie obiektow
        {               
            Instance instance = trainData.instance(i); //Pobranie obiektu                        
            String sDecVal = instance.toString(decAttrIndex); //Pobranie wartości decyzji w tym obiekcie w formie tekstowej
            double dDecVal = instance.value(decAttrIndex);  //Pobranie wartości decyzji w tym obiekcie w formie double                      
            if (!dictio.containsKey(sDecVal)) dictio.put(sDecVal, dDecVal);  //Dodanie pary do slownika          
        }
        
        //===================== koniec tworzenia slownika ===============================
                        
        int noDecClasses = trainData.classAttribute().numValues();  //Pobranie liczby wartosci decyzji                     
        
        doubleDecValues = new double[noDecClasses]; //Utworzenie tablicy dla przechowywania wartosci decyzji w formie double
               
        selectedDecisionValueIndex = -1; //trywialna wartosc
        
        for (int i=0; i<noDecClasses; i++)
        {
            String decValue = trainData.classAttribute().value(i); //Pobranie wartosci decyzji typu string
            double dDecValue = dictio.get(decValue); //Pobranie odpowiadajacej jej wartosci double
            doubleDecValues[i] = dDecValue; //ustawienie tej wartosci w pamieci
            
            if (decValue.equals(selectedDecValue)) //Zapamietanie numeru wysekcjonowanej wartosci decyzji
            {
                selectedDecisionValueIndex = i;
            }
        }
        
        //Obsluga bledu, gdy podano nie istniejaca wartosc decyzji
        if (selectedDecisionValueIndex<0) throw new IndexOutOfBoundsException("No decision value: "+selectedDecValue);        
        
    }
    
        
    //Klasyfikacja pojedynczego obiektu (instancji) z uwzglednieniem mnoznika
    public double [] classifyInstance(Classifier classifier,Instance instance,double multipier) throws Exception
    {   
        //Jesli multipier>0 to zmieniamy sile wyselekcjonowanej wartosci decyzji
        // wpp. zmieniamy sile innych wartosci decyzji
        
        if (Double.compare(multipier,0.0)==0) throw new IndexOutOfBoundsException("Multipier cannot be equal 0");        
                
        double multipierYes = 1;
        double multipierNo = 1;
        
        if (multipier>0) //wzmacniamy sile wyselekcjonowanej wartosci decyzji
        {
            multipierYes = multipier;
            multipierNo = 1;
        }
        else //wzmacniamy sile pozostalych wartosci decyzji
        {
            multipierNo = -multipier;
            multipierYes = 1;
        }
                
        double [] distribution = classifier.distributionForInstance(instance); //Klasyfikacja obiektu
        
        double sum = 0.0;
        
        //Modyfikacja wag za pomoca mnoznika
        for (int i=0; i<distribution.length; i++)
        {   
            if (i==selectedDecisionValueIndex) //To jest pozcycja wyselekcjonowanej wartosci decyzji 
            {
                distribution[i] = distribution[i] * multipierYes;
            }
            else
            {
                distribution[i] = distribution[i] * multipierNo;
            }
            
            sum = sum + distribution[i];
        }
        
                
        //Normalizacja zmodyfikowanych wag
        for (int i=0; i<distribution.length; i++)
        {            
            distribution[i] = distribution[i]/sum;            
        }
                       
            
        return distribution; //Zwracamy wartosc decyzji o maksymalnej wadze
    }
    
        
    //Test całej tablicy z uwzglednieniem mnoznika
    public Evaluation evaluateModel(Classifier classifier,Instances testData,double multipier)  throws Exception
    {                
        Evaluation evaluation = new Evaluation(trainData);
        for (int i = 0; i < testData.numInstances(); i++)
        {            
            double [] distribution = classifyInstance(classifier,testData.instance(i),multipier);  //Klasyfikacja obiektu z mnoznikiem                      
            evaluation.evaluateModelOnce(distribution,testData.instance(i)); //Uwzglednienie (WYMUSZENIE) wag posczegolnych wartosci decyzji w statystyce klasy Evaluation
        }
        return evaluation;
    }

    
    
    //Test metoda cross-validation z uwzglednieniem mnoznika
    public Evaluation crossValidateModel(Classifier classifier, Instances data, int numFolds, double multipier, Random random) throws Exception 
    {       
        Instances locData = new Instances(data);
        locData.randomize(random); //Tasujemy obiekty
        
        if (locData.classAttribute().isNominal()) locData.stratify(numFolds);
                           
        Evaluation evaluation = new Evaluation(data);
        
        for (int i = 0; i < numFolds; i++) 
        {
            Instances train = locData.trainCV(numFolds, i,random); //Pobranie tablicy treningowej dla danego podzialu                
            evaluation.setPriors(train);
                        
            Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier); //Skopiowanie struktury klasyfikatora
            copiedClassifier.buildClassifier(train); //Budowa klasyfikatora dla aktualnego podzialu
            
            Instances test = locData.testCV(numFolds, i); //Pobranie tablicy testowej dla danego podzialu                
                       
            for (int j = 0; j < test.numInstances(); j++)
            {   
                double [] distribution = classifyInstance(copiedClassifier,test.instance(j),multipier);  //Klasyfikacja obiektu z mnoznikiem                      
                evaluation.evaluateModelOnce(distribution,test.instance(j)); //Uwzglednienie (WYMUSZENIE) wag posczegolnych wartosci decyzji w statystyce klasy Evaluation
            }            
        }        
                
        return evaluation;
        
    }

    
}
