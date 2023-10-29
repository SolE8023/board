const commentWriterElem = document.querySelector("input[name=commentWriter]")
const commentPasswordElem = document.querySelector("input[name=commentPassword]")
const commentContentElem = document.querySelector("textarea[name=commentContent]")
const commentIdElem = document.querySelector("input[name=commentId]")
const commentTypeElem = document.querySelector("input[name=commentType]")
const commentPostId = document.querySelector("input[name=commentPostId]").value
const commentPopupId = document.querySelector("input[name=commentPopupId]")
const commentPopupPassword = document.querySelector("input[name=commentPopupPassword]")
const commentPopupType = document.querySelector("input[name=commentClickedType]")
async function addComment() {
    if(!validateComment()) return
    const writer = commentWriterElem.value
    const password = commentPasswordElem.value
    const content = commentContentElem.value

    const url = `/api/v1/comment`
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({writer, content, password, postId: commentPostId})
    }

    const response = await fetch(url, options)

    if (response.ok) {
        alert("댓글이 등록되었습니다.")
        location.reload()
    } else {
        alert("댓글 등록 실패.")
        return
    }
}

function validateComment() {
    if (commentPostId === "") {
        alert("게시글 id가 없습니다. 페이지를 새로고침 합니다.")
        location.reload()
    }
    if (commentWriterElem.value === "") {
        alert("댓글 작성자를 입력해주세요.");
        commentWriterElem.focus();
        return false;
    }
    if (commentPasswordElem.value === "") {
        alert("댓글 비밀번호를 입력해주세요.")
        commentPasswordElem.focus()
        return false
    }
    if (commentPasswordElem.value.length < 4) {
        alert("비밀번호는 4자리 이상이어야 합니다.")
        commentPasswordElem.focus()
        return false
    }
    if (commentContentElem.value === "") {
        alert("댓글 내용을 입력해주세요.");
        commentContentElem.focus();
        return false;
    }

    return true
}

function openCommentPasswordPopup() {
    document.querySelector(".comment-popup").classList.add("show")
}

function setCommentType(type) {
    document.querySelector("input[name=commentClickedType]").value = type
}

function setCommentId(commentId) {
    document.querySelector("input[name=commentPopupId]").value = commentId
}

function setButtonText(text) {
    document.querySelector("#comment-btn").innerText = text
}

function setButtonOnclick(methodName) {
    document.querySelector("#comment-btn").onclick = methodName
}

function init() {
    document.querySelector("input[name=commentPopupPassword]").value = ""
}

function modifyComment(commentId) {
    init()
    setCommentType("edit")
    setCommentId(commentId)
    openCommentPasswordPopup()
}

function deleteComment(commentId) {
    init()
    setCommentType("delete")
    setCommentId(commentId)
    openCommentPasswordPopup()
}

async function checkCommentPassword() {
    const commentId = commentPopupId.value
    const password = commentPopupPassword.value
    const type = commentPopupType.value

    if (commentId === "") {
        alert("commentId가 비었습니다. 새로고침 합니다.")
        location.reload()
        return
    }

    if (password === "") {
        alert("패스워드를 입력해주세요.")
        commentPopupPassword.focus()
        return
    }

    if (password.length < 4) {
        alert("패스워드는 4자리 이상이어야 합니다.")
        commentPopupPassword.focus()
        return
    }

    const url = `/api/v1/comment/comment-check-password`;
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: commentId, password: password, type: type})
    }

    const response = await fetch(url, options)
    if (response.ok) {
        if (type === "edit") {
            setEditForm()
            setButtonText("댓글 수정하기")
            setButtonOnclick(editCommentRequest)
            closeCommentPopup()
        }else if (type === "delete") {
            deleteCommentRequest()
        }
    } else {
        alert(await response.text())
        return false
    }
}

async function setEditForm() {
    const commentId = commentPopupId.value
    const url = `/api/v1/comment/get-edit-data?id=${commentId}`
    const options = {method: "GET"}
    const response = await fetch(url, options)
    if (response.ok) {
        const jsonData = await response.json()
        commentIdElem.value = jsonData.id
        commentTypeElem.value = "edit"
        commentWriterElem.value = jsonData.writer
        commentPasswordElem.value = jsonData.password
        commentContentElem.value = jsonData.content
    } else {
        alert("불러오기 실패. 페이지를 새로고침 합니다.")
        location.reload()
    }
}

async function editCommentRequest() {
    const id = commentIdElem.value
    const writer = commentWriterElem.value
    const password = commentPasswordElem.value
    const content = commentContentElem.value

    if (id === "") {
        alert("commentId가 비었습니다. 페이지를 새로고침 합니다.")
        location.reload()
        return
    }

    validateComment();

    const url = `/api/v1/comment`
    const options = {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id, writer, password, content})
    }

    const response = await fetch(url, options)
    if (response.ok) {
        alert("댓글이 수정되었습니다.");
        location.reload();
    } else {
        alert(await response.text())
    }
}

async function deleteCommentRequest() {
    const id = commentPopupId.value

    if (id === "") {
        alert("commentId가 비었습니다. 페이지를 새로고침 합니다.")
        location.reload()
        return
    }

    const url = `/api/v1/comment?id=${id}`
    const options = {method: 'DELETE'}

    const response = await fetch(url, options)
    if (response.ok) {
        alert("댓글이 삭제되었습니다.")
        location.reload()
    } else {
        alert("삭제 실패하였습니다. 페이지를 새로고침 합니다.")
        location.reload()
    }
}

function closeCommentPopup() {
    document.querySelector(".comment-popup").classList.remove("show")
}