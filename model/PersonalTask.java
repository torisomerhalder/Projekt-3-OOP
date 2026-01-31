package model;

public class PersonalTask extends Task {

    private String priority; // np. "Wysoki", "Niski"

    public PersonalTask(int id, String description, String priority) throws TaskValidationException {
        super(id, description);
        this.priority = priority;
    }

    @Override
    public Task makeCopy() {
        try {
            PersonalTask copy = new PersonalTask(this.getId(), this.getDescription(), this.priority);
            copy.setDone(this.isDone());
            return copy;
        } catch (TaskValidationException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (Priorytet: " + priority + ")";
    }
}
