export function handleResponse(response) {
    if(!response.ok) throw new Error(`에러 발생 status: ${response.status}`);
    return response.json()
}