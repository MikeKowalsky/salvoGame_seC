
const requests = async (urls) => {
    try {
        const data = await Promise.all(
            urls.map(
                url => fetch(url).then(
                            response => response.json()
                )
            )
        )
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

        const time = new Date(created).toISOString()
        const catchDate = time.split('T')
        const catchTime = catchDate[1].split('.')
        const catchHour = catchTime[0].split(':')

        const player01 = (gamePlayers[0]) ? gamePlayers[0].player.name : ' -- '
        const player02 = (gamePlayers[1]) ? gamePlayers[1].player.name : ' -- '            
        
        return `
                <li>
                    <p class="my-3 font-weight-bold">
                        Game: ${ game_id }, created ${catchDate[0]} 
                        at ${catchHour[0]}:${catchHour[1]}
                    </p>
                    <p class="ml-3 mb-0">Player 1: ${ player01 }</p>
                    <p class="ml-3 mb-0">Player 2: ${ player02 }</p>
                </li>
                `
    }).join('')
}

const workWithScoresArray= (scoresArr) => {
    playersScoreObj = {
        '0': 0,
        '0.5': 0,
        '1': 0
    }

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

const handleClick = (buttonType) => {
    if(buttonType == 'gameList'){
        document.querySelector('#gameList').style.display = 'block'
        document.querySelector('#leaderboard').style.display = 'none'
    } else {
        document.querySelector('#gameList').style.display = 'none'
        document.querySelector('#leaderboard').style.display = 'block'
    }
}
