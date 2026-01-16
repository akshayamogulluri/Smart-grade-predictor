package app;

import javax.swing.*;
import java.awt.*;

public class SmartGradeUI extends JFrame {

    JTextField roll, name, branch, year, email;
    JTextField code, subname, cia1, cia2, assign, lab, attend;
    JLabel result;

    public SmartGradeUI() {

        setTitle("Smart Grade Predictor");
        setSize(420, 580);
        setLayout(null);
        setLocationRelativeTo(null);

        int y = 20;

        addLabel("Roll No:", 20, y);
        roll = addField(120, y); y+=35;

        JButton autoBtn = new JButton("Auto Fill");
        autoBtn.setBounds(300, 20, 90, 25);
        autoBtn.addActionListener(e -> autoFill());
        add(autoBtn);

        addLabel("Name:", 20, y); name = addField(120, y); y+=35;
        addLabel("Branch:", 20, y); branch = addField(120, y); y+=35;
        addLabel("Year:", 20, y); year = addField(120, y); y+=35;
        addLabel("Email:", 20, y); email = addField(120, y); y+=40;

        addLabel("Sub Code:", 20, y); code = addField(120, y); y+=35;
        addLabel("Sub Name:", 20, y); subname = addField(120, y); y+=40;

        addLabel("CIA1:", 20, y); cia1 = addField(120, y); y+=35;
        addLabel("CIA2:", 20, y); cia2 = addField(120, y); y+=35;
        addLabel("Assign:", 20, y); assign = addField(120, y); y+=35;
        addLabel("Lab:", 20, y); lab = addField(120, y); y+=35;
        addLabel("Attend:", 20, y); attend = addField(120, y); y+=40;

        JButton predict = new JButton("Predict");
        predict.setBounds(150, y, 100, 35);
        predict.addActionListener(e -> doPredict());
        add(predict);

        result = new JLabel("");
        result.setBounds(140, y+40, 250, 30);
        result.setFont(new Font("Arial", Font.BOLD, 16));
        add(result);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void autoFill() {
        Student s = DBHelper.fetchStudent(roll.getText());
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Roll number not found!");
            return;
        }
        name.setText(s.name);
        branch.setText(s.branch);
        year.setText(String.valueOf(s.year));
        email.setText(s.email);
    }

    void doPredict() {
        try {
            MarkRecord m = new MarkRecord(
                    roll.getText(),
                    code.getText(),
                    Integer.parseInt(cia1.getText()),
                    Integer.parseInt(cia2.getText()),
                    Integer.parseInt(assign.getText()),
                    Integer.parseInt(lab.getText()),
                    Integer.parseInt(attend.getText())
            );

            Predictor p = new Predictor();
            int mark = p.computeInternal(m);
            String grade = p.mapGrade(mark);

            int sid = DBHelper.ensureStudent(
                    roll.getText(),
                    name.getText(),
                    branch.getText(),
                    Integer.parseInt(year.getText()),
                    email.getText()
            );

            int subid = DBHelper.ensureSubject(code.getText(), subname.getText(), 3);

            DBHelper.insertMark(sid, subid, m);
            DBHelper.insertPrediction(sid, subid, mark, grade);

            result.setText(mark + " → " + grade);
        } catch (Exception ex) {
            result.setText("Error!");
        }
    }

    void addLabel(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 100, 25);
        add(l);
    }

    JTextField addField(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 180, 25);
        add(f);
        return f;
    }

    public static void main(String[] args) {
        DBHelper.initDB();
        new SmartGradeUI();
    }
}
