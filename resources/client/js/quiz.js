function pageLoad() {
    let quizHTML =
        '<table align="center">' +
        '<tr>' +
        '<th>Maths</th>' +
        '<th>Physics</th>' +
        '<th>Computer Science</th>' +
        '</tr>';



    fetch('/maths/list', {method: 'get'}
    ).then(response => response.json()
    ).then(positions => {
        if(positions.hasOwnProperty('error')){
            alert(positions.error);
        }else{
            for (let position of positions){
                quizHTML += `<tr>` +
                    `<td>${position.Subtopic}</td>` +
                    `</tr>`;

            }

        }
        quizHTML += '</table>';
        document.getElementById("content2").innerHTML = quizHTML;
    });

    fetch('/physics/list', {method: 'get'}
    ).then(response => response.json()
    ).then(positions => {
        if(positions.hasOwnProperty('error')){
            alert(positions.error);
        }else{
            for (let position of positions){
                quizHTML += `<tr>` +
                    `<td>${position.Subtopic}</td>` +
                    `</tr>`;

            }

        }
        quizHTML += '</table>';
        document.getElementById("content2").innerHTML = quizHTML;
    });

    fetch('/cs/list', {method: 'get'}
    ).then(response => response.json()
    ).then(positions => {
        if(positions.hasOwnProperty('error')){
            alert(positions.error);
        }else{
            for (let position of positions){
                quizHTML += `<tr>` +
                    `<td>${position.Subtopic}</td>` +
                    `</tr>`;

            }

        }
        quizHTML += '</table>';
        document.getElementById("content2").innerHTML = quizHTML;
    });




}
