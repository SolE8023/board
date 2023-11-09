window.onload = drawCalendar()

function drawCalendar() {
    clearCalendar()
    setYearMonthText()
    appendYoilElem()
    appendDayElem()
}

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

function appendYoilElem() {
    const calendar = document.querySelector(`.calendar`)
    const yoilDiv = document.createElement(`div`)
    yoilDiv.classList.add(`yoil`)

    const span1 = document.createElement(`span`)
    const span2 = document.createElement(`span`)
    const span3 = document.createElement(`span`)
    const span4 = document.createElement(`span`)
    const span5 = document.createElement(`span`)
    const span6 = document.createElement(`span`)
    const span7 = document.createElement(`span`)

    span1.innerText = `일`
    span2.innerText = `월`
    span3.innerText = `화`
    span4.innerText = `수`
    span5.innerText = `목`
    span6.innerText = `금`
    span7.innerText = `토`

    yoilDiv.appendChild(span1)
    yoilDiv.appendChild(span2)
    yoilDiv.appendChild(span3)
    yoilDiv.appendChild(span4)
    yoilDiv.appendChild(span5)
    yoilDiv.appendChild(span6)
    yoilDiv.appendChild(span7)

    calendar.appendChild(yoilDiv)
}

function appendDayElem() {
    const queryString = window.location.search
    const params = new URLSearchParams(queryString)
    const today = new Date()
    let year = params.get("year") ?? today.getFullYear()
    let month = params.get("month") ?? today.getMonth() + 1

    const weeks = getWeeks(year, month);
    const firstDay = new Date(year, month-1, 1)
    const firstYoil = firstDay.getDay()
    const lastDay = new Date(year, month, 0)

    let day = 0
    let calendar = document.querySelector(`.calendar`)
    for (let i = 1; i <= weeks; i++) {
        let weekDiv = document.createElement('div')
        weekDiv.classList.add(`day`)
        for (let j = 0; j <= 6; j++) {
            let dayWrapSpan = document.createElement('span')
            dayWrapSpan.classList.add(`day-wrap`)
            let daySpan = document.createElement(`span`)
            daySpan.classList.add(`day-span`)
            if ((j === firstYoil || day > 0) && day < lastDay.getDate()) {
                day++;
                daySpan.innerText = day;
                dayWrapSpan.appendChild(daySpan);
                weekDiv.appendChild(dayWrapSpan);
            } else {
                weekDiv.appendChild(dayWrapSpan);
            }
        }
        calendar.appendChild(weekDiv)
        if(day >= lastDay.getDate()) break;
    }
}

function clearCalendar() {
    document.querySelector(`.calendar`).innerHTML = ``
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

function setYearMonthText() {
    const queryString = window.location.search
    const params = new URLSearchParams(queryString)
    const today = new Date()
    let year = params.get("year") ?? today.getFullYear()
    let month = params.get("month") ?? today.getMonth() + 1
    if(month < 10) month = `0` + month
    document.querySelector(`.year-month`).innerText = year + `-` + month
}