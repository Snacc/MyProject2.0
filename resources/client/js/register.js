function pageLoad(){
    document.getElementById('registerButton').addEventListener("click", register);
}

function register(event){
    event.preventDefault();

    const form = document.getElementById("registerForm");
    const formData = new FormData(form);

    fetch("/user/register", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if(responseData.hasOwnProperty('error')){
            alert(responseData.error);

        } else{

            window.location.href = '/client/index.html';
        }
    });
}