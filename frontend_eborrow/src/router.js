import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("./components/HelloWorld"),
    },
    {
      path: "/items",
      name: "items",
      component: () => import("./components/ItemsList"),
    },
    {
      path: "/items/:id",
      name: "item-details",
      component: () => import("./components/Item"),
    },
    {
      path: "/add",
      name: "add",
      component: () => import("./components/AddItem"),
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("./components/user/Profile"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("./components/user/Login"),
    },
    {
      path: "/register",
      name: "register",
      component: () => import("./components/user/Register"),
    },
  ],
});

// In bezkoder theres a way described to handle unauthorized requests, I don't like it though, must work in a better way.
// there must be a better way to handle unauthorized requests, like catching 403 errors and then forwarding.
