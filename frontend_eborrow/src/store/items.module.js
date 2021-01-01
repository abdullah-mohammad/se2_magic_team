import ItemDataService from "../services/ItemDataService";

export const items = {
    namespaced: true,
    state: {
        items: []
    },
    mutations: {
        SET_ITEMS(state, payload) {
            state.items = payload;
        },
        SET_MY_ITEMS(state, payload) {
            state.items = payload;
        },
        GET_ITEM(state, payload) {
            state.items = payload;
        }
    },
    actions: {
        async setItems(context){
            try {
                const res = await ItemDataService.getAll();
                context.commit('SET_ITEMS', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        },
        async setMyItems(id,context){
            try {
                const res = await ItemDataService.getMyGegenstaende(id);
                context.commit('SET_MY_ITEMS', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        },

        async getItem(id,context){
            try {
                const res = await ItemDataService.get(id);
                context.commit('GET_ITEM', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        }

    },
};
