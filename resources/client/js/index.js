function pageLoad() {
    let myHTML = '<div style="text-align:center;">'
        + '<h1>Quizzingo</h1>'
        + '<a href="register.html"><button class="loginButton" id ="loginButton" style="margin:5px;">Register</button></a>'
        + '<a href="login.html"><button class="loginButton" id="loginButton" style="margin:5px;">Login</button></a>'

    document.getElementById("menuButtons").innerHTML = myHTML;
    checkLogin()


}

function checkLogin(){
    let username = Cookies.get("Username");

    let logInHTML = '';

    if(username === undefined){

        let loginButtons=document.getElementsByClassName("loginButton");
        for(let button of loginButtons){
            button.style.visibility = "visible";
        }

        let logoutButtons=document.getElementsByClassName("logoutButton");
        for(let button of logoutButtons){
            button.style.visibility = "hidden";
        }


    } else{

        let loginButtons = document.getElementsByClassName("loginButton");
        for(let button of loginButtons){
            button.style.visibility= "hidden";

        }



        logInHTML="Logged in as " + username + ". <a href='login.html?logout'>Logout</a>"
    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;
}
