package agent;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgentCSVReader {

    //CSV file header
    private static final String[] FILE_HEADER_MAPPING = {"id", "name", "content", "label", "date", "resultat", "predicted"};

    //Student attributes
    private static final String AGENT_ID = "id";
    private static final String AGENT_NAME = "name";
    private static final String AGENT_CONTENT = "content";
    private static final String AGENT_LABEL = "label";
    private static final String AGENT_DATE = "date";

    public void readCsvFile(String fileName) {

        FileReader fileReader = null;

        CSVParser csvFileParser = null;

        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {

            //Create a new list of student to be filled by CSV file data
            List agents = new ArrayList();

            //initialize FileReader object
            fileReader = new FileReader(fileName);

            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);

            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = (CSVRecord) csvRecords.get(i);
                Agent agent = new Agent(record.get(AGENT_NAME), record.get(AGENT_CONTENT), record.get(AGENT_ID), record.get(AGENT_DATE), record.get(AGENT_LABEL));
                agents.add(agent);
            }

            //Print the new student list
            for (int i = 1; i < agents.size(); i++) {
                System.out.println(agents.get(i).toString());
            }

        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }

    }

}


