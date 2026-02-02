package app;
import model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<Task> todoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("Witaj w systemie ToDo!");

        while (isRunning) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Dodaj zadanie służbowe (WorkTask)");
            System.out.println("2. Dodaj zadanie osobiste (PersonalTask)");
            System.out.println("3. Pokaż wszystkie zadania");
            System.out.println("4. Wykonaj zadanie (oznacz jako zrobione)");
            System.out.println("0. Wyjdź");
            System.out.print("Wybierz opcję: ");

            String wybor = scanner.nextLine();

            try {
                switch (wybor) {
                    case "1":
                        System.out.print("Podaj ID zadania: ");
                        int idPraca = Integer.parseInt(scanner.nextLine());
                        System.out.print("Podaj opis: ");
                        String opisPraca = scanner.nextLine();
                        System.out.print("Podaj deadline (YYYY-MM-DD): ");
                        LocalDate deadline = LocalDate.parse(scanner.nextLine());

                        WorkTask praca = new WorkTask(idPraca, opisPraca, deadline);
                        todoList.add(praca);
                        System.out.println("Dodano zadanie służbowe!");
                        break;

                    case "2":
                        System.out.print("Podaj ID zadania: ");
                        int idDom = Integer.parseInt(scanner.nextLine());
                        System.out.print("Podaj opis: ");
                        String opisDom = scanner.nextLine();
                        System.out.print("Podaj priorytet (High/Low): ");
                        String priorytet = scanner.nextLine();

                        PersonalTask dom = new PersonalTask(idDom, opisDom, priorytet);
                        todoList.add(dom);
                        System.out.println("Dodano zadanie osobiste!");
                        break;

                    case "3":
                        System.out.println("\n--- TWOJA LISTA ZADAŃ ---");
                        if (todoList.isEmpty()) {
                            System.out.println("(Brak zadań)");
                        } else {
                            for (int i = 0; i < todoList.size(); i++) {
                                System.out.printf("%d: %s%n", i, todoList.get(i));
                            }
                        }
                        break;


                    case "4":
                        System.out.println("\n--- DOSTĘPNE ZADANIA ---");
                        if (todoList.isEmpty()) {
                            System.out.println("(Lista pusta)");
                        } else {
                            for (Task t : todoList) {
                                System.out.println(t);
                            }
                        }
                        System.out.print("\nPodaj ID zadania do wykonania: ");
                        int idToComplete = Integer.parseInt(scanner.nextLine());

                        List<Task> foundTasks = new ArrayList<>();
                        for (Task t : todoList) {
                            if (t.getId() == idToComplete) {
                                foundTasks.add(t);
                            }
                        }

                        if (!foundTasks.isEmpty()) {
                            System.out.println("\n Znaleziono i wykonano:");
                            for (Task t : foundTasks) {
                                t.setDone(true);
                                System.out.println("  " + t);
                            }
                            System.out.println("Gotowe!");
                        } else {
                            System.out.println(" Nie znaleziono zadania o ID: " + idToComplete);
                        }
                        break;


                    case "0":
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
    }
}