import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import javax.xml.crypto.Data;
import java.lang.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class Iris{
        //Les elements ( Attributs ) de chaque Iris
        private Integer id;
        private double sepalLength;
        private double sepalWidth;
        private double petalLength;
        private double petalWidth;
        private Integer irisType;
        //Constructeur de Iris avec des paramétres
        public Iris(Integer id,Integer irisType, double sepalLength, double sepalWidth,
         double petalLength, double petalWidth) {
            this.id = id;
            this.sepalLength = sepalLength;
            this.sepalWidth = sepalWidth;
            this.petalLength = petalLength;
            this.petalWidth = petalWidth;
            this.irisType = irisType;
        }
        //les methodes d'Iris
        public Integer getId(){
            return this.id;
        }
        public double getsepalLength() {
            return this.sepalLength;
        }
        public double getsepalWidth() {
            return this.sepalWidth;
        }
        public double getpetalLength() {
            return this.petalLength;
        }
        public double getpetalWidth() {
            return this.petalWidth;
        }
        public Integer getIrisType() {
            return irisType;
        }
        public void setId(int id) {
            this.id= id ;
        }
        public void setIrisType( Integer irisType) {
            this.irisType = irisType;
        }
        @Override
        public String toString() {
            return this.id + "," +
                this.sepalLength + "," +
                this.sepalWidth + "," +
                this.petalLength + "," +
                this.petalWidth + "," +
                this.irisType;
        }
        //Methode pour lire le fichier Csv
        public static List<Iris> DataFrame(String file){
            String ligne;
            List<Iris> iris = new ArrayList<Iris>();

            try {
            BufferedReader lireData = new BufferedReader(new FileReader(file));
                while((ligne = lireData.readLine()) != null){
                   String[] data = ligne.split("\\s+");//rompt les chaines de ligne autour l'expression régulière ","
                   Iris ajouterIris = new Iris(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Double.parseDouble(data[2]),Double.parseDouble(data[3]),
                   Double.parseDouble(data[4]),Double.parseDouble(data[5]));
       
                   iris.add(ajouterIris);
                }
            lireData.close();
            } catch (Exception e) {
            e.printStackTrace();
             }
            return iris;
        }
        //Construire train_data appartir 60% des données Iris + initialiser les ids
        public static List<Iris> getTrain_Data(List<Iris> dataOfIris){
        List<Iris> train_Data = new ArrayList<Iris>();
        train_Data.addAll(dataOfIris.subList(0,30));
        train_Data.addAll(dataOfIris.subList(50,80));
        train_Data.addAll(dataOfIris.subList(100,130));
        int j = 0;
        for(int i = 0; i < train_Data.size(); i++){
            train_Data.get(i).setId(j);
            j++;
        }
        return train_Data;
        }
        //Construire test_data appartir 40% des données Iris + initialiser les ids
        public static List<Iris> getTest_Data(List<Iris> dataOfIris){
        List<Iris> test_Data = new ArrayList<Iris>();
        test_Data.addAll(dataOfIris.subList(31,49));
        test_Data.addAll(dataOfIris.subList(81,99));
        test_Data.addAll(dataOfIris.subList(131,150));
        int j=0;
        for(int i = 0; i < test_Data.size(); i++){
            test_Data.get(i).setId(j);
            j++;
        }
        return test_Data;
        }
       //-------------------------------------------------------------------Modèle--------------------------------------------------------------------
        public static int bayesIris(List<Iris> train_Data,Iris IrisForTest){
            //Tache 1 : triée les quatres attributs selon les trois classes alors le resultat est une HashMap avec 12 Keys 
            Map<String,List<Double>> map =  new HashMap<>();
            for(int first=0;first<train_Data.size();first++){
                //lire Iris de chaque ligne dans train_Data
                Iris getIris = new Iris(train_Data.get(first).getId(),train_Data.get(first).getIrisType(),train_Data.get(first).getsepalLength(),train_Data.get(first).getsepalWidth(),
                train_Data.get(first).getpetalLength(),train_Data.get(first).getpetalWidth());
                //Ajouter les colonnes sepalLength dans map avec les Keys suivants :Iris-setosa_sepalLength,Iris-versicolor_sepalLength,Iris-virginica_sepalLength
                String sepalLengthOf = getIris.getIrisType()+"_sepalLength";
                      organisation(map,getIris.getsepalLength(),sepalLengthOf);
                //Ajouter les colonnes sepalWidth dans map avec des leys suivants :Iris-setosa_sepalWidth,Iris-versicolor_sepalWidth,Iris-virginica_sepalWidth
                String sepalWidthOf = getIris.getIrisType()+"_sepalWidth";
                      organisation(map,getIris.getsepalWidth(),sepalWidthOf);
                //Ajouter les colonnes petalLength dans map avec les Keys suivants :Iris-setosa_petalLength,Iris-versicolor_petalLength,Iris-virginice_petalLength
                String petalLengthOf = getIris.getIrisType()+"_petalLength";
                      organisation(map,getIris.getpetalLength(),petalLengthOf);
                //Ajouter les colonnes petalWidth dans map avec les Keys suivants :Iris-setosa_petalWidth,Iris-versicolor_petalWidth,Iris-virginica_petalWidth
                String petalWidthOf = getIris.getIrisType()+"_petalWidth";
                      organisation(map,getIris.getpetalWidth(),petalWidthOf);
            }      
            //Tache 2 : Calcule : Moyenne et Variance après calculee la fonction de densité de chaque Keys et stocker cette fonction dans Map1
            Map<String,Double> map1 = new HashMap<>();
            for(Map.Entry<String,List<Double>> entity:map.entrySet()){
                //2-1 calculer le moyenne et la variance appartir le Hashmap : map
                List<Double> values = entity.getValue();
                DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();
                Double mean = 0.0;
                for(Double value:values){
                    doubleSummaryStatistics.accept(value);
                }
                //Calcule : Moyenne
                mean = doubleSummaryStatistics.getAverage();
                Double variance = 0.0;
                for(double value:values){
                Double v = value - mean;
                variance = variance + Math.pow(v, 2);
                }
                //Calcule : Variance
                variance = variance / (values.size()-1);
            
                Double source = 0.0;
                         if(entity.getKey().contains("sepalLength")){
                source = IrisForTest.getsepalLength();
                         }if(entity.getKey().contains("sepalWidth")){
                source = IrisForTest.getsepalWidth();
                         }if(entity.getKey().contains("petalLength")){
                source = IrisForTest.getpetalLength();
                         }if(entity.getKey().contains("petalWidth")){
                source = IrisForTest.getpetalWidth();
                         }
                //calculer Fonction de Donsité f(x) = [1/√(2π)] exp{-(x-μ)²/2σ²}
                Double p = 1f / ( Math.sqrt( 2.0 * Math.PI * variance ) );
                Double exp = Math.exp( -0.5 * (Math.pow(source - mean, 2) / variance) );
                Double density = Math.log10(p * exp);
                map1.put(entity.getKey(),density);
            }
            //Tache3 : Calculee perior et le posterior de chaque classe
            List<Integer> type = new ArrayList<Integer>();
            for(int i = 0;i < train_Data.size();i++){
                if(type.contains(train_Data.get(i).irisType) == false){
                    type.add(train_Data.get(i).irisType);
                }
            }
            List<List<Object>> result = new ArrayList<List<Object>>();
            for(int i = 0;i < type.size();i++){
                result.add(new ArrayList<Object>());
                result.get(i).add(type.get(i));
            }
            
            for(int i = 0;i < result.size();i++){
                Double count = 0.0;
                for(int j = 0;j < train_Data.size();j++){
                    if(((Integer) result.get(i).get(0)).intValue() == train_Data.get(j).irisType){
                        count++;
                    }
                }
                Double perior = Math.log10(Double.valueOf(count / train_Data.size()));
                result.get(i).add(perior);
            }
            for(int i = 0;i < result.size();i++){
                String one = new StringBuilder().append(String.valueOf(result.get(i).get(0))).append("_sepalLength").toString();
                String two = new StringBuilder().append(String.valueOf(result.get(i).get(0))).append("_sepalWidth").toString();
                String tree = new StringBuilder().append(String.valueOf(result.get(i).get(0))).append("_petalLength").toString();
                String fore = new StringBuilder().append(String.valueOf(result.get(i).get(0))).append("_petalWidth").toString();
                Double res = (((Number) result.get(i).get(1)).doubleValue()+(map1.get(one))+(map1.get(two))+(map1.get(tree))+(map1.get(fore)));
                result.get(i).add(res);
            }
            //Comparaison les 3 probabilités et affichee le Max
            int index1 = 0;
            Double max = ((Number) result.get(0).get(2)).doubleValue();
            for(int i = 1;i < result.size();i++){
                if(((Number) result.get(i).get(2)).doubleValue() > max ){
                    max = ((Number) result.get(i).get(2)).doubleValue();
                    index1 = i;
                }
            }
            return ((Integer) result.get(index1).get(0)).intValue();
        }
        //Fonction organisation utilisee dans la tache 1 pour stocker les colonnes avec les 12 Keys
        public static Map<String,List<Double>>  organisation(Map<String,List<Double>> map ,Double att,String key){

            if(map.containsKey(key)){
                List<Double> list = map.get(key);
                list.add(att);
                map.put(key,list);
            }else{
                List<Double> list = new ArrayList<>();
                list.add(att);
                map.put(key,list);
            }
            return map;
        }
        //---------------------------------------------------------fin------------------------------------------------------------------------
        public static void main(String[] args) {
        List<Iris> Data = new ArrayList<Iris>();
        List<Iris> TrainData = new ArrayList<Iris>();
        List<Iris> TestData = new ArrayList<Iris>();
        Data = DataFrame("C:/Users/ISSAM/OneDrive/Bureau/PFE/ImplementationNB/Dataset/iris");
        TrainData = getTrain_Data(Data);
        TestData = getTest_Data(Data);
        Double count = 0.0;
        //TRAIN DATA
        System.out.println("TRAIN DATA -----------------------------");
        for(int size = 0;size<TrainData.size();size++){
            System.out.println(TrainData.get(size).toString());
        }
        //TEST DATA
        System.out.println();
        System.out.println("TEST DATA -----------------------------");
        for(int size = 0;size<TestData.size();size++){
            System.out.println(TestData.get(size).toString());
        }
        System.out.println();
        System.out.println("---------------------------------------");
        for(int size = 0;size<TestData.size();size++){
            Iris forTest = new Iris(null,null,TestData.get(size).getsepalLength()
            ,TestData.get(size).getsepalWidth()
            ,TestData.get(size).getpetalLength(),TestData.get(size).
            getpetalWidth());
            if((bayesIris(TrainData, forTest)) == TestData.
            get(size).getIrisType()){
                count++;
            }
        }            
        Double nb = (double)(TestData.size());
        System.out.println("Precision de notre modele est : "+ count / nb*100.0+"%"); 
        
    }
}