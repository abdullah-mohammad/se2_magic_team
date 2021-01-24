import BorrowDataService from "../services/BorrowDataService";

export const borrowedlist = {
    namespaced: true,
    state: {
        borrowedlist: [],
    },
    mutations: {
        SET_BORROWED_LIST(state, payload) {
            state.borrowedlist = payload;
        }
    },
    actions: {
        async setBorrowedList(context,id){
            try {
                const res = await BorrowDataService.getBorrowedList(id);
                context.commit('SET_BORROWED_LIST', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        }
    },
};
