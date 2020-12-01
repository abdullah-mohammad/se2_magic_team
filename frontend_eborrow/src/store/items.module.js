import ItemDataService from "../services/ItemDataService";

export const items = {
    namespaced: true,
    state: {
        items: [],
    },
    mutations: {
        SET_ITEMS(state, payload) {
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
    },
};
