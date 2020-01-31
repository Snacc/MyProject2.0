function pageLoad(){
    document.getElementById('registerButton').addEventListener("click", register);
}

function register(event){ //register(event) similar to the login function
    event.preventDefault();

    const form = document.getElementById("registerForm"); //form defined
    const formData = new FormData(form);//form data defined

    fetch("/user/register", {method: 'post', body: formData} //fetches register API from the User.java class, if any issues with input check the API/HTML.
    ).then(response => response.json() //converted to json
    ).then(responseData => {

        if(responseData.hasOwnProperty('error')){
            alert(responseData.error); //if response contains 'error' then an alert is returned to user

        } else{

            window.location.href = '/client/index.html'; //new record created and redirected back to index.html.
        }
    });
}