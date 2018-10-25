
export function handleDate(dataInMilli) {

    const time = new Date(dataInMilli).toISOString()
    const catchDate = time.split('T')
    const catchTime = catchDate[1].split('.')
    const catchHour = catchTime[0].split(':')
    return {
        'date': catchDate[0],
        'hour': catchHour[0],
        'minute': catchHour[1]
    }
}

// Game: ${ game_id }, created ${catchDate[0]} 
// at ${catchHour[0]}:${catchHour[1]}