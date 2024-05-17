package member.model.dto;

public class ChargeTimeDto {

//    public ChargeTimeDto(){}

    public ChargeTimeDto(String userid, int time) {
        this.userid = userid;
        this.time = time;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private String userid;
    private int time;

    @Override
    public String toString() {
        return "ChargeTimeDto{" +
                "userid='" + userid + '\'' +
                ", time=" + time +
                '}';
    }
}




