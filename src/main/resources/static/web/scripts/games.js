
// const requestGames = async () => {
//     try {
//         const response = await fetch('/api/games')
//         if(!response.ok){
//             throw new Error(response.statusText);
//         }
//         const data = await response.json()
//         // printGameList(data)
//     } catch(err) {
//         console.log(err)
//     }
// }

// const requestLeaderboard = async () => {
//     try {
//         const response = await fetch('/api/leaderboard')
//         if(!response.ok){
//             throw new Error(response.statusText);
//         }
//         const data = await response.json()
//         // printLeaderboard(data)
//     } catch(err) {
//         console.log(err)
//     }
// }

// const handleRequests = () => {
//     Promise.all([requestGames, requestLeaderboard])
//     .then(values => {
//         console.log({values})
//     })

// }

const requests = async (urls) => {
    try {
        const data = await Promise.all(
            urls.map(
                url => fetch(url).then(
                            response => response.json()
                )
            )
        )
        // console.log({data})
        printLeaderboard(data[0])
        printGameList(data[1])
    } catch(err) {
        console.log(err)
    }
}

onload = (() => requests(['/api/leaderboard', '/api/games']) )()
// onload = (() => handleRequests() )()

const printGameList = (dataGL) => {
    console.log({dataGL})

    const list = document.querySelector('#list')

    list.innerHTML = dataGL.map(game => {
        const { game_id, created, gamePlayers } = game

        const time = new Date(created).toISOString()
        const catchDate = time.split('T')
        const catchTime = catchDate[1].split('.')
        const catchHour = catchTime[0].split(':')
        // console.log({catchDate,catchTime, catchHour})

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

const printLeaderboard = (dataLB) => {
    console.log({dataLB})
    let template = '';
    
    dataLB.forEach(player => {
        const { player_id, scores } = player
        if(scores.length > 0){
            const scoresSum = scores.reduce((acc, cur) => acc + cur)
            template += 
                `
                    <tr>
                        <td>${ player_id }</td>
                        <td>${ scores }</td>
                        <td>${ scoresSum }</td>
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