import { http } from "../http-common";

class UserDataService {
  editUser(id, data) {
    return http.put(`/users/edit-user/${id}`, data);
  }

  getUser(id) {
    return http.get(`/users/user/${id}`);
  }

  checkUserPass(id, pass) {
    return http.post(`/users/check-pass/${id}`, pass)
  }
  getPicture(img) {
    return http.get(`/users/get-img/${img}/`)
  }
}

export default new UserDataService();
