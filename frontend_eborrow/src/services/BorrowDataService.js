import { http } from "../http-common";

class BorrowDataService {
    getAll() {
        return http.get("/borrow");
    }

    get(userId) {
        return http.get(`/borrow/${userId}`);
    }

    create(data) {
        return http.post("/borrow", data);
    }

    update(id, data) {
        return http.put(`/borrow/${id}`, data);
    }

    delete(id) {
        return http.delete(`/borrow/${id}`);
    }

    deleteAll() {
        return http.delete(`/borrow`);
    }
    getBorrowedList(id){
        return http.get(`/borrow/user/${id}`)
    }
}

export default new BorrowDataService();
