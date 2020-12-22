<template>
  <div id="app" class="gs-bg-orange-2">
    <nav class="navbar navbar-expand navbar-dark bg-dark gs-navbar-orange">
      <div
          class="navbar-collapse collapse w-100 order-1 order-md-0 dual-collapse2"
      >
        <router-link to="/" class="navbar-brand gs-navbar-brand"><strong>GO</strong><span class="font-weight-light">SHARE</span></router-link>
        <div class="navbar-nav gs-navbar-nav mr-auto">
          <li class="nav-item gs-nav-item">
            <router-link to="/items" class="nav-link gs-nav-link">Items</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/add" class="nav-link gs-nav-link">Add</router-link>
          </li>
        </div>
      </div>

      <div v-if="!currentUser" class="navbar-nav navbar-expand-lg gs-navbar-nav ml-auto">
        <li class="nav-item gs-nav-item dropdown">
          <a class="nav-link gs-nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <span class="fa fa-user-o"></span>
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/register" class="nav-link gs-nav-link">Sign Up</router-link>
            </li>
            <div class="dropdown-divider"></div>
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/login" class="nav-link gs-nav-link">Login</router-link>
            </li>
          </div>
        </li>
        <!-- <li class="nav-item gs-nav-item">
          <router-link to="/register" class="nav-link gs-nav-link" style="width:5rem;">Sign Up</router-link>
        </li>
        <li class="nav-item gs-nav-item">
          <router-link to="/login" class="nav-link gs-nav-link">Login</router-link>
        </li> -->
      </div>

      <div v-if="currentUser" class="navbar-nav gs-navbar-nav ml-auto">
        <li class="nav-item gs-nav-item">
          <router-link v-on:click.native="logOut" to="/" class="nav-link gs-nav-link"
          >Log out
          </router-link>
        </li>
      </div>
    </nav>

    <div class="container mt-3">
      <router-view/>
    </div>

    <div class="footerWrap">
      <nav>
      <div class="footer gs-footer">
          <a class="gs-footer-link"> <router-link to="/aboutUs" > About us</router-link> </a>
          <a class="gs-footer-link"><router-link to="/career" > Career </router-link></a>
          <a class="gs-footer-link" href="#"><i class="fa fa-1x fa-instagram"></i></a>
          <a class="gs-footer-link" href="#"><i class="fa fa-1.8x fa-twitter"></i></a>
          <a class="gs-footer-link" href="#"><i class="fa fa-1.8x fa-facebook"></i></a>
      </div>
      </nav>
    </div>

  </div>
</template>

<script>

export default {
  name: "app",

  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    logOut() {
      this.$store.dispatch("auth/logout");
      this.$store.dispatch("user/deleteUser");
    },
  },
};

</script>

<style>
#app {
  padding-bottom: 50px;
}

.footerWrap {
  background-color:#ffffff ;
  width: 100%;
  position: fixed;
  bottom: 0px;
  padding: 10px;
  color: #C55353 ;
  z-index: 100;
}
a:hover {
  opacity: 0.8;
}

/* a:link, a:visited {
  color: #C55353;
} */

.footer {
  font-size: 1.1em;
  text-align: center;
}

nav a {
  text-decoration-line: none;
  padding: 10px;
}

  @import './assets/style.css';
</style>
