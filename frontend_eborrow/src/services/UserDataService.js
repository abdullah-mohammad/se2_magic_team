import { http } from "../http-common";

class UserDataService {
  editUser(id, user) {
    return http.put(`/users/edit-user/${id}`, user);
  }

  getUser(id) {
    return http.get(`/users/user/${id}`);
  }
}

export default new UserDataService();
