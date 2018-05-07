package com.company;

import java.io.*;
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
    static StringBuilder tableLine = new StringBuilder();


    public static void main(String[] args) throws IOException, InterruptedException {

        int page = 5;

        int beginPage = 0;
        int stopPage = 5;

        File[] files = new File(".").listFiles();

        File inputFile = null;
        List<CSVItem> csvItems = null;
        for (File file : files) {
            if (file.isFile() && file.getName().contains("CSVTestFile")) {
                inputFile = file;
            }
        }

        if (inputFile == null) {
            System.out.println("no inputFile with name CSVTestFile was found.");
        } else {
            csvItems = new CSVProcessor().processInputFile(inputFile.getName());
        }
        int maxLengthEntry = 0;
        ArrayList<Integer> maxLengthNameArray = new ArrayList<Integer>();
        ArrayList<Integer> maxLengthAgeArray = new ArrayList<Integer>();
        ArrayList<Integer> maxLengthCityArray = new ArrayList<Integer>();

        ArrayList<String> outputArray = new ArrayList<String>();

        prepareViewer(page, defaultStartPage, defaultEndPage, csvItems, maxLengthNameArray, maxLengthAgeArray, maxLengthCityArray, outputArray);

    }

    private static void prepareViewer(int page, int startPage, int endPage, List<CSVItem> csvItems, ArrayList<Integer> maxLengthNameArray, ArrayList<Integer> maxLengthAgeArray, ArrayList<Integer> maxLengthCityArray, ArrayList<String> outputArray) throws IOException {
        for (CSVItem item : csvItems) {

            maxLengthNameArray.add(item.getName().length());
            maxLengthAgeArray.add(item.getAge().length());
            maxLengthCityArray.add(item.getCity().length());
        }
        Collections.sort(maxLengthNameArray);
        Collections.sort(maxLengthAgeArray);
        Collections.sort(maxLengthCityArray);


        int maxNameLength = maxLengthNameArray.get(maxLengthNameArray.size() - 1);
        int maxAgeLength = 3;
        int maxCityLength = maxLengthCityArray.get(maxLengthCityArray.size() - 1);


        for (CSVItem item : csvItems) {

            int entryNameLength = item.getName().length();
            int paddingNameLength = maxNameLength - entryNameLength;
            int entryCityLength = item.getCity().length();
            int paddingCityLength = maxCityLength - entryCityLength;
            int entryAgeLength = item.getAge().length();
            int paddingAgeLength = maxAgeLength - entryAgeLength;

            String entryName = rightPadding(item.getName(), paddingNameLength, " ");
            String entryAge = rightPadding(item.getAge(), paddingAgeLength, " ");
            String entryCity = rightPadding(item.getCity(), paddingCityLength, " ");

            String outputLine = entryName + "|" + entryAge + "|" + entryCity + "|";

            outputArray.add(outputLine);
            System.out.println(outputLine);


            if (item.getName().equalsIgnoreCase("Name")) {

                int maxLineLength = entryName.length() + entryAge.length() + entryCity.length() + 3;

                for (int x = 0; x < maxLineLength; x++) {

                    tableLine.append("-");

                }
            }
            System.out.print("\n");
        }

        String lastLine = "N(ext page), P(revious page), F(irst page), L(ast page), eX(it)";

        System.out.println(lastLine);

        List<String> pageArray = outputArray.subList(startPage + 1, endPage + 1);

        pageOutput(page, startPage, endPage, outputArray);
    }

    private static void pageOutput(int page, int startPage, int endPage, ArrayList<String> outputArray) throws IOException {
        int beginPage;
        int stopPage;
        List<String> pageArray = null;
        beginPage = startPage;
        stopPage = endPage;

        Scanner inputReader = new Scanner(System.in);

        String line = inputReader.nextLine();

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
                pageOutput(page, beginPage, stopPage, outputArray);
        }

        outputContent(outputArray.subList(0,1));
        System.out.println(tableLine.toString());
        outputContent(pageArray);
        String lastLine = "N(ext page), P(revious page), F(irst page), L(ast page), eX(it)";
        System.out.println(lastLine);

        pageOutput(page, beginPage, stopPage, outputArray);
    }


    private static void outputContent(List<String> pageArray) {
        for (String line : pageArray) {
            System.out.println(line);
        }

    }

    public static String rightPadding(String str, int size, String padSymbol) {

        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (str == null) {
            return null;
        }

        if (size <= 0) {
            return str; // returns original String when possible
        } else {
            for (int x = 0; x < size; x++) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }


    private List<CSVItem> processInputFile(String inputFilePath) throws IOException {

        File inputF = new File(inputFilePath);
        InputStream inputFS = new FileInputStream(inputF);

        List<CSVItem> inputList = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            {

                inputList = br.lines().map(mapToItem).collect(Collectors.toList());
                br.close();
            }
        } catch (IOException ioe) {

        }
        return inputList;
    }


    private Function<String, CSVItem> mapToItem = (line) -> {
        String[] p = line.split(SEPERATOR);// a CSV has comma separated lines
        CSVItem item = new CSVItem();

        if (p[0] != null && p[0].trim().length() > 0) {
            item.setName(p[0]);
        }

        if (p[1] != null && p[1].trim().length() > 0) {
            item.setAge(p[1]);
        }
        if (p[2] != null && p[2].trim().length() > 0) {
            item.setCity(p[2]);
        }


        //more initialization goes here
        return item;
    };
}
