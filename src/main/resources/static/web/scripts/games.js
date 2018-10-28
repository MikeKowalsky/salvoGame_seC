import { handleDate } from './handleTime.js'

const requests = async (urls) => {
    try {
        const data = await Promise.all(
            urls.map(
                url => fetch(url).then(
                            response => response.json()
                )
            )
        )
        activateListeners()
        printLeaderboard(data[0])
        printGameList(data[1])
    } catch(err) {
        console.log(err)
    }
}

onload = (() => requests(['/api/leaderboard', '/api/games']) )()

const printGameList = (dataGL) => {
    console.log({dataGL})

    const list = document.querySelector('#list')

    list.innerHTML = dataGL.map(game => {
        const { game_id, created, gamePlayers } = game
        const { date, hour, minute } = handleDate(created)

        const player01 = (gamePlayers[0]) ? gamePlayers[0].player.name : ' -- '
        const player02 = (gamePlayers[1]) ? gamePlayers[1].player.name : ' -- '            
        
        return `
                <li>
                    <p class="my-3 font-weight-bold my-text">
                        Game: ${ game_id }, created ${date} at ${hour}:${minute}
                    </p>
                    <p class="ml-3 mb-0">Player 1: ${ player01 }</p>
                    <p class="ml-3 mb-0">Player 2: ${ player02 }</p>
                </li>
                `
    }).join('')
}

const workWithScoresArray= (scoresArr) => {
    const playersScoreObj = {
        '0': 0,
        '0.5': 0,
        '1': 0}

    scoresArr.forEach(scoreVal => playersScoreObj[scoreVal]++)
    return playersScoreObj
}

const printLeaderboard = (dataLB) => {
    console.log({dataLB})
    let template = '';
    
    dataLB.forEach(player => {
        const { player_id, player_name, scores } = player
        const scoresObj = workWithScoresArray(scores)
        if(scores.length > 0){
            const scoresSum = scores.reduce((acc, cur) => acc + cur)
            template += 
                `
                    <tr>
                        <td>${ player_id }</td>
                        <td>${ player_name }</td>
                        <td class="text-center">${ scoresObj['1'] } - ${ scoresObj['0.5'] } - ${ scoresObj['0'] } </td>
                        <td class="text-center">${ scoresSum }</td>
                    </tr>
                `
        }
    })
    
    document.querySelector('#lboard').innerHTML = template
}

const activateListeners = () => {
    document.querySelector('#runGameList').addEventListener('click', handleRunGameListClick)
    document.querySelector('#runLeaderboard').addEventListener('click', handleRunLeaderboard)
    document.querySelector('#login').addEventListener('click', login)
}

const handleRunGameListClick = () => {
    document.querySelector('#gameList').style.display = 'block'
    document.querySelector('#leaderboard').style.display = 'none'
}

const handleRunLeaderboard = () => {
    document.querySelector('#gameList').style.display = 'none'
    document.querySelector('#leaderboard').style.display = 'block'
}

function login(){
    $.post("/api/login",
        { name: "j.bauer@ctu.gov",
        pwd: "24"})
    .done(function() {
        console.log("logged in!");
    })
    .fail(function(resp){
        console.log(resp);
        alert(`Something went wrong! Error code: ${ resp.status }, text: ${ resp.responseJSON.error }`);
    });

    // console.log('in login')

    // fetch('/api/login', {
    //     method: 'POST',
    //     headers: {
    //         "Content-Type": "application/json; charset=utf-8",
    //     },
    //     body: {
    //         // userName: "fokinSpring", 
    //         name: "j.bauer@ctu.gov",
    //         pwd: "24",
    //     }
    // })
    // .then(res => {
    //     console.log(res)
    //     return res.json()
    // })
    // // .then(response => console.log('Success:', JSON.stringify(response)))
    // .then(response => console.log(response))
    // .catch(error => console.log('Error:', error))

    // const response = await fetch('/api/login', {
    //     method: 'post',
    //     body: JSON.stringify(player)
    // })
    // const data = await response.json()
    // console.log({data})
}
