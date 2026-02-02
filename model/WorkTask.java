package model;

import java.time.LocalDate;

public class WorkTask extends Task {
    public LocalDate deadline;

    // KONSTRUKTOR
    public WorkTask(int id, String description, LocalDate deadline) throws TaskValidationException {
        super(id, description);

        if (deadline.isBefore(LocalDate.now())) {
            throw new TaskValidationException("Deadline nie może być w przeszłości!");
        }
        this.deadline = deadline;
    }

    // METODA makeCopy
    @Override
    public Task makeCopy() {
        try {
            WorkTask copy = new WorkTask(this.getId(), this.getDescription(), this.deadline);
            copy.setDone(this.isDone());
            return copy;
        } catch (TaskValidationException e) {
            return null;
        }
    }

    // TO STRING
    @Override
    public String toString() {
        return super.toString() + " (Deadline: " + deadline + ")";
    }
}