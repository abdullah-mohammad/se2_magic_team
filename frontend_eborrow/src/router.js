import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("./components/Home"),
    },
    {
      path: "/items",
      name: "items",
      component: () => import("./components/items/ItemsList"),
    },
    {
      path: "/items/:id",
      name: "item-details",
      component: () => import("./components/items/Item"),
    },
    {
      path: "/add",
      name: "add",
      component: () => import("./components/items/AddItem"),
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("./components/user/Profile"),
    },
    {
      path: "/edit-profile/:id",
      name: "edit-profile",
      component: () => import("./components/user/EditProfile"),
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
    {
      path: "/aboutUs",
      name: "aboutUs",
      component: () => import("./components/AboutUs"),
    },
    {
      path: "/career",
      name: "career",
      component: () => import("./components/Career"),
    },
    {
      path: "/myitems",
      name: "myitems",
      component: () => import("./components/items/MyItems"),
    },
    {
      path: "/editItem/:id",
      name: "editItem",
      component: () => import("./components/items/EditItem"),
    },

    {
      path: "/borrow/:id",
      name: "borrow",
      component: () => import("./components/ItemBorrow"),
    },
  ],
});

// In bezkoder theres a way described to handle unauthorized requests, I don't like it though, must work in a better way.
// there must be a better way to handle unauthorized requests, like catching 403 errors and then forwarding.
