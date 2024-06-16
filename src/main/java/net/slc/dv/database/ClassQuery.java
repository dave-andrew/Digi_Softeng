package net.slc.dv.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;

public class ClassQuery {

    private final LoggedUser loggedUser;
    private final Connect connect;

    public ClassQuery() {
        this.loggedUser = LoggedUser.getInstance();
        this.connect = Connect.getConnection();
    }

    public void createClass(Classroom classroom) {
        String query = "INSERT INTO msclass VALUES (?, ?, ?, ?, ?, NULL)";
        String query2 = "INSERT INTO class_member VALUES (?, ?, ?)";

        try (PreparedStatement ps = connect.prepareStatement(query); ) {
            assert ps != null;
            ps.setString(1, classroom.getClassId());
            ps.setString(2, classroom.getClassName());
            ps.setString(3, classroom.getClassDesc());
            ps.setString(4, classroom.getClassCode());
            ps.setString(5, classroom.getClassSubject());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement ps = connect.prepareStatement(query2)) {
            assert ps != null;
            ps.setString(1, classroom.getClassId());
            ps.setString(2, loggedUser.getId());
            ps.setString(3, "Teacher");

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Classroom> getUserClassroom() {
        ArrayList<Classroom> classrooms = new ArrayList<>();

        String query = "SELECT * FROM msclass WHERE ClassID IN (SELECT ClassID FROM class_member WHERE UserID = ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, loggedUser.getId());

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    classrooms.add(new Classroom(rs));
                }
            }
        } catch (Exception ignored) {
        }

        return classrooms;
    }

    public String joinClass(String classCode) {
        String checkGroupClassCode = "SELECT * FROM msclass WHERE ClassCode = ?";
        String query = "INSERT INTO class_member VALUES (?, ?, ?)";

        String classId;
        try (PreparedStatement ps = connect.prepareStatement(checkGroupClassCode)) {
            assert ps != null;
            ps.setString(1, classCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    classId = rs.getString("ClassID");
                } else {
                    return "no data";
                }
            }
        } catch (Exception e) {
            return "ingroup";
        }

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classId);
            ps.setString(2, loggedUser.getId());
            ps.setString(3, "Student");

            ps.executeUpdate();
            return classId;
        } catch (Exception e) {
            return "ingroup";
        }
    }
}
