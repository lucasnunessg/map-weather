let saldoConta1 = 100;
let limiteConta1 = 0;

let saldoConta2 = 500;
let limiteConta2 = 0;


function depositarConta1(valor){ 

  saldoConta1 += valor;
  if(saldoConta1 >= 1000 && limiteConta1 === 0 ) {
    limiteConta1 = saldoConta1 * 0.1
  }

}

function depositarConta2(valor) {
  saldoConta2 += valor;
  if(saldoConta2 >= 1000 && limiteConta2 === 0 ) {
    limiteConta2 = saldoConta2 * 0.1
  }
}

function saldoAccount1(){
  console.log(saldoAccount1)
  if(saldoAccount1 === 0 ) {
    return "Saldo zerado, o limite começou a ser utilizado!"
  }
}

function saldoAccount2(){
  console.log(saldoAccount2)
  if(saldoAccount2 === 0 ) {
    return "Saldo zerado, o limite começou a ser utilizado!"
  }
}

function saldoTotal() {
  const saldo = saldoAccount1 + saldoAccount1;
  console.log("Saldo total das duas contas: " + saldo);
}

function debitarConta1(valor) {

  let totalDisponivel = saldoAccount1 + limiteConta1;
  
  if(valor > totalDisponivel) {
    return "Não é possível debitar, saldo insuficiente."
  }
  
  if (valor <= saldoAccount1) {
    saldoAccount1 -= valor;
  }else {
    let restante = valor - saldoAccount1;
    saldoAccount1 = 0;
    limiteConta1 -= restante;
  }


}


function debitarConta2(valor) {

  let totalDisponivel = saldoAccount2 + limiteConta2;
  
  if(valor > totalDisponivel) {
    return "Não é possível debitar, saldo insuficiente."
  }
  
  if (valor <= saldoAccount2) {
    saldoAccount2 -= valor;
  }else {
    let restante = valor - saldoAccount2;
    saldoAccount2 = 0;
    limiteConta2 -= restante;
  }


}

function transferBetweenAccount1To2(valor){

let totalDisponivelC1 = saldoConta1 + limiteConta1;
if(valor > totalDisponivelC1 ) {
  
    return "Não há saldo ou limite disponível para transferir."
      
} if (valor <= saldoConta1) {
  saldoConta1 -= valor;
} else {
  let restante = valor - saldoConta1;
  saldoConta1 = 0;
  limiteConta1 -= restante;
}

saldoConta2 += valor


}



console.log("c1 = " + saldoConta1);
console.log("c2 = " + saldoConta2);


transferBetweenAccount1To2(50)

console.log("c1 = " + saldoConta1);
console.log("c2 = " + saldoConta2);




