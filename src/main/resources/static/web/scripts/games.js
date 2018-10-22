
const request = async () => {
    try {
        const response = await fetch('/api/games')
        if(!response.ok){
            throw new Error(response.statusText);
        }
        const data = await response.json()
        main(data)
    } catch(err) {
        console.log(err)
    }
}

onload = (() => request() )()

const main = (data) => {
    console.log({data})

    const list = document.querySelector('#list')

    list.innerHTML = data.map(game => {
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

const handleClick = (buttonType) => {
    if(buttonType == 'gameList'){
        document.querySelector('#gameList').style.display = 'block'
        document.querySelector('#leaderboard').style.display = 'none'
    } else {
        document.querySelector('#gameList').style.display = 'none'
        document.querySelector('#leaderboard').style.display = 'block'
    }
}