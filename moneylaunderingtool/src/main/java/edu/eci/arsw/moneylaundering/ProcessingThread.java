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

public class ProcessingThread extends Thread{
    private int inicio;
    private int tamaño;
    private List<File> transactionFiles;
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private MoneyLaundering moneyLaundering;
    private boolean detener = false;

    public ProcessingThread (int inicio, int tamaño, List<File> transactionFiles, TransactionAnalyzer transactionAnalyzer, MoneyLaundering moneyLaundering){
        this.inicio = inicio;
        this.tamaño = tamaño;
        this.transactionFiles = transactionFiles;
        this.transactionAnalyzer = transactionAnalyzer;
        transactionReader = new TransactionReader();
        this.moneyLaundering = moneyLaundering;
    }

    @Override
	public void run() {

		try {
			processTransactionData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

    public void processTransactionData() throws InterruptedException
    {
        
        for(int i = 0 ; i < tamaño; i++)
        {            
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(inicio));
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            inicio+= 1;
            moneyLaundering.amountOfFilesProcessed.incrementAndGet();
            synchronized(this){
				while(detener){
					wait();
				}
			}
        }
    }

    public void pause() {
		detener=true;
	}

	public void resumeThread() {
		detener = false;
		synchronized(this){
			notify();
		}
	}
}
