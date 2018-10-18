
const basicInfo = (data) => {
    console.log({data})
}

const request = async (url) => {
    // const response = await fetch('/api/game_view/13')
    const response = await fetch(url)
    const json = await response.json()
    // console.log(json)
    basicInfo(json)
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
