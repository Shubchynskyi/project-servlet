function removeOnclickFromTd() {
    let tdArray = document.getElementsByClassName("td");

    for (let i = 0; i < tdArray.length; i++) {
        tdArray.item(i).setAttribute("onclick","");
    }
}