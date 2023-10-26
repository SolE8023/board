const popupCode = document.querySelector("input[name=popupCode]").value
let popupId = document.querySelector("input[name=popupId]").value

function openPopup() {
    document.getElementById('popup').classList.add('show');
}

function editPost() {
    clearPopupForm()
    setType("edit")
    openPopup()
}

function deletePost() {
    clearPopupForm()
    setType("delete")
    openPopup()
}

function viewPost(postId) {
    clearPopupForm()
    setType("view")
    setId(`${postId}`)
    openPopup()
}

function setId(postId) {
    document.querySelector("input[name=popupId]").value = postId
}

function getId() {
    return document.querySelector("input[name=popupId]").value
}

function setType(type) {
    document.querySelector("input[name=clickedType]").value = `${type}`
}

function clearPopupForm() {
    document.querySelector("input[name=popupPassword]").value = ""
    document.querySelector("input[name=clickedType]").value = ""
}
function closePopup() {
    document.getElementById('popup').classList.remove('show');
}

function request() {
    const type = document.querySelector("input[name=clickedType]").value
    const pwdElem = document.querySelector("input[name=popupPassword]")

    if (pwdElem.value === "") {
        alert("비밀번호를 입력해주세요")
        pwdElem.focus()
        return
    }

    if (type === "edit") {
        editRequest(pwdElem.value, type)
    } else if (type === "delete") {
        deleteRequest(pwdElem.value, type)
    } else if (type === "view") {
        viewRequest(pwdElem.value, type)
    }

    closePopup();
}

async function editRequest(password, type) {
    if (await checkPassword(password, type)) location.href = `/post/${popupCode}/edit/${popupId}`
}

async function deleteRequest(password, type) {
    const result = await checkPassword(password, type)
    if(!result) return

    if (!confirm("삭제하시겠습니까? 삭제한 게시글은 복구가 불가능합니다.")) return

    const url = `/post/${code}/delete/${postId}`

    let form = document.createElement('form')
    form.method = 'POST'
    form.action = url

    let hiddenIdElem = document.createElement('input');
    hiddenIdElem.type = 'hidden'
    hiddenIdElem.name = 'id'
    hiddenIdElem.value = `${postId}`

    form.appendChild(hiddenIdElem)
    document.body.appendChild(form)
    form.submit()
}

async function viewRequest(password, type) {
    popupId = getId()
    if (await checkPassword(password, type)) location.href = `/post/${popupCode}/view/${popupId}`
}

async function checkPassword(password, type) {
    const url = `/post/chkPwd`
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // 데이터를 JSON 형식으로 보내기 위해 Content-Type 설정
        },
        body: JSON.stringify({id: popupId, password: password, type: type})
    }

    const response = await fetch(url, options)

    if (response.status === 400) {
        alert(await response.text())
        return false
    } else if (response.ok) {
        return true
    }
}