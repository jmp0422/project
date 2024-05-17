package member.model.dao;

import member.model.dto.MemberDto;
import member.view.AdminView;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static common.DBConnection.*;
import static run.Main.mainView;

public class MemberDao {
    private Properties prop = null;
    private static Connection conn = null;
    private static final long CHECK_INTERVAL = 60 * 1000;
    public String currentId = null;
    private Timer timer;

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

    //회원가입
    public void addMember(MemberDto memberDto) {
        // Statement stmt = null;// 실행할 쿼리
        ResultSet rset = null;// Select 한후 결과값 받아올 객체

        // 중복 아이디 확인
        String dupIdSql = prop.getProperty("selectById");
        try {
            PreparedStatement dupIdPs = conn.prepareStatement(dupIdSql);
            dupIdPs.setString(1, memberDto.getId());
            // stmt = conn.createStatement();
            rset = dupIdPs.executeQuery();


            if(rset.next()) {   // 중복이라면
                System.out.println("이미 존재하는 아이디입니다.");
            }else { // 중복이 아니라면 회원 추가
                String createMemberSql = prop.getProperty("createMember");
                PreparedStatement createMemberPs = conn.prepareStatement(createMemberSql);
                createMemberPs.setString(1, memberDto.getId());
                createMemberPs.setString(2, memberDto.getPw());
                createMemberPs.setString(3, memberDto.getName());
                createMemberPs.setString(4, memberDto.getNumber());
                createMemberPs.executeUpdate();
                System.out.println("회원가입이 완료되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void deleteMember(String id) {

        String deleteMemSql = prop.getProperty("deleteMember");
        try {
            PreparedStatement deleteMemPs = conn.prepareStatement(deleteMemSql);
            deleteMemPs.setString(1, id);
            deleteMemPs.executeUpdate();
            System.out.println("회원 탈퇴가 완료되었습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //회원조회(로그인)
    public boolean searchMember(MemberDto memberDto){
        String SignInSql = prop.getProperty("selectByIdPw");
        try {
            PreparedStatement SignInPs = conn.prepareStatement(SignInSql);
            SignInPs.setString(1, memberDto.getId());
            SignInPs.setString(2, memberDto.getPw());
            ResultSet rset = SignInPs.executeQuery();
            if(rset.next()) {
                System.out.println("로그인 성공");
                // 로그인 시간 기록
                LocalDateTime loginTime = LocalDateTime.now();
                currentId = memberDto.getId();
                // 로그인 성공 후 남은 시간 갱신 태스크 생성
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        // 현재 시간 기록
                        LocalDateTime currentTime = LocalDateTime.now();
                        // 경과 시간 계산
                        Duration duration = Duration.between(loginTime, currentTime);
                        long useTime = duration.getSeconds();
//                        System.out.println(888);
                        String str = Long.toString(useTime);
//                        System.out.println("usetime : " + str);

                        // 남은 시간 alert 해주는 함수
                        alertRemainingTime(useTime);
                    }
                }, CHECK_INTERVAL, CHECK_INTERVAL);
                return true;


            }else {
                System.out.println("로그인 실패");
                System.exit(0);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // 남은시간알려
    private void alertRemainingTime(long useTime) {
//        System.out.println(44444);
        Scanner scanner = new Scanner(System.in);
        // 남은 시간 갱신 로직 (가정)
        int remainingTime = getRemainingTime();
        remainingTime -= 60;
//        System.out.println(555);
//        System.out.println(remainingTime);
        // System.out.println("remainingTime: " + remainingTime + "useTime: " + useTime);
        PreparedStatement updateTimePs = null;
        PreparedStatement ps = null;
//        System.out.println(666);
//        updateTime = UPDATE PCMEMBER SET USER_TIME = ? WHERE USER_ID = ?
        String updateTimeSql = prop.getProperty("updateTime");
        try {
            updateTimePs = conn.prepareStatement(updateTimeSql);
            System.out.println(remainingTime);
            updateTimePs.setInt(1, remainingTime);
            //updateTimePs.setInt(1, );
            updateTimePs.setString(2, currentId);
            //System.out.println(usertime);
//            System.out.println(1212);
            int result = updateTimePs.executeUpdate();
            //커밋으로 해결!
//            System.out.println("커밋으로 해결!");
            if (result > 0) commit(conn);
            else rollback(conn);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        if (remainingTime <= 0) {
            // 남은 시간이 0 이하이면 타이머 종료 및 프로그램 종료
            timer.cancel();
            System.out.println();
            System.out.println("남은 시간이 모두 소진되었습니다. 프로그램을 종료합니다");

            // 데이터베이스에 남은 시간을 0으로 update시키기


            System.exit(0);

        } else {
            // 남은 시간 alert
            System.out.println(remainingTime / 60 + "분 남았습니다.");
        }
    }

    // 사용자의 남은 시간을 데이터베이스에서 조회하는 메서드
    private int getRemainingTime() {
        PreparedStatement getTimePs = null; // 실행할 쿼리
        ResultSet rset = null; // Select 한후 결과값 받아올 객체
        String getTimeSql = prop.getProperty("selectTime");
        try{
            getTimePs = conn.prepareStatement(getTimeSql);
            getTimePs.setString(1, currentId);
            rset = getTimePs.executeQuery();
            if(rset.next()) {
                return rset.getInt("USER_TIME");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }return 0;
    }

    // 회원 조회 및 수정(마이페이지)
    // 마이페이지 내 회원 정보 수정
    public void updateMember(int changeType, String changeContent) {
        String updateMemberSql = null;
        switch (changeType) {
            case 1:
                updateMemberSql = prop.getProperty("updatePassword");
                break;
            case 2:
                updateMemberSql = prop.getProperty("updateName");
                break;
            case 3:
                updateMemberSql = prop.getProperty("updateNumber");
                break;
            default:
                System.out.println("잘못 입력하셨습니다.");
                break;
        }
        try {
            PreparedStatement updateMemberPs = conn.prepareStatement(updateMemberSql);
            updateMemberPs.setString(1, changeContent);
            updateMemberPs.setString(2, currentId);
            updateMemberPs.executeUpdate();
            System.out.println("회원 정보 수정이 완료되었습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 내 정보
    public void myPage(String currentId) {
        PreparedStatement myPagePs = null; // 실행할 쿼리
        ResultSet rset = null; // Select 한후 결과값 받아올 객체
        String myPageSql = prop.getProperty("selectById");

        try {
            myPagePs = conn.prepareStatement(myPageSql);
            //System.out.println(1);
            myPagePs.setString(1, currentId);
            //System.out.println(2);
            rset = myPagePs.executeQuery();
            //System.out.println(3);
            while (rset.next()) {
                //System.out.println(4);
                String id = rset.getString("USER_ID");
                //System.out.println(5);
                String name = rset.getString("USER_NAME");
                //System.out.println(6);
                String num = rset.getString("USER_TEL");
                //System.out.println(7);
                int time = rset.getInt("USER_TIME");
                //System.out.println(8);

                // 가져온 데이터 출력 또는 처리
                System.out.println("ID: " + id);
                System.out.println("이름: " + name);
                System.out.println("번호: " + num);
                System.out.println("남은 시간: " + time/60/60 + "시간");
                System.out.println();
                //System.out.println(9);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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