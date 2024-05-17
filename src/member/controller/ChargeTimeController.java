package member.controller;

import member.service.ChargeTimeService;

public class ChargeTimeController {
    ChargeTimeService ChargeTimeService = new ChargeTimeService();


    public void ChargeTime(String currentId, int time) {

        ChargeTimeService.ChargeTime(currentId, time);
    }
}


