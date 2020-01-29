function pageLoad() {
    let leaderboardHTML =
        '<table id="leaderboardTable">' +
        '<tr id="tableHeadings">' +
        '<th class="headings">Username</th>' +
        '<th class="headings">Score</th>' +
        '</tr>'


    fetch('/leaderboard/list', {method: 'get'}
    ).then(response => response.json()
    ).then(positions => {
        for (let position of positions) {
            leaderboardHTML +=
                <tr> +
                    <td>${position.Username}</td> +
                    <td>${position.Score}</td> +
                </tr>;
        }
        leaderboardHTML += '</table>';
        document.getElementById("content").innerHTML = leaderboardHTML;
    });


}