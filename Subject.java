package app;

public class Subject {
	public int subjectId;
    public String code, name;
    public int credits;
    public Subject(int id, String code, String name, int credits) {
        this.subjectId = id; this.code = code; this.name = name; this.credits = credits;
    }

}
