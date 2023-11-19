window.onload = drawCalendar()

function getWeeks(year, month) {
    let weeks = 0
    let day = 0
    let firstDay = new Date(year, month-1, 1)
    let lastDay = new Date(year, month, 0)
    let firstYoil = firstDay.getDay()
    for (let i = 0; i <= 42; i++) {
        if (i === firstYoil) weeks++;
        if(weeks === 0) continue
        day++
        if(day > lastDay.getDate()) break;
        let currentDay = new Date(year, month-1, day)
        if(currentDay.getDay() === 0) weeks++
    }
    return weeks
}
function prevMonth() {
    const queryString = window.location.search
    const params = new URLSearchParams(queryString)
    const today = new Date()
    let year = params.get("year") ?? today.getFullYear()
    let month = params.get("month") ?? today.getMonth() + 1
    let prev = new Date(year, month-2, 1)
    params.set(`year`, prev.getFullYear())
    params.set(`month`, prev.getMonth() + 1)
    location.href = getBaseUrl() + `?` + params
}

function nextMonth() {
    const queryString = window.location.search
    const params = new URLSearchParams(queryString)
    const today = new Date()
    let year = params.get("year") ?? today.getFullYear()
    let month = params.get("month") ?? today.getMonth() + 1
    let next = new Date(year, month, 1)
    params.set(`year`, next.getFullYear())
    params.set(`month`, next.getMonth() + 1)
    location.href = getBaseUrl() + `?` + params
}

function getBaseUrl() {
    const currentLocation = window.location
    return currentLocation.protocol + `//` + currentLocation.host + currentLocation.pathname
}