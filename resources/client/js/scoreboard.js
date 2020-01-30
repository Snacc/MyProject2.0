function pageLoad() {
    let leaderboardHTML =
        '<table align="center">' +
        '<tr>' +
        '<th class="headings">Username</th>' +
        '<th class="headings">Score</th>' +
        '</tr>';


    fetch('/leaderboard/list', {method: 'get'}
    ).then(response => response.json()
    ).then(positions => {
        if(positions.hasOwnProperty('error')){
            alert(positions.error);
        } else{
            for (let position of positions){
                leaderboardHTML += '<tr>' +
                    '<td>${position.Username}</td>' +
                    '<td>${position.Score}</td>';

            }

        }
        leaderboardHTML += '</tr>';
        document.getElementById("content").innerHTML = leaderboardHTML;
    });


}

