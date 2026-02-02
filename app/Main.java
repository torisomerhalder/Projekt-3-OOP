package app;
import model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static int nextId = 1; // Automatyczne ID
    private static final String FILE_NAME = "todolist.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        List<Task> todoList = loadTasks();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        

        System.out.println("Witaj w systemie ToDo!");

        while (isRunning) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Dodaj zadanie służbowe (WorkTask)");
            System.out.println("2. Dodaj zadanie osobiste (PersonalTask)");
            System.out.println("3. Pokaż wszystkie zadania");
            System.out.println("4. Wykonaj zadanie (oznacz jako zrobione)");
            System.out.println("5. Filtrowanie");
            System.out.println("6. Sortuj");
            System.out.println("7. Statystyki");
            System.out.println("8. Zapisz do pliku");
            System.out.println("0. Wyjdź");
            System.out.print("Wybierz opcję: ");

            String wybor = scanner.nextLine();

            try {
                switch (wybor) {
                    case "1":
                        addWorkTask(todoList, scanner);
                        break;
                    case "2":
                        addPersonalTask(todoList, scanner);
                        break;
                    case "3":
                        showAll(todoList);
                        break;
                    case "4":
                        showAll(todoList);
                        completeTask(todoList, scanner);
                        break;
                    case "5":
                        filterTasks(todoList, scanner);
                        break;
                    case "6":
                        sortTasks(todoList);
                        break;
                    case "7":
                        showStats(todoList);
                        break;
                    case "8":
                        saveTasks(todoList);
                        break;
                    case "0":
                        saveTasks(todoList); // Auto-save przy wyjściu
                        isRunning = false;
                        System.out.println("Do widzenia!");
                        break;
                    default:
                        System.out.println("Nieznana opcja.");
                }
            } catch (TaskValidationException e) {
                System.out.println("!!! BŁĄD WALIDACJI: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("!!! BŁĄD DANYCH: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addWorkTask(List<Task> todoList, Scanner scanner) throws TaskValidationException {
        System.out.print("Podaj opis zadania: ");
        String description = scanner.nextLine();

        System.out.print("Podaj deadline (YYYY-MM-DD): ");
        String deadlineStr = scanner.nextLine();
        LocalDate deadline = LocalDate.parse(deadlineStr, DATE_FORMAT);

        WorkTask newTask = new WorkTask(nextId++, description, deadline);
        todoList.add(newTask);
        System.out.println("Dodano zadanie służbowe: " + newTask);
    }

    private static void addPersonalTask(List<Task> todoList, Scanner scanner) throws TaskValidationException {
        System.out.print("Podaj opis zadania: ");
        String description = scanner.nextLine();

        System.out.print("Podaj priorytet (High/Low): ");
        String priority = scanner.nextLine();

        PersonalTask newTask = new PersonalTask(nextId++, description, priority);
        todoList.add(newTask);
        System.out.println("Dodano zadanie osobiste: " + newTask);
    }

    private static void showAll(List<Task> todoList) {
        System.out.println("\n--- LISTA ZADAŃ ---");
        for (Task t : todoList) {
            System.out.println(t);
        }
    }

    private static void completeTask(List<Task> todoList, Scanner scanner) {
        System.out.print("Podaj ID zadania do oznaczenia jako wykonane: ");
        int id = Integer.parseInt(scanner.nextLine());
        for (Task t : todoList) {
            if (t.getId() == id) {
                t.setDone(true);
                System.out.println("Zadanie oznaczone jako wykonane: " + t);
                return;
            }
        }
        System.out.println("Nie znaleziono zadania o ID: " + id);
    }

    private static void filterTasks(List<Task> todoList, Scanner scanner) {
        System.out.println("1. Niewykonane\n2. Służbowe\n3. Osobiste\n4. Wysoki priorytet");
        String choice = scanner.nextLine();
        System.out.println("\n--- FILTR ---");
        for (Task t : todoList) {
            boolean show = switch (choice) {
                case "1" -> !t.isDone();
                case "2" -> t instanceof WorkTask;
                case "3" -> t instanceof PersonalTask;
                case "4" -> t instanceof PersonalTask pt && "HIGH".equals(((PersonalTask) t).priority);
                default -> true;
            };
            if (show) System.out.println(t);
        }
    }

    private static void sortTasks(List<Task> todoList) {
        todoList.sort((a, b) -> {
            if (a.isDone() != b.isDone()) return a.isDone() ? 1 : -1;
            return Integer.compare(a.getId(), b.getId());
        });
        System.out.println("Posortowano (niewykonane pierwsze)");
    }

    private static void showStats(List<Task> todoList) {
        long total = todoList.size();
        long done = todoList.stream().filter(Task::isDone).count();
        long work = todoList.stream().filter(t -> t instanceof WorkTask).count();
        long highPrio = todoList.stream()
            .filter(t -> t instanceof PersonalTask pt && "HIGH".equals(((PersonalTask) pt).priority))
            .count();
        System.out.printf("""
             STATYSTYKI:
            Razem: %d | Wykonane: %d (%.0f%%)
            Służbowe: %d | Wysoki priorytet: %d
            """, total, done, 100.0 * done / total, work, highPrio);
    }

    private static void saveTasks(List<Task> todoList) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
        // Nagłówek CSV
        writer.println("type,id,description,isDone,deadline,priority");
        
        for (Task task : todoList) {
            if (task instanceof WorkTask work) {
                writer.printf("WORK,%d,%s,%b,%s,%s%n",
                    work.getId(),
                    escapeCsv(work.getDescription()),
                    work.isDone(),
                    work.deadline,
                    ""
                );
            } else if (task instanceof PersonalTask personal) {
                writer.printf("PERSONAL,%d,%s,%b,%s,%s%n",
                    personal.getId(),
                    escapeCsv(personal.getDescription()),
                    personal.isDone(),
                    "",
                    personal.priority
                );
            }
        }
        System.out.println("💾 Zapisano " + todoList.size() + " zadań do CSV");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu: " + e.getMessage());
        }
    }

    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            System.out.println("📂 Brak pliku - nowa lista");
            return tasks;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Pomijamy nagłówek
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 6);
                if (parts.length < 4) continue;
                
                int id = Integer.parseInt(parts[1]);
                String desc = unescapeCsv(parts[2]);
                boolean done = Boolean.parseBoolean(parts[3]);
                
                Task task;
                if ("WORK".equals(parts[0])) {
                    LocalDate deadline = LocalDate.parse(parts[4], DATE_FORMAT);
                    task = new WorkTask(id, desc, deadline);
                } else if ("PERSONAL".equals(parts[0])) {
                    String priority = parts[5];
                    task = new PersonalTask(id, desc, priority);
                } else {
                    continue;
                }
                
                if (done) task.setDone(true);
                tasks.add(task);
                nextId = Math.max(nextId, id + 1); // Aktualizuj nextId
            }
            System.out.println("📂 Załadowano " + tasks.size() + " zadań z CSV");
        } catch (Exception e) {
            System.out.println("❌ Błąd odczytu CSV: " + e.getMessage());
            return new ArrayList<>();
        }
        return tasks;
    }

    // CSV helpers
    private static String escapeCsv(String s) {
        return s.replace("\"", "\"\"").replace(",", ";");
    }

    private static String unescapeCsv(String s) {
        return s.replace("\"\"", "\"").replace(";", ",");
    }
}