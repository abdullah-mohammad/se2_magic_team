<template>
  <div>
    <div v-if="(currentUser && nearMeItems.length > 0) || (!currentUser && items.length > 0)" class="list row">
      <div v-if="!isLoaded" style="position: relative; width: 100%;">
        <div style="position: absolute;
                  top: 0;
                  bottom: 0;
                  left: 0;
                  text-align: center;
                  right: 0;
                  margin: auto;">
              <radar-spinner
                style="margin:auto"
                :animation-duration="2000"
                :size="60"
                :color="'#ff1d5e'"
              />
            Loading filter from your address...
        </div>
      </div>
      <!-- Page Content -->
      <div v-else class="container">

        <div v-if="locationApiError" class="alert alert-danger alert-dismissible fade show" role="alert">
          <strong>Location-API Error!</strong> <br> An API-Error occured while trying to compute distance between your address and items location
          <button type="button" class="close">
            <span @click="resetLocationApiError">&times;</span>
          </button>
        </div>
      <!-- Filter -->
      <FilterItem />
      <!-- Filter -->

        <!-- Page Heading -->
        <h2 class="my-4 gs-title">List of tools: </h2>

        <div v-for="(item) in (currentUser? nearMeItems.slice(startLimit, endLimit) : items.slice(startLimit, endLimit))" :key="item.id">
          <!-- Item One -->
          <div class="row gs-tool-card">
            <div class="col-md-5">
              <!-- <a href="#"> -->
              <img :src="getItemPicture(item.picture)" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt="">
              <!-- http://placehold.it/700x300 -->
              <!-- </a> -->
            </div>
            <div class="col-md-7 gs-tool-card-infos img-fluid">
              <h3>{{ item.title }}</h3>
              <VClamp class="p"
                      :max-height="255"
              >
                {{ item.description }}
              </VClamp>
              <div class="gs-tool-card-actions">
                <span v-if="currentUser && item.distanceFromMe != Infinity" class="text-muted">{{item.distanceFromMe}} km from you </span> &nbsp;
                <router-link :to="{ path: '/items/'+ item.id}"
                             class="gs-btn-blue .gs-a btn btn-sm btn-primary pt-1 pb-1 pl-3 pr-3"
                             style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none;"
                             >See details
                </router-link>
                <router-link  
                  v-if="!currentUser || (currentUser && ( (!isNaN(item.user) && item.user !=currentUser.id) || (isNaN(item.user) && item.user.id!=currentUser.id)))" 
                  :to="{ path: '/borrow/' + item.id}" 
                    class="gs-btn-red .gs-a btn btn-sm btn-danger pt-1 pb-1 pl-3 pr-3" 
                    style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none;"
                  >Borrow
                </router-link>
              </div>
            </div>
          </div>
          <!-- /.row -->
        </div>

        <!-- Pagination -->
        <paginate
            page:1
            :page-count=getPageCount
            :container-class="'pagination justify-content-center'"
            :page-class="'page-item gs-page-item'"
            :prev-class="'page-item gs-page-item'"
            :next-class="'page-item gs-page-item'"
            :prev-link-class="'page-link gs-page-link'"
            :next-link-class="'page-link gs-page-link'"
            :page-link-class="'page-link gs-page-link'"
            :prev-text="'prev'"
            :next-text="'last'"
            :click-handler="paginateCallback"
        >
        </paginate>

      </div>
      <!-- /.container -->

    </div>

    <div v-else>
      <p :style="{color:'red'}">Sorry, there is no data 😢</p>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';
import Paginate from 'vuejs-paginate'
import VClamp from 'vue-clamp'
import FilterItem from './filter/FilterItem'
import {RadarSpinner} from 'epic-spinners'

const MAX_NUMBER_ITEMS_PER_LIST = 5;
const API_IMG_RESOURCE = process.env.VUE_APP_API_URL+"items/get-img/";

export default {
  name: "items-list",
  components: {
    Paginate,
    VClamp,
    FilterItem,
    RadarSpinner
  },
  data() {
    return {
      startLimit: 0,
      endLimit: 0,
    };
  },
  computed: {
    ...mapState('items', ['items', 'nearMeItems', 'isLoaded', 'locationApiError']),
    getPageCount() { // total pages
      return this.currentUser ? this.nearMeItems.length / MAX_NUMBER_ITEMS_PER_LIST : this.items.length / MAX_NUMBER_ITEMS_PER_LIST;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    ...mapState('user', {
      user: state => state.user
    }),
  },
  methods: {
    ...mapActions({
      setItems: "items/setItems",
      resetLocationApiError: "items/resetLocationApiError"
    }),
    paginateCallback: function (pageNum) {
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST * (pageNum - 1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    },
    getItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      }else{
        return "http://placehold.it/700x300";
      }
    }
  },
  mounted() {
    // this is call of promise: so make sure that data has been fetched before pursuiving...
    this.setItems().then(() => {
      if ((this.currentUser && this.nearMeItems.length > 0) || (!this.currentUser && this.items.length > 0))
        this.paginateCallback(1);
    })
  },

};
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>
