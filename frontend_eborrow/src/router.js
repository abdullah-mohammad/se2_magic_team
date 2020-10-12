import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      alias: "/items",
      name: "items",
      component: () => import("./components/ItemsList")
    },
    {
      path: "/items/:id",
      name: "item-details",
      component: () => import("./components/Item")
    },
    {
      path: "/add",
      name: "add",
      component: () => import("./components/AddItem")
    }
  ]
});
