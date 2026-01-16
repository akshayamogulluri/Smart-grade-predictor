package app;

public class MarkRecord {
	public int cia1, cia2, assignments, lab, attendance;
    public int totalInternal;
    public String rollNo, subjectCode;
    public MarkRecord(String rollNo, String subjectCode, int cia1, int cia2, int assignments, int lab, int attendance) {
        this.rollNo = rollNo; this.subjectCode = subjectCode;
        this.cia1 = cia1; this.cia2 = cia2; this.assignments = assignments; this.lab = lab; this.attendance = attendance;
    }

}
