package member.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static common.DBConnection.getConnection;

public class ChargeTimeDao {
    private Properties prop = null;
    private Connection conn = null;

    public ChargeTimeDao() {
        try {
            prop = new Properties();
            prop.load(new FileReader("resources/ChargeTimeQuery.properties"));
            conn = getConnection();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   // 여기까지 기본 세팅, 이후 메서드 작성하시면 됩니다.


    // 예시
    // public void 시간결제?(전 기능 몰라용) (){

    // }


    // public void 시간충전? (){
    public void ChargeTime(String currentId, int time) {

//        System.out.println(33333);
        int result = 0;
        PreparedStatement ps = null;


        try {
//            System.out.println(2);
            String sql = prop.getProperty("chargeTime");
            ps = conn.prepareStatement(sql);
            //ps.setInt(1, chargeTimeDto.getTime());
            //ps.setString(2, MemberDto.getId());
//            System.out.println(3);
            ps.setInt(1, time);
            ps.setString(2,currentId);

//            System.out.println(4);
            result = ps.executeUpdate();
            if (result > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("시간충전 중 에러발생");

            try {
                conn.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }

    }


}
