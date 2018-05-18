package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVProcessor {

    private static final String SEPERATOR = ";";
    private static File File​;

    static int defaultStartPage = 0;
    static int defaultEndPage = 5;
    static int page = 5;
    static StringBuilder tableLine = new StringBuilder();

    static String lastLine = "N(ext page), P(revious page), F(irst page), L(ast page), eX(it)";

    public static void main(String[] args) throws IOException, InterruptedException {


        List<String> csvFileContent = null;

        csvFileContent = retrieveCSVFile();

        final List<List<String>> csvData = processCSVLines(csvFileContent);

        List<String> outputArray = prepareOutput(csvData);

        String tableLine = renderTableLine(outputArray);

        String header = determineHeader(outputArray);

        System.out.println(header);
        System.out.println(tableLine.toString());
        System.out.println(lastLine);
        outputArray.remove(0);
        processUserInteraction(outputArray, header);

    }

    public static String determineHeader(List<String> outputArray) {
        String arrayHeader = outputArray.subList(0, 1).toString();
        return arrayHeader.substring(1, arrayHeader.length() - 1);
    }

    public static String renderTableLine(List<String> outputArray) {
        String outputLine = outputArray.get(0);

        for (int i = 0; i < outputLine.length(); i++) {

            tableLine.append("-");
        }
        return tableLine.toString();
    }

    public static List<String> retrieveCSVFile() throws IOException {
        Path path = Paths.get(".", "CSVTestFile.txt");


        if (path == null) {
            System.out.println("no inputFile with name CSVTestFile was found.");
            System.exit(1);

        } else {
            return Files.readAllLines(path);

        }
        return null;
    }

    public static List<String> prepareOutput(List<List<String>> csvData) throws IOException {

        ArrayList<Integer> maxLengthArray = new ArrayList<Integer>();

        int maxLengthEntry = 0;

        String outputLine = "";

        ArrayList<String> outputArray = new ArrayList<String>();

        for (List<String> line : csvData) {

            for (String entry : line) {

                maxLengthArray.add(entry.length());
            }
        }
        Collections.sort(maxLengthArray);

        int maxLength = maxLengthArray.get(maxLengthArray.size() - 1);

        for (List<String> line : csvData) {
            if (line.isEmpty() || line.get(0) == null) {
                System.out.println("CSV file includes empty lines or entries");
                System.exit(1);
            }

            outputLine = "";

            for (String entry : line) {

                int entryLength = entry.length();
                int paddingLength = maxLength - entryLength;
                String entryName = padding(entry, paddingLength, " ");

                outputLine = outputLine + entryName + "|";

            }

            outputArray.add(outputLine);

        }

        return outputArray;
    }

    public static void processUserInteraction(List<String> outputArray, String header) throws IOException {
        int beginPage;
        int stopPage;
        List<String> pageArray = null;
        beginPage = defaultStartPage;
        stopPage = defaultEndPage;

        String line = getUserInput();

        switch (line) {
            case "N":
                beginPage = beginPage + page;
                if (beginPage >= outputArray.size()) {
                    System.out.println("List finished.");
                    beginPage = beginPage - page;
                    pageArray = outputArray.subList(beginPage + 1, outputArray.size());
                    break;
                }
                stopPage = beginPage + page;
                if (stopPage < outputArray.size()) {
                    pageArray = outputArray.subList(beginPage + 1, stopPage + 1);
                    break;
                } else {
                    pageArray = outputArray.subList(beginPage + 1, outputArray.size());
                }
                ;
                break;
            case "P":
                if (beginPage - page <= defaultStartPage) {
                    pageArray = outputArray.subList(defaultStartPage, defaultEndPage + 1);
                    beginPage = defaultStartPage;
                    stopPage = defaultEndPage + 1;
                    break;
                } else {
                    pageArray = outputArray.subList(beginPage - page, stopPage - page);
                    break;
                }
            case "F":
                pageArray = outputArray.subList(defaultStartPage + 1, defaultEndPage + 1);
                break;
            case "L":
                pageArray = outputArray.subList((outputArray.size()) - page, outputArray.size());
                break;
            case "E":
                System.out.println("Goodbye!");
                System.exit(1);
            default:
                processUserInteraction(outputArray, header);
        }

        displayConsole(outputArray, header, pageArray);
    }

    public static void displayConsole(List<String> outputArray, String header, List<String> pageArray) throws IOException {
        System.out.println(header);
        System.out.println(tableLine.toString());
        outputContent(pageArray);
        System.out.println(lastLine);

        processUserInteraction(outputArray, header);
    }

    public static String getUserInput() {
        Scanner inputReader = new Scanner(System.in);

        return inputReader.nextLine();
    }


    private static void outputContent(List<String> pageArray) {
        for (String line : pageArray) {
            System.out.println(line);
        }

    }

    public static String padding(String str, int size, String padSymbol) {

        StringBuilder sb = new StringBuilder();

        if (str == null) {
            return null;
        }

        if (size <= 0) {
            return str; // returns original String when possible
        } else if (!(str.length() < 3)) {

            sb.append(str);
            fillpads(size, sb);
        } else {
            fillpads(size, sb);
            sb.append(str);
        }
        return sb.toString();
    }

    private static String fillpads(int size, StringBuilder sb) {
        for (int x = 0; x < size; x++) {
            sb.append(" ");
        }


        return sb.toString();
    }


    public static List<List<String>> processCSVLines(List<String> csvLines) throws IOException {

        List<List<String>> csvDatas = csvLines.stream().map(mapToItem).collect(Collectors.toList());

        return csvDatas;
    }


    private static Function<String, ArrayList<String>> mapToItem = (line) -> {
        String[] p = line.split(SEPERATOR);// a CSV has comma separated lines

        ArrayList<String> csvListItem = new ArrayList<>();

        for(String token:p) {

            if (token != null && token.trim().length() > 0) {
                csvListItem.add(token);
            }
        }

         return csvListItem;
    };
}
