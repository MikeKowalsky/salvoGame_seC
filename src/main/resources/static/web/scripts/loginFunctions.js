
export const signIn = async () => {
    const form = document.querySelector('#formSignIn')
    const response = await fetch('/api/players', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `name=${ form[0].value }&userName=${ form[1].value }&password=${ form[2].value }`,
    })
    // console.log(response)
    if(!response.ok){
        alert(`Error code: ${ response.status }`)
        throw new Error(response.status)
    }
    alert(`Hey ${ form[0].value }! Your account is created - userName: ${ form[1].value }.`)
    location.reload()
}

export const login = async () => {
    const form = document.querySelector('#formLogin')
    const response = await fetch('/api/login', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `userName=${ form[0].value }&password=${ form[1].value }`,
    })
    // console.log(response)
    if(!response.ok){
        alert(`Invalid username or password. Error code: ${ response.status }`)
        throw new Error(response.status)
    }
    location.reload()
}

export const logout = async () => {
    const response = await fetch('/api/logout', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: '',
    })
    if(!response.ok){
        alert(`Invalid username or password. Error code: ${ response.status }`)
        throw new Error(response.status)
    }
    location.reload()
}
