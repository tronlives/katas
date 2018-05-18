package com.company;

import static org.junit.jupiter.api.Assertions.*;
import com.company.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CSVProcessorTest {

    CSVProcessor csvProcessor;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {



    }

    @Test
    void testDetermineHeader() {

        List<String> outputArray = new ArrayList<String>();

      outputArray.add("Name  |Age   |City  |");
      outputArray.add("Mary  |    35|Munich|");

      String header = CSVProcessor.determineHeader(outputArray);

      assertEquals(outputArray.get(0),header);

    }

    @org.junit.jupiter.api.Test
    void testRenderTableLine() {

        List<String> outputArray = new ArrayList<String>();

        outputArray.add("Name  |Age   |City  |");
        outputArray.add("Mary  |    35|Munich|");

        String outputLineActual = CSVProcessor.renderTableLine(outputArray);

        String outputLineExpected = "---------------------";

        assertEquals(outputLineExpected, outputLineActual);

    }

    @org.junit.jupiter.api.Test
    void testRetrieveCSVFile() {

        try {
            List<String> csvData = CSVProcessor.retrieveCSVFile();

         assertEquals(12,csvData.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void testPrepareOutput() {


       List<String> preparedOutputArrayExpected = new ArrayList<String>();

        preparedOutputArrayExpected.add("Name  |Age   |City  |");
        preparedOutputArrayExpected.add("Mary  |    35|Munich|");

        List<List<String>> expectedLines = new ArrayList<>();

        List<String> expectedCSVEntryLine1 = new ArrayList<String>();
        List<String> expectedCSVEntryLine2 = new ArrayList<String>();

        expectedCSVEntryLine1.add("Name");
        expectedCSVEntryLine1.add("Age");
        expectedCSVEntryLine1.add("City");
        expectedCSVEntryLine2.add("Mary");
        expectedCSVEntryLine2.add("35");
        expectedCSVEntryLine2.add("Munich");



        expectedLines.add(expectedCSVEntryLine1);
        expectedLines.add(expectedCSVEntryLine2);


        List<List<String>> outputArray = new ArrayList<>();

        String header="";

        try {
            List<String> preparedOutputArrayActual = CSVProcessor.prepareOutput(expectedLines);

            assertEquals(preparedOutputArrayExpected,preparedOutputArrayActual);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @org.junit.jupiter.api.Test
    void testPadding() {

       String expectedPaddedCSVEntryName="Peter    ";
        String expectedPaddedCSVEntryAge="      42";


        String paddedCSVEntryName= CSVProcessor.padding("Peter",4," ");
       String paddedCSVEntryAge= CSVProcessor.padding("42",6," ");


        assertEquals(expectedPaddedCSVEntryName,paddedCSVEntryName);
        assertEquals(expectedPaddedCSVEntryAge,paddedCSVEntryAge);
    }

    @org.junit.jupiter.api.Test
    void testProcessCSVLines() {

        List<String> csvFileContent = new ArrayList<String>();

        csvFileContent.add("Name;Age;City");
        csvFileContent.add("Peter;42;NewYork");
        csvFileContent.add("Paul;57;London");

        List<List<String>> expectedLines = new ArrayList<>();

        List<String> expectedCSVEntryLine1 = new ArrayList<String>();
        List<String> expectedCSVEntryLine2 = new ArrayList<String>();
        List<String> expectedCSVEntryLine3 = new ArrayList<String>();

        expectedCSVEntryLine1.add("Name");
        expectedCSVEntryLine1.add("Age");
        expectedCSVEntryLine1.add("City");
        expectedCSVEntryLine2.add("Peter");
        expectedCSVEntryLine2.add("42");
        expectedCSVEntryLine2.add("NewYork");
        expectedCSVEntryLine3.add("Paul");
        expectedCSVEntryLine3.add("57");
        expectedCSVEntryLine3.add("London");

        expectedLines.add(expectedCSVEntryLine1);
        expectedLines.add(expectedCSVEntryLine2);
        expectedLines.add(expectedCSVEntryLine3);


        try {
            final List<List<String>> csvData = CSVProcessor.processCSVLines(csvFileContent);

            assertEquals(expectedLines,csvData);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}