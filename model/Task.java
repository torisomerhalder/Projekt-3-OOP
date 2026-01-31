package model;

import service.Storable;
import java.util.Objects;
import java.util.Objects;

public abstract class Task implements Storable {
    private int id;
    private String description;
    private boolean isDone;

    public Task(int id, String description) throws TaskValidationException {
        if (description == null || description.trim().length() < 3) {
            throw new TaskValidationException("Opis zadania jest zbyt krótki lub pusty!");

        }
        this.id = id;
        this.description = description;
        this.isDone = false; //domyślnie zadanie jest nieewykoane
    }
    public void setDone(boolean done) {
        isDone = done;
    }
    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
    public abstract Task makeCopy();

    @Override
    public void save() {
        System.out.println("Zapisywanie zadania ID: " + id + " do bazy danych...");
    }

    // Porównywanie zadań (equals i hashCode)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                isDone == task.isDone &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, isDone);
    }


    @Override
    public String toString() {
        // Jeśli zrobione, wstaw "X", jeśli nie - pustą spację
        String status = isDone ? "[X]" : "[ ]";
        return status + " ID:" + id + " " + description;
    }
}

