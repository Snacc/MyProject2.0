function pageLoad() {
    let leaderboardHTML = //table defined
        '<table align="center">' +
        '<tr>' +
        '<th>Username</th>' + //headers for table columns
        '<th>Score</th>' +
        '</tr>';



    fetch('/leaderboard/list', {method: 'get'} //leaderboard API fetched to list database records
    ).then(response => response.json()
    ).then(positions => {
        if(positions.hasOwnProperty('error')){ //if any error is found, alerted
            alert(positions.error);
        }else{
            for (let position of positions){ //loops through each record/position
                leaderboardHTML += `<tr>` +
                    `<td>${position.Username}</td>` + //Inputs the field entry in the record from the table into this position (i.e. username)
                    `<td>${position.Score}</td>` + //Inputs the field entry in the record from the table into this position (I.e. the score)
                    `</tr>`;

            }

        }
        leaderboardHTML += '</table>';
        document.getElementById("content2").innerHTML = leaderboardHTML; //defines the HTML to be inputted into div
    });


}

