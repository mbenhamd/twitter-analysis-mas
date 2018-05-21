package agent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


public class AgentCSVWriter {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final Object[] FILE_HEADER = {"id", "name", "content", "label", "date", "resultat", "predicted"};

    public static void writeCsvFile(String fileName) {

        FileWriter fileWriter = null;

        CSVPrinter csvFilePrinter = null;

        //Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {

            //initialize FileWriter object
            fileWriter = new FileWriter(fileName);

            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            //Create CSV file header
            csvFilePrinter.printRecord(FILE_HEADER);

            //Write a new student object list to the CSV file
            for (int i = 0; i < AnalyseAgent.history.size(); i++) {
                Agent e = AnalyseAgent.history.get(i);
                List agentDataRecord = new ArrayList();
                agentDataRecord.add(e.getId());
                agentDataRecord.add(e.getNameAgent());
                agentDataRecord.add(e.getContentAgent());
                agentDataRecord.add(e.getLabelContent());
                agentDataRecord.add(e.getDate());
                agentDataRecord.add(AnalyseAgent.resultList.get(i));
                agentDataRecord.add(AnalyseAgent.predictedList.get(i));
                csvFilePrinter.printRecord(agentDataRecord);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }
}

