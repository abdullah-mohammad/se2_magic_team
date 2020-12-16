<template>
  <div>
    <div v-if="items.length > 0" class="list row">
      
      <!-- Page Content -->
      <div class="container">

        <!-- Page Heading -->
        <h2 class="my-4 gs-title">List of tools: </h2>

        <div v-for="(item) in items.slice(startLimit, endLimit)" :key="item.id">
          <!-- Item One -->
          <div class="row gs-tool-card">
            <div class="col-md-5">
              <!-- <a href="#"> -->
                <img onerror="this.onerror=null;this.src='http://placehold.it/700x300';" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" :src="getItemPicture(item.picture)" alt=""> <!-- http://placehold.it/700x300 -->
              <!-- </a> -->
            </div>
            <div class="col-md-7 gs-tool-card-infos img-fluid">
              <h3>{{item.title}}</h3>
              <VClamp class="p"
                  :max-height="255"
                >
                  {{item.description}}
              </VClamp>
              <div class="gs-tool-card-actions">
                <router-link :to="{ path: '/items/'+ item.id}" class="btn btn-sm btn-rounded btn-primary gs-btn-blue .gs-a">See details</router-link>
                <router-link :to="{ path: '#'}" class="btn btn-sm btn-outline-danger gs-btn-red .gs-a">Borrow</router-link>
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
            :prev-text="'&laquo;'"
            :next-text="'&raquo;'"
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

const MAX_NUMBER_ITEMS_PER_LIST = 5;
const API_IMG_RESOURCE = "http://localhost:8080/items/get-img/";

export default {
  name: "items-list",
  components: {
    Paginate,
    VClamp
  },
  data() {
    return {
      startLimit: 0,
      endLimit: 0,
    };
  },
  computed: {
    ...mapState('items', ['items']),
    getPageCount() { // total pages
      return this.items.length / MAX_NUMBER_ITEMS_PER_LIST;
    },
  },
  methods: {
    ...mapActions({
      setItems: "items/setItems"
    }),
    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(this.showPosition);
      } else { 
        document.getElementById("demo").innerHTML = "Geolocation is not supported by this browser.";
      }
    },
    showPosition(position){this.position=position},
    paginateCallback: function (pageNum){
      console.log(pageNum)
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST*(pageNum-1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    },
    getItemPicture(img) {
      return `${API_IMG_RESOURCE}${img}/`
    }
  },
  mounted() {
    // this is call of promise: so make sure that data has been fetched before pursuiving...
    this.setItems().then(() => {
      if(this.items.length > 0)
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
