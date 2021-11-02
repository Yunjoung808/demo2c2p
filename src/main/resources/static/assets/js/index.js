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

let paymentToken = {
  merchantID: "JT01",
  invoiceNo: "1523953661",
  description: desc,
  amount: parseFloat(amt).toFixed(2),
  currencyCode: 0,
  paymentChannel: [],
};

for (const i of drul1List) {
  let ctx = i.textContent;
  i.addEventListener("click", () => {
    drPlaceholder1.textContent = ctx;
    paymentToken.currencyCode = ctx;
  });
}

for (const i of drul2List) {
  let ctx = i.textContent;
  i.addEventListener("click", () => {
    drPlaceholder2.textContent = ctx;
    paymentToken.paymentChannel.push(ctx);
  });
}

// btn_2C2P.addEventListener("click", () => {
//   const dataToSend = JSON.stringify(paymentToken);
//   fetch("/demo2c2p/generateJWTToken", {
//     credentials: "same-origin",
//     mode: "same-origin",
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: dataToSend,
//   })
//     .then((response) => {
//       return response.json();
//     })
//     .then((result)=>{
//         console.log("result");
//     })
// });

// btn_2C2P.addEventListener("click", () => {
//   // Creating a XHR object
//   let xhr = new XMLHttpRequest();
//   let url = "../server/src/main/java/com/test2c2p/sample/Server.java";

//   // open a connection
//   xhr.open("POST", url, true);

//   // Set the request header i.e. which type of content you are sending
//   xhr.setRequestHeader("Content-Type", "application/json");

//   // Create a state change callback
//   xhr.onreadystatechange = function () {
//     if (xhr.readyState === 4 && xhr.status === 200) {
//       // Print received data from server
//       alert(this.responseText);
//     }
//   };

//   // Converting JSON data to string
//   var data = JSON.stringify(paymentToken);

//   // Sending data with the request
//   xhr.send(data);
// });

function submitRequestParameter() {
  // const postData = {
  //   merchantID: "JT01",
  //   invoiceNo: "1523953661",
  //   description: desc,
  //   amount: parseFloat(amt).toFixed(2),
  //   currencyCode: "SGD",
  //   paymentChannel: ["CC"],
  // };

  $.ajax({
    url: encodeURI("/demo2c2p/generateJWTToken"),
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(paymentToken),
  });}