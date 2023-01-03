function restart() {
    fetch("/restart", {
        method: "POST",
        headers: {
            'content-type': 'application/json;charset=UTF-8'
        }
    }).then(location.reload())
}

function removeOnclickFromTd() {
    let tdArray = document.getElementsByClassName("td");

    for (let i = 0; i < tdArray.length; i++) {
        tdArray.item(i).setAttribute("onclick","");
    }
}