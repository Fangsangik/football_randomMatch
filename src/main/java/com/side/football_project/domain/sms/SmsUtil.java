package com.side.football_project.domain.sms;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SmsUtil {

    /** application.yml-에 설정된 발신번호를 주입 */
    @Value("${sms.phoneNumber}")
    private String fromPhoneNumber;

    private final Message messageService;
    private final SmsRepository smsLogRepository;

    /**
     * SMS/LMS 전송 후, 결과를 DB에 저장
     * @param to   수신번호 ("01012345678" 형태로 입력)
     * @param text 보낼 메시지 내용
     */
    public void sendAndLog(String to, String text) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromPhoneNumber);
        params.put("to", to);
        params.put("text", text);
        params.put("type", text.length() < 45 ? "sms" : "lms");

        JSONObject result;
        String status;
        try {
            result = messageService.send(params);
            Object statusObj = result.get("status");
            status = (statusObj != null && statusObj.toString().equals("true")) ? "success" : "fail";
        } catch (CoolsmsException e) {
            status = "error";
            result = new JSONObject();
            result.put("errorMessage", e.getMessage());
        }

        saveLog(to, text, status, result.toJSONString());
    }

    /**
     * 대량 메시지 전송 + 개별 로그 저장
     * @param requests List of SmsRequestDto(to, text)
     */
    public void sendBulkAndLog(List<SmsRequestDto> requests) {
        for (SmsRequestDto request : requests) {
            sendAndLog(request.getTo(), request.getText());
        }
    }

    /** 로그 저장용 공통 메서드 */
    private void saveLog(String to, String text, String status, String responseBody) {
        SmsLog log = SmsLog.builder()
                .recipient(to)
                .messageText(text)
                .messageType(text.length() < 45 ? "sms" : "lms")
                .status(status)
                .responseBody(responseBody)
                .build();
        smsLogRepository.save(log);
    }
}
