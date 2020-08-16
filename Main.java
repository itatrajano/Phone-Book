package phonebook;

import java.io.File;
import java.util.Scanner;



public class Main {

    static String readableTime(double timeInMilliseconds) {
        double min;
        double sec;
        double ms;
        double mod;
        double timeRemaining;

        //Calcular minutos
        mod = timeInMilliseconds % 60_000; // "mod" é a parte que sobra para ser calculada nos blocos abaixo
        timeRemaining = timeInMilliseconds - mod;
        min = timeRemaining / 60_000;
        timeRemaining = mod;

        //Calcular segundos
        mod = timeRemaining % 1_000;
        timeRemaining = timeRemaining - mod;
        sec = timeRemaining / 1_000;
        timeRemaining = mod;

        //Milisegundos
        ms = timeRemaining;

        /* //Esse bloco somente exibirá a unidade se esta for maior que zero.
        String minString = (min == 0.0) ? ("") : (min + " min. ");
        String secString = (sec == 0.0) ? ("") : (sec + " sec. ");
        String msString = (ms == 0.0) ? ("") : (ms + " ms.");
         */

        //Este bloco exibirá a unidade em qualquer situação.
        String minString = (int)min + " min. ";
        String secString = (int)sec + " sec. ";
        String msString = (int)ms + " ms.";

        return minString + secString + msString;
    }

    public static void main(String[] args) {

        String findLocation = "D:\\partes de agenda telefonica\\find.txt";
        String directoryLocation = "D:\\partes de agenda telefonica\\directory.txt";

        File find = new File(findLocation);
        File directory = new File(directoryLocation);

       //Cria o Array com 500 strings
        String[] findArray = new String[500];
        String[] directoryArray = new String[1014130];

        //LEITURA DOS ARQUIVOS
        //Transfere cada linha do arquivo "find.txt" para o "findArray[]"
        try(Scanner scanner = new Scanner(find)) {
            int arrayCounter = 0;
            while (scanner.hasNext()) {
                findArray[arrayCounter] = scanner.nextLine();
                arrayCounter++;
            }
        } catch (Exception e) {
            System.out.println("Erro de Leitura de " + findLocation + " - " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }

        //Transfere cada linha do arquivo "directory.txt" para o "directoryArray[]"
        try(Scanner scanner = new Scanner(directory)) {
            int arrayCounter = 0;
            while (scanner.hasNext()) {
                directoryArray[arrayCounter] = scanner.nextLine();
                arrayCounter++;
            }
        } catch (Exception e) {
            System.out.println("Erro de Leitura de " + directoryLocation + " - " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }

        //INICIA O ALGORITMO DE BUSCA
        System.out.println("Start searching...");
        double timeOfStart = System.currentTimeMillis();
        int matchesCounter = 0;
        //Compara cara elemento de findArray[] com todos os elementos de directoryArray[]"
        for (int i = 0; i < findArray.length; i++) {
            for (int j = 0; j < directoryArray.length; j++) {
                if (directoryArray[j].contains(findArray[i])) {
                    matchesCounter++;
                    System.out.print("Found " + matchesCounter + " / " + findArray.length + " entries. Time taken: " + readableTime(System.currentTimeMillis() - timeOfStart) + "\r");
                    break;
                }
            }
        }
    }
}
