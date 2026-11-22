package hello1;

import java.util.Arrays;
import java.util.Scanner;

public class Hello1 {

    static class TaskQueue {

        static class TaskNode {
            String title;
            int priority;
            TaskNode next;

            TaskNode(String title, int priority) {
                this.title = title;
                this.priority = priority;
                this.next = null;
            }
        }

        private TaskNode head = null;

        public void addTask(String title, int priority) {
            TaskNode newNode = new TaskNode(title, priority);

            if (head == null || priority < head.priority) {
                newNode.next = head;
                head = newNode;
                return;
            }

            TaskNode current = head;
            while (current.next != null && current.next.priority <= priority) {
                current = current.next;
            }

            newNode.next = current.next;
            current.next = newNode;
        }

        public TaskNode dispatchNextTask() {
            if (isEmpty()) {
                return null;
            }
            TaskNode top = head;
            head = head.next;
            return top;
        }

        public void displayTasks() {
            if (isEmpty()) {
                System.out.println("  [No pending tasks in queue]");
                return;
            }
            TaskNode current = head;
            int pos = 1;
            while (current != null) {
                System.out.println("  " + pos + ". [Priority " + current.priority + "] " + current.title);
                current = current.next;
                pos++;
            }
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    static class NumberSorter {

        public static void heapSort(int[] arr) {
            int n = arr.length;

            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(arr, n, i);
            }

            for (int end = n - 1; end > 0; end--) {
                swap(arr, 0, end);
                heapify(arr, end, 0);
            }
        }

        private static void heapify(int[] arr, int heapSize, int root) {
            int largest = root;
            int left = 2 * root + 1;
            int right = 2 * root + 2;

            if (left < heapSize && arr[left] > arr[largest]) {
                largest = left;
            }
            if (right < heapSize && arr[right] > arr[largest]) {
                largest = right;
            }

            if (largest != root) {
                swap(arr, root, largest);
                heapify(arr, heapSize, largest);
            }
        }

        private static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    static class TextSearchEngine {

        static class Suffix implements Comparable<Suffix> {
            int startIndex;
            String value;

            Suffix(int startIndex, String value) {
                this.startIndex = startIndex;
                this.value = value;
            }

            @Override
            public int compareTo(Suffix other) {
                return this.value.compareTo(other.value);
            }
        }

        private String text = "";
        private int[] suffixArray = new int[0];

        public void setText(String newText) {
            this.text = newText;
            buildSuffixArray();
        }

        private void buildSuffixArray() {
            int n = text.length();
            Suffix[] suffixes = new Suffix[n];

            for (int i = 0; i < n; i++) {
                suffixes[i] = new Suffix(i, text.substring(i));
            }

            Arrays.sort(suffixes);

            suffixArray = new int[n];
            for (int i = 0; i < n; i++) {
                suffixArray[i] = suffixes[i].startIndex;
            }
        }

        public void printSortedSuffixes() {
            if (text.isEmpty()) {
                System.out.println("  [No text loaded yet]");
                return;
            }
            System.out.println("  Suffix Array indices: " + Arrays.toString(suffixArray));
            for (int i = 0; i < suffixArray.length; i++) {
                int idx = suffixArray[i];
                System.out.println("    [" + i + "] index " + idx + " -> \"" + text.substring(idx) + "\"");
            }
        }

        public void searchPattern(String pattern) {
            if (text.isEmpty()) {
                System.out.println("  [Load text first before searching]");
                return;
            }
            if (pattern.isEmpty()) {
                System.out.println("  [Pattern cannot be empty]");
                return;
            }

            int left = 0;
            int right = suffixArray.length - 1;
            int foundPos = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                String suffix = text.substring(suffixArray[mid]);

                if (suffix.startsWith(pattern)) {
                    foundPos = mid;
                    break;
                } else if (suffix.compareTo(pattern) < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (foundPos == -1) {
                System.out.println("  Pattern \"" + pattern + "\" NOT found.");
                return;
            }

            int start = foundPos;
            while (start > 0 && text.substring(suffixArray[start - 1]).startsWith(pattern)) {
                start--;
            }

            int end = foundPos;
            while (end < suffixArray.length - 1 && text.substring(suffixArray[end + 1]).startsWith(pattern)) {
                end++;
            }

            System.out.print("  Found \"" + pattern + "\" at starting index/indices: ");
            for (int i = start; i <= end; i++) {
                System.out.print(suffixArray[i]);
                if (i < end) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskQueue taskQueue = new TaskQueue();
        TextSearchEngine searchEngine = new TextSearchEngine();

        taskQueue.addTask("Backup database", 2);
        taskQueue.addTask("Fix server outage", 1);
        taskQueue.addTask("Update README", 3);
        searchEngine.setText("banana");

        boolean running = true;

        while (running) {
            System.out.println("\n==========================================");
            System.out.println("   TASK, SORTING & TEXT SEARCH SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. Add Task to Priority Queue");
            System.out.println("2. View & Dispatch Highest-Priority Task");
            System.out.println("3. Sort Numbers (Heap Sort)");
            System.out.println("4. Set Text & View Suffix Array");
            System.out.println("5. Search Text Using Suffix Array");
            System.out.println("0. Exit System");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter task title: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Enter priority (lower number = higher priority, e.g. 1): ");
                    int priority = readInt(scanner);
                    if (title.isEmpty()) {
                        System.out.println("Task title cannot be empty.");
                    } else {
                        taskQueue.addTask(title, priority);
                        System.out.println("Task added!");
                    }
                    break;

                case "2":
                    System.out.println("\n--- Pending Tasks ---");
                    taskQueue.displayTasks();
                    if (!taskQueue.isEmpty()) {
                        System.out.print("Dispatch top task now? (y/n): ");
                        String ans = scanner.nextLine().trim().toLowerCase();
                        if (ans.equals("y") || ans.equals("yes")) {
                            TaskNode done = taskQueue.dispatchNextTask();
                            System.out.println("Dispatched: [" + done.title + "] (Priority " + done.priority + ")");
                        }
                    }
                    break;

                case "3":
                    System.out.print("How many numbers do you want to sort? ");
                    int count = readInt(scanner);
                    if (count <= 0) {
                        System.out.println("Please enter a positive count.");
                        break;
                    }
                    int[] numbers = new int[count];
                    for (int i = 0; i < count; i++) {
                        System.out.print("Enter number #" + (i + 1) + ": ");
                        numbers[i] = readInt(scanner);
                    }
                    System.out.println("Before Heap Sort: " + Arrays.toString(numbers));
                    NumberSorter.heapSort(numbers);
                    System.out.println("After Heap Sort:  " + Arrays.toString(numbers));
                    break;

                case "4":
                    System.out.print("Enter text to index: ");
                    String inputText = scanner.nextLine();
                    searchEngine.setText(inputText);
                    System.out.println("Text indexed: \"" + inputText + "\"");
                    searchEngine.printSortedSuffixes();
                    break;

                case "5":
                    System.out.print("Enter substring/pattern to search: ");
                    String pattern = scanner.nextLine();
                    searchEngine.searchPattern(pattern);
                    break;

                case "0":
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please pick a valid menu option.");
            }
        }

        scanner.close();
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}
