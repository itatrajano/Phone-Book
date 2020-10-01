package phonebook;

import java.io.File;
import java.util.Scanner;

class Contact {
    private String phoneNumber;
    private String name;

    public Contact(String phoneNumber, String name){
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }
}

public class Main {

    static void swap (Contact[] contacts, int i, int j) {
        Contact temp = contacts[i];
        contacts[i] = contacts[j];
        contacts[j] = temp;
    }

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

        //Milissegundos
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

    static boolean findByLinearSearch(String find, Contact contacts[]) {
        for (int j = 0; j < contacts.length; j++) {
            if (contacts[j].getName().contains(find)) { //Se localizar retorna true e encerra o for.
                return true;
            }
        }
        return false; //Se o o teste falhar, retorna false
    }

    static boolean sortByNameBubbleSort(Contact contacts[], double timeLimit) { //Retorna false se não concluir a organização. Para não limitar tempo, timeLimit deve ser 0

        double timeOfStart = System.currentTimeMillis();
        double timeWorkingOnBubbleSort = 0;

        for (int i = 0; i < contacts.length - 1; i++) {
            for (int j = 0; j < contacts.length - 1 - i; j++) {
                if(contacts[j].getName().compareTo(contacts[j + 1].getName()) > 0) {
                    swap(contacts, j, j+1);
                }
                //System.out.print("Sorting time: " + readableTime(timeWorkingOnBubbleSort) + "\r");
                timeWorkingOnBubbleSort = System.currentTimeMillis() - timeOfStart;
                if ((timeLimit != 0) && (timeWorkingOnBubbleSort > timeLimit)) {
                    return false;
                }
            }
        }

        return true; //Retorna false apenas caso timeLimit excedido.
    }

    static boolean findByJumpSearch(String find, Contact contacts[]) {

        int currentRight = 0; //Limite da direita do bloco atual (inclusivo)
        int prevRight = 0; //Limite da direita do bloco anterior (ou limite exclusivo do bloco atual)

        if(contacts.length == 0) { //Retorna falso se o tamanho do array contacts for zero
            return false;
        }

        if (contacts[currentRight].getName().equals(find)) { //Retorna true se o alvo estiver no 1º elemento do array
            return true;
        }

        int jumpLength = (int) Math.sqrt(contacts.length); //Tamanho do pulo

        //Busca pelo bloco alvo
        while (currentRight < contacts.length - 1) {
            //Atualiza current right até o limite do array contacts
            currentRight = Math.min(currentRight + jumpLength, contacts.length - 1);
            if (contacts[currentRight].getName().compareTo(find) >= 0) {
                break; //Se é maior ou igual ao alvo o bloco pode conter o alvo e para de pular.
            }
            prevRight = currentRight; //Atualiza prevRight
        }

        //Se alcançou o último bloco e não achou o alvo
        if ((currentRight == contacts.length - 1) && (contacts[currentRight].getName().compareTo(find) > 0)) {
            return false;
        }

        //Se o método não foi encerrado, aplica backwardSearch ao bloco localizado
        //BACKWARD SEARCH
        for (int i = currentRight; i > prevRight; i--) {
            if (contacts[i].getName().equals(find)) {
                return true;
            }
        }
        return false;

    }

    static void sortByNameQuickSort (Contact contacts[], int left, int right) {

        if (left < right) {
            int pivotIndex = partition(contacts, left, right); //Obtém o próximo pivô
            sortByNameQuickSort(contacts, left, pivotIndex - 1); //Ordena o sub array da esquerda
            sortByNameQuickSort(contacts, pivotIndex + 1, right); //Ordena o sub array da direita
        }
    }

    static int partition(Contact[] contacts, int left, int right) { //Método usado para Quick Sort
        String pivot = contacts[right].getName(); //Escolhe o último nome da lista
        int partitionIndex = left; //Inicia o índice da partição no primeiro elemento do array

        for (int i = left; i < right; i++) { //Percorre o array
            if(contacts[i].getName().compareTo(pivot) < 0) {
                swap(contacts, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(contacts, partitionIndex, right); //Troca o pivô de sua posição para o local adequado

        return partitionIndex;
    }

    static boolean findByBinarySearch (String find, Contact contacts[]) {
        int left = 0;
        int right = contacts.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (find.compareTo(contacts[mid].getName()) == 0) {
                return true;
            } else if (contacts[mid].getName().compareTo(find) > 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return false;
    }

    private static class TableEntry<T> {
        private final String key;
        private final T value;

        public TableEntry(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(String key, T value) {
            int idx = findEntry(key);

            if (idx == -1) {
                return false;
            }

            table[idx] = new TableEntry(key, value);

            //System.out.println("Adicionado:" + key + " - Hash: " + idx);
            return true;
        }

        public T get(String key) {
            int idx = findEntry(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        private int findEntry(String key) {
            char charArray[] = key.toCharArray();
            long firstHash = 0;
            String tempString = "";

            for (int i = 0; i < charArray.length && i < 10; i++) {
                firstHash += (Math.abs(charArray[i] - 65)) * Math.pow(53, i);
            }
            //long longHash = firstHash % 1_014_007;
            long longHash = firstHash % 2_000_083;
            int hash = (int) longHash;
            //int oldHash = hash;

            while (!(table[hash] == null || table[hash].getKey().equals(key))) {
                hash = (hash + 1);
                if (hash == (int) longHash % 2_000_083)  {
                //if (hash == oldHash)  {
                    return -1;
                }
            }

            return hash;
        }
    }

    public static void main(String[] args) {

        String findLocation = "D:\\partes de agenda telefonica\\find.txt";
        String directoryLocation = "D:\\partes de agenda telefonica\\directory.txt";

        File find = new File(findLocation);
        File directory = new File(directoryLocation);

        //Cria o Array com 500 strings
        String[] findArray = new String[500];
        //Cria um array com os 1.014.130 contatos da agenda
        Contact[] contacts = new Contact[1_014_130];

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

        //Transfere cada linha do arquivo "directory.txt" para um objeto Contact no array contacts[]"
        try(Scanner scanner = new Scanner(directory)) {
            int arrayCounter = 0;
            while (scanner.hasNext()) {
                String[] contactStringArray = scanner.nextLine().split(" ", 2); //Limita o resultado da divisão a 2 elementos
                //Transforma o primeiro elemento do array acima em int e cria uma instancia de Contact em contacts[]
                contacts[arrayCounter] = new Contact(contactStringArray[0], contactStringArray[1]);
                arrayCounter++;
            }
        } catch (Exception e) {
            System.out.println("Erro de Leitura de " + directoryLocation + " - " + e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }

        //INICIA O ALGORITMO DE BUSCA "LINEAR SEARCH"
        System.out.println("Start searching (linear search)...");
        double timeOfStart = System.currentTimeMillis();
        double timeWorkingOnLinearSearch = 0;
        int matchesCounter = 0;
        //Compara cara elemento de findArray[] com todos os elementos de contacts[]
        for (int i = 0; i < findArray.length; i++) {
            if (findByLinearSearch(findArray[i], contacts)) { //Busca a string de findArray[i] no array contacts;
                 matchesCounter++;
            }
            timeWorkingOnLinearSearch = System.currentTimeMillis() - timeOfStart;
        }
        System.out.println("Found " + matchesCounter + " / " + findArray.length + " entries. Time taken: " + readableTime(timeWorkingOnLinearSearch));
        System.out.println();

        //INICIA O ALGORITMO DE BUSCA "BUBBLE SORT + JUMP SEARCH"
        System.out.println("Start searching (bubble sort + jump search)...");
        matchesCounter = 0; //Zera matchesCounter
        timeOfStart = System.currentTimeMillis(); //Zera o tempo do início do algoritmo
        double sortingTime = 0;
        double timeWorkingOnJumpSearch = 0;
        double timeLimit = timeWorkingOnLinearSearch * 10;
        boolean isSorted = false;

        //BUBBLE SORT - Inicia a organização em ordem alfabética dos objetos Contact de contacts[]
        isSorted = sortByNameBubbleSort(contacts, timeLimit); //Tenta ordenar o array contacts. Se demorar mais que timeLimit, retorna falso. Se ordenar, retorna verdadeiro.
        sortingTime = System.currentTimeMillis() - timeOfStart;
/*
        if (isSorted) {
            System.out.print("Sorting time: " + readableTime(sortingTime));
        } else {
            System.out.print("Sorting time: " + readableTime(sortingTime) + " - STOPPED, moved to linear search");
        }
        System.out.println();
 */

        if (isSorted) { //Se organizado, inicia o Jump Search
            //JUMP SEARCH
            timeWorkingOnJumpSearch = 0;
            double searchStartTime = System.currentTimeMillis();
            for (int i = 0; i < findArray.length; i++) {
                if (findByJumpSearch(findArray[i], contacts)) { //Busca a string de findArray[i] no array contacts;
                    matchesCounter++;
                }
                timeWorkingOnJumpSearch = System.currentTimeMillis() - searchStartTime;
            }

        } else { //Se não organizado, retorna a Linear Search
            timeWorkingOnLinearSearch = 0;
            double searchStartTime = System.currentTimeMillis();
            for (int i = 0; i < findArray.length; i++) {
                if (findByLinearSearch(findArray[i], contacts)) { //Busca a string de findArray[i] no array contacts;
                    matchesCounter++;
                }
                timeWorkingOnLinearSearch = System.currentTimeMillis() - searchStartTime;
            }
        }

        //Print results for BUBBLE SORT + JUMP SEARCH
        //Print total time
        System.out.println("Found " + matchesCounter + " / " + findArray.length + " entries. Time taken: " + readableTime(System.currentTimeMillis() - timeOfStart));
        //Print sorting time
        System.out.println("Sorting time: " + readableTime(sortingTime));
        //If sorted is not complete, adds this information
        if (!isSorted) {
            //System.out.println("  - STOPPED, moved to linear search");
            System.out.println("Searching time: " + readableTime(timeWorkingOnLinearSearch));
        } else { //If not, just goes to the next line
            System.out.println();
            System.out.println("Searching time: " + readableTime(timeWorkingOnJumpSearch));
        }
        System.out.println();

        //INICIA O ALGORITMO DE BUSCA "QUICK SORT + BINARY SEARCH"
        System.out.println("Start searching (quick sort + binary search)...");
        matchesCounter = 0; //Zera matchesCounter
        timeOfStart = System.currentTimeMillis(); //Zera o tempo do início do algoritmo
        sortingTime = 0;
        double timeWorkingOnBinarySearch = 0;

        //QUICK SORT - Inicia a organização em ordem alfabética dos objetos Contact de contacts[]
        sortByNameQuickSort(contacts, 0, contacts.length -1); //Tenta ordenar o array contacts. Se demorar mais que timeLimit, retorna falso. Se ordenar, retorna verdadeiro.
        sortingTime = System.currentTimeMillis() - timeOfStart;

        //BINARY SEARCH
        double searchStartTime = System.currentTimeMillis();
        for (int i = 0; i < findArray.length; i++) {
            if (findByBinarySearch(findArray[i], contacts)) {
                matchesCounter++;
            }
        }
        timeWorkingOnBinarySearch = System.currentTimeMillis() - searchStartTime;

        //Print results for QUICK SORT + BINARY SEARCH
        //Print total time
        System.out.println("Found " + matchesCounter + " / " + findArray.length + " entries. Time taken: " + readableTime(System.currentTimeMillis() - timeOfStart));
        //Print sorting time
        System.out.println("Sorting time: " + readableTime(sortingTime));
        //Print searching time
        System.out.println("Searching time: " + readableTime(timeWorkingOnBinarySearch));
        System.out.println();


        //INICIA O ALGORITMO DE ORGANIZAÇÃO E BUSCA HASH TABLE
        System.out.println("Start searching (hash table)...");
        matchesCounter = 0;
        double creatingTime = 0;
        double timeWorkingOnHashSearch = 0;
        timeOfStart = System.currentTimeMillis();

        //HASHING - Cria uma Hash Table e a preenche com objetos de contacts[]
        HashTable hashedContacts = new HashTable(1_014_130 * 3);

        for (int i = 0; i < contacts.length; i++) {
            String name = contacts[i].getName();
            hashedContacts.put(name, contacts[i]);
        }
        creatingTime = System.currentTimeMillis() - timeOfStart;

        //SEARCH BY HASH
        searchStartTime = System.currentTimeMillis();
        for (int i = 0; i < findArray.length; i++) {
            String name = findArray[i];
            Contact found = (Contact) hashedContacts.get(name);
            if (found.getName().equals(name)) {
                matchesCounter++;
            }
        }
        timeWorkingOnHashSearch = System.currentTimeMillis() - searchStartTime;

        //Print results for HASH TABLE
        //Print total time
        System.out.println("Found " + matchesCounter + " / " + findArray.length + " entries. Time taken: " + readableTime(System.currentTimeMillis() - timeOfStart));
        //Print hashing time
        System.out.println("Creating time: " + readableTime(creatingTime));
        //Print searching time
        System.out.println("Searching time: " + readableTime(timeWorkingOnHashSearch));
        System.out.println();

    }
}
