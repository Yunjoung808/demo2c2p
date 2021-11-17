var cancelParam = {
    paymentToken:
      "kSAops9Zwhos8hSTSeLTUaZRFU0dixjVnk7f2UA6UaV+wgb6f9y6NZ9mSHiW6envNwV/XN+4cfJy56xZqHcN50Zno67F++V1N+IgxsGVTSWCLnMHdQolRmUZHkz8Uec9",
    clientID: "855ca0eeed4248af960bdef71151cd82",
    invoiceNo: "",
  };
  
  var paymentInquiryParam = {
    paymentToken:
      "kSAops9Zwhos8hSTSeLTUaZRFU0dixjVnk7f2UA6UaV+wgb6f9y6NZ9mSHiW6envNwV/XN+4cfJy56xZqHcN50Zno67F++V1N+IgxsGVTSWCLnMHdQolRmUZHkz8Uec9",
    merchantID: "702702000001662",
    invoiceNo: "123451039",
  };

var refundParam = {
    invoiceNo = "",
    actionAmount = "",
};

function submitCancelParameter() {
    cancelParam.invoiceNo = document.getElementById('invoice').value;
    $.ajax({
      url: encodeURI("/demo2c2p/cancel"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(cancelParam),
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
