package member.view;

import member.controller.FoodController;
import member.controller.OrderController;
import run.Main;

import java.util.Scanner;

public class AdminView {
    private static Scanner scanner = new Scanner(System.in);
    //private MemberController memberController = new MemberController();
    private static FoodController foodController = new FoodController();
    private static OrderController orderController = new OrderController();

    public static void adminView() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("메뉴를 선택하세요");
        System.out.println("1. 회원관리");
        System.out.println("2. 음식메뉴관리");
        System.out.println("3. 주문처리");
        System.out.println("4. 로그아웃");

        switch (scanner.nextInt()) {
            case 1:
                adminManage();
                break;
            case 2:
                foodController.foodManage();
                break;
            case 3:
                orderController.orderManage();
                break;
            case 4:
                Main.mainView();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                adminView();
                break;
        }
    }

    public static void adminManage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("메뉴를 선택하세요");
        System.out.println("1. 회원목록열람");
        System.out.println("2. 회원추가");
        System.out.println("3. 회원수정");
        System.out.println("4. 회원삭제");
        System.out.println("5. 취소");

        switch (scanner.nextInt()) {
            case 1:
                Member.memberList();
                break;
            case 2:
                Member.signUp();
                System.out.println("회원정보가 추가되었습니다.");
                System.out.println();
                adminManage();
                break;
            case 3:
                Member.modifyMember();
                System.out.println("회원정보가 수정되었습니다.");
                System.out.println();
                adminManage();
                break;
            case 4:
                Member.delete();
                System.out.println("회원정보가 삭제되었습니다.");
                System.out.println();
                adminManage();
                break;
            case 5:
                System.out.println("이전메뉴로 돌아갑니다.");
                System.out.println();
                adminView();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                System.out.println();
                System.out.println();
                adminManage();
                break;
        }

    }


}
