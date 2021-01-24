<template>
  <div id="app" class="gs-bg-orange-2">
    <!-- Navbar -->
    <nav class="container pr-md-0 pl-md-0 navbar navbar-expand-lg navbar-light bg-light gs-navbar-orange">
      <!-- Brand -->
      <router-link to="/" class="navbar-brand gs-navbar-brand">
        <img style="height:45px; vertical-align:baseline; transform: translateY(4px) scaleX(-1);"
             src="./assets/GoShareFox.png" alt="Logo"> &nbsp;&nbsp;
        <span class="gs-navbar-brand-text">
            <b style="font-family: 'GoShareFont'; letter-spacing: 2.5px;">GO</b><span
            class="font-weight-light">SHARE</span>
          </span>
      </router-link>
      <!-- Brand -->
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02"
              aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
        <ul class="navbar-nav gs-navbar-nav ml-auto mt-2 mt-lg-3">
          <!-- Links -->
          <li class="nav-item gs-nav-item">
            <router-link to="/" class="nav-link gs-nav-link">Discover <span class="sr-only">(current)</span>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/add" class="nav-link gs-nav-link">Share</router-link>
          </li>
          <li class="nav-item gs-nav-item">
            <router-link to="/items" class="nav-link gs-nav-link">Borrow</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/career" class="nav-link gs-nav-link">Help</router-link>
          </li>
          <!-- Links -->

          <!-- Account -->
          <div v-if="currentUser" class="navbar-nav navbar-expand-lg gs-navbar-nav ml-5">
            <li class="nav-item gs-nav-item dropdown">
              <a class="nav-link gs-nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                 data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <span class="fa fa-user-o"></span>
              </a>
              <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/myitems" class="nav-link gs-nav-link">My tools</router-link>
            </li>
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/borrowedlist" class="nav-link gs-nav-link">Borrowed</router-link>
            </li>
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/profile" class="nav-link gs-nav-link">Profile</router-link>
            </li>
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link :to="'/edit-profile/'+currentUser.id" class="nav-link gs-nav-link">Settings</router-link>
            </li>
            <div class="dropdown-divider"></div>
            <li class="dropdown-item nav-item gs-nav-item">
              <router-link to="/" class="nav-link gs-nav-link" v-on:click.native="logOut">Log Out</router-link>
            </li>
          </div>
          </li>
      </div>

      <div v-if="!currentUser" class="navbar-nav navbar-expand-lg gs-navbar-nav ml-5">
        <li class="nav-item gs-nav-item dropdown">
          <a class="nav-link gs-nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
             data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
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
  </div>
  <!-- Account -->
  </ul>
  </div>
  </nav>
  <!-- Navbar -->

  <!-- CONTENT -->
  <div class="container mt-3 pr-md-5 pl-md-5">
    <router-view/>
  </div>
  <!-- CONTENT -->

  <!-- Footer -->
  <div class="gs-footer-wrap pb-md-3">
    <nav>
      <div class="gs-footer">
        <a class="gs-footer-link">
          <router-link to="/aboutUs"> About us</router-link>
        </a>
        <a class="gs-footer-link ml-md-3">
          <router-link to="/career"> Career</router-link>
        </a>
        <a class="gs-footer-link ml-md-5" href="#"><i class="fa fa-1x fa-instagram"></i></a>
        <a class="gs-footer-link" href="#"><i class="fa fa-1.8x fa-twitter"></i></a>
        <a class="gs-footer-link" href="#"><i class="fa fa-1.8x fa-facebook"></i></a>
      </div>
    </nav>
  </div>
  <!-- Footer -->
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

a:hover {
  opacity: 0.8;
}

nav a {
  text-decoration-line: none;
  padding: 10px;
}

@import './assets/style.css';
</style>
