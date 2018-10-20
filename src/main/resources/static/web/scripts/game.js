
const printPlayersGrid = (data) => {
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
                this.addCellsIds()
                this.markShips()
        },
        methods:{
            addCellsIds(){
                const cellsArray = Array.from(document.querySelector('#playersGrid').querySelectorAll('td'))
                const idsArray = []

                this.columnArr.forEach((letter) => {
                    this.rowArr.forEach((number) => idsArray.push(`${ letter }${ number }`))
                })

                idsArray.forEach((id, index) => cellsArray[index].setAttribute('id', id))
            },
            markShips(){
                const shipsLocations = []
                this.dataIn.ships.forEach(ship => ship.location.forEach(loc => shipsLocations.push(loc)))
                console.log(shipsLocations)
                shipsLocations.forEach(loc => {
                    document.querySelector(`#${ loc }`).classList.add('shipLoc')
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
    const json = await response.json()
    console.log({json})
    basicInfo(json)
    printPlayersGrid(json)
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
