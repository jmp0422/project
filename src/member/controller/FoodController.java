package member.controller;

import member.model.dao.FoodDao;
import member.model.dto.FoodDto;
import member.model.dto.MemberDto;
import member.model.dto.OrderDto;
import member.service.FoodService;
import member.service.OrderService;
import member.view.AdminView;
import run.Main;

import java.util.ArrayList;
import java.util.Scanner;

public class FoodController {

    private static FoodService foodService = new FoodService();
    private static OrderService orderService = new OrderService();
    private static FoodOrderController foodOrderController = new FoodOrderController();
    static Scanner scanner = new Scanner(System.in);

    public void foodManage() {

        int choice;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 관리>");
        System.out.println("1.열람   2.추가   3.수정   4.삭제   0.이전화면");

        System.out.print("번호선택 >>> ");
        choice = scanner.nextInt();

        switch (choice) {
            case 0:
                AdminView.adminView();
                break;
            case 1:
                selectAllFood();
                break;
            case 2:
                insertFood();
                break;
            case 3:
                updateFood();
                break;
            case 4:
                deleteFood();
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                foodManage();
                break;
        }
    }

    private void selectAllFood() {

        int choice;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 열람>");

        ////////////////////////////////////////////////////////////////////////


        ArrayList<FoodDto> list;

        list = foodService.selectAllFood();

        if (!list.isEmpty()) {
            System.out.println("\n조회된 전체 음식메뉴 정보는 다음과 같습니다.");
            System.out.println("\n상품번호\t상품명\t가격");
            System.out.println("*********************************************************");




            for (FoodDto f : list) {

                System.out.println(f);
            }

            System.out.println("*********************************************************");
        } else {
            System.out.println("조회된 데이터가 없습니다.");
        }

        ///////////////////////////////////////////////////////////////////////

//        System.out.println("0. 이전화면");
//        System.out.print("번호선택 >>> ");
//        choice = scanner.nextInt();
//
//        switch (choice) {
//            case 0:
//                foodManage();
//                break;
//            default:
//                System.out.println("잘못된 입력입니다.");
//                selectAllFood();
//                break;
//        }

        foodManage();
    }

    public static void foodMenuChoice() {
        System.out.println();
        System.out.println("1. 음식 메뉴 주문");
        System.out.println("2. 음식 주문내역 조회");
        System.out.println("3. 이전메뉴 돌아가기");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()) {
            case 1:
                System.out.println();
                foodMenu();
                break;

            case 2:
                System.out.println();
                Member_selectByCurrentOrder();
                break;
            case 3:
                System.out.println();
                Main.mainView2();
                break;
        }
    }

    private static void Member_selectByCurrentOrder() {

        int choice;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<현재 접수된 주문 목록>");

        ////////////////////////////////////////////////////////////////////////

        ArrayList<OrderDto> list;


        list = orderService.Member_selectByCurrentOrder();
        //selectByCompletedOrder=SELECT * FROM TBL_ORDER WHERE SERVING = 'y'
        //selectByCurrentOrder=SELECT * FROM TBL_ORDER WHERE SERVING = 'n'

        if (!list.isEmpty()) {
            System.out.println("\n조회된 전체 음식메뉴 정보는 다음과 같습니다.");
            System.out.println("\n상품번호\t상품명\t가격");
            System.out.println("주문번호 / 주문시간 / 회원아이디 / 음식이름 / 개수 / 총가격");
            System.out.println("*********************************************************");
            int i = 1;
            for (OrderDto o : list) {

                System.out.print("주문내역 " + i + "번");
                System.out.println(o);
                i++;
            }

            System.out.println("*********************************************************");
        } else {
            System.out.println("조회된 데이터가 없습니다.");
        }

        ///////////////////////////////////////////////////////////////////////

        System.out.println();
        foodMenuChoice();
    }

    public static void foodMenu() {

        int choice;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 열람>");

        ////////////////////////////////////////////////////////////////////////


        ArrayList<FoodDto> list;

        list = foodService.selectAllFood();

        if (!list.isEmpty()) {
            System.out.println("\n조회된 전체 음식메뉴 정보는 다음과 같습니다.");
            System.out.println("\n상품번호\t상품명\t가격");
            System.out.println("*********************************************************");

            for (FoodDto f : list) {
                System.out.println(f);
            }

            System.out.println("*********************************************************");
        } else {
            System.out.println("조회된 데이터가 없습니다.");
        }

        foodOrder();
    }

    public static void foodOrder() {
        // 2. 주문받기(시작)
        String foodName;
        int cnt;

        // 입력한 값 받기
        System.out.print("주문할 음식이름 : ");
        foodName = scanner.next();
        System.out.print("주문할 개수 : ");
        cnt = scanner.nextInt();

        FoodDto foodDto = null;
        //selectFoodPriceByFoodName()메소드로 음식가격을 담은 DTO를 가져옴
        foodDto = selectFoodPriceByFoodName(foodName);
        int foodPrice = foodDto.getItem_price();
        int totalPrice = foodPrice * cnt;

        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(MemberDto.getId()); // currentId는 View - AdminMenu, Memeber 클래스에 전역변수로 선언되어있음.
        orderDto.setItem_name(foodName);
        orderDto.setCnt(cnt);
        orderDto.setTotal_price(totalPrice);
        //        private int order_no;
        //        private Date order_time;
        //        private String user_id;
        //        private String item_name;
        //        private int cnt;
        //        private int total_price;
        //        private String serving;


        // DTO 객체 컨트롤러에 전달하기
        int result = foodOrderController.foodOrder(orderDto);

        // result 가 1 이면, 메뉴추가 성공이라고 띄우고, 메뉴추가 실패
        if (result == 1) {
            System.out.println("주문 성공!!");
        } else {
            System.out.println("주문 실패!!");
        }
        System.out.println();
        System.out.println("이전메뉴로 돌아갑니다.");
        run.Main.mainView2();
    }

    private static FoodDto selectFoodPriceByFoodName(String item_name) {
        //foodDto1 은 음식이름을 담아서 Dao까지 보냄
        FoodDto foodDto1 = new FoodDto();
        foodDto1.setItem_name(item_name);

        //foodDto2 는 Dao에서부터 음식가격을 가져옴.
        FoodDto foodDto2 = null;
        foodDto2 = foodOrderController.selectFoodPriceByFoodName(foodDto1);
        return foodDto2;
    }

    private void insertFood() {

        String foodName;
        int price;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 추가>");
        System.out.print("음식이름 : ");
        foodName = scanner.next();
        System.out.print("가격 : ");
        price = scanner.nextInt();
        ////////////////////////////////////////////////////////////////////////

        // 음식메뉴 추가
        //1. 입력한 값 받기
        //2. DTO 에 값 담기
        FoodDto foodDto = new FoodDto();
        foodDto.setItem_name(foodName);
        foodDto.setItem_price(price);

        //3. 다오객체 생성해서 INSERT 만들어서 전달하기
        FoodDao foodDao = new FoodDao();
        int result = foodDao.insertFood(foodDto);

        //4. result 가 1 이면, 메뉴추가 성공이라고 띄우고, 메뉴추가 실패
        if (result == 1) {
            System.out.println("메뉴추가 성공!!");
            System.out.println("음식메뉴 \"" + foodName + "\"가 추가되었습니다!");
        } else {
            System.out.println("메뉴추가 실패!!");
        }

        ///////////////////////////////////////////////////////////////////////

        foodManage();


    }

    private void updateFood() {

        String foodName1;
        String foodName2;
        int price2;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 수정>");
        System.out.print("수정할 음식이름 : ");
        foodName1 = scanner.next();
        System.out.println("(수정을 시작하겠습니다)");
        System.out.print("음식이름 : ");
        foodName2 = scanner.next();
        System.out.print("가격 : ");
        price2 = scanner.nextInt();

        ////////////////////////////////////////////////////////////////////////

        // 음식메뉴 수정
        //1. 입력한 값 받기
        //2. DTO 에 값 담기
        FoodDto foodDto1 = new FoodDto();
        FoodDto foodDto2 = new FoodDto();
        foodDto1.setItem_name(foodName1);
        foodDto2.setItem_name(foodName2);
        foodDto2.setItem_price(price2);

        //3. 다오객체 생성해서 update 만들어서 전달하기
        FoodDao foodDao = new FoodDao();
        int result = foodDao.updateFood(foodDto1, foodDto2);

        //4. result 가 1 이면, 메뉴추가 성공이라고 띄우고, 메뉴추가 실패
        if (result == 1) {
            System.out.println("메뉴 수정 성공!!");
        } else {
            System.out.println("메뉴 수정 실패!!");
        }

        ///////////////////////////////////////////////////////////////////////

        foodManage();
    }

    private void deleteFood() {

        String foodName;

        System.out.println("----------------------------------------------------------------");
        System.out.println("<음식메뉴 삭제>");
        System.out.print("삭제할 음식이름 : ");
        foodName = scanner.next();

        ////////////////////////////////////////////////////////////////////////

        // 음식메뉴 삭제
        //1. 입력한 값 받기
        //2. DTO 에 값 담기
        FoodDto foodDto = new FoodDto();
        foodDto.setItem_name(foodName);

        //3. 다오객체 생성해서 delete 만들어서 전달하기
        FoodDao foodDao = new FoodDao();
        int result = foodDao.deleteFood(foodDto);

        //4. result 가 1 이면, 메뉴추가 성공이라고 띄우고, 메뉴추가 실패
        if (result == 1) {
            System.out.println("메뉴 삭제 성공!!");
        } else {
            System.out.println("메뉴 삭제 실패!!");
        }

        ///////////////////////////////////////////////////////////////////////

        foodManage();
    }


}


