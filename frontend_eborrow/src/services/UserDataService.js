import { http } from "../http-common";

class UserDataService {
  editUser(id, data) {
    return http.put(`/users/edit-user/${id}`, data);
  }

  editUserAddress(id, addrId) {
    return http.put(`/users/edit-user-address/${id}`, addrId);
  }

  getUser(id) {
    return http.get(`/users/user/${id}`);
  }

  deleteUser(id) {
    return http.delete(`/users/user/${id}`);
  }

  checkUserPass(id, pass) {
    return http.post(`/users/check-pass/${id}`, pass)
  }

  getUserInlineAddress(id) {
    return http.get(`/users/user-address/${id}`);
  }

}

export default new UserDataService();
