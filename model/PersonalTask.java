package model;

public class PersonalTask extends Task {

    public String priority; // np. "Wysoki", "Niski"

    public PersonalTask(int id, String description, String priority) throws TaskValidationException {
    super(id, description);
    
    // Walidacja priorytetu - case insensitive + trim
    String prio = priority != null ? priority.trim().toUpperCase() : "";
    
    if (!prio.equals("HIGH") && !prio.equals("LOW") && !prio.equals("H") && !prio.equals("L") && !prio.equals("MAŁY") && !prio.equals("NISKI")) {
        throw new TaskValidationException(
            "Nieprawidłowy priorytet! Dopuszczalne: High/H/high/h, Low/L/low/l. Podano: '" + priority + "'"
        );
    }
    
    this.priority = prio; // Zapisujemy jako UPPERCASE dla spójności
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
