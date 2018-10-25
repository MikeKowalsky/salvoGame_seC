
const printGrids = (data) => {
    const columnArray = ['', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']
    const rowArray = ['', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

    let vuePlayersGrid = new Vue({
        el: '#vueGrid',
        data:{
            columnArr: columnArray,
            rowArr: rowArray,
            dataIn: data
        },
        mounted(){
                this.addCellsIds('player')
                this.addCellsIds('opponent')
                this.markShips()
                this.markSalvoes()
        },
        methods:{
            addCellsIds(playerType){
                const idsArray = []
                let cellsArray;
                
                playerType == 'player'
                    ? cellsArray = Array.from(document.querySelector('#playersGrid').querySelectorAll('td'))
                    : cellsArray = Array.from(document.querySelector('#opponentsGrid').querySelectorAll('td'))

                this.columnArr.forEach((letter) => {
                    playerType == 'player' 
                        ? this.rowArr.forEach((number) => idsArray.push(`${ letter }${ number }`))
                        : this.rowArr.forEach((number) => idsArray.push(`s${ letter }${ number }`))
                })

                idsArray.forEach((id, index) => cellsArray[index].setAttribute('id', id))
            },
            markShips(){
                const shipsLocations = []
                this.dataIn.ships.forEach(ship => ship.locations.forEach(loc => shipsLocations.push(loc)))
                shipsLocations.forEach(loc => {
                    document.querySelector(`#${ loc }`).classList.add('shipLoc')
                })
            },
            markSalvoes(){
                this.dataIn.salvoes.forEach(salvoSet => {
                    if(salvoSet.player_id == this.dataIn.player.player_id){
                        salvoSet.locations.forEach(loc => {
                            const td = document.querySelector(`#s${ loc }`)
                            td.classList.add('salvoLoc')
                            td.innerHTML = salvoSet.turn
                        })
                    } else {
                        salvoSet.locations.forEach(loc => {
                            const td = document.querySelector(`#${ loc }`)
                            td.innerHTML = salvoSet.turn
                            td.classList.contains('shipLoc')
                                ? td.classList.add('hitLoc')
                                : td.classList.add('salvoLoc')
                        })
                    }
                })
            }
        }
    })
}

const basicInfo = (data) => {
    let vueInfo = new Vue({
        el: '#vueInfo',
        data: {
            dataIn: data
        },
        computed:{
            creationDate(){
                const catchDate = this.dataIn.created.split('T')
                const catchTime = catchDate[1].split('.')
                const catchHour = catchTime[0].split(':')
                // console.log({catchDate,catchTime, catchHour})
                return `${catchDate[0]} at ${catchHour[0]}:${catchHour[1]}`
            }
        }        
    })
}

const request = async (url) => {
    // const response = await fetch('/api/game_view/13')
    const response = await fetch(url)
    const data = await response.json()
    console.log({data})
    basicInfo(data)
    printGrids(data)
}

const getApiURL = () => {
    const urlSearchPart = window.location.search
    const tempArray = urlSearchPart.split('=')
    const myVar = tempArray[1]
    return `/api/game_view/${myVar}`
}

const main = () => {
    const url = getApiURL()
    request(url)
}

onload = (() => main() )()
