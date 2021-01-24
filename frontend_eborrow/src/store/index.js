import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';
import { items } from './items.module';
import { myitems } from './myitems.module';
import { borrowedlist } from './borrowedlist.module';


import { user } from './user.module';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
    items,
    myitems,
    borrowedlist,
    user
  }
});
