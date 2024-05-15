package member.model.dao;

import member.model.dto.MemberDto;
import member.view.AdminView;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static common.DBConnection.getConnection;
import static run.Main.mainView;

public class MemberDao {
    private Properties prop = null;
    private static Connection conn = null;

    public MemberDao() {
        try {
            prop = new Properties();
            prop.load(new FileReader("resources/memberQuery.properties"));
            conn = getConnection();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int adminLogin(String id, String pw) {

        int result = 0;
        PreparedStatement ps = null;

        // 관리자 로그인
        try {
//            "SELECT * FROM PCMEMBER WHERE USER_ID = ? AND USER_PW = ?"
            String sql = prop.getProperty("adminLogin");
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, pw);
            result = ps.executeUpdate();

            // 관리자라면
            if (id.equals("admin") && pw.equals("admin")) {
                System.out.println("관리자 아이디로 로그인합니다.");
                System.out.println();
                AdminView.adminView();
            } else {
                System.out.println("잘못된 아이디입니다. 재시도해주세요.");
                System.out.println();
                System.out.println();
                mainView();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    //회원추가
    public int addMember(String id, String pw, String name, String number) {
        int result = 0;
        PreparedStatement ps = null;


        try {
            //INSERT INTO PCMEMBER VALUES (?, ?, ?, ?)
            String sql = prop.getProperty("addMember");
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, pw);
            ps.setString(3, name);
            ps.setString(4, number);


            result = ps.executeUpdate();

            if (result > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("add시 에러발생");

            try {
                conn.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        return result;
    }


    // 회원 삭제(회원 탈퇴)
    public void deleteMember(String id, String pw, String name) {


    }

    //회원조회(로그인)


    // 회원 조회 및 수정(마이페이지)
    public void myPage(String id, String pw) {

    }

    // 사용 시간 수정
    public void updateTime(String id, int time) {

    }

    //회원수정
    public int modifyMember(String id, String pw, String name, String number) {
        int result = 0;
        PreparedStatement ps = null;


        try {
            //UPDATE PCMEMBER SET USER_PW = ?,USER_NAME =?,USER_TEL=? WHERE USER_ID=?
            String sql = prop.getProperty("modifyMember");
            ps = conn.prepareStatement(sql);
            ps.setString(1, pw);
            ps.setString(2, name);
            ps.setString(3, number);
            ps.setString(4, id);


            result = ps.executeUpdate();

            if (result > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("modify시 에러발생");

            try {
                conn.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //회원삭제
    public int delete(String id) {
        int result = 0;
        PreparedStatement ps = null;


        try {
            //DELETE FROM PCMEMBER WHERE USER_ID = ?
            String sql = prop.getProperty("delete");
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);


            result = ps.executeUpdate();

            if (result > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("delete시 에러발생");

            try {
                conn.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void memberList() {
        MemberDto rsDto = null;
        PreparedStatement ps = null;
        ResultSet rset = null;
        MemberDto memberDto = null;


        try {
            //SELECT * FROM PCMEMBER
            String sql = prop.getProperty("memberList");
            ps = conn.prepareStatement(sql);
            rset = ps.executeQuery();
            int i = 0;


            while (rset.next()) {
                memberDto = new MemberDto();
                memberDto.setId(rset.getString("USER_ID"));
                memberDto.setPw(rset.getString("USER_PW"));
                memberDto.setName(rset.getString("USER_NAME"));
                memberDto.setNumber(rset.getString("USER_TEL"));
                if( i == 0){
                    System.out.println("관리자 " + memberDto);
                    i++;
                }else {
                    System.out.println("회원번호 " + i + memberDto);
                    i++;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("delete시 에러발생");

            try {
                conn.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        System.out.println();
        AdminView.adminManage();
    }
}