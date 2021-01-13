import ItemDataService from "../services/ItemDataService";

export const myitems = {
    namespaced: true,
    state: {
        myitems: [],
        currentItem: null
    },
    mutations: {
        SET_MY_ITEMS(state, payload) {
            state.myitems = payload;
        },
        SET_CURRENT_ITEM(state, payload) {
            state.currentItem = payload;
        }
    },
    actions: {
        async setMyItems(context,id){
            try {
                const res = await ItemDataService.getMyGegenstaende(id);
                context.commit('SET_MY_ITEMS', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        },
        async setCurrentItem(context,id){
            try {
                const res = await ItemDataService.get(id);
                context.commit('SET_CURRENT_ITEM', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        }

    },
};
