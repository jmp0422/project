package member.view;

import member.controller.ChargeTimeController;

import java.sql.SQLException;
import java.util.Scanner;

public class ChargeTime {

    static Scanner sc = new Scanner(System.in);

    public static void pay(String currentId){

            try{
                paymenu();
                int input= sc.nextInt();
                int time = 0;
                switch (input){
                    case 1:
                        chargetime(1, 1000);
                        time = 60 * 60; // 60 * 60초 로 DB로 들어감
                        break;
                    case 2:
                        chargetime(3, 3000);
                        time = 180 * 60;
                        break;
                    case 3:
                        chargetime(6, 5000);
                        time = 360 * 60;

                        break;
                    case 4:
                        chargetime(15, 10000);
                        time = 900 * 60;
                        break;
                    case 5:
                        System.out.println("===================================");
                        System.out.println("충전을 취소하고, 마이페이지로 이동합니다.");
                        System.out.println("===================================");
                        Member.myPage();
                        return;
                }
//                memberController.searchMember(new MemberDto(id, pw));
                //ChargeTimeDto chargeTimeDto = new ChargeTimeDto(MemberDto.getId(),time);
                //chargeTimeDto.setTime(time);

                ChargeTimeController chargeTimeController = new ChargeTimeController();
                chargeTimeController.ChargeTime(currentId, time);
                //chargeTimeController.ChargeTime("11", 4000);
            }catch (Exception e){
                System.out.println("잘못된 입력입니다. 다시 확인해 주세요");
                return;
            }

    }

    static void paymenu(){
        System.out.println("========충전 메뉴========");
        System.out.println("충전할 시간을 선택하세요."  );
        System.out.println("1. 1시간 = 1,000원"  );
        System.out.println("2. 3시간 = 3,000원"  );
        System.out.println("3. 6시간 = 5,000원"  );
        System.out.println("4. 15시간 = 10,000원"  );
        System.out.println("5. 이전 메뉴로"  );
    }
    static void chargetime(int hour, int price) throws SQLException {
        System.out.println("=========================================");
        System.out.println("["+hour+"시간] 충전되었습니다.\n **마이페이지로 다시 이동합니다**\n 감사합니다");
        System.out.println("=========================================");
        //Member.myPage();
    }
}