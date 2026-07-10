const input = document.getElementById("input");
const output = document.getElementById("output");
const botao = document.getElementById("send");


const chatslist = document.getElementById("chatslist");


const uid = 1;

function open_chat(where){
    const sock = new WebSocket(where);

    sock.onopen = () => {
    }

    sock.onmessage = (e) => {
        output.value = output.value + "\n" +  e.data.toString();
    }

    sock.onerror = (erro) => {
        console.error("erro");
    };

    botao.addEventListener("click", (e) => {
        sock.send(input.value);
    });
}
open_chat(`ws://localhost:2000/${uid}`);




async function fetch_chats(){
    try{
        const resp = await fetch("http://localhost:3000/chats/list");

        if (!resp.ok) throw new Error("se fuder")

        const data = await resp.json();

        chatslist.replaceChildren();
        data.forEach(e => {
            const nb = document.createElement("li");

            const btn = document.createElement("button");
            btn.className = "entrarchatbtn";
            btn.textContent = e;

            btn.addEventListener("click", async f => {
                await fetch(`http://localhost:3000/chats/entrar/${e}/${uid}`);
                output.value = `Chat: ${e}`;
            });
            
            nb.appendChild(btn);
            chatslist.append(nb);
        });
    }
    catch (e){
        console.log(e);
    }
}
fetch_chats();
setInterval(fetch_chats, 3000);