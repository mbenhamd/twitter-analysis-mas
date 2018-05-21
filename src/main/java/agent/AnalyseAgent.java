package agent;

import com.github.habernal.confusionmatrix.ConfusionMatrix;
import io.indico.Indico;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.utils.IndicoException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalyseAgent {
    static public List<Agent> history = new ArrayList<Agent>();
    static public  List<String> realList = new ArrayList<String>();
    static public  List<String> predictedList = new ArrayList<String>();
    static public  List<String> resultList = new ArrayList<String>();

    public AnalyseAgent(){
    }

    public void main(){
        Indico indico = null;
        try {
            indico = new Indico("b158a5cd9613563c38d38afc6bc345c3");
        } catch (IndicoException e) {
            e.printStackTrace();
        }


        List<String> example = new ArrayList<String>();

        for (int i =0;i<AnalyseAgent.history.size();i++){
            String text = AnalyseAgent.history.get(i).getContentAgent();
            example.add(text);
        }

        BatchIndicoResult multiple = null;
        try {
            multiple = indico.sentimentHQ.predict(example);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndicoException e) {
            e.printStackTrace();
        }

        List<Double> results = null;
        try {
            results = multiple.getSentimentHQ();
        } catch (IndicoException e) {
            e.printStackTrace();
        }


        for (int i =0; i < history.size();i++) {
            System.out.println(new StringBuilder().
                    append("ID: ").
                    append(AnalyseAgent.history.get(i).getId()).
                    append(" | ").
                    append("Message Date: ").
                    append(AnalyseAgent.history.get(i).getDate()).
                    append(" | ").
                    append("Agent: ").
                    append(AnalyseAgent.history.get(i).getNameAgent()).
                    append(" | ").
                    append("Content: ").
                    append(AnalyseAgent.history.get(i).getContentAgent()).
                    append(" | ").
                    append("Label: ").
                    append(AnalyseAgent.history.get(i).getLabelContent()).
                    append(" | ").
                    append("Resultat: ").append(results.get(i)).
                    append(" | ").
                    append("Predicted: ").append(resltToString(results.get(i))).toString());
                    realList.add(history.get(i).getLabelContent());
                    predictedList.add(resltToString(results.get(i)));
                    resultList.add(results.get(i).toString());
        }
        calcul();
    }

    public int getResultCM(String real,String predicted){
        int indice1 =0;
        int indice2 =0;
        int indice3 =0;
        if(real==predicted){
            for(int i =0;i <history.size();i++){
                if((realList.get(i) == real) && (predictedList.get(i)==predicted)) indice1++;
            }
            return indice1;
        }
        else if(real!=predicted){
            for(int i =0;i <history.size();i++){
                if((realList.get(i) ==real ) && (real!=predicted) && (predictedList.get(i)==predicted)  ) indice2++;
            }
            return indice2;
        }
        else{
            for(int i =0;i <history.size();i++){
                if((realList.get(i) !=real ) && (real!=predicted) && (predictedList.get(i)==predicted)  ) indice3++;
            }
            return indice3;
        }
    }


    public String resltToString(Double r){
        if(r<1.0/3.0) return "Negative";
        else if(r>=1.0/3.0 && (r<(1.0/3.0)*2.0))  return "Neutral";
        else return "Positive";
    }

    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


    public void calcul(){
        ConfusionMatrix cm = new ConfusionMatrix();

        cm.increaseValue("Negative", "Negative",getResultCM("Negative","Negative"));
        cm.increaseValue("Negative", "Neutral",getResultCM("Negative","Neutral"));
        cm.increaseValue("Negative", "Positive",getResultCM("Negative","Positive"));
        cm.increaseValue("Neutral", "Positive",getResultCM("Neutral","Positive"));
        cm.increaseValue("Neutral", "Negative",getResultCM("Neutral","Negative"));
        cm.increaseValue("Neutral", "Neutral",getResultCM("Neutral","Neutral"));
        cm.increaseValue("Positive", "Positive",getResultCM("Positive","Positive"));
        cm.increaseValue("Positive", "Neutral",getResultCM("Positive","Neutral"));
        cm.increaseValue("Positive", "Negative",getResultCM("Positive","Negative"));

        System.out.println(cm);
        System.out.println(cm.printLabelPrecRecFm());
        System.out.println(cm.getPrecisionForLabels());
        System.out.println(cm.getRecallForLabels());
        System.out.println(cm.printNiceResults());
        System.out.println("Accuracy: " +cm.getAccuracy()*100.0+" %");
        System.out.println("Total element: "+cm.getTotalSum());

        AgentCSVWriter acsv = new AgentCSVWriter();
        acsv.writeCsvFile("Result.csv");


    }

}
