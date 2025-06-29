package com.side.football_project.domain.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsUtil smsUtil;

    @ResponseBody
    @PostMapping("/send")
    public ResponseEntity<Void> sendSms(@RequestBody SmsRequestDto request) {
        smsUtil.sendAndLog(request.getTo(), request.getText());
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/send-bulk")
    public ResponseEntity<Void> sendBulkSms(@RequestBody java.util.List<SmsRequestDto> requests) {
        smsUtil.sendBulkAndLog(requests);
        return ResponseEntity.ok().build();
    }
}
