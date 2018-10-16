//onDOMContentLoaded = (function(){ console.log("DOM ready!") })()
//
//onload = (function(){ console.log("Page fully loaded!") })()
//
//onloadeddata = (function(){ console.log("Data loaded!") })()

const request = async () => {
    const response = await fetch('/api/games')
    const json = await response.json()
//    console.log(json)
    main(json)
}

onload = (() => request() )()

const main = (data) => {
    console.log({data})

    const list = document.querySelector('#list')

    list.innerHTML = data.map(game => {
        const { game_id, created, gamePlayers } = game

        const player01 = (gamePlayers[0]) ? gamePlayers[0].player.email : ' -- '
        const player02 = (gamePlayers[1]) ? gamePlayers[1].player.email : ' -- '            
        
        return `
                <li>
                    <p class="font-weight-bold">Game: ${game_id}, created ${created}</p>
                    <p class="ml-3">Player 1: ${player01}</p>
                    <p class="ml-3">Player 2: ${player02}</p>
                </li>
                `
    }).join('')

}