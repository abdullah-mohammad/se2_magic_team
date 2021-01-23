import { http } from "../http-common";

class BorrowDataService {
    getAll() {
        return http.get("/borrow");
    }

    get(userId) {
        return http.get(`/borrow/${userId}`);
    }

    getByItemId(itemId) {
        return http.get(`/borrows/${itemId}`);
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

    getBorrowedCount(data) {
        return http.post("/borrow/check", data);
    }
}

export default new BorrowDataService();
