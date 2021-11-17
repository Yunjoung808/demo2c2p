package com.test.demo2c2p.service;

import com.test.demo2c2p.api.request.PaymentActionRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentActionService {

    private final CodecService codecService;
    private final HttpService httpService;

    public String send(PaymentActionRequest paymentActionRequest) throws Exception{
        String invoiceNo = paymentActionRequest.getInvoiceNo();
        String version = paymentActionRequest.getVersion();
        String processType = paymentActionRequest.getProcessType();
        String merchantID = paymentActionRequest.getMerchantID();
        String actionAmount = paymentActionRequest.getActionAmount();
        //https://developer.2c2p.com/docs/status-inquiry

        String toHash;
        if (processType.equals("I") || processType.equals("V") || processType.equals("RS")){
            toHash = version + merchantID + processType + invoiceNo;
        }
        else if (processType.equals("R")){
            toHash = version + merchantID + processType + invoiceNo + actionAmount;
        }
        else{
            return "error";
        }
        String hashed = codecService.hashHMAC(toHash);
        String xml;
        if (processType.equals("I") || processType.equals("V") || processType.equals("RS")){
            xml = String.format("<PaymentProcessRequest><version>%s</version><merchantID>%s</merchantID><processType>%s</processType><invoiceNo>%s</invoiceNo><hashValue>%s</hashValue></PaymentProcessRequest>",version,merchantID,processType,invoiceNo,hashed);
        }
        else if (processType.equals("R")){
            xml = String.format("<PaymentProcessRequest><version>%s</version><merchantID>%s</merchantID><processType>%s</processType><invoiceNo>%s</invoiceNo><actionAmount>%s</actionAmount><hashValue>%s</hashValue></PaymentProcessRequest>",version,merchantID,processType,invoiceNo,actionAmount,hashed);
        }
        else{
            return "error";
        }
        //encode xml
        String encoded = codecService.encodeString(xml);
        String response = httpService.sendPaymentActionAPIRequest(encoded);
        String decoded = codecService.decodeString(response);
        return decoded;


    }
}
