import { http } from "../http-common";

class ItemDataService {
  getAll() {
    return http.get("/items");
  }


  get(id) {
    return http.get(`/items/${id}`);
  }

  create(data) {
    return http.post("/items", data);
  }

  update(id, data) {
    return http.put(`/items/editItem/${id}`, data);
  }

  delete(id) {
    return http.delete(`/items/${id}`);
  }

  deleteAll() {
    return http.delete(`/items`);
  }

  findByTitle(title) {
    return http.get(`/items?title=${title}`);
  }

  getPicture(img) {
    return http.get(`/items/get-img/${img}/`)
  }

  getMyGegenstaende(id){
    return http.get(`/items/user/${id}`)
  }
}

export default new ItemDataService();
