package net.slc.dv.database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import net.slc.dv.database.connection.Connect;
import net.slc.dv.enums.Role;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.User;

public class MemberQuery {

    private final Connect connect;

    public MemberQuery() {
        this.connect = Connect.getConnection();
    }

    public ArrayList<ClassroomMember> getClassMember(String classCode) {
        ArrayList<ClassroomMember> classroomMembers = new ArrayList<>();

        String query =
                "SELECT\n" + "\tClassID, Role, msuser.UserID, UserName, UserEmail, UserDOB, UserProfile, UserColor\n"
                        + "FROM class_member\n"
                        + "JOIN msuser\n"
                        + "ON class_member.UserID = msuser.UserID\n"
                        + "WHERE ClassID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, classCode);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getString("UserID"),
                            rs.getString("UserName"),
                            rs.getString("UserEmail"),
                            "",
                            rs.getString("UserDOB"),
                            rs.getBlob("UserProfile"),
                            rs.getString("UserColor"));
                    classroomMembers.add(new ClassroomMember(rs.getString("ClassID"), user, rs.getString("Role")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classroomMembers;
    }

    public Role getRole(String classCode) {
        String role = "";

        String query = "SELECT Role FROM class_member WHERE UserID = ? AND ClassID = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            assert ps != null;
            ps.setString(1, LoggedUser.getInstance().getId());
            ps.setString(2, classCode);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    role = rs.getString("Role");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Role.getRole(role);
    }
}
