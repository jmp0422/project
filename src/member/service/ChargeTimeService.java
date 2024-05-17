package member.service;

import member.model.dao.ChargeTimeDao;

public class ChargeTimeService {
    ChargeTimeDao chargeTimeDao = new ChargeTimeDao();


    public void ChargeTime(String currentId, int time) {
//        System.out.println(22222);

        chargeTimeDao.ChargeTime(currentId, time);
    }
}

