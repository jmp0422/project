package member.controller;

import member.model.dto.MemberDto;
import member.service.MemberService;

import java.util.Scanner;

public class MemberController {
    MemberService memberService;
    static Scanner scan = new Scanner(System.in);
    static String currentId = null; // 로그아웃시 초기화해야함
    static MemberController memberController = new MemberController();

    public MemberController() {
        memberService = new MemberService();
    }

    public void addMember(String id, String pw, String name, String number) {
        memberService.addMember(id, pw, name, number);
    }

    public void adminLogin(String id, String pw) {
        memberService.adminLogin(id, pw);
    }

    public void modifyMember(String id, String pw, String name, String number) {
        memberService.modifyMember(id, pw, name, number);
    }

    public void delete(String id) {
        memberService.delete(id);
    }

    public void memberList(){
        memberService.memberList();
    }

    public boolean searchMember(MemberDto memberDto) {
        return memberService.searchMember(memberDto);
    }

    public void myPage(String currentId) {
        memberService.myPage(currentId);
    }

    public void updateMember(int changeType, String changeContent) {
        memberService.updateMember(changeType, changeContent);
    }

    public void signOut(){
        memberService.signOut();
    }

    public void addMember(MemberDto memberDto) {
        memberService.addMember(memberDto);
    }
    public void deleteMember(String id) {
        memberService.deleteMember(id);
    }
}