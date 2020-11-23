import User from "../models/User";
import userDataService from "../services/UserDataService";

export const user = {
  namespaced: true,
  state: {
      user: User,
      errMsg: ""
  },
  mutations: {
    SET_CURRENT_USER(state, editedUser) {
        console.log("KOMMISCH", editedUser)
      state.user = editedUser
    },
    SET_ERR_MSG(state, msg) {
        state.errMsg = msg
    }
  },
  actions: {
    setErrMsg(context, msg) {context.commit('SET_ERR_MSG', msg)},  
    editUser(context, editedUser) {
      userDataService.editUser(editedUser.id, editedUser)
        .then(res => {
            console.log(res.data)
            context.commit('SET_CURRENT_USER', editedUser)
            return Promise.resolve(res.data)
        })
        .catch(err => {
            //context.setErrMsg(err)
            console.log("eroor", err)
            return Promise.reject(err)
        })
    },
    setCurrentUser(context, id){
        userDataService.getUser(id)
            .then(res => {
                context.commit('SET_CURRENT_USER', res.data)
                return Promise.resolve(res.data)
            })
            .catch(err => {
                context.setErrMsg(err)
                return Promise.reject(err)
            })
    }
  },
};
