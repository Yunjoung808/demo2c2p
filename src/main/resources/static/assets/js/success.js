var cancelParam = {
    paymentToken:
      "kSAops9Zwhos8hSTSeLTUaZRFU0dixjVnk7f2UA6UaV+wgb6f9y6NZ9mSHiW6envNwV/XN+4cfJy56xZqHcN50Zno67F++V1N+IgxsGVTSWCLnMHdQolRmUZHkz8Uec9",
    clientID: "855ca0eeed4248af960bdef71151cd82",
    invoiceNo: "",
  };
  
  var paymentActionParam = {
    version: "3.4",
    merchantID: "702702000001670",
  };

var refundParam = {
    invoiceNo: "",
    actionAmount:"",
};

function submitCancelParameter() {
    paymentActionParam.invoiceNo = document.getElementById('invoice').value;
    paymentActionParam.processType = "V";
    $.ajax({
      url: encodeURI("/demo2c2p/inquiry"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(paymentActionParam),
      success: function (data, textStatus, xhr) {
        window.location = xhr.getResponseHeader("Location");
      },
    });
  }

  function submitInquiryParameter(){
    paymentActionParam.invoiceNo = document.getElementById('invoice').value;
    paymentActionParam.processType = "I";
    $.ajax({
      url: encodeURI("/demo2c2p/inquiry"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(paymentActionParam),
      success: function (data, textStatus, xhr) {
        window.location = xhr.getResponseHeader("Location");
      },
    });
  }
  
  function startPaymentInquiry() {
    paymentInquiryParam.invoiceNo = document.getElementById('invoice').value;
    $.ajax({
      url: encodeURI("/demo2c2p/paymentInquiry"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(paymentInquiryParam),
      success: function (data, textStatus, xhr) {
        window.location = xhr.getResponseHeader("Location");
      },
    });
  }

  function refundAction() {
    refundParam.invoiceNo = document.getElementById('invoice').value;
    refundParam.actionAmount = document.getElementById('amount').value;
    $.ajax({
      url: encodeURI("/demo2c2p/refundwhateverblahblah"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(refundParam),
    });
  }
