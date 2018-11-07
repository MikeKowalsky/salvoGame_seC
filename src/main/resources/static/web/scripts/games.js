import { handleDate } from './handleTime.js'
import { signIn, login, logout } from './loginFunctions.js'
import { createGame, joinGame } from './createJoinFunctions.js'

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
        activateListeners()
        activateListenersJoinButtons()

        if(data[1].loggedIn != null){
            document.querySelector('#loginInfo').innerHTML = `Welcome ${ data[1].loggedIn.name }!`
            showHide('logged', 'noLogged')
            showHide('loggedNewGame', 'noLogged')
        }
    } catch(err) {
        console.log(err)
    }
}

onload = (() => requests(['/api/leaderboard', '/api/games']) )()

const printGameList = (gamesRequest) => {
    const { loggedIn, gameList } = gamesRequest
    const list = document.querySelector('#list')

    list.innerHTML = gameList.map(game => {
        const { game_id, created, gamePlayers } = game
        const { date, hour, minute } = handleDate(created)
        let logInId, logInGpId, gameLink = null
        let joinButton = ''

        const player01 = gamePlayers[0].player.name
        const player02 = (isGameFull(game)) ? gamePlayers[1].player.name : ' -- '
        
        if(isLoggedIn(loggedIn)){
            logInId = loggedIn.player_id
            if(logInId == gamePlayers[0].player.player_id){
                logInGpId = gamePlayers[0].gp_id
            } else {
                (isGameFull(game) && logInId == gamePlayers[1].player.player_id)
                    ? logInGpId = gamePlayers[1].gp_id
                    : null
            }
        }
        
        if(logInGpId != null){
            gameLink = `<a href="/web/game.html?gp=${ logInGpId }">
                            <p class="my-3 font-weight-bold my-text">
                                Game: ${ game_id }, created ${date} at ${hour}:${minute}
                            </p>
                        </a>`
        } else {
            gameLink = `<p class="my-3 font-weight-bold my-text">
                            Game: ${ game_id }, created ${date} at ${hour}:${minute}
                        </p>`
        }

        if(showJoinButton(game, loggedIn, gamePlayers, logInId)){
            joinButton = `<button 
                            class="btn btn-danger btn-sm join-button"
                            id="game${ game_id }"
                            data-game_id="${ game_id }" >
                                Join game ${ game_id } with ${ gamePlayers[0].player.name }!
                            </button>`
        }

        return `
                <li class="my-4 p-3" id="gameId${ game_id }">
                    ${ gameLink }
                    ${ joinButton }
                    <p class="ml-3 mb-0">Player 1: ${ player01 }</p>
                    <p class="ml-3 mb-0">Player 2: ${ player02 }</p>
                </li>
                `
    }).join('')
}

const isLoggedIn = (loggedIn) => loggedIn == null ? false : true

const isGameFull = (game) => (game.gamePlayers.length == 2) ? true : false

const showJoinButton = (game, loggedIn, gamePlayers, logInId) => 
    isGameFull(game) || !isLoggedIn(loggedIn) || gamePlayers[0].player.player_id == logInId
        ? false 
        : true

const prepareScoreObj= (scoresArr) => {
    const playersScoreObj = {
        '0': 0,
        '0.5': 0,
        '1': 0}

    scoresArr.forEach(scoreVal => playersScoreObj[scoreVal]++)
    return playersScoreObj
}

const printLeaderboard = (dataLB) => {
    let template = '';
    
    dataLB.forEach(player => {
        const { player_id, player_name, scores } = player
        const scoresObj = prepareScoreObj(scores)
        if(scores.length > 0){
            const scoresSum = scores.reduce((acc, cur) => acc + cur)
            template += 
                `<tr>
                    <td>${ player_id }</td>
                    <td>${ player_name }</td>
                    <td class="text-center">${ scoresObj['1'] } - ${ scoresObj['0.5'] } - ${ scoresObj['0'] } </td>
                    <td class="text-center">${ scoresSum }</td>
                </tr>`
        }
    })
    document.querySelector('#lboard').innerHTML = template
}

const activateListeners = () => {
    document.querySelector('#runGameList').addEventListener('click', handleRunGameListClick)
    document.querySelector('#runLeaderboard').addEventListener('click', handleRunLeaderboard)
    document.querySelector('#login').addEventListener('click', login)
    document.querySelector('#logout').addEventListener('click', logout)
    document.querySelector('#createAccount').addEventListener('click', handleShowSignInForm)
    document.querySelector('#signIn').addEventListener('click', signIn)
    document.querySelector('#createGame').addEventListener('click', createGame)
}

const activateListenersJoinButtons = () => {
    Array.from(document.querySelectorAll('.join-button'))
         .forEach(btn => btn.addEventListener('click', joinGame))
}

const handleRunGameListClick = () => {
    document.querySelector('#gameList').style.display = 'block'
    document.querySelector('#leaderboard').style.display = 'none'
}

const handleRunLeaderboard = () => {
    document.querySelector('#gameList').style.display = 'none'
    document.querySelector('#leaderboard').style.display = 'block'
}

const handleShowSignInForm = () => {
    showHide('signInFormPart', 'loginFormPart')
}

const showHide = (showID, hideID) => {
    document.querySelector(`#${ showID }`).style.display = 'block'
    document.querySelector(`#${ hideID }`).style.display = 'none'
}
