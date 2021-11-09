let drul1List = document.querySelectorAll(".drul1 > li");
let drul2List = document.querySelectorAll(".drul2 > li");

let drPlaceholder1 = document.getElementById("dropdownMenuButton1");
let drPlaceholder2 = document.getElementById("dropdownMenuButton2");

let currencyCode;
let paymentChannel;

let desc = document.querySelector(".description > h3").textContent;
let amt = document.querySelector(".description > h5").textContent;
amt = amt.substring(amt.length - 2);

let btn_2C2P = document.querySelector(".btn-2C2P");

let curCode = drPlaceholder1.textContent;

let paymentToken = {
  merchantID: "JT01",
  invoiceNo: "1523953661",
  description: desc,
  amount: parseFloat(amt).toFixed(2),
  currencyCode: 0,
  paymentChannel: [],
};

for (const i of drul1List) {
  let ctx = i.textContent.split('-')[0].replace(' ', '');
  i.addEventListener("click", () => {
    unfilter();
    drPlaceholder2.textContent = 'PaymentChannel';
    drPlaceholder1.textContent = ctx;
    paymentToken.currencyCode = ctx;
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

function submitRequestParameter() {
  $.ajax({
    url: encodeURI("/demo2c2p/generateJWTToken"),
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(paymentToken),
  });}