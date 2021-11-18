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

    private String selectMerchant(String invoiceNo){
        int invoiceStart = 0;
        int invoiceEnd = 3;
        String country = invoiceNo.substring(invoiceStart,invoiceEnd);
        String merchantID;
        switch (country) {
            case "SGD":
              merchantID = "702702000001670";
              break;
            case "PHP":
              merchantID = "608608000000685";
              break;
            case "MYR":
              merchantID = "458458000001107";
              break;
            case "MMK":
              merchantID = "104104000000550";
              break;
            case "THB":
              merchantID = "764764000009889";
              break;
            case "VND":
              merchantID = "704704000000046";
              break;

            default:
                merchantID ="ND";
        }
        return merchantID;
    }

    public String send(PaymentActionRequest paymentActionRequest) throws Exception{
        String invoiceNo = paymentActionRequest.getInvoiceNo();
        String version = paymentActionRequest.getVersion();
        String processType = paymentActionRequest.getProcessType();
        String actionAmount = paymentActionRequest.getActionAmount();
        String jsMerchantID = paymentActionRequest.getMerchantID();
        String merchantID = selectMerchant(invoiceNo);
        merchantID = (merchantID.equals("ND")) ? (jsMerchantID) : merchantID;
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
