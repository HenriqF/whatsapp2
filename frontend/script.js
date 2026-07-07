const input = document.getElementById("input");
const output = document.getElementById("output");
const botao = document.getElementById("send");


const sock = new WebSocket("ws://localhost:2000");

sock.onopen = () => {
}

sock.onmessage = (e) => {
    output.value = output.value + "\n" +  e.data.toString();
}

sock.onerror = (erro) => {
    console.error("erro");
};

sock.onclose = () => {
};



botao.addEventListener("click", (e) => {
    sock.send(input.value);
});