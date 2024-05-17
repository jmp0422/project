package member.service;

import member.model.dao.MemberDao;
import member.model.dto.MemberDto;
import run.Main;

public class MemberService {
    MemberDao memberDao = null;

    public MemberService() {
        memberDao = new MemberDao();
    }

    public void addMember(String id, String pw, String name, String number) {
        memberDao.addMember(id, pw, name, number);
    }

    public void adminLogin(String id, String pw) {
        memberDao.adminLogin(id, pw);
    }

    public void modifyMember(String id, String pw, String name, String number) {
        memberDao.modifyMember(id, pw, name, number);
    }

    public void delete(String id) {
        memberDao.delete(id);
    }

    public void memberList() {
        memberDao.memberList();
    }

    public boolean searchMember(MemberDto memberDto) {
        return memberDao.searchMember(memberDto);
    }
    public void updateMember(int changeType, String changeContent) {
        memberDao.updateMember(changeType, changeContent);
    }
    public void signOut(){
        System.out.println("로그아웃 되었습니다.");
        System.out.println();
        Main.mainView();

        //데이터베이스에 남은시간 update시키기

        // currentId = null;   // 아이디 비우기
//        System.exit(0); // 프로그램 종료

    }
    public void myPage(String currentId) {
        memberDao.myPage(currentId);
    }

    public void deleteMember(String id) {
        memberDao.deleteMember(id);
    }

    public void addMember(MemberDto memberDto) {
        memberDao.addMember(memberDto);
    }
}