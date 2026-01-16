package app;

public class Predictor {

    public int computeInternal(MarkRecord m) {

        double cia1 = m.cia1;
        double cia2 = m.cia2;
        double assignments = m.assignments;
        double lab = m.lab;
        double attendance = m.attendance;

        double internalAvg = (cia1 + cia2) / 2;

        double predicted =
                (cia1 * 0.15) +
                (cia2 * 0.15) +
                (assignments * 0.10) +
                (lab * 0.20) +
                (attendance * 0.10) +
                (internalAvg * 0.30);

        return (int) Math.round(predicted);
    }

    public String mapGrade(int mark) {
        if (mark >= 90) return "A+";
        if (mark >= 80) return "A";
        if (mark >= 70) return "B";
        if (mark >= 60) return "C";
        if (mark >= 50) return "D";
        return "F";
    }
}
