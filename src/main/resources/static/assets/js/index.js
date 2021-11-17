let drul1List = document.querySelectorAll(".drul1 > li");
let drul2List = document.querySelectorAll(".drul2 > li");

let drPlaceholder1 = document.getElementById("dropdownMenuButton1");
let drPlaceholder2 = document.getElementById("dropdownMenuButton2");

let currencyCode;
let paymentChannel;

let desc = document.querySelector(".description > h3").textContent;
let amt = document.querySelector(".description > h5").textContent;
let amt2 = document.querySelector(".description > h5").textContent;

amt = amt.substring(amt.length - 2);

let btn_2C2P = document.querySelector(".btn-2C2P");

let curCode = drPlaceholder1.textContent;

let ran = parseInt(Math.random() * 10000000000);

let g

let paymentToken = {
  merchantID: "702702000001662",
  invoiceNo: ran.toString(),
  description: desc,
  amount: parseFloat(amt).toFixed(2),
  currencyCode: "",
  paymentChannel: [""],
  doPayment: {
    paymentToken: "",
    responseReturnUrl: "localhost:8080/success.html",
    payment: {
      code: {
        channelCode: ""
      },
      data: {
        name: "",
        email: "",
        mobileNo: "",
        accountNo: "",
      }
    }
  }
};
let responseParam = {
  invoiceNo: "12345",
  channelCode: "GRAB",
  respCode: "2000",
  respDesc: "Transaction is complete you are finished",
};

let cancelParam = {
  paymentToken: "kSAops9Zwhos8hSTSeLTUaZRFU0dixjVnk7f2UA6UaV+wgb6f9y6NZ9mSHiW6envNwV\/XN+4cfJy56xZqHcN50Zno67F++V1N+IgxsGVTSWCLnMHdQolRmUZHkz8Uec9",
  clientID: "855ca0eeed4248af960bdef71151cd82",
};


// document.querySelector('.modal-desc').textContent = document.querySelector('.modal-desc').textContent + desc;
// document.querySelector('.modal-amt').textContent = document.querySelector('.modal-amt').textContent + amt2;
// document.querySelector('.modal-invo').textContent = document.querySelector('.modal-invo').textContent + paymentToken.invoiceNo;
// document.querySelector('.modal-invo').setAttribute("value", ran.toString());

for (const i of drul1List) {
  let ctx = i.textContent.split('-')[0].replace(' ', '');
  i.addEventListener("click", () => {
    unfilter();
    drPlaceholder2.textContent = 'PaymentChannel';
    // document.querySelector('.modal-cur').textContent = "Currency : ";
    // document.querySelector('.modal-mid').textContent = "Mid : ";
    drPlaceholder1.textContent = ctx;
    paymentToken.currencyCode = ctx;
    curCode = ctx;
    // document.querySelector('.modal-cur').textContent = document.querySelector('.modal-cur').textContent + curCode;
    // document.querySelector('.modal-cur').setAttribute("value", curCode);    
    midSet(curCode);
    // document.querySelector('.modal-mid').textContent = document.querySelector('.modal-mid').textContent + paymentToken.merchantID;
    // document.querySelector('.modal-mid').setAttribute("value", paymentToken.merchantID);
    drPlaceholder2.removeAttribute('disabled');
    filtering(ctx)
  });
}

for (const i of drul2List) {
  let ctx = i.textContent.split('-')[0].replace(' ', '');
  i.addEventListener("click", () => {
    drPlaceholder2.textContent = ctx;
    paymentToken.paymentChannel = [];
    paymentToken.paymentChannel.push(ctx);
    paymentToken.doPayment.payment.code.channelCode = ctx;
  });
}

function filtering(curCode){
  for (const i of drul2List) {    
    if (i.classList.contains(curCode) || i.classList.contains('gl')) {
      i.style.display = 'block';
    }
  }
}

function unfilter(){
  for (const i of drul2List) {
    i.style.display = 'none';
  }
}

function midSet(curCode) {
  switch (curCode) {
    case 'SGD':
      paymentToken.merchantID = '702702000001670';
      break;
    case 'PHP':
      paymentToken.merchantID = '608608000000685';
      break;
    case 'MYR':
      paymentToken.merchantID = '458458000001107';
      break;
    case 'MMK':
      paymentToken.merchantID = '104104000000550';
      break;
    case 'THB':
      paymentToken.merchantID = '764764000009889';
      break;
    case 'VND':
      paymentToken.merchantID = '704704000000046';
      break;
  }
}


function submitResponseParameter(){
  $.ajax({
    url:encodeURI("/demo2c2p/paymentconfirmation"),
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(responseParam),
    success: function (data,textStatus,xhr) { 
      window.location = xhr.getResponseHeader("Location");
    }, 
  });
}

function submitRequestParameter() {
  $.ajax({
    url: encodeURI("/demo2c2p/generateJWTToken"),
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(paymentToken),
    success: function (data,textStatus,xhr) { 
      window.location = xhr.getResponseHeader("Location");
    }, 
  });}

  function submitCancelParameter() {
    $.ajax({
      url: encodeURI("/demo2c2p/cancel"),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(cancelParam),
      success: function (data,textStatus,xhr) { 
        window.location = xhr.getResponseHeader("Location");
      }, 
    });}