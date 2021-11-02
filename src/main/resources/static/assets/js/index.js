
function submitRequestParameter(){

  const postData = {
    merchantID : "JT01",
    invoiceNo : "1523953661",
    description : "item 1",
    amount : 1000.00 ,
    currencyCode : "SGD",
    paymentChannel : ["CC"]
  }

  $.ajax({
    url: encodeURI('/demo2c2p/generateJWTToken'),
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(postData)
})

}
