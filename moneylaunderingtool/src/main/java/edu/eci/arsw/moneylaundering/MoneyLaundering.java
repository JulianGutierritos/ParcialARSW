package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import edu.eci.arsw.moneylaundering.ProcessingThread;

public class MoneyLaundering
{
    static private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    static private int amountOfFilesTotal;
    static public AtomicInteger amountOfFilesProcessed;
    static private List<File> transactionFiles;
    static private int numhilo = 5;
    static private ProcessingThread[] hilos;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        amountOfFilesProcessed.set(0);
        transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        hilos = new ProcessingThread[numhilo];
    }

    public void processTransactionData(int inicio, int tamaño)
    {
        
        for(int i = 0 ; i < tamaño; i++)
        {            
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(inicio));
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            inicio+= 1;
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        System.out.println(getBanner());
        System.out.println(getHelp());
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        int inicio = 0;
        int tamaño = amountOfFilesTotal / numhilo;
        int res = amountOfFilesTotal % numhilo;
        for (int j = 0; j < numhilo; j++) {
            if (j+1 == numhilo){
                hilos[j] = new ProcessingThread(inicio, tamaño + res, transactionFiles, transactionAnalyzer, moneyLaundering);
            }
            else{
                hilos[j] = new ProcessingThread(inicio, tamaño, transactionFiles, transactionAnalyzer, moneyLaundering);
            }
            hilos[j].start();
            inicio+= tamaño;
        }
        //Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        //processingThread.start();
        while(amountOfFilesProcessed.get() < amountOfFilesTotal)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
            {
                System.exit(0);
            }
            else{
                for (int i = 0 ; i < numhilo ; i++){
                    hilos[i].pause();
                }
            }
            System.out.println("Pausado");
            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
            scanner.nextLine();
            if(line.contains("exit"))
            {
                System.exit(0);
            }
            else{
                for (int i = 0 ; i < numhilo ; i++){
                    hilos[i].resumeThread();
                }
            }
            System.out.println("Reunudado");
        }
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }

    private static String getBanner()
    {
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp()
    {
        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }
}