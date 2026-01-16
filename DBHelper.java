package app;

import java.sql.*;

public class DBHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/grade_predictor";
    private static final String USER = "root";
    private static final String PASS = "root";   

    
    public static Connection getConn() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // --------------------------
    // CREATE TABLES
    // --------------------------
    public static void initDB() {
        try (Connection con = getConn()) {

            Statement st = con.createStatement();

            st.execute("""
                CREATE TABLE IF NOT EXISTS students(
                    student_id INT AUTO_INCREMENT PRIMARY KEY,
                    roll_no VARCHAR(50) UNIQUE,
                    name VARCHAR(100),
                    branch VARCHAR(50),
                    year INT,
                    email VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS subjects(
                    subject_id INT AUTO_INCREMENT PRIMARY KEY,
                    code VARCHAR(50) UNIQUE,
                    name VARCHAR(100),
                    credits INT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS marks(
                    mark_id INT AUTO_INCREMENT PRIMARY KEY,
                    student_id INT,
                    subject_id INT,
                    cia1 INT,
                    cia2 INT,
                    assignments INT,
                    lab INT,
                    attendance INT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS predictions(
                    prediction_id INT AUTO_INCREMENT PRIMARY KEY,
                    student_id INT,
                    subject_id INT,
                    predicted_mark INT,
                    predicted_grade VARCHAR(5)
                );
            """);

            System.out.println("DB ready!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static Student fetchStudent(String roll) {
        try (Connection con = getConn()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM students WHERE roll_no = ?"
            );
            ps.setString(1, roll);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("branch"),
                        rs.getInt("year"),
                        rs.getString("email")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // NOT FOUND
    }

   
        public static int ensureStudent(String roll, String name,
                                    String branch, int year, String email)
            throws Exception {

        try (Connection con = getConn()) {

            // Check if exists
            PreparedStatement ps = con.prepareStatement(
                "SELECT student_id FROM students WHERE roll_no = ?"
            );
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("student_id");

            // New student
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO students(roll_no, name, branch, year, email) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ins.setString(1, roll);
            ins.setString(2, name);
            ins.setString(3, branch);
            ins.setInt(4, year);
            ins.setString(5, email);

            ins.executeUpdate();
            ResultSet keys = ins.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    
    public static int ensureSubject(String code, String name, int credits)
            throws Exception {

        try (Connection con = getConn()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT subject_id FROM subjects WHERE code = ?"
            );
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("subject_id");

            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO subjects(code, name, credits) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ins.setString(1, code);
            ins.setString(2, name);
            ins.setInt(3, credits);

            ins.executeUpdate();
            ResultSet keys = ins.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    
    public static void insertMark(int studentId, int subjectId, MarkRecord m)
            throws Exception {

        try (Connection con = getConn()) {

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO marks(student_id, subject_id, cia1, cia2, assignments, lab, attendance) " +
                "VALUES (?,?,?,?,?,?,?)"
            );

            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setInt(3, m.cia1);
            ps.setInt(4, m.cia2);
            ps.setInt(5, m.assignments);
            ps.setInt(6, m.lab);
            ps.setInt(7, m.attendance);

            ps.executeUpdate();
        }
    }

    
    public static void insertPrediction(int studentId, int subjectId,
                                        int mark, String grade)
            throws Exception {

        try (Connection con = getConn()) {

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO predictions(student_id, subject_id, predicted_mark, predicted_grade) " +
                "VALUES (?,?,?,?)"
            );

            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setInt(3, mark);
            ps.setString(4, grade);

            ps.executeUpdate();
        }
    }
}
